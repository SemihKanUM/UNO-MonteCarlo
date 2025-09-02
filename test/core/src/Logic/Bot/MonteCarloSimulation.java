// package Logic.Bot;


// import java.util.List;
// import Logic.Board;
// import Logic.Card;
// import Logic.State;

// public class MonteCarloSimulation {

//     private MonteCarloNode rootNode;
//     private static final int MAX_DEPTH = 50; // Adjust this based on your needs


//     public MonteCarloSimulation() {}

//     public void setRootNode(Board board){
//         rootNode = new MonteCarloNode(board);
//     }

//     public Card findBestAction() {

//         // System.out.println("------");
//         int size = rootNode.getStateBoard().getCurrentPlayer().getDeck().size();

//         run(); // Run the simulation

//         int size2 = rootNode.getStateBoard().getCurrentPlayer().getDeck().size();

         
//         MonteCarloNode bestChild = selectBestChildNode(rootNode);

//         // System.out.println("----");

//         if(bestChild == null){
//             return null;
//         }

//         //System.out.println(bestChild.getStateBoard().getCurrentIndex());

//         int[] treeStatistics = getTreeStatistics(rootNode);

//         // System.out.println("Total Nodes: " + treeStatistics[0]);
//         // System.out.println("Total Visits: " + treeStatistics[1]);

//         return bestChild.getStateBoard().playerChoosenCard;
//     }

//     private void run(){
//         int score = simulate(rootNode,0);
//         rootNode.addToTotalScore(score);
//     }

//     private int simulate(MonteCarloNode node,int depth) {

//         if (depth >= MAX_DEPTH) {
//             // Return a default or terminal value when the maximum depth is reached
//             return 0; // Adjust this based on your needs
//         }

//         if (isGameEnd(node.getStateBoard())) {
//             // Game has reached its end, return the game result or score
//             return calculateGameResult(node.getStateBoard());
//         }

//         if (node.getChildNodes().isEmpty()) {
//             // Node is a leaf, expand and simulate from a child node
//             node.expandNode();
            
//             MonteCarloNode selectedChild = selectBestChildNode(node);  
//             int score = simulate(selectedChild,depth + 1);
//             node.incrementVisitCount();
//             node.addToTotalScore(score);
//             return score;
//         } else {
//             // Node is not a leaf, select and simulate from a child node
//             MonteCarloNode selectedChild = selectBestChildNode(node);
//             int score = simulate(selectedChild,depth + 1);
//             node.incrementVisitCount();
//             node.addToTotalScore(score);
//             return score;
//         }
//     }

//     // Other methods for tree traversal, selection, expansion, and backpropagation
//     public MonteCarloNode selectBestChildNode(MonteCarloNode node) {
//         // Use UCB1 formula to select the best child node
//         MonteCarloNode bestChild = null;
//         double bestUCB1Value = -1;

//         for (MonteCarloNode child : node.getChildNodes()) {
//             double UCB1Value = calculateUCB1Value(child);

//             if (UCB1Value > bestUCB1Value) {
//                 bestUCB1Value = UCB1Value;
//                 bestChild = child;
//             }
//         }
//         //System.out.println(bestChild == null);
//         return bestChild;
//     }

//     private double calculateUCB1Value(MonteCarloNode node) {
//         // Implement the UCB1 formula
//         // UCB1 = averageScore + explorationFactor * sqrt(log(totalVisits) / nodeVisitCount)
//         // You may need to handle cases where nodeVisitCount is zero separately

//         double explorationFactor = 1.4; // You can experiment with different values
//         double averageScore = (double) node.getTotalScore() / node.getVisitCount();
//         double explorationTerm = explorationFactor * Math.sqrt(Math.log(rootNode.getVisitCount()) / node.getVisitCount());

//         return averageScore + explorationTerm;
//     }

//     public MonteCarloNode getRootNode() {
//         return rootNode;
//     }

//     public void setRootNode(MonteCarloNode rootNode) {
//         this.rootNode = rootNode;
//     }

//     private boolean isGameEnd(Board board) {
//         // Implement the condition to check if the game has ended
//         // This might involve checking for a win/loss condition or reaching a specific game state
//         // Return true if the game has ended, false otherwise
//         // Example: return board.isGameOver();
//         // Example: return board.getRoundCount() >= MAX_ROUNDS;
//         // ...
//         return board.endGame;
//     }
    
//     private int calculateGameResult(Board board) {
//         // Implement the logic to calculate the game result or score
//         // This might involve evaluating the final game state and assigning a score
//         // Return the calculated score
//         // Example: return board.calculateScore();
//         // ...

//         int score = 0;
//         for (int i = 0; i < board.getPlayers().size(); i++) {
//             if(board.getPlayers().get(i).getNumber() == 2){
//                 score = board.getPlayers().get(i).getScore();
//             }
//         }
        
//         return score;
//     }

//     public int[] getTreeStatistics(MonteCarloNode node) {
//         if (node == null) {
//             return new int[]{0, 0}; // [TotalNodes, TotalVisits]
//         }
    
//         int totalNodes = 1; // Count the current node
//         int totalVisits = node.getVisitCount();
    
//         // Recursively count child nodes and visits
//         for (MonteCarloNode child : node.getChildNodes()) {
//             int[] childStatistics = getTreeStatistics(child);
//             totalNodes += childStatistics[0];
//             totalVisits += childStatistics[1];
//         }
    
//         return new int[]{totalNodes, totalVisits};
//     }

// }