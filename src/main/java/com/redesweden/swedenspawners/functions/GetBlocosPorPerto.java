package com.redesweden.swedenspawners.functions;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class GetBlocosPorPerto {
    private List<Block> blocks = new ArrayList<>();

    public GetBlocosPorPerto(Location location, int radius) {
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
    }

    public List<Block> getBlocos() {
        return blocks;
    }
}
