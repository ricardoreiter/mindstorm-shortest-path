import java.util.ArrayList;


public class Graph {

	public static class Vertice {

		public Integer[] neighbors;
		
	}
	
	public Vertice[] vertices;
	
	public Graph(Posicao[][] matrix) {
		vertices = new Vertice[matrix.length * matrix.length];
		int currentVertice = 0;
		for (int y = 0; y < matrix.length; y++) {
			for (int x = 0; x < matrix[y].length; x++) {
				Vertice newVertice = new Vertice();
				ArrayList<Integer> neighbors = new ArrayList<>();
				if (matrix[y][x].freePaths[Posicao.CIMA]) {
					neighbors.add(currentVertice - 4);
				}
				if (matrix[y][x].freePaths[Posicao.BAIXO]) {
					neighbors.add(currentVertice + 4);
				}
				if (matrix[y][x].freePaths[Posicao.DIREITA]) {
					neighbors.add(currentVertice + 1);
				}
				if (matrix[y][x].freePaths[Posicao.ESQUERDA]) {
					neighbors.add(currentVertice - 1);
				}
				newVertice.neighbors = neighbors.toArray(new Integer[0]);
				vertices[currentVertice++] = newVertice;
			}
		}
	}
	
	public int size() {
		return vertices.length;
	}

	public Integer[] neighbors(int next) {
		return vertices[next].neighbors;
	}

	public int getWeight(int next, int v) {
		return 1;
	}

}
