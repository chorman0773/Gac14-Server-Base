package gmail.chorman64.gac14.basic.players.group;

import java.util.Set;

import gmail.chorman64.gac14.basic.permission.IPermission;
import gmail.chorman64.gac14.basic.players.IProfileInfoBase;
import gmail.chorman64.gac14.basic.players.PlayerProfile;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;

public class ProfileInfoGroup implements IProfileInfoBase<Group> {
	static{Group.clinit();}
	private PlayerProfile owner;
	private Group group = Group.def;

	public ProfileInfoGroup(PlayerProfile owner) {
		this.owner = owner;
	}

	@Override
	public PlayerProfile owner() {
		// TODO Auto-generated method stub
		return owner;
	}

	@Override
	public Group getValue() {
		// TODO Auto-generated method stub
		return group;
	}

	@Override
	public void setValue(Group nvalue) {
		for(IPermission node:group.permissionsToSet) 
			owner.removePermission(node);
		
		this.group = nvalue==null?Group.def:nvalue;
		for(IPermission node:group.permissionsToSet)
			owner.givePermission(node);
		
	}

	@Override
	public void markDirty() {
		
		
	}

	@Override
	public NBTBase serializeNBT() {
		// TODO Auto-generated method stub
		return new NBTTagString(group.name);
	}

	@Override
	public void deserializeNBT(NBTBase tag) {
		if(!(tag instanceof NBTTagString))
			throw new RuntimeException("Bad tag type");
		group = Group.groups.getOrDefault(((NBTTagString)tag).getString(), Group.def);
		
	}

	@Override
	public boolean ofString(String s) {
		// TODO Auto-generated method stub
		group = Group.groups.getOrDefault(s, Group.def);
		return true;
	}

	@Override
	public Set<String> getPossibleValues() {
		// TODO Auto-generated method stub
		return Group.groups.keySet();
	}

	@Override
	public String query(String s) {
		if(s.equals("get"))
			return toString();
		else if(s.startsWith("set"))
		{
			String v = s.substring(s.indexOf(" ")).trim();
			if(!ofString(v))
				return String.format("%s is not valid for this type", v);
			else
				return String.format("Updated to %s", v);
		}
		return String.format("Failed to understand %s", s);
	}
	public String toString() {
		return group.name;
	}

	@Override
	public Class<? extends Group> getType() {
		// TODO Auto-generated method stub
		return Group.class;
	}

}
