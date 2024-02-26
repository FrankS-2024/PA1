import java.io.*;
import java.net.*;

public class client {
    public static void main(String[] args) {
        Socket clientSocket = null;

        try {
            clientSocket = new Socket("localhost", 5618);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Receive and print the initial greeting
            String greetingMessage = in.readLine();
            System.out.println(greetingMessage);

            while (true) {
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Enter command: ");
                String command = userInput.readLine();
                out.println(command);

                String response = in.readLine();

                if (response.equals("disconnected")) {
                    System.out.println(response);
                    System.out.println("exit");
                    break;
                } else if (response.startsWith("Error")) {
                    System.out.println(response);
                } else {
                    int jokeNumber = Integer.parseInt(command.split(" ")[1]);
                    String jokeFile = "joke" + jokeNumber + ".txt";
                    try (PrintWriter fileWriter = new PrintWriter(new FileWriter(jokeFile))) {
                        if((jokeNumber == 1 || jokeNumber == 2 || jokeNumber == 3)){
                            fileWriter.println(response);
                        }
                        System.out.println("Joke saved to " + jokeFile);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}