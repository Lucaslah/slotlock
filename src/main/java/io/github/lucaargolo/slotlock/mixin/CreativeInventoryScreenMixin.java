package io.github.lucaargolo.slotlock.mixin;

import io.github.lucaargolo.slotlock.Slotlock;
import io.github.lucaargolo.slotlock.mixed.HandledScreenMixed;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeInventoryScreen.class)
public abstract class CreativeInventoryScreenMixin extends AbstractInventoryScreen<CreativeInventoryScreen.CreativeScreenHandler> {

    @Shadow private Slot deleteItemSlot;

    public CreativeInventoryScreenMixin(CreativeInventoryScreen.CreativeScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @Inject(at = @At("HEAD"), method = "onMouseClick", cancellable = true)
    public void onMouseClick(Slot slot, int invSlot, int clickData, SlotActionType actionType, CallbackInfo info) {

        PlayerInventory playerInventory = this.client.player.getInventory();

        if (slot == this.deleteItemSlot && actionType == SlotActionType.QUICK_MOVE) {
            for (int i = 0; i < playerInventory.size(); ++i) {
                if (!Slotlock.isLocked(i)) {
                    playerInventory.removeStack(i);
                }
            }
            info.cancel();
        }
        Slotlock.handleMouseClick(handler, ((HandledScreenMixed) this).slotlock$getPlayerInventory(), slot, invSlot, clickData, actionType, info);
    }

}
