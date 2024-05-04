/**
 * Author: Kaizhong Ying
 * Last Modified: February 18, 2024 Andrew ID: kying
 *
 * RemoteVariableClientUDP.java serves as a UDP client facilitating interactions with a remote server.
 * It allows users to perform 'add', 'subtract', and 'get' operations on a server-managed sum via a menu-driven interface.
 * Operations and values are sent to the server based on user input, with the server's response displayed to the user.
 * Aimed at demonstrating client-side communication and proxy design pattern in UDP networking.
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class RemoteVariableClientUDP {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("The client is running.\nPlease enter server port:");
        // listen to the needed IO
        int serverPort = Integer.parseInt(reader.readLine());

        while (true) {
            // print the I/O used
            System.out.println("\n1. Add a value to your sum.\n2. Subtract a value from your sum.\n" +
                    "3. Get your sum.\n4. Exit client");
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(reader.readLine());

            if (choice == 4) {
                System.out.println("Client side quitting. The remote variable server is still running.");
                break;
            }
            // The unique ID for each user
            System.out.print("Enter your ID: ");
            int id = Integer.parseInt(reader.readLine());
            // The unique ID has a initial value 0 if it is not initialized
            int value = 0;
            if (choice != 3) {
                System.out.print(choice == 1 ? "Enter value to add: " : "Enter value to subtract: ");
                value = Integer.parseInt(reader.readLine());
            }

            char operation = switch (choice) {
                // Add
                case 1 -> 'a';
                // Subtract
                case 2 -> 's';
                // Get
                default -> 'g';
            };

            int result = sendRequest(id, operation, value, serverPort);
            System.out.println("The result is " + result + ".");
        }
    }
    // In sendRequest method, ByteBuffer.allocate is suggested by ChatGPT
    private static int sendRequest(int id, char operation, int value, int serverPort) throws Exception {
        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress address = InetAddress.getByName("localhost");

            // Correctly allocate the buffer based on the data sizes: 4 bytes for ID, 2 bytes for operation, 4 bytes for value..
            ByteBuffer buffer = ByteBuffer.allocate(10);
            // putChar for operation, which correctly takes 2 bytes
            buffer.putInt(id).putChar(operation).putInt(value);
            byte[] byteArray = buffer.array();
            // create a datagram packet to send
            DatagramPacket packet = new DatagramPacket(byteArray, byteArray.length, address, serverPort);
            socket.send(packet);
            // create a datagram packet to receive
            byte[] response = new byte[4];
            DatagramPacket reply = new DatagramPacket(response, response.length);
            socket.receive(reply);

            return ByteBuffer.wrap(reply.getData()).getInt();
        }
    }
}


