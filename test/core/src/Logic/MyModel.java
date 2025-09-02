package Logic;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.DropoutLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.RmsProp;
import org.nd4j.linalg.lossfunctions.LossFunctions;

public class MyModel {

    public static void main(String[] args) {
        int inputSize = 350; // Update this based on your input dimension
        int outputSize = 1; // Assuming binary classification

        MultiLayerConfiguration config = new NeuralNetConfiguration.Builder()
            .seed(123)
            .updater(new RmsProp(0.005)) // Change the learning rate here
            .list()
            .layer(new DenseLayer.Builder()
                .nIn(inputSize)
                .nOut(64)
                .activation(Activation.RELU)
                .build())
            .layer(new DropoutLayer.Builder(0.3).build())
            .layer(new DenseLayer.Builder()
                .nOut(384)
                .activation(Activation.RELU)
                .build())
            .layer(new DropoutLayer.Builder(0.3).build())
            .layer(new DenseLayer.Builder()
                .nOut(384)
                .activation(Activation.RELU)
                .build())
            .layer(new DropoutLayer.Builder(0.3).build())
            .layer(new OutputLayer.Builder(LossFunctions.LossFunction.XENT)
                .activation(Activation.SIGMOID)
                .nOut(outputSize)
                .build())
            .build();

        MultiLayerNetwork model = new MultiLayerNetwork(config);
        model.init();

        // Print the summary of the model
        System.out.println(model.summary());

        // Train the model (replace X_train and Y_train with your training data)
        // model.fit(X_train, Y_train);

        // Save the model
        // model.save(new File("path/to/save/model.zip"));
    }
}
