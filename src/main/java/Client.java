import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args) throws UnknownHostException {
        String host = getIPHost("netology.homework");
        int port = 8880; //todo написать паттерн для использования одного и того же метода в разных классах

        try (Socket clientSocket = new Socket(host, port);
             PrintWriter out = new
                     PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new
                     InputStreamReader(clientSocket.getInputStream()))) {


            Runnable runnableReaderClient = () -> {
                try {
                    while (!clientSocket.isClosed()) {
                        if (in.ready()) {
                            String mes = in.readLine();
                            System.out.println(mes);
                        }
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            };

            Runnable runnableWriterClient = () -> {
                while (!clientSocket.isClosed()) {
                    out.println("Hi, server");
                }
            };

            Thread threadReaderClient = new Thread(runnableReaderClient);
            Thread threadWriterClient = new Thread(runnableWriterClient);

            threadReaderClient.start();
            threadWriterClient.start();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getIPHost(String host) throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getByName(host);
        return inetAddress.getHostAddress();
    }
}
