package gmail.chorman64.gac14.basic.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;

import net.minecraft.util.DamageSource;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class EntityUtil {

	private EntityUtil() {
		// TODO Auto-generated constructor stub
	}

	public static void killEntity(Entity e,boolean dropItems) {
		if(e instanceof EntityItem) {
			//Killing an item works slightly differntly.
			//Need to make sure a player does not pick it up while it is dying.
			EntityItem i = (EntityItem)e;
			i.setInfinitePickupDelay();
			World w = i.getEntityWorld();
			if(w!=null)
				w.removeEntity(i);
			return;
		}else if(e instanceof EntityPlayerMP) {
			//Players also handle differently, as we can't just kill the player entity.e
			//We also have to handle thinventory if dropItems is disabled and keepInventory is false
			World w = e.getEntityWorld();
			GameRules r = w.getGameRules();
			boolean keepInventory = Boolean.parseBoolean(r.getString("keepInventory"));
			if(!(keepInventory||dropItems)) {
				InventoryPlayer p = ((EntityPlayerMP) e).inventory;
				p.clear();

			}
			EntityPlayerMP player = (EntityPlayerMP) e;
			player.attackEntityFrom(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
			return;
		}else if(dropItems)
			e.setDropItemsWhenDead(false);
		e.setDead();
	}
	public static boolean canClearEntity(Entity e) {
		if(e instanceof EntityPlayer)
			return false;
		else if(e instanceof EntityItem)
			return true;
		else if(e instanceof EntityDragon)
			return false;
		else if(e instanceof EntityWither)
			return false;

		return true;//TODO
	}

}
