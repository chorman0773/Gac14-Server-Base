package gmail.chorman64.gac14.basic.players;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;

import gmail.chorman64.gac14.basic.Core;
import gmail.chorman64.gac14.basic.IOwnable;
import gmail.chorman64.gac14.basic.RegexConstants;
import gmail.chorman64.gac14.basic.permission.IPermissible;
import gmail.chorman64.gac14.basic.permission.IPermission;
import gmail.chorman64.gac14.basic.permission.PermissionManager;
import gmail.chorman64.gac14.basic.players.event.PlayerCombatEvent;
import gmail.chorman64.gac14.basic.players.event.PlayerDamageEvent;
import gmail.chorman64.gac14.basic.players.event.PlayerProfileCreatedEvent;
import gmail.chorman64.gac14.basic.players.event.PlayerProfileNewEvent;
import gmail.chorman64.gac14.basic.players.group.ProfileInfoGroup;
import gmail.chorman64.gac14.basic.players.permission.ManagementPermission;
import gmail.chorman64.gac14.basic.players.permission.PermissionInfoSet;
import joptsimple.internal.Strings;
import net.minecraft.entity.ai.attributes.AttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.management.PlayerList;
import net.minecraft.server.management.UserListOpsEntry;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@EventBusSubscriber
public final class PlayerProfile implements IOwnable<EntityPlayerMP>, IPermissible, INBTSerializable<NBTTagCompound>, RegexConstants, ITickable , Comparable<PlayerProfile> {
	private final EntityPlayerMP owner;
	private final Map<String,IProfileInfoBase<?>> infoTags = Maps.newHashMap();
	private final PermissionInfoSet permissions;
	private static File profDir;
	private static final Comparator<EntityPlayerMP> comparator = new TreeComparator();
	public static final SortedMap<EntityPlayerMP,PlayerProfile> profs = Maps.newTreeMap(comparator);
	private Vec3d acceleration;
	private Vec3d velocity;
	private final AttributeMap attrs = new AttributeMap();
	private static class TreeComparator implements Comparator<EntityPlayerMP>{
		public int compare(EntityPlayerMP a,EntityPlayerMP b) {
			return a.getUniqueID().compareTo(b.getUniqueID());
		}
	}
	private Set<ITickable> updateSubscribers = Sets.newHashSet();
	protected class DelayedCall implements ITickable{
		protected DelayedCall(Runnable event, int ticks) {
			this.event = event;
			this.ticks = ticks;
		}
		private Runnable event;
		private int ticks;
		@Override
		public void update() {
			ticks--;
			if(ticks<=0) {
				event.run();
				updateSubscribers.remove(this);
			}

		}


	}

	public void addDelayedEvent(Runnable event, int ticks) {
		DelayedCall ret = new DelayedCall(event,ticks);
		subscribeUpdates(ret);
	}
	public void subscribeUpdates(ITickable t) {
		updateSubscribers.add(t);
	}
	public void unsubscribe(ITickable t) {
		updateSubscribers.remove(t);
	}

	public static void setProfileDirectory(File f) throws IOException {
		if(!f.exists())
			f.mkdirs();
		if(!f.isDirectory())
			return;
		profDir = f;
	}
	@SubscribeEvent
	public static void  playerLoggedIn(PlayerEvent.PlayerLoggedInEvent ev) throws FileNotFoundException, IOException {
		EntityPlayerMP owner = (EntityPlayerMP) ev.player;

			PlayerProfile prof = new PlayerProfile(owner);
			File f = new File(profDir,owner.getCachedUniqueIdString()+".dat");
			if(!f.exists()) {
				MinecraftForge.EVENT_BUS.post(new PlayerProfileNewEvent(prof));
				f.createNewFile();
				CompressedStreamTools.writeCompressed(prof.serializeNBT(), new FileOutputStream(f));
			}else {
				MinecraftForge.EVENT_BUS.post(new PlayerProfileCreatedEvent(prof));
				NBTTagCompound comp = CompressedStreamTools.readCompressed(new FileInputStream(f));
				prof.deserializeNBT(comp);
			}

	}
	@SubscribeEvent
	public static void playerLoggedOut(PlayerEvent.PlayerLoggedOutEvent e) throws IOException {
		logOut((EntityPlayerMP)e.player);
	}
	private PlayerProfile(EntityPlayerMP owner) {
		this.owner = owner;
		infoTags.put("Permissions",permissions = new PermissionInfoSet(this));
		infoTags.put("Group", new ProfileInfoGroup(this));
		profs.put(owner, this);
	}
	public void registerInfoBase(String s,IProfileInfoBase<?> inf) {

		infoTags.putIfAbsent(s, inf);
	}

