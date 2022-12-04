import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import java.util.concurrent.atomic.AtomicInteger;

public class Client {

    public static void main(String[] args) {

        // for (int i = 0; i < 5; i++) {
        new Thread(() -> {
            try (Socket socket = new Socket("localhost", 8880);
                 PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader reader = new BufferedReader(
                         new InputStreamReader(
                                 socket.getInputStream()
                         )
                 )) {
                System.out.println("Connected to server");
                System.out.println("Введите ваш никнейм для участия в чате: ");

                Scanner scanner = new Scanner(System.in);

                while (!socket.isClosed()) {
                    String request = scanner.nextLine();

                    if (!request.equals("")) {
                        System.out.println("Request = " + request);

                        writer.println(request);
          //              writer.flush();
                    }

                    Thread.sleep(4000);

                    String response;
                     //  if (reader.ready()) {
                      while ((response = reader.readLine()) != null) {
                     // response = reader.readLine();
                            System.out.println("Response = " + response);
                        }
                           System.out.println("Напишите что-нибудь в чат");
                       // break;
                  // }
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
    //  }

    public static int getPort() {
        int port = 0;

        File fileSettings = new File("src ");

        try (BufferedReader br = new BufferedReader(new FileReader(fileSettings))) {
            String s;
            while ((s = br.readLine()) != null) {
                String[] settings = s.split("settings.txt");

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

