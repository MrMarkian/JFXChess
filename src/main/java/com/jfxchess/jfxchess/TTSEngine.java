package com.jfxchess.jfxchess;
import com.jfxchess.jfxchess.Data.BoardManager;
import com.jfxchess.jfxchess.Data.Move;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import javafx.application.Platform;

import javax.speech.AudioException;
import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import java.util.Locale;

public class TTSEngine {

    Synthesizer synthesizer;

    public TTSEngine() throws EngineException, AudioException, InterruptedException {
        System.setProperty( "freetts.voices",
                "com.sun.speech.freetts.en.us"
                        + ".cmu_us_kal.KevinVoiceDirectory");
        Central.registerEngineCentral( "com.sun.speech.freetts"
                + ".jsapi.FreeTTSEngineCentral");

        synthesizer = Central.createSynthesizer(new SynthesizerModeDesc(Locale.ENGLISH));
    }

    public void Speak(String textToSpeak){
        try {
            synthesizer.allocate();
            synthesizer.resume();
            synthesizer.speakPlainText(textToSpeak, null);
            synthesizer.waitEngineState(Synthesizer.QUEUE_NOT_EMPTY);

        } catch (EngineException e) {
            throw new RuntimeException(e);
        } catch (AudioException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public void SpeakMove(Move moveToSay){

        Move outputMove = BoardManager.ruleBook.TestForCapture(moveToSay);
                if (outputMove.isWillResultInCapture()){
                    Speak(outputMove.toString().replace("=",""));
                    System.out.println("Speaking Move with Capture" + moveToSay);
                }else
                {
                    System.out.println("Speaking Move with Capture" + moveToSay);
                    Speak(moveToSay.toString());
                }
    }

}
