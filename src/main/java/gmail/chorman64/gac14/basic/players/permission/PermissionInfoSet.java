package gmail.chorman64.gac14.basic.players.permission;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import gmail.chorman64.gac14.basic.Core;
import gmail.chorman64.gac14.basic.permission.IPermission;
import gmail.chorman64.gac14.basic.permission.PermissionManager;
import gmail.chorman64.gac14.basic.players.IProfileInfoBase;
import gmail.chorman64.gac14.basic.players.IProfileInfoSet;
import gmail.chorman64.gac14.basic.players.PlayerProfile;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public class PermissionInfoSet implements IProfileInfoBase<Map<IPermission, Boolean>> {

	private PlayerProfile owner;
	private static final Comparator<IPermission> compareNames = (a,b)->PermissionManager.getName(a).compareToIgnoreCase(PermissionManager.getName(b));
	private Map<IPermission,Boolean> permissions = Maps.newTreeMap(compareNames);

	public PermissionInfoSet(PlayerProfile owner) {
		this.owner = owner;
	}

	@Override
	public PlayerProfile owner() {
		// TODO Auto-generated method stub
		return owner;
	}



	@Override
	public void markDirty() {
		// TODO Auto-generated method stub

	}

	@Override
	public NBTBase serializeNBT() {
		NBTTagList l = new NBTTagList();
		for(IPermission p:permissions.keySet())
		{
			NBTTagCompound perm = new NBTTagCompound();
			perm.setString("Node", PermissionManager.getName(p));
			perm.setBoolean("IsSet", permissions.get(p));
			l.appendTag(perm);
		}

		return l;
	}

	@Override
	public void deserializeNBT(NBTBase tag) {
		if(!(tag instanceof NBTTagList))
			throw new RuntimeException("Bad Permissions Node");
		NBTTagList list = (NBTTagList)tag;
		for(int i=0;i<list.tagCount();i++) {
			NBTTagCompound comp = list.getCompoundTagAt(i);
			IPermission node = PermissionManager.getNode(comp.getString("Node"));
			permissions.put(node,comp.getBoolean("IsSet"));
		}
	}

	@Override
	public boolean ofString(String s) {
		return true;
	}

	@Override
	public Set<String> getPossibleValues() {
		// TODO Auto-generated method stub
		return Collections.emptySet();
	}

	@Override
	public Map<IPermission, Boolean> getValue() {
		// TODO Auto-generated method stub
		return permissions;
	}

	@Override
	public void setValue(Map<IPermission, Boolean> nvalue) {
		permissions.clear();
		permissions.putAll(nvalue);
	}


	public void add(IPermission info) {
		permissions.put(info, true);
	}


	public void remove(IPermission info) {
		permissions.put(info, false);
	}


	public boolean contains(IPermission info) {
		// TODO Auto-generated method stub
		return permissions.containsKey(info)&&permissions.get(info);
	}

	public boolean isBlocked(IPermission inf) {
		return permissions.containsKey(inf)&&!permissions.get(inf);
	}

	@Override
	public String query(String s) {
		return null;
	}

	@Override
	public Class<? extends Map<IPermission, Boolean>> getType() {
		// TODO Auto-generated method stub
		return (Class<? extends Map<IPermission, Boolean>>) permissions.getClass();
	}

	public void addAll(Set<IPermission> permissions) {
		this.permissions.putAll(permissions.stream().collect(Collectors.toMap(p->p,p->true)));

	}
	public void removeAll(Set<IPermission> permissions) {
		this.permissions.putAll(permissions.stream().collect(Collectors.toMap(p->p,p->false)));
	}

	public void clear() {
			this.permissions.clear();
	}

	public Set<IPermission> merge(Set<IPermission> permissions){
		return permissions.stream().filter(permission->this.contains(permission)&&!this.isBlocked(permission))
				.collect(()->new TreeSet<>(compareNames), Set::add, Set::addAll);
	}

	public Set<IPermission> exclusion(Set<IPermission> permissions){
		return permissions.stream().filter(permission->!this.contains(permission)||this.isBlocked(permission))
				.collect(()->new TreeSet<>(compareNames), Set::add, Set::addAll);
	}


}
