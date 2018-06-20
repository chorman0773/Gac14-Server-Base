package gmail.chorman64.gac14.basic.commands;

import java.util.Collections;
import java.util.List;

import gmail.chorman64.gac14.basic.Core;
import gmail.chorman64.gac14.basic.permission.IPermissibleAction;
import gmail.chorman64.gac14.basic.permission.IPermission;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public abstract class PermissibleCommandBase extends CommandBase implements IPermissibleAction {

	private IPermission node;
	private String name;

	private List<String> aliases;
	private String usage;


	public PermissibleCommandBase(IPermission node, String name,String usage,List<String> aliases) {
		this.node = node;
		this.name = name;
		this.aliases = aliases;
		this.usage = usage;
	}
	public PermissibleCommandBase(IPermission node, String name, String usage) {
		this(node,name,usage,Collections.emptyList());
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public String getUsage(ICommandSender sender) {

		return usage;
	}


	@Override
	public IPermission node() {
		// TODO Auto-generated method stub
		return node;
	}
	/* (non-Javadoc)
	 * @see net.minecraft.command.CommandBase#getRequiredPermissionLevel()
	 */
	@Override
	public int getRequiredPermissionLevel() {
		// TODO Auto-generated method stub
		return 0;
	}
	/* (non-Javadoc)
	 * @see net.minecraft.command.CommandBase#getAliases()
	 */
	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return aliases;
	}
	/* (non-Javadoc)
	 * @see net.minecraft.command.CommandBase#checkPermission(net.minecraft.server.MinecraftServer, net.minecraft.command.ICommandSender)
	 */
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		// TODO Auto-generated method stub
		return Core.instance.permissionManager.hasPermission(sender, node)&&super.checkPermission(server, sender);
	}

	public static EntityPlayerMP getSenderAsPlayer(ICommandSender sender) throws CommandException {
		if(sender instanceof EntityPlayerMP)
			return (EntityPlayerMP)sender;
		else if(sender.getCommandSenderEntity()!=null&&(sender.getCommandSenderEntity() instanceof EntityPlayerMP))
			return (EntityPlayerMP)sender.getCommandSenderEntity();
		else
			throw new PlayerNotFoundException(sender+" is not a player or command sender wrapping a player");
	}




}
