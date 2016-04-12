import java.util.ArrayList;

import lejos.geom.Point;
import lejos.nxt.Motor;
import lejos.nxt.UltrasonicSensor;

public abstract class Direcao {

	protected UltrasonicSensor sensor;
	
	public Direcao(UltrasonicSensor sensor) {
		this.sensor = sensor;
	}
	
	public abstract ArrayList<Direcao> checkBounds(Posicao pos, Point actualPos);
	
	public abstract GirarRoboInfo getTurnInfo(Direcao olderDirection);
	
	public abstract Point getUpdatedPoint(Point point);
	
	public abstract Direcao reverse();
	
	protected boolean isPathFree(int angle) {
		Motor.A.rotate(-angle);
		return sensor.getDistance() > Main.MAX_DISTANCE_TO_WALL;
	}
	
	protected Direcao createDirection(int direction) {
		switch (direction) {
			case Posicao.CIMA:
				return new Cima(sensor);
			case Posicao.BAIXO:
				return new Baixo(sensor);
			case Posicao.DIREITA:
				return new Direita(sensor);
			case Posicao.ESQUERDA:
				return new Esquerda(sensor);
		}
		return null;
	}
	
}

class Baixo extends Direcao {

	public Baixo(UltrasonicSensor sensor) {
		super(sensor);
	}

	@Override
	public ArrayList<Direcao> checkBounds(Posicao pos, Point actualPos) {
		pos.freePaths[Posicao.CIMA] = actualPos.y > 0;
		pos.freePaths[Posicao.BAIXO] = isPathFree(0) && actualPos.y < 3;
		pos.freePaths[Posicao.DIREITA] = isPathFree(90) && actualPos.x < 3;
		pos.freePaths[Posicao.ESQUERDA] = isPathFree(-180) && actualPos.x > 0;
		isPathFree(90);
		
		ArrayList<Direcao> directions = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			if (i == Posicao.CIMA) {
				continue;
			}
			
			if (pos.freePaths[i]) {
				directions.add(createDirection(i));
			}
		}
		return directions;
	}

	@Override
	public GirarRoboInfo getTurnInfo(Direcao olderDirection) {
		if (olderDirection instanceof Cima) {
			return new GirarRoboInfo(Main.TURN_ROTATE_VALUE * 2, -Main.TURN_ROTATE_VALUE * 2);
		}
		
		if (olderDirection instanceof Direita) {
			return new GirarRoboInfo(Main.TURN_ROTATE_VALUE, -Main.TURN_ROTATE_VALUE);
		}
		
		if (olderDirection instanceof Esquerda) {
			return new GirarRoboInfo(-Main.TURN_ROTATE_VALUE, Main.TURN_ROTATE_VALUE);
		}
		
		return null;
	}

	@Override
	public Point getUpdatedPoint(Point point) {
		return new Point(point.x, point.y + 1);
	}

	@Override
	public Direcao reverse() {
		return new Cima(sensor);
	}
	
}

class Cima extends Direcao {

	public Cima(UltrasonicSensor sensor) {
		super(sensor);
	}

	@Override
	public ArrayList<Direcao> checkBounds(Posicao pos, Point actualPos) {
		pos.freePaths[Posicao.BAIXO] = actualPos.y < 3;
		pos.freePaths[Posicao.CIMA] = isPathFree(0) && actualPos.y > 0;
		pos.freePaths[Posicao.ESQUERDA] = isPathFree(90) && actualPos.x > 0;
		pos.freePaths[Posicao.DIREITA] = isPathFree(-180) && actualPos.x < 3;
		
		isPathFree(90);
		ArrayList<Direcao> directions = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			if (i == Posicao.BAIXO) {
				continue;
			}
			
			if (pos.freePaths[i]) {
				directions.add(createDirection(i));
			}
		}
		return directions;
	}

	@Override
	public GirarRoboInfo getTurnInfo(Direcao olderDirection) {
		if (olderDirection instanceof Baixo) {
			return new GirarRoboInfo(Main.TURN_ROTATE_VALUE * 2, -Main.TURN_ROTATE_VALUE * 2);
		}
		
		if (olderDirection instanceof Direita) {
			return new GirarRoboInfo(-Main.TURN_ROTATE_VALUE, Main.TURN_ROTATE_VALUE);
		}
		
		if (olderDirection instanceof Esquerda) {
			return new GirarRoboInfo(Main.TURN_ROTATE_VALUE, -Main.TURN_ROTATE_VALUE);
		}
		
		return null;
	}
	
	@Override
	public Point getUpdatedPoint(Point point) {
		return new Point(point.x, point.y - 1);
	}
	
	@Override
	public Direcao reverse() {
		return new Baixo(sensor);
	}
	
}

