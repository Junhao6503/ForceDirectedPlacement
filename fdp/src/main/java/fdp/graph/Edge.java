package fdp.graph;

public class Edge {
	
	public Edge(Vertex v, Vertex u) {
		this.v = v;
		this.u = u;
		this.edge_type = -1;
	}

	private final Vertex v;
	private final Vertex u;
	public int edge_type;
	
	public Vertex getV() {
		return v;
	}

	public Vertex getU() {
		return u;
	}
}
