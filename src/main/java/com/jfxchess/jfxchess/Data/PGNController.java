package com.jfxchess.jfxchess.Data;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class PGNController {


    public PGNGame importSingleGame(Path fileName) throws IOException {

        return new PGNGame(getDiskFile_Lines(new File(fileName.toUri())));
    }


    public static String getDiskFile_Lines( File file ) throws IOException {
        StringBuilder text = new StringBuilder();
        FileInputStream fileStream = new FileInputStream( file );
        BufferedReader br = new BufferedReader( new InputStreamReader( fileStream ) );
        for ( String line; (line = br.readLine()) != null; )
            text.append(line).append(System.lineSeparator());
        return text.toString();
    }
}
