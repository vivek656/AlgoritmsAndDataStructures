package datastructures.graph.search;

import datastructures.graph.Graph;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static datastructures.graph.search.GraphSearch.VertexAttributes.VertexColor.*;

public non-sealed class DFS<T> extends GraphSearch<T> {

    private int globalTimer = 0;
    private DFS(Graph<T> g) {
        super(g);
    }

    private LinkedList<T> topologicalOrder = new LinkedList<>();


    private void initializeAttributes(){
        if(g == null)
            throw new IllegalStateException("Failed to Initialize run of dfs , without graph or vertex information");
        adjacencyMap = g.asAdjacencyMap();
        vertexAttributesMap.clear();
        for (var v : adjacencyMap.keySet()) {
            vertexAttributesMap.put(v, new DFSVertexAttributes<>(v));
        }
    }

    @NotNull
    @Contract("_ -> new")
    public static <E> DFS<E> of(Graph<E> g) {
        return new DFS<>(g);
    }

    /**
     * All values, attributes of class  is initialized by run method,
     * if the underlining Graph is modified,
     * U need to again call the run method to update attributes
     */
    public void run() {
        if(g == null)
            throw new IllegalStateException("Cant run dfs , without graph or vertex information");
        initializeAttributes();
        globalTimer = 0;
        for (var entry : vertexAttributesMap.entrySet()){
            if(entry.getValue().colour == WHITE){
                dfsVisit(getAttributesFor((entry.getKey())));
            }
        }
    }

    private DFSVertexAttributes<T> getAttributesFor(T u){
        return (DFSVertexAttributes<T>) vertexAttributesMap.get(u);
    }

    private void dfsVisit(@NotNull DFSVertexAttributes<T> u){
        globalTimer++;
        u.discoveryTime = globalTimer;
        u.colour = GRAY;

        for (var outgoingVertex : adjacencyMap.getOrDefault(u.key , Collections.emptySet())){
            var v = getAttributesFor(outgoingVertex.getEnd());
            if(v.colour==WHITE){
                v.predecessor = u;
                dfsVisit(v);
            }
        }
        globalTimer++;
        u.finishTime = globalTimer;
        topologicalOrder.addFirst(u.key);
        u.colour=BLACK;
    }

    public List<T> topologicalSort(){
        var result = new LinkedList<T>();
        topologicalOrder.iterator().forEachRemaining(result::addLast);
        return result;
    }


    private static class DFSVertexAttributes<E> extends VertexAttributes<E> {

        DFSVertexAttributes<E> predecessor;
        private final EnumMap<EdgeType, Set<DFSVertexAttributes<E>>> edgeRelation= new EnumMap<>(EdgeType.class);
        enum EdgeType{
            BACK_EDGE,FORWARD_EDGE,CROSS_EDGE,TREE_EDGE
        }

        private void addEdgeToRelation(EdgeType type , DFSVertexAttributes<E> v){
            edgeRelation.putIfAbsent(type , new HashSet<>());
            edgeRelation.get(type).add(v);
        }


        int discoveryTime;
        int finishTime;

        DFSVertexAttributes(E key) {
            super(key);
            predecessor = null;
            discoveryTime = -1;
            finishTime = -1;
        }

        /**
         *
         * The DFS algorithm has enough information to classify some edges as it encounters them. The key idea is that when an edge .(u,v )is first explored, the color of
         * vertex v says something about the edge: <br>
         * * 1. WHITE indicates a tree edge, <br>
         * * 2. GRAY indicates a back edge, and <br>
         * * 3. BLACK indicates a forward or cross edge. <br>
         * The first case is immediate from the specification of the algorithm. For the second case, observe that the gray vertices always form a linear chain of descendants
         * corresponding to the stack of active DFS-VISIT invocations. The number of gray
         * vertices is 1 more than the depth in the depth-first forest of the vertex most recently
         * discovered. Depth-first search always explores from the deepest gray vertex, so
         * that an edge that reaches another gray vertex has reached an ancestor. The third
         * case handles the remaining possibility.
         * an edge (u,v) is a forward edge if u:d < v:d and a cross edge if u:d > v:d.
         * because if u:d < v:d (this means v is from adjacency list of v, and hence is a forward edge)
         * v:d > u:d (this means v is not from adjacency list of u , and hence is a cross edge)
         * @param v , the vertex attribute to add a edge type
         */
        void addEdgeType(DFSVertexAttributes<E> v){
            switch (v.colour){
                case WHITE -> addEdgeToRelation(EdgeType.TREE_EDGE , v);
                case GRAY ->  addEdgeToRelation(EdgeType.BACK_EDGE , v);
                case BLACK -> {
                    if(discoveryTime < v.discoveryTime){
                        addEdgeToRelation(EdgeType.FORWARD_EDGE , v);
                    }
                    if(discoveryTime > v.discoveryTime){
                        addEdgeToRelation(EdgeType.CROSS_EDGE , v);
                    }
                }
            }
        }




    }



}
