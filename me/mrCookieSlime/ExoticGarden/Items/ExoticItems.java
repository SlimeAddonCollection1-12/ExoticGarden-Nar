/*     */ package me.mrCookieSlime.ExoticGarden.Items;
/*     */ 
/*     */ import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
/*     */ import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
/*     */ import me.mrCookieSlime.ExoticGarden.CustomWine;
/*     */ import me.mrCookieSlime.ExoticGarden.ExoticGarden;
/*     */ import me.mrCookieSlime.ExoticGarden.machines.ElectricityBrewing;
/*     */ import me.mrCookieSlime.ExoticGarden.machines.SeedAnalyzer;
/*     */ import me.mrCookieSlime.ExoticGarden.machines.YeastCulturer;
/*     */ import me.mrCookieSlime.Slimefun.Lists.RecipeType;
/*     */ import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
/*     */ import me.mrCookieSlime.Slimefun.Objects.Category;
/*     */ import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ 
/*     */ public class ExoticItems {
/*  18 */   public static ItemStack MysticSeed = (ItemStack)new CustomItem(Material.MELON_SEEDS, "§d神秘种子", 0, new String[] { "", "§7从未见过的种子", "§7直接种植没有什么用", "§7但可以放到特定机器中进行分析" });
/*     */   
/*  20 */   public static ItemStack Wine_Waker = (ItemStack)new CustomItem(Material.POTION, "§3醒酒药", 0, new String[] { "", "§7喝多了?没关系!", "§7来一瓶竹影牌醒酒药!", "", "§7▷▷ §b酒精度: §e-30", "§7▷▷ §d精神值: §e-3", "§7▷▷ §a饱食度: §e1" });
/*     */   
/*     */   public static ItemStack SeedAnalyzer_Core;
/*     */   
/*     */   public static ItemStack SeedAnalyzer_1;
/*     */   public static ItemStack SeedAnalyzer_2;
/*     */   public static ItemStack SeedAnalyzer_3;
/*     */   public static ItemStack Yeast_1;
/*     */   public static ItemStack Yeast_2;
/*     */   public static ItemStack Yeast_3;
/*     */   public static ItemStack Yeast_4;
/*  31 */   public static ItemStack Alcohol = (ItemStack)new CustomItem(Material.POTION, "§b酒精", 0, new String[] { "", "§7一种具有挥发性的易燃液体", "§7也是一种不错的有机溶剂" });
/*     */   
/*     */   public static ItemStack YeastCulturer;
/*     */   public static ItemStack ElectricityBrewing_1;
/*     */   public static ItemStack ElectricityBrewing_2;
/*     */   public static ItemStack ElectricityBrewing_3;
/*  37 */   public static ItemStack GoldKeLa = (ItemStack)new CustomItem(new ItemStack(Material.INK_SACK, 1, (short)15), "§6金坷垃", new String[] { "", "§7用于给异域森林的植物催熟" });
/*     */   
/*     */   static {
/*     */     try {
/*  41 */       SeedAnalyzer_Core = (ItemStack)new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2MxZTJiODJkYjEwYWQyNWNhNDEzNDVlOTI0NWY1ODQ3ZTc2NzYwY2QyNDVjNDhlNWFmMWZkODk4NWVmOTE1In19fQ=="), "&b种子分析机&7-&e核心", new String[] { "", "&7用于制造种子分析机的核心组件" });
/*     */       
/*  43 */       SeedAnalyzer_1 = (ItemStack)new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTI1NmY3ZmY1MmU3YmZkODE4N2I4M2RkMzRkZjM0NTAyOTUyYjhkYjlmYWZiNzI4OGViZWJiNmU3OGVmMTVmIn19fQ=="), "&b种子分析机&7-&eI", new String[] { "", "&7用于分析未知的种子", "&7并将其培养为可种植的苗", " ", "§7▷▷ §b耗电: §e50J/s", "§7▷▷ §b缓存: §e1024J", "§7▷▷ §b分析速度: §e100s", "§7▷▷ §a种子成活率: &8低" });
/*     */       
/*  45 */       SeedAnalyzer_2 = (ItemStack)new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTI1NmY3ZmY1MmU3YmZkODE4N2I4M2RkMzRkZjM0NTAyOTUyYjhkYjlmYWZiNzI4OGViZWJiNmU3OGVmMTVmIn19fQ=="), "&b种子分析机&7-&eII", new String[] { "", "&7用于分析未知的种子", "&7并将其培养为可种植的苗", " ", "§7▷▷ §b耗电: §e50J/s", "§7▷▷ §b缓存: §e1024J", "§7▷▷ §b分析速度: §e60s", "§7▷▷ §a种子成活率: &a中" });
/*     */       
/*  47 */       SeedAnalyzer_3 = (ItemStack)new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTI1NmY3ZmY1MmU3YmZkODE4N2I4M2RkMzRkZjM0NTAyOTUyYjhkYjlmYWZiNzI4OGViZWJiNmU3OGVmMTVmIn19fQ=="), "&b种子分析机&7-&eIII", new String[] { "", "&7用于分析未知的种子", "&7并将其培养为可种植的苗", " ", "§7▷▷ §b耗电: §e50J/s", "§7▷▷ §b缓存: §e1024J", "§7▷▷ §b分析速度: §e20s", "§7▷▷ §a种子成活率: &6高" });
/*     */       
/*  49 */       Yeast_1 = (ItemStack)new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTMzODI2Mjc1N2Q2M2U0MmQ0NGE2ZjE1OTliMTJkODI3NzQwZDdmM2FiOWEyZTZkZGIxNjFmZGE4YzNlZWYifX19"), "&2初级酒曲", new String[] { "", "&7用于酿酒", "&7最简单易制的酒曲" });
/*     */       
/*  51 */       Yeast_2 = (ItemStack)new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjI5NjAzZDgyOTYzMDU2YmUxMzUyMmNmYjdkNDUyMGM3NmJhNjg3ZjM5NmEwZGFiMTI1ZTYzYjVkYWNlYTgifX19"), "&a中级酒曲", new String[] { "", "&7用于酿酒", "&7制作这种酒曲需要一定制作工艺" });
/*     */       
/*  53 */       Yeast_3 = (ItemStack)new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmViZjUxYTNhNTJiNzQ3MmEyODVjNjU4Mjg0Njg4YmNiZTU3Y2Q5ZjZmYWE3YTNlNGMyNmE2MTA1MjU0In19fQ=="), "&e高级酒曲", new String[] { "", "&7用于酿酒", "&7用于制作高级酒的酒曲" });
/*     */       
/*  55 */       Yeast_4 = (ItemStack)new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWRiOTEyYzFlZDQ2MDg4OTBhZTU5NGUxYmY3ZGQ3MDM0ODY2NzVjZjY0NzU0ZmY5MmVlM2U0YWQzYWRiYyJ9fX0="), "&d特级酒曲", new String[] { "", "&7用于酿酒", "&7极难制作出来的酒曲" });
/*     */       
/*  57 */       YeastCulturer = (ItemStack)new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2MxZTJiODJkYjEwYWQyNWNhNDEzNDVlOTI0NWY1ODQ3ZTc2NzYwY2QyNDVjNDhlNWFmMWZkODk4NWVmOTE1In19fQ=="), "&b酒曲培养机", new String[] { "", "&7用于制作酒曲", " ", "§7▷▷ §b耗电: §e8J/s", "§7▷▷ §b缓存: §e128J" });
/*     */       
/*  59 */       ElectricityBrewing_1 = (ItemStack)new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmZiOTg1OTYyZjQ2ZTA1NWY1M2Q4ZWUzNWIxMWI4YTYyZjM5N2RhZDlkYjlmZWFlZmY0ODI5NjMwZDlkOSJ9fX0="), "&b电力酿造机&7-&eI", new String[] { "", "&7用于制作美酒", " ", "§7▷▷ §b耗电: §e16J/s", "§7▷▷ §b缓存: §e256J" });
/*     */       
/*  61 */       ElectricityBrewing_2 = (ItemStack)new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmZiOTg1OTYyZjQ2ZTA1NWY1M2Q4ZWUzNWIxMWI4YTYyZjM5N2RhZDlkYjlmZWFlZmY0ODI5NjMwZDlkOSJ9fX0="), "&b电力酿造机&7-&eII", new String[] { "", "&7用于制作美酒", " ", "§7▷▷ §b耗电: §e24J/s", "§7▷▷ §b缓存: §e512J" });
/*     */       
/*  63 */       ElectricityBrewing_3 = (ItemStack)new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmZiOTg1OTYyZjQ2ZTA1NWY1M2Q4ZWUzNWIxMWI4YTYyZjM5N2RhZDlkYjlmZWFlZmY0ODI5NjMwZDlkOSJ9fX0="), "&b电力酿造机&7-&eIII", new String[] { "", "&7用于制作美酒", " ", "§7▷▷ §b耗电: §e32J/s", "§7▷▷ §b缓存: §e768J" });
/*     */     
/*     */     }
/*  66 */     catch (Exception e) {
/*  67 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void registerItems() {
/*  72 */     (new SlimefunItem(ExoticGarden.category_misc, GoldKeLa, "GOLDKELA", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*  73 */           SlimefunItem.getItem("FERTILIZER_WHEAT"), null, null, null, null, null, null, null, null }, (ItemStack)new CustomItem(GoldKeLa, 16))).register();
/*  74 */     (new SlimefunItem(ExoticGarden.category_misc, MysticSeed, "MYSTIC_SEED", new RecipeType((ItemStack)new CustomItem(Material.LONG_GRASS, "&7破坏杂草获得", 1)), new ItemStack[] { null, null, null, null, (ItemStack)new CustomItem(Material.LONG_GRASS, 1), null, null, null, null
/*  75 */         })).register();
/*  76 */     (new SlimefunItem(ExoticGarden.category_tech, SeedAnalyzer_Core, "SEED_ANALYZER_CORE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { SlimefunItems.COOLING_UNIT, SlimefunItems.SYNTHETIC_DIAMOND, SlimefunItems.HEATING_COIL, SlimefunItems.COOLING_UNIT, SlimefunItems.BIO_REACTOR, SlimefunItems.HEATING_COIL, SlimefunItems.COOLING_UNIT, SlimefunItems.BIG_CAPACITOR, SlimefunItems.HEATING_COIL
/*     */ 
/*     */         
/*  79 */         })).register();
/*  80 */     (new SlimefunItem(ExoticGarden.category_misc, Yeast_1, "YEAST_1", new RecipeType(YeastCulturer), new ItemStack[] { null, null, null, null, new ItemStack(Material.HAY_BLOCK), null, null, null, null
/*  81 */         })).register();
/*  82 */     (new SlimefunItem(ExoticGarden.category_misc, Yeast_2, "YEAST_2", new RecipeType(YeastCulturer), new ItemStack[] { null, null, null, null, 
/*  83 */           ExoticGarden.getItem("WINEFRUIT"), null, null, null, null })).register();
/*  84 */     (new SlimefunItem(ExoticGarden.category_misc, Yeast_3, "YEAST_3", new RecipeType(YeastCulturer), new ItemStack[] { null, null, null, null, 
/*  85 */           ExoticGarden.getItem("DREAMFRUIT"), null, null, null, null })).register();
/*  86 */     (new SlimefunItem(ExoticGarden.category_misc, Yeast_4, "YEAST_4", new RecipeType(YeastCulturer), new ItemStack[] { null, null, null, null, (ItemStack)new CustomItem(YeastCulturer, "&b酒曲培养机", new String[] { "", "&7酒曲培养机产出的副产物" }), null, null, null, null
/*  87 */         })).register();
/*     */     
/*  89 */     (new SeedAnalyzer(ExoticGarden.category_tech, SeedAnalyzer_1, "SEED_ANALYZER_1", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { SlimefunItems.HARDENED_GLASS, SlimefunItems.HARDENED_GLASS, SlimefunItems.HARDENED_GLASS, SlimefunItems.HARDENED_GLASS, SeedAnalyzer_Core, SlimefunItems.HARDENED_GLASS, SlimefunItems.HARDENED_GLASS, SlimefunItems.REINFORCED_PLATE, SlimefunItems.HARDENED_GLASS })
/*     */       {
/*     */ 
/*     */ 
/*     */         
/*     */         public String getInventoryTitle()
/*     */         {
/*  96 */           return "&b&l种子分析机&7-&eI";
/*     */         }
/*     */ 
/*     */         
/*     */         public int getEnergyConsumption() {
/* 101 */           return 50;
/*     */         }
/*     */ 
/*     */         
/*     */         public int getLevel() {
/* 106 */           return 1;
/*     */         }
/* 108 */       }).registerChargeableBlock(true, 1024);
/*     */     
/* 110 */     (new SeedAnalyzer(ExoticGarden.category_tech, SeedAnalyzer_2, "SEED_ANALYZER_2", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { SlimefunItems.HARDENED_GLASS, SlimefunItems.HEATING_COIL, SlimefunItems.HARDENED_GLASS, SlimefunItems.REACTOR_COOLANT_CELL, SeedAnalyzer_1, SlimefunItems.REACTOR_COOLANT_CELL, SlimefunItems.HARDENED_GLASS, SlimefunItems.HEATING_COIL, SlimefunItems.HARDENED_GLASS })
/*     */       {
/*     */ 
/*     */ 
/*     */         
/*     */         public String getInventoryTitle()
/*     */         {
/* 117 */           return "&b&l种子分析机&7-&eII";
/*     */         }
/*     */ 
/*     */         
/*     */         public int getEnergyConsumption() {
/* 122 */           return 50;
/*     */         }
/*     */ 
/*     */         
/*     */         public int getLevel() {
/* 127 */           return 2;
/*     */         }
/* 129 */       }).registerChargeableBlock(true, 1024);
/*     */     
/* 131 */     (new SeedAnalyzer(ExoticGarden.category_tech, SeedAnalyzer_3, "SEED_ANALYZER_3", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { SlimefunItems.WITHER_PROOF_GLASS, SlimefunItems.WITHER_PROOF_GLASS, SlimefunItems.WITHER_PROOF_GLASS, SlimefunItems.NETHER_ICE_COOLANT_CELL, SeedAnalyzer_2, SlimefunItems.NETHER_ICE_COOLANT_CELL, SlimefunItems.WITHER_PROOF_GLASS, SlimefunItems.WITHER_PROOF_GLASS, SlimefunItems.WITHER_PROOF_GLASS })
/*     */       {
/*     */ 
/*     */ 
/*     */         
/*     */         public String getInventoryTitle()
/*     */         {
/* 138 */           return "&b&l种子分析机&7-&eIII";
/*     */         }
/*     */ 
/*     */         
/*     */         public int getEnergyConsumption() {
/* 143 */           return 50;
/*     */         }
/*     */ 
/*     */         
/*     */         public int getLevel() {
/* 148 */           return 3;
/*     */         }
/* 150 */       }).registerChargeableBlock(true, 1024);
/*     */ 
/*     */     
/* 153 */     (new YeastCulturer(ExoticGarden.category_tech, YeastCulturer, "YEAST_CULTURER", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.LOG), SlimefunItems.ELECTRIC_MOTOR, new ItemStack(Material.LOG), SlimefunItems.HEATING_COIL, SlimefunItems.FOOD_FABRICATOR_2, SlimefunItems.HEATING_COIL, SlimefunItems.STONE_CHUNK, SlimefunItems.ADVANCED_CIRCUIT_BOARD, SlimefunItems.STONE_CHUNK })
/*     */       {
/*     */ 
/*     */ 
/*     */         
/*     */         public String getInventoryTitle()
/*     */         {
/* 160 */           return "&b酒曲培养机";
/*     */         }
/*     */ 
/*     */         
/*     */         public int getEnergyConsumption() {
/* 165 */           return 16;
/*     */         }
/*     */ 
/*     */         
/*     */         public int getLevel() {
/* 170 */           return 1;
/*     */         }
/* 172 */       }).registerChargeableBlock(true, 256);
/*     */     
/* 174 */     (new ElectricityBrewing(ExoticGarden.category_tech, ElectricityBrewing_1, "ELECTRICITY_BREWING_1", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.LOG), SlimefunItems.ELECTRIC_MOTOR, new ItemStack(Material.LOG), SlimefunItems.SYNTHETIC_EMERALD, YeastCulturer, SlimefunItems.SYNTHETIC_EMERALD, new ItemStack(Material.LOG), SlimefunItems.SYNTHETIC_EMERALD, new ItemStack(Material.LOG) })
/*     */       {
/*     */ 
/*     */         
/*     */         public String getInventoryTitle()
/*     */         {
/* 180 */           return "&b电力酿造机&7-&eI";
/*     */         }
/*     */ 
/*     */         
/*     */         public int getEnergyConsumption() {
/* 185 */           return 16;
/*     */         }
/*     */ 
/*     */         
/*     */         public int getLevel() {
/* 190 */           return 1;
/*     */         }
/* 192 */       }).registerChargeableBlock(true, 256);
/*     */     
/* 194 */     (new ElectricityBrewing(ExoticGarden.category_tech, ElectricityBrewing_2, "ELECTRICITY_BREWING_2", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.LOG), SlimefunItems.ELECTRIC_MOTOR, new ItemStack(Material.LOG), SlimefunItems.SYNTHETIC_DIAMOND, ElectricityBrewing_1, SlimefunItems.SYNTHETIC_DIAMOND, new ItemStack(Material.LOG), SlimefunItems.SYNTHETIC_DIAMOND, new ItemStack(Material.LOG) })
/*     */       {
/*     */ 
/*     */         
/*     */         public String getInventoryTitle()
/*     */         {
/* 200 */           return "&b电力酿造机&7-&eII";
/*     */         }
/*     */ 
/*     */         
/*     */         public int getEnergyConsumption() {
/* 205 */           return 24;
/*     */         }
/*     */ 
/*     */         
/*     */         public int getLevel() {
/* 210 */           return 2;
/*     */         }
/* 212 */       }).registerChargeableBlock(true, 512);
/*     */     
/* 214 */     (new ElectricityBrewing(ExoticGarden.category_tech, ElectricityBrewing_3, "ELECTRICITY_BREWING_3", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.LOG), SlimefunItems.ELECTRIC_MOTOR, new ItemStack(Material.LOG), SlimefunItems.CARBONADO, ElectricityBrewing_2, SlimefunItems.CARBONADO, new ItemStack(Material.LOG), SlimefunItems.CARBONADO, new ItemStack(Material.LOG) })
/*     */       {
/*     */ 
/*     */         
/*     */         public String getInventoryTitle()
/*     */         {
/* 220 */           return "&b电力酿造机&7-&eIII";
/*     */         }
/*     */ 
/*     */         
/*     */         public int getEnergyConsumption() {
/* 225 */           return 32;
/*     */         }
/*     */ 
/*     */         
/*     */         public int getLevel() {
/* 230 */           return 3;
/*     */         }
/* 232 */       }).registerChargeableBlock(true, 768);
/*     */     
/*     */     try {
/* 235 */       (new SlimefunItem(ExoticGarden.category_misc, Alcohol, "Alcohol", new RecipeType((ItemStack)new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmZiOTg1OTYyZjQ2ZTA1NWY1M2Q4ZWUzNWIxMWI4YTYyZjM5N2RhZDlkYjlmZWFlZmY0ODI5NjMwZDlkOSJ9fX0="), "&b电力酿造机", new String[] { "", "&7用于制作美酒" })), new ItemStack[] { null, null, null, null, (ItemStack)new CustomItem(new ItemStack(Material.GHAST_TEAR), "&7酿造副产品"), null, null, null, null
/* 236 */           })).register();
/* 237 */     } catch (Exception e) {
/* 238 */       e.printStackTrace();
/*     */     } 
/* 240 */     (new CustomWine(ExoticGarden.category_misc, Wine_Waker, "WINE_WAKER", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { null, new ItemStack(Material.BROWN_MUSHROOM), null, new ItemStack(Material.LONG_GRASS, 1, (short)2), Alcohol, new ItemStack(Material.RED_ROSE, 1, (short)1), null, new ItemStack(Material.EGG), null }1, -3.0F, -30))
/*     */ 
/*     */       
/* 243 */       .register(true);
/*     */   }
/*     */ }


/* Location:              C:\Users\hai'man\Desktop\粘液科技1.12\server\plugins\[粘液附属异域花园修复版]ExoticGarden-Nar.jar!\me\mrCookieSlime\ExoticGarden\Items\ExoticItems.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */