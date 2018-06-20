package gmail.chorman64.gac14.basic.client;

import net.minecraft.network.PacketBuffer;

public interface IPacketHandler<T extends IPacketHandler<T>> {
String getChannelName();
IPacket<T> getPacket(PacketBuffer src);
PacketBuffer encodePacket(IPacket<T> packet);
void handlePacket(PacketBuffer buff);

}
