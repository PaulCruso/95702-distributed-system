/**
 * Author: Kaizhong Ying
 * Andrew ID: kying
 * Last Modified: February 23, 2024
 *
 * NeuralNetworkClient serves a UDP Client to give the output for NeuralNetwork
 * It does not process any data processing, but deal with the data output
 * It gives a client a menu for five options and generate output from server
 * It represents the output similar to the original NeuralNetwork.java
 * It uses Json to send and receive datagram socket
 */
import com.google.gson.Gson;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class NeuralNetworkClient {
    private static final int SERVER_PORT = 6789;
    private static final String SERVER_ADDRESS = "localhost";
    private static final Gson gson = new Gson();
    // Implementation includes creating a menu, preparing operations,
    // sending requests, and displaying responses.
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            InetAddress address = InetAddress.getByName(SERVER_ADDRESS);
            DatagramSocket socket = new DatagramSocket();

            while (true) {
                int userSelection = menu(scanner);
                if (userSelection == 5) {
                    System.out.println("Exiting program.");
                    break;
                }
                NeuralNetworkOperation operation = prepareOperation(userSelection, scanner);
                //send the request to server
                String jsonRequest = gson.toJson(operation);
                byte[] buffer = jsonRequest.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, SERVER_PORT);
                socket.send(packet);
                // get the Json response from server
                byte[] responseBuffer = new byte[1024];
                DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length);
                socket.receive(responsePacket);
                String jsonResponse = new String(responsePacket.getData(), 0, responsePacket.getLength());
                NeuralNetworkResponse response = gson.fromJson(jsonResponse, NeuralNetworkResponse.class);
                displayResponse(response);
            }
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Displays the user menu and handles user input.
     * @param scanner Scanner instance for reading user input.
     * @return The user's menu selection.
     */
    private static int menu(Scanner scanner) {
        System.out.println("\nUsing a neural network to learn a truth table.\nMain Menu");
        System.out.println("0. Display the current truth table.");
        System.out.println("1. Set new truth table (0 1 1 0 for XOR).");
        System.out.println("2. Perform a single training step.");
        System.out.println("3. Perform n training steps.");
        System.out.println("4. Test with a pair of inputs.");
        System.out.println("5. Exit.");
        System.out.print("Select an option: ");
        return scanner.nextInt();
    }
    /**
     * Prepares an operation based on user selection.
     * @param userSelection The user's menu selection.
     * @param scanner Scanner instance for reading additional input.
     * @return A NeuralNetworkOperation representing the requested operation.
     */
    private static NeuralNetworkOperation prepareOperation(int userSelection, Scanner scanner) {
        NeuralNetworkOperation operation = new NeuralNetworkOperation();
        operation.request = switch (userSelection) {
            case 0 -> "getCurrentRange";
            case 1 -> {
                System.out.println("Enter the four results of a 4 by 2 truth table. Each value should be 0 or 1.");
                operation.inputs = new double[]{scanner.nextDouble(), scanner.nextDouble(),
                        scanner.nextDouble(), scanner.nextDouble()};
                yield "setCurrentRange";
            }
            case 2 -> {
                operation.iterations = 1;
                yield "trainSingleStep";
            }
            case 3 -> {
                System.out.println("Enter the number of training steps:");
                operation.iterations = scanner.nextInt();
                yield "train";
            }
            case 4 -> {
                System.out.println("Enter a pair of doubles from a row of the truth table. These are domain values.");
                operation.inputs = new double[]{scanner.nextDouble(), scanner.nextDouble()};
                yield "test";
            }
            default -> throw new IllegalStateException();
        };
        return operation;
    }
    /**
     * Displays the server's response to the user.
     * @param response The server's response to display.
     */
    private static void displayResponse(NeuralNetworkResponse response) {
        if ("getCurrentRange".equals(response.response)) {
            System.out.println("Current truth table range: \n"+
                    "0.0  0.0  " + response.val1 + "\n" +
                    "0.0  1.0  "+ response.val2 + "\n" +
                    "1.0  0.0  " + response.val3 + "\n" +
                    "1.0  1.0  " + response.val4);
        } else if ("train".equals(response.response) || "trainSingleStep".equals(response.response)) {
            System.out.println("After this step the error is : " + response.totalError);
        } else if ("test".equals(response.response)) {
            System.out.println("The range value is approximately " + response.val1);
        } else {
            System.out.println();
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












