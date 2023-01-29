package com.tympanic.fixes.commands;

import baritone.api.BaritoneAPI;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.tympanic.fixes.utils.LogFinder;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.commands.Command;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.BlockState;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class AutoStart extends Command {
    public AutoStart() {
        super("auto-start", "gets the basic minecraft stuff when ran");
    }

    private int type = -1;

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("iron")
            .executes(context -> {
                type = 0;
                //System.out.println(BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("mine iron_ore"));
                //new TickHandler();
                var LogType = LogFinder.findLogsAroundPlayer(mc.player);
                if (LogType != null) {
                    BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("mine %s".formatted(LogType));
                } else {
                    ChatUtils.sendMsg("AutoStart", Text.of("Cannot fine logs around you"));
                }
                return SINGLE_SUCCESS;
            })
        );
        builder.then(literal("diamond")
            .executes(context -> {
                type = 1;
                return SINGLE_SUCCESS;
            })
        );
    }

    private static class TickHandler {
        private int ticks;
        public TickHandler() {
            MeteorClient.EVENT_BUS.subscribe(this);
        }

        @EventHandler
        private void onTick(TickEvent.Post event) {
            ticks++;
            if (ticks == 20) {
                MeteorClient.EVENT_BUS.unsubscribe(this);
                ticks = 0;
            }
        }
    }

    private void blockStuff(BlockState block)  {
        System.out.println(block.getBlock().getName());
        System.out.println(block.getBlock().toString());
    }
}
//-2875 113 2951
