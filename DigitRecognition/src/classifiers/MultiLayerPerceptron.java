package classifiers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import dataObjects.DataSample;
import dataObjects.Feature;
import enums.DigitClass;
import interfaces.Algorithm;

public class MultiLayerPerceptron implements Algorithm {

	Float[] inputNeurons;
	Float[] hiddenNeurons;
	Float[] outputNeurons;
	Float[] weights;

	// Temp storage for weights
	Float[] tempWeights;
	// Used to store the previous weights before modification
	Float[] prWeigths;

	int inputN, outputN, hiddenN, hiddenL;

	List<Feature> allPossibleFeatures;

	public MultiLayerPerceptron(int hiddenN, int hiddenL) {
		this.hiddenN = hiddenN;
		this.hiddenL = hiddenL;
		initialSetup();
	}

	private void initialSetup() {

		outputN = 10; // For each 10 digit outputs possible
		inputN = 400; // # of Inputs per sample

		// Compute the size of the weights
		int weightsSize = inputN * hiddenN + (hiddenN * hiddenN * (hiddenL - 1)) + hiddenN * outputN;

		// Set initial size of the weights arraylist
		weights = new Float[weightsSize];

		// Set initial size all other list too
		inputNeurons = new Float[inputN];
		hiddenNeurons = new Float[hiddenN * hiddenL];
		outputNeurons = new Float[outputN];

		Random random = new Random();
		random.setSeed(System.currentTimeMillis());

		// Initialize all weights to a random value between -0.5 and 0.5
		for (int i = 0; i < weightsSize; i++) {

			weights[i] = (random.nextFloat() - 0.5f);
		}
	}

	// Get values for inputs
	public void populateInput(DataSample sample) {

		for (int i = 0; i < inputN; i++) {

			// Set the specific neuron to equal the data in the sample
			if (sample.getData().get(allPossibleFeatures.get(i)) > 0) {
				inputNeurons[i] = 1.0f;
			} else {
				inputNeurons[i] = 0.0f;
			}
		}
	}

	// Calculates the network. Prepares each of the nodes in the network to be
	// ready for training
	public void calculateNetwork() {

		// Go trough each of the hidden neurons in layer 1 and calculate their
		// values based on the input in them and the activation function
		for (int hidden = 0; hidden < hiddenN; hidden++) {

			// Neuron in layer 1
			hiddenNeurons[hidden] = 0.0f;

			// For each input in layer 1
			for (int input = 0; input < inputN; input++) {
				hiddenNeurons[hidden] += inputNeurons[input] * inputToHidden(input, hidden);
			}

			// Pass this layer through the Activation function go get a value
			hiddenNeurons[hidden] = sigmoid(hiddenNeurons[hidden]);

		}

		// For each of the other layers. Starts at 2 for easier to get
		// hiddenToHidden index
		for (int i = 2; i <= hiddenL; i++) {

			// For each of these layers go through it's neurons and calculate
			// their values
			for (int j = 0; j < hiddenN; j++) {

				// (i - 1) Since we start the for loop at i = 2 instead of i = 1
				hiddenNeurons[(i - 1) * hiddenN + j] = 0.0f;

				for (int k = 0; k < hiddenN; k++) {

					hiddenNeurons[(i - 1) * hiddenN + j] += hiddenNeurons[(i - 2) * hiddenN + k]
							* hiddenToHidden(i, k, j);

				}

				// Pass this layer through the Activation function go get a
				// value
				hiddenNeurons[(i - 1) * hiddenN + j] = sigmoid(hiddenNeurons[(i - 1) * hiddenN + j]);
			}

		}

		// Do same, now for hidden to Output neurons
		for (int i = 0; i < outputN; i++) {

			outputNeurons[i] = 0.0f;

			for (int j = 0; j < hiddenN; j++) {
				outputNeurons[i] += hiddenNeurons[(hiddenL - 1) * hiddenN + j] * hiddenToOutput(j, i);
			}

			outputNeurons[i] = sigmoid(outputNeurons[i]);
		}

	}

