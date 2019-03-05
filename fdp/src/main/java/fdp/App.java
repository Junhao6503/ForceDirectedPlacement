package fdp;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.vecmath.Vector2d;

import org.jgrapht.Graph;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import parsii.tokenizer.ParseException;
import fdp.GraphConfiguration;
import fdp.Parameter;
import fdp.Simulation;
import fdp.graph.Edge;
import fdp.graph.Vertex;

public class App {

	public static double score(Graph<Vertex, Edge> graph) {
		double total_score = 0;
		for (Edge e : graph.edgeSet()) {
			// normalized difference position vector of v and u
			Vector2d deltaPos = new Vector2d();
			deltaPos.sub(e.getV().getPos(), e.getU().getPos());
			double length = deltaPos.length();
			
			
			if(e.edge_type ==0 ) {
				total_score += Math.abs(length - 100); 
			} else if(e.edge_type == 1) {
				total_score += Math.abs(length - 300); 
			}
			
		}
		
		for (Vertex v : graph.vertexSet()) {
			for (Vertex u : graph.vertexSet()) {
				if (!v.equals(u) && v.communities == u.communities && !v.neighbors.contains(u)) {
					// normalized difference position vector of v and u
					Vector2d deltaPos = new Vector2d();
					deltaPos.sub(v.getPos(), u.getPos());
					double length = deltaPos.length();
					total_score += Math.abs(length - 200); 
					
				}
			}
		}
		return total_score;
	}
	public static void main(String[] args) throws InterruptedException, ExecutionException, IOException, ParseException {
		
		List<Future<?>> futures = new ArrayList<Future<?>>();
		Parameter p = new Parameter();
		Scanner sc = new Scanner(System.in);
		
		
		
		
		System.out.println("Enter the input file: ");
		String input;
		String comm;
		int start;
		int vertex_num;
		int dem;
		switch (sc.nextLine()) {
			case "kara":
				input = "kara.txt";
				comm = "communities_kara.txt";
				start = 1;
				vertex_num = 34;
				dem = 0;
				break;
			case "dolphins":
				input = "dolpins.txt";
				comm = "communities_dolp.txt";
				start = 0;
				vertex_num = 62;
				dem = 0;
				break;
			case "soc-tribes":
				input = "soc-tribes.txt";
				comm = "communities_tribe.txt";
				start = 1;
				vertex_num = 16;
				dem = 1;
				break;
			default:
				input = "kara.txt";
				comm = "communities_kara.txt";
				start = 1;
				vertex_num = 34;
				dem = 0;
				break;
		}
		sc.close();
		String input_dir = "/Users/junhao/Downloads/SNAP/" + input;
		String comm_dir = "/Users/junhao/Downloads/SNAP/" + comm;
		
		p.setEquilibriumCriterion(true);
		p.setCriterion(15.0);
		p.setCoolingRate(0.01);
		p.setFrameDelay(5);
		p.setFrameWidth(800);
		p.setFrameHeight(600);
		p.input_name = input;
		GraphConfiguration config = new GraphConfiguration(p);
		Graph<Vertex, Edge> graph = config.generateGraph(input_dir, comm_dir, start, vertex_num, dem);
		
		
		
		Future<?> f = Executors.newSingleThreadExecutor().submit(new Simulation(graph, config, p));
		
		futures.add(f);
		
		for(Future<?> future : futures)
		    future.get(); // get will block until the future is done
		
		String [] split = input.split("\\.");
		String input_name = split[0];
		String output  = "/Users/junhao/Documents/old_fdp_out/" + input_name + "_output.txt";
		String score_output = "/Users/junhao/Documents/old_fdp_out/" + input_name + "_score.txt";
		FileWriter fileWriter = new FileWriter(output);
		FileWriter fileWriter_score = new FileWriter(score_output);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		PrintWriter printWriter_score = new PrintWriter(fileWriter_score);
		
		scoreGen scoregen = new scoreGen(graph,config.communitiesMap, p);
		
		for(Vertex v : graph.vertexSet()) {
			//System.out.print("" + v.id + ": ");
			printWriter.printf("" + v.id + "\t");
			double x = v.getPos().x;
			double y = v.getPos().y;
			printWriter.printf("" + x + "\t" + y);
			printWriter.printf("\n");
			
			//System.out.print("" + x + ", " + y);
			//System.out.println();
		}
		
		printWriter.close();
		
		double total_score = score(graph);
		double node_spread = scoregen.nodeSpread();
		double edge_length = scoregen.edgeLengthVariation();
		printWriter_score.printf("total score = " + total_score + "\n");
		printWriter_score.printf("node spread = " + node_spread + "\n");
		printWriter_score.printf("edge_length = " + edge_length + "\n");
		printWriter_score.close();
		System.out.println("total score = " + total_score);
	}
}
