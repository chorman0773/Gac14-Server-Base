package gmail.chorman64.gac14.basic.util;

public class BooleanUtil {

	private BooleanUtil() {
		// TODO Auto-generated constructor stub
	}
	public static byte encode(boolean...vals) {
		byte val = 0;
		if(vals.length<8)
			for(int i=0;i<vals.length;i++)
				if(vals[i])
					val |= 1<<i;
				else
					val |=0;
		else
			for(int i=0;i<8;i++)
				if(vals[i])
					val|=1<<i;
		return val;
					
	}
	public static boolean[] decode(byte bitfield) {
		boolean[] ret = new boolean[8];
		for(int i =0;i<256;i++) {
			if((bitfield&(1<<i))!=0)
				ret[i] = true;
			else
				ret[i] = false;
		}
		return ret;
	}

}
