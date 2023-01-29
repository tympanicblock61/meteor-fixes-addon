package com.tympanic.fixes;

import com.mojang.logging.LogUtils;
import com.tympanic.fixes.commands.AutoStart;
import com.tympanic.fixes.commands.EnchantCalc;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.commands.Commands;
import org.slf4j.Logger;


public class Addon extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        LOG.info("Initializing Meteor Fixes Addon");
        Commands.get().add(new AutoStart());
        Commands.get().add(new EnchantCalc());
    }

    @Override
    public void onRegisterCategories() {

    }

    @Override
    public String getPackage() {

        return "com.tympanic.addon";
    }
}
