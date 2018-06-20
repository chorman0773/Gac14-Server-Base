package gmail.chorman64.gac14.basic.util;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.Vec3d;

public class PhysicsUtil {
	private static final double VELOCITY_SCALE = 0.85;
	private PhysicsUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public static final void compoundMotion(EntityPlayerMP player,Vec3d velInc) {
		Vec3d currMotion = new Vec3d(player.motionX,player.motionY,player.motionZ);
		Vec3d absInc = velInc.scale(VELOCITY_SCALE);
		Vec3d newMotion = currMotion.add(absInc);
		
	}

}
