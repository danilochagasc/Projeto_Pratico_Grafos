import java.util.*;

public class Graph {
    private Map<String, Map<String, Double>> userAdjacencyList;
    private Map<String, Map<String, Double>> productAdjacencyList;

    public Graph() {
        this.userAdjacencyList = new HashMap<>();
        this.productAdjacencyList = new HashMap<>();
    }

    //adds a user to the graph
    public void addUser(String user, String product, Double rating) {
        userAdjacencyList.putIfAbsent(user, new HashMap<>());
        userAdjacencyList.get(user).put(product, rating);

        //add the product to the product adjacency list if it doesn't exist
        productAdjacencyList.putIfAbsent(product, new HashMap<>());
        productAdjacencyList.get(product).put(user, rating);
    }

    //get the users that reviewed the same products as the user
    public Map<String, Double> getUserNeighbors(String user) {
        return userAdjacencyList.getOrDefault(user, Collections.emptyMap());
    }

    //get the products that were reviewed by the same users as the product
    public Map<String, Double> getProductNeighbors(String product) {
        return productAdjacencyList.getOrDefault(product, Collections.emptyMap());
    }

    //gets all the users(only users) in the graph
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String user : userAdjacencyList.keySet()) {
            sb.append("User ").append(user).append(": ");
            for (Map.Entry<String, Double> entry : userAdjacencyList.get(user).entrySet()) {
                sb.append(entry.getKey()).append(" (").append(entry.getValue()).append("), ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public Map<String, Map<String, Double>> getProductAdjacencyList() {
        return productAdjacencyList;
    }

    public Map<String, Map<String, Double>> getUserAdjacencyList() {
        return userAdjacencyList;
    }
}
