package datastructures.graph.search;

import com.google.common.base.Objects;
import datastructures.common.Graph;
import datastructures.graph.GraphEdge;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static datastructures.graph.search.GraphSearch.VertexAttributes.VertexColor.WHITE;

abstract sealed class GraphSearch<T> permits BFS, DFS {

    protected HashMap<T, VertexAttributes<T>> vertexAttributesMap =  new HashMap<>();

    protected Map<T, Set<GraphEdge<T,T>>> adjacencyMap;

    protected Graph<T> g;

    GraphSearch(Graph<T> g){
        adjacencyMap = g.asAdjacencyMap();
        this.g = g;
    }

    protected static class VertexAttributes<E> {


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof VertexAttributes<?> that)) return false;
            return Objects.equal(key, that.key);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(key);
        }

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
