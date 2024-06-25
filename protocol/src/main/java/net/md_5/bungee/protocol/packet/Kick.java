package net.md_5.bungee.protocol.packet;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.ProtocolConstants;
import net.md_5.bungee.protocol.util.ChatDeserializable;
import net.md_5.bungee.protocol.util.ChatDeserializableJson;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Kick extends DefinedPacket
{

    private ChatDeserializable message;

    @Override
    public void read(ByteBuf buf, Protocol protocol, ProtocolConstants.Direction direction, int protocolVersion)
    {
        if ( protocol == Protocol.LOGIN )
        {
            message = new ChatDeserializableJson( readString( buf ) );
        } else
        {
            message = readBaseComponent( buf, protocolVersion );
        }
    }

    @Override
    public void write(ByteBuf buf, Protocol protocol, ProtocolConstants.Direction direction, int protocolVersion)
    {
        if ( protocol == Protocol.LOGIN )
        {
            writeString( ComponentSerializer.toString( message.get() ), buf );
        } else
        {
            writeBaseComponent( message, buf, protocolVersion );
        }
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }
}
