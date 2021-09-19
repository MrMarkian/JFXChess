package com.jfxchess.jfxchess.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface NetworkingCommon {
     boolean isRunning = false;
      List<String> networkingLog = new ArrayList<>();

     void SendMove(Move moveToSend) throws IOException;
     void ReceiveMove(Move receivedMove);
     void QuitNetworking();
     List<String> PrintLog();

}
