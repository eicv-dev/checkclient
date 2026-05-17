package net.checkclient;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class CheckClientCommand {

    public static void registerClient(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
            ClientCommandManager.literal("checkclient")
                .executes(ctx -> {
                    String myBrand = clientBrand();
                    String myEstimate = estimate(myBrand);

                    var nh = MinecraftClient.getInstance().getNetworkHandler();
                    String serverBrand = (nh != null && nh.getBrand() != null)
                        ? nh.getBrand() : "unknown";

                    ctx.getSource().sendFeedback(
                        Text.literal("> check client v1.0\n").formatted(Formatting.GOLD)
                            .append(Text.literal(" Your client:  ").formatted(Formatting.GRAY))
                            .append(Text.literal(myBrand).formatted(Formatting.WHITE))
                            .append(Text.literal("  -> ").formatted(Formatting.DARK_GRAY))
                            .append(Text.literal(myEstimate).formatted(Formatting.GREEN))
                            .append(Text.literal("\n Server brand: ").formatted(Formatting.GRAY))
                            .append(Text.literal(serverBrand).formatted(Formatting.WHITE))
                            .append(Text.literal("  -> ").formatted(Formatting.DARK_GRAY))
                            .append(Text.literal(estimate(serverBrand)).formatted(Formatting.AQUA)));
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
        return "Unrecognized (\"" + brand + "\")";
    }
}
