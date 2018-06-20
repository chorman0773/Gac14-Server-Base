package gmail.chorman64.gac14.basic.client.server;

import gmail.chorman64.gac14.basic.client.IPacket;
import gmail.chorman64.gac14.basic.client.IPacketHandler;
import net.minecraft.network.PacketBuffer;

public class ConnectionHandler implements IPacketHandler<ConnectionHandler> {

	private int magic;

	public ConnectionHandler(int magic) {
		this.magic = magic;
	}

	@Override
	public String getChannelName() {
		// TODO Auto-generated method stub
		return "gac14-auth";
	}


	@Override
	public IPacket<ConnectionHandler> getPacket(PacketBuffer src) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public PacketBuffer encodePacket(IPacket<ConnectionHandler> packet) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void handlePacket(PacketBuffer buff) {
		// TODO Auto-generated method stub

	}

}
