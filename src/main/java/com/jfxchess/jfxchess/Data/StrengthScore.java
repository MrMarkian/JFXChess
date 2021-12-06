package com.jfxchess.jfxchess.Data;

public class StrengthScore {
    int WhiteTeam=0;
    int BlackTeam=0;

    public StrengthScore(int whiteTeam, int blackTeam) {
        WhiteTeam = whiteTeam;
        BlackTeam = blackTeam;
    }

    public StrengthScore() {
    }

    public int getWhiteTeam() {
        return WhiteTeam;
    }

    public void setWhiteTeam(int whiteTeam) {
        WhiteTeam = whiteTeam;
    }

    public String getBlackText(){
        return String.valueOf(BlackTeam);
    }

    public String getWhiteText(){
        return String.valueOf(WhiteTeam);
    }
    public int getBlackTeam() {
        return BlackTeam;
    }

    public void setBlackTeam(int blackTeam) {
        BlackTeam = blackTeam;
    }
}
