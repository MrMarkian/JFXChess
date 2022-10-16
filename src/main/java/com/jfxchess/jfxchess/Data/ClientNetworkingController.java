package com.jfxchess.jfxchess.Data;

import com.jfxchess.jfxchess.Main;
import com.jfxchess.jfxchess.MainUIController;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientNetworkingController extends Thread implements NetworkingCommon{

    public boolean runClient = false;
    Socket socket;
    String hostIp = "localhost";
    int port = 5000;
    public MainUIController uiController;

    @Override
    public void run() {

        runClient = true;
        this.setDaemon(true);

        try{
            socket = new Socket(hostIp, port);
            BufferedReader incomingData = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter outgoingData = new PrintWriter(socket.getOutputStream(), true);
            networkingLog.add("CLIENT::Client Instance Started" + hostIp + " Port: " + port);
            Scanner scanner = new Scanner(System.in);
            String echoString;
            String response;
            boolean ProcessNetworking = true;

            do {
                response = incomingData.readLine();
                System.out.println("Full String Received: " + response);

                String[] parseDataStream = response.split("%");
                if( parseDataStream.length > 1){
                    System.out.println("CLIENT::Command Found! " + parseDataStream[0]);
                    parseCommand(parseDataStream);
                    Platform.runLater(()-> {
                        try {
                            uiController.UpdateUI();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            } while(runClient);

            socket.close();
        } catch (IOException e) {
            networkingLog.add("Client Error: " + e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private void parseCommand(String[] parseDataStream) throws InterruptedException {

        switch (parseDataStream[0]){
            case "FEN":{
                networkingLog.add("CLIENT::FEN Received!:-" + parseDataStream[1]);
                Platform.runLater(() -> BoardManager.LoadPositionsFromFEN(parseDataStream[1]));
                Platform.runLater(()-> {
                    try {
                        uiController.UpdateUI();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
                break;
            }
            case "EXIT":{
                runClient = false;
                break;
            }
            case "NEW":{
                BoardManager.SetupNewStandardBoard();
                Platform.runLater(()-> {
                    try {
                        uiController.UpdateUI();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
                break;
            }
            case "MOVE":{
                networkingLog.add("CLIENT::MOVE::" + parseDataStream[1]);
                ReceiveMove(new Move(parseDataStream[1]));
                Platform.runLater(()-> {
                    try {
                        uiController.UpdateUI();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
                break;
            }
            case "MESSAGE":{
                networkingLog.add("Message Recieved: " );
                Platform.runLater(()-> {
                    try {
                        uiController.addChatMessage(parseDataStream[1]);
                        uiController.UpdateUI();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
                break;

            }
            default:{
                networkingLog.add("CLIENT::UNHANDLED COMMAND:" + parseDataStream[0] + " DATASTREAM:" + parseDataStream[1]);
            }

        }
    }

    @Override
    public void SendMove(Move moveToSend) throws IOException {
        if(BoardManager.ruleBook.isMoveValid(moveToSend,BoardManager.gameBoard)) {
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            output.println("MOVE%" + moveToSend.printNetworkTextValues());
            networkingLog.add("MOVE%" + moveToSend.printNetworkTextValues());
            networkingLog.add("CLIENT: MOVE SENT");

        }
    }

    @Override
    public void ReceiveMove(Move receivedMove) {

    }

    @Override
    public void QuitNetworking() {
        runClient = false;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> PrintLog() {
        List<String> logs = new ArrayList<>(NetworkingCommon.networkingLog);
        return logs;
    }

    @Override
    public boolean SendMessage(String message) throws IOException {
        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
        output.println("MESSAGE%" + message);

        return false;
    }

    @Override
    public void ReceiveMessage(String message) {

    }

}
