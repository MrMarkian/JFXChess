package com.jfxchess.jfxchess.Data;

import com.jfxchess.jfxchess.Main;
import javafx.application.Platform;
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

    public ServerNetworkingController() {
    }



    @Override
    public void run() {

        runServer = true;
        this.setDaemon(true);

        try {
            serverSocket = new ServerSocket(5000);
            socket = serverSocket.accept();
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

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

                    System.out.println("SERVER INCOMING MOVE:" + incomingMove);

                    if (BoardManager.ruleBook.isMoveValid(incomingMove, BoardManager.gameBoard)) {
                        BoardManager.MovePiece(incomingMove);

                        Platform.runLater(() -> {
                            try {
                                Main.getMainUIController().UpdateUI();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        });
                        System.out.println("SERVER:: MOVE INSTRUCTION RECEIVED");
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
        output.println("FEN%"+BoardManager.SavePositionToFEN());
    }

    @Override
    public void SendMove(Move moveToSend) throws IOException {
        if(BoardManager.ruleBook.isMoveValid(moveToSend,BoardManager.gameBoard)) {
            BoardManager.MovePiece(moveToSend);
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
