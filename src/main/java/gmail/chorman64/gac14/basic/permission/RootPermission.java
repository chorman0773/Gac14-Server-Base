package gmail.chorman64.gac14.basic.permission;

import gmail.chorman64.gac14.basic.Core;
import net.minecraft.server.management.PlayerList;

public final class RootPermission implements IPermission {
	public static final IPermission ROOT = new RootPermission();
	private RootPermission() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean operatorHasPermission(int level, PlayerList l) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPermission getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPermission getTreeElement(String key) {
		// TODO Auto-generated method stub
		return Core.instance.permissionManager.getNode(key);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "";
	}

	/* (non-Javadoc)
	 * @see gmail.chorman64.gac14.basic.permission.IPermission#isManagement()
	 */
	@Override
	public boolean isManagement() {
		// TODO Auto-generated method stub
		return true;
	}

	/* (non-Javadoc)
	 * @see gmail.chorman64.gac14.basic.permission.IPermission#isRoot()
	 */
	@Override
	public boolean isRoot() {
		// TODO Auto-generated method stub
		return true;
	}

}
