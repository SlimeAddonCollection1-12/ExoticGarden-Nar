/*     */ package me.mrCookieSlime.ExoticGarden.gui;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
/*     */ import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
/*     */ import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
/*     */ import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
/*     */ import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
/*     */ import me.mrCookieSlime.ExoticGarden.recipe.DefaultMachineRecipe;
/*     */ import me.mrCookieSlime.ExoticGarden.recipe.DefaultSubRecipe;
/*     */ import me.mrCookieSlime.Slimefun.Lists.RecipeType;
/*     */ import me.mrCookieSlime.Slimefun.Misc.compatibles.ProtectionUtils;
/*     */ import me.mrCookieSlime.Slimefun.Objects.Category;
/*     */ import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
/*     */ import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
/*     */ import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
/*     */ import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineHelper;
/*     */ import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.BlockTicker;
/*     */ import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.ItemHandler;
/*     */ import me.mrCookieSlime.Slimefun.api.BlockStorage;
/*     */ import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
/*     */ import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
/*     */ import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
/*     */ import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.inventory.InventoryClickEvent;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ import org.bukkit.material.MaterialData;
/*     */ 
/*     */ public abstract class DefaultGUI extends SlimefunItem {
/*  38 */   public static Map<Block, DefaultMachineRecipe> processing = new HashMap<>();
/*  39 */   public static Map<Block, Integer> progress = new HashMap<>();
/*  40 */   protected List<DefaultMachineRecipe> recipes = new ArrayList<>();
/*  41 */   private static final int[] border = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53 };
/*  42 */   private static final int[] inputBorder = new int[] { 10, 11, 12, 14, 15, 16 };
/*  43 */   private static final int[] centerBorder = new int[] { 19, 20, 21, 22, 23, 24, 25 };
/*  44 */   private static final int[] outputBorder = new int[] { 30, 32, 39, 40, 41 };
/*  45 */   private static final int[] subSlotSign = new int[] { 28, 29 };
/*  46 */   private static final int[] mainSlotSign = new int[] { 33, 34 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultGUI(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
/*  53 */     super(category, item, name, recipeType, recipe);
/*     */     
/*  55 */     new BlockMenuPreset(name, getInventoryTitle())
/*     */       {
/*     */         
/*     */         public void init()
/*     */         {
/*  60 */           DefaultGUI.this.constructMenu(this);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void newInstance(BlockMenu menu, Block b) {}
/*     */ 
/*     */         
/*     */         public boolean canOpen(Block b, Player p) {
/*  69 */           boolean perm = (p.hasPermission("slimefun.inventory.bypass") || CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), b, true));
/*  70 */           return (perm && ProtectionUtils.canAccessItem(p, b));
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
/*  76 */           if (flow.equals(ItemTransportFlow.INSERT)) {
/*  77 */             return DefaultGUI.this.getInputSlots();
/*     */           }
/*  79 */           return DefaultGUI.this.getOutputMainSlots();
/*     */         }
/*     */       };
/*  82 */     registerBlockHandler(name, new SlimefunBlockHandler()
/*     */         {
/*     */           public void onPlace(Player p, Block b, SlimefunItem item) {}
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
/*  90 */             BlockMenu inv = BlockStorage.getInventory(b);
/*  91 */             if (inv != null) {
/*     */               
/*  93 */               for (int slot : DefaultGUI.this.getInputSlots()) {
/*  94 */                 if (inv.getItemInSlot(slot) != null) {
/*  95 */                   b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
/*     */                 }
/*     */               } 
/*  98 */               for (int slot : DefaultGUI.this.getOutputMainSlots()) {
/*  99 */                 if (inv.getItemInSlot(slot) != null) {
/* 100 */                   b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
/*     */                 }
/*     */               } 
/* 103 */               for (int slot : DefaultGUI.this.getOutputSubSlots()) {
/* 104 */                 if (inv.getItemInSlot(slot) != null) {
/* 105 */                   b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
/*     */                 }
/*     */               } 
/*     */             } 
/* 109 */             DefaultGUI.progress.remove(b);
/* 110 */             DefaultGUI.processing.remove(b);
/* 111 */             return true;
/*     */           }
/*     */         });
/* 114 */     registerDefaultRecipes();
/*     */   }
/*     */   
/*     */   public DefaultGUI(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
/* 118 */     super(category, item, name, recipeType, recipe, recipeOutput);
/*     */     
/* 120 */     new BlockMenuPreset(name, getInventoryTitle())
/*     */       {
/*     */         
/*     */         public void init()
/*     */         {
/* 125 */           DefaultGUI.this.constructMenu(this);
/*     */         }
/*     */ 
/*     */         
/*     */         public void newInstance(BlockMenu menu, Block b) {}
/*     */ 
/*     */         
/*     */         public boolean canOpen(Block b, Player p) {
/* 133 */           boolean perm = (p.hasPermission("slimefun.inventory.bypass") || CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), b, true));
/* 134 */           return (perm && ProtectionUtils.canAccessItem(p, b));
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
/* 140 */           if (flow.equals(ItemTransportFlow.INSERT)) {
/* 141 */             return DefaultGUI.this.getInputSlots();
/*     */           }
/* 143 */           return DefaultGUI.this.getOutputMainSlots();
/*     */         }
/*     */       };
/* 146 */     registerBlockHandler(name, new SlimefunBlockHandler()
/*     */         {
/*     */           public void onPlace(Player p, Block b, SlimefunItem item) {}
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
/* 154 */             for (int slot : DefaultGUI.this.getInputSlots()) {
/* 155 */               if (BlockStorage.getInventory(b).getItemInSlot(slot) != null) {
/* 156 */                 b.getWorld().dropItemNaturally(b.getLocation(), BlockStorage.getInventory(b).getItemInSlot(slot));
/*     */               }
/*     */             } 
/* 159 */             for (int slot : DefaultGUI.this.getOutputMainSlots()) {
/* 160 */               if (BlockStorage.getInventory(b).getItemInSlot(slot) != null) {
/* 161 */                 b.getWorld().dropItemNaturally(b.getLocation(), BlockStorage.getInventory(b).getItemInSlot(slot));
/*     */               }
/*     */             } 
/* 164 */             for (int slot : DefaultGUI.this.getOutputSubSlots()) {
/* 165 */               if (BlockStorage.getInventory(b).getItemInSlot(slot) != null) {
/* 166 */                 b.getWorld().dropItemNaturally(b.getLocation(), BlockStorage.getInventory(b).getItemInSlot(slot));
/*     */               }
/*     */             } 
/* 169 */             DefaultGUI.processing.remove(b);
/* 170 */             DefaultGUI.progress.remove(b);
/* 171 */             return true;
/*     */           }
/*     */         });
/* 174 */     registerDefaultRecipes();
/*     */   }
/*     */   
/*     */   private void constructMenu(BlockMenuPreset preset) {
/* 178 */     for (int i : border) {
/* 179 */       preset.addItem(i, (ItemStack)new CustomItem(new MaterialData(Material.STAINED_GLASS_PANE, (byte)3), " ", new String[0]), new ChestMenu.MenuClickHandler()
/*     */           {
/*     */             public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction)
/*     */             {
/* 183 */               return false;
/*     */             }
/*     */           });
/*     */     } 
/* 187 */     for (int i : inputBorder) {
/* 188 */       preset.addItem(i, (ItemStack)new CustomItem(new MaterialData(Material.STAINED_GLASS_PANE, (byte)0), " ", new String[0]), new ChestMenu.MenuClickHandler()
/*     */           {
/*     */             public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction)
/*     */             {
/* 192 */               return false;
/*     */             }
/*     */           });
/*     */     } 
/* 196 */     for (int i : centerBorder) {
/* 197 */       preset.addItem(i, (ItemStack)new CustomItem(new MaterialData(Material.STAINED_GLASS_PANE, (byte)4), " ", new String[0]), new ChestMenu.MenuClickHandler()
/*     */           {
/*     */             public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction)
/*     */             {
/* 201 */               return false;
/*     */             }
/*     */           });
/*     */     } 
/* 205 */     for (int i : outputBorder) {
/* 206 */       preset.addItem(i, (ItemStack)new CustomItem(new MaterialData(Material.STAINED_GLASS_PANE, (byte)9), " ", new String[0]), new ChestMenu.MenuClickHandler()
/*     */           {
/*     */             public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction)
/*     */             {
/* 210 */               return false;
/*     */             }
/*     */           });
/*     */     } 
/* 214 */     for (int i : subSlotSign) {
/* 215 */       preset.addItem(i, (ItemStack)new CustomItem(new MaterialData(Material.STAINED_GLASS_PANE, (byte)5), "&e副输出槽", new String[] { "", "&7副输出槽通常会输出机器的副产物", "&7有些副产物极其有用甚至非常珍贵" }), new ChestMenu.MenuClickHandler()
/*     */           {
/*     */             public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction)
/*     */             {
/* 219 */               return false;
/*     */             }
/*     */           });
/*     */     } 
/* 223 */     for (int i : mainSlotSign) {
/* 224 */       preset.addItem(i, (ItemStack)new CustomItem(new MaterialData(Material.STAINED_GLASS_PANE, (byte)5), "&c主输出槽", new String[] { "", "&7主输出槽输出机器的常规产品" }), new ChestMenu.MenuClickHandler()
/*     */           {
/*     */             public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction)
/*     */             {
/* 228 */               return false;
/*     */             }
/*     */           });
/*     */     } 
/* 232 */     preset.addItem(31, (ItemStack)new CustomItem(new MaterialData(Material.STAINED_GLASS_PANE, (byte)15), " ", new String[0]), new ChestMenu.MenuClickHandler()
/*     */         {
/*     */           public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction)
/*     */           {
/* 236 */             return false;
/*     */           }
/*     */         });
/*     */     
/* 240 */     preset.addItem(38, null, (ChestMenu.MenuClickHandler)new ChestMenu.AdvancedMenuClickHandler()
/*     */         {
/*     */           public boolean onClick(Player player, int i, ItemStack item, ClickAction action) {
/* 243 */             return false;
/*     */           }
/*     */ 
/*     */           
/*     */           public boolean onClick(InventoryClickEvent event, Player player, int slot, ItemStack item, ClickAction action) {
/* 248 */             return (item == null || item.getType() == null || item.getType() == Material.AIR);
/*     */           }
/*     */         });
/*     */   }
/*     */   public int[] getInputSlots() {
/* 253 */     return new int[] { 13 };
/*     */   }
/*     */   public int[] getOutputSubSlots() {
/* 256 */     return new int[] { 37, 38 };
/*     */   }
/*     */   public int[] getOutputMainSlots() {
/* 259 */     return new int[] { 42, 43 };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultMachineRecipe getProcessing(Block b) {
/* 278 */     return processing.get(b);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isProcessing(Block b) {
/* 283 */     return (getProcessing(b) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerRecipe(DefaultMachineRecipe recipe) {
/* 288 */     recipe.setTicks(recipe.getTicks());
/* 289 */     this.recipes.add(recipe);
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerRecipe(int seconds, ItemStack[] input, ItemStack[] output) {
/* 294 */     registerRecipe(new DefaultMachineRecipe(seconds, input, output));
/*     */   }
/*     */ 
/*     */   
/*     */   private Inventory inject(Block b) {
/* 299 */     int size = BlockStorage.getInventory(b).toInventory().getSize();
/* 300 */     Inventory inv = Bukkit.createInventory(null, size);
/* 301 */     for (int i = 0; i < size; i++) {
/* 302 */       inv.setItem(i, (ItemStack)new CustomItem(Material.COMMAND, " &4ALL YOUR PLACEHOLDERS ARE BELONG TO US", 0));
/*     */     }
/* 304 */     for (int slot : getOutputMainSlots()) {
/* 305 */       inv.setItem(slot, BlockStorage.getInventory(b).getItemInSlot(slot));
/*     */     }
/* 307 */     return inv;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean fits(Block b, ItemStack[] items) {
/* 312 */     return inject(b).addItem(items).isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void pushMainItems(Block b, ItemStack[] items) {
/* 317 */     Inventory inv = inject(b);
/* 318 */     inv.addItem(items);
/* 319 */     for (int slot : getOutputMainSlots()) {
/* 320 */       BlockStorage.getInventory(b).replaceExistingItem(slot, inv.getItem(slot));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private Inventory injectSub(Block b) {
/* 326 */     int size = BlockStorage.getInventory(b).toInventory().getSize();
/* 327 */     Inventory inv = Bukkit.createInventory(null, size);
/* 328 */     for (int i = 0; i < size; i++) {
/* 329 */       inv.setItem(i, (ItemStack)new CustomItem(Material.COMMAND, " &4ALL YOUR PLACEHOLDERS ARE BELONG TO US", 0));
/*     */     }
/* 331 */     for (int slot : getOutputSubSlots()) {
/* 332 */       inv.setItem(slot, BlockStorage.getInventory(b).getItemInSlot(slot));
/*     */     }
/* 334 */     return inv;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void pushSubItems(Block b, DefaultSubRecipe recipe) {
/* 339 */     if (recipe != null && willOutput(recipe) && fits(b, new ItemStack[] { recipe.getItem() })) {
/* 340 */       Inventory inv = injectSub(b);
/* 341 */       inv.addItem(new ItemStack[] { recipe.getItem() });
/* 342 */       for (int slot : getOutputSubSlots()) {
/* 343 */         BlockStorage.getInventory(b).replaceExistingItem(slot, inv.getItem(slot));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected DefaultSubRecipe selectSubItem(List<DefaultSubRecipe> subRecipes) {
/* 349 */     int random = (int)(Math.random() * subRecipes.size());
/* 350 */     return subRecipes.get(random);
/*     */   }
/*     */   
/*     */   private boolean willOutput(DefaultSubRecipe recipe) {
/* 354 */     Random random = new Random();
/* 355 */     int point = random.nextInt(10000);
/* 356 */     return (point < recipe.getChance());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void register(boolean slimefun) {
/* 362 */     addItemHandler(new ItemHandler[] { (ItemHandler)new BlockTicker()
/*     */           {
/*     */             
/*     */             public void tick(Block b, SlimefunItem sf, Config data)
/*     */             {
/* 367 */               DefaultGUI.this.tick(b);
/*     */             }
/*     */ 
/*     */ 
/*     */             
/*     */             public void uniqueTick() {}
/*     */ 
/*     */             
/*     */             public boolean isSynchronized() {
/* 376 */               return false;
/*     */             }
/*     */           } });
/* 379 */     super.register(slimefun);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void tick(Block b) {
/* 385 */     if (isProcessing(b)) {
/*     */       
/* 387 */       int timeleft = ((Integer)progress.get(b)).intValue();
/* 388 */       if (timeleft > 0) {
/*     */         
/* 390 */         ItemStack item = getProgressBar().clone();
/* 391 */         item.setDurability(MachineHelper.getDurability(item, timeleft, ((DefaultMachineRecipe)processing.get(b)).getTicks()));
/* 392 */         ItemMeta im = item.getItemMeta();
/* 393 */         im.setDisplayName(" ");
/* 394 */         List<String> lore = new ArrayList<>();
/* 395 */         lore.add(MachineHelper.getProgress(timeleft, ((DefaultMachineRecipe)processing.get(b)).getTicks()));
/* 396 */         lore.add("");
/* 397 */         lore.add(MachineHelper.getTimeLeft(timeleft / 2));
/* 398 */         im.setLore(lore);
/* 399 */         item.setItemMeta(im);
/*     */         
/* 401 */         BlockStorage.getInventory(b).replaceExistingItem(31, item);
/* 402 */         if (ChargableBlock.isChargable(b))
/*     */         {
/* 404 */           if (ChargableBlock.getCharge(b) < getEnergyConsumption()) {
/*     */             return;
/*     */           }
/* 407 */           ChargableBlock.addCharge(b, -getEnergyConsumption());
/* 408 */           progress.put(b, Integer.valueOf(timeleft - 1));
/*     */         }
/*     */         else
/*     */         {
/* 412 */           progress.put(b, Integer.valueOf(timeleft - 1));
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 417 */         BlockStorage.getInventory(b).replaceExistingItem(31, (ItemStack)new CustomItem(new MaterialData(Material.STAINED_GLASS_PANE, (byte)15), " ", new String[0]));
/* 418 */         pushMainItems(b, ((DefaultMachineRecipe)processing.get(b)).getOutput());
/* 419 */         pushSubItems(b, selectSubItem(getSubRecipes()));
/* 420 */         progress.remove(b);
/* 421 */         processing.remove(b);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 426 */       DefaultMachineRecipe r = null;
/* 427 */       Map<Integer, Integer> found = new HashMap<>();
/* 428 */       for (DefaultMachineRecipe recipe : this.recipes) {
/*     */         
/* 430 */         for (ItemStack input : recipe.getInput()) {
/* 431 */           for (int slot : getInputSlots()) {
/* 432 */             if (SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(slot), input, true))
/*     */             {
/* 434 */               if (input != null) {
/* 435 */                 found.put(Integer.valueOf(slot), Integer.valueOf(input.getAmount()));
/*     */               }
/*     */             }
/*     */           } 
/*     */         } 
/*     */         
/* 441 */         if (found.size() == (recipe.getInput()).length) {
/*     */           
/* 443 */           r = recipe;
/*     */           break;
/*     */         } 
/* 446 */         found.clear();
/*     */       } 
/* 448 */       if (r != null) {
/*     */         
/* 450 */         if (!fits(b, r.getOutput())) {
/*     */           return;
/*     */         }
/* 453 */         for (Map.Entry<Integer, Integer> entry : found.entrySet()) {
/* 454 */           BlockStorage.getInventory(b).replaceExistingItem(((Integer)entry.getKey()).intValue(), InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(((Integer)entry.getKey()).intValue()), ((Integer)entry.getValue()).intValue()));
/*     */         }
/* 456 */         processing.put(b, r);
/* 457 */         progress.put(b, Integer.valueOf(r.getTicks()));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public abstract String getInventoryTitle();
/*     */   
/*     */   public abstract ItemStack getProgressBar();
/*     */   
/*     */   public abstract List<DefaultSubRecipe> getSubRecipes();
/*     */   
/*     */   public abstract void registerDefaultRecipes();
/*     */   
/*     */   public abstract int getEnergyConsumption();
/*     */   
/*     */   public abstract int getLevel();
/*     */   
/*     */   public abstract String getMachineIdentifier();
/*     */ }


/* Location:              C:\Users\hai'man\Desktop\粘液科技1.12\server\plugins\[粘液附属异域花园修复版]ExoticGarden-Nar.jar!\me\mrCookieSlime\ExoticGarden\gui\DefaultGUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */