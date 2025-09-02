package Logic;

import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.DataSetPreProcessor;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.iterator.StandardScaler;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import com.badlogic.gdx.scenes.scene2d.ui.List;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.DoubleStream;

public class NeuralNetwork {

        private MultiLayerNetwork model;
    private int numInputs; // Number of input nodes

    public NeuralNetwork(int numInputs) {
        this.numInputs = numInputs;
        this.model = createModel();
    }

    private MultiLayerNetwork createModel() {
        int numOutputs = 2; // Probability of winning
        int numHiddenNodes = 100; // Example value

        NeuralNetConfiguration.ListBuilder listBuilder = new NeuralNetConfiguration.Builder()
                .updater(new Adam(0.001))
                .weightInit(WeightInit.XAVIER)
                .list();

        listBuilder.layer(0, new DenseLayer.Builder().nIn(numInputs).nOut(numHiddenNodes)
                .activation(Activation.RELU)
                .build());

        // Add more hidden layers as needed
        listBuilder.layer(1, new DenseLayer.Builder().nIn(numHiddenNodes).nOut(numHiddenNodes)
                .activation(Activation.RELU)
                .build());

        listBuilder.layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.XENT)
                .activation(Activation.SIGMOID)
                .nIn(numHiddenNodes).nOut(numOutputs).build());

        MultiLayerNetwork model = new MultiLayerNetwork(listBuilder.build());
        model.init();
        model.setListeners(new ScoreIterationListener(10));
        return model;
    }

    public void train(String gameStatesFilePath, String winnersFilePath) {
        try (ObjectInputStream oisGameStates = new ObjectInputStream(new FileInputStream(gameStatesFilePath));
             ObjectInputStream oisWinners = new ObjectInputStream(new FileInputStream(winnersFilePath))) {

            ArrayList<ArrayList<int[]>> gameStates = (ArrayList<ArrayList<int[]>>) oisGameStates.readObject();
            ArrayList<Integer> winners = (ArrayList<Integer>) oisWinners.readObject();

            ArrayList<double[]> inputList = new ArrayList<>();
            ArrayList<double[]> outputList = new ArrayList<>();


            for (int i = 0; i < gameStates.size(); i++) {
                ArrayList<int[]> gameStateList = gameStates.get(i);
                
                System.out.println("size of game states : " + gameStates.size());
                System.out.println("size of game gameStateList: " + gameStateList.size());
                System.out.println("size of int[]: " + gameStateList.get(0).length);

                double[] inputArray = flattenGameStates(gameStateList);
                double[] outputArray = {(double) winners.get(i)};

                inputList.add(inputArray);

                // for (int j = 0; j < inputArray.length; j++) {
                //     System.out.print(inputArray[j] + " ");
                // }
                // System.out.println();

                outputList.add(outputArray);
            }

            // Check if all input arrays have the same length
            int expectedLength = inputList.get(0).length; // Assuming at least one element in the list

            for (double[] arr : inputList) {

                //System.out.println("Input array lengths: " + arr.length + " (expected: " + expectedLength + ")");

                if (arr.length != expectedLength) {
                    throw new IllegalArgumentException("Input arrays must have the same length.");
                }

            }

            //why 2d array
            INDArray input = Nd4j.create(inputList.toArray(new double[0][]));
            INDArray output = Nd4j.create(outputList.toArray(new double[0][]));

            DataSet dataSet = new DataSet(input, output);
            
            StandardScaler scaler = new StandardScaler();
            scaler.fit(dataSet);
            scaler.transform(dataSet);

            DataSetIterator iterator = new org.nd4j.linalg.dataset.api.iterator.DataSetIterator() {

                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public DataSet next() {
                    return null;
                }

                @Override
                public void remove() {
                }

                @Override
                public void forEachRemaining(Consumer<? super DataSet> action) {
                }

                @Override
                public DataSet next(int num) {
                    return null;
                }

                @Override
                public int inputColumns() {
                    return 0;
                }

                @Override
                public int totalOutcomes() {
                    return 0;
                }

                @Override
                public boolean resetSupported() {
                    return false;
                }

                @Override
                public boolean asyncSupported() {
                    return false;
                }

                @Override
                public void reset() {

                }

                @Override
                public int batch() {
                    return 0;
                }

                @Override
                public void setPreProcessor(DataSetPreProcessor preProcessor) {

                }

                @Override
                public DataSetPreProcessor getPreProcessor() {
                    return null;
                }

                @Override
                public ArrayList<String> getLabels() {
                    return null;
                }
            };

            for (int i = 0; i < 5; i++) { // Adjust the number of epochs
                model.fit(iterator);
                System.out.println("Completed epoch " + i);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private double[] flattenGameStates(ArrayList<int[]> gameStateList) {
        // Find the maximum turn number across all games
    
        double[] flattenedArray = new double[350 * 25];
        int currentIndex = 0;

        // Flatten the 2D array into a 1D array, padding with zeros
            for (int[] turn : gameStateList) {
                for (int i = 0; i < turn.length; i++) {
                    flattenedArray[currentIndex++] = turn[i];
                }
                // Pad with zeros to make each turn array the same length
                for (int i = turn.length; i < 25; i++) {
                    flattenedArray[currentIndex++] = 0;
                }
            }
        
        return flattenedArray;
    }
    

    public double predictProbability(double[] gameState) {
        INDArray input = Nd4j.create(gameState);
        INDArray output = model.output(input);
        return output.getDouble(0);
    }

    public static void main(String[] args) {
        NeuralNetwork neuralNetwork = new NeuralNetwork(27);

        // Train the model using saved .ser files
        String gameStatesFilePath = "gameStates.ser";
        String winnersFilePath = "winners.ser";
        neuralNetwork.train(gameStatesFilePath, winnersFilePath);

        // Example usage
        double[] gameState = {0, 1, 50, 104, 25, 0, 0, 1, 1, 0, 0, 0, 2, 0, 0, 0, 1, 1, 0, 0, 0, 2, 0, 3, 1,
        0, 1};
        double probability = neuralNetwork.predictProbability(gameState);
        System.out.println("Probability of winning: " + probability);
    }
}