class Direita extends Direcao {

	public Direita(UltrasonicSensor sensor) {
		super(sensor);
	}

	@Override
	public ArrayList<Direcao> checkBounds(Posicao pos, Point actualPos) {
		pos.freePaths[Posicao.ESQUERDA] = true;
		pos.freePaths[Posicao.DIREITA] = isPathFree(0) && actualPos.x < 3;
		pos.freePaths[Posicao.CIMA] = isPathFree(90) && actualPos.y > 0;
		pos.freePaths[Posicao.BAIXO] = isPathFree(-180) && actualPos.y < 3;
		
		isPathFree(90);
		ArrayList<Direcao> directions = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			if (i == Posicao.ESQUERDA) {
				continue;
			}
			
			if (pos.freePaths[i]) {
				directions.add(createDirection(i));
			}
		}
		return directions;
	}

	@Override
	public GirarRoboInfo getTurnInfo(Direcao olderDirection) {
		if (olderDirection instanceof Cima) {
			return new GirarRoboInfo(Main.TURN_ROTATE_VALUE, -Main.TURN_ROTATE_VALUE);
		}
		
		if (olderDirection instanceof Baixo) {
			return new GirarRoboInfo(-Main.TURN_ROTATE_VALUE, Main.TURN_ROTATE_VALUE);
		}
		
		if (olderDirection instanceof Esquerda) {
			return new GirarRoboInfo(-Main.TURN_ROTATE_VALUE * 2, Main.TURN_ROTATE_VALUE * 2);
		}
		
		return null;
	}
	
	@Override
	public Point getUpdatedPoint(Point point) {
		return new Point(point.x + 1, point.y);
	}
	
	@Override
	public Direcao reverse() {
		return new Esquerda(sensor);
	}
	
}

class Esquerda extends Direcao {

	public Esquerda(UltrasonicSensor sensor) {
		super(sensor);
	}

	@Override
	public ArrayList<Direcao> checkBounds(Posicao pos, Point actualPos) {
		pos.freePaths[Posicao.DIREITA] = true;
		pos.freePaths[Posicao.ESQUERDA] = isPathFree(0) && actualPos.x > 0;
		pos.freePaths[Posicao.BAIXO] = isPathFree(90) && actualPos.y < 3;
		pos.freePaths[Posicao.CIMA] = isPathFree(-180) && actualPos.y > 0;
		
		isPathFree(90);
		ArrayList<Direcao> directions = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			if (i == Posicao.DIREITA) {
				continue;
			}
			
			if (pos.freePaths[i]) {
				directions.add(createDirection(i));
			}
		}
		return directions;
	}

	@Override
	public GirarRoboInfo getTurnInfo(Direcao olderDirection) {
		if (olderDirection instanceof Cima) {
			return new GirarRoboInfo(-Main.TURN_ROTATE_VALUE, Main.TURN_ROTATE_VALUE);
		}
		
		if (olderDirection instanceof Baixo) {
			return new GirarRoboInfo(Main.TURN_ROTATE_VALUE, -Main.TURN_ROTATE_VALUE);
		}
		
		if (olderDirection instanceof Direita) {
			return new GirarRoboInfo(-Main.TURN_ROTATE_VALUE * 2, Main.TURN_ROTATE_VALUE * 2);
		}
		
		return null;
	}
	
	@Override
	public Point getUpdatedPoint(Point point) {
		return new Point(point.x - 1, point.y);
	}
	
	@Override
	public Direcao reverse() {
		return new Direita(sensor);
	}
	
}