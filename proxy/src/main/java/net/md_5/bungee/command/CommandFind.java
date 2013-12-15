package net.md_5.bungee.command;

import java.util.Collections;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class CommandFind extends PlayerCommand
{

    public CommandFind()
    {
        super( "find", "bungeecord.command.find" );
    }

    @Override
    public void execute(CommandSender sender, String[] args)
    {
        if ( args.length != 1 )
        {
            sender.sendMessage( ChatColor.RED + "Please follow this command by a user name" );
        } else
        {
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer( args[0] );
            if ( player == null || player.getLocation().getServer() == null )
            {
                sender.sendMessage( ChatColor.RED + "That user is not online" );
            } else
            {
                sender.sendMessage( ChatColor.BLUE + args[0] + " is online at " + player.getLocation().getServer().getInfo().getName() );
            }
        }
    }
}
