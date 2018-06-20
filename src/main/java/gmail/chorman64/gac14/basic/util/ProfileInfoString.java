package gmail.chorman64.gac14.basic.util;

import java.util.Collections;
import java.util.Set;

import gmail.chorman64.gac14.basic.players.IProfileInfoBase;
import gmail.chorman64.gac14.basic.players.PlayerProfile;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;

public class ProfileInfoString implements IProfileInfoBase<String> {

	private PlayerProfile owner;
	private String value;
	private boolean persistant;

	public ProfileInfoString(PlayerProfile owner) {
		this(owner,true);
	}
	public ProfileInfoString(PlayerProfile owner,boolean persistant) {
		this.owner = owner;
		this.value = "";
		this.persistant = persistant;
	}
	

	@Override
	public PlayerProfile owner() {
		// TODO Auto-generated method stub
		return owner;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return value;
	}

	@Override
	public void setValue(String nvalue) {
		value = nvalue.intern();

	}

	@Override
	public void markDirty() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see gmail.chorman64.gac14.basic.players.IProfileInfoBase#isPersistant()
	 */
	@Override
	public boolean isPersistant() {
		// TODO Auto-generated method stub
		return persistant;
	}
	@Override
	public NBTBase serializeNBT() {
		// TODO Auto-generated method stub
		return new NBTTagString(value);
	}

	@Override
	public void deserializeNBT(NBTBase tag) {
		if(!(tag instanceof NBTTagString))
			throw new IllegalArgumentException("Invalid tag");
		this.value = ((NBTTagString)tag).getString();

	}

	@Override
	public boolean ofString(String s) {
		setValue(s);
		return true;
	}

	@Override
	public Set<String> getPossibleValues() {
		// TODO Auto-generated method stub
		return Collections.emptySet();
	}

	@Override
	public String query(String s) {
		if(s.equals("get"))
			return value;
		else if(s.startsWith("set")) {
			String v = s.substring(s.indexOf(" "));
			v = v.trim();
			setValue(v);
			return String.format("Updated to %s", v);
		}
		return "Query Failed";
	}

	@Override
	public Class<String> getType() {
		// TODO Auto-generated method stub
		return String.class;
	}

}
