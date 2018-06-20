package gmail.chorman64.gac14.basic.chat;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.ITextComponent;

/**
 * Called to check for a chatter's prefix to a user.
 * This is assigned to its user
 * @author Connor Horman
 *
 */
public interface IPlayerBasedPrefixHandler {
	/**
	 * Gets the prefix for a message sender to its user
	 * @param msgSender
	 * @return the prefix;
	 */
 ITextComponent getPrefix(ICommandSender msgSender);
}
