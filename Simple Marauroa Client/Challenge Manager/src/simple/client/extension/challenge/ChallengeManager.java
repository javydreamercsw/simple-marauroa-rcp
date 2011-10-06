package simple.client.extension.challenge;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import marauroa.common.game.RPAction;
import marauroa.common.game.RPEvent;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import simple.marauroa.client.components.api.IChallengeComponent;
import simple.marauroa.client.components.api.IChallengeManager;
import simple.marauroa.client.components.common.MCITool;
import simple.server.extension.ChallengeEvent;
import simple.server.extension.ChallengeExtension;

/**
 * IChallengeManager, also the default IChallengeComponent
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@ServiceProviders({
    @ServiceProvider(service = IChallengeManager.class),
    @ServiceProvider(service = IChallengeComponent.class)})
public class ChallengeManager implements IChallengeManager, IChallengeComponent {

    private static final Logger logger =
            Logger.getLogger(ChallengeManager.class.getSimpleName());

    @Override
    public void onRPEventReceived(RPEvent event) throws Exception {
        logger.log(Level.INFO, "Challenge event received: {0}", event);
        int action = event.getInt(ChallengeEvent.getAction());
        switch (action) {
            case ChallengeEvent.ACCEPT:
                challengeAccepted(event);
                break;
            case ChallengeEvent.REJECT:
                challengeRejected(event);
                break;
            case ChallengeEvent.CHALLENGE:
                challenged(event);
                break;
            case ChallengeEvent.CANCEL:
                canceled(event);
                break;
            default:
                logger.log(Level.SEVERE, "Invalid challenge action: {0}", action);
        }
    }

    private void canceled(RPEvent event) {
        try {
            logger.info("Challenge canceled");
            Lookup.getDefault().lookup(IChallengeComponent.class).onChallengeCancelled(event);
        } catch (Exception e) {
            Exceptions.printStackTrace(e);
        }
    }

    private void challengeAccepted(RPEvent event) {
        try {
            logger.info("Challenge accepted");
            Lookup.getDefault().lookup(IChallengeComponent.class).onChallengeAccepted(event);
        } catch (Exception e) {
            Exceptions.printStackTrace(e);
        }
    }

    private void challengeRejected(RPEvent event) {
        try {
            logger.info("Challenge rejected");
            Lookup.getDefault().lookup(IChallengeComponent.class).onChallengeRejected(event);
        } catch (Exception e) {
            Exceptions.printStackTrace(e);
        }
    }

    private void challenged(RPEvent event) {
        try {
            logger.info("Challenged!");
            Lookup.getDefault().lookup(IChallengeComponent.class).onChallenge(event);
        } catch (Exception e) {
            Exceptions.printStackTrace(e);
        }
    }

    @Override
    public void onChallenge(RPEvent event) {
        Object[] options = {"Accept", "Reject"};
        int n = JOptionPane.showOptionDialog(null,
                event.get(ChallengeEvent.CHALLENGER) + " "
                + "is challenging you.", "Challenge",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, //do not use a custom Icon
                options, //the titles of buttons
                options[0]); //default button title
        if (n == JOptionPane.YES_OPTION) {
            acceptChallenge(event);
        } else {
            rejectChallenge(event);
        }
    }

    @Override
    public void onChallengeAccepted(RPEvent event) {
        JOptionPane.showMessageDialog(null,
                event.get(ChallengeEvent.CHALLENGED) + " "
                + " accepted your challenge",
                "Challenge Accepted",
                JOptionPane.INFORMATION_MESSAGE);
        //TODO: react on accepting the challenge
    }

    @Override
    public void onChallengeRejected(RPEvent event) {
        JOptionPane.showMessageDialog(null,
                event.get(ChallengeEvent.CHALLENGED) + " "
                + "rejected your challenge",
                "Challenge Rejected",
                JOptionPane.ERROR_MESSAGE);
        //TODO: Get ready to issue another challenge!
    }

    @Override
    public void onChallengeCancelled(RPEvent event) {
        JOptionPane.getRootFrame().dispose();
        JOptionPane.showMessageDialog(null,
                event.get(ChallengeEvent.CHALLENGER) + " "
                + "rejected your challenge."
                + (event.has("text") ? " " + "Reason"
                + ": " + event.get("text") : ""),
                "Challenge Cancelled",
                JOptionPane.ERROR_MESSAGE);
    }

    private void rejectChallenge(RPEvent event) {
        //Challenge rejected
        RPAction action = new RPAction();
        action.put("type", ChallengeExtension._REJECT_CHALLENGE);
        action.put(ChallengeEvent.CHALLENGER, event.get(ChallengeEvent.CHALLENGER));
        action.put(ChallengeEvent.CHALLENGED, MCITool.getClient().getPlayerRPC().getName());
        MCITool.getClient().send(action);
    }

    private void acceptChallenge(RPEvent event) {
        //Challenge accepted
        RPAction action = new RPAction();
        action.put("type", ChallengeExtension._ACCEPT_CHALLENGE);
        action.put(ChallengeEvent.CHALLENGER, event.get(ChallengeEvent.CHALLENGER));
        action.put(ChallengeEvent.CHALLENGED, MCITool.getClient().getPlayerRPC().getName());
        MCITool.getClient().send(action);
        //TODO: react on accepting the challenge
    }
}
