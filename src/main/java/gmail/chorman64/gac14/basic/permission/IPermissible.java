package gmail.chorman64.gac14.basic.permission;

import net.minecraft.server.management.PlayerList;

/**
 * An entity which can be given permissions and can take permissible actions.
 * Certain Objects are permissible without implementing this class, but those are internally controlled
 * by the permission manager.
 * Implementations of this interface MUST make the following exception guarentee:
 * 	-No Implementation of any method of this interface MAY NOT throw an exception.
 * @author Connor Horman
 *
 */
public interface IPermissible {
	/**
	 * Checks if The given permission is set on the Permissible Object.
	 * @param l The current player list, to check if the permission enables bypass for the current operator level
	 * @param p The permission object. 
	 * @return true if the current permissible has the given permission
	 */
	boolean hasPermission(PlayerList l, IPermission p);
	/**
	 * If possible, adds the given permission as an accessable permission.
	 * This method should modify this object such that,
	 * if Any non-null IPermission p is passed to givePermission. until either removePermission(p) is called or
	 * Some low level change is made to the internal permission list, for any non-null PermissionList l,
	 * hasPermission(l,p) returns true.
	 * 
	 * @param p
	 */
	void givePermission(IPermission p);
	void removePermission(IPermission p);
	boolean isOperator(PlayerList l);
	int getOperatorLevel(PlayerList l);
	default boolean isRoot() {
		return false;
	}
}
