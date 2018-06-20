package gmail.chorman64.gac14.basic.event;

import gmail.chorman64.gac14.basic.Core;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Fired to inform mods that they can load data from the dataDir now.
 * The event is fired after Core has been initalized and guarentees that all fields of Core are Initialized.<br/>
 * This event is Not Cancelable<br/>
 * This event does not have a result<br/>
 * This event contains an instance of Core.
 * @author Connor Horman
 *
 */
public final class LoadEvent extends Event {
	private Core core;
	public LoadEvent(Core c) {
		this.core = c;
	}
	public Core getCoreInstance() {
		return core;
	}

}
