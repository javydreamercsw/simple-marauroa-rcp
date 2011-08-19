/*
 * $Rev$
 * $LastChangedDate$
 * $LastChangedBy$
 */
package simple.server.core.action.chat;

import simple.server.core.action.CommandCenter;
import simple.server.core.engine.SimpleSingletonRepository;
import simple.common.game.ClientObjectInterface;
import simple.server.core.entity.clientobject.GagManager;
import simple.server.util.TimeUtil;
import marauroa.common.game.RPAction;

/**
 * Processes /chat, /tell (/msg) and /support.
 */
public class ChatAction {

    private static final String _SUPPORT = "support";
    public static final String _TELL = "tell";
    public static final String _CHAT = "chat";
    private static final String _ANSWER = "answer";

    /**
     * Registers AnswerAction ChatAction TellAction and SupportAction.
     */
    public static void register() {
        CommandCenter.register(_ANSWER, new AnswerAction());
        CommandCenter.register(_CHAT, new PublicChatAction());
        CommandCenter.register(_TELL, new TellAction());
        CommandCenter.register(_SUPPORT, new AskForSupportAction());
    }

    public void onAction(final ClientObjectInterface player, final RPAction action) {
        if (GagManager.isGagged(player)) {
            long timeRemaining = SimpleSingletonRepository.get().get(GagManager.class).getTimeRemaining(player);
            player.sendPrivateText("You are gagged, it will expire in " + TimeUtil.approxTimeUntil((int) (timeRemaining / 1000L)));
            return;
        }
    }
}
