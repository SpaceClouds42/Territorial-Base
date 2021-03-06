package io.github.profjb58.territorial;

import io.github.profjb58.territorial.config.TBConfig;
import io.github.profjb58.territorial.event.*;
import io.github.profjb58.territorial.networking.C2SPackets;
import io.github.profjb58.territorial.util.ActionLogger;
import io.github.profjb58.territorial.util.LockUtils;
import io.github.profjb58.territorial.util.SideUtils;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Territorial implements ModInitializer {

	public static final String MOD_ID = "territorial";
	public static final String BASE_GROUP_ID = "territorial_base";
	public static final Logger logger = LogManager.getLogger();
	public static ActionLogger actionLogger;
	
	public static final ItemGroup BASE_GROUP = FabricItemGroupBuilder.build(
			new Identifier(MOD_ID, BASE_GROUP_ID),
			() -> new ItemStack(TerritorialRegistry.LOCKPICK));

	@Override
	public void onInitialize() {
		AutoConfig.register(TBConfig.class, GsonConfigSerializer::new);

		if(SideUtils.isDedicatedServer()) {
			actionLogger = new ActionLogger();
			actionLogger.write(ActionLogger.LogType.INFO, "Server started... ");
		};

		TerritorialRegistry.registerAll();

		AttackBlockHandler.init();
		AttackEntityHandler.init();
		WorldTickHandler.init();
		UseBlockHandler.init();
		C2SPackets.init();
	}

	public static TBConfig getConfig() {
		return AutoConfig.getConfigHolder(TBConfig.class).getConfig();
	}
}
