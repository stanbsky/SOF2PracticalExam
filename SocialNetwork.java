import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

public class SocialNetwork {
    String name; // The name of the social network.
    Map<String, User> users; // Contains the social network's users. Keys are the
                             // user'ids, values are the User instances.

    /**
     * Construct an instance of SocialNetwork.
     * 
     * @param name
     *            the name of the user
     */
    public SocialNetwork(String name) {
        this.name = name;
        this.users = new HashMap<String, User>();
    }

    /**
     * Add a user to the social network. Return User if the operation is
     * successful, otherwise throws IllegalArgumentException (if the user already
     * exists).
     *
     * @param id
     *            the user's id to be added to the social network instance.
     * @param name
     *            the name of the user.
     * @return User if the operation is successful, throws exception otherwise.
     * @throws IllegalArgumentException
     *            thrown if the user already exists in this social network instance.
     */
    public User createUser(String id, String name) {
        if (this.users.containsKey(id)) {
            throw new IllegalArgumentException();
        }
        User newUser = new User(id, name);
        this.users.put(id, newUser);
        return newUser;
    }

    /**
     * Returns the user matching the unique identifier provided. If user is
     * not found, throws IllegalArgumentException.
     * 
     * @param id
     *            the user'id to be retrieved as a User instance.
     * @return the User instance corresponding to the user'id.
     * @throws IllegalArgumentException thrown if the user does not exist in
     *            this social network instance.
     */
    public User getUser(String id) {
        User user = this.users.get(id);
        if (user != null) {
            return user;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Add a connection between the two users specified by their user'ids. Return
     * true if the operation is successful, false otherwise (if the user already
     * part of his/her circle of connection). If either of the users are not
     * registered in the social network instance, throws an IllegalArgumentException.
     * 
     * @param userOneID
     *            the first user of the pair forming a connection.
     * @param userTwoID
     *            the second user of the pair forming a connection.
     * @return true if the operation is successful, false otherwise (e.g. either
     *         user is already in a connection with the other).
     * @throws IllegalArgumentException
     *            thrown if either of the users is not registered in the social
     *            network instance.
     */
    public boolean addRelationship(String userOneID, String userTwoID) {
        User userOne = this.users.get(userOneID);
        User userTwo = this.users.get(userTwoID);
        if (userOne == null || userTwo == null) {
            throw new IllegalArgumentException();
        }
        if (!userOne.addConnection(userTwoID)) {
            return false;
        }
        if (!userTwo.addConnection(userOneID)) {
            return false;
        }
        return true;
    }

    /**
     * Calculates and returns the normalised closeness centrality measure of the
     * user specified by the userID provided. If the user doesn't exist in the 
     * social network instance, throws IllegalArgumentException.
     * 
     * <p>The normalised closeness centrality value is given by the formula
     * C(x) = (N-1) / Sum_y d(y,x), where C(x) is the closeness of x, d(y,x) is
     * the shortest distance between y and x (in the number of edges between them),
     * and N is the total number of users in the social network instance.
     * 
     * @param userID
     *            the user'id of the user to be measured for centrality.
     * @return the normalised closeness centrality measure of the user specified.
     * @throws IllegalArgumentException thrown if the user specified is not
     *            registered in the social network instance.
     */
    public double closeness(String userID) {
        User source = this.users.get(userID);
        if (source == null) {
            throw new IllegalArgumentException();
        }
        Map<String, Integer> distances = this.getDistances(userID);
        int distanceSum = distances.values().stream().mapToInt(Integer::valueOf).sum();
        return (double) (this.users.size() - 1) / (double) distanceSum;
    }

    private Map<String, Integer> getDistances(String source) {
        // List of nodes queued to be visited
        Queue<String> bfsQueue = new LinkedList<String>();
        // Keeps track of which nodes have already been visited
        Map<String, Boolean> visited = (Map<String, Boolean>) this.users.entrySet()
        .stream().collect(Collectors.toMap(Map.Entry::getKey, v -> false));
        // Keeps track of distances between nodes
        Map<String, Integer> distances = (Map<String, Integer>) this.users.entrySet()
        .stream().collect(Collectors.toMap(Map.Entry::getKey, v -> 0));

        // Add source node (user to be measured), label as visited, distance 0
        bfsQueue.add(source);
        visited.put(source, true);
        distances.put(source, 0);
        
        // Conduct BFS of the network and calculate the distances
        // between the user of interest and the rest
        while (!bfsQueue.isEmpty()) {
            String current = bfsQueue.remove();
            for (String connection : this.getUser(current).getConnections()) {
                if (!visited.get(connection)) {
                    visited.put(connection, true);
                    bfsQueue.add(connection);
                    distances.put(connection, distances.get(current) + 1);
                }
            }
        }

        return distances;
    }

}