package net.checkclient;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class CheckClientCommand {

    public static void registerClient(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
            ClientCommands.literal("checkclient")
                .executes(ctx -> {
                    String myBrand = clientBrand();
                    String myEstimate = estimate(myBrand);

                    var nh = Minecraft.getInstance().getConnection();
                    String serverBrand = (nh != null && nh.serverBrand() != null)
                        ? nh.serverBrand() : "unknown";

                    ctx.getSource().sendFeedback(
                        Component.literal("> check client v1.0\n").withStyle(ChatFormatting.GOLD)
                            .append(Component.literal(" Your client:  ").withStyle(ChatFormatting.GRAY))
                            .append(Component.literal(myBrand).withStyle(ChatFormatting.WHITE))
                            .append(Component.literal("  -> ").withStyle(ChatFormatting.DARK_GRAY))
                            .append(Component.literal(myEstimate).withStyle(ChatFormatting.GREEN))
                            .append(Component.literal("\n Server brand: ").withStyle(ChatFormatting.GRAY))
                            .append(Component.literal(serverBrand).withStyle(ChatFormatting.WHITE))
                            .append(Component.literal("  -> ").withStyle(ChatFormatting.DARK_GRAY))
                            .append(Component.literal(estimate(serverBrand)).withStyle(ChatFormatting.AQUA)));
                    return 1;
                }));
    }

    private static String clientBrand() {
        boolean fabric = FabricLoader.getInstance().isModLoaded("fabric")
            || FabricLoader.getInstance().isModLoaded("fabricloader");
        return fabric ? "fabric" : "vanilla";
    }

    public static String estimate(String brand) {
        String lower = brand.toLowerCase();
        if (lower.equals("vanilla"))                                 return "Vanilla";
        if (lower.contains("fabric"))                                return "Fabric (modded)";
        if (lower.contains("quilt"))                                 return "Quilt";
        if (lower.contains("neoforge"))                              return "NeoForge";
        if (lower.contains("forge"))                                 return "MinecraftForge";
        if (lower.contains("lunar"))                                 return "Lunar Client";
        if (lower.contains("badlion"))                               return "Badlion Client";
        if (lower.contains("labymod"))                               return "LabyMod";
        if (lower.contains("feather"))                               return "Feather Client";
        if (lower.contains("paper"))                                 return "Paper";
        if (lower.contains("spigot"))                                return "Spigot";
        if (lower.contains("bukkit"))                                return "Bukkit";
        if (lower.contains("purpur"))                                return "Purpur";
        if (lower.contains("velocity") || lower.contains("bungee"))  return "Proxy";
        if (lower.isBlank() || lower.equals("unknown"))              return "Unknown / possibly spoofed";
        return "Unrecognized(\"" + brand + "\")";
    }
}
