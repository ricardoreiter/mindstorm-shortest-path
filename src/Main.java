import java.awt.Color;
import java.util.ArrayList;

import lejos.geom.Point;
import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class Main {

	public static final int MAX_DISTANCE_TO_WALL = 25;
	public static final int TURN_ROTATE_VALUE = 280;
	
	private static Point pos = new Point(0, 0);
	
	// 0 baixo 1 cima 2 direita 3 esquerda
	private static Direcao actualDirection = null;
	private static Direcao newDirection = null;
	private static ArrayList<Direcao> directionQueue = new ArrayList<>();
	private static boolean stepBack = false;
	
	private static Posicao[][] matriz = {{null, null, null, null},
										{null, null, null, null},
										{null, null, null, null},
										{null, null, null, null}};
	
	public static class Posicao {
		
		public static final int BAIXO = 0;
		public static final int CIMA = 1;
		public static final int DIREITA = 2;
		public static final int ESQUERDA = 3;
		
		boolean freePaths[] = new boolean[4];
		
	}
	
	private static class AndarRastrearBloco implements Behavior {

		UltrasonicSensor ultrasonicSensor;
		
		public AndarRastrearBloco(UltrasonicSensor ultrasonicSensor) {
			this.ultrasonicSensor = ultrasonicSensor;
			Motor.A.setSpeed(400);
			Motor.B.setSpeed(400);
			Motor.C.setSpeed(400);
		}
		
		@Override
		public boolean takeControl() {
			return true;
		}

		@Override
		public void action() {
			if (actualDirection != null) {
				Motor.B.rotate(800, true);
				Motor.C.rotate(800);
				
				pos = actualDirection.getUpdatedPoint(pos);
			} else {
				actualDirection = new Baixo(ultrasonicSensor);
			}
			printMapState();
			
			Posicao posicaoAtual = matriz[(int) pos.y][(int) pos.x];
			if (posicaoAtual != null) {
				System.out.println("Está em um nodo já visitado!");
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
				newDirection = directions.get(0);
				directionQueue.add(newDirection);
			} else {
				System.out.println("Não tem posições posíveis, step-back");
				stepBack = true;
			}
		}
		
		@Override
		public void suppress() {
			Motor.B.stop(true);
			Motor.C.stop();
		}
		
	}

	private static class StepBack implements Behavior {

		@Override
		public boolean takeControl() {
			return stepBack;
		}

		@Override
		public void action() {
			Direcao lastDirection = directionQueue.get(directionQueue.size() - 1);
			directionQueue.remove(directionQueue.size() - 1);
			
			newDirection = lastDirection.reverse();
			stepBack = false;
		}

		@Override
		public void suppress() {
			
		}
		
	}

	private static class ProcurarPosFinal implements Behavior {

		private ColorSensor colorSensor;
		
		public ProcurarPosFinal(ColorSensor colorSensor) {
			this.colorSensor = colorSensor;
		}

		@Override
		public boolean takeControl() {
			return colorSensor.getColorID() == 1;
		}

		@Override
		public void action() {
			System.out.println("Achou nó final, caminho mapeado");
		}

		@Override
		public void suppress() {
			
		}
		
	}
	
	private static class Girar implements Behavior {

		@Override
		public boolean takeControl() {
			return newDirection != null;
		}

		@Override
		public void action() {
			GirarRoboInfo girarRoboInfo = newDirection.getTurnInfo(actualDirection);
			
			if (girarRoboInfo != null) {
				Motor.B.rotate(girarRoboInfo.motorBRotate, true);
				Motor.C.rotate(girarRoboInfo.motorCRotate);
			}
			
			actualDirection = newDirection;
			newDirection = null;
		}

		@Override
		public void suppress() {
			
		}
		
	}
	
	public static void printMapState() {
		LCD.clear();
		LCD.drawString(formatMapLine(matriz[0]), 0, 0);
		LCD.drawString(formatMapLine(matriz[1]), 0, 1);
		LCD.drawString(formatMapLine(matriz[2]), 0, 2);
		LCD.drawString(formatMapLine(matriz[3]), 0, 3);
	}
	
	private static String formatMapLine(Posicao[] line) {
		return getFreePathCount(line[0]) + "|" + getFreePathCount(line[1]) + "|" + getFreePathCount(line[2]) + "|" + getFreePathCount(line[3]);
	}
	
	private static String getFreePathCount(Posicao pos) {
		if (pos == null) {
			return "-";
		}
		if (pos.equals(Main.pos)) {
			return "X";
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
		ColorSensor colorSensor = new ColorSensor(SensorPort.S2);
		Behavior[] behaviorList = {new AndarRastrearBloco(ultrasonicSensor), new Girar(), new StepBack(), new ProcurarPosFinal(colorSensor)};
		
		Arbitrator arbitrator = new Arbitrator(behaviorList);
		arbitrator.start();
	}
	
}
