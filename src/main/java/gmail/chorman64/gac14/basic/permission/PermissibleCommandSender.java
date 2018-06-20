package gmail.chorman64.gac14.basic.permission;

import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;

import net.minecraft.command.CommandResultStats.Type;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public abstract class PermissibleCommandSender implements ICommandSender, IPermissible {
	private Set<IPermission> permissions = Sets.newHashSet();
	private MinecraftServer server;
	private Entity owner;
	public PermissibleCommandSender(MinecraftServer s,Entity owner) {
		this.server = s;
		this.owner = owner;
	}

	@Override
	public boolean hasPermission(PlayerList l, IPermission p) {
		if(isOperator(l)&&p.operatorHasPermission(getOperatorLevel(l), l))
			return true;
		return permissions.contains(p);

	}

	@Override
	public void givePermission(IPermission p) {
		permissions.add(p);

	}

	@Override
	public void removePermission(IPermission p) {
		permissions.remove(p);

	}

	@Override
	public boolean isOperator(PlayerList l) {
		// TODO Auto-generated method stub
		return getOperatorLevel(l)>1;
	}

	@Override
	public abstract int getOperatorLevel(PlayerList l);

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return owner==null?"@":owner.getName();
	}

	@Override
	public ITextComponent getDisplayName() {
		// TODO Auto-generated method stub
		return new TextComponentString(getName());
	}

	@Override
	public abstract void sendMessage(ITextComponent component);

	@Override
	public boolean canUseCommand(int permLevel, String commandName) {
		// TODO Auto-generated method stub
		return getOperatorLevel(getServer().getPlayerList())>=permLevel;
	}

	@Override
	public BlockPos getPosition() {
		// TODO Auto-generated method stub
		return owner==null?BlockPos.ORIGIN:owner.getPosition();
	}

	@Override
	public Vec3d getPositionVector() {
		// TODO Auto-generated method stub
		return owner==null?Vec3d.ZERO:owner.getPositionVector();
	}

	@Override
	public World getEntityWorld() {
		// TODO Auto-generated method stub
		return owner==null?server.worldServerForDimension(0):owner.getEntityWorld();
	}

	@Override
	public Entity getCommandSenderEntity() {
		// TODO Auto-generated method stub
		return owner;
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


}
