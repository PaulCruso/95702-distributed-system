/**
 * Author: Kaizhong Ying
 * Andrew ID: kying
 * Last Modified: February 23, 2024
 *
 * NeuralNetworkServer.java serves as the proxy server to process the data from client
 * Using the similar function as NeuralNetwork.java
 * Generate the Json output to client
 */
import com.google.gson.Gson;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class NeuralNetworkServer {
    private static final int PORT = 6789;
    // Initial neuralNetwork as in NeuralNetwork.java

    private static NeuralNetwork neuralNetwork = new NeuralNetwork(2, 5,
            1, null, null, null, null);
    private static final Gson gson = new Gson();
    private static Random rand = new Random();
    // Initial training sets as in NeuralNetwork.java
    private static List<Double[][]> userTrainingSets = Arrays.asList(
            new Double[][]{{0.0, 0.0}, {0.0}},
            new Double[][]{{0.0, 1.0}, {0.0}},
            new Double[][]{{1.0, 0.0}, {0.0}},
            new Double[][]{{1.0, 1.0}, {0.0}}
    );

    // Implementation includes creating a menu, preparing operations,
    // sending requests, and displaying responses.
    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println(received);
                NeuralNetworkOperation operation = gson.fromJson(received, NeuralNetworkOperation.class);

                String jsonResponse = performOperation(operation);
                System.out.println(jsonResponse);
                buffer = jsonResponse.getBytes();
                DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length,
                        packet.getAddress(), packet.getPort());
                socket.send(responsePacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Processes the received operation and generates a response.
     * @param operation The operation to be processed.
     * @return A JSON string representing the response.
     */
    private static String performOperation(NeuralNetworkOperation operation) {
        NeuralNetworkResponse response = new NeuralNetworkResponse();
        response.response = operation.request;
        response.status = "OK";

        switch (operation.request) {
            case "getCurrentRange":
                response.val1 = userTrainingSets.get(0)[1][0];
                response.val2 = userTrainingSets.get(1)[1][0];
                response.val3 = userTrainingSets.get(2)[1][0];
                response.val4 = userTrainingSets.get(3)[1][0];
                break;
            case "setCurrentRange":
                setTruthTable(operation.inputs);
                break;
            case "trainSingleStep":
                response.totalError = trainSingleStep();
                break;
            case "train":
                response.totalError = trainNSteps(operation.iterations);
                break;
            case "test":
                response.val1 = test(operation.inputs);
                break;
            default:
                response.status = "Error";
                break;
        }

        return gson.toJson(response);
    }
    // These four function is similar to the NeuralNetwork.java
    // And I use these as blackbox
    private static double trainSingleStep() {
        int randomChoice = rand.nextInt(userTrainingSets.size());
        List<Double> inputs = Arrays.asList(userTrainingSets.get(randomChoice)[0]);
        List<Double> outputs = Arrays.asList(userTrainingSets.get(randomChoice)[1]);
        neuralNetwork.train(inputs, outputs);
        return neuralNetwork.calculateTotalError(new ArrayList<>(userTrainingSets));
    }

    private static double trainNSteps(int n) {
        double totalError = 0;
        for (int i = 0; i < n; i++) {
            totalError = trainSingleStep();
        }
        return totalError;
    }

    private static double test(double[] inputs) {
        List<Double> testInputs = Arrays.asList(inputs[0], inputs[1]);
        List<Double> output = neuralNetwork.feedForward(testInputs);
        return output.get(0);
    }

    private static void setTruthTable(double[] newTruthTable) {
        for (int i = 0; i < userTrainingSets.size(); i++) {
            userTrainingSets.get(i)[1][0] = newTruthTable[i];
        }
    }
    /**
     * Represents a request operation from the client.
     */
    static class NeuralNetworkOperation {
        String request;
        int iterations;
        double[] inputs;
    }
    /**
     * Represents a response to be sent to the client.
     */
    static class NeuralNetworkResponse {
        String response;
        String status;
        Double val1;
        Double val2;
        Double val3;
        Double val4;
        Double totalError;
    }
}