	// Trains the network. Changes the different weights in the network to find
	// ideal weights.
	// LeastMinimunError = Minimum value for how much error we accept before we
	// stop training the network to prevent over fitting but still have a good %
	public boolean trainNetwork(float teachingStep, float leastMinimumError, float momentum, List<DataSample> samples, int maxNumEpochs) {
		// The mean square error
		float mse = 999.0f;

		// Count for # of time we go from the input layer all the way to the
		// output layer while calculating the weights of the neurons
		int epochs = 1;
		float error = 0.0f;

		// Delta of the output layer
		float[] odelta = new float[outputN];

		// Delta of the hidden layer
		float[] hdelta = new float[hiddenN * hiddenL];

		// variable to keep track of certain weights
		tempWeights = Arrays.copyOf(weights, weights.length);
		prWeigths = Arrays.copyOf(weights, weights.length);

		int target = 0;

		// Store all possible features
		allPossibleFeatures = new ArrayList<Feature>(samples.get(0).getData().keySet());

		while (Math.abs(mse - leastMinimumError) > 0.0001f) {

			// Reset the mean square error for each iteration
			mse = 0.0f;

			// Populate the inputs For Each Sample
			for (DataSample sample : samples) {

				populateInput(sample);

				target = sample.getDigitClass().getValue();

				// Calculate the network weights
				calculateNetwork();

				// Now we want to train the weights by trying to get the weights
				// to the target value

				// For each output neuron
				for (int i = 0; i < outputN; i++) {
					// Get the delta of the output layer and the summed error
					if (i != target) {
						odelta[i] = (0.0f - outputNeurons[i]) * dersigmoid(outputNeurons[i]);
						error += (0.0f - outputNeurons[i]) * (0.0f - outputNeurons[i]);
					} else {
						odelta[i] = (1.0f - outputNeurons[i]) * dersigmoid(outputNeurons[i]);
						error += (1.0f - outputNeurons[i]) * (1.0f - outputNeurons[i]);
					}
				}

				// We not go back propagate into the hidden layer to get the
				// error of each neuron in each layer
				for (int i = 0; i < hiddenN; i++) {

					// get the values from the last previous hidden layer
					hdelta[(hiddenL - 1) * hiddenN + i] = 0.0f;

					// Add to the delta for each connection there is with an
					// output neuron
					for (int j = 0; j < outputN; j++) {
						hdelta[(hiddenL - 1) * hiddenN + i] += odelta[j] * hiddenToOutput(i, j);
					}

					hdelta[(hiddenL - 1) * hiddenN + i] *= dersigmoid(hiddenAt(hiddenL, i));
				}

				// Do the same for each other hidden layer
				for (int i = hiddenL - 1; i < 0; i--) {

					// Add to each neuron's their hidden delta
					for (int j = 0; j < hiddenN; j++) {

						// Get the values from the previous iteration
						hdelta[(i - 1) * hiddenN + j] = 0.0f;

						for (int k = 0; k < hiddenN; k++) {
							hdelta[(i - 1) * hiddenN + j] += hdelta[i * hiddenN + k] * hiddenToHidden(i + 1, j, k);
						}

						hdelta[(i - 1) * hiddenN + j] *= dersigmoid(hiddenAt(i, j));

					}

				}

				// Store the weights for later use
				tempWeights = Arrays.copyOf(weights, weights.length);

				// From hidden to input weights
				for (int i = 0; i < inputN; i++) {

					for (int j = 0; j < hiddenN; j++) {
						weights[inputN * j + i] += (momentum * (inputToHidden(i, j) - prevInputToHidden(i, j)))
								+ (teachingStep * hdelta[j] * inputNeurons[i]);
					}

				}

				// Calculating all the weights from the Hidden to hidden layer
				for (int i = 2; i <= hiddenL; i++) {

					for (int j = 0; j < hiddenN; j++) {

						for (int k = 0; k < hiddenN; k++) {
							weights[inputN * hiddenN + ((i - 2) * hiddenN * hiddenN) + hiddenN * j + k] += (momentum
									* (hiddenToHidden(i, j, k) - prevHiddenToHidden(i, j, k)))
									+ (teachingStep * hdelta[(i - 1) * hiddenN + k] * hiddenAt(i - 1, j));
						}
					}
				}

				// Last From Hidden layer to output layer, calculate the weights
				for (int i = 0; i < outputN; i++) {
					for (int j = 0; j < hiddenN; j++) {
						weights[(inputN * hiddenN + (hiddenL - 1) * hiddenN * hiddenN + j * outputN + i)] += (momentum
								* (hiddenToOutput(j, i) - prevHiddenToOutput(j, i)))
								+ (teachingStep * odelta[i] * hiddenAt(hiddenL, j));
					}

				}

				prWeigths = Arrays.copyOf(tempWeights, tempWeights.length);

				// add the total error
				mse += error / (outputN + 1f);
				// Reset error
				error = 0.0f;
			}

			// Don't do more than 1000 epochs so no over fitting
			if (epochs > maxNumEpochs)
				break;

			System.out.print(" Epoch done: " + epochs);
			
			// Finished an epoch
			epochs++;
		}

		return true;
	}

