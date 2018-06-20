package gmail.chorman64.gac14.basic.players.lists;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import javax.sql.RowSet;

import com.mojang.authlib.GameProfile;

import gmail.chorman64.gac14.basic.Core;
import net.minecraft.server.management.UserListBans;
import net.minecraft.server.management.UserListBansEntry;

public class BanListSql extends UserListBans {

	private String tbl;
	private Connection con;
	private Map<UUID,UserListBansEntry> cachedTable = new TreeMap<>();
	private Map<UUID,String> cachedNames = new TreeMap<>();
	private static final DateTimeFormatter frmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	public BanListSql(Connection c,String tblName) {
		super(null);
		this.con = c;
		this.tbl = tblName;
	}
	private UserListBansEntry getAndUpdateActiveBan(UUID id) {
		try {
		Statement s = con.createStatement();
		ResultSet tbl = s.executeQuery(String.format("SELECT * FROM %s WHERE playerId='%s' AND InEffect=true", this.tbl,id));
		if(!tbl.first())
			return null;
		if(tbl.getDate("Expiry").after(Date.from(Instant.now())))
		{
			int banId = tbl.getInt(tbl.findColumn("banId"));
			s.execute(String.format("UPDATE %s SET InEffect=false, PardonReason='Ban Expired', PardonDate=%s WHERE banId = %d", this.tbl,frmt.format(LocalDateTime.now()),banId));
			return null;
		}
		UserListBansEntry e = new UserListBansEntry(Core.instance.server.getPlayerProfileCache().getProfileByUUID(id), tbl.getDate("Issued"), tbl.getString("Reason"), tbl.getDate("Expiry"), tbl.getString("Reason"));
		cachedTable.put(id, e);
		cachedNames.put(id, tbl.getString("playername"));
		return e;
		}catch(SQLException e) {
			return null;
		}
		}

	/* (non-Javadoc)
	 * @see net.minecraft.server.management.UserListBans#isBanned(com.mojang.authlib.GameProfile)
	 */
	@Override
	public boolean isBanned(GameProfile profile) {
		if(cachedTable.containsKey(profile.getId())) {
			UserListBansEntry entry = cachedTable.get(profile.getId());
			if(entry.getBanEndDate().after(Date.from(Instant.now()))) {
				getAndUpdateActiveBan(profile.getId());
				return false;
			}
			else
				return true;
		}else {
			UserListBansEntry entry = getEntry(profile);
			if(entry!=null)
				return true;
			else return false;
		}
	}


	/* (non-Javadoc)
	 * @see net.minecraft.server.management.UserListBans#getBannedProfile(java.lang.String)
	 */
	@Override
	public GameProfile getBannedProfile(String username) {
		GameProfile ref = Core.instance.server.getPlayerProfileCache().getGameProfileForUsername(username);
		if(!isBanned(ref))
			return null;
		else
			return ref;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.server.management.UserList#addEntry(net.minecraft.server.management.UserListEntry)
	 */
	@Override
	public void addEntry(UserListBansEntry entry) {
		// TODO Auto-generated method stub
		super.addEntry(entry);
	}

	/* (non-Javadoc)
	 * @see net.minecraft.server.management.UserList#getEntry(java.lang.Object)
	 */
	@Override
	public UserListBansEntry getEntry(GameProfile obj) {
		if(isBanned(obj))
			return null;
		else if(cachedTable.containsKey(obj.getId()))
			return cachedTable.get(obj.getId());
		else
			return getAndUpdateActiveBan(obj.getId());
	}

	/* (non-Javadoc)
	 * @see net.minecraft.server.management.UserList#removeEntry(java.lang.Object)
	 */
	@Override
	public void removeEntry(GameProfile entry) {
		// TODO Auto-generated method stub
		super.removeEntry(entry);
	}

	/* (non-Javadoc)
	 * @see net.minecraft.server.management.UserList#hasEntry(java.lang.Object)
	 */
	@Override
	protected boolean hasEntry(GameProfile entry) {
		// TODO Auto-generated method stub
		return isBanned(entry);
	}

	/* (non-Javadoc)
	 * @see net.minecraft.server.management.UserList#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		try {
		Statement s = con.createStatement();
		ResultSet r = s.executeQuery("SELECT * FROM "+tbl);
		return !r.first();
		}catch(SQLException e) {
			return true;
		}
	}



}
