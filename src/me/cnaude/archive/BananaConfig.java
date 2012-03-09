package me.cnaude.archive;

import org.bukkit.configuration.Configuration;

public class BananaConfig {
    private final Configuration config;
    
    private static final String MAX_SIZE = "max-size-in-kb";
    
    private double maxSize;

    public BananaConfig(BananaLogArchiver plug) {
        config = plug.getConfig();
        
        maxSize = config.getDouble(MAX_SIZE, 10000.0);
    }
    
    public double maxSize() {
        return maxSize;
    }
}
