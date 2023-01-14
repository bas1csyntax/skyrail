package net.md_5.bungee.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class CommandIP extends PlayerCommand
{

    public CommandIP()
    {
        super( "ip", "bungeecord.command.ip" );
    }

    @Override
    public void execute(CommandSender sender, String[] args)
    {
        if ( args.length < 1 )
        {
            sender.sendMessage( ProxyServer.getInstance().getTranslation( "username_needed" ) );
            return;
        }
        ProxiedPlayer user = ProxyServer.getInstance().getPlayer( args[0] );
        if ( user == null )
        {
            sender.sendMessage( ProxyServer.getInstance().getTranslation( "user_not_online" ) );
        } else
        {
            TextComponent message = new TextComponent( ProxyServer.getInstance().getTranslation( "command_ip", user.getName(), user.getSocketAddress() ) );
            message.setClickEvent( new ClickEvent( ClickEvent.Action.COPY_TO_CLIPBOARD, user.getAddress().getAddress().getHostAddress() ) );
            sender.sendMessage( message );
        }
    }
}
