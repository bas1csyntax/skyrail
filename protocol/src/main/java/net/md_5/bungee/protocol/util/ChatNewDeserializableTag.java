package net.md_5.bungee.protocol.util;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.jetbrains.annotations.NotNull;
import se.llbit.nbt.SpecificTag;

@ToString
@EqualsAndHashCode(callSuper = true)
public class ChatNewDeserializableTag extends ChatNewCapturingDeserializable
{

    public ChatNewDeserializableTag(@NonNull SpecificTag chatTag)
    {
        super( chatTag );
    }

    @NotNull
    @Override
    public BaseComponent deserialize()
    {
        return ComponentSerializer.deserialize( TagUtil.toJson( original() ) );
    }
}
