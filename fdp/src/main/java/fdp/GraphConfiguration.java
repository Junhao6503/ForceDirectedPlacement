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
	
	public GraphConfiguration(Parameter param) {
		//this.generator = generator;
		this.setParameter(param);
	}

	/**
	 * Generates a new Graph using the GraphGenerator.
	 * @return
	 * @throws FileNotFoundException 
	 */
	public Graph<Vertex, Edge> generateGraph(String input_dir, String comm_dir, int start, int vertex_num, int dem) throws FileNotFoundException {
		Graph<Vertex, Edge> graph = new SimpleGraph<Vertex, Edge>(new EdgeFactory());
//		this.generator.generateGraph(graph, new VertexFactory(), null);
//		Vertex v1 = new Vertex();
//		Vertex v2  = new Vertex();
//		Vertex v3 = new Vertex();
//		graph.addVertex(v1);
//		graph.addVertex(v2);
//		graph.addEdge(v1, v2);
		readFiletoGraph(graph, input_dir, comm_dir, start,vertex_num, dem);
		return graph;
	}

	public Parameter getParameter() {
		return param;
	}

	public void setParameter(Parameter param) {
		this.param = param;
	}
	
	private void readFiletoGraph(Graph<Vertex, Edge> graph, String input_dir, String comm_dir, int start, int vertex_num, int dem) throws FileNotFoundException {
		String line;
		Set<String> set = new HashSet<String>();
		ArrayList<Vertex> list = new ArrayList();
		for(int i = 0; i < vertex_num+start; i ++) {
			list.add(new Vertex());
		}
		File file = new File(comm_dir); 
		Scanner sc = new Scanner(file); 
			    while (sc.hasNextLine()) {
			        line = sc.nextLine();
			        String[] splited;
			        if(dem == 0) {
			        	splited = line.split("\\t");
			        } else {
			        	splited = line.split("\\s+");
			        }
			        int node = Integer.parseInt(splited[0]);
			        int comm = Integer.parseInt(splited[1]);
			        list.get(node).id = node;
			        list.get(node).communities = comm;
			        
			        
		} 
		if(start == 1) {
			for (int i = 1; i <= vertex_num; i ++) {
				graph.addVertex(list.get(i));
			}
		} else {
			for (int i = 0; i < vertex_num; i ++) {
				graph.addVertex(list.get(i));
			}
		}
		file = new File(input_dir); 
		sc = new Scanner(file); 
			    while (sc.hasNextLine()) {
			        line = sc.nextLine();
			        String[] splited;
			        if(dem == 0) {
			        	splited = line.split("\\t");
			        } else {
			        	splited = line.split("\\s+");
			        }
			        int source = Integer.parseInt(splited[0]);
			        int dest = Integer.parseInt(splited[1]);
			        if(source != dest) {
			        	graph.addEdge(list.get(source), list.get(dest));
			        }
			        list.get(source).neighbors.add(list.get(dest));
			        list.get(dest).neighbors.add(list.get(source));
			        
			    } 
	}
}
