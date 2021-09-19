package com.jfxchess.jfxchess.Data;

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

    @Override
    public void run() {

        runClient = true;
        this.setDaemon(true);

        try{
            socket = new Socket("localhost", 5000);
            BufferedReader incomingData = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter outgoingData = new PrintWriter(socket.getOutputStream(), true);

            Scanner scanner = new Scanner(System.in);
            String echoString;
            String response;
            boolean ProcessNetworking = true;

            do {
                response = incomingData.readLine();
                System.out.println("Full String Recieved: " + response);

                String[] parseDataStream = response.split("%");
                if( parseDataStream.length > 1){
                    System.out.println("Command Found! " + parseDataStream[0]);
                    parseCommand(parseDataStream);
                }
            } while(runClient);

            socket.close();
        } catch (IOException e) {
            System.out.println("Client Error: " + e.getMessage());
        }
    }


    private void parseCommand(String[] parseDataStream) {

        switch (parseDataStream[0]){
            case "FEN":{
                System.out.println("FEN Received!:-" + parseDataStream[1]);
                Platform.runLater(() -> BoardManager.LoadPositionsFromFEN(parseDataStream[1]));
                break;
            }
            case "EXIT":{
                runClient = false;
                break;
            }
            case "NEW":{
                BoardManager.SetupNewStandardBoard();
                break;
            }
            case "MOVE":{
                ReceiveMove(new Move(parseDataStream[1]));

                break;
            }
            default:{
                System.out.println("UNHANDLED COMMAND:" + parseDataStream[0] + " DATASTREAM:" + parseDataStream[1]);
            }

        }
    }

    @Override
    public void SendMove(Move moveToSend) throws IOException {
        if(BoardManager.ruleBook.isMoveValid(moveToSend,BoardManager.gameBoard)) {
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            output.println("MOVE%" + moveToSend.printNetworkTextValues());
            System.out.println("CLIENT: MOVE SENT");
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

}
