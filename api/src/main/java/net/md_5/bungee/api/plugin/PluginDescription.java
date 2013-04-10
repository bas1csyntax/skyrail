package net.md_5.bungee.api.plugin;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * POJO representing the plugin.yml file.
 */
@Data
@AllArgsConstructor
public class PluginDescription
{

    /**
     * Friendly name of the plugin.
     */
    private String name;
    /**
     * Plugin main class. Needs to extend {@link Plugin}.
     */
    private String main;
    /**
     * Plugin version.
     */
    private String version;
    /**
     * Plugin author.
     */
    private String author;
    /**
     * Plugin hard dependencies.
     */
    private Set<String> depends;

    public PluginDescription()
    {
    }
}
