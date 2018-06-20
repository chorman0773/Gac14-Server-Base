package gmail.chorman64.gac14.basic;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;

import static gmail.chorman64.gac14.basic.Core.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

import gmail.chorman64.gac14.basic.chat.AdminChatChannel;
import gmail.chorman64.gac14.basic.chat.GlobalChatChannel;
import gmail.chorman64.gac14.basic.chat.IChatChannel;
import gmail.chorman64.gac14.basic.commands.CommandBanOverride;
import gmail.chorman64.gac14.basic.commands.CommandClearBase;
import gmail.chorman64.gac14.basic.commands.CommandClearBase.Vanilla;
import gmail.chorman64.gac14.basic.commands.CommandListPlayers;
import gmail.chorman64.gac14.basic.commands.CommandPex;
import gmail.chorman64.gac14.basic.commands.CommandVarOp;
import gmail.chorman64.gac14.basic.event.LoadEvent;
import gmail.chorman64.gac14.basic.event.SaveEvent;
import gmail.chorman64.gac14.basic.permission.PermissionManager;
import gmail.chorman64.gac14.basic.players.PlayerProfile;
import gmail.chorman64.gac14.basic.players.permission.ManagementPermission;
import gmail.chorman64.gac14.basic.players.permission.OperatorPermission;

@Mod(version=VER, modid = ID,serverSideOnly=true)
public class Core {

	public static final String ID = "serverutils";
	public static final String VER = "2.0.0";
	public static final String[] GLOBAL_MOD_BLACKLIST = new String[]{};
	@Instance(ID)
	public static Core instance;



	public MinecraftServer server;
	public PermissionManager permissionManager;
	public File dataDirectory;
	public IChatChannel chat;
	public IChatChannel admin;
	public String[] modBlacklist;


	public Properties conf;

	@EventHandler public void serverStarting(FMLServerStartingEvent e) throws IOException {

		this.server = e.getServer();
		this.permissionManager = new PermissionManager(this.server.getPlayerList());

		dataDirectory = new File(this.server.getDataDirectory(),"/files");
		if(!dataDirectory.exists())
			dataDirectory.mkdir();
		PlayerProfile.setProfileDirectory(new File(dataDirectory,"players"));
		File configFile = new File(dataDirectory,"/config.properties");
		conf = new Properties();
		if(configFile.exists())
			conf.load(new FileReader(configFile));
		else
			configFile.createNewFile();
		e.registerServerCommand(new CommandBanOverride());
		e.registerServerCommand(new CommandListPlayers());
		e.registerServerCommand(new CommandPex());
		e.registerServerCommand(new CommandVarOp());
		e.registerServerCommand(new CommandClearBase(OperatorPermission.CLEAR_INVENTORY,"clear","/clear [player] [item] [data] [maxCount] [nbt]",Vanilla.INV));
		e.registerServerCommand(new CommandClearBase(OperatorPermission.CLEAR_ENDER,"clearender","/clearender [player] [item] [data] [maxCount] [nbt]",Vanilla.ENDER));
		chat = new GlobalChatChannel(server);
		admin = new AdminChatChannel(server);
		MinecraftForge.EVENT_BUS.post(new LoadEvent(this));
		String blacklistedMods = conf.getProperty("BlacklistedMods");
		if(blacklistedMods==null)
			modBlacklist = new String[0];
		else
			modBlacklist = blacklistedMods.split(";");
	}

	@EventHandler
	public void serverStopping(FMLServerStoppingEvent e) throws IOException {
		MinecraftForge.EVENT_BUS.post(new SaveEvent());
		conf.store(new FileWriter(new File(dataDirectory,"/config.properties")), null);
	}

	public static ITextComponent format(String format,Object... o) {
		return new TextComponentString(String.format(format, o));
	}
	@NetworkCheckHandler
	public boolean acceptConnect(Map<String,String> mods,Side side) {
		if(side==Side.SERVER)
			return true;
		for(String s:GLOBAL_MOD_BLACKLIST)
			if(mods.containsKey(s))
				return false;
		for(String s:modBlacklist)
			if(mods.containsKey(s))
				return false;
		return true;
	}

}