	public static final PlayerProfile get(EntityPlayerMP owner) {
		if(profs.containsKey(owner))
			return profs.get(owner);
		return null;
	}

	public static final void logOut(EntityPlayerMP owner)throws IOException{
		PlayerProfile prof = profs.get(owner);
		profs.remove(owner);
		File f = new File(profDir,owner.getCachedUniqueIdString()+".dat");
		CompressedStreamTools.writeCompressed(prof.serializeNBT(), new FileOutputStream(f));
	}

	@Override
	public boolean hasPermission(PlayerList l,IPermission p) {
		if(permissions.isBlocked(p))
			return false;
		if(isOperator(l)&&p.operatorHasPermission(getOperatorLevel(l), l))
			return true;


		return permissions.contains(p);

	}
	public AttributeMap getCustomAttributes() {
		return attrs;
	}
	/**
	 * Checks if a query is only executeable by the Server or a player that can make Management Queries.
	 * @param query the query to check
	 * @return true if its a permission mutation query and the node refers to a management permission, or the query is a compound query, and the node responds that the subquery is a management Query.
	 */
	public boolean isManagement(String query) {
		if(query.startsWith("+")||query.startsWith("-")) {
			query = query.substring(1);
			IPermission p = Core.instance.permissionManager.getNode(query);
			if(p==null)
				return false;

			return p.isManagement();
		}else if(query.startsWith("?"))
			return false;
		else if(query.indexOf(".")<query.indexOf(" ")){
			String node = query.substring(0,query.indexOf("."));
			String nQuery = query.substring(query.indexOf(".")+1).trim();
			IProfileInfoBase<?> info = getTag(node);
			if(info==null)
				return false;
			return info.isManagement(nQuery);
		}
		else{
			String node = query.substring(0, query.indexOf(" "));
			String nQuery = query.substring(query.indexOf(" ")).trim();
			IProfileInfoBase<?> info = getTag(node);
			if(info==null)
				return false;
			return info.isManagement(nQuery);
		}
	}

	public IProfileInfoBase<?> getTag(String key){
		if(key.matches(identifierQualified)) {
			String[] info = key.split("\\.");
			IProfileInfoBase<?> tag = infoTags.get(info[0]);
			String[] newinfo = new String[info.length-1];
			System.arraycopy(info, 1, newinfo, 0, newinfo.length);
			info = newinfo;
			if(tag instanceof IProfileInfoMap<?>)
				return ((IProfileInfoMap<?>)tag).getInfo(Strings.join(info, "."));
			else
				return tag;
		}
		return infoTags.get(key);
	}
	public boolean setTag(String key,String value) {
		if(getTag(key)==null)
			return false;
		return infoTags.get(key).ofString(value);
	}

