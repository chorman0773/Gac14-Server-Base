package gmail.chorman64.gac14.basic.players.event;

import gmail.chorman64.gac14.basic.players.PlayerProfile;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PlayerProfileEvent extends Event {
	public final PlayerProfile prof;
	public PlayerProfileEvent(PlayerProfile prof) {
		this.prof = prof;
	}
}
