package ru.geekbrains.Lesson2_6_1;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 15228;

    public static void main(String[] args) {
        Socket socket = null;
        Scanner scanner = new Scanner(System.in);

        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Подключен к серверу " + socket.getRemoteSocketAddress());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());


            Thread threadRead = new Thread(() -> {
                try {
                    while (true) {
                        out.writeUTF(scanner.nextLine());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            threadRead.setDaemon(true);
            threadRead.start();

            while (true) {
                String str = in.readUTF();
                if (str.equals("/end")) {
                    System.out.println("Клиент отключился");
                    out.writeUTF("/end");
                    break;
                } else {
                    System.out.println("Сервер " + str);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}
