package me.orbitium.oFHeroes.listener;

import me.orbitium.oFHeroes.OFHeroes;
import me.orbitium.oFHeroes.mysql.MySQL;
import me.orbitium.oFHeroes.skill.BaseSkill;
import me.orbitium.oFHeroes.skill.SkillLoader;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BrewingStartEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassEventListeners implements Listener {

    public static Map<Class<? extends Event>, List<BaseSkill>> baseSkills = new HashMap<>();

    @EventHandler
    public void joinListener(PlayerJoinEvent playerJoinEvent) {
        String selectedClass = MySQL.getSelectedClass(playerJoinEvent.getPlayer().getUniqueId().toString());
        if (selectedClass != null) {
            playerJoinEvent.getPlayer().getPersistentDataContainer().set(OFHeroes.selectedHero, PersistentDataType.STRING, selectedClass);
            OFHeroes.sendMessage(playerJoinEvent.getPlayer(), "hero-selected", "%hero%", selectedClass);
        } else
            OFHeroes.sendMessage(playerJoinEvent.getPlayer(), "hero-select-reminder");

        List<AttributeModifier> modifiers = new ArrayList<>();
        modifiers.addAll(playerJoinEvent.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers());

        for (AttributeModifier modifier : modifiers)
            playerJoinEvent.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).removeModifier(modifier);
        playerJoinEvent.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);

//        playerJoinEvent.getPlayer().getPersistentDataContainer().set(OFClasses.playerClass, PersistentDataType.STRING, "unknown");
//        playerJoinEvent.getPlayer().getPersistentDataContainer().set(OFClasses.playerPurchasedSkills, PersistentDataType.STRING, "");
//        playerJoinEvent.getPlayer().getPersistentDataContainer().remove(SorcererBaseSkill.isStunned);

