import java.util.ArrayList;

import lejos.geom.Point;
import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class Main {

	public static final int MAX_DISTANCE_TO_WALL = 25;
	public static final int TURN_ROTATE_VALUE = 285;
	
	private static Point pos = new Point(0, 0);
	
	// 0 baixo 1 cima 2 direita 3 esquerda
	private static Direcao actualDirection = null;
	private static Direcao newDirection = null;
	private static ArrayList<Direcao> directionQueue = new ArrayList<>();
	
	private static Point greenPos = null;
	private static boolean mapped = false;
	private static Posicao[][] matriz = {{null, null, null, null},
										{null, null, null, null},
										{null, null, null, null},
										{null, null, null, null}};
	
	private static ArrayList<Direcao> pathToGreen = new ArrayList<>();
	
	private static class AndarRastrearBloco implements Behavior {

		UltrasonicSensor ultrasonicSensor;
		ColorSensor colorSensor;
		TouchSensor touchSensor;
		
		public AndarRastrearBloco(UltrasonicSensor ultrasonicSensor, ColorSensor colorSensor, TouchSensor touchSensor) {
			this.ultrasonicSensor = ultrasonicSensor;
			this.colorSensor = colorSensor;
			this.touchSensor = touchSensor;
			Motor.A.setSpeed(400);
		}
		
		@Override
		public boolean takeControl() {
			return !mapped;
		}

		@Override
		public void action() {
			Motor.B.setSpeed(400);
			Motor.C.setSpeed(400);
			if (actualDirection != null) {
				Motor.B.rotate(820, true);
				Motor.C.rotate(820);
				
				pos = actualDirection.getUpdatedPoint(pos);
			} else {
				actualDirection = new Baixo(ultrasonicSensor);
			}
			if (greenPos == null && colorSensor.getColorID() == 7) {
				greenPos = pos;
			}
			printMapState();
			
			if (!hasNodesToSearch()) {
				LCD.drawString("Achou final! Pressione o botão", 0, 0);
				pathToGreen = getPathToGreen();
				while (!touchSensor.isPressed()) {
					
				}
				LCD.drawString("Faça o caminho willie!", 0, 0);
				mapped = true;
				return;
			}
			
			if (pos.y >= matriz.length || pos.x > matriz[(int) pos.y].length) {
				System.out.println("ArrayIndex");
				stepBack();
				return;
			} 
			
			Posicao posicaoAtual = matriz[(int) pos.y][(int) pos.x];
			if (posicaoAtual != null) {
			} else {
				Posicao newPos = new Posicao();
				matriz[(int) pos.y][(int) pos.x] = newPos;
				posicaoAtual = newPos;
			}
			
			ArrayList<Direcao> directions = actualDirection.checkBounds(posicaoAtual, pos);
			Direcao direction = null;
			
			for (int i = 0; i < directions.size(); i++) {
				Point directionPoint = directions.get(i).getUpdatedPoint(pos);
				if (matriz[(int) directionPoint.y][(int) directionPoint.x] == null) {
					direction = directions.get(i);
					break;
				}
			}
			
			if (direction != null) {
				directionQueue.add(direction);
				newDirection = direction;
			} else {
				System.out.println("Não tem posições posíveis, step-back");
				stepBack();
			}
		}
		
		/**
		 * Retorna uma lista de direções para chegar até o destino final (Marca verde)
		 * Transforma a matriz em um grafo, aplica o dijkstra, e mapeia as direções de acordo com o resultado do dijkstra
		 * @return lista de direções para chegar a marca verde
		 */
		private ArrayList<Direcao> getPathToGreen() {
			Graph graph = new Graph(matriz);
			int[] paths = Dijkstra.dijkstra(graph, 0);
			int greenPosArray = (int) ((greenPos.y * 4) + greenPos.x);
			
			ArrayList<Direcao> directions = new ArrayList<>();
			
			int currentPos = greenPosArray;
			while (currentPos != 0) {
				int newPos = paths[currentPos];
				int dif = currentPos - newPos;
				if (dif == 4) {
					directions.add(new Baixo(ultrasonicSensor));
				} else if (dif == -4) {
					directions.add(new Cima(ultrasonicSensor));
				} else if (dif == 1) {
					directions.add(new Direita(ultrasonicSensor));
				} else if (dif == -1) {
					directions.add(new Esquerda(ultrasonicSensor));
				}
				currentPos = newPos;
			}
			
			return directions;
		}

		@Override
		public void suppress() {
		}
		
	}
	
	private static boolean hasNodesToSearch() {
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[i].length; j++) {
				if (matriz[i][j] == null) 
					return true;
			}	
		}
		return false;
	}
	
	private static void stepBack() {
		Direcao lastDirection = directionQueue.get(directionQueue.size() - 1);
		directionQueue.remove(directionQueue.size() - 1);
		
		newDirection = lastDirection.reverse();
	}

	private static class Girar implements Behavior {

		@Override
		public boolean takeControl() {
			return newDirection != null;
		}

		@Override
		public void action() {
			Motor.B.setSpeed(200);
			Motor.C.setSpeed(200);
			GirarRoboInfo girarRoboInfo = newDirection.getTurnInfo(actualDirection);
			
			if (girarRoboInfo != null) {
				Motor.C.rotate(girarRoboInfo.motorCRotate, true);
				Motor.B.rotate(girarRoboInfo.motorBRotate);
			}
			
			actualDirection = newDirection;
			newDirection = null;
		}

		@Override
		public void suppress() {
			
		}
		
	}
	
	private static class DoPath implements Behavior {

		@Override
		public boolean takeControl() {
			return mapped && pathToGreen.size() > 0;
		}

		@Override
		public void action() {
			
		}

		@Override
		public void suppress() {
			
		}
		
	}
	
	public static void printMapState() {
		LCD.clear();
		LCD.drawString(formatMapLine(matriz[0], 0), 0, 1);
		LCD.drawString(formatMapLine(matriz[1], 1), 0, 2);
		LCD.drawString(formatMapLine(matriz[2], 2), 0, 3);
		LCD.drawString(formatMapLine(matriz[3], 3), 0, 4);
	}
	
	private static String formatMapLine(Posicao[] line, int y) {
		return getFreePathCount(line[0], 0, y) + "|" + getFreePathCount(line[1], 1, y) + "|" + getFreePathCount(line[2], 2, y) + "|" + getFreePathCount(line[3], 3, y);
	}
	
	private static String getFreePathCount(Posicao pos, int x, int y) {
		if (pos == null) {
			return "-";
		}
		if (Main.pos.x == x && Main.pos.y == y) {
			return "X";
		}
		
		if (greenPos != null && x == greenPos.x && y == greenPos.y) {
			return "D";
		}
		
		
		int count = 0;
		for (int i = 0; i < pos.freePaths.length; i++) {
			if (pos.freePaths[i]) {
				count++;
			}
		}
		return String.valueOf(count);
	}
	
	public static void main(String[] args) throws InterruptedException {
		Thread.sleep(2000);
		UltrasonicSensor ultrasonicSensor = new UltrasonicSensor(SensorPort.S3);
		ColorSensor colorSensor = new ColorSensor(SensorPort.S1);
		TouchSensor touchSensor = new TouchSensor(SensorPort.S4);
		Behavior[] behaviorList = { //
									new AndarRastrearBloco(ultrasonicSensor, colorSensor, touchSensor), //
									new DoPath(),
									new Girar(), //
								  };
		
		Arbitrator arbitrator = new Arbitrator(behaviorList);
		arbitrator.start();
	}
	
}
