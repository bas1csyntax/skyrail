package net.md_5.bungee.chat;

import com.google.common.base.Preconditions;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Locale;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.nbt.NBTUtil;
import net.md_5.bungee.api.chat.nbt.NbtEntity;
import net.md_5.bungee.api.chat.nbt.NbtItem;

public class BaseComponentSerializer
{

    protected void deserialize(JsonObject object, BaseComponent component, JsonDeserializationContext context)
    {
        if ( object.has( "color" ) )
        {
            component.setColor( ChatColor.valueOf( object.get( "color" ).getAsString().toUpperCase( Locale.ROOT ) ) );
        }
        if ( object.has( "bold" ) )
        {
            component.setBold( object.get( "bold" ).getAsBoolean() );
        }
        if ( object.has( "italic" ) )
        {
            component.setItalic( object.get( "italic" ).getAsBoolean() );
        }
        if ( object.has( "underlined" ) )
        {
            component.setUnderlined( object.get( "underlined" ).getAsBoolean() );
        }
        if ( object.has( "strikethrough" ) )
        {
            component.setStrikethrough( object.get( "strikethrough" ).getAsBoolean() );
        }
        if ( object.has( "obfuscated" ) )
        {
            component.setObfuscated( object.get( "obfuscated" ).getAsBoolean() );
        }
        if ( object.has( "insertion" ) )
        {
            component.setInsertion( object.get( "insertion" ).getAsString() );
        }
        if ( object.has( "extra" ) )
        {
            component.setExtra( Arrays.<BaseComponent>asList( context.<BaseComponent[]>deserialize( object.get( "extra" ), BaseComponent[].class ) ) );
        }

        //Events
        if ( object.has( "clickEvent" ) )
        {
            JsonObject event = object.getAsJsonObject( "clickEvent" );
            component.setClickEvent( new ClickEvent(
                    ClickEvent.Action.valueOf( event.get( "action" ).getAsString().toUpperCase( Locale.ROOT ) ),
                    event.get( "value" ).getAsString() ) );
        }
        if ( object.has( "hoverEvent" ) )
        {
            JsonObject event = object.getAsJsonObject( "hoverEvent" );
            HoverEvent.Action hoverAction = HoverEvent.Action.valueOf( event.get( "action" ).getAsString().toUpperCase( Locale.ROOT ) );
            HoverEvent hoverEvent = null;
            if ( hoverAction == HoverEvent.Action.SHOW_ITEM )
            {
                //TODO
                //String nbt = context.deserialize( event.get( "value" ), String.class );
                //hoverEvent = HoverEvent.showItem( context.deserialize( event.get( "value" ), String.class ) );
            } else if ( hoverAction == HoverEvent.Action.SHOW_ENTITY )
            {
                //TODO
                //hoverEvent = HoverEvent.showEntity( context.deserialize( event.get( "value" ), String.class ) );
            } else
            {
                BaseComponent[] res;
                if ( event.get( "value" ).isJsonArray() )
                {
                    res = context.deserialize( event.get( "value" ), BaseComponent[].class );
                } else
                {
                    res = new BaseComponent[]
                    {
                        context.<BaseComponent>deserialize( event.get( "value" ), BaseComponent.class )
                    };
                }
                hoverEvent = new HoverEvent( HoverEvent.Action.valueOf( event.get( "action" ).getAsString().toUpperCase( Locale.ROOT ) ), res );
            }
            component.setHoverEvent( hoverEvent );
        }
    }

    protected void serialize(JsonObject object, BaseComponent component, JsonSerializationContext context)
    {
        boolean first = false;
        if ( ComponentSerializer.serializedComponents.get() == null )
        {
            first = true;
            ComponentSerializer.serializedComponents.set( Collections.newSetFromMap( new IdentityHashMap<BaseComponent, Boolean>() ) );
        }
        try
        {
            Preconditions.checkArgument( !ComponentSerializer.serializedComponents.get().contains( component ), "Component loop" );
            ComponentSerializer.serializedComponents.get().add( component );
            if ( component.getColorRaw() != null )
            {
                object.addProperty( "color", component.getColorRaw().getName() );
            }
            if ( component.isBoldRaw() != null )
            {
                object.addProperty( "bold", component.isBoldRaw() );
            }
            if ( component.isItalicRaw() != null )
            {
                object.addProperty( "italic", component.isItalicRaw() );
            }
            if ( component.isUnderlinedRaw() != null )
            {
                object.addProperty( "underlined", component.isUnderlinedRaw() );
            }
            if ( component.isStrikethroughRaw() != null )
            {
                object.addProperty( "strikethrough", component.isStrikethroughRaw() );
            }
            if ( component.isObfuscatedRaw() != null )
            {
                object.addProperty( "obfuscated", component.isObfuscatedRaw() );
            }
            if ( component.getInsertion() != null )
            {
                object.addProperty( "insertion", component.getInsertion() );
            }

            if ( component.getExtra() != null )
            {
                object.add( "extra", context.serialize( component.getExtra() ) );
            }

            //Events
            if ( component.getClickEvent() != null )
            {
                JsonObject clickEvent = new JsonObject();
                clickEvent.addProperty( "action", component.getClickEvent().getAction().toString().toLowerCase( Locale.ROOT ) );
                clickEvent.addProperty( "value", component.getClickEvent().getValue() );
                object.add( "clickEvent", clickEvent );
            }
            if ( component.getHoverEvent() != null )
            {
                JsonObject hoverEvent = new JsonObject();
                hoverEvent.addProperty( "action", component.getHoverEvent().getAction().toString().toLowerCase( Locale.ROOT ) );
                if ( component.getHoverEvent().getValue() instanceof NbtItem )
                {
                    hoverEvent.add( "value", context.serialize( NBTUtil.nbtAsString( ( (NbtItem) component.getHoverEvent().getValue() ).asTag() ) ) );
                } else if ( component.getHoverEvent().getValue() instanceof NbtEntity )
                {
                    hoverEvent.add( "value", context.serialize( NBTUtil.nbtAsString( ( (NbtEntity) component.getHoverEvent().getValue() ).asTag() ) ) );
                } else
                {
                    hoverEvent.add( "value", context.serialize( component.getHoverEvent().getValue() ) );
                }
                object.add( "hoverEvent", hoverEvent );
            }
        } finally
        {
            ComponentSerializer.serializedComponents.get().remove( component );
            if ( first )
            {
                ComponentSerializer.serializedComponents.set( null );
            }
        }
    }
}
