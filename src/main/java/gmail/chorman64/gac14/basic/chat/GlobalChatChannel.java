package gmail.chorman64.gac14.basic.chat;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.function.BiPredicate;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class GlobalChatChannel extends BasicChatChannel {
	public static final UUID id = UUID.fromString("cb39a7c6-b831-11e7-abc4-cec278b6b50a");
	public static final ITextComponent PREFIX = new TextComponentString("");
	private MinecraftServer server;


	public GlobalChatChannel(MinecraftServer server) {
		super(id,PREFIX,"global");
		this.server = server;
	}


	@Override
	public Set<? extends ICommandSender> users() {
		Set<EntityPlayerMP> ret = new TreeSet<EntityPlayerMP>((a,b)->a.getUniqueID().compareTo(b.getUniqueID()));
		ret.addAll(server.getPlayerList().getPlayers());
		return ret;
	}


	@Override
	public Set<? extends ICommandSender> listeners() {
		// TODO Auto-generated method stub
		return users();
	}






}
