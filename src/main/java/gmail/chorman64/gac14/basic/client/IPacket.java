package gmail.chorman64.gac14.basic.client;

import net.minecraft.network.PacketBuffer;

public interface IPacket<T extends IPacketHandler<T>> {
	PacketBuffer encode();
	void decode(PacketBuffer data);
}
