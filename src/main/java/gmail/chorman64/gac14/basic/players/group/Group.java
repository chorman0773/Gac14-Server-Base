package gmail.chorman64.gac14.basic.players.group;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import gmail.chorman64.gac14.basic.permission.IPermission;
import gmail.chorman64.gac14.basic.players.permission.ManagementPermission;
import gmail.chorman64.gac14.basic.players.permission.OperatorPermission;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class Group implements Comparable<Group>  {

	public static void clinit(){}

	public static final Map<String,Group> groups = Maps.newHashMap();
	public static Group def = new Group("default",new TextComponentString("\u00a78\u00a7oDefault\u00a7r"),0);
	public static Group admin = new Group("admin",new TextComponentString("\u00a7l[\u00a7k%\u00a7r\u00a74\u00a7lAdmin\u00a7r\u00a7l\u00a7k%\u00a7r\u00a7l]\u00a7r"),15).addPermission(OperatorPermission.COMMAND)
			.addPermission(ManagementPermission.ACTION_PROTECT);

	final Set<IPermission> permissionsToSet = Sets.newHashSet();
	public final String name;
	public final int level;
	public final ITextComponent prefix;
	public Group(String name, ITextComponent tag, int level) {
		this.name = name;
		groups.put(name, this);
		this.level = level;
		this.prefix = tag;
	}
	protected Group addPermission(IPermission node) {
		permissionsToSet.add(node);
		return this;
	}
	protected Group removePermission(IPermission node) {
		permissionsToSet.remove(node);
		return this;
	}
	@Override
	public int compareTo(Group other) {
		if(this.level<other.level)
			return -1;
		else if(this.level==other.level)
			return 0;
		else
			return 1;
	}


}
