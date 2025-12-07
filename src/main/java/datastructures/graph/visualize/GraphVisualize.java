package datastructures.graph.visualize;

import datastructures.common.Graph;
import datastructures.graph.edge.GraphEdge;
import org.apache.commons.lang3.RandomUtils;
import org.graphstream.graph.implementations.AdjacencyListGraph;

public class GraphVisualize<T> {

    private final Graph<T> graph;
    private String name;

    private org.graphstream.graph.Graph visualGraph;


    private GraphVisualize(Graph<T> graph) {
        this.graph = graph;
        this.name = "GRAPPH" + RandomUtils.nextLong(999, 9999);
    }

    public static <E> GraphVisualize<E> of(Graph<E> graph) {
        return new GraphVisualize<>(graph);
    }

    public GraphVisualize<T> withName(String name) {
        this.name = name;
        return this;
    }

    public GraphVisualize<T> build() {
        System.setProperty("org.graphstream.ui" , "swing");
        visualGraph = new AdjacencyListGraph(name);
        var adjacencyList = graph.asAdjacencyMap();
        adjacencyList.keySet().forEach(key ->
            visualGraph.addNode(getNodeName(key))
        );
        adjacencyList.forEach((key,value) ->
            value.forEach(this::addEdge)
        );
        return this;
    }

    public void display(){
        if (this.visualGraph==null){
            throw new IllegalStateException("Cant display, as no graph is configure run , use build method first");
        }
        visualGraph.display();

    }

    private void addEdge(GraphEdge<T,T> graphEdge){
        visualGraph.addEdge(
            generateIdForEdge(graphEdge),
                getNodeName(graphEdge.start()),
                getNodeName(graphEdge.end())
        );
    }


    private String generateIdForEdge(GraphEdge<T,T> edge){
        return getNodeName(edge.start()) + "-" + getNodeName(edge.end())
                + "-" + RandomUtils.nextLong(99,9999);
    }

    private String getNodeName(Object t){
        if(t instanceof String){
            return  t.toString();
        } else if (t instanceof Number) {
            return t.toString();
        } else {
            return String.valueOf(t.hashCode());
        }
    }
}
