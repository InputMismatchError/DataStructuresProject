package DataStructuresProject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

class Pair{

    String city;
    int distance;

    Pair(String city,int distance){
        this.city = city;
        this.distance = distance;
    }


} 
class Graph {


    // We use Hashmap to store the edges in the graph
    public Map<String, List<Pair> > map = new LinkedHashMap<>();

    public Graph(String path) throws IOException{

        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.ISO_8859_1))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                for(int i = 0 ; i < values.length ; i ++){
                    values[i] = values[i].replace((char)160 , ' ').trim();}
                records.add(Arrays.asList(values));
            }
        }

        for(String i : records.get(0)){
            if(i != "")
                addVertex(i);
        }        

        for(int i = 1 ; i < records.size() ; i ++)
            for(int j = i+1 ; j < records.get(i).size() ; j++){
                if(Integer.parseInt(records.get(i).get(j)) == 99999)
                    continue;
                addEdge(records.get(0).get(i), records.get(0).get(j), Integer.parseInt(records.get(i).get(j)));}

    }
 
 
    // This function adds a new vertex to the graph
    public void addVertex(String s)
    {
        map.put(s, new LinkedList<Pair>());
    }
 
    // This function adds the edge
    // between source to destination
    public void addEdge(String source,
                        String destination,
                        int distance)
    {
 
        if (!map.containsKey(source))
            addVertex(source);
 
        if (!map.containsKey(destination))
            addVertex(destination);
 
        map.get(source).add(new Pair(destination,distance));
        map.get(destination).add(new Pair(source,distance));
    }
    
    // This function gives the count of vertices
    public void getVertexCount()
    {
        System.out.println("The graph has "
                           + map.keySet().size()
                           + " vertex");
    }
 
    // This function gives the count of edges
    public void getEdgesCount()
    {
        int count = 0;
        for (String v : map.keySet()) {
            count += map.get(v).size();
        }

        System.out.println("The graph has "
                           + count/2
                           + " edges.");
    }
 
    // This function gives whether
    // a vertex is present or not.
    public void hasVertex(String s)
    {
        if (map.containsKey(s)) {
            System.out.println("The graph contains "
                               + s + " as a vertex.");
        }
        else {
            System.out.println("The graph does not contain "
                               + s + " as a vertex.");
        }
    }
 
    // This function gives whether an edge is present or not.
    public void hasEdge(String s, String d)
    {

        Iterator<Pair> it = map.get(s).iterator();
        while(it.hasNext()){
            if (it.next().city.equals(d)) {
                System.out.println("The graph has an edge between "+ s + " and " + d + ".");
                return;
        }
        }
        System.out.println("The graph has no edge between "+ s + " and " + d + ".");
        return;
        
    }
 
    // Prints the adjancency list of each vertex.
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        Pair w;

        for (String v : map.keySet()) {
            builder.append(v.toString() + ": ");
            Iterator<Pair> it = map.get(v).iterator();
            while(it.hasNext()){
                w = it.next();
                builder.append(w.city.toString() + "("+w.distance+" km)" + " ");
            }
            builder.append("\n");
        }
 
        return (builder.toString());
    }
}

class BFS {
    Graph graph;

    public BFS(Graph x) {
        this.graph = x;
    }

    public PathInfo shortestPath(String source, String dest) {
        Map<String, String> parentMap = new HashMap<>();
        Map<String, Integer> distanceMap = new HashMap<>();
        Queue<String> queue = new LinkedList<>();

        queue.offer(source);
        parentMap.put(source, null);
        distanceMap.put(source, 0);

        while (!queue.isEmpty()) {
            String current = queue.poll();

            if (current.equals(dest)) {
                return reconstructPathAndDistance(parentMap, distanceMap, source, dest);
            }

            for (Pair neighbor : graph.map.get(current)) {
                String neighborCity = neighbor.city;
                int distanceToNeighbor = neighbor.distance;

                if (!parentMap.containsKey(neighborCity)) {
                    queue.offer(neighborCity);
                    parentMap.put(neighborCity, current);
                    distanceMap.put(neighborCity, distanceMap.get(current) + distanceToNeighbor);
                }
            }
        }

        return null; // No path exists
    }

    private PathInfo reconstructPathAndDistance(Map<String, String> parentMap, Map<String, Integer> distanceMap, String source, String dest) {
        List<String> path = new ArrayList<>();
        int totalDistance = distanceMap.get(dest);
        String current = dest;

        while (current != null) {
            path.add(0, current);
            current = parentMap.get(current);
        }

        return new PathInfo(path, totalDistance);
    }

    public static class PathInfo {
        List<String> path;
        int totalDistance;

        public PathInfo(List<String> path, int totalDistance) {
            this.path = path;
            this.totalDistance = totalDistance;
        }

        public List<String> getPath() {
            return path;
        }

        public int getTotalDistance() {
            return totalDistance;
        }
    }
}
// Driver Code
public class Main {
 
    public static void main(String args[]) throws IOException
    {
        Graph cityMap = new Graph("sa.csv");

        // Printing the graph
        System.out.println("Graph:\n"+ cityMap.toString());

        BFS search = new BFS(cityMap);

        BFS.PathInfo shortestPathInfo = search.shortestPath("Batman", "Izmir");

        if (shortestPathInfo != null) {
            System.out.println("Shortest path: " + shortestPathInfo.getPath());
            System.out.println("Total distance: " + shortestPathInfo.getTotalDistance() + " km");
        } else {
            System.out.println("No path found between the cities.");
        }

    
}
}
