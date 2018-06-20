package gmail.chorman64.gac14.basic.permission;

import net.minecraft.server.management.PlayerList;

public interface IPermission {
	boolean operatorHasPermission(int level,PlayerList l);
	String description();
	default boolean isManagement() {
		return false;
	}
	IPermission getParent();
	default boolean isRoot() {
		return false;
	}
	IPermission getTreeElement(String key);
	String getName();
}
