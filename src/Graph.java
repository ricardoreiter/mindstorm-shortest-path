
public class Graph {

	public static class Vertice {

		public int[] neighbors;
		
	}
	
	public Vertice[] vertices;
	
	public int size() {
		return vertices.length;
	}

	public int[] neighbors(int next) {
		return vertices[next].neighbors;
	}

	public int getWeight(int next, int v) {
		return 1;
	}

}
