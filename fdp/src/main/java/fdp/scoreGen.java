package fdp;

import fdp.graph.Edge;
import fdp.graph.Vertex;
import org.jgrapht.Graph;

import javax.vecmath.Vector2d;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class scoreGen {
    private Graph<Vertex, Edge> graph;
    //private double nodeSpread;
    private int commuNumber;
    private HashMap<Integer, HashSet<Vertex>> communitiesMap;
    private Parameter p;

    /**
     *
     * @param graph
     */
    public scoreGen(Graph<Vertex, Edge> graph, HashMap<Integer, HashSet<Vertex>> communitiesMap, Parameter p) {
        this.graph = graph;
        this.communitiesMap =communitiesMap;
        this.commuNumber = communitiesMap.keySet().size();
        this.p = p;
    }

    /**
     *
     * @return nodeSpread Score
     */
    public double nodeSpread() {
        double finalScore = 0.0;
        for(int c : communitiesMap.keySet()) {
            int comN = communitiesMap.get(c).size();

            double sumX = 0.0;
            double sumY = 0.0;
            Set<Vertex> vertexSet= communitiesMap.get(c);
            for(Vertex v :  vertexSet) {
                sumX += v.getPos().getX();
                sumY += v.getPos().getY();
            }
            double meanX = sumX / comN;
            double meanY = sumY / comN;
            double localScore = 0.0;
            for(Vertex v :  vertexSet) {
                double firstPart = Math.pow((meanX - v.getPos().getX()), 2.0);
                double secondPart = Math.pow((meanY - v.getPos().getY()), 2.0);
                localScore += Math.sqrt(firstPart + secondPart);
            }

            finalScore  += (localScore / this.commuNumber);
        }

        return finalScore;

    }

    /**
     *
     * @param n
     * @return
     */
    public double occlusions(int n) {
        double score = 0.0;

        double x_n = this.p.getFrameWidth() / n;
        double y_n = this.p.getFrameHeight() / n;




        return score;

    }

    /**
     *
     * @return
     */
    public double edgeLengthVariation() {
        int edgeNum = graph.edgeSet().size();
        double sumLength = 0.0;
        for (Edge e : graph.edgeSet()) {
            // normalized difference position vector of v and u
            Vector2d deltaPos = new Vector2d();
            deltaPos.sub(e.getV().getPos(), e.getU().getPos());
            double length = deltaPos.length();
            sumLength += length;

        }
        double lMean = sumLength / edgeNum;
        double std = 0.0;
        for(Edge e :  graph.edgeSet()) {
            Vector2d deltaPos = new Vector2d();
            deltaPos.sub(e.getV().getPos(), e.getU().getPos());
            double length = deltaPos.length();
            std += Math.pow((length - lMean), 2);
        }
        double la = Math.sqrt(std / (edgeNum*(lMean*lMean)));
        return la/Math.sqrt(edgeNum-1);

    }

    public double myScore() {
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



}
