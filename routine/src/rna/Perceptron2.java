package rna;

import java.util.Random;

public class Perceptron2 {

    // The Perceptron2 stores its weights and learning constants.
    float[] weights;
    float c = 0.01F;

    public Perceptron2(int n) {
        weights = new float[n];
        // Weights start off random.
        for (int i = 0; i < weights.length; i++) {
            weights[i] = random(-1, 1);
            System.out.println("weights[" + i + "] = " + weights[i]);
        }
    }

    // Return an output based on inputs.
    public int feedforward(float[] inputs) {
        float sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += inputs[i] * weights[i];
        }
        System.out.println("sum = " + sum);
        return activate(sum);
    }

    // Output is a +1 or -1.
    private int activate(float sum) {
        if (sum > 0) {
            return 1;
        } else {
            return -1;
        }
    }

    // Train the network against known data.
    private void train(float[] inputs, int desired) {
        int guess = feedforward(inputs);
        float error = desired - guess;
        for (int i = 0; i < weights.length; i++) {
            weights[i] += c * error * inputs[i];
        }
    }

    private float random(int ini, int end) {
        Random r = new Random();
        return r.nextFloat();
    }
    
//    public static void main(String[] args) {
//        Perceptron2 p = new Perceptron2(3);
//        // The input is 3 values: x,y and bias.
//        float[] point = {50, -12, 1};
//        // The answer!
//        int result = p.feedforward(point);
//        System.out.println("result = " + (result > 1 ? true : false));        
//    }
}
