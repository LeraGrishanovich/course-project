package org.example;

import org.example.Utility.ClientThread;
import org.example.Utility.HibernateUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static int PORT_SERVER = 8055;
    private static ServerSocket serverSocket;
    private static ClientThread clientThread;
    private static Thread thread;


    static int clientNumber = 1;

    public static void main(String[] args) {
        try {
            HibernateUtil.getSessionFactory();
            serverSocket = new ServerSocket(PORT_SERVER);
            System.out.println("Сервер начал свою работу! ");
            while (true) {
                Socket socket = serverSocket.accept();
                clientThread = new ClientThread(socket, clientNumber);
                System.out.println("Клиент № " + clientNumber + " подключился! ");
                clientNumber++;
                thread = new Thread(clientThread);
                thread.start();
            }

        } catch (IOException e) {
            System.out.println("Error " + e.getMessage());
        }
    }
}