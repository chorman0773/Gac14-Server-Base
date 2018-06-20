package gmail.chorman64.gac14.basic.players.permission;


import gmail.chorman64.gac14.basic.permission.IPermission;
import gmail.chorman64.gac14.basic.permission.PermissionBase;
import gmail.chorman64.gac14.basic.permission.PermissionManager;
import gmail.chorman64.gac14.basic.permission.RootPermission;

public class OperatorPermission extends PermissionBase{

	public static final OperatorPermission OP = new OperatorPermission("operator","Base Permission for Operators",RootPermission.ROOT);
	public static final OperatorPermission COMMAND = new OperatorPermission("command","Base Permission for Operator commands",OP);
	public static final OperatorPermission COMMAND_KICK = new OperatorPermission("kick","Permission to kick players",COMMAND);
	public static final OperatorPermission COMMAND_PEX = new OperatorPermission("profile","Permission to use the pex-command",COMMAND);
	public static final OperatorPermission COMMAND_LIST = new OperatorPermission("list","Permission to list all online players",COMMAND);
	public static final OperatorPermission COMMAND_AO = new OperatorPermission("adminchat","Permission to use, and listen too the Admin Chat",COMMAND);
	public static final OperatorPermission CLEAR = new OperatorPermission("clear","Base Permission for clearing Inventory",OP);
	public static final OperatorPermission CLEAR_INVENTORY = new OperatorPermission("inventory","Enables use of /clear",CLEAR);
	public static final OperatorPermission CLEAR_ENDER = new OperatorPermission("ender","Enables use of /clearender",CLEAR);


	static {
		OP.registerSubpermission(COMMAND);
		COMMAND.registerSubpermission(COMMAND_KICK);
		COMMAND.registerSubpermission(COMMAND_PEX);
		COMMAND.registerSubpermission(COMMAND_LIST);
		COMMAND.registerSubpermission(COMMAND_AO);
		OP.registerSubpermission(CLEAR);
		CLEAR.registerSubpermission(CLEAR_INVENTORY);
		CLEAR.registerSubpermission(CLEAR_ENDER);
		PermissionManager.registerPermission(OP);
	}

	OperatorPermission(String name,String description,IPermission root){
		super(root,name,description,2);
	}


}
