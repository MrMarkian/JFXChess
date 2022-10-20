package com.jfxchess.jfxchess.Data;

import com.jfxchess.jfxchess.Main;
import com.jfxchess.jfxchess.MainUIController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

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
    public Socket socket;
    public String hostIp = "localhost";
    public int port = 5000;
    public MainUIController uiController;
    public Player me;
    @Override
    public void run() {
        System.out.println("Networking Client Started.");
        runClient = true;
        this.setDaemon(true);

        try{
            socket = new Socket(me.IP, port);
            BufferedReader incomingData = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter outgoingData = new PrintWriter(socket.getOutputStream(), true);
            networkingLog.add("CLIENT::Client Instance Started" + me.IP + " Port: " + port);

            String response;
            boolean ProcessNetworking = true;
            updateColor();
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
                networkingLog.add("CLIENT::FEN Received: " + parseDataStream[1]);
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
                networkingLog.add("CLIENT::MESSAGE:: " );
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
            case "SETCOLOR":{
                me.COLOR = ChessTeamColor.valueOf(parseDataStream[1]);
            }

            case "ALERT":{
                networkingLog.add("CLIENT::ALERT:: ");
                Platform.runLater(()-> {
                    ReceiveAlert(parseDataStream[1],parseDataStream[2], parseDataStream[3]);
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

    @Override
    public void SendAlert(String title, String header, String content) throws IOException {

    }


    //TODO: This is now depricated, now use the function in MainUIController
    @Override
    public void ReceiveAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                System.out.println("Pressed OK.");
            }
        });
    }

    public void updateColor() throws IOException {
        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
        output.println("GETCOLOR%");
    }
}
