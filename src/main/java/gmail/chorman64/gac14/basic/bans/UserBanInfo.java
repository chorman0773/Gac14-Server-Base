package gmail.chorman64.gac14.basic.bans;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import gmail.chorman64.gac14.basic.Core;
import gmail.chorman64.gac14.basic.IOwnable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.util.INBTSerializable;

public final class UserBanInfo implements IOwnable<UUID>, INBTSerializable<NBTTagCompound>, Comparable<UserBanInfo> {
	private Map<UUID,UserBanInfo> currentBans = Maps.newTreeMap();
	private static Set<UserBanInfo> bans = Sets.newTreeSet();
	private UUID owner;
	private String loggedName;
	private Instant expiry;
	private Instant given;
	private UUID bannerId;
	private String bannerName;
	private String reason;
	private boolean hasLifted;
	private int pardonReason;
	private UUID pardonerId;
	private String pardonerName;
	private static final MinecraftServer server = Core.instance.server;
	
	public static final int REASON_NONPARDONNED = 0, REASON_MANUAL = 1, REASON_EXPIRED = 2, 
			REASON_AUTOPARDON = 3, REASON_UNKNOWN = 4;
	
	private static final String[] reasonString = new String[] {"Ban is still in effect","Pardonned by Operator",
			"Ban Expired", "Unban Purchased", "Unknown Reason"
	};
	
	protected UserBanInfo(UUID owner) {
		// TODO Auto-generated constructor stub
	}
	protected UserBanInfo() {}
	@Override
	public UUID owner() {
		// TODO Auto-generated method stub
		return owner;
	}
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound out = new NBTTagCompound();
		out.setString("PlayerId", owner.toString());
		out.setString("PlayerLoggedName",loggedName);
		out.setString("IssuerId", bannerId.toString());
		out.setString("IssuerName", bannerName);
		out.setLong("IssueInstant", given.toEpochMilli());
		out.setLong("ExpiryInstant",given.toEpochMilli());
		out.setString("BanReason",reason);
		out.setBoolean("HasLifted", hasLifted);
		if(hasLifted) {
		NBTTagCompound pardon = out.getCompoundTag("PardonInfo");
		pardon.setInteger("PardonReason", pardonReason);
		pardon.setString("PardonerId", pardonerId.toString());
		pardon.setString("PardonerName", pardonerName);
		}
		
		return out;
	}
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
		
	}
	
	public void update() {
		if(hasLifted)
			return;
		Instant now = Instant.now();
		if(now.isAfter(expiry)) {
			hasLifted = true;
			pardonReason = REASON_EXPIRED;
		}
	}
	@Override
	public int compareTo(UserBanInfo o) {
		// TODO Auto-generated method stub
		return given.compareTo(o.given);
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(loggedName).append("(").append(owner.toString()).append(")").append("\n")
		.append("Banned By: ").append(bannerName).append("(").append(bannerId).append(")").append("\n")
		.append("For ").append(reason);
		if(hasLifted) {
			builder.append("Lifted by:").append(pardonerName).append("(").append(pardonerId).append(")").append("\n")
			.append(reasonString[pardonReason]);
		}else
			builder.append(reasonString[REASON_NONPARDONNED]);
		return builder.toString();
	}
	

}
