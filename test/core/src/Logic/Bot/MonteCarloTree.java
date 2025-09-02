package Logic.Bot;

import Logic.Card;

/**
 * MonteCarloTree class represents the Monte Carlo Tree used for the Monte Carlo Tree Search (MCTS) algorithm.
 */
public class MonteCarloTree {

    public MonteCarloNode rootNode; // Root node of the Monte Carlo Tree

    /**
     * Default constructor for the MonteCarloTree class.
     */
    public MonteCarloTree() {
    }

    /**
     * Search method to perform Monte Carlo Tree Search from the given root node.
     * @param rootNode The root node from which the search begins.
     * @return The selected card to play based on the search results.
     */
    public Card search(MonteCarloNode rootNode) {
        this.rootNode = rootNode;
        rootNode.expandNode(); // Expand the root node to generate child nodes

        // Perform rollout for each child node of the root node
        for (int i = 0; i < rootNode.getChildNodes().size(); i++) {
            rootNode.getChildNodes().get(i).RollOut(); // Simulate a rollout for the child node
            rootNode.incrementVisitCount(); // Increment the visit count of the root node
            rootNode.calcTotalScore(); // Calculate the total score of the root node
        }

        // Select the best child node based on the UCB1 formula and return the corresponding card
        if (selectBestChildNode() != null) {
            return selectBestChildNode().boardCard; // Return the card associated with the best child node
        }

        return null; // Return null if no best child node is found
    }

    /**
     * Selects the best child node of the root node based on the UCB1 formula.
     * @return The best child node selected based on the UCB1 formula.
     */
    public MonteCarloNode selectBestChildNode() {
        MonteCarloNode bestChild = null; // Initialize the best child node to null
        double bestUCB1Value = -1; // Initialize the best UCB1 value to a negative value

        // Iterate through each child node of the root node to calculate and compare their UCB1 values
        for (MonteCarloNode child : rootNode.getChildNodes()) {
            double UCB1Value = calculateUCB1Value(child); // Calculate the UCB1 value for the child node

            // Update the best child node and UCB1 value if a better child node is found
            if (UCB1Value > bestUCB1Value) {
                bestUCB1Value = UCB1Value; // Update the best UCB1 value
                bestChild = child; // Update the best child node
            }
        }

        return bestChild; // Return the best child node based on the UCB1 value
    }

    /**
     * Calculates the UCB1 value for the given node using the UCB1 formula.
     * @param node The node for which the UCB1 value needs to be calculated.
     * @return The calculated UCB1 value for the given node.
     */
    private double calculateUCB1Value(MonteCarloNode node) {
        // UCB1 formula: UCB1 = averageScore + explorationFactor * sqrt(log(totalVisits) / nodeVisitCount)
        double explorationFactor = 1.4; // Exploration factor used in the UCB1 formula
        double averageScore = (double) node.getTotalScore() / node.getVisitCount(); // Calculate the average score of the node
        double explorationTerm = explorationFactor * Math.sqrt(Math.log(rootNode.getVisitCount()) / node.getVisitCount()); // Calculate the exploration term

        return averageScore + explorationTerm; // Return the calculated UCB1 value
    }
}
