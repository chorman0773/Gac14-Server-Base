package gmail.chorman64.gac14.basic.permission;

import net.minecraft.command.CommandResultStats.Type;
import gmail.chorman64.gac14.basic.Core;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class RootCommandSender implements IPermissible, ICommandSender {

	private String name;
	private MinecraftServer server;
	private TextComponentString disName;
	public static RootCommandSender sender = new RootCommandSender("Server",Core.instance.server);
	public RootCommandSender(String name,MinecraftServer s) {
		this.name = name;
		this.server = s;
		this.disName = new TextComponentString(name);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public ITextComponent getDisplayName() {
		// TODO Auto-generated method stub
		return disName;
	}

	@Override
	public void sendMessage(ITextComponent component) {
		server.sendMessage(component);
	}

	@Override
	public boolean canUseCommand(int permLevel, String commandName) {
		// TODO Auto-generated method stub
		return true;//The server can use all commands
	}

	@Override
	public BlockPos getPosition() {
		// TODO Auto-generated method stub
		return BlockPos.ORIGIN;
	}

	@Override
	public Vec3d getPositionVector() {
		// TODO Auto-generated method stub
		return Vec3d.ZERO;
	}

	@Override
	public World getEntityWorld() {
		// TODO Auto-generated method stub
		return server.getEntityWorld();
	}

	@Override
	public Entity getCommandSenderEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendCommandFeedback() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setCommandStat(Type type, int amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MinecraftServer getServer() {
		// TODO Auto-generated method stub
		return server;
	}

	@Override
	public boolean hasPermission(PlayerList l, IPermission p) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void givePermission(IPermission p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removePermission(IPermission p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isOperator(PlayerList l) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int getOperatorLevel(PlayerList l) {
		// TODO Auto-generated method stub
		return 4;
	}
	
	@Override
	public boolean isRoot() {
		return true;
	}

}
