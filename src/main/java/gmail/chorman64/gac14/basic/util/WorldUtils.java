package gmail.chorman64.gac14.basic.util;

import gmail.chorman64.gac14.basic.Core;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class WorldUtils {
	
	public WorldUtils() {
		// TODO Auto-generated constructor stub
	}
	
	public static int getDimension(World w) {
		MinecraftServer s = Core.instance.server;
		if(s.worldServerForDimension(0)==w)
			return 0;
		else if(s.worldServerForDimension(-1)==w)
			return -1;
		else if(s.worldServerForDimension(1)==w)
			return 1;
		return 0;
	}

}
