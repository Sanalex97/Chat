import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    public static void main(String[] args) {
        int port = getPort();

        try (ServerSocket serverSocket = new ServerSocket(port);
             Socket clientSocket = serverSocket.accept();
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            Runnable runnableWriter = () -> {
                while (!clientSocket.isClosed()) {
                    out.println("Hi, client");
                }
            };

            Runnable runnableReader = () -> {
                try {
                    while (!clientSocket.isClosed()) {
                        String mesIn = in.readLine();
                        System.out.println(mesIn);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            };


            Thread threadReader = new Thread(runnableReader);
            Thread threadWriter = new Thread(runnableWriter);
            threadReader.start();
            threadWriter.start();

            System.out.println("New connection accepted");

        } catch (IOException e) {
            throw new RuntimeException(e);
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
