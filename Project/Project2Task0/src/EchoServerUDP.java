/**
 * EchoServerUDP.java
 * Author: Kaizhong Ying AndrewID: kying
 * Last Modified: February 17, 2024
 *
 * This class implements a simple UDP server that listens for messages from clients
 * then echoes those messages back to the clients
 * The server is designed to demonstrate basic UDP socket programming concept
 * It illustrates how to handle incoming UDP packets, extract data and respond to the sender.
 * This example serves as a foundation for understanding UDP communication mechanisms
 * emphasizing the connectionless nature of UDP
 * showcasing how to perform basic network I/O operations using Java's DatagramSocket.
 */
import java.net.*;
import java.io.*;
public class EchoServerUDP{
    public static void main(String args[]){
        // Announce the client is running
        System.out.println("The UDP client is running.");
        // Initialize a datagram socket to listen for and send packets
        DatagramSocket aSocket = null;
        // Create a buffer to store 1000 bytes
        byte[] buffer = new byte[1000];
        BufferedReader serverPortInput = new BufferedReader(new InputStreamReader(System.in));
        try{
            // Prompt the user for the server port number.
            System.out.print("Enter the port number to listen on: ");
            // Read the port number from the console.
            int portNumber = Integer.parseInt(serverPortInput.readLine());
            //Create a socket on port 6789 to listen for packet received
            aSocket = new DatagramSocket(portNumber);
            //Create a datagram packet to receive data
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            while(true){
                //Receive the data into the datagram packet
                aSocket.receive(request);
                //Create a reply message to the server, including the data, length, address and port
                DatagramPacket reply = new DatagramPacket(request.getData(),
                        request.getLength(), request.getAddress(), request.getPort());
                //Initialize a new receivedData byte array to store the correct number of bytes
                byte[] receivedData = new byte[request.getLength()];
                System.arraycopy(buffer, 0, receivedData, 0, request.getLength());
                //Convert the request to String
                String requestString = new String(receivedData);
                //Print the request string
                System.out.println("Echoing: "+requestString);
                //Send the reply back to the client
                aSocket.send(reply);
                //If the request is halt!, it will exit
                if (requestString.equals("halt!")){
                    System.out.println("UDP Server side quitting");
                    System.exit(0);
                }

            }
            //Catch and print any socket exception
        }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
            //Catch and print any IO exception
        }catch (IOException e) {System.out.println("IO: " + e.getMessage());
            //Ensure the socket is closed
        }finally {if(aSocket != null) aSocket.close();}
    }
}
