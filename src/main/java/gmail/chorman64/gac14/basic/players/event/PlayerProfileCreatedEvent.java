package gmail.chorman64.gac14.basic.players.event;

import gmail.chorman64.gac14.basic.players.PlayerProfile;

/**
 * Fires whenever a Player Profile is loaded into the games core.
 * This event is not Cancelable
 * This event does not have a result.
 * You can use this time to register Info Tags to the player.
 * @author Connor Horman
 *
 */
public class PlayerProfileCreatedEvent extends PlayerProfileEvent {

	public PlayerProfileCreatedEvent(PlayerProfile prof) {
		super(prof);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see net.minecraftforge.fml.common.eventhandler.Event#isCancelable()
	 */
	@Override
	public boolean isCancelable() {
		// TODO Auto-generated method stub
		return false;
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
