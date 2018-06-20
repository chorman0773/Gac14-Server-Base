package gmail.chorman64.gac14.basic.chat;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import gmail.chorman64.gac14.basic.Core;
import gmail.chorman64.gac14.basic.players.permission.OperatorPermission;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class AdminChatChannel extends BasicChatChannel {
	public static final UUID channelID = UUID.fromString("5bd06c84-b837-11e7-abc4-cec278b6b50a");
	public static final ITextComponent PREFIX = new TextComponentString("Admin Only").setStyle(new Style().setColor(TextFormatting.GREEN));

	private MinecraftServer server;
	public AdminChatChannel(MinecraftServer server) {
		super(channelID,PREFIX,"Admin Only");
		this.server = server;
	}
	@Override
	public Set<? extends ICommandSender> users() {

		return server.getPlayerList().getPlayers().stream()
				.filter(s->Core.instance.permissionManager.hasPermission(s, OperatorPermission.COMMAND_AO))
				.collect(Collectors.toSet());
	}
	@Override
	public Set<? extends ICommandSender> listeners() {
		// TODO Auto-generated method stub
		return users();
	}



}
