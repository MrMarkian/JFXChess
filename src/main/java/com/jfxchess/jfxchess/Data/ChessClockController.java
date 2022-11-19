package com.jfxchess.jfxchess.Data;
import com.jfxchess.jfxchess.MainUIController;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;

import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

public class ChessClockController {
    public Duration whiteDuration;
    public Duration blackDuration;

    public ObservableValue<String> whiteTimeText;
    public ObservableValue<String> blackTimeText;

    private final Timer timer = new Timer();

    private MainUIController uiRef;

    private static ChessTeamColor currentTurn;


    public ChessClockController(MainUIController ref){
        timer.schedule(new timerRunner(), 1000, 1000);
        uiRef = ref;

    }

    public void InitTimers(long totalTime){
        whiteDuration = Duration.ofMinutes(totalTime);
        blackDuration = Duration.ofMinutes(totalTime);

    }

    public void SetCurrentTurn(ChessTeamColor team){
        currentTurn = team;
    }

    public void SetClocks(String times){
        String[] parsedData;
        parsedData = times.split("!");
        whiteDuration = Duration.parse(parsedData[0]);
        blackDuration = Duration.parse(parsedData[1]);
    }

    public String GetClocks(){
        return whiteDuration.toString() + "!" + blackDuration.toString();
    }

    public static String formatTime(Duration d) {

        long seconds = d.getSeconds();
        long absSeconds = Math.abs(seconds);
        String positive = String.format(
                "%d:%02d:%02d",
                absSeconds / 3600,
                (absSeconds % 3600) / 60,
                absSeconds % 60);
        return seconds < 0 ? "-" + positive : positive;
    }

    private class timerRunner extends TimerTask{

        @Override
        public void run() {
            DecrementTime(1);
        }
    }

    private void DecrementTime(long timeExpended){
            switch(currentTurn){
                case WHITE -> {
                    System.out.println("Decrementing white timer");
                    whiteDuration =  whiteDuration.minusSeconds(timeExpended);

                }
                case BLACK -> {
                    System.out.println("Decrementing black timer");
                    blackDuration = blackDuration.minusSeconds(timeExpended);

                }
            }

        UpdateClockLabels();
    }


    private void UpdateClockLabels(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                uiRef.WhiteClockLabel.setText("WHITE : " + formatTime(whiteDuration));
                uiRef.BlackClockLabel.setText("BLACK : " + formatTime(blackDuration));
            }
        });
    }


}
