package net.md_5.bungee.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.md_5.bungee.Util;

import java.util.UUID;

/**
 * Represents the standard list data returned by opening a server in the
 * Minecraft client server list, or hitting it with a packet 0xFE.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerPing
{

    private Protocol version;

    @Data
    @AllArgsConstructor
    public static class Protocol
    {

        private String name;
        private int protocol;
    }
    private Players players;

    @Data
    @AllArgsConstructor
    public static class Players
    {

        private int max;
        private int online;
        private PlayerInfo[] sample;
    }

    @Data
    @AllArgsConstructor
    public static class PlayerInfo
    {

        private String name;
        private UUID uniqueId;

        private static final UUID md5UUID = Util.getUUID( "af74a02d19cb445bb07f6866a861f783" );

        public PlayerInfo(String name, String id)
        {
            setName( name );
            setId( id );
        }

        public void setId(String id)
        {
            try
            {
                uniqueId = Util.getUUID( id );
            } catch ( Exception e )
            {
                // Fallback on a valid uuid otherwise Minecraft complains
                uniqueId = md5UUID;
            }
        }

        public String getId()
        {
            return uniqueId.toString().replaceAll( "-", "" );
        }
    }

    /**
     * Loads a favicon from a File object.
     *
     * @param icon The icon object.
     */
    public void setFavicon(Favicon icon)
    {
        this.favicon = icon.getIcon();
    }

    /**
     * Set's the favicon for this instance. If you want to load a favicon from a
     * File, you should use {@link #setFavicon(net.md_5.bungee.api.Favicon)}
     *
     * @param icon The icon string
     */
    public void setFavicon(String icon)
    {
        this.favicon = icon;
    }

    public ServerPing(final Protocol version, final Players players, final String description, final Favicon favicon)
    {
        this.version = version;
        this.players = players;
        this.description = description;
        this.favicon = favicon.getIcon();
    }

    private String description;
    private String favicon;
}
