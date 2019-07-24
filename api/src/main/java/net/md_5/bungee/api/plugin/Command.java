package net.md_5.bungee.api.plugin;

import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.CommandSender;

/**
 * A command that can be executed by a {@link CommandSender}.
 */
@Data
@RequiredArgsConstructor(access = AccessLevel.NONE)
public abstract class Command
{

    private final String name;
    private final String permission;
    private final String[] aliases;
    private String label;

    /**
     * Construct a new command with no permissions or aliases.
     *
     * @param name the name of this command
     */
    public Command(String name)
    {
        this( name, null );
    }

    /**
     * Construct a new command.
     *
     * @param name primary name of this command
     * @param permission the permission node required to execute this command,
     * null or empty string allows it to be executed by everyone
     * @param aliases aliases which map back to this command
     */
    public Command(String name, String permission, String... aliases)
    {
        Preconditions.checkArgument( name != null, "name" );
        this.name = name;
        this.permission = permission;
        this.aliases = aliases;
        this.label = name;
    }

    /**
     * Execute this command with the specified sender and arguments.
     *
     * @param sender the executor of this command
     * @param label - the label used to invoke this command
     * @param args arguments used to invoke this command
     */
    pulbic void execute(CommandSender sender, String label, String[] args)
    {
        this.label = label;
        execute(sender, args);
    }

    public abstract void execute(CommandSender sender, String[] args);

    /**
     * Check if this command can be executed by the given sender.
     *
     * @param sender the sender to check
     * @return whether the sender can execute this
     */
    public boolean hasPermission(CommandSender sender)
    {
        return permission == null || permission.isEmpty() || sender.hasPermission( permission );
    }
    
    public final String getLabel()
    {
        return label;
    }
}
