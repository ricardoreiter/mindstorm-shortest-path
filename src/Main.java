import java.util.ArrayList;
import java.util.List;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class Main {

	private static final int MAX_DISTANCE_TO_WALL = 25;
	private static final int TURN_ROTATE_VALUE = 280;
	
	private static int x = 0;
	private static int y = 0;
	
	// 0 baixo 1 cima 2 direita 3 esquerda
	private static int actualDirection = -1;
	private static int newDirection = -1;
	
	private static Posicao[][] matriz = {{null, null, null, null},
										{null, null, null, null},
										{null, null, null, null},
										{null, null, null, null}};
	
	private static class Posicao {
		
		boolean cima;
		boolean baixo;
		boolean direita;
		boolean esquerda;
		
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
			if (actualDirection > -1) {
				Motor.B.rotate(800, true);
				Motor.C.rotate(800);
				
				incrementPosWithDirection();
			} else {
				actualDirection = 0;
			}
			
			if (matriz[y][x] != null) {
				System.out.println("Está em um nodo já visitado!");
			}
			Posicao newPos = new Posicao();
			matriz[y][x] = newPos;
			int reverseDirection = -1;
			
			boolean freePaths[] = new boolean[4];
			switch (actualDirection) {
				case 0:
					reverseDirection = 1;
					newPos.cima = true;
					newPos.baixo = isPathFree(0);
					newPos.direita = isPathFree(90);
					newPos.esquerda = isPathFree(-180);
					if (newPos.baixo) {
						freePaths[0] = true;
					}
					if (newPos.direita) {
						freePaths[2] = true;
					}
					if (newPos.esquerda) {
						freePaths[3] = true;
					}
					break;
				case 1:
					reverseDirection = 0;
					newPos.baixo = true;
					newPos.cima = isPathFree(0);
					newPos.esquerda = isPathFree(90);
					newPos.direita = isPathFree(-180);
					if (newPos.cima) {
						freePaths[1] = true;
					}
					if (newPos.direita) {
						freePaths[2] = true;
					}
					if (newPos.esquerda) {
						freePaths[3] = true;
					}
					break;
				case 2:
					reverseDirection = 3;
					newPos.esquerda = true;
					newPos.direita = isPathFree(0);
					newPos.cima = isPathFree(90);
					newPos.baixo = isPathFree(-180);
					if (newPos.baixo) {
						freePaths[0] = true;
					}
					if (newPos.cima) {
						freePaths[1] = true;
					}
					if (newPos.direita) {
						freePaths[2] = true;
					}
					break;
				case 3:
					reverseDirection = 2;
					newPos.direita = true;
					newPos.esquerda = isPathFree(0);
					newPos.baixo = isPathFree(90);
					newPos.cima = isPathFree(-180);
					if (newPos.baixo) {
						freePaths[0] = true;
					}
					if (newPos.cima) {
						freePaths[1] = true;
					}
					if (newPos.esquerda) {
						freePaths[3] = true;
					}
					break;
			}
			isPathFree(90);
			newDirection = -1;
			for (int i = 0; i < 4; i++) {
				if (freePaths[i]) {
					System.out.println("New Direction");
					newDirection = i;
					break;
				}
			}
			if (newDirection == -1) {
				newDirection = reverseDirection;
			}
		}
		
		private boolean isPathFree(int angle) {
			Motor.A.rotate(-angle);
			return ultrasonicSensor.getDistance() > MAX_DISTANCE_TO_WALL;
		}
		
		private void incrementPosWithDirection() {
			switch (actualDirection) {
				case 0:
					y++;
					break;
				case 1:
					y--;
					break;
				case 2:
					x++;
					break;
				case 3:
					x--;
					break;
			}
		}
		
		@Override
		public void suppress() {
			Motor.B.stop(true);
			Motor.C.stop();
		}
		
	}
	
	private static class Girar implements Behavior {

		@Override
		public boolean takeControl() {
			return newDirection > -1;
		}

		@Override
		public void action() {
			int motorBRotate = 0;
			int motorCRotate = 0;
			int rotateTimes = 1;
			
			System.out.println("New " + newDirection + " actual " + actualDirection);
			switch (newDirection) {
				case 0:
					switch (actualDirection) {
					case 1:
						rotateTimes = 2;
						motorBRotate = TURN_ROTATE_VALUE;
						motorCRotate = -TURN_ROTATE_VALUE;
						break;
					case 2:
						motorBRotate = TURN_ROTATE_VALUE;
						motorCRotate = -TURN_ROTATE_VALUE;
						break;
					case 3:
						motorBRotate = -TURN_ROTATE_VALUE;
						motorCRotate = TURN_ROTATE_VALUE;
						break;
					}
					break;
				case 1:
					switch (actualDirection) {
					case 0:
						rotateTimes = 2;
						motorBRotate = TURN_ROTATE_VALUE;
						motorCRotate = -TURN_ROTATE_VALUE;
						break;
					case 2:
						motorBRotate = -TURN_ROTATE_VALUE;
						motorCRotate = TURN_ROTATE_VALUE;
						break;
					case 3:
						motorBRotate = TURN_ROTATE_VALUE;
						motorCRotate = -TURN_ROTATE_VALUE;
						break;
					}
					break;
				case 2:
					switch (actualDirection) {
					case 1:
						motorBRotate = TURN_ROTATE_VALUE;
						motorCRotate = -TURN_ROTATE_VALUE;
						break;
					case 0:
						motorBRotate = -TURN_ROTATE_VALUE;
						motorCRotate = TURN_ROTATE_VALUE;
						break;
					case 3:
						rotateTimes = 2;
						motorBRotate = -TURN_ROTATE_VALUE;
						motorCRotate = TURN_ROTATE_VALUE;
						break;
					}
					break;
				case 3:
					switch (actualDirection) {
					case 1:
						motorBRotate = -TURN_ROTATE_VALUE;
						motorCRotate = TURN_ROTATE_VALUE;
						break;
					case 0:
						motorBRotate = TURN_ROTATE_VALUE;
						motorCRotate = -TURN_ROTATE_VALUE;
						break;
					case 2:
						rotateTimes = 2;
						motorBRotate = -TURN_ROTATE_VALUE;
						motorCRotate = TURN_ROTATE_VALUE;
						break;
					}
					break;
			}
			
			for (int i = 0; i < rotateTimes; i++) {
				Motor.B.rotate(motorBRotate, true);
				Motor.C.rotate(motorCRotate);
			}
			actualDirection = newDirection;
			newDirection = -1;
		}

		@Override
		public void suppress() {
			
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		Thread.sleep(2000);
		UltrasonicSensor ultrasonicSensor = new UltrasonicSensor(SensorPort.S3);
		Behavior[] behaviorList = {new AndarRastrearBloco(ultrasonicSensor), new Girar()};
		
		Arbitrator arbitrator = new Arbitrator(behaviorList);
		arbitrator.start();
	}
	
}
