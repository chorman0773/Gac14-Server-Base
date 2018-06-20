package gmail.chorman64.gac14.basic.commands;

import java.util.List;

import gmail.chorman64.gac14.basic.Core;
import gmail.chorman64.gac14.basic.permission.IPermission;
import gmail.chorman64.gac14.basic.players.PlayerProfile;
import gmail.chorman64.gac14.basic.players.permission.ManagementPermission;
import gmail.chorman64.gac14.basic.players.permission.OperatorPermission;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

/**
 * Comand which can preform queries on player profiles.
 * A Query can take several forms:
 * To add, remove, or query a permission use +, -, or ? respectively, followed by the permission to add, remove, or query.
 * To read a property use {@literal <}key{@literal >} get.
 * To assinng a property
 * @author Connor Horman
 *
 */
public class CommandPex extends PermissibleCommandBase {


	public CommandPex() {
		super(OperatorPermission.COMMAND_PEX, "pex", "/pex user <player> [<+|-|?><permission>|<key> get|<key> set <value>]");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length<2)
			throw new WrongUsageException(getUsage(sender));
		EntityPlayerMP player = CommandBase.getPlayer(server, sender, args[1]);
		PlayerProfile prof = PlayerProfile.get(player);
		if(args.length==2) {
			String[] msg = prof.dump();
			for(String s:msg)
				sender.sendMessage(new TextComponentString(s));
		}else {
			String query = CommandBase.buildString(args, 2);

				if(prof.isManagement(query)&&!Core.instance.permissionManager.hasPermission(sender, ManagementPermission.PLAYERS))
					throw new CommandException("You do not have permission to preform that action");
				else
					query = prof.query(query);
			query = String.format("pex command targeting %s-> result: %s", args[1],query);
			CommandBase.notifyCommandListener(sender, this, query);
		}

	}

}
