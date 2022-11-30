import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static final ArrayList<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        int port = getPort();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started");

            while (true) {
                Socket clientSocket = serverSocket.accept();

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);

                new Thread(clientHandler).start();

            }
        }
    }


    public static int getPort() {
        int port = 0;

        File fileSettings = new File("src/main/resources/settings.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(fileSettings))) {
            String s;
            while ((s = br.readLine()) != null) {
                String[] settings = s.split("=");

                if (settings[0].equals("port")) {
                    port = Integer.parseInt(settings[1]);
                }
            }

            System.out.println(port);

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return port;
    }
}
