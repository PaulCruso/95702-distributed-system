import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.Locale;

public class HTTPServer {
    public static void main(String args[]) {
        Socket clientSocket = null;
        try {
            int serverPort = 7777;
            ServerSocket listenSocket = new ServerSocket(serverPort);
            while (true) {
                clientSocket = listenSocket.accept();

                Scanner inFromSocket = new Scanner(clientSocket.getInputStream());
                PrintWriter outToSocket = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));

                String line = inFromSocket.nextLine();
                String fileName = line.split(" ")[1].substring(1);

                // Print the file path to the console
                System.out.println(line);

                File file = new File(fileName);
                if (file.exists() && !file.isDirectory()) {
                    outToSocket.println("HTTP/1.1 200 OK");
                    outToSocket.println();
                    BufferedReader fileReader = new BufferedReader(new FileReader(file));
                    String str;
                    while ((str = fileReader.readLine()) != null) {
                        outToSocket.println(str);
                    }
                    fileReader.close();
                } else {
                    outToSocket.println("HTTP/1.1 404 Not Found");
                    outToSocket.println();
                }
                outToSocket.flush();
                outToSocket.close();
                clientSocket.close();
            }
        } catch (IOException e) {
            System.out.println("IO Exception:" + e.getMessage());
        } finally {
            try {
                if (clientSocket != null) clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

