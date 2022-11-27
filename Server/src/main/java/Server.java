import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(getPort())) {
            System.out.println("Server started");

            while (true) {
                Socket socket = serverSocket.accept();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter
                                (socket.getOutputStream()));

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()
                        ));

                new Thread(() -> {
                    String request;
                    try {
                        request = reader.readLine();

                        System.out.println("Request = " + request);

                        Thread.sleep(4000);

                        String response = "Hi, client";

                        System.out.println("Response = " + response);

                        writer.write(response);
                        writer.newLine();

                        //    writer.flush();
                        writer.close();
                        reader.close();

                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }).start();

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
