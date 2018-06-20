package gmail.chorman64.gac14.basic.util;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import gmail.chorman64.gac14.basic.players.IProfileInfoBase;
import gmail.chorman64.gac14.basic.players.PlayerProfile;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagByte;

public class ProfileInfoBoolean implements IProfileInfoBase<Boolean> {

	private PlayerProfile owner;
	private boolean value;
	private boolean persistant;

	public ProfileInfoBoolean(PlayerProfile owner) {
		this(owner,true);
	}
	public ProfileInfoBoolean(PlayerProfile owner,boolean persistant) {
		this.owner = owner;
		this.value = false;
		this.persistant = persistant;
	}

	@Override
	public PlayerProfile owner() {
		// TODO Auto-generated method stub
		return owner;
	}

	@Override
	public Boolean getValue() {
		// TODO Auto-generated method stub
		return value;
	}

	@Override
	public void setValue(Boolean nvalue) {
		value = nvalue;

	}

	@Override
	public void markDirty() {
		// TODO Auto-generated method stub

	}

	@Override
	public NBTBase serializeNBT() {
		NBTTagByte val = new NBTTagByte((byte) (value?1:0));
		return val;
	}

	@Override
	public void deserializeNBT(NBTBase tag) {
		if(!(tag instanceof NBTPrimitive))
			throw new IllegalArgumentException("Bad tag");
		this.value = ((NBTPrimitive)tag).getByte()!=0;
	}

	@Override
	public boolean ofString(String s) {
		this.value = Boolean.parseBoolean(s);
		return true;
	}

	@Override
	public Set<String> getPossibleValues() {
		// TODO Auto-generated method stub
		return ImmutableSet.of("true", "false");
	}

	@Override
	public String query(String s) {
		String[] query = s.split("\\s+");
		if(query.length==0)
			return "Given Query is Empty";
		else if(query[0].equals("get"))
			return Boolean.toString(value);
		else if(query[0].equals("set"))
			if(query.length==1)
				return "Mutation command must have a value";
			else {
				this.value = Boolean.parseBoolean(s);
				return "Updated to "+value;
			}
		return String.format("Failed to understand %s.", s);
	}

	@Override
	public Class<Boolean> getType() {
		// TODO Auto-generated method stub
		return Boolean.class;
	}
	
	@Override
	public boolean isPersistant() {
		// TODO Auto-generated method stub
		return persistant;
	}
	

}
