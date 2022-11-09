package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import java.util.List;


public class ClientHandler extends Thread {
    private Socket s;
    private List<ClientHandler> clients;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private static int qtabiglietti;
    public ClientHandler(Socket s, List<ClientHandler> clients , int qtabiglietti) {
   
        this.s = s;
        this.clients = clients;
        this.qtabiglietti = qtabiglietti;
        try {
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        
        String request;

        boolean running = true;
        while (running) {
            try {

                request = in.readLine();
                // COMANDO DATA
                if (request.equalsIgnoreCase("D")) {
                   
                    out.println("sono disponibili: " + qtabiglietti +" biglietti");

                } else if (request.startsWith("A")) {

                    if(qtabiglietti > 0){
                    qtabiglietti --;
                        
                    out.println("è stato tolto un biglietto");

                    }else{

                        //quando finiscono i biglietti chiude la comunicazione client server

                        out.println("non puoi piu eliminare i biglietti");

                        running = false;

                        s.close();
                    }
                    
                } else if (request.startsWith("Q")) {

                    out.println("Hai chiuso la connessione");
                    
                    running = false;

                    s.close();
                } else if (request.startsWith("T")) {

                    out.println("Hai chiuso la connessione");
                    sendToAll("@");
                    running = false;
                } else
                    out.println("Comando non valido");
             
        } catch (IOException e) {
            e.printStackTrace();
        }
            }

            
    }

    private void sendToAll(String msg) {
        for (ClientHandler client : clients) {
            System.out.println(client.getName());
            client.out.println(msg);
        }
    }

    }
