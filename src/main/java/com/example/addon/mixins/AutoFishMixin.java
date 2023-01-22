package com.example.addon.mixins;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.player.AutoFish;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AutoFish.class, remap = false)
public abstract class AutoFishMixin extends Module {
    private AutoFishMixin(Category category, String name, String description) {
        super(category, name, description);
    }

    private Setting<Boolean> dura;
    private Setting<Integer> duraAmount;
    private Setting<Boolean> duraSwitch;
    private Setting<Integer> duraSwitchSlot;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectInit(CallbackInfo ci) {
        SettingGroup sgDurability = settings.createGroup("Durability detection");

        dura = sgDurability.add(new BoolSetting.Builder()
            .name("Durability")
            .description("Automatically stops and changes rods on durability low")
            .onChanged((Boolean n) -> {
                if (n) {
                    ChatUtils.sendMsg(0, "AutoFish", Formatting.DARK_RED, Formatting.GRAY, "Durability on");
                } else {
                    ChatUtils.sendMsg(0, "AutoFish", Formatting.DARK_RED, Formatting.GRAY, "Durability off");
                }
            })
            .defaultValue(false)
            .build()
        );

        duraAmount = sgDurability.add(new IntSetting.Builder()
            .name("Durability Stop Amount")
            .description("amount used to stop and switch rods")
            .visible(dura::get)
            .defaultValue(1)
            .sliderRange(1, 64)
            .build()
        );

        duraSwitch = sgDurability.add(new BoolSetting.Builder()
            .name("Rod Switch")
            .description("Automatically changes rod with one that has high enough durability")
            .defaultValue(false)
            .visible(dura::get)
            .onChanged((Boolean n) -> {
                if (n) {
                    ChatUtils.sendMsg(0, "AutoFish", Formatting.DARK_RED, Formatting.GRAY, "Durability switch on");
                } else {
                    ChatUtils.sendMsg(0, "AutoFish", Formatting.DARK_RED, Formatting.GRAY, "Durability switch off");
                }
            })
            .build()
        );

        duraSwitchSlot = sgDurability.add(new IntSetting.Builder()
            .name("Rod Switch Slot")
            .description("the slot the rod switches")
            .visible(() -> dura.get() && duraSwitch.get())
            .defaultValue(0)
            .sliderRange(0, 9)
            .build()
        );
    }

    @Inject(method = "onTick", at = @At("HEAD"))
    private void onTick(TickEvent.Post event, CallbackInfo ci) {
        assert mc.player != null;
        PlayerInventory inventory = mc.player.getInventory();
        if (dura.get()){
            if (duraSwitch.get() && inventory.main.get(duraSwitchSlot.get()).getMaxDamage() - inventory.main.get(duraSwitchSlot.get()).getDamage() <= duraAmount.get()) {
                boolean foundItem = false;
                int count;
                for (count = 0; count<inventory.size(); count++) {
                    ItemStack stack = inventory.getStack(count);
                    System.out.println(stack.getMaxDamage() - stack.getDamage());
                    if (stack.getTranslationKey().equals("item.minecraft.fishing_rod") && stack.getMaxDamage() - stack.getDamage() > duraAmount.get()) {
                        ChatUtils.sendMsg(0, "AutoFish", Formatting.DARK_RED, Formatting.GRAY, "Durability switch item found: switching slots");
                        ItemStack stack2 = inventory.main.get(duraSwitchSlot.get());
                        inventory.main.set(count, stack2);
                        inventory.main.set(duraSwitchSlot.get(), stack);
                        foundItem = true;
                        break;
                    }
                }
                if (!foundItem) {
                    ChatUtils.sendMsg(0, "AutoFish", Formatting.DARK_RED, Formatting.GRAY, "Durability switch cannot find another rod: stopping");
                    toggle();
                }
            } else if (inventory.main.get(inventory.selectedSlot).getMaxDamage() - inventory.main.get(inventory.selectedSlot).getDamage() <= duraAmount.get()){
                ChatUtils.sendMsg(0, "AutoFish", Formatting.DARK_RED, Formatting.GRAY, "Durability matches setting: stopping");
                toggle();
            }
        }
    }

    @Inject(method = "onTick", at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/utils/Utils;rightClick()V"))
    private void onRightClick(CallbackInfo ci) {
        if (dura.get() && duraSwitch.get()) {
            assert mc.player != null;
            if (mc.player.getInventory().selectedSlot != duraSwitchSlot.get()) {
                ChatUtils.sendMsg(0, "AutoFish", Formatting.BLUE, Formatting.GRAY, "Switching selected slot to slot in settings");
                mc.player.getInventory().selectedSlot = duraSwitchSlot.get();
            }
        }
        Utils.rightClick();
    }
}
