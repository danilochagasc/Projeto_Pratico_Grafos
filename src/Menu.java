import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Menu {
    private Scanner input;
    private Graph graph;

    public Menu() {
        input = new Scanner(System.in);
        graph = new Graph();
    }

    //show the menu and get the user input
    public void show() {
        int op;
        do {
            showOptions();
            op = Integer.parseInt(input.nextLine());
            startOption(op);
        } while (op != 5);
    }

    //show the options of the menu
    private void showOptions() {
        System.out.println("=========Menu de opções=========");
        System.out.println("1 - Inicializar Grafo");
        System.out.println("2 - Apresentar Lista de Adjacência");
        System.out.println("3 - Apresentar dados do grafo");
        System.out.println("4 - Apresentar Lista Recomendações de Produtos");
        System.out.println("5 - Sair");
        System.out.print("Escolha a sua opção: ");
    }

    //start the option chosen by the user
    private void startOption(int op) {
        switch (op) {
            case 1:
                initializeGraph();
                break;
            case 2:
                System.out.println(graph.toString());
                break;
            case 3:
                getData();
                break;
            case 4:
                getRecommendations(getUserInput());
                break;
            case 5:
                System.out.println("Saindo...");
                break;
            default:
                System.out.println("Escolha uma opção entre 1 e 4!");
        }
    }

    //initialize the graph with the data from the file
    private void initializeGraph() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/foodsMenor.txt"))) {
            String line;
            String currentUser = null;
            double currentRating = 0.0;
            String currentProduct = null;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("review/userId:")) {
                    currentUser = line.split(":")[1].trim();
                } else if (line.startsWith("product/productId:")) {
                    currentProduct = line.split(":")[1].trim();
                } else if (line.startsWith("review/score:")) {
                    currentRating = Double.parseDouble(line.split(":")[1].trim());
                    if (currentUser != null) {
                        graph.addUser(currentUser, currentProduct, currentRating);
                    }
                }
            }
            System.out.println("Grafo inicializado com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo!");
        }
    }

    //get the user input
    private String getUserInput() {
        System.out.print("Digite o Id do usuário: ");
        return input.nextLine();
    }

    //get the data from the graph
    private void getData() {
        System.out.println("Número de usuários: " + graph.getUserAdjacencyList().size());
        System.out.println("Número de produtos: " + graph.getProductAdjacencyList().size());
        System.out.println("Número de avaliações: " + graph.getUserAdjacencyList().values().stream().mapToInt(Map::size).sum());
    }

    //get the recommendations for the user
    public void getRecommendations(String userId) {
        List<String> recommendations = new ArrayList<>();

        //get the related users
        List<String> relatedUsers = getRelatedUsers(userId);

        //bfs to get the recommendations from the related users and their neighbors (products)
        Set<String> visitedProducts = new HashSet<>();
        Queue<String> queue = new LinkedList<>();

        for (String user : relatedUsers) {
            if (graph.getUserAdjacencyList().containsKey(user)) {
                queue.add(user);
                visitedProducts.add(user);

                while (!queue.isEmpty()) {
                    String currentUser = queue.poll();
                    Map<String, Double> products = graph.getUserAdjacencyList().get(currentUser);

                    for (Map.Entry<String, Double> entry : products.entrySet()) {
                        String product = entry.getKey();
                        double rating = entry.getValue();

                        //verify if the product is not already recommended and if the rating is greater than 4
                        if (!userId.equals(currentUser) && !visitedProducts.contains(product) && rating > 4.0) {
                            recommendations.add(product);
                            visitedProducts.add(product);
                        }

                        //add the neighbors of the product to the queue
                        if (graph.getProductAdjacencyList().containsKey(product)) {
                            Map<String, Double> neighbors = graph.getProductAdjacencyList().get(product);

                            for (String neighbor : neighbors.keySet()) {
                                if (!visitedProducts.contains(neighbor)) {
                                    queue.add(neighbor);
                                    visitedProducts.add(neighbor);
                                }
                            }
                        }
                    }
                }
            }
        }

        //print the recommendations
        System.out.println("Produtos recomendados para o usuário(com nota maior que 4) " + userId + ":");
        for (String product : recommendations) {
            System.out.println(product);
        }
    }

    //bfs to get the related users of the target user
    private List<String> getRelatedUsers(String targetUser) {
        List<String> relatedUsers = new ArrayList<>();
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        if(graph.getUserAdjacencyList().containsKey(targetUser)){
            queue.add(targetUser);
            visited.add(targetUser);

            while (!queue.isEmpty()){
                String currentUser = queue.poll();

                //add the related user to the list
                relatedUsers.add(currentUser);

                //get the products of the current user
                Map<String, Double> products = graph.getUserAdjacencyList().get(currentUser);

                //run through the products and add the related users to the queue
                for(Map.Entry<String, Double> entry : products.entrySet()){
                    String product = entry.getKey();

                    if(graph.getProductAdjacencyList().containsKey(product)){
                        Map<String,Double> users = graph.getProductAdjacencyList().get(product);

                        for (String relatedUser : users.keySet()){
                            if(!visited.contains(relatedUser)){
                                queue.add(relatedUser);
                                visited.add(relatedUser);
                            }
                        }
                    }
                }
            }
        }
        return relatedUsers;
    }

}
