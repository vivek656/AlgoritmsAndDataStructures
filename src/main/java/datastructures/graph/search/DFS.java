package datastructures.graph.search;

import com.google.common.base.Objects;
import datastructures.graph.DirectedGraph;
import datastructures.graph.GraphEdge;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static datastructures.graph.search.GraphSearch.VertexAttributes.VertexColor.*;

public non-sealed class DFS<T> extends GraphSearch<T> {

    private int globalTimer = 0;

    private DFS(DirectedGraph<T> g) {
        super(g);
    }

    private final LinkedList<T> topologicalOrder = new LinkedList<>();


    private final EnumMap<EdgeType, Set<GraphEdge<T, T>>> edgeRelation = new EnumMap<>(EdgeType.class);


    private void initializeAttributes() {
        adjacencyMap = g.asAdjacencyMap();
        vertexAttributesMap.clear();
        edgeRelation.clear();
        topologicalOrder.clear();
        for (var v : adjacencyMap.keySet()) {
            vertexAttributesMap.put(v, new DFSVertexAttributes<>(v));
        }
    }

    @NotNull
    @Contract("_ -> new")
    public static <E> DFS<E> of(DirectedGraph<E> g) {
        return new DFS<>(g);
    }

    /**
     * All values, attributes of class  is initialized by run method,
     * if the underlining Graph is modified,
     * U need to again call the run method to update attributes
     */
    public void run() {
        if (g == null)
            throw new IllegalStateException("Cant run dfs , without graph or vertex information");
        initializeAttributes();
        globalTimer = 0;
        for (var entry : vertexAttributesMap.entrySet()) {
            if (entry.getValue().colour == WHITE) {
                dfsVisit(getAttributesFor((entry.getKey())));
            }
        }
    }

    private DFSVertexAttributes<T> getAttributesFor(T u) {
        return (DFSVertexAttributes<T>) vertexAttributesMap.get(u);
    }

    private void dfsVisit(@NotNull DFSVertexAttributes<T> u) {
        globalTimer++;
        u.discoveryTime = globalTimer;
        u.colour = GRAY;
        for (var outgoingEdge : adjacencyMap.getOrDefault(u.key, Collections.emptySet())) {
            var v = getAttributesFor(outgoingEdge.end());
            addEdgeToRelation(u.getEdgeType(v) , outgoingEdge);
            if (v.colour == WHITE) {
                v.predecessor = u;
                dfsVisit(v);
            }
        }
        globalTimer++;
        u.finishTime = globalTimer;
        topologicalOrder.addFirst(u.key);
        u.colour = BLACK;
    }

    public List<T> topologicalSort() {
        var result = new LinkedList<T>();
        if(!getALLEdgesOfType(EdgeType.BACK_EDGE).isEmpty())return result;
        topologicalOrder.iterator().forEachRemaining(result::addLast);
        return result;
    }

    public enum EdgeType {
        BACK_EDGE, FORWARD_EDGE, CROSS_EDGE, TREE_EDGE
    }

    public Set<GraphEdge<T,T>> getALLEdgesOfType(EdgeType edgeType) {
        return edgeRelation.getOrDefault(edgeType, Collections.emptySet());
    }

    private void addEdgeToRelation(EdgeType type, GraphEdge<T, T> edge) {
        edgeRelation.putIfAbsent(type, new HashSet<>());
        edgeRelation.get(type).add(edge);
    }


    private static class DFSVertexAttributes<E> extends VertexAttributes<E> {

        DFSVertexAttributes<E> predecessor;
        int discoveryTime;
        int finishTime;

        DFSVertexAttributes(E key) {
            super(key);
            predecessor = null;
            discoveryTime = -1;
            finishTime = -1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DFSVertexAttributes<?> that)) return false;
            return com.google.common.base.Objects.equal(key, that.key);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(key);
        }

        /**
         * The DFS algorithm has enough information to classify some edges as it encounters them. The key idea is that when an edge .(u,v )is first explored, the color of
         * vertex v says something about the edge: <br>
         * 1. WHITE indicates a tree edge, <br>
         * 2. GRAY indicates a back edge, and <br>
         * 3. BLACK indicates a forward or cross edge. <br>
         * The first case is immediate from the specification of the algorithm. For the second case, observe that the gray vertices always form a linear chain of descendants
         * corresponding to the stack of active DFS-VISIT invocations. The number of gray
         * vertices is 1 more than the depth in the depth-first forest of the vertex most recently
         * discovered. Depth-first search always explores from the deepest gray vertex, so
         * that an edge that reaches another gray vertex has reached an ancestor. The third
         * case handles the remaining possibility.
         * an edge (u,v) is a forward edge if u:d < v:d and a cross edge if u:d > v:d.<br>
         * Because if u:d < v:d this ,means v in child of u (ajd+ of some other vertex in u adj+) and hence will forward u.<br>
         * v:d > u:d , means v is finished and is on another dfs forest , than U , and hence cross edge
         *
         * @param v , the vertex attribute to add a edge type
         */
        EdgeType getEdgeType(DFSVertexAttributes<E> v) {
            switch (v.colour) {
                case WHITE -> {
                    return EdgeType.TREE_EDGE;
                }
                case GRAY -> {
                    return EdgeType.BACK_EDGE;
                }
                case BLACK -> {
                    if (discoveryTime < v.discoveryTime)
                        return EdgeType.FORWARD_EDGE;
                }
            }
            return EdgeType.CROSS_EDGE;
        }

    }
}