	public String query(String query) {
		if(query.equals("?-*")) {
			return permissions.exclusion(PermissionManager.getAllPermissions())
					.stream().map(PermissionManager::getName).collect(Collectors.joining("\n", String.format("%s does not have:\n", owner.getName()), ""));
		}
		else if(query.startsWith("+")||query.endsWith("-")||query.startsWith("?")) {
			String node = query.substring(1);
			if(node.equals("*")) {
				if(query.startsWith("+"))
				{
					permissions.addAll(PermissionManager.getAllPermissions());
					return String.format("Added all Permissions to %s", owner.getName());
				}else if(query.startsWith("-")) {
					permissions.clear();
				}else
					return permissions.merge(PermissionManager.getAllPermissions())
							.stream().map(PermissionManager::getName).collect(Collectors.joining("\n", String.format("%s has:\n", owner.getName()), ""));
			}else {

			IPermission perm = PermissionManager.getNode(node);
			if(perm==null)
				return String.format("Failed to resolve %s. Permission %s does not exist", query, node);
			if(query.startsWith("+")) {
				givePermission(perm);
				return String.format("Successfuly added permission %s.", node);
			}

			else if(query.startsWith("?"))
				return String.format("This player %s permission %s", hasPermission(owner.getServer().getPlayerList(),perm)?"has":"does not have", node);
			else {
				removePermission(perm);
				return String.format("Sucessfuly removed permission %s.", node);
			}
			}
		}
		else if(query.indexOf(".")<query.indexOf(" ")){
			String node = query.substring(0,query.indexOf("."));
			String nQuery = query.substring(query.indexOf(".")+1).trim();
			IProfileInfoBase<?> info = getTag(node);
			if(info==null)
				return String.format("Failed to understand %s.", query);
			return info.query(nQuery);
		}
		else{
			String node = query.substring(0, query.indexOf(" "));
			String nQuery = query.substring(query.indexOf(" ")).trim();
			IProfileInfoBase<?> info = getTag(node);
			if(info==null)
				return String.format("Failed to understand %s.", query);
			return info.query(nQuery);
		}
		return null;

	}
	public String getValue(String key) {
		if(getTag(key)==null)
			return String.format("Failed to get the value of %s, key does not exist.", key);
		return infoTags.get(key).toString();
	}

	@Override
	public void givePermission(IPermission p) {
		permissions.add(p);

	}

	@Override
	public boolean isOperator(PlayerList l) {
		UserListOpsEntry entry = l.getOppedPlayers().getEntry(owner.getGameProfile());
		if(entry==null)
			return false;
		return entry.getPermissionLevel()>=1;
	}

	@Override
	public EntityPlayerMP owner() {
		// TODO Auto-generated method stub
		return owner;
	}
	@Override
	public int getOperatorLevel(PlayerList l) {
		if(!isOperator(l))
			return 0;
		else
		{
			UserListOpsEntry entry = l.getOppedPlayers().getEntry(owner.getGameProfile());
			return entry.getPermissionLevel();
		}
	}

	public static final float computeResistance(float resistance) {
		return resistance*0.20f;
	}

