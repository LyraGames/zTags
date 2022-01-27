package net.lyragames.ztags.utils.menu.buttons;

import lombok.AllArgsConstructor;
import net.lyragames.ztags.utils.Color;
import net.lyragames.ztags.utils.ItemBuilder;
import net.lyragames.ztags.utils.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class Glass extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder item =  new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(Color.translate(" "));
        return item.build();
    }

    @Override
    public void clicked(Player player, int i, ClickType clickType, int hb) {
        //Button.playNeutral(player);
    }

}
