package gmail.chorman64.gac14.basic.players.permission;

import gmail.chorman64.gac14.basic.Core;
import gmail.chorman64.gac14.basic.permission.IPermission;
import gmail.chorman64.gac14.basic.permission.PermissionBase;
import gmail.chorman64.gac14.basic.permission.PermissionManager;
import gmail.chorman64.gac14.basic.permission.RootPermission;
import net.minecraft.server.management.PlayerList;

public class ManagementPermission extends PermissionBase {
	/*
	BLOCKBAN("actionblock.ban",true,"Prevents Normal Ops from banning a player with this permission."),
	BLOCKDEOP("actionblock.deop",true,"Prevents Normal Ops from deoping a player with this permission"),
	MANAGE_SERVER("server.general",false,"Manage the server in general. "),
	MODIFY_MANAGEMENT("profile.modify",false,"Allows users with this permission to give or remove these permissions from players"),
	DEOP("deop.normal",true,"Allows players with this permission to deop other operators unless they have a permission which blocks that action"),
	DEOP_BLOCKING("deop.override",false,"Allows players with this permission to deop players that normally block that action"),
	BAN("ban.normal",true,"Allows players with this permission to ban players unless they have a permission which blocks that action"),
	BAN_BLOCKING("ban.override",false,"Allows players with this permission to ban players that normally block that action"),
	OP_PLAYERS("action.op",true,"Allows players with this permission to op players. They cannot op players with a higher permission level then they have");
	*/

	public static final ManagementPermission MANAGEMENT = new ManagementPermission("management",false,"Base of all managment permissions",RootPermission.ROOT);
	public static final ManagementPermission ACTION_PROTECT = new ManagementPermission("actionblock",true,"Prevents certain actions from applying to this user unless the source overrides this protection",MANAGEMENT);
	public static final ManagementPermission BLOCK_BAN = new ManagementPermission("ban",true,"Protects applicible players from being banned",ACTION_PROTECT);
	public static final ManagementPermission BLOCK_DEOP = new ManagementPermission("deop",true,"Protects applicible players from being deopped.",ACTION_PROTECT);
	public static final ManagementPermission SERVER = new ManagementPermission("server",false,"Manage the server in general",MANAGEMENT);
	public static final ManagementPermission PLAYERS = new ManagementPermission("profile",false,"Enables Management Level queries",MANAGEMENT);
	public static final ManagementPermission COMMAND = new ManagementPermission("commands",true,"Enables access to most Management Commands",MANAGEMENT);
	public static final ManagementPermission BAN = new ManagementPermission("ban",true,"Access to /ban",COMMAND);
	public static final ManagementPermission DEOP = new ManagementPermission("deop",true,"Access to /deop",COMMAND);
	public static final ManagementPermission OP = new ManagementPermission("op",true,"Access to /op",COMMAND);
	public static final ManagementPermission OVERRIDES = new ManagementPermission("override",false,"Access to override Action Protections",MANAGEMENT);
	public static final ManagementPermission OVERRIDES_BAN = new ManagementPermission("ban",false,"Able to /ban blocking users",OVERRIDES);
	public static final ManagementPermission OVERRIDES_DEOP = new ManagementPermission("deop",false,"Able to /deop blocking users",OVERRIDES);

	static {
		PermissionManager.registerPermission(MANAGEMENT);
		MANAGEMENT.registerSubpermission(ACTION_PROTECT);
		MANAGEMENT.registerSubpermission(SERVER);
		MANAGEMENT.registerSubpermission(PLAYERS);
		MANAGEMENT.registerSubpermission(COMMAND);
		MANAGEMENT.registerSubpermission(OVERRIDES);
		ACTION_PROTECT.registerSubpermissions(BLOCK_BAN,BLOCK_DEOP);
		COMMAND.registerSubpermissions(BAN,DEOP,OP);
		OVERRIDES.registerSubpermissions(OVERRIDES_BAN,OVERRIDES_DEOP);

	}

	/* (non-Javadoc)
	 * @see gmail.chorman64.gac14.basic.permission.IPermission#isManagement()
	 */
	@Override
	public boolean isManagement() {
		// TODO Auto-generated method stub
		return true;
	}

	 ManagementPermission(String name,boolean bypassesable,String description,IPermission root) {
		super(root,name,description,bypassesable?4:-1);
	}


}
