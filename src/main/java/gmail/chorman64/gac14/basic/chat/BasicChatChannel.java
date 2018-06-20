package gmail.chorman64.gac14.basic.chat;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import com.google.common.base.Objects;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public abstract class BasicChatChannel implements IChatChannel {

	private UUID id;
	private ITextComponent prefix;
	private String name;
	private Set<ICommandSender> muted;
	private Set<ICommandSender> deafend;
	private Map<ICommandSender,ITextComponent> prefixes;
	private Map<ICommandSender,ITextComponent> suffixes;
	private BiFunction<ICommandSender,ICommandSender,? extends ITextComponent> prefixHandler;
	private Set<ICommandSender> spies;
	private Set<BiPredicate<ICommandSender,String>> filters;
	private static final ITextComponent DEFAULT = new TextComponentString("");
	public BasicChatChannel(UUID id,ITextComponent prefix,String name) {
		this.id = id;
		this.prefix = prefix;
		this.name = name;
	}


	@Override
	public boolean isMuted(ICommandSender s) {
		// TODO Auto-generated method stub
		return muted.contains(s)&&isMutable(s);
	}

	@Override
	public void setMuted(ICommandSender s, boolean b) {
		if(b)
			muted.add(s);
		else
			muted.remove(s);
	}

	@Override
	public boolean isMutable(ICommandSender s) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isDeaf(ICommandSender s) {
		// TODO Auto-generated method stub
		return deafend.contains(s)&&isDeafenable(s);
	}

	@Override
	public void setDeaf(ICommandSender s, boolean b) {
		if(b)
			deafend.add(s);
		else
			deafend.remove(s);

	}

	@Override
	public boolean isDeafenable(ICommandSender s) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public UUID channelID() {
		// TODO Auto-generated method stub
		return id;
	}
	public BasicChatChannel addFilter(BiPredicate<ICommandSender,String> filter) {
		this.filters.add(filter);
		return this;
	}
	public BasicChatChannel addFilter(Predicate<String> filter) {
		return this.addFilter((t,s)->filter.test(s));
	}

	@Override
	public String channelName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public ITextComponent getPrefix(ICommandSender reciever, ICommandSender sender) {
		ITextComponent ret = (this.prefixHandler!=null?this.prefixHandler.apply(sender,reciever):DEFAULT);
		return (ret!=null?ret:DEFAULT).createCopy();
	}

	@Override
	public void setPrefixHandler(BiFunction<ICommandSender,ICommandSender,? extends ITextComponent> handler) {
		this.prefixHandler = handler;

	}

	@Override
	public ITextComponent getGeneralPrefix(ICommandSender sender) {
		// TODO Auto-generated method stub
		return prefixes.getOrDefault(sender,DEFAULT.createCopy());
	}

	@Override
	public void setGeneralPrefix(ICommandSender sender, ITextComponent prefix) {
		// TODO Auto-generated method stub
		prefixes.put(sender, prefix);
	}

	@Override
	public ITextComponent getGeneralSuffix(ICommandSender sender) {
		// TODO Auto-generated method stub
		return suffixes.getOrDefault(sender,DEFAULT);
	}

	@Override
	public void setGeneralSuffix(ICommandSender sender, ITextComponent suffix) {
		suffixes.put(sender, suffix);
	}

	@Override
	public boolean isSpy(ICommandSender s) {
		// TODO Auto-generated method stub
		return spies.contains(s);
	}

	@Override
	public void addSpy(ICommandSender s) {
		spies.add(s);
	}

	@Override
	public void removeSpy(ICommandSender s) {
		spies.remove(s);

	}
	/* (non-Javadoc)
	 * @see gmail.chorman64.gac14.basic.chat.IChatChannel#isMember(net.minecraft.command.ICommandSender)
	 */
	@Override
	public boolean isMember(ICommandSender s) {
		// TODO Auto-generated method stub
		return listeners().contains(s);
	}


	/* (non-Javadoc)
	 * @see gmail.chorman64.gac14.basic.chat.IChatChannel#canSendMessages(net.minecraft.command.ICommandSender)
	 */
	@Override
	public boolean canSendMessages(ICommandSender s) {
		// TODO Auto-generated method stub
		return users().contains(s);
	}


	/**
	 * Returns all users who can send messages in the channel. (Ignoring if they are muted)
	 */
	public abstract Set<? extends ICommandSender> users();

	/**
	 * Returns all users who can normally listen to messages in this channel. (Not including deafend users)
	 */
	public abstract Set<? extends ICommandSender> listeners();


	@Override
	public boolean canSendMessage(ICommandSender s, String msg) {
		if(!canSendMessages(s))
			return false;
		else if(isMuted(s))
			return false;
		else for(BiPredicate<ICommandSender,String> filter:filters)
			if(filter.test(s, msg))
				return false;
		return true;
	}

	@Override
	public boolean sendMessage(ICommandSender s, ITextComponent msg) {
		if(!canSendMessage(s,msg.getUnformattedText()))
			return false;
		else for(ICommandSender listen:listeners())
			if(!isDeaf(listen))
				recieveMessage(listen,s,msg);

		return true;
	}

	@Override
	public void sendInternalMessage(ITextComponent msg) {
		for(ICommandSender listen:listeners())
			if(!isDeaf(listen))
				recieveInternalMessage(listen,msg);

	}

	@Override
	public void recieveInternalMessage(ICommandSender reciever, ITextComponent msg) {
		ITextComponent realMSG = prefix.createCopy().appendSibling(msg);
		reciever.sendMessage(realMSG);
	}

	@Override
	public void recieveMessage(ICommandSender reciever, ICommandSender sender, ITextComponent msg) {
		ITextComponent realMSG = prefix.createCopy().appendText("").appendSibling(getGeneralPrefix(sender))
				.appendText("").appendSibling(getPrefix(reciever,sender))
				.appendText("").appendSibling(sender.getDisplayName())
				.appendSibling(getGeneralSuffix(sender)).appendText(">>")
				.appendSibling(msg);
		reciever.sendMessage(realMSG);
	}

	@Override
	public String channelPrefix() {
		// TODO Auto-generated method stub
		return prefix.getUnformattedText();
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return Objects.toStringHelper(this).add("id", id).add("name", name).toString();
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof BasicChatChannel)) {
			return false;
		}
		BasicChatChannel other = (BasicChatChannel) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

}
