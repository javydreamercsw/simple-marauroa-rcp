package simple.client.smileys;

import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import org.openide.modules.ModuleInstall;
import org.openide.util.lookup.ServiceProvider;
import simple.marauroa.application.api.IStringReplacer;

@ServiceProvider(service = IStringReplacer.class)
public class Installer extends ModuleInstall implements IStringReplacer {

    private static final Logger logger =
            Logger.getLogger(Installer.class.getCanonicalName());
    private static HashMap<String, String> smileys = new HashMap<String, String>();
    private String smileyPath = "resouces/images/smileys";

    @Override
    public void restored() {
        logger.fine("Initializing Smileys");
        addSmiley(":-)", "happy.gif");
        addSmiley(":-(", "sad.gif");
        addSmiley(":-o", "surprise.gif");
        addSmiley(":-d", "smile.gif");
        addSmiley(":-p", "tongue.gif");
        addSmiley(":-i", "no-expression.gif");
        addSmiley(";-)", "wink.gif");
        addSmiley(">-@", "mad.gif");
        addSmiley("8-)", "cool.gif");
        addSmiley(":'-(", "tear.gif");
        addSmiley("(y)", "yes.gif");
        addSmiley("(n)", "no.gif");
        addSmiley("lol", "LOL.gif");
        addSmiley(":-$", "shhhh.gif");
        addSmiley("brb", "BRB.gif");
    }

    private void addSmiley(String s, String imagePath) {
        if (s.contains("-")) {
            logger.log(Level.INFO, "Adding: {0}: {1}",
                    new Object[]{s.toLowerCase().replaceAll("-", ""), imagePath});
            smileys.put(s.toLowerCase().replaceAll("-", ""), smileyPath
                    + imagePath);
        }
        logger.log(Level.INFO, "Adding: {0}: {1}",
                new Object[]{s, imagePath});
        smileys.put(s.toLowerCase(), smileyPath + imagePath);
    }

    /**
     *
     * @param txt
     * @return
     */
    public ImageIcon getSmiley(String txt) {
        if (isSmiley(txt)) {
            return loadImage(smileys.get(txt.toLowerCase().replaceAll("-", "")));
        }
        return null;
    }

    private ImageIcon loadImage(String image_name) {
        URL image_url = null;
        try {
            image_url = getClass().getResource(image_name);
            if (image_url != null) {
                return new ImageIcon(image_url);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        } finally {
            image_url = null;
        }
    }

    /**
     *
     * @param txt
     * @return
     */
    public boolean isSmiley(String txt) {
        return smileys.containsKey(txt.toLowerCase()) && smileys.get(txt) != null;
    }

    @Override
    public boolean hasReplacement(String text) {
        return smileys.containsKey(text);
    }

    @Override
    public Object getReplacementObject(String text) {
        return getSmiley(text);
    }

    @Override
    public String getReplacementString(String text) {
        return null;
    }

    @Override
    public ImageIcon getReplacementImageIcon(String text) {
        return getSmiley(text);
    }

    @Override
    public boolean supportsString() {
        return false;
    }

    @Override
    public boolean supportsObject() {
        return false;
    }

    @Override
    public boolean supportsImageIcon() {
        return true;
    }
}
