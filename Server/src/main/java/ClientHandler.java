import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private final BufferedReader reader;
    private PrintWriter writer;

    private static final List<ClientHandler> clients = new ArrayList<>();
    private boolean newClient = true;

    private String name;

    private static final Logger logger = Logger.getInstance();

    public PrintWriter getWriter() {
        return writer;
    }

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        this.reader = createReader();
        this.writer = createPrintWriter();

        synchronized (clients) {
            clients.add(this);
        }
    }

    private BufferedReader createReader() {
        try {
            return new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private PrintWriter createPrintWriter() {
        try {
            return new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    @Override
    public void run() {
        while (!clientSocket.isClosed()) {
            try {
                if (reader.ready()) {
                    String request = reader.readLine();

                    System.out.println("Request = " + request);

                    if (newClient) {
                        String response;
                        response = "Welcome, client " + request;
                        writer.println(response);

                        this.name = request;

                        logger.log("сервер отправил сообщение: " + response);


                        synchronized (clients) {
                            writer.println("Клиентов в чате " + clients.size());
                            System.out.println("Клиентов в чате " + clients.size());
                            logger.log("сервер отправил сообщение: " + "Клиентов в чате " + clients.size());
                        }
                        newClient = false;
                    } else {
                        for (ClientHandler client : clients) {
                            if (client.getClientSocket() != clientSocket) {
                                client.getWriter().println("Клиент " + name + " написал всем: " + request);
                                System.out.println("Response = " + request);

                                logger.log(name + " отправил сообщение: " + request);
                            }
                        }
                    }

                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
