/**
 * EchoClientUDP.java
 * Author: Kaizhong Ying
 * Last Modified: February 17, 2024
 *
 * This class implements a simple UDP client that sends messages to a UDP server and receives echoes of those messages.
 * The client showcases how to package user input
 * It demonstrates the process of marshaling (converting data into a suitable format for transmission)
 * un-marshaling (extracting data from received packets) in a UDP communication context
 * This example highlights the differences between UDP and TCP sockets
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
public class EchoClientUDP{
    public static void main(String args[]){
        // Announce the client is running
        System.out.println("The UDP client is running.");
        //Initialize a DatagramSocket for sending and receiving packets.
        DatagramSocket aSocket = null;
        try {
            //Get the address of the host
            InetAddress aHost = InetAddress.getByName("localhost");
            //Initialize the server port number
            BufferedReader serverPortInput = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter server port number: ");
            // Read the server port number from the user
            int serverPort = Integer.parseInt(serverPortInput.readLine());
            //Create a socket for sending and receiving data
            aSocket = new DatagramSocket();
            String nextLine;
            //Initialize a reader to read client's input
            BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));
            //Loop until all client's input is read
            while ((nextLine = typed.readLine()) != null) {
                //Convert input string to byte
                byte [] m = nextLine.getBytes();
                //Create a request packet with the user input, targeting the server's address and port
                DatagramPacket request = new DatagramPacket(m,  m.length, aHost, serverPort);
                //Send the request packet
                aSocket.send(request);
                // Create a buffer to store 1000 bytes
                byte[] buffer = new byte[1000];
                //Prepare a packet to receive the reply
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                //Receive the server's reply
                aSocket.receive(reply);
                //Initialize a new replyData byte array to store the correct number of bytes
                byte[] replyData = new byte[reply.getLength()];
                System.arraycopy(reply.getData(), 0, replyData, 0, reply.getLength());

                String replyString = new String(replyData);
                //Print the reply
                System.out.println("Reply from server: " + replyString);
                // If the input is halt!, it will exit
                if (replyString.equals("halt!")){
                    System.out.println("UDP Client side quitting");
                    System.exit(0);
                }
            }
            //Catch and print any socket exception
        }catch (SocketException e) {System.out.println("Socket Exception: " + e.getMessage());
            //Catch and print any IO exception
        }catch (IOException e){System.out.println("IO Exception: " + e.getMessage());
            //Ensure the socket is closed
        }finally {if(aSocket != null) aSocket.close();}
    }
}
