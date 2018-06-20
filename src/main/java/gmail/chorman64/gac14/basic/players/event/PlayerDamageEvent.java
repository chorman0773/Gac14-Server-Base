package gmail.chorman64.gac14.basic.players.event;

import gmail.chorman64.gac14.basic.players.PlayerProfile;
import net.minecraft.util.DamageSource;

/**
 * Fired when a player deals damage to annother player.
 * This event allows for players to modify the damage dealt, by a multiplicative ammount.
 * Custom Enchantments may use this to modify damage.
 * @author Connor Horman
 *
 */
public class PlayerDamageEvent extends PlayerProfileEvent {

	private PlayerProfile attacker;
	private DamageSource src;
	private float modifier = 0.0f;
	public PlayerDamageEvent(PlayerProfile prof,PlayerProfile attacker,DamageSource src) {
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
	public float getModifier() {
		return modifier;
	}
	public void applyModifier(float f) {
		modifier*=f;
	}


}
