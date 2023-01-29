package com.tympanic.fixes.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class LogFinder {
    private static int OAK_LOG = 0;
    private static int ACACIA_LOG = 0;
    private static int SPRUCE_LOG = 0;
    private static int BIRCH_LOG = 0;
    private static int JUNGLE_LOG = 0;
    private static int DARK_OAK_LOG = 0;
    private static int MANGROVE_LOG = 0;

    @Nullable
    public static String findLogsAroundPlayer(PlayerEntity player) {
        World world = player.world;
        BlockPos playerPos = player.getBlockPos();
        int chunkX = playerPos.getX() >> 4;
        int chunkZ = playerPos.getZ() >> 4;

        for (BlockPos pos : BlockPos.iterate(chunkX - 1 << 4, 0, chunkZ - 1 << 4, chunkX + 2 << 4, 256, chunkZ + 2 << 4)) {
            BlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            if (Blocks.OAK_LOG.equals(block)) {
                OAK_LOG += 1;
            } else if (Blocks.ACACIA_LOG.equals(block)) {
                ACACIA_LOG += 1;
            } else if (Blocks.SPRUCE_LOG.equals(block)) {
                SPRUCE_LOG += 1;
            } else if (Blocks.BIRCH_LOG.equals(block)) {
                BIRCH_LOG += 1;
            } else if (Blocks.JUNGLE_LOG.equals(block)) {
                JUNGLE_LOG += 1;
            } else if (Blocks.DARK_OAK_LOG.equals(block)) {
                DARK_OAK_LOG += 1;
            } else if (Blocks.MANGROVE_LOG.equals(block)) {
                MANGROVE_LOG += 1;
            }
        }
        int max = Math.max(Math.max(Math.max(Math.max(Math.max(Math.max(OAK_LOG, ACACIA_LOG), SPRUCE_LOG), BIRCH_LOG), JUNGLE_LOG), DARK_OAK_LOG), MANGROVE_LOG);
        if (max == OAK_LOG) {
            return "oak_log";
        } else if (max == ACACIA_LOG) {
            return "acacia_log";
        } else if (max == SPRUCE_LOG) {
            return "spruce_log";
        } else if (max == BIRCH_LOG) {
            return "birch_log";
        } else if (max == JUNGLE_LOG) {
            return "jungle_log";
        } else if (max == DARK_OAK_LOG) {
            return "dark_oak_log";
        } else if (max == MANGROVE_LOG) {
            return "mangrove_log";
        }
        return null;
    }
}

