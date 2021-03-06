package dreamer.card.game.gui;

import com.reflexit.magiccards.core.CannotDetermineSetAbbriviation;
import com.reflexit.magiccards.core.cache.ICardCache;
import com.reflexit.magiccards.core.model.Editions;
import com.reflexit.magiccards.core.model.ICard;
import com.reflexit.magiccards.core.model.ICardGame;
import com.reflexit.magiccards.core.model.ICardSet;
import com.reflexit.magiccards.core.model.storage.db.DBException;
import com.reflexit.magiccards.core.model.storage.db.IDataBaseCardStorage;
import dreamer.card.game.core.Tool;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//dreamer.card.game.gui//CardViewer//EN",
autostore = false)
@TopComponent.Description(preferredID = "CardViewerTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "navigator", openAtStartup = true, roles = "game_view")
@TopComponent.OpenActionRegistration(displayName = "#CTL_CardViewerAction")
@Messages({
    "CTL_CardViewerAction=Card Viewer",
    "CTL_CardViewerTopComponent=Card Viewer Window",
    "HINT_CardViewerTopComponent=Card Viewer window"
})
public final class CardViewerTopComponent extends TopComponent
        implements LookupListener {

    private static final Logger LOG = Logger.getLogger(CardViewerTopComponent.class.getName());
    private Lookup.Result<ICard> result = Utilities.actionsGlobalContext().lookupResult(ICard.class);

    public CardViewerTopComponent() {
        initComponents();
        setName(Bundle.CTL_CardViewerTopComponent());
        setToolTipText(Bundle.HINT_CardViewerTopComponent());
        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_MAXIMIZATION_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_KEEP_PREFERRED_SIZE_WHEN_SLIDED_IN, Boolean.TRUE);
        //Set up the listener stuff
        result.allItems();
        result.addLookupListener(CardViewerTopComponent.this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cardLabel = new javax.swing.JLabel();

        cardLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        org.openide.awt.Mnemonics.setLocalizedText(cardLabel, org.openide.util.NbBundle.getMessage(CardViewerTopComponent.class, "CardViewerTopComponent.cardLabel.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cardLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cardLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel cardLabel;
    // End of variables declaration//GEN-END:variables

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    public void resultChanged(LookupEvent le) {
        Lookup.Result res = (Lookup.Result) le.getSource();
        Collection instances = res.allInstances();

        if (!instances.isEmpty()) {
            Iterator it = instances.iterator();
            while (it.hasNext()) {
                Object item = it.next();
                if (item instanceof ICard) {
                    ICard card = (ICard) item;
                    try {
                        HashMap parameters = new HashMap();
                        parameters.put("name", card.getSetName());
                        List temp = Lookup.getDefault().lookup(IDataBaseCardStorage.class).namedQuery("CardSet.findByName", parameters);
                        if (temp.isEmpty()) {
                            cardLabel.setIcon(null);
                            cardLabel.setText("No card selected");
                        } else {
                            ICardSet cs = (ICardSet) temp.get(0);
                            ICardCache cache = null;
                            for (Iterator<? extends ICardGame> it2 = Lookup.getDefault().lookupAll(ICardGame.class).iterator(); it2.hasNext();) {
                                ICardGame game = it2.next();
                                if (game.getName().equals(cs.getGameName())) {
                                    List<ICardCache> impl = game.getCardCacheImplementations();
                                    if (!impl.isEmpty()) {
                                        cache = impl.get(0);
                                    }
                                }
                            }
                            ImageIcon icon = Tool.loadImage(this, ImageIO.read(cache.getCardImage(card, cs, cache.createRemoteImageURL(card, Editions.getInstance().getEditionByName(cs.getName())), true, false)));
                            icon.getImage().flush();
                            cardLabel.setIcon(icon);
                            cardLabel.setText("");
                        }
                    } catch (MalformedURLException ex) {
                        LOG.log(Level.SEVERE, null, ex);
                    } catch (CannotDetermineSetAbbriviation ex) {
                        LOG.log(Level.SEVERE, null, ex);
                    } catch (DBException ex) {
                        LOG.log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        LOG.log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
}
