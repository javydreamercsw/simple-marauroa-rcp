package simple.marauroa.sound;

import java.io.File;
import org.openide.modules.InstalledFileLocator;

/**
 *
 * @author Andreas Stefik, with code borrowed from the web
 */
public class SoundPlayer {

    private static String soundFileRoot = "sound";
    private static String codeNameBase = "simple.marauroa.sound";
    private static File root = null;
    private static SoundPlayer player = null;

    /**
     * @return the soundFileRoot
     */
    public static String getSoundFileRoot() {
        return soundFileRoot;
    }

    /**
     * @param aSoundFileRoot the soundFileRoot to set
     */
    public static void setSoundFileRoot(String aSoundFileRoot) {
        soundFileRoot = aSoundFileRoot;
    }

    /**
     * @return the codeNameBase
     */
    public static String getCodeNameBase() {
        return codeNameBase;
    }

    /**
     * @param aCodeNameBase the codeNameBase to set
     */
    public static void setCodeNameBase(String aCodeNameBase) {
        codeNameBase = aCodeNameBase;
    }

    private SoundPlayer() {
        File file = InstalledFileLocator.getDefault().locate(
                soundFileRoot, codeNameBase, false);
        root = file;
    }

    public static synchronized SoundPlayer instance() {
        root = InstalledFileLocator.getDefault().locate(
                soundFileRoot, codeNameBase, false);
        if (player == null) {
            player = new SoundPlayer();
        }
        return player;
    }

    public void play(String name) {
        ThreadedSound sound = new ThreadedSound();
        File file = new File(root.getAbsolutePath() + "/" + name);
        String path = file.getAbsolutePath();
        sound.setSoundFile(path);
        Thread thread = new Thread(sound);
        thread.start();
    }
}