	@Override
	public void removePermission(IPermission p) {
		permissions.remove(p);

	}
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound comp = new NBTTagCompound();
		for(String s:infoTags.keySet()) {
			IProfileInfoBase<?> tag = getTag(s);
			if(tag.isPersistant())
				comp.setTag(s, tag.serializeNBT());
		}
		return comp;
	}
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		for(String s:infoTags.keySet()) {
			IProfileInfoBase<?> tag = getTag(s);
			if(tag.isPersistant())
				tag.deserializeNBT(nbt.getTag(s));
		}

	}
	public String[] dump() {
		List<String> ret = Lists.newArrayList();
		ret.add("Permissions:");
		for(IPermission p:permissions.getValue().keySet())
			if(permissions.contains(p))
				ret.add(PermissionManager.getName(p));
		ret.add("");
		for(String s:infoTags.keySet())
			if(s.equals("Permission"))
				continue;
			else
				ret.add(s+"="+infoTags.get(s).toString());
		return ret.toArray(new String[0]);
	}
	public String toString() {
		return String.join("\n", dump());
	}
	@Override
	public void update() {
		for(ITickable subscriber:updateSubscribers)
			subscriber.update();
		for(AttributeModifier mod:attrs.getAttributeInstanceSet().stream().map(IAttributeInstance::getModifiers).collect(()->new ArrayList<AttributeModifier>(), (l,c)->l.addAll(c), ArrayList::addAll)) {
			if(mod instanceof TemporaryAttributeModifier)
				if(((TemporaryAttributeModifier)mod).hasExpired())
					attrs.getAttributeInstanceByName(mod.getName()).removeModifier(mod.getID());
		}

	}

	protected <T extends Number> boolean setNumber(String key,T k) {
		IProfileInfoBase<?> tag = getTag(key);
		if(!k.getClass().isAssignableFrom(tag.getType()))
			return false;
		IProfileInfoBase<T> num = (IProfileInfoBase<T>)tag;
		num.setValue(k);
		return true;
	}

	protected Number getNumber(String key) {
		IProfileInfoBase<?> tag = getTag(key);
		Object o = tag.getValue();
		if(o instanceof Number)
			return (Number)o;
		return 0;
	}

	public int getInt(String key) {
		return getNumber(key).intValue();
	}

	public boolean setInt(String key,int val) {
		return setNumber(key,val);
	}

	public float getFloat(String key) {
		return getNumber(key).floatValue();
	}

	public boolean setFloat(String key,float value) {
		return setNumber(key,value);
	}

	public long getLong(String key) {
		return getNumber(key).longValue();
	}

	public boolean setLong(String key,long value) {
		return setNumber(key,value);
	}

	public double getDouble(String key) {
		return getNumber(key).doubleValue();
	}

	public boolean setDouble(String key,double value) {
		return setNumber(key,value);
	}

	public boolean increment(String key) {
		return setNumber(key,getInt(key)+1);
	}
	public boolean incrementLong(String key) {
		return setNumber(key,getLong(key)+1);
	}

	public boolean decrement(String key) {
		return setNumber(key,getInt(key)-1);
	}
	public boolean decrementLong(String key) {
		return setNumber(key,getLong(key)-1);
	}

	public boolean getBoolean(String key) {
		IProfileInfoBase<?> tag = getTag(key);
		Object o = tag.getValue();
		if(o instanceof Boolean)
			return ((Boolean)o).booleanValue();
		return false;
	}

	/**
	 * Obtains the game profile which this PlayerProfile implements.
	 * Note that this is a convience method for owner().getGameProfile()
	 * @see EntityPlayerMP#getGameProfile()
	 * @return the profile of the wrapped player
	 */
	public GameProfile getGameProfile() {
		return owner.getGameProfile();
	}

	@Override
	public int compareTo(PlayerProfile o) {
		UUID id = this.owner.getUniqueID();
		UUID oId = this.owner.getUniqueID();
		return id.compareTo(oId);
	}

	@Override
	public boolean equals(Object o) {
		if(o==null)
			return false;
		else if(o==this)
			return true;
		else if(!(o instanceof PlayerProfile))
			return false;
		return compareTo((PlayerProfile)o)==0;
	}

	@Override
	public int hashCode() {
		int nHash = owner.getName().hashCode();
		int idHash = owner.getUniqueID().hashCode();
		return ~(nHash^idHash);
	}
	public World getWorld() {
		// TODO Auto-generated method stub
		return owner.getEntityWorld();
	}

	public static void entityAttacked(LivingAttackEvent e) {
		if(e.getEntityLiving() instanceof EntityPlayerMP) {
			DamageSource src = e.getSource();
			if(src.getEntity()==null)
				return;
			else if(src.getEntity() instanceof EntityPlayerMP) {
				EntityPlayerMP attacked = (EntityPlayerMP)e.getEntityLiving();
				EntityPlayerMP attacker = (EntityPlayerMP)src.getEntity();
				MinecraftForge.EVENT_BUS.post(new PlayerCombatEvent(get(attacked),get(attacker),src));
			}
		}
	}

	public static void entityDamage(LivingHurtEvent e) {
		if(e.getEntityLiving() instanceof EntityPlayerMP) {
			DamageSource src = e.getSource();
			if(src.getEntity()==null)
				return;
			else if(src.getEntity() instanceof EntityPlayerMP) {
				EntityPlayerMP attacked = (EntityPlayerMP)e.getEntityLiving();
				EntityPlayerMP attacker = (EntityPlayerMP)src.getEntity();
				PlayerDamageEvent e1 = new PlayerDamageEvent(get(attacked),get(attacker),src);
				MinecraftForge.EVENT_BUS.post(e1);
				if(e1.getModifier()==0.0f)
					e.setCanceled(true);
				e.setAmount(e.getAmount()*e1.getModifier());
			}
		}
	}


}