	// Takes the input data and tries to get an answer. The main Classification
	// method to call
	public DigitClass recallNetwork(DataSample sample) {

		// Populate the input neurons
		populateInput(sample);

		// calculate the network weights
		calculateNetwork();

		// Now find the best output neuron. The values pro the probabilities are contained in the
		// output neurons. The activation function has already been applied. the
		// best one is the biggest.
		float highestProbability = 0;
		int index = 0;

		// Find the most fitting output
		for (int i = 0; i < outputN; i++) {

			if (outputNeurons[i] > highestProbability) {

				highestProbability = outputNeurons[i];
				index = i;
			}

		}

		// Output result
		System.out.println("The neural network thinks the image is : " + index + " When it should of been: " + sample.getDigitClass());

		System.out.println("0 with " + (int) (outputNeurons[0] * 100) + " probability \n\r" + "1 with "
				+ (int) (outputNeurons[1] * 100) + " probability \n\r" + "2 with " + (int) (outputNeurons[2] * 100)
				+ " probability \n\r" + "3 with " + (int) (outputNeurons[3] * 100) + " probability \n\r" + "4 with "
				+ (int) (outputNeurons[4] * 100) + " probability \n\r" + "5 with " + (int) (outputNeurons[5] * 100)
				+ " probability \n\r" + "6 with " + (int) (outputNeurons[6] * 100) + " probability \n\r" + "7 with "
				+ (int) (outputNeurons[7] * 100) + " probability \n\r" + "8 with " + (int) (outputNeurons[8] * 100)
				+ " probability \n\r" + "9 with " + (int) (outputNeurons[9] * 100) + " probability \n\r");

		return DigitClass.values()[index];
	}

	// Returns weights at different indexes that are important and require some
	// calculations
	private float inputToHidden(int inp, int hid) {
		return weights[inputN * hid + inp];
	}

	private float hiddenAt(int layer, int hid) {
		return hiddenNeurons[(layer - 1) * hiddenN + hid];
	}

	// Returns weights at different indexes that are important and require some
	// calculations
	private float hiddenToHidden(int toLayer, int fromHid, int toHid) {
		return weights[inputN * hiddenN + ((toLayer - 2) * hiddenN * hiddenN) + hiddenN * fromHid + toHid];
	}

	// Returns weights at different indexes that are important and require some
	// calculations
	private float hiddenToOutput(int hid, int out) {
		return weights[inputN * hiddenN + (hiddenL - 1) * hiddenN * hiddenN + hid * outputN + out];
	}

	private float prevInputToHidden(int inp, int hid) {
		return prWeigths[inputN * hid + inp];
	}

	private float prevHiddenToHidden(int toLayer, int fromHid, int toHid) {
		return prWeigths[inputN * hiddenN + ((toLayer - 2) * hiddenN * hiddenN) + hiddenN * fromHid + toHid];
	}

	private float prevHiddenToOutput(int hid, int out) {
		return prWeigths[inputN * hiddenN + (hiddenL - 1) * hiddenN * hiddenN + hid * outputN + out];
	}

	// Activaion Functions (Functions that normalize output and gives a result
	private float sigmoid(float value) {
		return (float) (1f / (1f + Math.exp(-value)));
	}

	private float dersigmoid(float value) {
		return (value * (1f - value));
	}

	@Override
	public void train(List<DataSample> trainingSet) {
		//Takes in Teaching step, least minimum error , momentum
		if(!trainNetwork(0.04f, 0.01f, 0.4f, trainingSet, 50))
				System.out.println("Something went wrong while training");
	}

	@Override
	public DigitClass classify(DataSample ds) {
		return recallNetwork(ds);
	}

	@Override
	public String getName() {
		return "Multilayer Perceptron";
	}
	
	
	
	

}
