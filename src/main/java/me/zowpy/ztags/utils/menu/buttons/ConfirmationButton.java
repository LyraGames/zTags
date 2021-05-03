package me.zowpy.ztags.utils.menu.buttons;

import lombok.AllArgsConstructor;
import me.zowpy.ztags.utils.menu.TypeCallback;
import me.zowpy.ztags.utils.Color;
import me.zowpy.ztags.utils.ItemBuilder;
import me.zowpy.ztags.utils.menu.Button;
import me.zowpy.ztags.utils.menu.TypeCallback;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class ConfirmationButton extends Button {

    private boolean confirm;
    private TypeCallback<Boolean> callback;
    private boolean closeAfterResponse;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder item = new ItemBuilder(Material.WOOL);

        item.durability(this.confirm ? ((byte) 5) : ((byte) 14));
        item.name(Color.translate(this.confirm ? "&aConfirm" : "&cCancel"));

        return item.build();
    }

    @Override
    public void clicked(Player player, int i, ClickType clickType, int hb) {
        if (this.confirm) {
            player.playSound(player.getLocation(), Sound.NOTE_PIANO, 20f, 0.1f);
        } else {
            player.playSound(player.getLocation(), Sound.DIG_GRAVEL, 20f, 0.1F);
        }

        if (this.closeAfterResponse) {
            player.closeInventory();
        }

        this.callback.callback(this.confirm);
    }

}

