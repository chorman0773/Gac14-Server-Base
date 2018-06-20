package gmail.chorman64.gac14.basic.permission;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.server.management.PlayerList;

public abstract class PermissionBase implements IPermission {

	public PermissionBase(IPermission root,String name,String description,int minPermissionLvl) {
		this.root = root;
		this.name = name;
		this.description = description;
		this.minPermissionLvl = minPermissionLvl;
		if(root instanceof PermissionBase)
			((PermissionBase)root).registerSubpermission(this);
	}
	private Map<String,IPermission> subpermissions = Maps.newTreeMap();
	private IPermission root;
	private String name;
	private String description;
	private int minPermissionLvl;
	private boolean management;

	protected final void registerSubpermission(IPermission perm) {
		subpermissions.put(perm.getName(), perm);
	}
	protected final void registerSubpermissions(IPermission... perms) {
		for(IPermission perm:perms)
			registerSubpermission(perm);
	}
	protected final PermissionBase setManagement(boolean management){
		this.management = management;
		return this;
	}

	/* (non-Javadoc)
	 * @see gmail.chorman64.gac14.basic.permission.IPermission#isManagement()
	 */
	@Override
	public boolean isManagement() {
		// TODO Auto-generated method stub
		return management;
	}
	@Override
	public boolean operatorHasPermission(int level, PlayerList l) {
		if(minPermissionLvl<0)
			return false;
		return level<=minPermissionLvl;
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return description;
	}

	@Override
	public IPermission getParent() {
		// TODO Auto-generated method stub
		return root;
	}

	@Override
	public IPermission getTreeElement(String key) {
		// TODO Auto-generated method stub
		return subpermissions.get(key);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

}
