package gmail.chorman64.gac14.basic.players.event;

import gmail.chorman64.gac14.basic.players.PlayerProfile;
import net.minecraft.util.DamageSource;

/**
 * Fired when a player is attacked by annother player, in response to an LivingHurtEvent
 * The event is a filter for only when a player attacks annother player.
 * This event is Not Cancelable
 * @author Connor Horman
 *
 */
public class PlayerCombatEvent extends PlayerProfileEvent {
	private PlayerProfile attacker;
	private DamageSource src;
	public PlayerCombatEvent(PlayerProfile prof,PlayerProfile attacker,DamageSource src) {
		super(prof);
		this.src = src;
		this.attacker = attacker;
	}
	
	public PlayerProfile getAttacker() {
		return attacker;
	}
	public DamageSource getSource() {
		return src;
	}

}
