package fdp;

import org.jgrapht.Graph;
import org.jgrapht.generate.GraphGenerator;
import org.jgrapht.graph.SimpleGraph;

import fdp.graph.Edge;
import fdp.graph.EdgeFactory;
import fdp.graph.Vertex;
import fdp.graph.VertexFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.lang.String;;

/**
 * Container class that represents a kind of Graph and parameters later used in the simulation.
 * @author Bennet
 */
public class GraphConfiguration {

	private GraphGenerator<Vertex, Edge, ?> generator;
	private Parameter param;
	
	public GraphConfiguration(GraphGenerator<Vertex, Edge, ?> generator, Parameter param) {
		this.generator = generator;
		this.setParameter(param);
	}

	/**
	 * Generates a new Graph using the GraphGenerator.
	 * @return
	 * @throws FileNotFoundException 
	 */
	public Graph<Vertex, Edge> generateGraph() throws FileNotFoundException {
		Graph<Vertex, Edge> graph = new SimpleGraph<>(new EdgeFactory());
//		this.generator.generateGraph(graph, new VertexFactory(), null);
//		Vertex v1 = new Vertex();
//		Vertex v2  = new Vertex();
//		Vertex v3 = new Vertex();
//		graph.addVertex(v1);
//		graph.addVertex(v2);
//		graph.addEdge(v1, v2);
		readFiletoGraph(graph);
		return graph;
	}

	public Parameter getParameter() {
		return param;
	}

	public void setParameter(Parameter param) {
		this.param = param;
	}
	
	private void readFiletoGraph(Graph<Vertex, Edge> graph) throws FileNotFoundException {
		String line;
		Set<String> set = new HashSet<>();
		ArrayList<Vertex> list = new ArrayList();
		for(int i = 1; i <= 63; i ++) {
			list.add(new Vertex());
		}
		for (int i = 0; i < 63; i ++) {
			graph.addVertex(list.get(i));
		}
		File file = 
			      new File("/Users/junhao/Downloads/SNAP/dolpins.txt"); 
			    Scanner sc = new Scanner(file); 
			    while (sc.hasNextLine()) {
			        line = sc.nextLine();
			        String[] splited = line.split("\\t");
			        int source = Integer.parseInt(splited[0]);
			        int dest = Integer.parseInt(splited[1]);
			        graph.addEdge(list.get(source), list.get(dest));
			        
			    } 
	}
}
