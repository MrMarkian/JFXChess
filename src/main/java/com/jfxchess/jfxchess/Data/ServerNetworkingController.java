package com.jfxchess.jfxchess.Data;

import com.jfxchess.jfxchess.Main;
import javafx.application.Platform;
import javafx.scene.control.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class ServerNetworkingController extends Thread implements NetworkingCommon{

    public boolean runServer = false;
    ServerSocket serverSocket;
    Socket socket;

    boolean logVerbose = true;

    public ServerNetworkingController() {
    }


//Some validation should happen here in case no one is connected ect.

    @Override
    public void run() {

        runServer = true;
        this.setDaemon(true);

        try {
            serverSocket = new ServerSocket(5000);
            socket = serverSocket.accept();
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            networkingLog.add("Server Instance started @" );

            while(runServer) {
                String inputString = input.readLine();
                System.out.println(inputString);
                ProcessIncomingData(inputString);
            }

            socket.close();
            serverSocket.close();
        } catch(IOException e) {
            System.out.println("SERVER EXCEPTION: " + e.getMessage());
        }
    }

    private void ProcessIncomingData(String inputString) throws IOException {
        String[] commandData = inputString.split("%");
        if(commandData.length > 1){
            switch (commandData[0]) {
                case "MOVE" -> {

                    Move incomingMove = new Move(commandData[1]);

                    networkingLog.add("SERVER INCOMING MOVE:" + incomingMove);

                    if (BoardManager.ruleBook.isMoveValid(incomingMove, BoardManager.gameBoard)) {
                        BoardManager.MovePiece(incomingMove);

                        Platform.runLater(() -> {
                            try {
                                Main.getMainUIController().UpdateUI();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        });
                        networkingLog.add("SERVER:: MOVE INSTRUCTION RECEIVED :: " + incomingMove.toString());
                        SendBoardState();
                    }
                }
                case "BOARD" -> {
                    try {
                        SendBoardState();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void SendBoardState() throws IOException {
        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
        networkingLog.add("SERVER:: Sending Board State :: ");
        output.println("FEN%"+BoardManager.SavePositionToFEN());
    }

    @Override
    public void SendMove(Move moveToSend) throws IOException {
        networkingLog.add("SERVER:: Sending Move :: " + moveToSend.toString());
        if(BoardManager.MovePiece(moveToSend)){
            SendFENString(BoardManager.SavePositionToFEN());
        }

    }

    @Override
    public void ReceiveMove(Move receivedMove) {

    }

    @Override
    public void QuitNetworking() {
        runServer = false;
        try {
            socket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<String> PrintLog() {
        return null;
    }

    public void SendFENString (String FENString) throws IOException {
        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
        output.println("FEN%" + FENString);
    }
}
