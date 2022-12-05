import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client1 {
    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("localhost", getPort());

        new Thread(() -> {
            try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

                System.out.println("Connected to server");
                System.out.println("Введите ваше имя для участия в чате: ");

                Scanner scanner = new Scanner(System.in);

                while (!socket.isClosed()) {
                    String request = scanner.nextLine();

                    if (request.equals("/exit")) {
                        socket.close();
                    } else {
                        System.out.println("Request = " + request);
                        writer.println(request);
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }).start();

        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                while (!socket.isClosed()) {
                    String response;
                    //  if (reader.ready()) {
                    while ((response = reader.readLine()) != null) {
                        // response = reader.readLine();
                        System.out.println("Response = " + response);
                    }
                }

            } catch (IOException e) {
                if(e.getMessage().equals("Socket closed")) {
                    System.out.println("Клиент покинул чат");
                } else {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
    //  }

    public static int getPort() {
        int port = 0;

        File fileSettings = new File("Server/src/main/resources/Settings.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(fileSettings))) {
            String s;
            while ((s = br.readLine()) != null) {
                String[] settings = s.split("=");

                if (settings[0].equals("port")) {
                    port = Integer.parseInt(settings[1]);
                }
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return port;
    }
}

