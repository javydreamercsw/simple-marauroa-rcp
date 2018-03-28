package simple.marauroa.sound;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;

/**
 *
 * @author Andreas Stefik
 */
public class ThreadedSound implements Runnable {

    private final int BUFFER_SIZE = 128_000;
    private AudioInputStream audioStream;
    private AudioFormat audioFormat;
    private SourceDataLine sourceLine;
    private String soundFile = "";

    @Override
    public void run() {
        play();
    }

    private void play() {
        try {
            File path = new File(getSoundFile());
            audioStream = AudioSystem.getAudioInputStream(path);

            audioFormat = audioStream.getFormat();

            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            try {
                sourceLine = (SourceDataLine) AudioSystem.getLine(info);
                sourceLine.open(audioFormat);
            } catch (LineUnavailableException ex) {
                Logger.getLogger(ThreadedSound.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(SoundPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }


            sourceLine.start();

            int nBytesRead = 0;
            byte[] abData = new byte[BUFFER_SIZE];
            while (nBytesRead != -1) {
                try {
                    nBytesRead = audioStream.read(abData, 0, abData.length);
                } catch (IOException ex) {
                    Logger.getLogger(ThreadedSound.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (nBytesRead >= 0) {
                    sourceLine.write(abData, 0, nBytesRead);
                }
            }

            sourceLine.drain();
            sourceLine.close();

        } catch (UnsupportedAudioFileException | IOException ex) {
            Logger.getLogger(ThreadedSound.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the soundFile
     */
    public String getSoundFile() {
        return soundFile;
    }

    /**
     * @param soundFile the soundFile to set
     */
    public void setSoundFile(String soundFile) {
        this.soundFile = soundFile;
    }
}
