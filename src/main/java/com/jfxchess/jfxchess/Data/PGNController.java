package com.jfxchess.jfxchess.Data;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PGNController {


    final List<PGNGame> pgnGamesDataBase = new ArrayList<>();


    public PGNGame importSingleGame(Path fileName) throws IOException {
        return new PGNGame(getDiskFile_Lines(new File(fileName.toUri())));
    }


    public void LoadFullDatabase(Path fileName) throws IOException{
        String rawData = Files.readString(fileName, StandardCharsets.US_ASCII);
        String[] dataSplitter = rawData.split("\n\n");

        for(int counter = 0; counter < dataSplitter.length /2; counter +=2)
        {
            PGNGame loadPGN = new PGNGame();
            loadPGN.parseHeaders(dataSplitter[counter]);
            loadPGN.setMoves(dataSplitter[counter+1]);
            pgnGamesDataBase.add(loadPGN);
        }
    }

    //Todo: Read the full spec and work this out

    public Move ConvertToMove(String pgnMove){
        return new Move();
    }

    public static String getDiskFile_Lines( File file ) throws IOException {
        StringBuilder text = new StringBuilder();
        FileInputStream fileStream = new FileInputStream( file );
        BufferedReader br = new BufferedReader( new InputStreamReader( fileStream ) );
        for ( String line; (line = br.readLine()) != null; )
            text.append(line).append(System.lineSeparator());
        return text.toString();
    }

    public List<PGNGame> getPgnGamesDataBase() {
        return pgnGamesDataBase;
    }


}
