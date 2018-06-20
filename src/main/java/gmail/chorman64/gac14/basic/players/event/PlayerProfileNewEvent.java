package gmail.chorman64.gac14.basic.players.event;

import gmail.chorman64.gac14.basic.players.PlayerProfile;

/**
 * Fired when a Profile is created for a Player for the first time.
 * @author Connor Horman
 *
 */
public class PlayerProfileNewEvent extends PlayerProfileEvent {

	public PlayerProfileNewEvent(PlayerProfile prof) {
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