//
//        try {
//            for (Map.Entry<Class<? extends Event>, List<BaseSkill>> entry : baseSkills.entrySet()) {
//                for (BaseSkill baseSkill : entry.getValue()) {
//                    baseSkill.onPlayerAdded(playerJoinEvent.getPlayer(), 0);
//                    System.out.println(baseSkill.skillName + " added");
//                }
//            }
//            for (BaseSkill baseSkill : SkillLoader.skills) {
////                System.out.println(baseSkill);
//                baseSkill.onPlayerAdded(playerJoinEvent.getPlayer(), 1);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        List<BaseSkill> listeningSkills = baseSkills.get(event.getClass());

        if (listeningSkills == null)
            return;

        for (BaseSkill listeningSkill : listeningSkills)
            if (listeningSkill.ownerPlayers.containsKey(player))
                listeningSkill.onEvent(event);
    }

    @EventHandler
    public void onPotionEffect(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player player))
            return;

        List<BaseSkill> listeningSkills = baseSkills.get(event.getClass());

        if (listeningSkills == null)
            return;

        for (BaseSkill listeningSkill : listeningSkills)
            if (listeningSkill.ownerPlayers.containsKey(player))
                listeningSkill.onEvent(event);
    }

    @EventHandler
    public void onItemSwap(PlayerSwapHandItemsEvent event) {
        List<BaseSkill> listeningSkills = baseSkills.get(event.getClass());


        if (listeningSkills == null)
            return;

        for (BaseSkill listeningSkill : listeningSkills) {
            if (listeningSkill.ownerPlayers.containsKey(event.getPlayer())) {
                if (event.getPlayer().isSneaking())
                    event.setCancelled(true);

                listeningSkill.onEvent(event);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        List<BaseSkill> listeningSkills = baseSkills.get(event.getClass());

        if (listeningSkills == null)
            return;

        for (BaseSkill listeningSkill : listeningSkills)
            if (listeningSkill.ownerPlayers.containsKey(event.getPlayer()))
                listeningSkill.onEvent(event);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        List<BaseSkill> listeningSkills = baseSkills.get(event.getClass());

        if (listeningSkills == null)
            return;

        for (BaseSkill listeningSkill : listeningSkills)
            if (listeningSkill.ownerPlayers.containsKey(event.getPlayer()))
                listeningSkill.onEvent(event);
    }

    @EventHandler
    public void onEat(PlayerItemConsumeEvent event) {
        List<BaseSkill> listeningSkills = baseSkills.get(event.getClass());

        if (listeningSkills == null)
            return;

        for (BaseSkill listeningSkill : listeningSkills)
            if (listeningSkill.ownerPlayers.containsKey(event.getPlayer()))
                listeningSkill.onEvent(event);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        List<BaseSkill> listeningSkills = baseSkills.get(event.getClass());

        if (listeningSkills == null)
            return;

        for (BaseSkill listeningSkill : listeningSkills)
            if (listeningSkill.ownerPlayers.containsKey(event.getPlayer()))
                listeningSkill.onEvent(event);
    }

    @EventHandler
    public void onBlockGrow(BlockGrowEvent event) {
        List<BaseSkill> listeningSkills = baseSkills.get(event.getClass());
        if (listeningSkills == null)
            return;

        for (BaseSkill listeningSkill : listeningSkills)
            listeningSkill.onEvent(event);
    }


    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Fireball fireball) {
            if (fireball.getEntitySpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM) {
                event.setDamage(3);
                return;
            }
        }


//        try {
//            if (event.getDamager().getPersistentDataContainer().has(ThirdStrike.extraDamage, PersistentDataType.INTEGER)) {
//                event.setDamage(event.getDamage() + event.getDamager().getPersistentDataContainer().get(ThirdStrike.extraDamage, PersistentDataType.INTEGER));
//            }
//        } catch (Exception ignored) {
//        }
//
//        try {
//            if (event.getDamager().getPersistentDataContainer().has(OFClasses.damageDecrease, PersistentDataType.INTEGER)) {
//                int decreasePercentage = event.getDamager().getPersistentDataContainer().get(OFClasses.damageDecrease, PersistentDataType.INTEGER);
//                event.setDamage(event.getDamage() - ((event.getDamage() / 100) * decreasePercentage));
//            }
//
//        } catch (Exception ignored) {
//        }

        if (!(event.getDamager() instanceof Player player))
            return;

        List<BaseSkill> listeningSkills = baseSkills.get(event.getClass());

        if (listeningSkills == null)
            return;

        for (BaseSkill listeningSkill : listeningSkills)
            if (listeningSkill.ownerPlayers.containsKey(player))
                listeningSkill.onEvent(event);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player))
            return;

        List<BaseSkill> listeningSkills = baseSkills.get(event.getClass());

        if (listeningSkills == null)
            return;

        for (BaseSkill listeningSkill : listeningSkills)
            if (listeningSkill.ownerPlayers.containsKey(player))
                listeningSkill.onEvent(event);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        List<BaseSkill> listeningSkills = baseSkills.get(event.getClass());

        if (listeningSkills == null)
            return;

        Player player = event.getEntity().getKiller();
        if (player == null)
            return;

        for (BaseSkill listeningSkill : listeningSkills)
            if (listeningSkill.ownerPlayers.containsKey(player))
                listeningSkill.onEvent(event);
    }

    @EventHandler
    public void onEntityDeath(CreatureSpawnEvent event) {
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL)
            return;

        List<BaseSkill> listeningSkills = baseSkills.get(event.getClass());

        if (listeningSkills == null)
            return;

        for (BaseSkill listeningSkill : listeningSkills)
            listeningSkill.onEvent(event);
    }

    @EventHandler
    public void onShift(PlayerToggleSneakEvent event) {
        List<BaseSkill> listeningSkills = baseSkills.get(event.getClass());

        if (listeningSkills == null)
            return;

        for (BaseSkill listeningSkill : listeningSkills)
            if (listeningSkill.ownerPlayers.containsKey(event.getPlayer()))
                listeningSkill.onEvent(event);
    }

    @EventHandler
    public void onToggleFly(PlayerToggleFlightEvent event) {
        List<BaseSkill> listeningSkills = baseSkills.get(event.getClass());

        if (listeningSkills == null)
            return;

        for (BaseSkill listeningSkill : listeningSkills)
            if (listeningSkill.ownerPlayers.containsKey(event.getPlayer()))
                listeningSkill.onEvent(event);
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        List<BaseSkill> listeningSkills = baseSkills.get(event.getClass());

        if (listeningSkills == null)
            return;

        for (BaseSkill listeningSkill : listeningSkills)
            if (listeningSkill.ownerPlayers.containsKey((Player) event.getView().getPlayer()))
                listeningSkill.onEvent(event);
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        Player player = (Player) event.getView().getPlayer();

//        try {
////            System.out.println(event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().getKeys());
//            if (event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().has(Baker.saturationValueKey, PersistentDataType.INTEGER)) {
//                if (!player.getPersistentDataContainer().get(OFClasses.playerClass, PersistentDataType.STRING).equals("farmer")) {
//                    OFClasses.sendMessage(player, "restricted-craft", "%class%", "farmer");
//                    event.setCancelled(true);
//                    return;
//                }
//
//                int index = event.getRecipe().getResult().getItemMeta().getPersistentDataContainer().get(Baker.customFoodID, PersistentDataType.INTEGER);
////                System.out.println(index);
//                BaseSkill skill = SkillLoader.getSkill("Baker");
//                int playerLevel = -1;
//                if (skill.ownerPlayers.containsKey(player))
//                    playerLevel = skill.getLevel(player);
//
//                if (index > playerLevel) {
//                    OFClasses.sendMessage(player, "restricted-craft", "%class%", "farmer");
//                    event.setCancelled(true);
//                    return;
//                }
//            }
//
//            ItemStack target = event.getCurrentItem();
//            if (target.getType().isEdible()) {
//                if (player.getPersistentDataContainer().has(OFClasses.extraFood, PersistentDataType.DOUBLE)) {
//                    int playerValue = (int) Math.round(player.getPersistentDataContainer().get(OFClasses.extraFood, PersistentDataType.DOUBLE));
//                    Material material = target.getType();
//
//                    Bukkit.getScheduler().scheduleSyncDelayedTask(OFClasses.instance, () -> {
//                        for (ItemStack itemStack : player.getInventory()) {
//                            if (itemStack != null)
//                                if (itemStack.getType() == material) {
//                                    ItemMeta itemMeta = itemStack.getItemMeta();
//                                    itemMeta.getPersistentDataContainer().set(OFClasses.extraFood, PersistentDataType.INTEGER, playerValue);
//                                    itemStack.setItemMeta(itemMeta);
//                                }
//                        }
//                        ItemStack itemStack = player.getItemOnCursor();
//                        if (itemStack != null)
//                            if (itemStack.getType() == material) {
//                                ItemMeta itemMeta = itemStack.getItemMeta();
//                                itemMeta.getPersistentDataContainer().set(OFClasses.extraFood, PersistentDataType.INTEGER, playerValue);
//                                itemStack.setItemMeta(itemMeta);
//                            }
//                    }, 2L);
////                    ItemMeta itemMeta = event.getCurrentItem().getItemMeta();
////                    itemMeta.getPersistentDataContainer().set(OFClasses.extraFood, PersistentDataType.INTEGER, playerValue);
////                    event.getCurrentItem().setItemMeta(itemMeta);
//                }
//            }

//            System.out.println(event.getCurrentItem());

        List<BaseSkill> listeningSkills = baseSkills.get(event.getClass());

        if (listeningSkills == null)
            return;

        for (BaseSkill listeningSkill : listeningSkills)
            if (listeningSkill.ownerPlayers.containsKey(player))
                listeningSkill.onEvent(event);
    }

    @EventHandler
    public void onTame(EntityTameEvent event) {
        List<BaseSkill> listeningSkills = baseSkills.get(event.getClass());

        if (listeningSkills == null)
            return;

        for (BaseSkill listeningSkill : listeningSkills)
            if (listeningSkill.ownerPlayers.containsKey((Player) event.getOwner()))
                listeningSkill.onEvent(event);
    }

    @EventHandler
    public void onBowShoot(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player player))
            return;


        List<BaseSkill> listeningSkills = baseSkills.get(event.getClass());

        if (listeningSkills == null)
            return;

        for (BaseSkill listeningSkill : listeningSkills)
            if (listeningSkill.ownerPlayers.containsKey(player))
                listeningSkill.onEvent(event);
    }

    @EventHandler
    public void slotChangeEvent(PlayerItemHeldEvent event) {
        List<BaseSkill> listeningSkills = baseSkills.get(event.getClass());

        if (listeningSkills == null)
            return;

        for (BaseSkill listeningSkill : listeningSkills)
            if (listeningSkill.ownerPlayers.containsKey(event.getPlayer()))
                listeningSkill.onEvent(event);
    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        List<BaseSkill> listeningSkills = baseSkills.get(event.getClass());

        if (listeningSkills == null)
            return;

        for (BaseSkill listeningSkill : listeningSkills)
            if (listeningSkill.ownerPlayers.containsKey(event.getPlayer()))
                listeningSkill.onEvent(event);
    }

    @EventHandler
    public void onPotionMakeStart(BrewingStartEvent event) {
        List<BaseSkill> listeningSkills = baseSkills.get(event.getClass());

        if (listeningSkills == null)
            return;

        for (BaseSkill listeningSkill : listeningSkills) {
            for (Map.Entry<Player, Integer> entry : listeningSkill.ownerPlayers.entrySet()) {
                Player player = entry.getKey();
                Inventory topInventory = player.getOpenInventory().getTopInventory();

                // Check if the top inventory is a brewing stand
//                System.out.println(topInventory.getHolder() instanceof BrewingStand);
                if (topInventory.getHolder() instanceof BrewingStand) {
                    listeningSkill.onEvent(event);
                    return;
                }
            }
        }
    }

    long lastMoveTrigger = 0;

