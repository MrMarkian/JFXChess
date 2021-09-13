package com.jfxchess.jfxchess.Data;

public class ChessGrid {
    public ChessPiece pieceOnGrid;
    public GridColor gridColor;

    public BoardLabels gridLabel;

    int location;

    public ChessGrid(ChessPiece pieceOnGrid, GridColor gridColor) {
        this.pieceOnGrid = pieceOnGrid;
        this.gridColor = gridColor;
    }

    public ChessGrid(ChessPiece pieceOnGrid, GridColor gridColor, int location) {
        this.pieceOnGrid = pieceOnGrid;
        this.gridColor = gridColor;
        this.location = location;
    }

    public ChessGrid(ChessPiece pieceOnGrid) {
        this.pieceOnGrid = pieceOnGrid;
    }
    public ChessGrid(ChessPiece pieceOnGrid, int location) {
        this.pieceOnGrid = pieceOnGrid;
        this.gridColor = CalculateGridColorFromLocation(location);
        this.location = location;

    }

    public BoardLabels getGridLabel() {
        return gridLabel;
    }

    public void setGridLabel(BoardLabels gridLabel) {
        this.gridLabel = gridLabel;
    }

    public void setLabelFromInt(int input){
        gridLabel = BoardLabels.values()[input];
    }

    public static BoardLabels getLabelFromInt(int input){
        return BoardLabels.values()[input];
    }

    private GridColor CalculateGridColorFromLocation(int location) {

        int x, y = 0;

        x = location % 8;
        y = location / 8;

        if ((x % 2) == (y % 2)) {
            return GridColor.BLACK;
        } else return GridColor.WHITE;
    }

    public  String printLocation(){
        int column_pos = location % 8;
        int row_pos = location / 8;
        return "X" + column_pos +"-"+ "Y"+row_pos;
    }

    @Override
    public String toString() {
        String pieceString = new String("");
        if (pieceOnGrid.teamColor != null)
            pieceString += pieceOnGrid.teamColor;
        if (pieceOnGrid.type != null)
            pieceString += pieceOnGrid.type;

        return  "pieceOnGrid=" + pieceString +
                ", gridColor=" + gridColor +
                ", location=" + printLocation() + "("+ location+
                " gridLabel=" + gridLabel+ ")";
    }

}
