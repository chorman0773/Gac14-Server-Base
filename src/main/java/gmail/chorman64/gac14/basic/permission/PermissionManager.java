package gmail.chorman64.gac14.basic.permission;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;

import gmail.chorman64.gac14.basic.Core;
import gmail.chorman64.gac14.basic.RegexConstants;
import gmail.chorman64.gac14.basic.players.PlayerProfile;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.management.PlayerList;

public class PermissionManager implements RegexConstants {
	private PlayerList list;
	private static Map<String,IPermission> permissions = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	private static Set<IPermission> permissionList = new TreeSet<>((a,b)->a.getName().compareToIgnoreCase(b.getName()));



	public PermissionManager(PlayerList l) {
		this.list = l;
	}

	public static void registerPermission(IPermission p) {
		String name = p.getName();
		if(!name.matches(identifierQualified))
			throw new IllegalArgumentException("Bad Permission, must be a java legal qualified identifier");
		if(permissions.containsKey(name))
			throw new IllegalArgumentException("This node is already registered");
		else if(permissions.containsValue(p))
			throw new IllegalArgumentException("Permission Already exists on this map");
		permissions.put(name, p);
		permissionList.add(p);
	}
	public static String getName(IPermission p) {
		if(p.getParent()==null)
			return "";
		else
			return getName(p.getParent())+"."+p.getName();
	}
	public static IPermission getNode(String name) {
		if(name.contains(".")) {
			String[] tree = name.split("\\.");
			String[] subtree = Arrays.copyOfRange(tree, 1, tree.length);
			IPermission perm = permissions.get(tree[0]);
			for(String node:subtree)
				perm = perm.getTreeElement(node);
			return perm;
		}else
			return	permissions.get(name);

	}
	public boolean hasPermission(IPermissible p,String name) {
		if(p.isRoot())
			return true;
		IPermission node = getNode(name);
		return hasPermission(p, node);
	}
	public boolean hasPermission(IPermissible p,IPermission node) {
		if(p.isRoot())
			return true;
		else if(p.hasPermission(list, node))
			return true;
		else if(node.isRoot())
			return false;
		else if(hasPermission(p,node.getParent()))
			return true;
		else if(node.operatorHasPermission(p.getOperatorLevel(list), list))
			return true;
		else
			return false;
	}


	public IPermissible getPermissible(ICommandSender sender) {
		if(sender instanceof IPermissible)
			return (IPermissible)sender;
		else if(sender instanceof MinecraftServer&&isRoot(sender))
			return RootCommandSender.sender;
		else if(sender instanceof EntityPlayerMP)
			return PlayerProfile.get((EntityPlayerMP)sender);
		return null;//Nothing else is possible here.
	}

	public 	ICommandSender getCommandSender(IPermissible p) {
		if(p instanceof ICommandSender)
			return (ICommandSender)p;
		else if(p==RootCommandSender.sender)
			return Core.instance.server;
		else if(p instanceof PlayerProfile)
			return ((PlayerProfile)p).owner();
		return null;
	}

	public boolean isRoot(ICommandSender sender) {
		if((sender instanceof MinecraftServer)&&sender.getServer()==sender)
			return true;
		else if(sender instanceof IPermissible)
			return ((IPermissible)sender).isRoot();
		return false;
	}

	public boolean hasPermission(ICommandSender sender, IPermission node) {
		if(isRoot(sender))
			return true;//The root implicitly has All permission and can never have a permission revoked.
		if(sender instanceof IPermissible)
			return hasPermission(((IPermissible)sender), node);
		else if(sender instanceof EntityPlayerMP)
			return hasPermission(PlayerProfile.get((EntityPlayerMP)sender), node);
		else if(sender instanceof DedicatedServer&&sender==Core.instance.server)
			return true;
		return false;

	}

	public static Set<IPermission> getAllPermissions() {
		return Collections.unmodifiableSet(permissionList);
	}



}
