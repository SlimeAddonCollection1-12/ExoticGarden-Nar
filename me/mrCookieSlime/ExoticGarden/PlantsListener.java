/*     */ package me.mrCookieSlime.ExoticGarden;
/*     */ import com.bekvon.bukkit.residence.protection.ClaimedResidence;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
/*     */ import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
/*     */ import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
/*     */ import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
/*     */ import me.mrCookieSlime.Slimefun.api.BlockStorage;
/*     */ import org.bukkit.Effect;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.SkullType;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.BlockFace;
/*     */ import org.bukkit.block.Skull;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.block.Action;
/*     */ import org.bukkit.event.block.BlockBreakEvent;
/*     */ import org.bukkit.event.block.BlockDispenseEvent;
/*     */ import org.bukkit.event.block.BlockExplodeEvent;
/*     */ import org.bukkit.event.block.LeavesDecayEvent;
/*     */ import org.bukkit.event.entity.EntityExplodeEvent;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
/*     */ import org.bukkit.event.world.ChunkPopulateEvent;
/*     */ import org.bukkit.event.world.StructureGrowEvent;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ 
/*     */ public class PlantsListener implements Listener {
/*  36 */   BlockFace[] bf = new BlockFace[] { BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST }; Config cfg;
/*     */   
/*     */   public PlantsListener(ExoticGarden plugin) {
/*  39 */     this.cfg = plugin.cfg;
/*  40 */     plugin.getServer().getPluginManager().registerEvents(this, (Plugin)plugin);
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onDispence(BlockDispenseEvent event) {
/*  45 */     if (event.getItem() != null && 
/*  46 */       event.getItem().isSimilar(new ItemStack(Material.INK_SACK, 1, (short)15))) {
/*  47 */       if (event.getItem().hasItemMeta() && 
/*  48 */         event.getItem().getItemMeta().hasLore()) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/*  53 */       event.setCancelled(true);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void onGrow(StructureGrowEvent e) {
/*  61 */     SlimefunItem item = BlockStorage.check(e.getLocation().getBlock());
/*  62 */     if (item != null) {
/*  63 */       e.setCancelled(true);
/*  64 */       if (!e.getLocation().getChunk().isLoaded()) e.getLocation().getWorld().loadChunk(e.getLocation().getChunk()); 
/*  65 */       for (Tree tree : ExoticGarden.trees) {
/*  66 */         if (item.getName().equalsIgnoreCase(tree.getSapling())) {
/*     */           
/*  68 */           if (canGrow(e))
/*  69 */             return;  BlockStorage.retrieve(e.getLocation().getBlock());
/*  70 */           Schematic.pasteSchematic(e.getLocation(), tree);
/*     */           break;
/*     */         } 
/*     */       } 
/*  74 */       for (Berry berry : ExoticGarden.berries) {
/*  75 */         if (item.getName().equalsIgnoreCase(berry.toBush())) {
/*     */           Skull s;
/*  77 */           if (canGrow(e))
/*  78 */             return;  BlockStorage.store(e.getLocation().getBlock(), berry.getItem());
/*  79 */           switch (berry.getType()) {
/*     */             case BUSH:
/*  81 */               e.getLocation().getBlock().setType(Material.LEAVES);
/*  82 */               e.getLocation().getBlock().setData(berry.getData().toByte());
/*     */               break;
/*     */             
/*     */             case ORE_PLANT:
/*     */             case DOUBLE_PLANT:
/*  87 */               BlockStorage.store(e.getLocation().getBlock().getRelative(BlockFace.UP), berry.getItem());
/*  88 */               e.getLocation().getBlock().setType(Material.LEAVES);
/*  89 */               e.getLocation().getBlock().setData((byte)4);
/*  90 */               e.getLocation().getBlock().getRelative(BlockFace.UP).setType(Material.SKULL);
/*  91 */               s = (Skull)e.getLocation().getBlock().getRelative(BlockFace.UP).getState();
/*  92 */               s.setSkullType(SkullType.PLAYER);
/*  93 */               s.setRotation(this.bf[(new Random()).nextInt(this.bf.length)]);
/*  94 */               s.setRawData((byte)1);
/*  95 */               s.update();
/*     */               
/*     */               try {
/*  98 */                 CustomSkull.setSkull(e.getLocation().getBlock().getRelative(BlockFace.UP), berry.getData().getTexture());
/*  99 */               } catch (Exception e1) {
/* 100 */                 e1.printStackTrace();
/*     */               } 
/*     */               break;
/*     */             
/*     */             default:
/* 105 */               e.getLocation().getBlock().setType(Material.SKULL);
/* 106 */               s = (Skull)e.getLocation().getBlock().getState();
/* 107 */               s.setSkullType(SkullType.PLAYER);
/* 108 */               s.setRotation(this.bf[(new Random()).nextInt(this.bf.length)]);
/* 109 */               s.setRawData((byte)1);
/* 110 */               s.update();
/*     */               
/*     */               try {
/* 113 */                 CustomSkull.setSkull(e.getLocation().getBlock(), berry.getData().getTexture());
/* 114 */               } catch (Exception e1) {
/* 115 */                 e1.printStackTrace();
/*     */               } 
/*     */               break;
/*     */           } 
/*     */           
/* 120 */           e.getWorld().playEffect(e.getLocation(), Effect.STEP_SOUND, Material.LEAVES);
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void onGenerate(ChunkPopulateEvent e) {
/* 130 */     if (!this.cfg.getStringList("world-blacklist").contains(e.getWorld().getName())) {
/* 131 */       if (CSCoreLib.randomizer().nextInt(100) < this.cfg.getInt("chances.TREE")) {
/* 132 */         Berry berry = ExoticGarden.berries.get(CSCoreLib.randomizer().nextInt(ExoticGarden.berries.size()));
/* 133 */         if (berry.getType().equals(PlantType.ORE_PLANT))
/*     */           return; 
/* 135 */         int x = e.getChunk().getX() * 16 + CSCoreLib.randomizer().nextInt(16);
/* 136 */         int z = e.getChunk().getZ() * 16 + CSCoreLib.randomizer().nextInt(16);
/* 137 */         for (int y = e.getWorld().getMaxHeight(); y > 30; y--) {
/* 138 */           Block current = e.getWorld().getBlockAt(x, y, z);
/* 139 */           if (!current.getType().isSolid() && current.getType() != Material.STATIONARY_WATER && berry.isSoil(current.getRelative(BlockFace.DOWN).getType())) {
/* 140 */             Skull s; BlockStorage.store(current, berry.getItem());
/* 141 */             switch (berry.getType()) {
/*     */               case BUSH:
/* 143 */                 current.setType(Material.LEAVES);
/* 144 */                 current.setData(berry.getData().toByte());
/*     */                 break;
/*     */               
/*     */               case FRUIT:
/* 148 */                 current.setType(Material.SKULL);
/* 149 */                 s = (Skull)current.getState();
/* 150 */                 s.setSkullType(SkullType.PLAYER);
/* 151 */                 s.setRotation(this.bf[(new Random()).nextInt(this.bf.length)]);
/* 152 */                 s.setRawData((byte)1);
/* 153 */                 s.update();
/*     */                 
/*     */                 try {
/* 156 */                   CustomSkull.setSkull(current, berry.getData().getTexture());
/* 157 */                 } catch (Exception e1) {
/* 158 */                   e1.printStackTrace();
/*     */                 } 
/*     */                 break;
/*     */               
/*     */               case ORE_PLANT:
/*     */               case DOUBLE_PLANT:
/* 164 */                 BlockStorage.store(current.getRelative(BlockFace.UP), berry.getItem());
/* 165 */                 current.setType(Material.LEAVES);
/* 166 */                 current.setData((byte)4);
/* 167 */                 current.getRelative(BlockFace.UP).setType(Material.SKULL);
/* 168 */                 s = (Skull)current.getRelative(BlockFace.UP).getState();
/* 169 */                 s.setSkullType(SkullType.PLAYER);
/* 170 */                 s.setRotation(this.bf[(new Random()).nextInt(this.bf.length)]);
/* 171 */                 s.setRawData((byte)1);
/* 172 */                 s.update();
/*     */                 
/*     */                 try {
/* 175 */                   CustomSkull.setSkull(current.getRelative(BlockFace.UP), berry.getData().getTexture());
/* 176 */                 } catch (Exception e1) {
/* 177 */                   e1.printStackTrace();
/*     */                 } 
/*     */                 break;
/*     */             } 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/* 188 */       } else if (CSCoreLib.randomizer().nextInt(100) < this.cfg.getInt("chances.BUSH")) {
/* 189 */         Tree tree = ExoticGarden.trees.get(CSCoreLib.randomizer().nextInt(ExoticGarden.trees.size()));
/*     */         
/* 191 */         int x = e.getChunk().getX() * 16 + CSCoreLib.randomizer().nextInt(16);
/* 192 */         int z = e.getChunk().getZ() * 16 + CSCoreLib.randomizer().nextInt(16);
/* 193 */         boolean flat = false;
/* 194 */         for (int y = e.getWorld().getMaxHeight(); y > 30; y--) {
/* 195 */           Block current = e.getWorld().getBlockAt(x, y, z);
/* 196 */           if (!current.getType().isSolid() && current.getType() != Material.STATIONARY_WATER && tree.isSoil(current.getRelative(0, -1, 0).getType())) {
/* 197 */             flat = true;
/* 198 */             for (int i = 0; i < 5; i++) {
/* 199 */               for (int j = 0; j < 5; j++) {
/* 200 */                 for (int k = 0; k < 6; k++) {
/* 201 */                   if (current.getRelative(i, k, j).getType().isSolid() || current.getRelative(i, k, j).getType().toString().contains("LEAVES") || !current.getRelative(i, -1, j).getType().isSolid()) flat = false; 
/*     */                 } 
/*     */               } 
/*     */             } 
/* 205 */             if (flat) {
/* 206 */               Schematic.pasteSchematic(new Location(e.getWorld(), x, y, z), tree);
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
/*     */   public void onHarvest(BlockBreakEvent e) {
/* 217 */     if (e.getBlock().getType().equals(Material.SKULL)) dropFruitFromTree(e.getBlock()); 
/* 218 */     if (e.getBlock().getType().equals(Material.LEAVES) || e.getBlock().getType().equals(Material.LEAVES_2)) dropFruitFromTree(e.getBlock()); 
/* 219 */     if (e.getBlock().getType() == Material.LONG_GRASS) {
/* 220 */       if (CSCoreLib.randomizer().nextInt(100) < 6) e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), ExoticGarden.items.get(((String[])ExoticGarden.items.keySet().toArray((T[])new String[ExoticGarden.items.keySet().size()]))[CSCoreLib.randomizer().nextInt(ExoticGarden.items.keySet().size())]));
/*     */     
/*     */     } else {
/* 223 */       ItemStack item = ExoticGarden.harvestPlant(e.getBlock());
/* 224 */       if (item != null) {
/* 225 */         e.setCancelled(true);
/* 226 */         e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), item);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
/*     */   public void onDecay(LeavesDecayEvent e) {
/* 233 */     dropFruitFromTree(e.getBlock());
/* 234 */     ItemStack item = BlockStorage.retrieve(e.getBlock());
/* 235 */     if (item != null) {
/* 236 */       e.setCancelled(true);
/* 237 */       e.getBlock().setType(Material.AIR);
/* 238 */       e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), item);
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onInteract(PlayerInteractEvent e) {
/* 244 */     if (e.getAction() == Action.RIGHT_CLICK_BLOCK && 
/* 245 */       e.getItem() != null && e.getItem().getType() != Material.AIR && 
/* 246 */       e.getItem().hasItemMeta() && 
/* 247 */       e.getItem().getItemMeta().hasDisplayName() && 
/* 248 */       e.getItem().getItemMeta().getDisplayName().equals("§3镰刀")) {
/* 249 */       e.setCancelled(true);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 255 */     if (e.getAction() != Action.RIGHT_CLICK_BLOCK)
/* 256 */       return;  if (ExoticGarden.instance.isResidenceEnabled()) {
/* 257 */       ClaimedResidence res = Residence.getInstance().getResidenceManager().getByLoc(e.getClickedBlock().getLocation());
/* 258 */       if (res != null && 
/* 259 */         !res.getPermissions().playerHas(e.getPlayer().getName(), e.getPlayer().getWorld().getName(), "exo-harvest", true) && !res.getPermissions().has("exo-harvest", true) && !e.getPlayer().hasPermission("residence.bypass.use") && 
/* 260 */         ExoticGarden.isPlant(e.getClickedBlock())) {
/* 261 */         e.getPlayer().sendMessage("§8[§a异域森林§8] §c你需要这个领地的§eexo-harvest§c标识§8(flag)§c才能这么做");
/* 262 */         e.setCancelled(true);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     
/* 268 */     ItemStack item = ExoticGarden.harvestPlant(e.getClickedBlock());
/* 269 */     if (item != null) {
/* 270 */       e.getClickedBlock().getWorld().playEffect(e.getClickedBlock().getLocation(), Effect.STEP_SOUND, Material.LEAVES);
/* 271 */       e.getClickedBlock().getWorld().dropItemNaturally(e.getClickedBlock().getLocation(), item);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
/*     */   void onBlockExplode(BlockExplodeEvent e) {
/* 278 */     e.blockList().removeAll(explosionHandler(e.blockList()));
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
/*     */   void onEntityExplode(EntityExplodeEvent e) {
/* 284 */     e.blockList().removeAll(explosionHandler(e.blockList()));
/*     */   }
/*     */ 
/*     */   
/*     */   Set<Block> explosionHandler(List<Block> blockList) {
/* 289 */     Set<Block> blocksToRemove = new HashSet<>();
/* 290 */     for (Block block : blockList) {
/*     */       
/* 292 */       ItemStack item = ExoticGarden.harvestPlant(block);
/* 293 */       if (item != null) {
/* 294 */         blocksToRemove.add(block);
/* 295 */         block.getWorld().dropItemNaturally(block.getLocation(), item);
/*     */       } 
/*     */     } 
/* 298 */     return blocksToRemove;
/*     */   }
/*     */   
/*     */   public void dropFruitFromTree(Block block) {
/* 302 */     for (int x = -1; x < 2; x++) {
/* 303 */       for (int y = -1; y < 2; y++) {
/* 304 */         for (int z = -1; z < 2; z++) {
/* 305 */           Block drop = block.getRelative(x, y, z);
/* 306 */           SlimefunItem check = BlockStorage.check(drop);
/* 307 */           if (check != null) {
/* 308 */             for (Tree tree : ExoticGarden.trees) {
/* 309 */               if (check.getName().equalsIgnoreCase(tree.fruit)) {
/* 310 */                 BlockStorage.clearBlockInfo(drop);
/* 311 */                 ItemStack fruits = check.getItem();
/* 312 */                 drop.getWorld().playEffect(drop.getLocation(), Effect.STEP_SOUND, Material.LEAVES);
/* 313 */                 drop.getWorld().dropItemNaturally(drop.getLocation(), fruits);
/* 314 */                 drop.setType(Material.AIR);
/*     */               } 
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean canGrow(StructureGrowEvent e) {
/* 324 */     if (checkGrow(e.isFromBonemeal(), e.getPlayer())) {
/* 325 */       e.setCancelled(true);
/* 326 */       if (e.getPlayer() != null) {
/* 327 */         e.getPlayer().sendMessage("§8[§a异域森林§8] §c你需要使用§6金坷拉§c才能催熟这个植物");
/*     */       }
/* 329 */       return true;
/*     */     } 
/* 331 */     return false;
/*     */   }
/*     */   
/*     */   private boolean checkGrow(boolean isBoneMeal, Player player) {
/* 335 */     boolean cancel = true;
/* 336 */     if (isBoneMeal) {
/* 337 */       if (player != null) {
/* 338 */         ItemStack boneMeal = player.getInventory().getItemInMainHand();
/* 339 */         if (boneMeal != null && 
/* 340 */           boneMeal.hasItemMeta() && 
/* 341 */           boneMeal.getItemMeta().hasLore())
/*     */         {
/* 343 */           cancel = false;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 348 */       return cancel;
/*     */     } 
/* 350 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\hai'man\Desktop\粘液科技1.12\server\plugins\[粘液附属异域花园修复版]ExoticGarden-Nar.jar!\me\mrCookieSlime\ExoticGarden\PlantsListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */