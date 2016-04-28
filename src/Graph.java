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
				newVertice.neighbors = new Integer[neighbors.size()];
				neighbors.toArray(newVertice.neighbors);
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
	
		Posicao pos11 = new Posicao();
		pos11.freePaths[Posicao.CIMA] = false;
		pos11.freePaths[Posicao.BAIXO] = true;
		pos11.freePaths[Posicao.ESQUERDA] = false;
		pos11.freePaths[Posicao.DIREITA] = false;
		
		Posicao pos21 = new Posicao();
		pos21.freePaths[Posicao.CIMA] = true;
		pos21.freePaths[Posicao.BAIXO] = true;
		pos21.freePaths[Posicao.ESQUERDA] = false;
		pos21.freePaths[Posicao.DIREITA] = false;
		
		Posicao pos31 = new Posicao();
		pos31.freePaths[Posicao.CIMA] = true;
		pos31.freePaths[Posicao.BAIXO] = true;
		pos31.freePaths[Posicao.ESQUERDA] = false;
		pos31.freePaths[Posicao.DIREITA] = false;
		
		Posicao pos41 = new Posicao();
		pos41.freePaths[Posicao.CIMA] = true;
		pos41.freePaths[Posicao.BAIXO] = false;
		pos41.freePaths[Posicao.ESQUERDA] = false;
		pos41.freePaths[Posicao.DIREITA] = true;
		
		Posicao pos12 = new Posicao();
		pos12.freePaths[Posicao.CIMA] = false;
		pos12.freePaths[Posicao.BAIXO] = true;
		pos12.freePaths[Posicao.ESQUERDA] = false;
		pos12.freePaths[Posicao.DIREITA] = false;
		
		Posicao pos22 = new Posicao();
		pos22.freePaths[Posicao.CIMA] = true;
		pos22.freePaths[Posicao.BAIXO] = true;
		pos22.freePaths[Posicao.ESQUERDA] = false;
		pos22.freePaths[Posicao.DIREITA] = true;
		
		Posicao pos32 = new Posicao();
		pos32.freePaths[Posicao.CIMA] = true;
		pos32.freePaths[Posicao.BAIXO] = true;
		pos32.freePaths[Posicao.ESQUERDA] = false;
		pos32.freePaths[Posicao.DIREITA] = false;
		
		Posicao pos42 = new Posicao();
		pos42.freePaths[Posicao.CIMA] = true;
		pos42.freePaths[Posicao.BAIXO] = false;
		pos42.freePaths[Posicao.ESQUERDA] = true;
		pos42.freePaths[Posicao.DIREITA] = true;
		
		Posicao pos13 = new Posicao();
		pos13.freePaths[Posicao.CIMA] = false;
		pos13.freePaths[Posicao.BAIXO] = true;
		pos13.freePaths[Posicao.ESQUERDA] = false;
		pos13.freePaths[Posicao.DIREITA] = true;
		
		Posicao pos23 = new Posicao();
		pos23.freePaths[Posicao.CIMA] = true;
		pos23.freePaths[Posicao.BAIXO] = true;
		pos23.freePaths[Posicao.ESQUERDA] = true;
		pos23.freePaths[Posicao.DIREITA] = false;
		
		Posicao pos33 = new Posicao();
		pos33.freePaths[Posicao.CIMA] = true;
		pos33.freePaths[Posicao.BAIXO] = false;
		pos33.freePaths[Posicao.ESQUERDA] = false;
		pos33.freePaths[Posicao.DIREITA] = true;
		
		Posicao pos43 = new Posicao();
		pos43.freePaths[Posicao.CIMA] = false;
		pos43.freePaths[Posicao.BAIXO] = false;
		pos43.freePaths[Posicao.ESQUERDA] = true;
		pos43.freePaths[Posicao.DIREITA] = false;
		
		Posicao pos14 = new Posicao();
		pos14.freePaths[Posicao.CIMA] = false;
		pos14.freePaths[Posicao.BAIXO] = true;
		pos14.freePaths[Posicao.ESQUERDA] = true;
		pos14.freePaths[Posicao.DIREITA] = false;
		
		Posicao pos24 = new Posicao();
		pos24.freePaths[Posicao.CIMA] = true;
		pos24.freePaths[Posicao.BAIXO] = true;
		pos24.freePaths[Posicao.ESQUERDA] = false;
		pos24.freePaths[Posicao.DIREITA] = false;
		
		Posicao pos34 = new Posicao();
		pos34.freePaths[Posicao.CIMA] = true;
		pos34.freePaths[Posicao.BAIXO] = true;
		pos34.freePaths[Posicao.ESQUERDA] = true;
		pos34.freePaths[Posicao.DIREITA] = false;
		
		Posicao pos44 = new Posicao();
		pos44.freePaths[Posicao.CIMA] = true;
		pos44.freePaths[Posicao.BAIXO] = false;
		pos44.freePaths[Posicao.ESQUERDA] = false;
		pos44.freePaths[Posicao.DIREITA] = false;
		
		Posicao[][] posicao = {{pos11, pos12, pos13, pos14},
								{pos21, pos22, pos23, pos24},
								{pos31, pos32, pos33, pos34},
								{pos41, pos42, pos43, pos44}};
		Graph graph = new Graph(posicao);
		
		int[] paths = Dijkstra.dijkstra(graph, 0);
		int greenPosArray = 15;
		
		ArrayList<Direcao> directions = new ArrayList<>();
		
		int currentPos = greenPosArray;
		while (currentPos != 0) {
			int newPos = paths[currentPos];
			int dif = currentPos - newPos;
			if (dif == 4) {
				directions.add(new Baixo(null));
			} else if (dif == -4) {
				directions.add(new Cima(null));
			} else if (dif == 1) {
				directions.add(new Direita(null));
			} else if (dif == -1) {
				directions.add(new Esquerda(null));
			}
			currentPos = newPos;
		}
		
		for (int i = 0; i < directions.size(); i++) {
			System.out.println(directions.get(i));
		}
	}

}
