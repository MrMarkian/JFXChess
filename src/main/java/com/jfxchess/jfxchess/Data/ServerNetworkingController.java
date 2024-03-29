package com.jfxchess.jfxchess.Data;

import com.jfxchess.jfxchess.MainUIController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import kotlin.NotImplementedError;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;


public class ServerNetworkingController extends Thread implements NetworkingCommon, Runnable{

    public boolean runServer = false;
    ServerSocket serverSocket;
    Socket socket;
    public MainUIController uiController;
    public Player localPlayer, remotePlayer;
    public boolean passEnabled = false;

    boolean logVerbose = true;

    public ServerNetworkingController() {
    }


//Some validation should happen here in case no one is connected ect.

    @Override
    public void run() {
        System.out.println("Networking Server Started.");
        runServer = true;
        this.setDaemon(true);

        try {
            serverSocket = new ServerSocket(5000);
            socket = serverSocket.accept();
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            networkingLog.add("Server Instance started @" );

            //check password:
            while(runServer) {
                String inputString = input.readLine();
                System.out.println(inputString);
                ProcessIncomingData(inputString);
                UpdateUI();
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
                    if (BoardManager.getPlayerToMoveNext() == localPlayer.COLOR){
                        SendAlert("Not Your Turn", "Wait for your turn", "bastard");
                        return;
                    }
                    if (BoardManager.MovePiece(incomingMove)) {
                        networkingLog.add("SERVER:: MOVE INSTRUCTION RECEIVED :: " + incomingMove.toString());
                        SendBoardState();
                        Platform.runLater(()-> {
                            try {

                                uiController.UpdateUI();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }
                }

                case "BOARD" -> {
                    try {
                        SendBoardState();
                        Platform.runLater(()-> {
                            try {

                                uiController.UpdateUI();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                case "MESSAGE" -> {
                    networkingLog.add("Message Recieved: " );
                    Platform.runLater(()-> {
                        try {
                            uiController.addChatMessage(commandData[1]);
                            uiController.UpdateUI();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }

                case "COLOR"->{
                    networkingLog.add("Color Request: " );
                    PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                    String colortext = null;
                    if (localPlayer.COLOR == ChessTeamColor.BLACK)
                        colortext = ChessTeamColor.WHITE.toString();
                    if (localPlayer.COLOR == ChessTeamColor.WHITE)
                        colortext = ChessTeamColor.BLACK.toString();

                    output.println("SETCOLOR%" + colortext );
                }
            }

        }
    }

    private void UpdateUI(){
        Platform.runLater(() -> {
            try {
                uiController.UpdateUI();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public void SendBoardState() throws IOException {
        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
        networkingLog.add("SERVER:: Sending Board State :: ");
        output.println("FEN%"+BoardManager.SavePositionToFEN());
    }

    public void SyncTimerClocks() throws IOException {
        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
        networkingLog.add("SERVER:: Sending CLOCKS State :: ");
        output.println("SETCLOCKS%"+ BoardManager.chessClocks.GetClocks());
    }

    @Override
    public void SendMove(Move moveToSend) throws IOException {
        if(BoardManager.playerToMoveNext != localPlayer.COLOR){
            System.out.println("Wrong team color! - Ignoring Move.");

        }
        networkingLog.add("SERVER:: Sending Move :: " + moveToSend.toString());
        Platform.runLater(()->{
            if(BoardManager.MovePiece(moveToSend)){
                try {
                    SendFENString(BoardManager.SavePositionToFEN());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

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
        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
        output.println("ALERT%" + title + "%" + header +"%" + content);
        networkingLog.add("SERVER:: Alert Sent :: " + title);
    }

    @Override
    public void ReceiveAlert(String title, String header, String content) {
        MainUIController.DisplayAlert(title,header,content);
    }

    public void SendCapturedPieces() throws IOException {

        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
        StringBuilder outputString = new StringBuilder("CAPTURED%" + BoardManager.capturedPieces.size() + "%");

        for (ChessPiece cp: BoardManager.capturedPieces) {
                outputString.append(cp.exportChessPieceToFENChar()).append("%");
        }

        output.println(outputString.toString());
        networkingLog.add("SERVER:: Captured Pieces Sent :: " + outputString);
    }

    public void SendMoveHistory(){

        throw new NotImplementedError();

    }



    public void SendFENString (String FENString) throws IOException {
        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
        output.println("FEN%" + FENString);
        networkingLog.add("SERVER:: Sending FEN String :: " + FENString);
    }

    private void WriteToOutputStream(String rawString){}

}
