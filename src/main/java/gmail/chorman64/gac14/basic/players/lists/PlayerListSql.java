package gmail.chorman64.gac14.basic.players.lists;

import java.sql.Connection;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;

public class PlayerListSql extends PlayerList {

	private Connection con;

	public PlayerListSql(MinecraftServer server,Connection c) {
		super(server);
		this.con = c;
	}

}
