package server;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
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
            String joke = "";
            while (true) {
                String command = in.readLine();
                System.out.println(command);

                if (command.startsWith("Joke")) {
                    try {
                        String[] parts = command.split(" ");
                        if (parts.length >= 2) {
                            int jokeNumber = Integer.parseInt(parts[1]);
                            String jokeFile = "joke" + jokeNumber + ".txt";

                            if ((jokeNumber == 1 || jokeNumber == 2 || jokeNumber == 3)) {
                                Path file = Path.of(jokeFile);
                                try {
                                    Files.lines(file).forEach(out::println);
                                    out.println("EOF");  // End of file indicator
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                out.println("Error: Invalid Command");
                            }
                        } else {
                            out.println("Error: Invalid Command");
                        }
                    } catch (NumberFormatException e) {
                        out.println("Error: Invalid Command");
                    }
                } else if (command.equals("bye")) {
                    out.println("disconnected");
                    break;
                } else {
                    out.println("Error: Invalid Command");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
