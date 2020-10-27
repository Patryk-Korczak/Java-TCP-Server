package com.company;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread{
    ServerSocket myServer = null;
    ArrayList<Socket> myClients = new ArrayList<>();

    public Server() {
        this.myClients.clear();
    }

    public void startServer(int port) {
        try {
            if(this.myServer != null) {
                this.myServer.close();
            }
            this.myServer = new ServerSocket(port);
            start();
        } catch(Exception e){
            this.myServer = null;
            System.out.println(e.getMessage());
        }
    }

    public void stopServer() {
        try {
            if(this.myServer != null) {
                for (Socket s : this.myClients) {
                    if(s.isConnected()){
                        s.close();
                    }
                }
                this.myServer.close();
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void printInfo() {
        System.out.println("Clients connected: " + this.myClients.size());
        System.out.println();
    }

    public void printClients() {
        if(this.myClients.isEmpty()) {
            System.out.println("No clients connected.");
        } else {
            for (Socket client : this.myClients) {
                System.out.println("Name: " + client.getInetAddress().getHostName() +
                                   " Address: " + client.getInetAddress().getHostAddress() +
                                   " Port: " + client.getPort());
            }
        }
    }

    public void help() {
        System.out.println("Available commands: ");
        System.out.println("-stop | Disconnect all connected clients and close the server ");
        System.out.println("-info | Display information about server ");
        System.out.println("-clients | Display information about all connected users ");
    }


    public void run() {
        try {
            System.out.println("Server started on port: " + this.myServer.getLocalPort());
            System.out.println("waiting for connection...");
            while(true) {
                Socket client = this.myServer.accept();
                this.myClients.add(client);
                Service serviceThread = new Service(client);
                serviceThread.start();
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

