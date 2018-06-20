package gmail.chorman64.gac14.basic.util;

import java.lang.ref.WeakReference;
import java.util.Comparator;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.core.helpers.UUIDUtil;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;

import gmail.chorman64.gac14.basic.Core;
import gmail.chorman64.gac14.basic.IOwnable;
import gmail.chorman64.gac14.basic.RegexConstants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

import static java.lang.Math.PI;

public class NPCHelper implements RegexConstants {
	private static final MinecraftServer server = Core.instance.server;
	private static final Map<UUID,NPC> npcs = Maps.newTreeMap();
	private static final double MAX_LOOK_DISTANCE = 25.45;
	public static class NPC implements IOwnable<FakePlayer>,ITickable{
		private WeakReference<FakePlayer> owner;
		private UUID id;
		private boolean look;
		@Override
		public FakePlayer owner() {
			// TODO Auto-generated method stub
			return owner.get();
		}
		@Override
		public void update() {
			if(owner.get()==null) {
				npcs.remove(id);
				return;
			}
		look:
			if(look) {
				final FakePlayer fake = owner();
				World w = owner().getEntityWorld();
				EntityPlayerMP target = w.getPlayers(EntityPlayerMP.class, new Predicate<EntityPlayerMP>() {

					@Override
					public boolean apply(EntityPlayerMP input) {
						// TODO Auto-generated method stub
						return input!=owner()&&input.getPositionVector().distanceTo(owner().getPositionVector())<=MAX_LOOK_DISTANCE;
					}
					
				}).stream().min(new Comparator<EntityPlayer>() {

					@Override
					public int compare(EntityPlayer arg0, EntityPlayer arg1) {
						// TODO Auto-generated method stub
						return Double.compare(arg0.getDistanceSqToEntity(owner()),arg1.getDistanceSqToEntity(owner()));
					}
					
				}).orElse(null);
				if(target==null)
					break look;
				Vec3d pos = fake.getPositionVector();
				Vec3d other = target.getPositionVector();
				Vec3d distance = other.subtract(pos);
				double x = distance.xCoord;
				double z = distance.zCoord;
				double add = 0;
				if(x<0) {
					x = -x;
					add +=0.5*PI;
				}
				
				
			}
			
		}
		protected NPC(UUID id,FakePlayer owner) {
			this.owner = new WeakReference<>(owner);
		}
		
	};
	private NPCHelper() {
		// TODO Auto-generated constructor stub
	}
	public static final NPC createNPC(String name) {
		return createNPC(name,server.worldServerForDimension(0));
	}
	public static final NPC createNPC(String name,WorldServer world) {
		GameProfile prof;
		PlayerProfileCache pCache = server.getPlayerProfileCache();
		if(uuid.matches(name)) {
			UUID id = UUID.fromString(name);
			prof = pCache.getProfileByUUID(id);
		}else {
			prof = pCache.getGameProfileForUsername(name);
		}
		UUID npcId = UUIDUtil.getTimeBasedUUID();
		FakePlayer player = FakePlayerFactory.get(world, prof);
		player.setPosition(0, 60, 0);
		NPC npc = new NPC(npcId,player);
		npcs.put(npcId, npc);
		return npc;
	}
	

}
