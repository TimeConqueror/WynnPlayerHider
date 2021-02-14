package ru.timeconqueror.wph;

import com.wynntils.Reference;
import com.wynntils.core.events.custom.WynnWorldEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Mod.EventBusSubscriber(Side.CLIENT)
public class CHEvents {
    private static List<String> friends = new ArrayList<>();
    private static long nextExecution = 5000L;

    private static int friendCommandCounter = 0;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRenderLiving(RenderLivingEvent.Pre<?> event) {
        if (event.getEntity() instanceof EntityPlayer) {
            Minecraft mc = Minecraft.getMinecraft();
            EntityLivingBase player = event.getEntity();

            if (!CHConfig.type.equals("vanish")) {
                GlStateManager.pushMatrix();
            }

            if (inWynWorld() && mc.player != player && player.getName().length() >= 3 && player.getName().length() <= 16 && !friends.contains(player.getName())) {
                if (CHConfig.type.equals("scale")) {
                    float distance = mc.player.getDistance(player);

                    if (distance < CHConfig.distance) {
                        double distFactor = easeInExpo(distance / CHConfig.distance);
                        float factor = (float) Math.max(distFactor, CHConfig.size);

                        GlStateManager.scale(factor, factor, factor);
                        GlStateManager.translate(-event.getX(), -event.getY(), -event.getZ());
                        GlStateManager.translate(event.getX() / factor, event.getY() / factor, event.getZ() / factor);
                    }
                } else if (CHConfig.type.equals("vanish")) {
                    event.setCanceled(true);
                }
            }
        }
    }

    public static double easeInExpo(double x) {
        return x == 0 ? 0 : Math.pow(2, 10 * x - 10);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPostLiving(RenderLivingEvent.Post<?> event) {
        if (event.getEntity() instanceof EntityPlayer) {
            if (!CHConfig.type.equals("vanish")) {
                GlStateManager.popMatrix();
            }
        }
    }

    @SubscribeEvent
    public static void onSoundPlay(PlaySoundEvent event) {
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.PlayerTickEvent event) {
        if (inWynWorld() && event.phase == TickEvent.Phase.START && event.side == Side.CLIENT) {
            if (Minecraft.getSystemTime() - nextExecution > 2000L) {
                nextExecution = Minecraft.getSystemTime();

                Minecraft.getMinecraft().player.sendChatMessage("/friends list");

                friendCommandCounter++;
            }
        }
    }

    @SubscribeEvent
    public static void onMessageReceived(ClientChatReceivedEvent event) {
        if (friendCommandCounter > 0) {
            String text = event.getMessage().getUnformattedText();

            Minecraft mc = Minecraft.getMinecraft();
            mc.player.getName();

            if (text.equals("We couldn't find any friends.")) {
                CHEvents.friends = Collections.emptyList();
                event.setCanceled(true);
            } else if (text.equals("Try typing /friend add Username!")) {
                event.setCanceled(true);
                friendCommandCounter--;
            } else if (text.startsWith(mc.player.getName() + "'s friends ")) {
                int commaIndex = text.indexOf(":");

                String friendList = "";
                if (commaIndex > -1 && commaIndex + 1 < text.length()) {
                    friendList = text.substring(commaIndex + 1);
                }

                String[] friends = friendList.trim().split(",");
                for (int i = 0; i < friends.length; i++) {
                    friends[i] = friends[i].trim();
                }

                CHEvents.friends = Arrays.asList(friends);

                event.setCanceled(true);
                friendCommandCounter--;
            }
        }
    }

    @SubscribeEvent
    public static void onLeave(WynnWorldEvent.Join event) {
        friendCommandCounter = 0;
    }

    /**
     * Calculates the value, that represents the part ({@code percentage}) of the {@code [start-to-end]} range, counting from the {@code start}.
     * Also works if {@code end} is less then {@code start}
     *
     * @param factor percentage value
     * @param start  start point, can be also more than end
     * @param end    end point, can be also less than end
     */
    public static float interpolate(float factor, float start, float end) {
        return start + factor * (end - start);
    }

    public static boolean inWynWorld() {
        return Reference.onWorld;
    }
}
