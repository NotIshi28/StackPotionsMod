package com.notishi28.stackplus;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.commands.Commands;


@Mod("stackplus")
public class StackPlus {
    private static boolean isEnabled = true; // Global toggle for the mod
    private static int stackLimit = 64;

    public StackPlus() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::commonSetup);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ForgeRegistries.ITEMS.getValues().forEach(item -> {
                if (16 >= new ItemStack(item).getMaxStackSize() && isEnabled) {
                    try {
                        ObfuscationReflectionHelper.setPrivateValue(Item.class, item, stackLimit, "f_41370_");
                    } catch (Exception e) {
                        System.err.println("Failed to modify stack size for item: " + item.getRegistryName());
                    }
                }
            });
        });
    }
    private void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
            Commands.literal("stackplus")
                .then(Commands.literal("enable")
                    .executes(context -> {
                        isEnabled = true;
                        stackLimit=50;
                        context.getSource().sendSuccess(new net.minecraft.network.chat.TextComponent("EzStack mod enabled!"), true);
                        return 1;
                }))
                .then(Commands.literal("disable")
                    .executes(context -> {
                        isEnabled = false;
                        context.getSource().sendSuccess(new net.minecraft.network.chat.TextComponent("EzStack mod disabled!"), true);
                        System.out.println(isEnabled);
                        return 1;
                }))
        );
    }
}