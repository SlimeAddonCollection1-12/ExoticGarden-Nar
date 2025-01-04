/*    */ package me.mrCookieSlime.ExoticGarden;
/*    */ 
/*    */ import me.mrCookieSlime.CSCoreLibPlugin.events.ItemUseEvent;
/*    */ import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
/*    */ import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
/*    */ import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.Sound;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.EventPriority;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.block.BlockPlaceEvent;
/*    */ import org.bukkit.event.inventory.InventoryClickEvent;
/*    */ import org.bukkit.event.inventory.InventoryType;
/*    */ import org.bukkit.inventory.EquipmentSlot;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.bukkit.plugin.Plugin;
/*    */ 
/*    */ public class FoodListener implements Listener {
/*    */   ExoticGarden plugin;
/*    */   
/*    */   public FoodListener(ExoticGarden plugin) {
/* 24 */     this.plugin = plugin;
/* 25 */     plugin.getServer().getPluginManager().registerEvents(this, (Plugin)plugin);
/*    */   }
/*    */   @EventHandler(priority = EventPriority.HIGH)
/*    */   public void onUse(final ItemUseEvent e) {
/*    */     SlimefunItem item;
/* 30 */     if (e.getPlayer().getFoodLevel() >= 20)
/*    */       return; 
/* 32 */     EquipmentSlot hand = e.getParentEvent().getHand();
/*    */     
/* 34 */     switch (hand) {
/*    */       case HAND:
/* 36 */         item = SlimefunItem.getByItem((ItemStack)new CustomItem(e.getPlayer().getInventory().getItemInMainHand(), 1));
/* 37 */         if (item != null && 
/* 38 */           item instanceof EGPlant && (
/* 39 */           (EGPlant)item).isEdible()) {
/* 40 */           ((EGPlant)item).restoreHunger(e.getPlayer());
/* 41 */           e.getPlayer().getWorld().playSound(e.getPlayer().getEyeLocation(), Sound.ENTITY_GENERIC_EAT, 1.0F, 1.0F);
/* 42 */           Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)this.plugin, new Runnable()
/*    */               {
/*    */                 public void run()
/*    */                 {
/* 46 */                   e.getPlayer().getInventory().setItemInMainHand(InvUtils.decreaseItem(e.getPlayer().getInventory().getItemInMainHand(), 1));
/*    */                 }
/*    */               }0L);
/*    */         } 
/*    */         break;
/*    */ 
/*    */ 
/*    */       
/*    */       case OFF_HAND:
/* 55 */         item = SlimefunItem.getByItem((ItemStack)new CustomItem(e.getPlayer().getInventory().getItemInOffHand(), 1));
/* 56 */         if (item != null && 
/* 57 */           item instanceof EGPlant && (
/* 58 */           (EGPlant)item).isEdible()) {
/* 59 */           ((EGPlant)item).restoreHunger(e.getPlayer());
/* 60 */           e.getPlayer().getWorld().playSound(e.getPlayer().getEyeLocation(), Sound.ENTITY_GENERIC_EAT, 1.0F, 1.0F);
/* 61 */           Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)this.plugin, new Runnable()
/*    */               {
/*    */                 public void run()
/*    */                 {
/* 65 */                   e.getPlayer().getInventory().setItemInOffHand(InvUtils.decreaseItem(e.getPlayer().getInventory().getItemInOffHand(), 1));
/*    */                 }
/*    */               }0L);
/*    */         } 
/*    */         break;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
/*    */   public void onPlace(BlockPlaceEvent e) {
/* 80 */     SlimefunItem item = SlimefunItem.getByItem(e.getItemInHand());
/* 81 */     if (item != null && item instanceof EGPlant && e.getItemInHand().getType() == Material.SKULL_ITEM) e.setCancelled(true); 
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   public void onEquip(InventoryClickEvent e) {
/* 86 */     if (e.getSlotType() != InventoryType.SlotType.ARMOR)
/* 87 */       return;  SlimefunItem item = SlimefunItem.getByItem(e.getCursor());
/* 88 */     if (item != null && item instanceof EGPlant && e.getCursor().getType() == Material.SKULL_ITEM) e.setCancelled(true); 
/*    */   }
/*    */ }


/* Location:              C:\Users\hai'man\Desktop\粘液科技1.12\server\plugins\[粘液附属异域花园修复版]ExoticGarden-Nar.jar!\me\mrCookieSlime\ExoticGarden\FoodListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */