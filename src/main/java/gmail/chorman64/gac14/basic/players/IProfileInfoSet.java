package gmail.chorman64.gac14.basic.players;

import java.util.Set;

import gmail.chorman64.gac14.basic.IOwnable;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;


public interface IProfileInfoSet<T> extends IProfileInfoBase<Set<T>> {
	void add(T info);
	void remove(T info);
	boolean contains(T info);
}
