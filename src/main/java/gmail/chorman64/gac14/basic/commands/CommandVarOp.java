package gmail.chorman64.gac14.basic.commands;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import gmail.chorman64.gac14.basic.RegexConstants;
import gmail.chorman64.gac14.basic.players.permission.ManagementPermission;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListOps;
import net.minecraft.server.management.UserListOpsEntry;

public class CommandVarOp extends PermissibleCommandBase implements RegexConstants {



	public CommandVarOp() {
		super(ManagementPermission.OP, "op", "/op <player> [level=default] [bypassesPlayerLimit=level>2]");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length==0)
			throw new WrongUsageException(getUsage(sender));
		int level = args.length>1?CommandBase.parseInt(args[1], 1, 4):server.getOpPermissionLevel();
		if(!sender.canUseCommand(level, "op"))
			throw new CommandException("You do not have permission to set this level");
		boolean bypassesPlayerLimit = args.length>2?Boolean.parseBoolean(args[3]):level>2;
		GameProfile prof = args[0].matches(uuid)?server.getPlayerProfileCache().getProfileByUUID(UUID.fromString(args[0])):server.getPlayerProfileCache().getGameProfileForUsername(args[0]);
		UserListOps ops = server.getPlayerList().getOppedPlayers();
		if(ops.getPermissionLevel(prof)>0) {
			if(ops.getPermissionLevel(prof)>level)
				throw new CommandException("This command cannot reduce the permission level of a player");
			ops.removeEntry(prof);
		}
		UserListOpsEntry op = new UserListOpsEntry(prof, level, bypassesPlayerLimit);
		ops.addEntry(op);
		CommandBase.notifyCommandListener(sender, this,"Opped %s with permission level %s.",prof.getName(),Integer.toString(level));
	}

}
