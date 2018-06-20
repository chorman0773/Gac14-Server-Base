package gmail.chorman64.gac14.basic.players.lists;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.management.UserListOps;
import net.minecraft.server.management.UserListOpsEntry;

public class OpListSql extends UserListOps {

	public OpListSql() {
		super(null);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see net.minecraft.server.management.UserListOps#getKeys()
	 */
	@Override
	public String[] getKeys() {
		// TODO Auto-generated method stub
		return super.getKeys();
	}

	/* (non-Javadoc)
	 * @see net.minecraft.server.management.UserListOps#getPermissionLevel(com.mojang.authlib.GameProfile)
	 */
	@Override
	public int getPermissionLevel(GameProfile profile) {
		// TODO Auto-generated method stub
		return super.getPermissionLevel(profile);
	}

	/* (non-Javadoc)
	 * @see net.minecraft.server.management.UserListOps#bypassesPlayerLimit(com.mojang.authlib.GameProfile)
	 */
	@Override
	public boolean bypassesPlayerLimit(GameProfile profile) {
		// TODO Auto-generated method stub
		return super.bypassesPlayerLimit(profile);
	}

	/* (non-Javadoc)
	 * @see net.minecraft.server.management.UserListOps#getObjectKey(com.mojang.authlib.GameProfile)
	 */
	@Override
	protected String getObjectKey(GameProfile obj) {
		// TODO Auto-generated method stub
		return super.getObjectKey(obj);
	}

	/* (non-Javadoc)
	 * @see net.minecraft.server.management.UserListOps#getGameProfileFromName(java.lang.String)
	 */
	@Override
	public GameProfile getGameProfileFromName(String username) {
		// TODO Auto-generated method stub
		return super.getGameProfileFromName(username);
	}

	/* (non-Javadoc)
	 * @see net.minecraft.server.management.UserList#addEntry(net.minecraft.server.management.UserListEntry)
	 */
	@Override
	public void addEntry(UserListOpsEntry entry) {
		// TODO Auto-generated method stub
		super.addEntry(entry);
	}

}
