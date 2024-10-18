package me.orbitium.oFHeroes.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {

    private final ItemStack itemStack;
    public static NamespacedKey globalKey;

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
    }

    public ItemBuilder(Material material, int amount) {
        this.itemStack = new ItemStack(material, amount);
    }

    public ItemBuilder withName(String name) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            itemStack.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder setCustomModelData(int customModelData) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setCustomModelData(customModelData);
        itemStack.setItemMeta(itemMeta);

        return this;
    }

    public void addGlow() {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            // Add an enchantment (e.g., ARROW_INFINITE) to the item
            meta.addEnchant(Enchantment.FLAME, 1, true);
            // Hide the enchantment details
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            itemStack.setItemMeta(meta);
        }
    }

    public ItemBuilder withLore(String... lore) {
        ItemMeta meta = itemStack.getItemMeta();
        List<String> updatedLore = new ArrayList<>();

        for (String string : lore)
            updatedLore.add(ChatColor.translateAlternateColorCodes('&', string));

        if (meta != null) {
            meta.setLore(updatedLore);
            itemStack.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder withLore(List<String> lore) {
        ItemMeta meta = itemStack.getItemMeta();
        List<String> updatedLore = new ArrayList<>();

        for (String string : lore)
            updatedLore.add(ChatColor.translateAlternateColorCodes('&', string));

        if (meta != null) {
            meta.setLore(updatedLore);
            itemStack.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder addLore(String lore) {
        ItemMeta meta = itemStack.getItemMeta();
        List<String> updatedLore = meta.getLore();
        if (updatedLore == null)
            updatedLore = new ArrayList<>();

        updatedLore.add(ChatColor.translateAlternateColorCodes('&', lore));
        meta.setLore(updatedLore);
        itemStack.setItemMeta(meta);

        return this;
    }

    public ItemBuilder withCustomHead(String texture) {
        if (itemStack.getType() == Material.PLAYER_HEAD) {
            SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
            if (skullMeta != null) {
                skullMeta.setDisplayName("Custom Head");

                // Apply the custom texture
                String textureJson = "{\"textures\":{\"SKIN\":{\"url\":\"http://textures.minecraft.net/texture/" + texture + "\"}}}";
                skullMeta.setOwningPlayer(null); // Required for the head to be properly updated

                itemStack.setItemMeta(skullMeta);
            }
        } else {
            throw new IllegalArgumentException("Item is not a player head!");
        }
        return this;
    }

    public ItemBuilder withCustomTag(String key, String value) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.getPersistentDataContainer().set(new NamespacedKey("yourplugin", key), PersistentDataType.STRING, value);
            itemStack.setItemMeta(meta);
        }
        return this;
    }

    public ItemMeta getItemMeta() {
        return itemStack.getItemMeta();
    }

    public void setItemMeta(ItemMeta itemMeta) {
        itemStack.setItemMeta(itemMeta);
    }

    public ItemStack build() {
        if (globalKey != null) {
            ItemMeta meta = itemStack.getItemMeta();
            if (meta != null) {
                meta.getPersistentDataContainer().set(globalKey, PersistentDataType.STRING, "custom_value");
                itemStack.setItemMeta(meta);
            }
        }
        return itemStack;
    }

    // Static method to create a custom player head with a base64 texture string
    public static ItemStack createCustomHead(String base64Texture) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        if (skullMeta != null) {
            skullMeta.setDisplayName("Custom Head");

            // Apply the custom texture
            String textureJson = "{\"textures\":{\"SKIN\":{\"url\":\"http://textures.minecraft.net/texture/" + base64Texture + "\"}}}";
            skullMeta.setOwningPlayer(null); // Required for the head to be properly updated

            head.setItemMeta(skullMeta);
        }
        return head;
    }
}
