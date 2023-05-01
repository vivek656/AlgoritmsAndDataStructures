package datastructures.graph;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static datastructures.graph.GraphSearch.VertexAttributes.VertexColor.WHITE;

sealed class GraphSearch<T> permits BFS, DFS {

    protected HashMap<T, VertexAttributes<T>> vertexAttributesMap =  new HashMap<>();

    protected Map<T, Set<GraphEdge<T,T>>> adjacencyMap;

    protected Graph<T> g;

    GraphSearch(Graph<T> g){
        adjacencyMap = g.asAdjacencyMap();
        this.g = g;

    }

    protected static class VertexAttributes<E> {


        protected enum VertexColor {
            WHITE, GRAY, BLACK
        }

        protected E key;
        protected VertexColor colour;

        VertexAttributes(E key) {
            this.key = key;
            colour = WHITE;
        }

    }

}
