package carpet.mixins;

import carpet.network.CarpetClient;
import carpet.network.ServerNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin
{
    @Shadow public ServerPlayerEntity player;

    @Inject(method = "onCustomPayload", at = @At("HEAD"), cancellable = true)
    private void onCustomCarpetPayload(CustomPayloadC2SPacket packet, CallbackInfo ci)
    {
        Identifier channel = packet.getChannel();
        if (CarpetClient.CARPET_CHANNEL.equals(channel))
        {
            ServerNetworkHandler.handleData(new PacketByteBuf(packet.getData().copy()), player);
            ci.cancel();
        }
    }
}
