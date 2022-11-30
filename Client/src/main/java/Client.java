import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Client {

    public static void main(String[] args) {

        //todo сделать возможным переписку

        // for (int i = 0; i < 5; i++) {
        new Thread(() -> {
            try (Socket socket = new Socket("localhost", getPort());
                 PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader reader = new BufferedReader(
                         new InputStreamReader(
                                 socket.getInputStream()
                         )
                 )) {
                System.out.println("Connected to server");
                System.out.println("Введите ваш никнейм для участия в чате: ");

                Scanner scanner = new Scanner(System.in);

                System.out.println(socket.isClosed());

                while (!socket.isClosed()) {
                String request = scanner.nextLine();

                if (!request.equals("")) {
                    System.out.println("Request = " + request);

                    writer.println(request);
                    writer.flush();
                }


                    String response = reader.readLine();
                    System.out.println("Response = " + response);
                    // Thread.sleep(4000);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    //  }

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

