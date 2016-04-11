import java.util.ArrayList;

import lejos.geom.Point;

public abstract class Utils {

	public static ArrayList<Direcao> findPathToNextAvaiblePos(Main.Posicao[][] map, Point currentPos) {
		ArrayList<Direcao> path = new ArrayList<>();
		ArrayList<Point> stack = new ArrayList<>();
		
		Point[] neighbors = getNeighbors(map, currentPos);
		for (int i = 0; i < neighbors.length; i++) {
			
			Point currentNeighbor = neighbors[i];
			if (currentNeighbor != null) {
				Main.Posicao pos = map[(int) currentNeighbor.y][(int) currentNeighbor.x];
				if (pos == null) {
					break;
				} else {
					
				}
			}
		}
		
		return path;
	}
	
	private static void find(Main.Posicao[][] map, Point currentPos, ArrayList<Point> stack) {
		stack.add(currentPos);
		Point[] neighbors = getNeighbors(map, currentPos);
		for (int i = 0; i < neighbors.length; i++) {
			
			Point currentNeighbor = neighbors[i];
			if (currentNeighbor != null) {
				Main.Posicao pos = map[(int) currentNeighbor.y][(int) currentNeighbor.x];
				if (pos == null) {
					stack.add(currentNeighbor);
					break;
				} else {
					find(map, currentNeighbor, stack);
				}
			}
		}
	}
	
	public static Point[] getNeighbors(Main.Posicao[][] map, Point currentPos) {
		Point[] children = new Point[4];
		Main.Posicao pos = map[(int) currentPos.y][(int) currentPos.x];
		
		if (pos.freePaths[Main.Posicao.BAIXO]) {
			children[Main.Posicao.BAIXO] = new Point(currentPos.x, currentPos.y + 1);
		}
		
		if (pos.freePaths[Main.Posicao.CIMA]) {
			children[Main.Posicao.CIMA] = new Point(currentPos.x, currentPos.y - 1);
		}
		
		if (pos.freePaths[Main.Posicao.DIREITA]) {
			children[Main.Posicao.DIREITA] = new Point(currentPos.x + 1, currentPos.y);
		}
		
		if (pos.freePaths[Main.Posicao.ESQUERDA]) {
			children[Main.Posicao.ESQUERDA] = new Point(currentPos.x - 1, currentPos.y);
		}
		
		return children;
	}
	
}
