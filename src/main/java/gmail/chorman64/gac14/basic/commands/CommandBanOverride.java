package gmail.chorman64.gac14.basic.commands;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import gmail.chorman64.gac14.basic.Core;
import gmail.chorman64.gac14.basic.RegexConstants;
import gmail.chorman64.gac14.basic.players.PlayerProfile;
import gmail.chorman64.gac14.basic.players.event.PlayerBanEvent;
import gmail.chorman64.gac14.basic.players.permission.ManagementPermission;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.server.management.UserListBans;
import net.minecraft.server.management.UserListBansEntry;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;

public class CommandBanOverride extends PermissibleCommandBase implements RegexConstants {

	public CommandBanOverride() {
		super(ManagementPermission.BAN, "ban", "/ban <player> [time] [reason ...] [-s]");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

		if(args.length==0)throw new WrongUsageException(getUsage(sender));
		String pname = args[0];
		long time = (args.length>1&&args[1].matches("(Infinity|[0-9]+)")?parseTime(args[0]):1L<<48);
		Instant now = Instant.now();
		Instant then = now.plus(time, ChronoUnit.SECONDS);
		boolean silent = false;
		boolean kick = time==0;
		if(args[args.length-1].equals("-s"))
			silent = true;
		String reason = kick?"Kicked by Operator":"Banned by Operator";
		if(args.length!=(silent?2:1)) {
			String[] newargs = new String[args.length-(silent?2:1)];
			System.arraycopy(args, 1, newargs, 0, newargs.length);
		}
		GameProfile prof = pname.matches(uuid)?server.getPlayerProfileCache().getProfileByUUID(UUID.fromString(pname)):server.getPlayerProfileCache().getGameProfileForUsername(pname);
		if(prof==null)
			throw new CommandException("Failed to ban player "+pname+" player does not exist");
		PlayerList list = Core.instance.server.getPlayerList();
		EntityPlayerMP player = list.getPlayerByUUID(prof.getId());
		boolean canBan =true;
		if(player!=null) {
				PlayerProfile pp = PlayerProfile.get(player);
				if(pp!=null)
					canBan = !pp.hasPermission(list, ManagementPermission.BLOCK_BAN);
		}
		canBan = canBan&&!MinecraftForge.EVENT_BUS.post(new PlayerBanEvent(prof,sender));
		canBan = canBan||Core.instance.permissionManager.hasPermission(sender, ManagementPermission.OVERRIDES_BAN);
		if(!canBan) {
			CommandBase.notifyCommandListener(sender, this, "You do not have permission to ban this player");
			if(player!=null)
				player.sendMessage(new TextComponentString("\u00a74Ban Management\u00a7r: "+sender.getName()+" attempted to ban you're account but failed"));
		}else {
			if(!kick) {
			UserListBans bans = list.getBannedPlayers();
			UserListBansEntry ban = new UserListBansEntry(prof, Date.from(now), reason, Date.from(then),reason);
			bans.addEntry(ban);
			}
			if(player!=null)
				player.connection.disconnect(reason);
			notifyPunishmentAction(sender,kick,time,silent,prof.getName(),reason);
		}

	}

	private void notifyPunishmentAction(ICommandSender sender, boolean kick, long time, boolean silent, String name,String reason) {
		String punishment;
		if(kick)
			punishment = String.format("\u00a79Kicked %s\u00a7r", name);
		else if(time>=(1<<48L))
			punishment = String.format("\u00a74Permanently Banned %s\u00a7r", name);
		else {
			Duration dur = Duration.of(time, ChronoUnit.SECONDS);

			punishment = String.format("\u00a7cBanned %s for %s\u00a7r", name,dur.toString());
		}
		String msg = String.format("\u00a74Gac14 Bans\u00a7r: %s %s for %s", sender.getDisplayName().getFormattedText(),punishment,reason);
		if(silent)
			Core.instance.admin.sendInternalMessage(new TextComponentString(msg));
		else
			Core.instance.chat.sendInternalMessage(new TextComponentString(msg));

	}

	private static long parseTime(String val) {
		if(val.equals("Infinity"))
			return 1L<<48;
		return Long.parseLong(val);
	}

}
