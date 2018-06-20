package gmail.chorman64.gac14.basic.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class MCMathUtil {

	private MCMathUtil() {
		// TODO Auto-generated constructor stub
	}
	public static BlockPos ofVec(Vec3d vec) {
		return new BlockPos((int)vec.xCoord,(int)vec.yCoord,(int)vec.zCoord);
	}

}
