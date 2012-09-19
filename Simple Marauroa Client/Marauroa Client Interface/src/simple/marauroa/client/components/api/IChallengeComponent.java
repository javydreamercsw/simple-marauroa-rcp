package simple.marauroa.client.components.api;

import marauroa.common.game.RPEvent;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public interface IChallengeComponent {

    /**
     * Received a challenge
     *
     * @param event
     */
    void onChallenge(RPEvent event);

    /**
     * Challenge Accepted
     *
     * @param event
     */
    void onChallengeAccepted(RPEvent event);

    /**
     * Challenge rejected
     *
     * @param event
     */
    void onChallengeRejected(RPEvent event);

    /**
     * Challenge canceled
     *
     * @param event
     */
    void onChallengeCancelled(RPEvent event);
}
