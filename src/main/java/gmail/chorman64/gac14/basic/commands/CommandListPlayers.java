package gmail.chorman64.gac14.basic.commands;

import java.util.Comparator;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import gmail.chorman64.gac14.basic.players.PlayerProfile;
import gmail.chorman64.gac14.basic.players.group.Group;
import gmail.chorman64.gac14.basic.players.permission.OperatorPermission;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TextComponentString;

public class CommandListPlayers extends PermissibleCommandBase {

	private static final Comparator<EntityPlayerMP> nameCaseInsenitive = new Comparator<EntityPlayerMP>() {

		@Override
		public int compare(EntityPlayerMP arg0, EntityPlayerMP arg1) {
			// TODO Auto-generated method stub
			return arg0.getName().compareToIgnoreCase(arg1.getName());
		}};



	public CommandListPlayers() {
		super(OperatorPermission.COMMAND_LIST, "list", "/list [group=*]");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		SortedMap<Group,Set<String>> players = Maps.newTreeMap();
		for(Group g:Group.groups.values()) {
			players.put(g, Sets.newHashSet());
		}
		PlayerList list = server.getPlayerList();
		for(String s:list.getOnlinePlayerNames()) {
			EntityPlayerMP player = list.getPlayerByUsername(s);
			PlayerProfile prof = PlayerProfile.get(player);
			Group g = (Group)prof.getTag("Group").getValue();
			players.get(g).add(s);

		}
		if(args.length==0) {
			for(Group g:players.keySet()) {
				if(players.get(g).size()==0)continue;
				String msg = g.name+" >> "+players.get(g).size();
				for(String s:players.get(g))
					msg+=s+" ";
				sender.sendMessage(new TextComponentString(msg));
			}
		}else {
			String group = args[0];
			Group g = Group.groups.getOrDefault(group,Group.def);
			if(players.get(g).size()==0)
				sender.sendMessage(new TextComponentString("There are no players online in this group."));
			else {
				String msg = g.name+" >> "+players.get(g).size();
				for(String s:players.get(g))
					msg+=s+" ";
				sender.sendMessage(new TextComponentString(msg));
			}
		}
	}

}
