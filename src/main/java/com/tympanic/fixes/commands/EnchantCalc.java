package com.tympanic.fixes.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.systems.commands.Command;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

import static net.minecraft.registry.Registries.ENCHANTMENT;

//this was just some testing rn

public class EnchantCalc extends Command {
        public EnchantCalc() {
            super("enchant-calc", "gets the basic minecraft stuff when ran");
        }

        private static Map<Enchantment,Integer> parseEnchantmentsWithLevels(String enchantments){
            Map<Enchantment,Integer> enchantmentsWithLevel = new HashMap<>();
            String[] enchs = enchantments.split(" ");
            for (String ench : enchs) {
                String[] parts = ench.split(":");
                Enchantment enchantment = ENCHANTMENT.getOrEmpty(new Identifier(parts[0])).orElse(null);
                if (enchantment == null) {
                    continue;
                }
                int level = Integer.parseInt(parts[1]);
                enchantmentsWithLevel.put(enchantment, level);
            }
            return enchantmentsWithLevel;
        }

        @Override
        public void build(LiteralArgumentBuilder<CommandSource> builder) {
            builder.then(literal("tool")
                .then(argument("enchantments", StringArgumentType.greedyString())
                    .executes(context -> {
                        PlayerEntity player = mc.player;
                        String enchantments = StringArgumentType.getString(context, "enchantments");
                        ItemStack item = player.getMainHandStack();
                        if(item.getItem() instanceof ToolItem){
                            Map<Enchantment,Integer> enchantmentsWithLevel = parseEnchantmentsWithLevels(enchantments);
                            for(Enchantment enchant : enchantmentsWithLevel.keySet()){
                                item.addEnchantment(enchant, enchantmentsWithLevel.get(enchant));
                            }
                            ChatUtils.sendMsg(Text.of("Successfully enchanted tool"));
                            return 1;
                        }else{
                            ChatUtils.sendMsg(Text.of("You have to hold a tool in your main hand"));
                            return 0;
                        }
                    })));
        }
}
