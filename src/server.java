import java.io.*;
import java.net.*;

public class server {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(5618);
            System.out.println("Server listening on port 5618");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                handleClient(clientSocket);
                clientSocket.close();
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void handleClient(Socket clientSocket) {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            out.println("Hello!");

            while (true) {
                String command = in.readLine();
                System.out.println(command);

                if (command.startsWith("Joke")) {
                    try {
                        int jokeNumber = Integer.parseInt(command.split(" ")[1]);
                        String jokeFile = "joke" + jokeNumber + ".txt";

                        if((jokeNumber == 1 || jokeNumber == 2 || jokeNumber == 3)){
                            out.println(jokeFile);
                        }
                        else {
                            out.println("Error: Invalid Joke Number");
                        }
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        out.println("Error: Invalid Joke Number");
                    }
                } else if (command.equals("bye")) {
                    out.println("disconnected");
                    break;
                } else{
                    out.println("Error: Invalid Command");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
