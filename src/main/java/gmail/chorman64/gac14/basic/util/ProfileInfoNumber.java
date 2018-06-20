package gmail.chorman64.gac14.basic.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Set;

import gmail.chorman64.gac14.basic.players.IProfileInfoBase;
import gmail.chorman64.gac14.basic.players.PlayerProfile;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;

public class ProfileInfoNumber implements IProfileInfoBase<Number> {
	private Number val;
	private Class<? extends Number> base;
	private PlayerProfile owner;
	private boolean persistant;
	public ProfileInfoNumber(PlayerProfile owner) {
		this(owner,Integer.class,true);
	}
	public ProfileInfoNumber(PlayerProfile owner,Class<? extends Number> cl) {
		this(owner,cl,true);
	}
	public ProfileInfoNumber(PlayerProfile owner,boolean persistant) {
		this(owner,Integer.class,persistant);
	}
	public ProfileInfoNumber(PlayerProfile owner,Class<? extends Number> cl,boolean persistant) {
		this.owner = owner;
		this.base = cl;
		this.persistant = persistant;
		if(cl==Integer.class) {
			val = 0;
		}else if(cl==Long.class) {
			val = 0L;
		}else if(cl==Short.class) {
			val = (short)0;
		}else if(cl==Byte.class) {
			val = (byte)0;
		}else if(cl==Float.class) {
			val = 0.0f;
		}else if(cl==Double.class) {
			val = 0.0;
		}else if(cl==BigInteger.class) {
			val = BigInteger.ZERO;
		}else if(cl==BigDecimal.class) {
			val = BigDecimal.ZERO;
		}else
			throw new IllegalArgumentException(String.format("Error in creating this number info, the given number class %s, in unsupported.%n"
					+ "Note that this implementation only supports the wrapper types for the primitive neumerical types"
					+ "And the 2 \"Large\" Numerical Utility classes, java.math.BigInteger, and java.math.BigDecimal", cl.getSimpleName()));
	}
 
	@Override
	public PlayerProfile owner() {
		// TODO Auto-generated method stub
		return owner;
	}

	@Override
	public Number getValue() {
		// TODO Auto-generated method stub
		return val;
	}

	@Override
	public Class<? extends Number> getType() {
		// TODO Auto-generated method stub
		return base;
	}

	@Override
	public void setValue(Number nvalue) {
		if(nvalue.getClass()==base)
			val = nvalue;
		else if(base == Integer.class) 
			val = nvalue.intValue();
		else if(base == Long.class)
			val = nvalue.longValue();
		else if(base == Short.class)
			val = nvalue.shortValue();
		else if(base == Byte.class)
			val = nvalue.byteValue();
		else if(base == Float.class)
			val = nvalue.floatValue();
		else if(base == Double.class)
			val = nvalue.doubleValue();
		else if(base == BigInteger.class)
			if(nvalue.getClass()==BigDecimal.class) {
				val = ((BigDecimal)nvalue).toBigInteger();
			}else
				val = BigInteger.valueOf(nvalue.longValue());
		else if(base == BigDecimal.class)
			val = BigDecimal.valueOf(nvalue.doubleValue());
		
	}

	@Override
	public void markDirty() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public NBTBase serializeNBT() {
		if(base==BigInteger.class) 
		{
			byte[] data = ((BigInteger)val).toByteArray();
			NBTTagByteArray array = new NBTTagByteArray(data);
			return array;
		}else if(base==BigDecimal.class) {
			String form = val.toString();
			NBTTagString ret = new NBTTagString(form);
			return ret;
		}else if(base==Integer.class) {
			return new NBTTagInt(val.intValue());
		}else if(base==Long.class)
			return new NBTTagLong(val.longValue());
		else if(base==Short.class)
			return new NBTTagShort(val.shortValue());
		else if(base==Byte.class)
			return new NBTTagByte(val.byteValue());
		else if(base==Float.class)
			return new NBTTagFloat(val.floatValue());
		else 
			return new NBTTagDouble(val.doubleValue());
	}

	@Override
	public void deserializeNBT(NBTBase tag) {
		if(base==BigInteger.class) {
			if(!(tag instanceof NBTTagByteArray))
				throw new IllegalArgumentException("A BigInteger tag requires byte array data");
			NBTTagByteArray bytes = (NBTTagByteArray)tag;
			val = new BigInteger(bytes.getByteArray());
		}else if(base==BigDecimal.class) {
			if(!(tag instanceof NBTTagString))
				throw new IllegalArgumentException("A BigDecimal tag requires string data");
			NBTTagString data = (NBTTagString)tag;
			val = new BigDecimal(data.getString());
		}else {
			if(!(tag instanceof NBTPrimitive))
				throw new IllegalArgumentException("This tag requires a primitive element");
			NBTPrimitive in = (NBTPrimitive)tag;
			if(base==Double.class)
				val = in.getDouble();
			else if(base==Float.class)
				val = in.getFloat();
			else
				setValue(in.getLong());
		}
		
	}

	@Override
	public boolean ofString(String s) {
		try {
		if(base==BigInteger.class) {
			val = new BigInteger(s);
		}else if(base==BigDecimal.class)
			val = new BigDecimal(s);
		else if(base==Integer.class)
			val = Integer.parseInt(s);
		else if(base==Long.class)
			val = Long.parseLong(s);
		else if(base==Byte.class)
			val = Byte.parseByte(s);
		else if(base==Short.class)
			val = Short.parseShort(s);
		else if(base==Float.class)
			val = Float.parseFloat(s);
		else 
			val = Double.parseDouble(s);
		return true;
		}catch(NumberFormatException e) {
		return false;
		}
	}

	@Override
	public Set<String> getPossibleValues() {
		// TODO Auto-generated method stub
		return Collections.emptySet();
	}

	@Override
	public String query(String s) {
		if(s.equals("get"))
			return val.toString();
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

}