//    @EventHandler
//    public void onMove(PlayerMoveEvent event) {
//        if (System.currentTimeMillis() - lastMoveTrigger < 0.15)
//            return;
//
////        boolean isStunned = false;
////
////        if (event.getPlayer().getPersistentDataContainer().has(SorcererBaseSkill.isStunned, PersistentDataType.LONG)) {
////            isStunned = true;
////            long date = event.getPlayer().getPersistentDataContainer().get(SorcererBaseSkill.isStunned, PersistentDataType.LONG);
////            if (date <= OFClasses.getCurrentSecond()) {
////                event.getPlayer().getPersistentDataContainer().remove(SorcererBaseSkill.isStunned);
////                isStunned = false;
////            }
////        }
////
////        if (isStunned) {
////            event.setCancelled(true);
////            return;
////        }
//
//        List<BaseSkill> listeningSkills = baseSkills.get(event.getClass());
//        lastMoveTrigger = System.currentTimeMillis();
//
//        if (listeningSkills == null)
//            return;
//
//        for (BaseSkill listeningSkill : listeningSkills)
//            if (listeningSkill.ownerPlayers.containsKey(event.getPlayer()))
//                listeningSkill.onEvent(event);
//    }


    @EventHandler
    public void onWaterChange(WeatherChangeEvent event) {
        List<BaseSkill> listeningSkills = baseSkills.get(event.getClass());

        if (listeningSkills == null)
            return;

        for (BaseSkill listeningSkill : listeningSkills) {
            listeningSkill.onEvent(event);
        }
    }

}
