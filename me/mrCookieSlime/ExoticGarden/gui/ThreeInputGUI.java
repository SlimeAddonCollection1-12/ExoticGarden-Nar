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
/*     */ public abstract class ThreeInputGUI extends SlimefunItem {
/*  38 */   public static Map<Block, DefaultMachineRecipe> processing = new HashMap<>();
/*  39 */   public static Map<Block, Integer> progress = new HashMap<>();
/*  40 */   protected List<DefaultMachineRecipe> recipes = new ArrayList<>();
/*  41 */   private static final int[] border = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53 };
/*  42 */   private static final int[] inputBorder = new int[] { 10, 12, 14, 16 };
/*  43 */   private static final int[] centerBorder = new int[] { 19, 20, 21, 22, 23, 24, 25 };
/*  44 */   private static final int[] outputBorder = new int[] { 30, 32, 39, 40, 41 };
/*  45 */   private static final int[] subSlotSign = new int[] { 28, 29 };
/*  46 */   private static final int[] mainSlotSign = new int[] { 33, 34 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThreeInputGUI(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
/*  53 */     super(category, item, name, recipeType, recipe);
/*     */     
/*  55 */     new BlockMenuPreset(name, getInventoryTitle())
/*     */       {
/*     */         
/*     */         public void init()
/*     */         {
/*  60 */           ThreeInputGUI.this.constructMenu(this);
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
/*  77 */             return ThreeInputGUI.this.getInputSlots();
/*     */           }
/*  79 */           return ThreeInputGUI.this.getOutputMainSlots();
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
/*  93 */               for (int slot : ThreeInputGUI.this.getInputSlots()) {
/*  94 */                 if (inv.getItemInSlot(slot) != null) {
/*  95 */                   b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
/*     */                 }
/*     */               } 
/*  98 */               for (int slot : ThreeInputGUI.this.getOutputMainSlots()) {
/*  99 */                 if (inv.getItemInSlot(slot) != null) {
/* 100 */                   b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
/*     */                 }
/*     */               } 
/* 103 */               for (int slot : ThreeInputGUI.this.getOutputSubSlots()) {
/* 104 */                 if (inv.getItemInSlot(slot) != null) {
/* 105 */                   b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
/*     */                 }
/*     */               } 
/*     */             } 
/* 109 */             ThreeInputGUI.progress.remove(b);
/* 110 */             ThreeInputGUI.processing.remove(b);
/* 111 */             return true;
/*     */           }
/*     */         });
/* 114 */     registerDefaultRecipes();
/*     */   }
/*     */ 
/*     */   
/*     */   public ThreeInputGUI(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
/* 119 */     super(category, item, name, recipeType, recipe, recipeOutput);
/*     */     
/* 121 */     new BlockMenuPreset(name, getInventoryTitle())
/*     */       {
/*     */         
/*     */         public void init()
/*     */         {
/* 126 */           ThreeInputGUI.this.constructMenu(this);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void newInstance(BlockMenu menu, Block b) {}
/*     */ 
/*     */         
/*     */         public boolean canOpen(Block b, Player p) {
/* 135 */           boolean perm = (p.hasPermission("slimefun.inventory.bypass") || CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), b, true));
/* 136 */           return (perm && ProtectionUtils.canAccessItem(p, b));
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
/* 142 */           if (flow.equals(ItemTransportFlow.INSERT)) {
/* 143 */             return ThreeInputGUI.this.getInputSlots();
/*     */           }
/* 145 */           return ThreeInputGUI.this.getOutputMainSlots();
/*     */         }
/*     */       };
/* 148 */     registerBlockHandler(name, new SlimefunBlockHandler()
/*     */         {
/*     */           public void onPlace(Player p, Block b, SlimefunItem item) {}
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
/* 156 */             for (int slot : ThreeInputGUI.this.getInputSlots()) {
/* 157 */               if (BlockStorage.getInventory(b).getItemInSlot(slot) != null) {
/* 158 */                 b.getWorld().dropItemNaturally(b.getLocation(), BlockStorage.getInventory(b).getItemInSlot(slot));
/*     */               }
/*     */             } 
/* 161 */             for (int slot : ThreeInputGUI.this.getOutputMainSlots()) {
/* 162 */               if (BlockStorage.getInventory(b).getItemInSlot(slot) != null) {
/* 163 */                 b.getWorld().dropItemNaturally(b.getLocation(), BlockStorage.getInventory(b).getItemInSlot(slot));
/*     */               }
/*     */             } 
/* 166 */             for (int slot : ThreeInputGUI.this.getOutputSubSlots()) {
/* 167 */               if (BlockStorage.getInventory(b).getItemInSlot(slot) != null) {
/* 168 */                 b.getWorld().dropItemNaturally(b.getLocation(), BlockStorage.getInventory(b).getItemInSlot(slot));
/*     */               }
/*     */             } 
/* 171 */             ThreeInputGUI.processing.remove(b);
/* 172 */             ThreeInputGUI.progress.remove(b);
/* 173 */             return true;
/*     */           }
/*     */         });
/* 176 */     registerDefaultRecipes();
/*     */   }
/*     */   
/*     */   private void constructMenu(BlockMenuPreset preset) {
/* 180 */     for (int i : border) {
/* 181 */       preset.addItem(i, (ItemStack)new CustomItem(new MaterialData(Material.STAINED_GLASS_PANE, (byte)3), " ", new String[0]), new ChestMenu.MenuClickHandler()
/*     */           {
/*     */             public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction)
/*     */             {
/* 185 */               return false;
/*     */             }
/*     */           });
/*     */     } 
/* 189 */     for (int i : inputBorder) {
/* 190 */       preset.addItem(i, (ItemStack)new CustomItem(new MaterialData(Material.STAINED_GLASS_PANE, (byte)0), " ", new String[0]), new ChestMenu.MenuClickHandler()
/*     */           {
/*     */             public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction)
/*     */             {
/* 194 */               return false;
/*     */             }
/*     */           });
/*     */     } 
/* 198 */     for (int i : centerBorder) {
/* 199 */       preset.addItem(i, (ItemStack)new CustomItem(new MaterialData(Material.STAINED_GLASS_PANE, (byte)4), " ", new String[0]), new ChestMenu.MenuClickHandler()
/*     */           {
/*     */             public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction)
/*     */             {
/* 203 */               return false;
/*     */             }
/*     */           });
/*     */     } 
/* 207 */     for (int i : outputBorder) {
/* 208 */       preset.addItem(i, (ItemStack)new CustomItem(new MaterialData(Material.STAINED_GLASS_PANE, (byte)9), " ", new String[0]), new ChestMenu.MenuClickHandler()
/*     */           {
/*     */             public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction)
/*     */             {
/* 212 */               return false;
/*     */             }
/*     */           });
/*     */     } 
/* 216 */     for (int i : subSlotSign) {
/* 217 */       preset.addItem(i, (ItemStack)new CustomItem(new MaterialData(Material.STAINED_GLASS_PANE, (byte)5), "&e副输出槽", new String[] { "", "&7副输出槽通常会输出机器的副产物", "&7有些副产物极其有用甚至非常珍贵" }), new ChestMenu.MenuClickHandler()
/*     */           {
/*     */             public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction)
/*     */             {
/* 221 */               return false;
/*     */             }
/*     */           });
/*     */     } 
/* 225 */     for (int i : mainSlotSign) {
/* 226 */       preset.addItem(i, (ItemStack)new CustomItem(new MaterialData(Material.STAINED_GLASS_PANE, (byte)5), "&c主输出槽", new String[] { "", "&7主输出槽输出机器的常规产品" }), new ChestMenu.MenuClickHandler()
/*     */           {
/*     */             public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction)
/*     */             {
/* 230 */               return false;
/*     */             }
/*     */           });
/*     */     } 
/* 234 */     preset.addItem(31, (ItemStack)new CustomItem(new MaterialData(Material.STAINED_GLASS_PANE, (byte)15), " ", new String[0]), new ChestMenu.MenuClickHandler()
/*     */         {
/*     */           public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction)
/*     */           {
/* 238 */             return false;
/*     */           }
/*     */         });
/*     */     
/* 242 */     preset.addItem(38, null, (ChestMenu.MenuClickHandler)new ChestMenu.AdvancedMenuClickHandler()
/*     */         {
/*     */           public boolean onClick(Player player, int i, ItemStack item, ClickAction action) {
/* 245 */             return false;
/*     */           }
/*     */ 
/*     */           
/*     */           public boolean onClick(InventoryClickEvent event, Player player, int slot, ItemStack item, ClickAction action) {
/* 250 */             return (item == null || item.getType() == null || item.getType() == Material.AIR);
/*     */           }
/*     */         });
/*     */   }
/*     */   public int[] getInputSlots() {
/* 255 */     return new int[] { 11, 13, 15 };
/*     */   }
/*     */   public int[] getOutputSubSlots() {
/* 258 */     return new int[] { 37, 38 };
/*     */   }
/*     */   public int[] getOutputMainSlots() {
/* 261 */     return new int[] { 42, 43 };
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
/* 280 */     return processing.get(b);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isProcessing(Block b) {
/* 285 */     return (getProcessing(b) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerRecipe(DefaultMachineRecipe recipe) {
/* 290 */     recipe.setTicks(recipe.getTicks());
/* 291 */     this.recipes.add(recipe);
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerRecipe(int seconds, ItemStack[] input, ItemStack[] output) {
/* 296 */     registerRecipe(new DefaultMachineRecipe(seconds, input, output));
/*     */   }
/*     */ 
/*     */   
/*     */   private Inventory inject(Block b) {
/* 301 */     int size = BlockStorage.getInventory(b).toInventory().getSize();
/* 302 */     Inventory inv = Bukkit.createInventory(null, size);
/* 303 */     for (int i = 0; i < size; i++) {
/* 304 */       inv.setItem(i, (ItemStack)new CustomItem(Material.COMMAND, " &4ALL YOUR PLACEHOLDERS ARE BELONG TO US", 0));
/*     */     }
/* 306 */     for (int slot : getOutputMainSlots()) {
/* 307 */       inv.setItem(slot, BlockStorage.getInventory(b).getItemInSlot(slot));
/*     */     }
/* 309 */     return inv;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean fits(Block b, ItemStack[] items) {
/* 314 */     return inject(b).addItem(items).isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void pushMainItems(Block b, ItemStack[] items) {
/* 319 */     Inventory inv = inject(b);
/* 320 */     inv.addItem(items);
/* 321 */     for (int slot : getOutputMainSlots()) {
/* 322 */       BlockStorage.getInventory(b).replaceExistingItem(slot, inv.getItem(slot));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private Inventory injectSub(Block b) {
/* 328 */     int size = BlockStorage.getInventory(b).toInventory().getSize();
/* 329 */     Inventory inv = Bukkit.createInventory(null, size);
/* 330 */     for (int i = 0; i < size; i++) {
/* 331 */       inv.setItem(i, (ItemStack)new CustomItem(Material.COMMAND, " &4ALL YOUR PLACEHOLDERS ARE BELONG TO US", 0));
/*     */     }
/* 333 */     for (int slot : getOutputSubSlots()) {
/* 334 */       inv.setItem(slot, BlockStorage.getInventory(b).getItemInSlot(slot));
/*     */     }
/* 336 */     return inv;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void pushSubItems(Block b, DefaultSubRecipe recipe) {
/* 341 */     if (recipe != null && willOutput(recipe) && fits(b, new ItemStack[] { recipe.getItem() })) {
/* 342 */       Inventory inv = injectSub(b);
/* 343 */       inv.addItem(new ItemStack[] { recipe.getItem() });
/* 344 */       for (int slot : getOutputSubSlots()) {
/* 345 */         BlockStorage.getInventory(b).replaceExistingItem(slot, inv.getItem(slot));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected DefaultSubRecipe selectSubItem(List<DefaultSubRecipe> subRecipes) {
/* 351 */     int random = (int)(Math.random() * subRecipes.size());
/* 352 */     return subRecipes.get(random);
/*     */   }
/*     */   
/*     */   private boolean willOutput(DefaultSubRecipe recipe) {
/* 356 */     Random random = new Random();
/* 357 */     int point = random.nextInt(10000);
/* 358 */     return (point < recipe.getChance());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void register(boolean slimefun) {
/* 364 */     addItemHandler(new ItemHandler[] { (ItemHandler)new BlockTicker()
/*     */           {
/*     */             
/*     */             public void tick(Block b, SlimefunItem sf, Config data)
/*     */             {
/* 369 */               ThreeInputGUI.this.tick(b);
/*     */             }
/*     */ 
/*     */ 
/*     */             
/*     */             public void uniqueTick() {}
/*     */ 
/*     */             
/*     */             public boolean isSynchronized() {
/* 378 */               return false;
/*     */             }
/*     */           } });
/* 381 */     super.register(slimefun);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void tick(Block b) {
/* 387 */     if (isProcessing(b)) {
/*     */       
/* 389 */       int timeleft = ((Integer)progress.get(b)).intValue();
/* 390 */       if (timeleft > 0) {
/*     */         
/* 392 */         ItemStack item = getProgressBar().clone();
/* 393 */         item.setDurability(MachineHelper.getDurability(item, timeleft, ((DefaultMachineRecipe)processing.get(b)).getTicks()));
/* 394 */         ItemMeta im = item.getItemMeta();
/* 395 */         im.setDisplayName(" ");
/* 396 */         List<String> lore = new ArrayList<>();
/* 397 */         lore.add(MachineHelper.getProgress(timeleft, ((DefaultMachineRecipe)processing.get(b)).getTicks()));
/* 398 */         lore.add("");
/* 399 */         lore.add(MachineHelper.getTimeLeft(timeleft / 2));
/* 400 */         im.setLore(lore);
/* 401 */         item.setItemMeta(im);
/*     */         
/* 403 */         BlockStorage.getInventory(b).replaceExistingItem(31, item);
/* 404 */         if (ChargableBlock.isChargable(b))
/*     */         {
/* 406 */           if (ChargableBlock.getCharge(b) < getEnergyConsumption()) {
/*     */             return;
/*     */           }
/* 409 */           ChargableBlock.addCharge(b, -getEnergyConsumption());
/* 410 */           progress.put(b, Integer.valueOf(timeleft - 1));
/*     */         }
/*     */         else
/*     */         {
/* 414 */           progress.put(b, Integer.valueOf(timeleft - 1));
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 419 */         BlockStorage.getInventory(b).replaceExistingItem(31, (ItemStack)new CustomItem(new MaterialData(Material.STAINED_GLASS_PANE, (byte)15), " ", new String[0]));
/* 420 */         pushMainItems(b, ((DefaultMachineRecipe)processing.get(b)).getOutput());
/* 421 */         pushSubItems(b, selectSubItem(getSubRecipes()));
/* 422 */         progress.remove(b);
/* 423 */         processing.remove(b);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 428 */       DefaultMachineRecipe r = null;
/* 429 */       Map<Integer, Integer> found = new HashMap<>();
/* 430 */       for (DefaultMachineRecipe recipe : this.recipes) {
/*     */         
/* 432 */         for (ItemStack input : recipe.getInput()) {
/* 433 */           for (int slot : getInputSlots()) {
/* 434 */             if (SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(slot), input, true))
/*     */             {
/* 436 */               if (input != null) {
/* 437 */                 found.put(Integer.valueOf(slot), Integer.valueOf(input.getAmount()));
/*     */               }
/*     */             }
/*     */           } 
/*     */         } 
/*     */         
/* 443 */         if (found.size() == (recipe.getInput()).length) {
/*     */           
/* 445 */           r = recipe;
/*     */           break;
/*     */         } 
/* 448 */         found.clear();
/*     */       } 
/* 450 */       if (r != null) {
/*     */         
/* 452 */         if (!fits(b, r.getOutput())) {
/*     */           return;
/*     */         }
/* 455 */         for (Map.Entry<Integer, Integer> entry : found.entrySet()) {
/* 456 */           BlockStorage.getInventory(b).replaceExistingItem(((Integer)entry.getKey()).intValue(), InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(((Integer)entry.getKey()).intValue()), ((Integer)entry.getValue()).intValue()));
/*     */         }
/* 458 */         processing.put(b, r);
/* 459 */         progress.put(b, Integer.valueOf(r.getTicks()));
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


/* Location:              C:\Users\hai'man\Desktop\粘液科技1.12\server\plugins\[粘液附属异域花园修复版]ExoticGarden-Nar.jar!\me\mrCookieSlime\ExoticGarden\gui\ThreeInputGUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */