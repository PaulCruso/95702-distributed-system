/**
 * EavesdropperUDP.java
 * Author: Kaizhong Ying Andrew ID: kying
 * Last Modified: February 17, 2024
 *
 * Serves as a middleman for UDP communication
 * Simulating a man-in-the-middle attack for educational use.
 * It intercepts and optionally modifies messages between a client and server
 * Specifically changing "like" to "dislike" in client messages.
 * Users must specify listening and server port numbers on startup.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
public class EavesdropperUDP {
    public static void main(String[] args) {

        System.out.println("Malicious UDP is running.");
        //Initialize the socket to client
        DatagramSocket socketToClient = null;
        //Initialize the socket to server
        DatagramSocket socketToServer = null;

        try {
            BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));
            // Listen to 6798
            System.out.print("Enter the port number to listen on: ");
            int listeningPort = Integer.parseInt(systemIn.readLine());
            // Work as a client to 6789
            System.out.print("Enter the server port number: ");
            int serverPort = Integer.parseInt(systemIn.readLine());
            // The socket to client
            socketToClient = new DatagramSocket(listeningPort);
            // The socket to server
            socketToServer = new DatagramSocket();

            System.out.println("Listening on port: " + listeningPort + ", Forwarding to server port: " + serverPort);

            while (true) {
                // Receive the message from client
                byte[] bufferFromClient = new byte[1000];
                DatagramPacket packetFromClient = new DatagramPacket(bufferFromClient, bufferFromClient.length);
                socketToClient.receive(packetFromClient);
                // print the message from client
                String messageFromClient = new String(packetFromClient.getData(), 0, packetFromClient.getLength());
                System.out.println("Received from client: " + messageFromClient);

                // Modify the message if it contains the word "like"
                if (messageFromClient.contains("like")) {
                    messageFromClient = messageFromClient.replaceFirst("like", "dislike");
                }

                // Forward the message to the server
                byte[] modifiedMessageBytes = messageFromClient.getBytes();
                InetAddress serverAddress = InetAddress.getByName("localhost");
                DatagramPacket packetToServer = new DatagramPacket(modifiedMessageBytes, modifiedMessageBytes.length,
                        serverAddress, serverPort);
                socketToServer.send(packetToServer);

                // Receive the response from the server
                byte[] bufferFromServer = new byte[1000];
                DatagramPacket packetFromServer = new DatagramPacket(bufferFromServer, bufferFromServer.length);
                socketToServer.receive(packetFromServer);

                String messageToServer = new String(packetFromServer.getData(), 0, packetFromServer.getLength());
                System.out.println("Forward to server: " + messageToServer);

                // Relay the server's response to the client unchanged
                InetAddress clientAddress = packetFromClient.getAddress();
                int clientPort = packetFromClient.getPort();
                DatagramPacket packetToClient = new DatagramPacket(packetFromServer.getData(), packetFromServer.getLength(),
                        clientAddress, clientPort);
                socketToClient.send(packetToClient);
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } finally {
            // close two socket
            if (socketToClient != null) {
                socketToClient.close();
            }
            if (socketToServer != null) {
                socketToServer.close();
            }
        }
    }
}
