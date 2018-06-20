package gmail.chorman64.gac14.basic.players.event;

import com.mojang.authlib.GameProfile;

import gmail.chorman64.gac14.basic.players.PlayerProfile;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Fires when a player is about to be banned. It uses the GameProfile, rather than the PlayerProfile as it can be fired for offline players.
 * The event is cancelable. <br/>
 * If the player executing the ban command does not have The ManagementPermission BAN_BLOCKING, then canceling the event will prevent the player from being banned.
 * If the player has that permission canceling the event has no effect.
 * @author Connor Horman
 *
 */
@Cancelable
public class PlayerBanEvent extends Event {
	
	public final GameProfile prof;
	public final ICommandSender sender;
	
	public PlayerBanEvent(GameProfile prof,ICommandSender sender) {
		this.prof = prof;
		this.sender = sender;
		
	}
	/* (non-Javadoc)
	 * @see net.minecraftforge.fml.common.eventhandler.Event#isCancelable()
	 */
	@Override
	public boolean isCancelable() {
		// TODO Auto-generated method stub
		return true;
	}
	
	/* (non-Javadoc)
	 * @see net.minecraftforge.fml.common.eventhandler.Event#hasResult()
	 */
	@Override
	public boolean hasResult() {
		// TODO Auto-generated method stub
		return false;
	}
	
	

}
