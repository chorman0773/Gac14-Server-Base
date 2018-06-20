package gmail.chorman64.gac14.basic.util.chrono;

import java.time.Duration;
import java.time.Instant;

import net.minecraft.nbt.NBTTagCompound;

public class ChronoUtils {

	private ChronoUtils() {
		// TODO Auto-generated constructor stub
	}

	public static NBTTagCompound serializeInstant(Instant i) {
		NBTTagCompound ret = new NBTTagCompound();
		ret.setLong("Seconds", i.getEpochSecond());
		ret.setInteger("Nanos", i.getNano());
		return ret;
	}

	public static Instant deserializeInstant(NBTTagCompound c) {
		return Instant.ofEpochSecond(c.getLong("Seconds"), c.getInteger("Nanos"));
	}


	public static NBTTagCompound serializeDuration(Duration d) {
		return serializeInstant(Instant.EPOCH.plus(d));
	}

	public static Duration deserializeDuration(NBTTagCompound c) {
		return Duration.between(Instant.EPOCH, deserializeInstant(c));
	}

}
