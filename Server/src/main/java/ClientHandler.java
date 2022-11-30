import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private final BufferedReader reader;
  //  private final BufferedWriter writer;
    private PrintWriter writer;
    private Scanner inMsg;
    private static final AtomicInteger clientsСount = new AtomicInteger(0);

    private String name;

    public ClientHandler(Socket socket) {

        this.clientSocket = socket;
        this.reader = createReader();
        this.writer = createPrintWriter();

        synchronized (clientsСount) {
            clientsСount.getAndIncrement();

            clientsСount.notify();

        }



      /*      this.outMsg = new PrintWriter(socket.getOutputStream());
            this.inMsg = new Scanner(socket.getInputStream());*/


    }

    private BufferedReader createReader() {
        try {
            return new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private BufferedWriter createWriter() {
        try {
            return new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
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

    @Override
    public void run() {
        while (!clientSocket.isClosed()) {
            try {
                //todo пока не отправится два сообщения, другом потоку ничего не передавать
                String request = reader.readLine();

                System.out.println("Request = " + request);


                String response = "Welcome, client " + request;

                System.out.println("Response = " + response);

                writer.println(response);


                synchronized (clientsСount) {

                    writer.println("Клиентов в чате " + clientsСount);
                  //  writer.newLine();

                    System.out.println("Клиентов в чате " + clientsСount);


                    try {
                        clientsСount.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                 //   writer.flush();

                  //  writer.close();
                    //  reader.close();

                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
