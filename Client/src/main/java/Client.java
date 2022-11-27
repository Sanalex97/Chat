import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class Client {
    private static final AtomicInteger count = new AtomicInteger(0);

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                try (Socket socket = new Socket("localhost", getPort());
                     BufferedWriter writer = new BufferedWriter(
                             new OutputStreamWriter
                                     (socket.getOutputStream()));
                     BufferedReader reader = new BufferedReader(
                             new InputStreamReader(
                                     socket.getInputStream()
                             )
                     )) {
                    System.out.println("Connected to server");

                    String request = "Hi, server! My name is client" + count.getAndIncrement();
                    System.out.println("Request = " + request);

                    writer.write(request);
                    writer.newLine();
                    writer.flush();

                    String response = reader.readLine();
                    System.out.println("Response = " + response);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
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

