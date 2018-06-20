package gmail.chorman64.gac14.basic.players;

import java.util.Set;

import gmail.chorman64.gac14.basic.IOwnable;
import net.minecraft.nbt.NBTBase;

public interface IProfileInfoBase<T> extends IOwnable<PlayerProfile> {
	/***
	 * Gets the value of this Info Tag from its owner.
	 * @return
	 */
	T getValue();
	
	Class<? extends T> getType();
	/**
	 * Updates the value of the given Info Tag to its owner.
	 * @param nvalue
	 */
	void setValue(T nvalue);
	/**
	 * Called when the value of this parameter is updated
	 */
	void markDirty();
	/**
	 * Serializes the value to an NBTTagBased on the class given.
	 * @return
	 */
	NBTBase serializeNBT();
	/**
	 * Loads the value of the NBTTag into this profile tag's value.
	 * @param tag
	 */
	void deserializeNBT(NBTBase tag);
	/**
	 * Loads the value in the string into this profile tag's value.
	 * @param s
	 * @return true if the operation suceeded, false otherwise.
	 */
	boolean ofString(String s);
	/**
	 * Returns the Value as a string.
	 * @return
	 */
	String toString();
	
	Set<String> getPossibleValues();
	
	default boolean isManagement(String query) {
		return false;
	}
	String query(String s);
	
	default boolean isPersistant() {
		return true;
	}
}
