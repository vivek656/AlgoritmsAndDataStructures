package datastructures.graph;


import java.util.*;

/**
 * graph is a collection of ordered pair , V -> vertices
 * (u,v) one orderpair is called edge
 * Directed Graph
 */
public class Graph<T> {

    /**
     * maintaining a map , with vertices and their outgoing vertices .
     */
    private final HashMap<T , Set<GraphEdge<T,T>>> adjacencyMap;



    public static <E> Graph<E> fromPairs(List<E[]> pairs){
        return new Graph<>(pairs);
    }
    private Graph (){
        adjacencyMap = new HashMap<>();
    }

    private Graph(List<T[]> pairs){
        this();
        for(T[] pair : pairs){
            if(!adjacencyMap.containsKey(pair[0])) adjacencyMap.put(pair[0] , new LinkedHashSet<>());
            adjacencyMap.get(pair[0]).add(new GraphEdge<>(pair[0], pair[1]));
        }
    }

    /**
     * adjPlus mean outgoing vertices, that represented by set.
     * @param u
     * @return
     */
    private Set<GraphEdge<T,T>> adjPlus(T u){
        return this.adjacencyMap.getOrDefault(u , new LinkedHashSet<>());
    }

    /**
     * BFS , returns a level set and a parent map ,
     * BFS , query all vertices that a one level above it .
     * i.e. we will discover each vertex that is d distance reachable from s.
     * before discovering all vertex which are d+1 distance reachable
     * @return [BFSOutput] , a objcet wrapper that contains levelset , graph and parentmap
     */

    public BFSOutput<T> breadthFirstSearch(T s){
        var parent = new HashMap<T,T>();
        parent.put(s,s);
        var levels = new ArrayList<LinkedList<T>>();
        levels.add(new LinkedList<>(List.of(s)));
        while(!levels.get(levels.size()-1).isEmpty() ){
            levels.add(new LinkedList<>());
            for (T u  : levels.get(levels.size()-2)){
                for(GraphEdge<T,T> edge : adjPlus(u))
                    parent.computeIfAbsent(edge.end, a -> {
                        levels.get(levels.size() - 1).add(edge.end);
                        return u;
                    });
            }
        }
        return new BFSOutput<>(this, parent, levels);
    }

    public List<T> unweightedShortestPath(T s, T t){
        var parent = breadthFirstSearch(s).parent;
        if(parent == null || parent.isEmpty() || !parent.containsKey(t)) return Collections.emptyList();
        var i = t;

        LinkedList<T> path = new LinkedList<>();
        path.addFirst(t);
        while(i!=s){
            i = parent.get(i);
            path.addFirst(i);
        }
        return path;
    }

    abstract static class SearchOutput<T>{
        Graph<T> graph;
        public final Map<T,T> parent;

        protected SearchOutput(Graph<T> graph, Map<T, T> parentMap) {
            this.graph = graph;
            this.parent = parentMap;
        }

        public List<T> unweightedShortestPath(T s, T t){
            if(parent == null || parent.isEmpty() || !parent.containsKey(t)) return Collections.emptyList();
            var i = t;
            LinkedList<T> path = new LinkedList<>();
            path.addFirst(t);
            while(i!=s && (i != parent.get(i))){
                i = parent.get(i);
                path.addFirst(i);
            }
            if(i!=s)return Collections.emptyList();
            return path;
        }
    }

    public static class BFSOutput<T> extends SearchOutput<T>{

        public List<LinkedList<T>> levels;

        public BFSOutput(Graph<T> graph, Map<T, T> parentMap, List<LinkedList<T>> levels) {
            super(graph,parentMap);
            this.levels = levels;
        }
        public BFSOutput(Graph<T> graph) {
            this(graph,new HashMap<>(),new ArrayList<>());
        }
    }

    public List<List<T>> asListOfEdges(){
        var res = new  LinkedList<List<T>>();
        for(Map.Entry<T,Set<GraphEdge<T,T>>> entry : this.adjacencyMap.entrySet()){
            entry.getValue().forEach(val -> res.add(List.of(entry.getKey() , val.end)));
        }
        return res;
    }

    public Map<T , Set<GraphEdge<T,T>>> asAdjacencyMap(){
        return Map.copyOf(adjacencyMap);
    }


    @Override
    public String toString() {
        return "Graph{" +
                "adjacencyMap=" + adjacencyMap +
                '}';
    }
}
