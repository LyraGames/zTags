package net.lyragames.ztags.tag.menu;

import com.cryptomorin.xseries.XMaterial;
import net.lyragames.ztags.profile.Profile;
import net.lyragames.ztags.tag.Tag;
import net.lyragames.ztags.tag.impl.TagHandler;
import net.lyragames.ztags.utils.Color;
import net.lyragames.ztags.utils.ItemBuilder;
import net.lyragames.ztags.utils.menu.Button;
import net.lyragames.ztags.utils.menu.CC;
import net.lyragames.ztags.utils.menu.buttons.Glass;
import net.lyragames.ztags.utils.menu.pagination.PaginatedMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TagsMenu extends PaginatedMenu {

    @Override
    public String getPrePaginatedTitle(Player player) {
        return Color.translate("&7Tags");
    }

    @Override
    public boolean isUpdateAfterClick() {
        return true;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> toReturn = new HashMap<>();

        int i = 0;
        for (Tag tag : TagHandler.getTags()) {
            if (i < 27) {
                toReturn.put(i, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        Profile profile = Profile.getByUUID(player.getUniqueId());

                        if (profile.getTag() == null) {
                            if (!player.hasPermission(tag.getPermission())) {
                                return new ItemBuilder(XMaterial.RED_DYE.parseItem()).name(CC.PRIMARY + tag.getName()).lore(Arrays.asList(
                                        Color.translate("&7&m-------------"),
                                        CC.PRIMARY + "Tag: " + CC.SECONDARY + Color.translate(tag.getPrefix()),
                                        Color.translate("&7&m-------------"),
                                        "",
                                        CC.RED + "You do not have access to this tag."
                                )).build();
                            }

                            if (player.hasPermission(tag.getPermission())) {
                                return new ItemBuilder(XMaterial.GRAY_DYE.parseItem()).name(CC.PRIMARY + tag.getName()).lore(Arrays.asList(
                                        Color.translate("&7&m-------------"),
                                        CC.PRIMARY + "Tag: " + CC.SECONDARY + Color.translate(tag.getPrefix()),
                                        Color.translate("&7&m-------------"),
                                        "",
                                        CC.GREEN + "Click to set this as your tag."
                                )).build();
                            }
                            return null;
                        }
                        if (profile.getTag().equals(tag) && player.hasPermission(tag.getPermission())) {
                            return new ItemBuilder(XMaterial.LIME_DYE.parseItem()).name(CC.PRIMARY + tag.getName()).lore(Arrays.asList(
                                    Color.translate("&7&m-------------"),
                                    CC.PRIMARY + "Tag: " + CC.SECONDARY + Color.translate(tag.getPrefix()),
                                    Color.translate("&7&m-------------"),
                                    "",
                                    CC.RED + "You already equipped this tag!"
                            )).build();
                        }

                        if (!player.hasPermission(tag.getPermission())) {
                            return new ItemBuilder(XMaterial.RED_DYE.parseItem()).name(CC.PRIMARY + tag.getName()).lore(Arrays.asList(
                                    Color.translate("&7&m-------------"),
                                    CC.PRIMARY + "Tag: " + CC.SECONDARY + Color.translate(tag.getPrefix()),
                                    Color.translate("&7&m-------------"),
                                    "",
                                    CC.RED + "You do not have access to this tag."
                            )).build();
                        }

                        if (player.hasPermission(tag.getPermission()) && !profile.getTag().equals(tag)) {
                            return new ItemBuilder(XMaterial.GRAY_DYE.parseItem()).name(CC.PRIMARY + tag.getName()).lore(Arrays.asList(
                                    Color.translate("&7&m-------------"),
                                    CC.PRIMARY + "Tag: " + CC.SECONDARY + Color.translate(tag.getPrefix()),
                                    Color.translate("&7&m-------------"),
                                    "",
                                    CC.GREEN + "Click to set this as your tag."
                            )).build();
                        }

                        return null;
                    }

                    @Override
                    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                        if (player.hasPermission(tag.getPermission())) {
                            Profile prof = Profile.getByUUID(player.getUniqueId());
                            if (prof.getTag() == null) {
                                prof.setTag(tag);
                                player.sendMessage(CC.PRIMARY + "You have successfully set your tag to " + CC.SECONDARY + tag.getName());
                                return;
                            }
                            if (prof.getTag().equals(tag)) {
                                player.sendMessage(CC.RED + "You already equipped this tag!");
                                return;
                            }

                            if (prof.getTag() != tag) {
                                prof.setTag(tag);
                                prof.save();
                                player.sendMessage(CC.PRIMARY + "You have successfully set your tag to " + CC.SECONDARY + tag.getName());
                                return;
                            }
                        }else {
                            player.sendMessage(CC.RED + "You do not have permission to use this tag.");
                        }

                    }
                });
                i++;
            }

        }

        return toReturn;
    }

    @Override
    public int getSize() {
        return 9*4;
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> toReturn = new HashMap<>();

        toReturn.put(27, new Glass());
        toReturn.put(28, new Glass());
        toReturn.put(29, new Glass());
        toReturn.put(30, new Glass());
        toReturn.put(31, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(XMaterial.HOPPER.parseItem()).name(CC.YELLOW + "Click to reset your tag!").build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                Profile profile = Profile.getByUUID(player.getUniqueId());
                if (profile.getTag() == null) {
                    player.sendMessage(CC.RED + "You don't have any tag enabled!");
                    return;
                }

                profile.setTag(null);
                profile.save();
                player.sendMessage(CC.PRIMARY + "Successfully cleared your tag!");
            }


        });
        toReturn.put(32, new Glass());
        toReturn.put(33, new Glass());
        toReturn.put(34, new Glass());
        toReturn.put(35, new Glass());



        return toReturn;
    }
}
