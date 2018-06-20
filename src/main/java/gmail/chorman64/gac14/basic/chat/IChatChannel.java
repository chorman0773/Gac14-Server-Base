package gmail.chorman64.gac14.basic.chat;

import java.util.UUID;
import java.util.function.BiFunction;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.ITextComponent;

/**
 * IChatChannel is a general interface that discribes a channel in chat.
 * It is heveally discoraged to use sendMessage, but rather Core.instance.sendMessage(IChatChannel,ITextComponent).
 * IChatChannels not only allow for chatters to be muted or deafend, but to apply filters.
 * This can also provide a spam timeout
 * @author Connor Horman
 *
 */
public interface IChatChannel {
	/**
	 * Checks if a command sender is a member of this channel.
	 * This is whether or not the sender can recieve messages on this channel, without including spies
	 * This does not filter deafened users
	 * @param s the command sender
	 * @return true if the given sender is allowed to listen to this channel, excluding channel spies.
	 */
	boolean isMember(ICommandSender s);
	/**
	 * Checks if a command sender can send messages on this channel.
	 * This ignores if a sender is muted, or is on Chat cooldown
	 * @param s the command sender
	 * @return true if the sender is allowed to send messages on the channel, excluding  muted users or cooldown
	 */
	boolean canSendMessages(ICommandSender s);

	/**
	 * Checks if the sender is able to send Messsages but is muted.
	 * @param s
	 * @return true if the sender is able to send messages on this channel
	 */
	boolean isMuted(ICommandSender s);

	/**
	 * Sets the muted state of the command sender. If the sender cannot be muted or is not allowed to send messages on this channel,
	 * the method should fail silently. Otherwise, for any non-null ICommandSender s, and boolean b
	 * calling setMuted(s,b), should update the muted state such that, unless an update which changes the muted state of the sender ocurred,
	 * isMuted(s) should return true if and only if b equals true
	 * @param s
	 */
	void setMuted(ICommandSender s,boolean b);
	/**
	 * Checks if a sender can be muted in this channel. A sender which cannot send messages in this channel is never mutable.
	 * @param s the sender
	 * @return whether or not the sender can be muted in this channel.
	 */
	boolean isMutable(ICommandSender s);

	/**
	 * Checks if a sender is unable to recieve messages in this channel, but is a member of this channel.
	 * @param s The channle to check
	 * @return Whether or not the user can recieve messages
	 */
	boolean isDeaf(ICommandSender s);

	/**
	 * Sets the deafend state of the sender. If the sender canot be muted or is not a member of this channel,
	 * the method should fail silently. Otherwise for any non-null ICommandSender s, and boolean b
	 * calling setDeaf(s,b), should update the deaf state such that, unless an update which changes the deaf state of the sender occurred,
	 * isMuted(s) should return true if and only if b equals true
	 * @param s
	 * @param b
	 */
	void setDeaf(ICommandSender s,boolean b);

	/**
	 * Checks if a sender can be deafened. A sender can never be deafened if its not a member of the channel.
	 * @param s
	 * @return
	 */
	boolean isDeafenable(ICommandSender s);

	/**
	 * The id of the channel. Used to identify this channel.
	 * Equals should simply compare channelID();
	 * 2 channels that are different may never have the same channelID.
	 */
	UUID channelID();

	/**
	 * The name of the channel. This does not need to follow any naming convention, but should be descriptive.
	 * For example a Private Message channel would be {@literal <}Sender{@literal>}{@literal ->}{@literal <}Reciver{@literal >}
	 * @return a Descriptive name of this channel
	 */
	String channelName();
	/**
	 * returns a prefix for messsages from a sender to a reciever.
	 * @param reciever
	 * @param sender
	 * @return
	 */
	ITextComponent getPrefix(ICommandSender reciever, ICommandSender sender);
	void setPrefixHandler(BiFunction<ICommandSender,ICommandSender,? extends ITextComponent> prefixHandler);

	ITextComponent getGeneralPrefix(ICommandSender sender);
	void setGeneralPrefix(ICommandSender sender,ITextComponent prefix);

	ITextComponent getGeneralSuffix(ICommandSender sender);
	void setGeneralSuffix(ICommandSender sender,ITextComponent suffix);

	/**
	 * Checks if the sender is a spy on the channel.
	 * @param s the sender
	 * @return true if the sender is a spy in this channel
	 */
	boolean isSpy(ICommandSender s);

	/**
	 * Adds the sender as a spy to this channel. Without a call to removeSpy, or a update which changes spies to this channel.
	 * For any non-null ICommandSender s, after a call to addSpy(s); isSpy(s) should return true.
	 * A sender may not be added as a spy if it is already as spy, in which case this method should fail silently.
	 * @param s the sender
	 */
	void addSpy(ICommandSender s);

	/**
	 * Adds the sender as a spy to this channel. Without a call to adSpy, or a update which changes spies to this channel.
	 * For any non-null ICommandSender s, after a call to removeSpy(s); isSpy(s) should return false.
	 * A sender may not be remvoed as a spy, if it is not a spy in this channel,
	 * @param s the sender
	 */
	void removeSpy(ICommandSender s);

	/**
	 * Returns true if the sender can send the given message.
	 * This checks if the sender can send messages,
	 * Then checks if the sender is muted.
	 * Finally it checks if the message passes all filters which the sender does not bypass.
	 * @param s
	 * @param msg
	 * @return
	 */
	boolean canSendMessage(ICommandSender s,String msg);

	boolean sendMessage(ICommandSender s,ITextComponent msg);

	void sendInternalMessage(ITextComponent msg);
	void recieveInternalMessage(ICommandSender reciever,ITextComponent msg);

	void recieveMessage(ICommandSender reciever,ICommandSender sender, ITextComponent msg);

	String channelPrefix();
}
