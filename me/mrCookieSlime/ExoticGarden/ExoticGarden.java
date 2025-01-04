/*      */ package me.mrCookieSlime.ExoticGarden;
/*      */ import java.io.File;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.jar.JarFile;
/*      */ import java.util.zip.ZipEntry;
/*      */ import me.mrCookieSlime.CSCoreLibPlugin.PluginUtils;
/*      */ import me.mrCookieSlime.CSCoreLibPlugin.events.ItemUseEvent;
/*      */ import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
/*      */ import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomPotion;
/*      */ import me.mrCookieSlime.CSCoreLibPlugin.general.Player.PlayerInventory;
/*      */ import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
/*      */ import me.mrCookieSlime.ExoticGarden.CSCoreLibSetup.CSCoreLibLoader;
/*      */ import me.mrCookieSlime.ExoticGarden.Items.ExoticItems;
/*      */ import me.mrCookieSlime.Slimefun.Lists.Categories;
/*      */ import me.mrCookieSlime.Slimefun.Lists.RecipeType;
/*      */ import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
/*      */ import me.mrCookieSlime.Slimefun.Objects.Category;
/*      */ import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.HandledBlock;
/*      */ import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
/*      */ import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.ItemHandler;
/*      */ import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
/*      */ import me.mrCookieSlime.Slimefun.api.BlockStorage;
/*      */ import org.bukkit.Bukkit;
/*      */ import org.bukkit.Effect;
/*      */ import org.bukkit.Material;
/*      */ import org.bukkit.block.Block;
/*      */ import org.bukkit.block.BlockFace;
/*      */ import org.bukkit.configuration.ConfigurationSection;
/*      */ import org.bukkit.configuration.file.YamlConfiguration;
/*      */ import org.bukkit.entity.Player;
/*      */ import org.bukkit.event.block.BlockBreakEvent;
/*      */ import org.bukkit.inventory.ItemStack;
/*      */ import org.bukkit.material.MaterialData;
/*      */ import org.bukkit.plugin.Plugin;
/*      */ import org.bukkit.potion.PotionEffect;
/*      */ import org.bukkit.potion.PotionEffectType;
/*      */ import org.bukkit.scheduler.BukkitRunnable;
/*      */ 
/*      */ public class ExoticGarden extends JavaPlugin {
/*   50 */   static List<Berry> berries = new ArrayList<>();
/*   51 */   static List<Tree> trees = new ArrayList<>();
/*   52 */   static Map<String, ItemStack> items = new HashMap<>(); public static Category category_main; public static Category category_food; public static Category category_drinks;
/*      */   public static Category category_magic;
/*      */   public static Category category_tech;
/*      */   public static Category category_misc;
/*      */   Config cfg;
/*      */   private static boolean skullitems;
/*      */   static ExoticGarden instance;
/*   59 */   public static ConcurrentHashMap<String, PlayerAlcohol> drunkPlayers = new ConcurrentHashMap<>();
/*   60 */   private static List<String> drunkMsg = new ArrayList<>();
/*   61 */   private YamlConfiguration yamlStorge = null;
/*      */   
/*      */   private boolean sanity = false;
/*      */   
/*      */   private boolean residence = false;
/*   66 */   private HashMap<String, String> traslateNames = new HashMap<>();
/*      */   
/*      */   private static final String ALCOHOL_PATH = "Players.%p.Alcohol";
/*      */   
/*      */   private static final String DRUNK_PATH = "Players.%p.Drunk";
/*      */ 
/*      */   
/*      */   public void onEnable() {
/*   74 */     CSCoreLibLoader loader = new CSCoreLibLoader((Plugin)this);
/*      */     
/*   76 */     if (Bukkit.getServer().getPluginManager().isPluginEnabled("Sanity")) {
/*   77 */       this.sanity = true;
/*      */     }
/*   79 */     if (getServer().getPluginManager().getPlugin("Residence") != null) {
/*   80 */       FlagPermissions.addFlag("exo-harvest");
/*   81 */       this.residence = true;
/*      */     } 
/*      */     
/*   84 */     getCommand("exotic").setExecutor((CommandExecutor)new ExoticCommand());
/*      */     
/*   86 */     if (loader.load()) {
/*   87 */       if (!(new File("plugins/ExoticGarden")).exists()) (new File("plugins/ExoticGarden")).mkdirs(); 
/*   88 */       PluginUtils utils = new PluginUtils((Plugin)this);
/*   89 */       utils.setupConfig();
/*   90 */       this.cfg = utils.getConfig();
/*   91 */       utils.setupMetrics();
/*   92 */       utils.setupUpdater(88425, getFile());
/*      */       
/*   94 */       File storgeFile = new File(getDataFolder() + File.separator + "storge.yml");
/*   95 */       createDefaultConfiguration(storgeFile, "storge.yml");
/*   96 */       initDataFromYAML(storgeFile);
/*      */       
/*   98 */       registerDrunkMessage();
/*      */       
/*  100 */       skullitems = this.cfg.getBoolean("options.item-heads");
/*      */       
/*  102 */       category_main = new Category((ItemStack)new CustomItem(getSkull(Material.NETHER_STALK, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWIxODcwNGFhMGM5MmFiN2YzMDY1YjNlMzg0OTZkZDFjZDQyZjkzYjExODNhM2ZiNGYyNmI0ZThkOTk5YWUifX19"), "&a异域森林 &8- &e植物与水果", new String[] { "", "&a> 点击打开" }));
/*      */       
/*  104 */       category_food = new Category((ItemStack)new CustomItem(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTE0MjE2ZDEwNzE0MDgyYmJlM2Y0MTI0MjNlNmIxOTIzMjM1MmY0ZDY0ZjlhY2EzOTEzY2I0NjMxOGQzZWQifX19"), "&a异域森林 &8- &e美食", new String[] { "", "&a> 点击打开" }));
/*      */       
/*  106 */       category_drinks = new Category((ItemStack)new CustomItem(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmE4ZjFmNzBlODU4MjU2MDdkMjhlZGNlMWEyYWQ0NTA2ZTczMmI0YTUzNDVhNWVhNmU4MDdjNGIzMTNlODgifX19"), "&a异域森林 &8- &e饮料", new String[] { "", "§7请注意适度饮酒", "§c未成年人禁止饮酒!", "&a> 点击打开" }));
/*      */       
/*  108 */       category_magic = new Category((ItemStack)new CustomItem(getSkull(Material.NETHER_STALK, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTVhNWM0YTBhMTZkYWJjOWIxZWM3MmZjODNlMjNhYzE1ZDAxOTdkZTYxYjEzOGJhYmNhN2M4YTI5YzgyMCJ9fX0="), "&a异域森林 &8- &d异化植物", new String[] { "", "&7各种神奇的植物", "&7无法合成, 只能通过特殊方式获得", "", "&a> 点击打开" }));
/*      */       
/*  110 */       category_tech = new Category((ItemStack)new CustomItem(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTI1NmY3ZmY1MmU3YmZkODE4N2I4M2RkMzRkZjM0NTAyOTUyYjhkYjlmYWZiNzI4OGViZWJiNmU3OGVmMTVmIn19fQ=="), "&a异域森林 &8- &b科技", new String[] { "", "&a> 点击打开" }));
/*      */       
/*  112 */       category_misc = new Category((ItemStack)new CustomItem(getSkull(Material.INK_SACK, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjZmY2ViYzVjZDM0N2UzMDNlNGUzYTU5NDhkMGIyNDdiMGRiY2M0MTBhYmFmODlkMzY3OTM4ZmYxMTM0Njk1In19fQ=="), "&a异域森林 &8- &3杂项", new String[] { "", "&a> 点击打开" }));
/*      */ 
/*      */       
/*  115 */       (new SlimefunItem(Categories.MISC, (ItemStack)new CustomItem(getSkull(Material.ICE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTM0MGJlZjJjMmMzM2QxMTNiYWM0ZTZhMWE4NGQ1ZmZjZWNiYmZhYjZiMzJmYTdhN2Y3NjE5NTQ0MmJkMWEyIn19fQ=="), "&b冰块"), "ICE_CUBE", RecipeType.GRIND_STONE, new ItemStack[] { new ItemStack(Material.ICE), null, null, null, null, null, null, null, null }, (ItemStack)new CustomItem((ItemStack)new CustomItem(
/*      */               
/*  117 */               getSkull(Material.ICE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTM0MGJlZjJjMmMzM2QxMTNiYWM0ZTZhMWE4NGQ1ZmZjZWNiYmZhYjZiMzJmYTdhN2Y3NjE5NTQ0MmJkMWEyIn19fQ=="), "&b冰块"), 4)))
/*      */         
/*  119 */         .register();
/*      */       
/*  121 */       initTransNames();
/*      */       
/*  123 */       registerBerry("葡萄", "&c", 8201, PlantType.BUSH, new PlantData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmVlOTc2NDliZDk5OTk1NTQxM2ZjYmYwYjI2OWM5MWJlNDM0MmIxMGQwNzU1YmFkN2ExN2U5NWZjZWZkYWIwIn19fQ=="));
/*  124 */       registerBerry("蓝莓", "&9", 8205, PlantType.BUSH, new PlantData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWIxODcwNGFhMGM5MmFiN2YzMDY1YjNlMzg0OTZkZDFjZDQyZjkzYjExODNhM2ZiNGYyNmI0ZThkOTk5YWUifX19"));
/*  125 */       registerBerry("接骨木果", "&c", 8193, PlantType.BUSH, new PlantData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWU0ODgzYTFlMjJjMzI0ZTc1MzE1MWUyYWM0MjRjNzRmMWNjNjQ2ZWVjOGVhMGRiMzQyMGYxZGQxZDhiIn19fQ=="));
/*  126 */       registerBerry("覆盆子", "&d", 8193, PlantType.BUSH, new PlantData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODI2MmM0NDViYzJkZDFjNWJiYzhiOTNmMjQ4MmY5ZmRiZWY0OGE3MjQ1ZTFiZGIzNjFkNGE1NjgxOTBkOWI1In19fQ=="));
/*  127 */       registerBerry("黑莓", "&8", 8200, PlantType.BUSH, new PlantData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjc2OWY4Yjc4YzQyZTI3MmE2NjlkNmU2ZDE5YmE4NjUxYjcxMGFiNzZmNmI0NmQ5MDlkNmEzZDQ4Mjc1NCJ9fX0="));
/*  128 */       registerBerry("蔓越莓", "&c", 8193, PlantType.BUSH, new PlantData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDVmZTZjNzE4ZmJhNzE5ZmY2MjIyMzdlZDllYTY4MjdkMDkzZWZmYWI4MTRiZTIxOTJlOTY0M2UzZTNkNyJ9fX0="));
/*  129 */       registerBerry("越桔", "&c", 8193, PlantType.BUSH, new PlantData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTA0ZTU0YmYyNTVhYjBiMWM0OThjYTNhMGNlYWU1YzdjNDVmMTg2MjNhNWEwMmY3OGE3OTEyNzAxYTMyNDkifX19"));
/*      */       
/*  131 */       registerBerry("草莓", "&4", 8193, PlantType.FRUIT, new PlantData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2JjODI2YWFhZmI4ZGJmNjc4ODFlNjg5NDQ0MTRmMTM5ODUwNjRhM2Y4ZjA0NGQ4ZWRmYjQ0NDNlNzZiYSJ9fX0="));
/*  132 */       registerPlant("番茄", "&4", Material.APPLE, PlantType.FRUIT, new PlantData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTkxNzIyMjZkMjc2MDcwZGMyMWI3NWJhMjVjYzJhYTU2NDlkYTVjYWM3NDViYTk3NzY5NWI1OWFlYmQifX19"));
/*  133 */       registerPlant("生菜", "&2", Material.LEAVES, PlantType.FRUIT, new PlantData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDc3ZGQ4NDJjOTc1ZDhmYjAzYjFhZGQ2NmRiODM3N2ExOGJhOTg3MDUyMTYxZjIyNTkxZTZhNGVkZTdmNSJ9fX0="));
/*  134 */       registerPlant("茶叶", "&a", Material.LEAVES, PlantType.DOUBLE_PLANT, new PlantData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTUxNGM4YjQ2MTI0N2FiMTdmZTM2MDZlNmUyZjRkMzYzZGNjYWU5ZWQ1YmVkZDAxMmI0OThkN2FlOGViMyJ9fX0="));
/*  135 */       registerPlant("卷心菜", "&2", Material.LEAVES, PlantType.FRUIT, new PlantData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmNkNmQ2NzMyMGM5MTMxYmU4NWExNjRjZDdjNWZjZjI4OGYyOGMyODE2NTQ3ZGIzMGEzMTg3NDE2YmRjNDViIn19fQ=="));
/*  136 */       registerPlant("番薯", "&6", Material.LEAVES, PlantType.FRUIT, new PlantData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2ZmNDg1NzhiNjY4NGUxNzk5NDRhYjFiYzc1ZmVjNzVmOGZkNTkyZGZiNDU2ZjZkZWY3NjU3NzEwMWE2NiJ9fX0="));
/*  137 */       registerPlant("芥菜籽", "&e", Material.GOLD_NUGGET, PlantType.FRUIT, new PlantData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWQ1M2E0MjQ5NWZhMjdmYjkyNTY5OWJjM2U1ZjI5NTNjYzJkYzMxZDAyN2QxNGZjZjdiOGMyNGI0NjcxMjFmIn19fQ=="));
/*      */       
/*  139 */       registerPlant("玉米", "&6", Material.GOLDEN_CARROT, PlantType.DOUBLE_PLANT, new PlantData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWJkMzgwMmU1ZmFjMDNhZmFiNzQyYjBmM2NjYTQxYmNkNDcyM2JlZTkxMWQyM2JlMjljZmZkNWI5NjVmMSJ9fX0="));
/*  140 */       registerPlant("菠萝", "&6", Material.GOLDEN_CARROT, PlantType.DOUBLE_PLANT, new PlantData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDdlZGRkODJlNTc1ZGZkNWI3NTc5ZDg5ZGNkMjM1MGM5OTFmMDQ4M2E3NjQ3Y2ZmZDNkMmM1ODdmMjEifX19"));
/*      */       
/*  142 */       registerTree("苹果", new MaterialData(Material.APPLE), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2JiMzExZjNiYTFjMDdjM2QxMTQ3Y2QyMTBkODFmZTExZmQ4YWU5ZTNkYjIxMmEwZmE3NDg5NDZjMzYzMyJ9fX0=", "APPLE", "&c", 0, "", true, new Material[] { Material.DIRT, Material.GRASS });
/*  143 */       registerTree("椰子", new MaterialData(Material.INK_SACK, (byte)3), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmQyN2RlZDU3Yjk0Y2Y3MTViMDQ4ZWY1MTdhYjNmODViZWY1YTdiZTY5ZjE0YjE1NzNlMTRlN2U0MmUyZTgifX19", "COCONUT", "&6", 8194, "椰奶", false, new Material[] { Material.SAND });
/*      */       
/*  145 */       registerTree("樱桃", new MaterialData(Material.APPLE), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzUyMDc2NmI4N2QyNDYzYzM0MTczZmZjZDU3OGIwZTY3ZDE2M2QzN2EyZDdjMmU3NzkxNWNkOTExNDRkNDBkMSJ9fX0=", "CHERRY", "&c", 8193, "樱桃汁", true, new Material[] { Material.DIRT, Material.GRASS });
/*      */       
/*  147 */       registerTree("石榴", new MaterialData(Material.APPLE), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2JiMzExZjNiYTFjMDdjM2QxMTQ3Y2QyMTBkODFmZTExZmQ4YWU5ZTNkYjIxMmEwZmE3NDg5NDZjMzYzMyJ9fX0=", "POMEGRANATE", "&4", 8201, "石榴汁", true, new Material[] { Material.DIRT, Material.GRASS });
/*      */       
/*  149 */       registerTree("柠檬", new MaterialData(Material.POTATO_ITEM), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTU3ZmQ1NmNhMTU5Nzg3NzkzMjRkZjUxOTM1NGI2NjM5YThkOWJjMTE5MmM3YzNkZTkyNWEzMjliYWVmNmMifX19", "LEMON", "&e", 8227, "柠檬汁", true, new Material[] { Material.DIRT, Material.GRASS });
/*      */       
/*  151 */       registerTree("李子", new MaterialData(Material.APPLE), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjlkNjY0MzE5ZmYzODFiNGVlNjlhNjk3NzE1Yjc2NDJiMzJkNTRkNzI2Yzg3ZjY0NDBiZjAxN2E0YmNkNyJ9fX0=", "PLUM", "&5", 8201, "李子汁", true, new Material[] { Material.DIRT, Material.GRASS });
/*      */       
/*  153 */       registerTree("酸橙", new MaterialData(Material.SLIME_BALL), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWE1MTUzNDc5ZDlmMTQ2YTVlZTNjOWUyMThmNWU3ZTg0YzRmYTM3NWU0Zjg2ZDMxNzcyYmE3MWY2NDY4In19fQ==", "LIME", "&a", 8203, "酸橙汁", true, new Material[] { Material.DIRT, Material.GRASS });
/*      */       
/*  155 */       registerTree("橙子", new MaterialData(Material.APPLE), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjViMWRiNTQ3ZDFiNzk1NmQ0NTExYWNjYjE1MzNlMjE3NTZkN2NiYzM4ZWI2NDM1NWEyNjI2NDEyMjEyIn19fQ==", "ORANGE", "&6", 8195, "橙汁", true, new Material[] { Material.DIRT, Material.GRASS });
/*      */       
/*  157 */       registerTree("桃子", new MaterialData(Material.APPLE), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDNiYTQxZmU4Mjc1Nzg3MWU4Y2JlYzlkZWQ5YWNiZmQxOTkzMGQ5MzM0MWNmODEzOWQxZGZiZmFhM2VjMmE1In19fQ==", "PEACH", "&5", 8201, "桃汁", true, new Material[] { Material.DIRT, Material.GRASS });
/*      */       
/*  159 */       registerTree("香梨", new MaterialData(Material.SLIME_BALL), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmRlMjhkZjg0NDk2MWE4ZWNhOGVmYjc5ZWJiNGFlMTBiODM0YzY0YTY2ODE1ZThiNjQ1YWVmZjc1ODg5NjY0YiJ9fX0=", "PEAR", "&a", 8203, "梨汁", true, new Material[] { Material.DIRT, Material.GRASS });
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  164 */       registerMagicalPlant("煤炭", (ItemStack)new CustomItem(new ItemStack(Material.COAL, 2), "§d煤炭", new String[] { "", "&7异化植物粉末合成" }), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzc4OGY1ZGRhZjUyYzU4NDIyODdiOTQyN2E3NGRhYzhmMDkxOWViMmZkYjFiNTEzNjVhYjI1ZWIzOTJjNDcifX19", new ItemStack[] { new ItemStack(Material.BEDROCK), new ItemStack(Material.COAL_ORE), new ItemStack(Material.BEDROCK), new ItemStack(Material.COAL_ORE), new ItemStack(Material.SEEDS), new ItemStack(Material.COAL_ORE), null, new ItemStack(Material.COAL_ORE), null });
/*      */ 
/*      */       
/*  167 */       registerMagicalPlant("铁", (ItemStack)new CustomItem(new ItemStack(Material.IRON_INGOT), "§d铁", new String[] { "", "&7异化植物粉末合成" }), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGI5N2JkZjkyYjYxOTI2ZTM5ZjVjZGRmMTJmOGY3MTMyOTI5ZGVlNTQxNzcxZTBiNTkyYzhiODJjOWFkNTJkIn19fQ==", new ItemStack[] { new ItemStack(Material.BEDROCK), new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.BEDROCK), new ItemStack(Material.IRON_BLOCK), 
/*  168 */             getItem("COAL_PLANT"), new ItemStack(Material.IRON_BLOCK), null, new ItemStack(Material.IRON_BLOCK), null });
/*      */       
/*  170 */       registerMagicalPlant("黄金", SlimefunItems.GOLD_4K, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTRkZjg5MjI5M2E5MjM2ZjczZjQ4ZjllZmU5NzlmZTA3ZGJkOTFmN2I1ZDIzOWU0YWNmZDM5NGY2ZWNhIn19fQ==", new ItemStack[] { new ItemStack(Material.BEDROCK), SlimefunItems.GOLD_16K, new ItemStack(Material.BEDROCK), SlimefunItems.GOLD_16K, 
/*  171 */             getItem("IRON_PLANT"), SlimefunItems.GOLD_16K, null, SlimefunItems.GOLD_16K, null });
/*      */       
/*  173 */       registerMagicalPlant("红石", (ItemStack)new CustomItem(new ItemStack(Material.REDSTONE, 6), "§d红石", new String[] { "", "&7异化植物粉末合成" }), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZThkZWVlNTg2NmFiMTk5ZWRhMWJkZDc3MDdiZGI5ZWRkNjkzNDQ0ZjFlM2JkMzM2YmQyYzc2NzE1MWNmMiJ9fX0=", new ItemStack[] { new ItemStack(Material.BEDROCK), new ItemStack(Material.REDSTONE_BLOCK), new ItemStack(Material.BEDROCK), new ItemStack(Material.REDSTONE_BLOCK), 
/*  174 */             getItem("GOLD_PLANT"), new ItemStack(Material.REDSTONE_BLOCK), null, new ItemStack(Material.REDSTONE_BLOCK), null });
/*      */       
/*  176 */       registerMagicalPlant("青金石", (ItemStack)new CustomItem((new MaterialData(Material.INK_SACK, (byte)4)).toItemStack(10), "§d青金石", new String[] { "", "&7异化植物粉末合成" }), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmFhMGQwZmVhMWFmYWVlMzM0Y2FiNGQyOWQ4Njk2NTJmNTU2M2M2MzUyNTNjMGNiZWQ3OTdlZDNjZjU3ZGUwIn19fQ==", new ItemStack[] { new ItemStack(Material.BEDROCK), new ItemStack(Material.LAPIS_ORE), new ItemStack(Material.BEDROCK), new ItemStack(Material.LAPIS_ORE), 
/*  177 */             getItem("REDSTONE_PLANT"), new ItemStack(Material.LAPIS_ORE), null, new ItemStack(Material.LAPIS_ORE), null });
/*      */       
/*  179 */       registerMagicalPlant("末影", (ItemStack)new CustomItem(new ItemStack(Material.ENDER_PEARL, 3), "§d末影珍珠", new String[] { "", "&7异化植物粉末合成" }), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGUzNWFhZGU4MTI5MmU2ZmY0Y2QzM2RjMGVhNmExMzI2ZDA0NTk3YzBlNTI5ZGVmNDE4MmIxZDE1NDhjZmUxIn19fQ==", new ItemStack[] { new ItemStack(Material.BEDROCK), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.BEDROCK), new ItemStack(Material.ENDER_PEARL), 
/*  180 */             getItem("LAPIS_PLANT"), new ItemStack(Material.ENDER_PEARL), null, new ItemStack(Material.ENDER_PEARL), null });
/*      */       
/*  182 */       registerMagicalPlant("石英", (ItemStack)new CustomItem(new ItemStack(Material.QUARTZ, 4), "§d石英", new String[] { "", "&7异化植物粉末合成" }), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjZkZTU4ZDU4M2MxMDNjMWNkMzQ4MjQzODBjOGE0NzdlODk4ZmRlMmViOWE3NGU3MWYxYTk4NTA1M2I5NiJ9fX0=", new ItemStack[] { new ItemStack(Material.BEDROCK), new ItemStack(Material.QUARTZ_ORE), new ItemStack(Material.BEDROCK), new ItemStack(Material.QUARTZ_ORE), 
/*  183 */             getItem("ENDER_PLANT"), new ItemStack(Material.QUARTZ_ORE), null, new ItemStack(Material.QUARTZ_ORE), null });
/*      */       
/*  185 */       registerMagicalPlant("钻石", (ItemStack)new CustomItem(new ItemStack(Material.DIAMOND), "§d钻石", new String[] { "", "&7异化植物粉末合成" }), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjg4Y2Q2ZGQ1MDM1OWM3ZDU4OThjN2M3ZTNlMjYwYmZjZDNkY2IxNDkzYTg5YjllODhlOWNiZWNiZmU0NTk0OSJ9fX0=", new ItemStack[] { new ItemStack(Material.BEDROCK), new ItemStack(Material.DIAMOND), new ItemStack(Material.BEDROCK), new ItemStack(Material.DIAMOND), 
/*  186 */             getItem("QUARTZ_PLANT"), new ItemStack(Material.DIAMOND), null, new ItemStack(Material.DIAMOND), null });
/*      */       
/*  188 */       registerMagicalPlant("绿宝石", (ItemStack)new CustomItem(new ItemStack(Material.EMERALD), "§d绿宝石", new String[] { "", "&7异化植物粉末合成" }), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGZjNDk1ZDFlNmViNTRhMzg2MDY4YzZjYjEyMWM1ODc1ZTAzMWI3ZjYxZDcyMzZkNWYyNGI3N2RiN2RhN2YifX19", new ItemStack[] { new ItemStack(Material.BEDROCK), new ItemStack(Material.EMERALD), new ItemStack(Material.BEDROCK), new ItemStack(Material.EMERALD), 
/*  189 */             getItem("DIAMOND_PLANT"), new ItemStack(Material.EMERALD), null, new ItemStack(Material.EMERALD), null });
/*      */       
/*  191 */       registerMagicalPlant("萤石", (ItemStack)new CustomItem(new ItemStack(Material.GLOWSTONE_DUST, 8), "§d萤石", new String[] { "", "&7异化植物粉末合成" }), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjVkN2JlZDhkZjcxNGNlYTA2M2U0NTdiYTVlODc5MzExNDFkZTI5M2RkMWQ5YjkxNDZiMGY1YWIzODM4NjYifX19", new ItemStack[] { new ItemStack(Material.BEDROCK), new ItemStack(Material.GLOWSTONE), new ItemStack(Material.BEDROCK), new ItemStack(Material.GLOWSTONE), 
/*  192 */             getItem("REDSTONE_PLANT"), new ItemStack(Material.GLOWSTONE), null, new ItemStack(Material.GLOWSTONE), null });
/*      */       
/*  194 */       registerMagicalPlant("黑曜石", (ItemStack)new CustomItem(new ItemStack(Material.OBSIDIAN), "§d黑曜石", new String[] { "", "&7异化植物粉末合成" }), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzg0MGI4N2Q1MjI3MWQyYTc1NWRlZGM4Mjg3N2UwZWQzZGY2N2RjYzQyZWE0NzllYzE0NjE3NmIwMjc3OWE1In19fQ==", new ItemStack[] { new ItemStack(Material.BEDROCK), new ItemStack(Material.OBSIDIAN), new ItemStack(Material.BEDROCK), new ItemStack(Material.OBSIDIAN), 
/*  195 */             getItem("LAPIS_PLANT"), new ItemStack(Material.OBSIDIAN), null, new ItemStack(Material.OBSIDIAN), null });
/*      */       
/*  197 */       registerMagicalPlant("史莱姆", (ItemStack)new CustomItem(new ItemStack(Material.SLIME_BALL), "§d史莱姆", new String[] { "", "&7异化植物粉末合成" }), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTBlNjVlNmU1MTEzYTUxODdkYWQ0NmRmYWQzZDNiZjg1ZThlZjgwN2Y4MmFhYzIyOGE1OWM0YTk1ZDZmNmEifX19", new ItemStack[] { new ItemStack(Material.BEDROCK), new ItemStack(Material.SLIME_BALL), new ItemStack(Material.BEDROCK), new ItemStack(Material.SLIME_BALL), 
/*  198 */             getItem("ENDER_PLANT"), new ItemStack(Material.SLIME_BALL), null, new ItemStack(Material.SLIME_BALL), null });
/*      */       
/*  200 */       registerMagicalPlant("潜影壳", (ItemStack)new CustomItem(new ItemStack(Material.SHULKER_SHELL), "§d潜影壳", new String[] { "", "&7异化植物粉末合成" }), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmEyZDhkOGYzNDQ3ZGE0OTgyYWQ2NGMxNmQ0NTNiZjgwMmNmODgwZmFmNzRkYjQ0NTJjODFjYjk2NDQ3MzgifX19", new ItemStack[] { new ItemStack(Material.BEDROCK), new ItemStack(Material.SHULKER_SHELL), new ItemStack(Material.BEDROCK), new ItemStack(Material.SHULKER_SHELL), 
/*  201 */             getItem("SLIME_PLANT"), new ItemStack(Material.SHULKER_SHELL), null, new ItemStack(Material.SHULKER_SHELL), null });
/*      */       
/*  203 */       registerTechPlant("咖啡豆", "&c", Material.COCOA, PlantType.DOUBLE_PLANT, new PlantData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTA4M2VjMmIwMWRjMGZlZTc5YWEzMjE4OGQ5NDI5YWNjNjhlY2Y3MTQwOGRjYTA0YWFhYjUzYWQ4YmVhMCJ9fX0="));
/*      */ 
/*      */       
/*  206 */       registerTechPlant("仙馐果", "&b", Material.APPLE, PlantType.DOUBLE_PLANT, new PlantData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGNkY2YzOGE4NDM4ZWQzYTU0N2Y4ZDViNDdlMDgwMTU1OWM1OTVmMGUyNmM0NTY1NmE3NmI1YmY4YTU2ZiJ9fX0="));
/*      */ 
/*      */       
/*  209 */       registerTechPlant("酒香果", "&b", Material.LEAVES, PlantType.DOUBLE_PLANT, new PlantData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzRjMDVkZDVkN2E5Mjg4OWQ4ZDIyZDRkZjBmMWExZmUyYmVlM2VkZGYxOTJmNzhmYzQ0ZTAyZTE0ZGJmNjI5In19fQ=="));
/*      */ 
/*      */ 
/*      */       
/*  213 */       final CustomItem grass_seeds = new CustomItem(new MaterialData(Material.PUMPKIN_SEEDS), "&a草籽", new String[] { "", "&7用于绿化泥土" });
/*      */       
/*  215 */       final SlimefunItem crook = new SlimefunItem(Categories.TOOLS, (ItemStack)new CustomItem(new MaterialData(Material.IRON_HOE), "&3镰刀", new String[] { "", "&7+ &b6% &7树种获取率" }), "CROOK", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { SlimefunItems.STEEL_INGOT, SlimefunItems.COPPER_INGOT, null, SlimefunItems.STEEL_INGOT, new ItemStack(Material.STICK), null, null, new ItemStack(Material.STICK), null });
/*      */ 
/*      */       
/*  218 */       registerDishes();
/*      */ 
/*      */       
/*  221 */       ExoticItems.registerItems();
/*      */ 
/*      */       
/*  224 */       crook.register(false, new ItemHandler[] { (ItemHandler)new BlockBreakHandler()
/*      */             {
/*      */               public boolean onBlockBreak(BlockBreakEvent arg0, ItemStack arg1, int arg2, List<ItemStack> arg3)
/*      */               {
/*  228 */                 if (SlimefunManager.isItemSimiliar(arg1, crook.getItem(), true)) {
/*  229 */                   PlayerInventory.damageItemInHand(arg0.getPlayer());
/*  230 */                   if ((arg0.getBlock().getType() == Material.LEAVES || arg0.getBlock().getType() == Material.LEAVES_2) && CSCoreLib.randomizer().nextInt(100) < 6) {
/*  231 */                     ItemStack sapling = (new MaterialData(Material.SAPLING, (byte)(arg0.getBlock().getData() % 4 + ((arg0.getBlock().getType() == Material.LEAVES_2) ? 4 : 0)))).toItemStack(1);
/*  232 */                     arg3.add(sapling);
/*      */                   } 
/*  234 */                   return true;
/*      */                 } 
/*  236 */                 return false;
/*      */               }
/*      */             } });
/*      */       
/*  240 */       (new SlimefunItem(category_main, (ItemStack)customItem, "GRASS_SEEDS", new RecipeType((ItemStack)new CustomItem(Material.LONG_GRASS, "&7破坏杂草获得", 1)), new ItemStack[] { null, null, null, null, (ItemStack)new CustomItem(Material.LONG_GRASS, 1), null, null, null, null
/*      */           
/*  242 */           })).register(false, new ItemHandler[] { (ItemHandler)new ItemInteractionHandler()
/*      */             {
/*      */               public boolean onRightClick(ItemUseEvent arg0, Player arg1, ItemStack arg2)
/*      */               {
/*  246 */                 if (SlimefunManager.isItemSimiliar(arg2, grass_seeds, true)) {
/*  247 */                   if (arg0.getClickedBlock() != null && arg0.getClickedBlock().getType() == Material.DIRT) {
/*  248 */                     PlayerInventory.consumeItemInHand(arg1);
/*  249 */                     arg0.getClickedBlock().setType(Material.GRASS);
/*  250 */                     arg0.getClickedBlock().getWorld().playEffect(arg0.getClickedBlock().getLocation(), Effect.STEP_SOUND, Material.GRASS);
/*      */                   } 
/*  252 */                   return true;
/*      */                 } 
/*  254 */                 return false;
/*      */               }
/*      */             } });
/*      */       
/*  258 */       new PlantsListener(this);
/*  259 */       new FoodListener(this);
/*  260 */       getServer().getPluginManager().registerEvents(new PlayerListener(), (Plugin)this);
/*      */       
/*  262 */       items.put("SEEDS", new ItemStack(Material.SEEDS));
/*  263 */       items.put("PUMPKIN_SEEDS", new ItemStack(Material.PUMPKIN_SEEDS));
/*  264 */       items.put("MELON_SEEDS", new ItemStack(Material.MELON_SEEDS));
/*  265 */       items.put("OAK_SAPLING", new ItemStack(Material.SAPLING));
/*  266 */       items.put("SPRUCE_SAPLING", new CustomItem(Material.SAPLING, 1));
/*  267 */       items.put("BIRCH_SAPLING", new CustomItem(Material.SAPLING, 2));
/*  268 */       items.put("JUNGLE_SAPLING", new CustomItem(Material.SAPLING, 3));
/*  269 */       items.put("ACACIA_SAPLING", new CustomItem(Material.SAPLING, 4));
/*  270 */       items.put("DARK_OAK_SAPLING", new CustomItem(Material.SAPLING, 5));
/*  271 */       items.put("GRASS_SEEDS", customItem);
/*  272 */       items.put("MYSTIC_SEED", ExoticItems.MysticSeed);
/*      */       
/*  274 */       Iterator<String> iterator = items.keySet().iterator();
/*  275 */       while (iterator.hasNext()) {
/*  276 */         String key = iterator.next();
/*  277 */         this.cfg.setDefaultValue("long-grass-drops." + key, Boolean.valueOf(true));
/*  278 */         if (!this.cfg.getBoolean("long-grass-drops." + key)) iterator.remove(); 
/*      */       } 
/*  280 */       this.cfg.save();
/*  281 */       getServer().getScheduler().runTaskTimer((Plugin)this, new BukkitRunnable()
/*      */           {
/*      */             public void run() {
/*  284 */               ExoticGarden.this.checkDrunkers();
/*      */             }
/*      */           },  120L, 120L);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void onLoad() {
/*  292 */     instance = this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void registerDishes() {
/*  298 */     (new CustomFood(category_drinks, (ItemStack)new CustomItem(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWQ1ZThjNDg2YjUyNmRkYTgxMmI0MjQ0YzJmMjE5NDE4OWZiZWJjY2JlYmZiYTVhOTM3YTU2NTMzNWRhNDEyIn19fQ=="), "§3咖啡", new String[] { "", "&7提神醒脑的咖啡", "&7恢复&e2&7点饥饿", "&7恢复&e6&7点精神" }), "COFFEE", RecipeType.JUICER, new ItemStack[] {
/*      */           
/*  300 */           getItem("COFFEEBEAN"), null, null, null, null, null, null, null, null }, 2, 6.0F)).register();
/*      */     
/*  302 */     (new CustomFood(category_drinks, (ItemStack)new CustomPotion("&a酸橙冰沙", 8203, new String[] { "", "§7恢复 §b5.0 §7饥饿值§8和§7精神值" }, new PotionEffect(PotionEffectType.SATURATION, 10, 0)), "LIME_SMOOTHIE", RecipeType.JUICER, new ItemStack[] {
/*  303 */           getItem("LIME_JUICE"), getItem("ICE_CUBE"), null, null, null, null, null, null, null }, 5))
/*  304 */       .register();
/*      */     
/*  306 */     (new CustomFood(category_drinks, (ItemStack)new CustomPotion("&4番茄汁", 8193, new String[] { "", "§7恢复 §b3.0 §7饥饿值§8和§7精神值" }, new PotionEffect(PotionEffectType.SATURATION, 6, 0)), "TOMATO_JUICE", RecipeType.JUICER, new ItemStack[] {
/*  307 */           getItem("TOMATO"), null, null, null, null, null, null, null, null }, 3))
/*  308 */       .register();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  314 */     (new CustomFood(category_drinks, (ItemStack)new CustomPotion("&e柠檬冰茶", 8227, new String[] { "", "§7恢复 §b6.5 §7饥饿值§8和§7精神值" }, new PotionEffect(PotionEffectType.SATURATION, 13, 0)), "LEMON_ICED_TEA", RecipeType.JUICER, new ItemStack[] {
/*  315 */           getItem("LEMON"), getItem("ICE_CUBE"), getItem("TEA_LEAF"), null, null, null, null, null, null }, 6))
/*  316 */       .register();
/*      */     
/*  318 */     (new CustomFood(category_drinks, (ItemStack)new CustomPotion("&d覆盆子冰茶", 8193, new String[] { "", "§7恢复 §b6.5 §7饥饿值§8和§7精神值" }, new PotionEffect(PotionEffectType.SATURATION, 13, 0)), "RASPBERRY_ICED_TEA", RecipeType.JUICER, new ItemStack[] {
/*  319 */           getItem("RASPBERRY"), getItem("ICE_CUBE"), getItem("TEA_LEAF"), null, null, null, null, null, null }, 6))
/*  320 */       .register();
/*      */     
/*  322 */     (new CustomFood(category_drinks, (ItemStack)new CustomPotion("&d香桃冰茶", 8193, new String[] { "", "§7恢复 §b6.5 §7饥饿值§8和§7精神值" }, new PotionEffect(PotionEffectType.SATURATION, 13, 0)), "PEACH_ICED_TEA", RecipeType.JUICER, new ItemStack[] {
/*  323 */           getItem("PEACH"), getItem("ICE_CUBE"), getItem("TEA_LEAF"), null, null, null, null, null, null }, 6))
/*  324 */       .register();
/*      */     
/*  326 */     (new CustomFood(category_drinks, (ItemStack)new CustomPotion("&c草莓冰茶", 8193, new String[] { "", "§7恢复 §b6.5 §7饥饿值§8和§7精神值" }, new PotionEffect(PotionEffectType.SATURATION, 13, 0)), "STRAWBERRY_ICED_TEA", RecipeType.JUICER, new ItemStack[] {
/*  327 */           getItem("STRAWBERRY"), getItem("ICE_CUBE"), getItem("TEA_LEAF"), null, null, null, null, null, null }, 6))
/*  328 */       .register();
/*      */     
/*  330 */     (new CustomFood(category_drinks, (ItemStack)new CustomPotion("&c樱桃冰茶", 8193, new String[] { "", "§7恢复 §b6.5 §7饥饿值§8和§7精神值" }, new PotionEffect(PotionEffectType.SATURATION, 13, 0)), "CHERRY_ICED_TEA", RecipeType.JUICER, new ItemStack[] {
/*  331 */           getItem("CHERRY"), getItem("ICE_CUBE"), getItem("TEA_LEAF"), null, null, null, null, null, null }, 6))
/*  332 */       .register();
/*      */     
/*  334 */     (new CustomFood(category_drinks, (ItemStack)new CustomPotion("&6泰式甜茶", 8201, new String[] { "", "§7恢复 §b7.0 §7饥饿值§8和§7精神值" }, new PotionEffect(PotionEffectType.SATURATION, 14, 0)), "THAI_TEA", RecipeType.JUICER, new ItemStack[] {
/*  335 */           getItem("TEA_LEAF"), new ItemStack(Material.SUGAR), SlimefunItems.HEAVY_CREAM, getItem("COCONUT_MILK"), null, null, null, null, null }, 7))
/*  336 */       .register();
/*      */     
/*  338 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjM0ODdkNDU3ZjkwNjJkNzg3YTNlNmNlMWM0NjY0YmY3NDAyZWM2N2RkMTExMjU2ZjE5YjM4Y2U0ZjY3MCJ9fX0="), "&e南瓜面包", new String[] { "", "§7恢复 §b4.0 §7饥饿值§8和§7精神值" }), "PUMPKIN_BREAD", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { SlimefunItems.WHEAT_FLOUR, SlimefunItems.WHEAT_FLOUR, SlimefunItems.WHEAT_FLOUR, new ItemStack(Material.SUGAR), new ItemStack(Material.PUMPKIN), new ItemStack(Material.SUGAR), SlimefunItems.WHEAT_FLOUR, SlimefunItems.WHEAT_FLOUR, SlimefunItems.WHEAT_FLOUR }, 8))
/*      */ 
/*      */ 
/*      */       
/*  342 */       .register();
/*      */     
/*  344 */     (new EGPlant(Categories.MISC, (ItemStack)new CustomItem(getSkull(Material.MILK_BUCKET, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2Y4ZDUzNmM4YzJjMjU5NmJjYzE3MDk1OTBhOWQ3ZTMzMDYxYzU2ZTY1ODk3NGNkODFiYjgzMmVhNGQ4ODQyIn19fQ=="), "&e蛋黄酱"), "MAYO", RecipeType.GRIND_STONE, false, new ItemStack[] { new ItemStack(Material.EGG), new ItemStack(Material.EGG), null, null, null, null, null, null, null
/*      */ 
/*      */         
/*  347 */         })).register();
/*      */     
/*  349 */     (new EGPlant(Categories.MISC, (ItemStack)new CustomItem(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWI5ZTk5NjIxYjk3NzNiMjllMzc1ZTYyYzY0OTVmZjFhYzg0N2Y4NWIyOTgxNmMyZWI3N2I1ODc4NzRiYTYyIn19fQ=="), "&e芥末"), "MUSTARD", RecipeType.GRIND_STONE, false, new ItemStack[] {
/*      */           
/*  351 */           getItem("MUSTARD_SEED"), getItem("MUSTARD_SEED"), null, null, null, null, null, null, null
/*  352 */         })).register();
/*      */     
/*  354 */     (new EGPlant(Categories.MISC, (ItemStack)new CustomItem(getSkull(Material.MILK_BUCKET, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTg2ZjE5YmYyM2QyNDhlNjYyYzljOGI3ZmExNWVmYjhhMWYxZDViZGFjZDNiODYyNWE5YjU5ZTkzYWM4YSJ9fX0="), "&c烤肉酱"), "BBQ_SAUCE", RecipeType.ENHANCED_CRAFTING_TABLE, false, new ItemStack[] {
/*      */           
/*  356 */           getItem("TOMATO"), getItem("MUSTARD"), getItem("SALT"), new ItemStack(Material.SUGAR), null, null, null, null, null
/*  357 */         })).register();
/*      */     
/*  359 */     (new SlimefunItem(Categories.MISC, (ItemStack)new CustomItem(new MaterialData(Material.SUGAR), "&r玉米粉", new String[0]), "CORNMEAL", RecipeType.GRIND_STONE, new ItemStack[] {
/*  360 */           getItem("CORN"), null, null, null, null, null, null, null, null
/*  361 */         })).register();
/*      */     
/*  363 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(new MaterialData(Material.INK_SACK, (byte)3), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODE5Zjk0OGQxNzcxOGFkYWNlNWRkNmUwNTBjNTg2MjI5NjUzZmVmNjQ1ZDcxMTNhYjk0ZDE3YjYzOWNjNDY2In19fQ=="), "&3巧克力棒", new String[] { "", "§7恢复 §b1.5 §7饥饿值§8和§7精神值" }), "CHOCOLATE_BAR", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { (new MaterialData(Material.INK_SACK, (byte)3))
/*      */           
/*  365 */           .toItemStack(1), SlimefunItems.HEAVY_CREAM, null, null, null, null, null, null, null }3))
/*      */       
/*  367 */       .register();
/*      */     
/*  369 */     (new CustomFood(category_food, (ItemStack)new CustomItem(Material.MUSHROOM_SOUP, "&e土豆沙拉", 0, new String[] { "", "§7恢复 §b6.0 §7饥饿值§8和§7精神值" }), "POTATO_SALAD", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.BAKED_POTATO), 
/*  370 */           getItem("MAYO"), new ItemStack(Material.BAKED_POTATO), null, new ItemStack(Material.BOWL), null, null, null, null }, 6))
/*      */       
/*  372 */       .register();
/*      */     
/*  374 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTE0MjE2ZDEwNzE0MDgyYmJlM2Y0MTI0MjNlNmIxOTIzMjM1MmY0ZDY0ZjlhY2EzOTEzY2I0NjMxOGQzZWQifX19"), "&e鸡肉三明治", new String[] { "", "§7恢复 §b5.5 §7饥饿值§8和§7精神值" }), "CHICKEN_SANDWICH", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.COOKED_CHICKEN), 
/*      */           
/*  376 */           getItem("MAYO"), new ItemStack(Material.BREAD), null, null, null, null, null, null }, 11))
/*      */       
/*  378 */       .register();
/*      */     
/*  380 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTE0MjE2ZDEwNzE0MDgyYmJlM2Y0MTI0MjNlNmIxOTIzMjM1MmY0ZDY0ZjlhY2EzOTEzY2I0NjMxOGQzZWQifX19"), "&3鱼肉三明治", new String[] { "", "§7恢复 §b5.5 §7饥饿值§8和§7精神值" }), "FISH_SANDWICH", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.COOKED_FISH), 
/*      */           
/*  382 */           getItem("MAYO"), new ItemStack(Material.BREAD), null, null, null, null, null, null }, 11))
/*      */       
/*  384 */       .register();
/*      */     
/*  386 */     (new CustomFood(category_food, (ItemStack)new CustomItem(Material.MUSHROOM_SOUP, "&e鸡蛋沙拉", 0, new String[] { "", "§7恢复 §b6.0 §7饥饿值§8和§7精神值" }), "EGG_SALAD", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.EGG), 
/*  387 */           getItem("MAYO"), new ItemStack(Material.EGG), null, new ItemStack(Material.BOWL), null, null, null, null }, 6))
/*      */       
/*  389 */       .register();
/*      */     
/*  391 */     (new CustomFood(category_food, (ItemStack)new CustomItem(Material.MUSHROOM_SOUP, "&4番茄浓汤", 0, new String[] { "", "§7恢复 §b5.5 §7饥饿值§8和§7精神值" }), "TOMATO_SOUP", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { SlimefunItems.HEAVY_CREAM, 
/*  392 */           getItem("TOMATO"), SlimefunItems.SALT, null, new ItemStack(Material.BOWL), null, null, null, null }, 5))
/*      */       
/*  394 */       .register();
/*      */     
/*  396 */     (new CustomFood(category_food, (ItemStack)new CustomItem(Material.MUSHROOM_SOUP, "&c草莓沙拉", 0, new String[] { "", "§7恢复 §b5.0 §7饥饿值§8和§7精神值" }), "STRAWBERRY_SALAD", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.BOWL), 
/*  397 */           getItem("STRAWBERRY"), null, null, null, null, null, null, null }, 4))
/*      */       
/*  399 */       .register();
/*      */     
/*  401 */     (new CustomFood(category_food, (ItemStack)new CustomItem(Material.MUSHROOM_SOUP, "&c葡萄沙拉", 0, new String[] { "", "§7恢复 §b5.0 §7饥饿值§8和§7精神值" }), "GRAPE_SALAD", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.BOWL), 
/*  402 */           getItem("GRAPE"), null, null, null, null, null, null, null }, 4))
/*      */       
/*  404 */       .register();
/*      */     
/*  406 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjM2NWI2MWU3OWZjYjkxM2JjODYwZjRlYzYzNWQ0YTZhYjFiNzRiZmFiNjJmYjZlYTZkODlhMTZhYTg0MSJ9fX0="), "&r芝士蛋糕", new String[] { "", "§7恢复 §b8.0 §7饥饿值§8和§7精神值" }), "CHEESECAKE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { null, new ItemStack(Material.SUGAR), null, SlimefunItems.HEAVY_CREAM, new ItemStack(Material.EGG), SlimefunItems.HEAVY_CREAM, SlimefunItems.WHEAT_FLOUR, SlimefunItems.WHEAT_FLOUR, SlimefunItems.WHEAT_FLOUR }, 16))
/*      */ 
/*      */ 
/*      */       
/*  410 */       .register();
/*      */     
/*  412 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjM2NWI2MWU3OWZjYjkxM2JjODYwZjRlYzYzNWQ0YTZhYjFiNzRiZmFiNjJmYjZlYTZkODlhMTZhYTg0MSJ9fX0="), "&c樱桃芝士蛋糕", new String[] { "", "§7恢复 §b8.5 §7饥饿值§8和§7精神值" }), "CHERRY_CHEESECAKE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  414 */           getItem("CHEESECAKE"), getItem("CHERRY"), null, null, null, null, null, null, null }, 17))
/*      */       
/*  416 */       .register();
/*      */     
/*  418 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjM2NWI2MWU3OWZjYjkxM2JjODYwZjRlYzYzNWQ0YTZhYjFiNzRiZmFiNjJmYjZlYTZkODlhMTZhYTg0MSJ9fX0="), "&9蓝莓芝士蛋糕", new String[] { "", "§7恢复 §b8.5 §7饥饿值§8和§7精神值" }), "BLUEBERRY_CHEESECAKE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  420 */           getItem("CHEESECAKE"), getItem("BLUEBERRY"), null, null, null, null, null, null, null }, 17))
/*      */       
/*  422 */       .register();
/*      */     
/*  424 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjM2NWI2MWU3OWZjYjkxM2JjODYwZjRlYzYzNWQ0YTZhYjFiNzRiZmFiNjJmYjZlYTZkODlhMTZhYTg0MSJ9fX0="), "&6南瓜芝士蛋糕", new String[] { "", "§7恢复 §b8.5 §7饥饿值§8和§7精神值" }), "PUMPKIN_CHEESECAKE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  426 */           getItem("CHEESECAKE"), new ItemStack(Material.PUMPKIN), null, null, null, null, null, null, null }, 17))
/*      */       
/*  428 */       .register();
/*      */     
/*  430 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjM2NWI2MWU3OWZjYjkxM2JjODYwZjRlYzYzNWQ0YTZhYjFiNzRiZmFiNjJmYjZlYTZkODlhMTZhYTg0MSJ9fX0="), "&6甜梨芝士蛋糕", new String[] { "", "§7恢复 §b9.0 §7饥饿值§8和§7精神值" }), "SWEETENED_PEAR_CHEESECAKE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  432 */           getItem("CHEESECAKE"), new ItemStack(Material.SUGAR), getItem("PEAR"), null, null, null, null, null, null }, 18))
/*      */       
/*  434 */       .register();
/*      */     
/*  436 */     (new CustomFood(category_food, (ItemStack)new CustomItem(Material.COOKIE, "&6松饼", 0, new String[] { "", "§7恢复 §b2.0 §7饥饿值§8和§7精神值" }), "BISCUIT", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { SlimefunItems.WHEAT_FLOUR, SlimefunItems.BUTTER, null, null, null, null, null, null, null }, 2))
/*      */ 
/*      */       
/*  439 */       .register();
/*      */     
/*  441 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzZjMzY1MjNjMmQxMWI4YzhlYTJlOTkyMjkxYzUyYTY1NDc2MGVjNzJkY2MzMmRhMmNiNjM2MTY0ODFlZSJ9fX0="), "&8黑莓脆皮饼", new String[] { "", "§7恢复 §b6.0 §7饥饿值§8和§7精神值" }), "BLACKBERRY_COBBLER", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.SUGAR), 
/*      */           
/*  443 */           getItem("BLACKBERRY"), SlimefunItems.WHEAT_FLOUR, null, null, null, null, null, null }, 4))
/*      */       
/*  445 */       .register();
/*      */     
/*  447 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjM2NWI2MWU3OWZjYjkxM2JjODYwZjRlYzYzNWQ0YTZhYjFiNzRiZmFiNjJmYjZlYTZkODlhMTZhYTg0MSJ9fX0="), "&e帕芙洛娃", new String[] { "", "§7恢复 §b9.0 §7饥饿值§8和§7精神值" }), "PAVLOVA", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  449 */           getItem("LEMON"), getItem("STRAWBERRY"), new ItemStack(Material.SUGAR), new ItemStack(Material.EGG), SlimefunItems.HEAVY_CREAM, null, null, null, null }, 18))
/*      */       
/*  451 */       .register();
/*      */     
/*  453 */     (new CustomFood(category_food, (ItemStack)new CustomItem(Material.GOLDEN_CARROT, "&6香甜玉米棒", 0, new String[] { "", "§7恢复 §b4.5 §7饥饿值§8和§7精神值" }), "CORN_ON_THE_COB", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { SlimefunItems.BUTTER, 
/*  454 */           getItem("CORN"), null, null, null, null, null, null, null }, 3))
/*      */       
/*  456 */       .register();
/*      */     
/*  458 */     (new CustomFood(category_food, (ItemStack)new CustomItem(Material.MUSHROOM_SOUP, "&r奶油玉米粒", 0, new String[] { "", "§7恢复 §b4.0 §7饥饿值§8和§7精神值" }), "CREAMED_CORN", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { SlimefunItems.HEAVY_CREAM, 
/*  459 */           getItem("CORN"), new ItemStack(Material.BOWL), null, null, null, null, null, null }, 2))
/*      */       
/*  461 */       .register();
/*  462 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.GRILLED_PORK, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTdiYTIyZDVkZjIxZTgyMWE2ZGU0YjhjOWQzNzNhM2FhMTg3ZDhhZTc0ZjI4OGE4MmQyYjYxZjI3MmU1In19fQ=="), "&3培根", new String[] { "", "§7恢复 §b1.5 §7饥饿值§8和§7精神值" }), "BACON", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.GRILLED_PORK), null, null, null, null, null, null, null, null }, 3))
/*      */ 
/*      */ 
/*      */       
/*  466 */       .register();
/*      */     
/*  468 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTE0MjE2ZDEwNzE0MDgyYmJlM2Y0MTI0MjNlNmIxOTIzMjM1MmY0ZDY0ZjlhY2EzOTEzY2I0NjMxOGQzZWQifX19"), "&3三明治", new String[] { "", "§7恢复 §b9.5 §7饥饿值§8和§7精神值" }), "SANDWICH", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.BREAD), 
/*      */           
/*  470 */           getItem("MAYO"), new ItemStack(Material.COOKED_BEEF), getItem("TOMATO"), getItem("LETTUCE"), null, null, null, null }, 19))
/*      */       
/*  472 */       .register();
/*      */     
/*  474 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTE0MjE2ZDEwNzE0MDgyYmJlM2Y0MTI0MjNlNmIxOTIzMjM1MmY0ZDY0ZjlhY2EzOTEzY2I0NjMxOGQzZWQifX19"), "&3BLT三明治", new String[] { "", "§7恢复 §b9.0 §7饥饿值§8和§7精神值" }), "BLT", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.BREAD), new ItemStack(Material.GRILLED_PORK), 
/*      */           
/*  476 */           getItem("TOMATO"), getItem("LETTUCE"), null, null, null, null, null }, 18))
/*      */       
/*  478 */       .register();
/*      */     
/*  480 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTE0MjE2ZDEwNzE0MDgyYmJlM2Y0MTI0MjNlNmIxOTIzMjM1MmY0ZDY0ZjlhY2EzOTEzY2I0NjMxOGQzZWQifX19"), "&3鲜蔬鸡肉三明治", new String[] { "", "§7恢复 §b6.5 §7饥饿值§8和§7精神值" }), "LEAFY_CHICKEN_SANDWICH", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  482 */           getItem("CHICKEN_SANDWICH"), getItem("LETTUCE"), null, null, null, null, null, null, null }, 1))
/*      */       
/*  484 */       .register();
/*      */     
/*  486 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTE0MjE2ZDEwNzE0MDgyYmJlM2Y0MTI0MjNlNmIxOTIzMjM1MmY0ZDY0ZjlhY2EzOTEzY2I0NjMxOGQzZWQifX19"), "&3时蔬鲜鱼三明治", new String[] { "", "§7恢复 §b6.5 §7饥饿值§8和§7精神值" }), "LEAFY_FISH_SANDWICH", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  488 */           getItem("FISH_SANDWICH"), getItem("LETTUCE"), null, null, null, null, null, null, null }, 11))
/*      */       
/*  490 */       .register();
/*      */     
/*  492 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RhZGYxNzQ0NDMzZTFjNzlkMWQ1OWQyNzc3ZDkzOWRlMTU5YTI0Y2Y1N2U4YTYxYzgyYmM0ZmUzNzc3NTUzYyJ9fX0="), "&3汉堡", new String[] { "", "§7恢复 §b5.0 §7饥饿值§8和§7精神值" }), "HAMBURGER", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.BREAD), new ItemStack(Material.COOKED_BEEF), null, null, null, null, null, null, null }, 10))
/*      */ 
/*      */ 
/*      */       
/*  496 */       .register();
/*      */     
/*  498 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RhZGYxNzQ0NDMzZTFjNzlkMWQ1OWQyNzc3ZDkzOWRlMTU5YTI0Y2Y1N2U4YTYxYzgyYmM0ZmUzNzc3NTUzYyJ9fX0="), "&e芝士汉堡", new String[] { "", "§7恢复 §b6.5 §7饥饿值§8和§7精神值" }), "CHEESEBURGER", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  500 */           getItem("HAMBURGER"), SlimefunItems.CHEESE, null, null, null, null, null, null, null }, 13))
/*      */       
/*  502 */       .register();
/*      */     
/*  504 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RhZGYxNzQ0NDMzZTFjNzlkMWQ1OWQyNzc3ZDkzOWRlMTU5YTI0Y2Y1N2U4YTYxYzgyYmM0ZmUzNzc3NTUzYyJ9fX0="), "&e培根芝士汉堡", new String[] { "", "§7恢复 §b8.5 §7饥饿值§8和§7精神值" }), "BACON_CHEESEBURGER", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  506 */           getItem("CHEESEBURGER"), getItem("BACON"), null, null, null, null, null, null, null }, 17))
/*      */       
/*  508 */       .register();
/*      */     
/*  510 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RhZGYxNzQ0NDMzZTFjNzlkMWQ1OWQyNzc3ZDkzOWRlMTU5YTI0Y2Y1N2U4YTYxYzgyYmM0ZmUzNzc3NTUzYyJ9fX0="), "&e豪华芝士汉堡", new String[] { "", "§7恢复 §b8.0 §7饥饿值§8和§7精神值" }), "DELUXE_CHEESEBURGER", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  512 */           getItem("CHEESEBURGER"), getItem("LETTUCE"), getItem("TOMATO"), null, null, null, null, null, null }, 16))
/*      */       
/*  514 */       .register();
/*      */     
/*  516 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjkxMzY1MTRmMzQyZTdjNTIwOGExNDIyNTA2YTg2NjE1OGVmODRkMmIyNDkyMjAxMzllOGJmNjAzMmUxOTMifX19"), "&c胡萝卜蛋糕", new String[] { "", "§7恢复 §b6.0 §7饥饿值§8和§7精神值" }), "CARROT_CAKE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.CARROT_ITEM), SlimefunItems.WHEAT_FLOUR, new ItemStack(Material.SUGAR), new ItemStack(Material.EGG), null, null, null, null, null }, 12))
/*      */ 
/*      */ 
/*      */       
/*  520 */       .register();
/*      */     
/*  522 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RhZGYxNzQ0NDMzZTFjNzlkMWQ1OWQyNzc3ZDkzOWRlMTU5YTI0Y2Y1N2U4YTYxYzgyYmM0ZmUzNzc3NTUzYyJ9fX0="), "&3鸡肉汉堡", new String[] { "", "§7恢复 §b5.0 §7饥饿值§8和§7精神值" }), "CHICKEN_BURGER", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.BREAD), new ItemStack(Material.COOKED_CHICKEN), null, null, null, null, null, null, null }, 10))
/*      */ 
/*      */ 
/*      */       
/*  526 */       .register();
/*      */     
/*  528 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RhZGYxNzQ0NDMzZTFjNzlkMWQ1OWQyNzc3ZDkzOWRlMTU5YTI0Y2Y1N2U4YTYxYzgyYmM0ZmUzNzc3NTUzYyJ9fX0="), "&e鸡肉芝士汉堡", new String[] { "", "§7恢复 §b6.5 §7饥饿值§8和§7精神值" }), "CHICKEN_CHEESEBURGER", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  530 */           getItem("CHICKEN_BURGER"), SlimefunItems.CHEESE, null, null, null, null, null, null, null }, 13))
/*      */       
/*  532 */       .register();
/*      */     
/*  534 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RhZGYxNzQ0NDMzZTFjNzlkMWQ1OWQyNzc3ZDkzOWRlMTU5YTI0Y2Y1N2U4YTYxYzgyYmM0ZmUzNzc3NTUzYyJ9fX0="), "&c培根汉堡", new String[] { "", "§7恢复 §b5.0 §7饥饿值§8和§7精神值" }), "BACON_BURGER", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.BREAD), 
/*      */           
/*  536 */           getItem("BACON"), null, null, null, null, null, null, null }, 10))
/*      */       
/*  538 */       .register();
/*      */     
/*  540 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTE0MjE2ZDEwNzE0MDgyYmJlM2Y0MTI0MjNlNmIxOTIzMjM1MmY0ZDY0ZjlhY2EzOTEzY2I0NjMxOGQzZWQifX19"), "&c培根三明治", new String[] { "", "§7恢复 §b9.5 §7饥饿值§8和§7精神值" }), "BACON_SANDWICH", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.BREAD), 
/*      */           
/*  542 */           getItem("BACON"), getItem("MAYO"), getItem("TOMATO"), getItem("LETTUCE"), null, null, null, null }, 19))
/*      */       
/*  544 */       .register();
/*      */     
/*  546 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOThjZWQ3NGEyMjAyMWE1MzVmNmJjZTIxYzhjNjMyYjI3M2RjMmQ5NTUyYjcxYTM4ZDU3MjY5YjM1MzhjZiJ9fX0="), "&e墨西哥玉米卷", new String[] { "", "§7恢复 §b9.0 §7饥饿值§8和§7精神值" }), "TACO", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  548 */           getItem("CORNMEAL"), new ItemStack(Material.COOKED_BEEF), getItem("LETTUCE"), getItem("TOMATO"), getItem("CHEESE"), null, null, null, null }, 18))
/*      */       
/*  550 */       .register();
/*      */     
/*  552 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOThjZWQ3NGEyMjAyMWE1MzVmNmJjZTIxYzhjNjMyYjI3M2RjMmQ5NTUyYjcxYTM4ZDU3MjY5YjM1MzhjZiJ9fX0="), "&3鲜鱼玉米卷", new String[] { "", "§7恢复 §b9.0 §7饥饿值§8和§7精神值" }), "FISH_TACO", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  554 */           getItem("CORNMEAL"), new ItemStack(Material.COOKED_FISH), getItem("LETTUCE"), getItem("TOMATO"), getItem("CHEESE"), null, null, null, null }, 18))
/*      */       
/*  556 */       .register();
/*      */     
/*  558 */     (new CustomFood(category_food, (ItemStack)new CustomItem(Material.COOKIE, "&c果酱夹心饼", 0, new String[] { "", "§7恢复 §b5.0 §7饥饿值§8和§7精神值" }), "JAMMY_DODGER", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { null, 
/*  559 */           getItem("BISCUIT"), null, null, getItem("RASPBERRY_JUICE"), null, null, getItem("BISCUIT"), null }, 8))
/*      */       
/*  561 */       .register();
/*      */     
/*  563 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQ3ZjRmNWE3NGM2NjkxMjgwY2Q4MGU3MTQ4YjQ5YjJjZTE3ZGNmNjRmZDU1MzY4NjI3ZjVkOTJhOTc2YTZhOCJ9fX0="), "&e薄煎饼", new String[] { "", "§7恢复 §b6.0 §7饥饿值§8和§7精神值" }), "PANCAKES", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  565 */           getItem("WHEAT_FLOUR"), new ItemStack(Material.SUGAR), getItem("BUTTER"), new ItemStack(Material.EGG), new ItemStack(Material.EGG), null, null, null, null }, 12))
/*      */       
/*  567 */       .register();
/*      */     
/*  569 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQ3ZjRmNWE3NGM2NjkxMjgwY2Q4MGU3MTQ4YjQ5YjJjZTE3ZGNmNjRmZDU1MzY4NjI3ZjVkOTJhOTc2YTZhOCJ9fX0="), "&b蓝莓煎饼", new String[] { "", "§7恢复 §b6.5 §7饥饿值§8和§7精神值" }), "BLUEBERRY_PANCAKES", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  571 */           getItem("PANCAKES"), getItem("BLUEBERRY"), null, null, null, null, null, null, null }, 13))
/*      */       
/*  573 */       .register();
/*      */     
/*  575 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.POTATO_ITEM, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTYzYjhhZWFmMWRmMTE0ODhlZmM5YmQzMDNjMjMzYTg3Y2NiYTNiMzNmN2ZiYTljMmZlY2FlZTk1NjdmMDUzIn19fQ=="), "&e炸薯条", new String[] { "", "§7恢复 §b6.0 §7饥饿值§8和§7精神值" }), "FRIES", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.POTATO_ITEM), 
/*      */           
/*  577 */           getItem("SALT"), null, null, null, null, null, null, null }, 12))
/*      */       
/*  579 */       .register();
/*      */     
/*  581 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.POTATO_ITEM, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTQ5N2IxNDdjZmFlNTIyMDU1OTdmNzJlM2M0ZWY1MjUxMmU5Njc3MDIwZTRiNGZhNzUxMmMzYzZhY2RkOGMxIn19fQ=="), "&e爆米花", new String[] { "", "§7恢复 §b4.0 §7饥饿值§8和§7精神值" }), "POPCORN", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  583 */           getItem("CORN"), getItem("BUTTER"), null, null, null, null, null, null, null }, 8))
/*      */       
/*  585 */       .register();
/*      */     
/*  587 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.POTATO_ITEM, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTQ5N2IxNDdjZmFlNTIyMDU1OTdmNzJlM2M0ZWY1MjUxMmU5Njc3MDIwZTRiNGZhNzUxMmMzYzZhY2RkOGMxIn19fQ=="), "&e爆米花 &7(甜)", new String[] { "", "§7恢复 §b6.0 §7饥饿值§8和§7精神值" }), "SWEET_POPCORN", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  589 */           getItem("CORN"), getItem("BUTTER"), new ItemStack(Material.SUGAR), null, null, null, null, null, null }, 12))
/*      */       
/*  591 */       .register();
/*      */     
/*  593 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.POTATO_ITEM, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTQ5N2IxNDdjZmFlNTIyMDU1OTdmNzJlM2M0ZWY1MjUxMmU5Njc3MDIwZTRiNGZhNzUxMmMzYzZhY2RkOGMxIn19fQ=="), "&e爆米花 &7(咸)", new String[] { "", "§7恢复 §b6.0 §7饥饿值§8和§7精神值" }), "SALTY_POPCORN", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  595 */           getItem("CORN"), getItem("BUTTER"), getItem("SALT"), null, null, null, null, null, null }, 12))
/*      */       
/*  597 */       .register();
/*      */     
/*  599 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQxOGM2YjBhMjlmYzFmZTc5MWM4OTc3NGQ4MjhmZjYzZDJhOWZhNmM4MzM3M2VmM2FhNDdiZjNlYjc5In19fQ=="), "&e牧羊人派", new String[] { "", "§7恢复 §b8.0 §7饥饿值§8和§7精神值" }), "SHEPARDS_PIE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  601 */           getItem("CABBAGE"), new ItemStack(Material.CARROT_ITEM), SlimefunItems.WHEAT_FLOUR, new ItemStack(Material.COOKED_BEEF), getItem("TOMATO"), null, null, null, null }, 16))
/*      */       
/*  603 */       .register();
/*      */     
/*  605 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQxOGM2YjBhMjlmYzFmZTc5MWM4OTc3NGQ4MjhmZjYzZDJhOWZhNmM4MzM3M2VmM2FhNDdiZjNlYjc5In19fQ=="), "&e鸡肉派", new String[] { "", "§7恢复 §b8.5 §7饥饿值§8和§7精神值" }), "CHICKEN_POT_PIE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.COOKED_CHICKEN), new ItemStack(Material.CARROT_ITEM), SlimefunItems.WHEAT_FLOUR, new ItemStack(Material.POTATO_ITEM), null, null, null, null, null }, 17))
/*      */ 
/*      */ 
/*      */       
/*  609 */       .register();
/*      */     
/*  611 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTExOWZjYTRmMjhhNzU1ZDM3ZmJlNWRjZjZkOGMzZWY1MGZlMzk0YzFhNzg1MGJjN2UyYjcxZWU3ODMwM2M0YyJ9fX0="), "&c巧克力蛋糕", new String[] { "", "§7恢复 §b8.5 §7饥饿值§8和§7精神值" }), "CHOCOLATE_CAKE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  613 */           getItem("CHOCOLATE_BAR"), new ItemStack(Material.SUGAR), SlimefunItems.WHEAT_FLOUR, SlimefunItems.BUTTER, new ItemStack(Material.EGG), null, null, null, null }, 17))
/*      */       
/*  615 */       .register();
/*      */     
/*  617 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.COOKIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGZkNzFlMjBmYzUwYWJmMGRlMmVmN2RlY2ZjMDFjZTI3YWQ1MTk1NTc1OWUwNzJjZWFhYjk2MzU1ZjU5NGYwIn19fQ=="), "&r奶油曲奇", new String[] { "", "§7恢复 §b6.0 §7饥饿值§8和§7精神值" }), "CREAM_COOKIE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  619 */           getItem("CHOCOLATE_BAR"), new ItemStack(Material.SUGAR), SlimefunItems.WHEAT_FLOUR, SlimefunItems.BUTTER, SlimefunItems.HEAVY_CREAM, null, null, null, null }, 12))
/*      */       
/*  621 */       .register();
/*      */     
/*  623 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODM3OTRjNzM2ZmM3NmU0NTcwNjgzMDMyNWI5NTk2OTQ2NmQ4NmY4ZDdiMjhmY2U4ZWRiMmM3NWUyYWIyNWMifX19"), "&b蓝莓玛芬", new String[] { "", "§7恢复 §b6.5 §7饥饿值§8和§7精神值" }), "BLUEBERRY_MUFFIN", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  625 */           getItem("BLUEBERRY"), new ItemStack(Material.SUGAR), SlimefunItems.WHEAT_FLOUR, SlimefunItems.BUTTER, SlimefunItems.HEAVY_CREAM, new ItemStack(Material.EGG), null, null, null }, 13))
/*      */       
/*  627 */       .register();
/*      */     
/*  629 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODM3OTRjNzM2ZmM3NmU0NTcwNjgzMDMyNWI5NTk2OTQ2NmQ4NmY4ZDdiMjhmY2U4ZWRiMmM3NWUyYWIyNWMifX19"), "&e南瓜玛芬", new String[] { "", "§7恢复 §b6.5 §7饥饿值§8和§7精神值" }), "PUMPKIN_MUFFIN", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.PUMPKIN), new ItemStack(Material.SUGAR), SlimefunItems.WHEAT_FLOUR, SlimefunItems.BUTTER, SlimefunItems.HEAVY_CREAM, new ItemStack(Material.EGG), null, null, null }, 13))
/*      */ 
/*      */ 
/*      */       
/*  633 */       .register();
/*      */     
/*  635 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODM3OTRjNzM2ZmM3NmU0NTcwNjgzMDMyNWI5NTk2OTQ2NmQ4NmY4ZDdiMjhmY2U4ZWRiMmM3NWUyYWIyNWMifX19"), "&c巧克力薄片玛芬", new String[] { "", "§7恢复 §b6.5 §7饥饿值§8和§7精神值" }), "CHOCOLATE_CHIP_MUFFIN", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  637 */           getItem("CHOCOLATE_BAR"), new ItemStack(Material.SUGAR), SlimefunItems.WHEAT_FLOUR, SlimefunItems.BUTTER, SlimefunItems.HEAVY_CREAM, new ItemStack(Material.EGG), null, null, null }, 13))
/*      */       
/*  639 */       .register();
/*      */     
/*  641 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGZkNzFlMjBmYzUwYWJmMGRlMmVmN2RlY2ZjMDFjZTI3YWQ1MTk1NTc1OWUwNzJjZWFhYjk2MzU1ZjU5NGYwIn19fQ=="), "&r波士顿奶油派", new String[] { "", "§7恢复 §b4.5 §7饥饿值§8和§7精神值" }), "BOSTON_CREAM_PIE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { null, 
/*      */           
/*  643 */           getItem("CHOCOLATE_BAR"), null, null, SlimefunItems.HEAVY_CREAM, null, null, getItem("BISCUIT"), null }, 9))
/*      */       
/*  645 */       .register();
/*      */     
/*  647 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzNmMmQ3ZDdhOGIxYjk2OTE0Mjg4MWViNWE4N2U3MzdiNWY3NWZiODA4YjlhMTU3YWRkZGIyYzZhZWMzODIifX19"), "&c香肠", new String[] { "", "§7恢复 §b5.0 §7饥饿值§8和§7精神值" }), "HOT_DOG", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { null, null, null, null, new ItemStack(Material.GRILLED_PORK), null, null, new ItemStack(Material.BREAD), null }, 10))
/*      */ 
/*      */ 
/*      */       
/*  651 */       .register();
/*      */     
/*  653 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzNmMmQ3ZDdhOGIxYjk2OTE0Mjg4MWViNWE4N2U3MzdiNWY3NWZiODA4YjlhMTU3YWRkZGIyYzZhZWMzODIifX19"), "&c培根芝士香肠", new String[] { "", "§7恢复 §b8.5 §7饥饿值§8和§7精神值" }), "BACON_WRAPPED_CHEESE_FILLED_HOT_DOG", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  655 */           getItem("BACON"), getItem("HOT_DOG"), getItem("BACON"), null, getItem("CHEESE"), null, null, null, null }, 17))
/*      */       
/*  657 */       .register();
/*      */     
/*  659 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzNmMmQ3ZDdhOGIxYjk2OTE0Mjg4MWViNWE4N2U3MzdiNWY3NWZiODA4YjlhMTU3YWRkZGIyYzZhZWMzODIifX19"), "&c烤肉培根香肠", new String[] { "", "§7恢复 §b8.5 §7饥饿值§8和§7精神值" }), "BBQ_BACON_WRAPPED_HOT_DOG", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  661 */           getItem("BACON"), getItem("HOT_DOG"), getItem("BACON"), null, getItem("BBQ_SAUCE"), null, null, null, null }, 17))
/*      */       
/*  663 */       .register();
/*      */     
/*  665 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzNmMmQ3ZDdhOGIxYjk2OTE0Mjg4MWViNWE4N2U3MzdiNWY3NWZiODA4YjlhMTU3YWRkZGIyYzZhZWMzODIifX19"), "&c双重烤肉培根香肠", new String[] { "", "§7恢复 §b10.0 §7饥饿值§8和§7精神值" }), "BBQ_DOUBLE_BACON_WRAPPED_HOT_DOG_IN_A_TORTILLA_WITH_CHEESE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  667 */           getItem("BACON"), getItem("BBQ_SAUCE"), getItem("BACON"), getItem("BACON"), new ItemStack(Material.GRILLED_PORK), getItem("BACON"), getItem("CORNMEAL"), getItem("CHEESE"), getItem("CORNMEAL") }, 20))
/*      */       
/*  669 */       .register();
/*      */     
/*  671 */     (new CustomFood(category_drinks, (ItemStack)new CustomItem(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDhlOTRkZGQ3NjlhNWJlYTc0ODM3NmI0ZWM3MzgzZmQzNmQyNjc4OTRkN2MzYmVlMDExZThlNGY1ZmNkNyJ9fX0="), "&a甜茶", new String[] { "", "§7恢复 §b3.0 §7饥饿值§8和§7精神值" }), "SWEETENED_TEA", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  673 */           getItem("TEA_LEAF"), new ItemStack(Material.SUGAR), null, null, null, null, null, null, null }, 6))
/*      */       
/*  675 */       .register();
/*      */     
/*  677 */     (new CustomFood(category_drinks, (ItemStack)new CustomItem(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDExNTExYmRkNTViY2I4MjgwM2M4MDM5ZjFjMTU1ZmQ0MzA2MjYzNmUyM2Q0ZDQ2YzRkNzYxYzA0ZDIyYzIifX19"), "&6热巧克力", new String[] { "", "§7恢复 §b4.0 §7饥饿值§8和§7精神值" }), "HOT_CHOCOLATE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  679 */           getItem("CHOCOLATE_BAR"), SlimefunItems.HEAVY_CREAM, null, null, null, null, null, null, null }, 8))
/*      */       
/*  681 */       .register();
/*      */     
/*  683 */     (new CustomFood(category_drinks, (ItemStack)new CustomItem(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmE4ZjFmNzBlODU4MjU2MDdkMjhlZGNlMWEyYWQ0NTA2ZTczMmI0YTUzNDVhNWVhNmU4MDdjNGIzMTNlODgifX19"), "&6椰林飘香", new String[] { "", "§7恢复 §b7.0 §7饥饿值§8和§7精神值" }), "PINACOLADA", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  685 */           getItem("PINEAPPLE"), getItem("ICE_CUBE"), getItem("COCONUT_MILK"), null, null, null, null, null, null }, 14))
/*      */       
/*  687 */       .register();
/*      */     
/*  689 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.NETHER_STALK, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmQ0ZWQ3YzczYWMyODUzZGZjYWE5Y2E3ODlmYjE4ZGExZDQ3YjE3YWQ2OGIyZGE3NDhkYmQxMWRlMWE0OWVmIn19fQ=="), "&c巧克力脆皮草莓", new String[] { "", "§7恢复 §b2.5 §7饥饿值§8和§7精神值" }), "CHOCOLATE_STRAWBERRY", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  691 */           getItem("CHOCOLATE_BAR"), getItem("STRAWBERRY"), null, null, null, null, null, null, null }, 5))
/*      */       
/*  693 */       .register();
/*      */     
/*  695 */     (new CustomFood(category_drinks, (ItemStack)new CustomPotion("&e柠檬水", 8227, new String[] { "", "§7恢复 §b3.0 §7饥饿值§8和§7精神值" }, new PotionEffect(PotionEffectType.SATURATION, 6, 0)), "LEMONADE", RecipeType.JUICER, new ItemStack[] {
/*  696 */           getItem("LEMON_JUICE"), new ItemStack(Material.SUGAR), null, null, null, null, null, null, null }, 3))
/*  697 */       .register();
/*      */     
/*  699 */     new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQxOGM2YjBhMjlmYzFmZTc5MWM4OTc3NGQ4MjhmZjYzZDJhOWZhNmM4MzM3M2VmM2FhNDdiZjNlYjc5In19fQ=="), "&c番薯派", new String[] { "", "§7恢复 §b6.5 §7饥饿值§8和§7精神值" }), "SWEET_POTATO_PIE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  701 */           getItem("SWEET_POTATO"), new ItemStack(Material.EGG), SlimefunItems.HEAVY_CREAM, SlimefunItems.WHEAT_FLOUR, null, null, null, null, null }, 13);
/*      */ 
/*      */     
/*  704 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTExOWZjYTRmMjhhNzU1ZDM3ZmJlNWRjZjZkOGMzZWY1MGZlMzk0YzFhNzg1MGJjN2UyYjcxZWU3ODMwM2M0YyJ9fX0="), "&r巧克力椰丝蛋糕", new String[] { "", "§7恢复 §b9.0 §7饥饿值§8和§7精神值" }), "LAMINGTON", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  706 */           getItem("CHOCOLATE_BAR"), new ItemStack(Material.SUGAR), SlimefunItems.WHEAT_FLOUR, SlimefunItems.BUTTER, getItem("COCONUT"), null, null, null, null }, 18))
/*      */       
/*  708 */       .register();
/*      */     
/*  710 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQ3ZjRmNWE3NGM2NjkxMjgwY2Q4MGU3MTQ4YjQ5YjJjZTE3ZGNmNjRmZDU1MzY4NjI3ZjVkOTJhOTc2YTZhOCJ9fX0="), "&e华夫饼", new String[] { "", "§7恢复 §b6.0 §7饥饿值§8和§7精神值" }), "WAFFLES", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  712 */           getItem("WHEAT_FLOUR"), new ItemStack(Material.EGG), new ItemStack(Material.SUGAR), getItem("BUTTER"), null, null, null, null, null }, 12))
/*      */       
/*  714 */       .register();
/*      */     
/*  716 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTE0MjE2ZDEwNzE0MDgyYmJlM2Y0MTI0MjNlNmIxOTIzMjM1MmY0ZDY0ZjlhY2EzOTEzY2I0NjMxOGQzZWQifX19"), "&e俱乐部三明治", new String[] { "", "§7恢复 §b9.5 §7饥饿值§8和§7精神值" }), "CLUB_SANDWICH", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.BREAD), 
/*      */           
/*  718 */           getItem("MAYO"), getItem("BACON"), getItem("TOMATO"), getItem("LETTUCE"), getItem("MUSTARD"), null, null, null }, 19))
/*      */       
/*  720 */       .register();
/*      */     
/*  722 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTM4N2E2MjFlMjY2MTg2ZTYwNjgzMzkyZWIyNzRlYmIyMjViMDQ4NjhhYjk1OTE3N2Q5ZGMxODFkOGYyODYifX19"), "&e卷饼", new String[] { "", "§7恢复 §b9.0 §7饥饿值§8和§7精神值" }), "BURRITO", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  724 */           getItem("CORNMEAL"), new ItemStack(Material.COOKED_BEEF), getItem("LETTUCE"), getItem("TOMATO"), getItem("HEAVY_CREAM"), getItem("CHEESE"), null, null, null }, 18))
/*      */       
/*  726 */       .register();
/*      */     
/*  728 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTM4N2E2MjFlMjY2MTg2ZTYwNjgzMzkyZWIyNzRlYmIyMjViMDQ4NjhhYjk1OTE3N2Q5ZGMxODFkOGYyODYifX19"), "&e鸡肉卷饼", new String[] { "", "§7恢复 §b9.0 §7饥饿值§8和§7精神值" }), "CHICKEN_BURRITO", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  730 */           getItem("CORNMEAL"), new ItemStack(Material.COOKED_CHICKEN), getItem("LETTUCE"), getItem("TOMATO"), getItem("HEAVY_CREAM"), getItem("CHEESE"), null, null, null }, 18))
/*      */       
/*  732 */       .register();
/*      */     
/*  734 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmFlZTg0ZDE5Yzg1YWZmNzk2Yzg4YWJkYTIxZWM0YzkyYzY1NWUyZDY3YjcyZTVlNzdiNWFhNWU5OWVkIn19fQ=="), "&c烧烤三明治", new String[] { "", "§7恢复 §b5.5 §7饥饿值§8和§7精神值" }), "GRILLED_SANDWICH", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.BREAD), new ItemStack(Material.GRILLED_PORK), 
/*      */           
/*  736 */           getItem("CHEESE"), null, null, null, null, null, null }, 11))
/*      */       
/*  738 */       .register();
/*      */     
/*  740 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMDNhMzU3NGE4NDhmMzZhZTM3MTIxZTkwNThhYTYxYzEyYTI2MWVlNWEzNzE2ZjZkODI2OWUxMWUxOWUzNyJ9fX0="), "&c千层面", new String[] { "", "§7恢复 §b8.5 §7饥饿值§8和§7精神值" }), "LASAGNA", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  742 */           getItem("TOMATO"), getItem("CHEESE"), SlimefunItems.WHEAT_FLOUR, getItem("TOMATO"), getItem("CHEESE"), new ItemStack(Material.COOKED_BEEF), null, null, null }, 17))
/*      */       
/*  744 */       .register();
/*      */     
/*  746 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.SNOW_BALL, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTUzNjZjYTE3OTc0ODkyZTRmZDRjN2I5YjE4ZmViMTFmMDViYTJlYzQ3YWE1MDM1YzgxYTk1MzNiMjgifX19"), "&r冰激凌", new String[] { "", "§7恢复 §b8.0 §7饥饿值§8和§7精神值" }), "ICE_CREAM", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  748 */           getItem("HEAVY_CREAM"), getItem("ICE_CUBE"), new ItemStack(Material.SUGAR), (new MaterialData(Material.INK_SACK, (byte)3)).toItemStack(1), getItem("STRAWBERRY"), null, null, null, null }16))
/*      */       
/*  750 */       .register();
/*      */     
/*  752 */     (new CustomFood(category_drinks, (ItemStack)new CustomPotion("§6菠萝汁", 8195, new String[] { "", "§7恢复 §b3.0 §7饥饿值§8和§7精神值" }, new PotionEffect(PotionEffectType.SATURATION, 6, 0)), "PINEAPPLE_JUICE", RecipeType.JUICER, new ItemStack[] {
/*  753 */           getItem("PINEAPPLE"), null, null, null, null, null, null, null, null }, 3))
/*  754 */       .register();
/*      */     
/*  756 */     (new CustomFood(category_drinks, (ItemStack)new CustomPotion("§6菠萝冰沙", 8195, new String[] { "", "§7恢复 §b5.0 §7饥饿值§8和§7精神值" }, new PotionEffect(PotionEffectType.SATURATION, 10, 0)), "PINEAPPLE_SMOOTHIE", RecipeType.JUICER, new ItemStack[] {
/*  757 */           getItem("PINEAPPLE_JUICE"), getItem("ICE_CUBE"), null, null, null, null, null, null, null }, 5))
/*  758 */       .register();
/*      */     
/*  760 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.SNOW_BALL, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTY5MDkxZDI4ODAyMmM3YjBlYjZkM2UzZjQ0YjBmZWE3ZjJjMDY5ZjQ5NzQ5MWExZGNhYjU4N2ViMWQ1NmQ0In19fQ=="), "&r提拉米苏", new String[] { "", "§7恢复 §b8.0 §7饥饿值§8和§7精神值" }), "TIRAMISU", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  762 */           getItem("HEAVY_CREAM"), new ItemStack(Material.EGG), new ItemStack(Material.SUGAR), (new MaterialData(Material.INK_SACK, (byte)3)).toItemStack(1), new ItemStack(Material.EGG), null, null, null, null }16))
/*      */       
/*  764 */       .register();
/*      */     
/*  766 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.SNOW_BALL, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTY5MDkxZDI4ODAyMmM3YjBlYjZkM2UzZjQ0YjBmZWE3ZjJjMDY5ZjQ5NzQ5MWExZGNhYjU4N2ViMWQ1NmQ0In19fQ=="), "&c草莓提拉米苏", new String[] { "", "§7恢复 §b9.0 §7饥饿值§8和§7精神值" }), "TIRAMISU_WITH_STRAWBERRIES", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  768 */           getItem("HEAVY_CREAM"), getItem("STRAWBERRY"), null, null, null, null, null, null, null }, 18))
/*      */       
/*  770 */       .register();
/*      */     
/*  772 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.SNOW_BALL, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTY5MDkxZDI4ODAyMmM3YjBlYjZkM2UzZjQ0YjBmZWE3ZjJjMDY5ZjQ5NzQ5MWExZGNhYjU4N2ViMWQ1NmQ0In19fQ=="), "&c覆盆子提拉米苏", new String[] { "", "§7恢复 §b9.0 §7饥饿值§8和§7精神值" }), "TIRAMISU_WITH_RASPBERRIES", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  774 */           getItem("HEAVY_CREAM"), getItem("RASPBERRY"), null, null, null, null, null, null, null }, 18))
/*      */       
/*  776 */       .register();
/*      */     
/*  778 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.SNOW_BALL, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTY5MDkxZDI4ODAyMmM3YjBlYjZkM2UzZjQ0YjBmZWE3ZjJjMDY5ZjQ5NzQ5MWExZGNhYjU4N2ViMWQ1NmQ0In19fQ=="), "&7黑莓提拉米苏", new String[] { "", "§7恢复 §b9.0 §7饥饿值§8和§7精神值" }), "TIRAMISU_WITH_BLACKBERRIES", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  780 */           getItem("HEAVY_CREAM"), getItem("BLACKBERRY"), null, null, null, null, null, null, null }, 18))
/*      */       
/*  782 */       .register();
/*      */     
/*  784 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTExOWZjYTRmMjhhNzU1ZDM3ZmJlNWRjZjZkOGMzZWY1MGZlMzk0YzFhNzg1MGJjN2UyYjcxZWU3ODMwM2M0YyJ9fX0="), "&e巧克力香梨蛋糕", new String[] { "", "§7恢复 §b9.5 §7饥饿值§8和§7精神值" }), "CHOCOLATE_PEAR_CAKE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  786 */           getItem("CHOCOLATE_BAR"), new ItemStack(Material.SUGAR), SlimefunItems.WHEAT_FLOUR, SlimefunItems.BUTTER, getItem("PEAR"), new ItemStack(Material.EGG), null, null, null }, 19))
/*      */       
/*  788 */       .register();
/*      */     
/*  790 */     (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQxOGM2YjBhMjlmYzFmZTc5MWM4OTc3NGQ4MjhmZjYzZDJhOWZhNmM4MzM3M2VmM2FhNDdiZjNlYjc5In19fQ=="), "&c苹果香梨蛋糕", new String[] { "", "§7恢复 §b9.0 §7饥饿值§8和§7精神值" }), "APPLE_PEAR_CAKE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*      */           
/*  792 */           getItem("APPLE"), new ItemStack(Material.SUGAR), SlimefunItems.WHEAT_FLOUR, SlimefunItems.BUTTER, getItem("PEAR"), new ItemStack(Material.EGG), null, null, null }, 18))
/*      */       
/*  794 */       .register();
/*      */ 
/*      */     
/*  797 */     (new CustomWine(category_drinks, (ItemStack)new CustomItem(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2M2YjRhN2JkODI0NDE2MTliYmMxMWQ5YjhlMGU2NGFlOGI5NWYyZTQwYjM5MjEzNTVmY2M1NDM0MzI2MDE3In19fQ=="), "§3土烧", new String[] { "§8初级酒", "§7传统的土法制酒", "§7味道一般但胜在天然", "", "§7▷▷ §b酒精度: §e20", "§7▷▷ §d精神值: §e3", "§7▷▷ §a饱食度: §e2" }), "NORMAL_BREW", new RecipeType(ExoticItems.ElectricityBrewing_1), new ItemStack[] { ExoticItems.Yeast_1, new ItemStack(Material.WHEAT), new ItemStack(Material.WHEAT), null, null, null, null, null, null }, 2, 3.0F, 20))
/*      */       
/*  799 */       .register();
/*      */     
/*  801 */     (new CustomWine(category_drinks, (ItemStack)new CustomItem(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzZlMWExZjNjNzcxNTNhYmZlMzhjZjgyZWVmMzZhOGJmN2VjMzJjM2M0MTc1NzZiMDU5YTVmMmU2ZGI0YmY3In19fQ=="), "§3苹果酒", new String[] { "§8初级酒", "§7甜酸的苹果发酵而来", "§7是一种传统的果酒", "", "§7▷▷ §b酒精度: §e15", "§7▷▷ §d精神值: §e3", "§7▷▷ §a饱食度: §e4" }), "APPLE_WINE", new RecipeType(ExoticItems.ElectricityBrewing_1), new ItemStack[] { ExoticItems.Yeast_1, new ItemStack(Material.APPLE), new ItemStack(Material.SUGAR), null, null, null, null, null, null }, 4, 3.0F, 15))
/*      */       
/*  803 */       .register();
/*      */     
/*  805 */     (new CustomWine(category_drinks, (ItemStack)new CustomItem(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2M2YjRhN2JkODI0NDE2MTliYmMxMWQ5YjhlMGU2NGFlOGI5NWYyZTQwYjM5MjEzNTVmY2M1NDM0MzI2MDE3In19fQ=="), "§3格瓦斯", new String[] { "§8初级酒", "§7一种传统的东欧饮料", "§7由面包发酵而来", "", "§7▷▷ §b酒精度: §e10", "§7▷▷ §d精神值: §e3", "§7▷▷ §a饱食度: §e6" }), "BREAD_WINE", new RecipeType(ExoticItems.ElectricityBrewing_1), new ItemStack[] { ExoticItems.Yeast_1, new ItemStack(Material.BREAD), new ItemStack(Material.SUGAR), null, null, null, null, null, null }, 6, 3.0F, 10))
/*      */       
/*  807 */       .register();
/*      */     
/*  809 */     (new CustomWine(category_drinks, (ItemStack)new CustomItem(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjc2MmRmYjJmMjQ0YjU2NWVhZjY5Yzg1ZTkyNDY4M2E5ODU0MWVhODg2ZDkzZDFhMzA0NTEyYWEzZDM2NzY2MyJ9fX0="), "§3土豆酒", new String[] { "§8初级酒", "§7廉价易制的酒", "§7有一种特殊的香气", "", "§7▷▷ §b酒精度: §e20", "§7▷▷ §d精神值: §e3", "§7▷▷ §a饱食度: §e4" }), "POTATO_WINE", new RecipeType(ExoticItems.ElectricityBrewing_1), new ItemStack[] { ExoticItems.Yeast_1, new ItemStack(Material.POTATO_ITEM), new ItemStack(Material.SUGAR), null, null, null, null, null, null }, 4, 3.0F, 20))
/*      */       
/*  811 */       .register();
/*      */     
/*  813 */     (new CustomWine(category_drinks, (ItemStack)new CustomItem(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzZlMWExZjNjNzcxNTNhYmZlMzhjZjgyZWVmMzZhOGJmN2VjMzJjM2M0MTc1NzZiMDU5YTVmMmU2ZGI0YmY3In19fQ=="), "§3下界酒", new String[] { "§8初级酒", "§7来自下界的酿品", "§7饮用后有些其妙的感觉...", "", "§7▷▷ §b酒精度: §e25", "§7▷▷ §d精神值: §e3", "§7▷▷ §a饱食度: §e4" }), "NETHER_WINE", new RecipeType(ExoticItems.ElectricityBrewing_1), new ItemStack[] { ExoticItems.Yeast_1, new ItemStack(Material.NETHER_STALK), new ItemStack(Material.SUGAR), null, null, null, null, null, null }, 4, 3.0F, 25, new PotionEffect[] { new PotionEffect(PotionEffectType.SPEED, 200, 1)
/*      */ 
/*      */         
/*  816 */         })).register();
/*      */     
/*  818 */     (new CustomWine(category_drinks, (ItemStack)new CustomItem(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2M2YjRhN2JkODI0NDE2MTliYmMxMWQ5YjhlMGU2NGFlOGI5NWYyZTQwYjM5MjEzNTVmY2M1NDM0MzI2MDE3In19fQ=="), "§3酸奶酒", new String[] { "§8初级酒", "§7特殊的牛奶酿造品", "§7饮用后有些许的解酒功效", "", "§7▷▷ §b酒精度: §e-10", "§7▷▷ §d精神值: §e1", "§7▷▷ §a饱食度: §e8" }), "MILK_WINE", new RecipeType(ExoticItems.ElectricityBrewing_1), new ItemStack[] { ExoticItems.Yeast_1, new ItemStack(Material.MILK_BUCKET), new ItemStack(Material.SUGAR), null, null, null, null, null, null }, 8, 1.0F, -10, new PotionEffect[] { new PotionEffect(PotionEffectType.SPEED, 200, 1)
/*      */ 
/*      */         
/*  821 */         })).register();
/*      */     
/*  823 */     (new CustomWine(category_drinks, (ItemStack)new CustomItem(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGFmODE4ZjNmNGNjMmI3YzhlNzBmOGJlYWY4MGY3OWU5MjY1YjMzZmJmZDcxNzZjN2MzMzQ5ZDRiNzZiIn19fQ=="), "§3紫影酒", new String[] { "§8初级酒", "§7来自终末之地的酿品", "§7会让人失去几秒的疼痛感", "", "§7▷▷ §b酒精度: §e25", "§7▷▷ §d精神值: §e1", "§7▷▷ §a饱食度: §e4" }), "ENDER_WINE", new RecipeType(ExoticItems.ElectricityBrewing_1), new ItemStack[] { ExoticItems.Yeast_1, new ItemStack(Material.CHORUS_FRUIT), new ItemStack(Material.SUGAR), null, null, null, null, null, null }, 4, 3.0F, 25, new PotionEffect[] { new PotionEffect(PotionEffectType.ABSORPTION, 160, 1)
/*      */ 
/*      */         
/*  826 */         })).register();
/*      */ 
/*      */     
/*  829 */     (new CustomWine(category_drinks, (ItemStack)new CustomItem(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGFmODE4ZjNmNGNjMmI3YzhlNzBmOGJlYWY4MGY3OWU5MjY1YjMzZmJmZDcxNzZjN2MzMzQ5ZDRiNzZiIn19fQ=="), "§b二锅头", new String[] { "§8中级酒", "§7经典的蒸馏型白酒", "§7酒精度数很高", "", "§7▷▷ §b酒精度: §e65", "§7▷▷ §d精神值: §e15", "§7▷▷ §a饱食度: §e4" }), "WHITE_WINE", new RecipeType(ExoticItems.ElectricityBrewing_2), new ItemStack[] { ExoticItems.Yeast_2, 
/*      */           
/*  831 */           getItem("NORMAL_BREW"), new ItemStack(Material.WHEAT), null, null, null, null, null, null }, 4, 15.0F, 65, new PotionEffect[] { new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 240, 1)
/*  832 */         })).register();
/*      */     
/*  834 */     (new CustomWine(category_drinks, (ItemStack)new CustomItem(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjc2MmRmYjJmMjQ0YjU2NWVhZjY5Yzg1ZTkyNDY4M2E5ODU0MWVhODg2ZDkzZDFhMzA0NTEyYWEzZDM2NzY2MyJ9fX0="), "§b苹果醋", new String[] { "§8中级酒", "§7香甜的苹果酒继续发酵制成", "§7酸甜可口，几乎没有酒精度", "", "§7▷▷ §b酒精度: §e5", "§7▷▷ §d精神值: §e10", "§7▷▷ §a饱食度: §e10" }), "APPLE_VINEGAR", new RecipeType(ExoticItems.ElectricityBrewing_2), new ItemStack[] { ExoticItems.Yeast_2, 
/*      */           
/*  836 */           getItem("APPLE_WINE"), getItem("APPLE"), null, null, null, null, null, null }, 10, 10.0F, 5, new PotionEffect[] { new PotionEffect(PotionEffectType.LUCK, 200, 1)
/*  837 */         })).register();
/*      */     
/*  839 */     (new CustomWine(category_drinks, (ItemStack)new CustomItem(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzZlMWExZjNjNzcxNTNhYmZlMzhjZjgyZWVmMzZhOGJmN2VjMzJjM2M0MTc1NzZiMDU5YTVmMmU2ZGI0YmY3In19fQ=="), "§b赤炎酒", new String[] { "§8中级酒", "§7如火焰般的酿品", "§7非常辣口但却令人精神舒畅无惧烈焰", "", "§7▷▷ §b酒精度: §e60", "§7▷▷ §d精神值: §e20", "§7▷▷ §a饱食度: §e4" }), "FIRE_WINE", new RecipeType(ExoticItems.ElectricityBrewing_2), new ItemStack[] { ExoticItems.Yeast_2, 
/*      */           
/*  841 */           getItem("NETHER_WINE"), new ItemStack(Material.MAGMA_CREAM), null, null, null, null, null, null }, 4, 20.0F, 60, new PotionEffect[] { new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1200, 1)
/*  842 */         })).register();
/*      */     
/*  844 */     (new CustomWine(category_drinks, (ItemStack)new CustomItem(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzZlMWExZjNjNzcxNTNhYmZlMzhjZjgyZWVmMzZhOGJmN2VjMzJjM2M0MTc1NzZiMDU5YTVmMmU2ZGI0YmY3In19fQ=="), "§b葡萄酒", new String[] { "§8中级酒", "§7传统的葡萄酒", "§7酸甜中略微有一股特殊的苦涩味", "", "§7▷▷ §b酒精度: §e20", "§7▷▷ §d精神值: §e15", "§7▷▷ §a饱食度: §e4" }), "GRAPE_WINE", new RecipeType(ExoticItems.ElectricityBrewing_2), new ItemStack[] { ExoticItems.Yeast_2, 
/*      */           
/*  846 */           getItem("GRAPE"), getItem("GRAPE"), null, null, null, null, null, null }, 4, 15.0F, 20)).register();
/*      */     
/*  848 */     (new CustomWine(category_drinks, (ItemStack)new CustomItem(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjFiMzVmNzA3N2VjZjk4ZWYzZWJhMGYzNWQ5M2E5ODEzMDMwZjliOGI4ZTQyNmFlYjY4ZGFiMzhmMTExNiJ9fX0="), "§b玉米酒", new String[] { "§8中级酒", "§7富含淀粉的玉米发酵而来", "§7兼有玉米清香与酒的醇香", "", "§7▷▷ §b酒精度: §e20", "§7▷▷ §d精神值: §e15", "§7▷▷ §a饱食度: §e4" }), "CORN_WINE", new RecipeType(ExoticItems.ElectricityBrewing_2), new ItemStack[] { ExoticItems.Yeast_2, 
/*      */           
/*  850 */           getItem("CORN"), new ItemStack(Material.SUGAR), null, null, null, null, null, null }, 4, 15.0F, 25)).register();
/*      */     
/*  852 */     (new CustomWine(category_drinks, (ItemStack)new CustomItem(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjFiMzVmNzA3N2VjZjk4ZWYzZWJhMGYzNWQ5M2E5ODEzMDMwZjliOGI4ZTQyNmFlYjY4ZGFiMzhmMTExNiJ9fX0="), "§b黄酒", new String[] { "§8中级酒", "§7拥有特殊酱香味的酒", "§7既可以饮用也可以用于烹饪", "", "§7▷▷ §b酒精度: §e30", "§7▷▷ §d精神值: §e15", "§7▷▷ §a饱食度: §e4" }), "YELLOW_WINE", new RecipeType(ExoticItems.ElectricityBrewing_2), new ItemStack[] { ExoticItems.Yeast_2, 
/*      */           
/*  854 */           getItem("SWEET_POTATO"), new ItemStack(Material.SUGAR), null, null, null, null, null, null }, 4, 15.0F, 30)).register();
/*      */     
/*  856 */     (new CustomWine(category_drinks, (ItemStack)new CustomItem(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDA1M2UyNjg2N2JiNTc1MzhlOTc4OTEzN2RiYmI1Mzc3NGUxOGVkYTZmZWY1MWNiMmVkZjQyNmIzNzI2NCJ9fX0="), "§b淡啤酒", new String[] { "§8中级酒", "§7清淡的啤酒", "§7冰镇后风味更佳", "", "§7▷▷ §b酒精度: §e10", "§7▷▷ §d精神值: §e10", "§7▷▷ §a饱食度: §e2" }), "LIGHT_BEER", new RecipeType(ExoticItems.ElectricityBrewing_2), new ItemStack[] { ExoticItems.Yeast_2, 
/*      */           
/*  858 */           getItem("WINEFRUIT"), getItem("WINEFRUIT"), null, null, null, null, null, null }, 2, 10.0F, 10)).register();
/*      */     
/*  860 */     (new CustomWine(category_drinks, (ItemStack)new CustomItem(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjBjOGFhMTNlMTJhZjYxNWNiMzYyZjhhZjk0ZGQ1ZWEyNzgxODM5MDdmZTBhYmQ4NGQ2NWEwNzk5OTJkYTQifX19"), "§b啤酒", new String[] { "§8中级酒", "§7苦涩味略重的啤酒", "§7冰镇后风味更佳", "", "§7▷▷ §b酒精度: §e15", "§7▷▷ §d精神值: §e10", "§7▷▷ §a饱食度: §e4" }), "BEER", new RecipeType(ExoticItems.ElectricityBrewing_2), new ItemStack[] { ExoticItems.Yeast_2, 
/*      */           
/*  862 */           getItem("WINEFRUIT"), new ItemStack(Material.WHEAT), null, null, null, null, null, null }, 4, 10.0F, 15)).register();
/*      */     
/*  864 */     (new CustomWine(category_drinks, (ItemStack)new CustomItem(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjFiMzVmNzA3N2VjZjk4ZWYzZWJhMGYzNWQ5M2E5ODEzMDMwZjliOGI4ZTQyNmFlYjY4ZGFiMzhmMTExNiJ9fX0="), "§b朗姆酒", new String[] { "§8中级酒", "§7来自古巴的传统佳酿", "§7由海盗和商贩们传向世界各地", "", "§7▷▷ §b酒精度: §e40", "§7▷▷ §d精神值: §e20", "§7▷▷ §a饱食度: §e6" }), "RUM_WINE", new RecipeType(ExoticItems.ElectricityBrewing_2), new ItemStack[] { ExoticItems.Yeast_2, 
/*      */           
/*  866 */           getItem("LIME"), new ItemStack(Material.VINE), null, null, null, null, null, null }, 6, 20.0F, 40)).register();
/*      */     
/*  868 */     (new CustomWine(category_drinks, (ItemStack)new CustomItem(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDA1M2UyNjg2N2JiNTc1MzhlOTc4OTEzN2RiYmI1Mzc3NGUxOGVkYTZmZWY1MWNiMmVkZjQyNmIzNzI2NCJ9fX0="), "§b菠萝啤", new String[] { "§8中级酒", "§7使用菠萝特别调制的啤酒", "§7酸甜可口而又有啤酒淡淡的苦涩味", "", "§7▷▷ §b酒精度: §e18", "§7▷▷ §d精神值: §e16", "§7▷▷ §a饱食度: §e12" }), "PINEAPPLE_BEER", new RecipeType(ExoticItems.ElectricityBrewing_2), new ItemStack[] { ExoticItems.Yeast_2, 
/*      */           
/*  870 */           getItem("BEER"), getItem("PINEAPPLE"), null, null, null, null, null, null }, 12, 16.0F, 18)).register();
/*      */ 
/*      */     
/*  873 */     (new CustomWine(category_drinks, (ItemStack)new CustomItem(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTBhY2RlZWQ2MDcyNWQ5NWI2OTExNDM3MmQ3MDI0ZjlkNjY4ZjlmZTc0NjkzN2UwNTkzMjhiYmZiZmY2In19fQ=="), "§6仙馐酒", new String[] { "§8高级酒", "§7由神秘的仙馐果酿制", "§7拥有梦幻般的味道", "", "§7▷▷ §b酒精度: §e40", "§7▷▷ §d精神值: §e30", "§7▷▷ §a饱食度: §e10" }), "DREAMFRUIT_WINE", new RecipeType(ExoticItems.ElectricityBrewing_3), new ItemStack[] { ExoticItems.Yeast_3, 
/*      */           
/*  875 */           getItem("DREAMFRUIT"), getItem("LEMON"), null, null, null, null, null, null }, 10, 30.0F, 40)).register();
/*      */     
/*  877 */     (new CustomWine(category_drinks, (ItemStack)new CustomItem(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjFiMzVmNzA3N2VjZjk4ZWYzZWJhMGYzNWQ5M2E5ODEzMDMwZjliOGI4ZTQyNmFlYjY4ZGFiMzhmMTExNiJ9fX0="), "§6金果酒", new String[] { "§8高级酒", "§7金苹果酿制的酒", "§7拥有梦幻般的味道", "", "§7▷▷ §b酒精度: §e40", "§7▷▷ §d精神值: §e30", "§7▷▷ §a饱食度: §e16" }), "GLODAPPLE_WINE", new RecipeType(ExoticItems.ElectricityBrewing_3), new ItemStack[] { ExoticItems.Yeast_3, new ItemStack(Material.GOLDEN_APPLE), 
/*      */           
/*  879 */           getItem("NETHER_WINE"), null, null, null, null, null, null }, 16, 30.0F, 40, new PotionEffect[] { new PotionEffect(PotionEffectType.ABSORPTION, 600, 1), new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600, 1), new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1400, 1)
/*      */         
/*  881 */         })).register();
/*      */     
/*  883 */     (new CustomWine(category_drinks, (ItemStack)new CustomItem(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjFiMzVmNzA3N2VjZjk4ZWYzZWJhMGYzNWQ5M2E5ODEzMDMwZjliOGI4ZTQyNmFlYjY4ZGFiMzhmMTExNiJ9fX0="), "§6英雄酒", new String[] { "§8高级酒", "§7远古时期的祭祀用酒", "§7通常用于纪念名垂青史的英雄", "", "§7英雄已然逝去", "§7历史仍将继续", "", "§7▷▷ §b酒精度: §e60", "§7▷▷ §d精神值: §e30", "§7▷▷ §a饱食度: §e10" }), "HERO_WINE", new RecipeType(ExoticItems.ElectricityBrewing_3), new ItemStack[] { ExoticItems.Yeast_3, 
/*      */           
/*  885 */           getItem("WHITE_WINE"), getItem("YELLOW_WINE"), null, null, null, null, null, null }, 10, 30.0F, 60, new PotionEffect[] { new PotionEffect(PotionEffectType.ABSORPTION, 600, 1), new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600, 1), new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1400, 1)
/*      */         
/*  887 */         })).register();
/*      */ 
/*      */     
/*  890 */     (new CustomWine(category_drinks, (ItemStack)new CustomItem(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWVkOGE5ODVkYTdiMzRiZjkyODdiYWQyMWY2YmZlY2FiMWQ5MGZiOGEyZjlmMTMwNWJmMzI4ZWE4ZGNmIn19fQ=="), "§d琼浆玉液", new String[] { "§8特级酒", "§7此物只应天上有", "§7人间能得几回闻", "", "§7▷▷ §b酒精度: §e70", "§7▷▷ §d精神值: §e60", "§7▷▷ §a饱食度: §e10" }), "SUPER_WINE", new RecipeType(ExoticItems.ElectricityBrewing_3), new ItemStack[] { ExoticItems.Yeast_4, 
/*      */           
/*  892 */           getItem("DREAMFRUIT_WINE"), getItem("YELLOW_WINE"), null, null, null, null, null, null }, 10, 60.0F, 70, new PotionEffect[] { new PotionEffect(PotionEffectType.HEALTH_BOOST, 600, 1), new PotionEffect(PotionEffectType.HEAL, 20, 1), new PotionEffect(PotionEffectType.SPEED, 1400, 2)
/*      */         
/*  894 */         })).register();
/*      */     
/*  896 */     (new CustomWine(category_drinks, (ItemStack)new CustomItem(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjc1NTg0ZTZmZDU0Y2EwMWRmNGVmZmQ1Zjc0NmIyZDgzYTU4OWRlNjc3NzU1NzU2YmI1OGQ5ZWEyODQ1MTYifX19"), "§d醉生梦死", new String[] { "§8特级酒", "§7醉入癫狂无谓死", "§7梦醒味逝不知生", "", "§7▷▷ §b酒精度: §e70", "§7▷▷ §d精神值: §e60", "§7▷▷ §a饱食度: §e10" }), "DREAMER_WINE", new RecipeType(ExoticItems.ElectricityBrewing_3), new ItemStack[] { ExoticItems.Yeast_4, 
/*      */           
/*  898 */           getItem("DREAMFRUIT_WINE"), getItem("WHITE_WINE"), null, null, null, null, null, null }, 6, 100.0F, 90, new PotionEffect[] { new PotionEffect(PotionEffectType.CONFUSION, 600, 1)
/*  899 */         })).register();
/*      */   }
/*      */ 
/*      */   
/*      */   public void onDisable() {
/*  904 */     saveDatas();
/*  905 */     berries = null;
/*  906 */     trees = null;
/*  907 */     items = null;
/*      */   }
/*      */   
/*      */   public void registerTree(String rawName, MaterialData material, String texture, String fruitName, String color, int potion, String juice, boolean pie, Material... soil) {
/*  911 */     String name = getTranlateName(rawName);
/*  912 */     String id = name.toUpperCase().replace(" ", "_");
/*  913 */     Tree tree = new Tree(id, fruitName, texture, soil);
/*  914 */     trees.add(tree);
/*      */     
/*  916 */     items.put(id + "_SAPLING", new CustomItem(Material.SAPLING, color + rawName + "树苗", 0));
/*      */     
/*  918 */     (new SlimefunItem(category_main, (ItemStack)new CustomItem(Material.SAPLING, color + rawName + "树苗", 0), id + "_SAPLING", new RecipeType((ItemStack)new CustomItem(Material.LONG_GRASS, "&7破坏杂草获得", 1)), new ItemStack[] { null, null, null, null, (ItemStack)new CustomItem(Material.LONG_GRASS, 1), null, null, null, null
/*      */         
/*  920 */         })).register();
/*      */     
/*      */     try {
/*  923 */       (new EGPlant(category_main, (ItemStack)new CustomItem(getSkull(material, texture), color + StringUtils.format(rawName)), fruitName, new RecipeType((ItemStack)new CustomItem(Material.LEAVES, "&7从指定树木上收获", 0)), true, new ItemStack[] { null, null, null, null, 
/*  924 */             getItem(id + "_SAPLING"), null, null, null, null
/*  925 */           })).register();
/*  926 */     } catch (Exception e1) {
/*  927 */       e1.printStackTrace();
/*      */     } 
/*      */     
/*  930 */     if (potion > 0) {
/*  931 */       (new CustomFood(category_drinks, (ItemStack)new CustomPotion(color + juice, potion, new String[] { "", "§7恢复 §b3.0 §7饥饿值§8和§7精神值" }, new PotionEffect(PotionEffectType.SATURATION, 6, 0)), juice.toUpperCase().replace(" ", "_"), RecipeType.JUICER, new ItemStack[] {
/*  932 */             getItem(fruitName), null, null, null, null, null, null, null, null }, 3))
/*  933 */         .register();
/*      */     }
/*  935 */     if (pie) {
/*      */       try {
/*  937 */         (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQxOGM2YjBhMjlmYzFmZTc5MWM4OTc3NGQ4MjhmZjYzZDJhOWZhNmM4MzM3M2VmM2FhNDdiZjNlYjc5In19fQ=="), color + 
/*  938 */               StringUtils.format(rawName) + "派", new String[] { "", "§7恢复 §b6.5 §7饥饿值§8和§7精神值" }), name.toUpperCase() + "_PIE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/*  939 */               getItem(name.toUpperCase()), new ItemStack(Material.EGG), new ItemStack(Material.SUGAR), new ItemStack(Material.MILK_BUCKET), SlimefunItems.WHEAT_FLOUR, null, null, null, null }, 13))
/*      */           
/*  941 */           .register();
/*  942 */       } catch (Exception e) {
/*  943 */         e.printStackTrace();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  948 */     InputStream stream = Tree.class.getResourceAsStream("schematics/" + id + "_TREE.schematic");
/*  949 */     OutputStream out = null;
/*      */     
/*  951 */     byte[] buffer = new byte[4096];
/*      */     try {
/*  953 */       out = new FileOutputStream(new File("plugins/ExoticGarden/" + id + "_TREE.schematic")); int read;
/*  954 */       while ((read = stream.read(buffer)) > 0) {
/*  955 */         out.write(buffer, 0, read);
/*      */       }
/*  957 */     } catch (IOException e) {
/*  958 */       e.printStackTrace();
/*      */     } finally {
/*      */       try {
/*  961 */         stream.close();
/*  962 */         out.close();
/*  963 */       } catch (IOException e) {
/*  964 */         e.printStackTrace();
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void registerBerry(String rawName, String color, int potion, PlantType type, PlantData data) {
/*  970 */     String name = getTranlateName(rawName);
/*  971 */     Berry berry = new Berry(name.toUpperCase(), type, data);
/*  972 */     berries.add(berry);
/*      */     
/*  974 */     items.put(name.toUpperCase() + "_BUSH", new CustomItem(Material.SAPLING, color + rawName + "丛", 0));
/*      */     
/*  976 */     (new SlimefunItem(category_main, (ItemStack)new CustomItem(Material.SAPLING, color + rawName + "丛", 0), name.toUpperCase() + "_BUSH", new RecipeType((ItemStack)new CustomItem(Material.LONG_GRASS, "&7破坏杂草获得", 1)), new ItemStack[] { null, null, null, null, (ItemStack)new CustomItem(Material.LONG_GRASS, 1), null, null, null, null
/*      */         
/*  978 */         })).register();
/*      */     
/*  980 */     (new EGPlant(category_main, (ItemStack)new CustomItem(getSkull(Material.NETHER_STALK, data.getTexture()), color + rawName), name.toUpperCase(), new RecipeType((ItemStack)new CustomItem(Material.LEAVES, "&7从特定的植物上收获", 0)), true, new ItemStack[] { null, null, null, null, 
/*  981 */           getItem(name.toUpperCase() + "_BUSH"), null, null, null, null
/*  982 */         })).register();
/*      */     
/*  984 */     (new CustomFood(category_drinks, (ItemStack)new CustomPotion(color + rawName + "果汁", potion, new String[] { "", "§7恢复 §b3.0 §7饥饿值§8和§7精神值" }, new PotionEffect(PotionEffectType.SATURATION, 6, 0)), name.toUpperCase() + "_JUICE", RecipeType.JUICER, new ItemStack[] {
/*  985 */           getItem(name.toUpperCase()), null, null, null, null, null, null, null, null }, 5))
/*  986 */       .register();
/*      */     
/*  988 */     (new CustomFood(category_drinks, (ItemStack)new CustomPotion(color + rawName + "沙冰", potion, new String[] { "", "§7恢复 §b5.0 §7饥饿值§8和§7精神值" }, new PotionEffect(PotionEffectType.SATURATION, 10, 0)), name.toUpperCase() + "_SMOOTHIE", RecipeType.JUICER, new ItemStack[] {
/*  989 */           getItem(name.toUpperCase() + "_JUICE"), getItem("ICE_CUBE"), null, null, null, null, null, null, null }, 5))
/*  990 */       .register();
/*      */     
/*      */     try {
/*  993 */       (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGM4YTkzOTA5M2FiMWNkZTY2NzdmYWY3NDgxZjMxMWU1ZjE3ZjYzZDU4ODI1ZjBlMGMxNzQ2MzFmYjA0MzkifX19"), color + rawName + "果酱三明治", new String[] { "", "§7恢复 §b8.0 §7饥饿值§8和§7精神值" }), name
/*  994 */           .toUpperCase() + "_JELLY_SANDWICH", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { null, new ItemStack(Material.BREAD), null, null, 
/*  995 */             getItem(name.toUpperCase() + "_JUICE"), null, null, new ItemStack(Material.BREAD), null }, 16))
/*      */         
/*  997 */         .register();
/*      */       
/*  999 */       (new CustomFood(category_food, (ItemStack)new CustomItem(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQxOGM2YjBhMjlmYzFmZTc5MWM4OTc3NGQ4MjhmZjYzZDJhOWZhNmM4MzM3M2VmM2FhNDdiZjNlYjc5In19fQ=="), color + rawName + "派", new String[] { "", "§7恢复 §b6.5 §7饥饿值§8和§7精神值" }), name
/* 1000 */           .toUpperCase() + "_PIE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
/* 1001 */             getItem(name.toUpperCase()), new ItemStack(Material.EGG), new ItemStack(Material.SUGAR), new ItemStack(Material.MILK_BUCKET), SlimefunItems.WHEAT_FLOUR, null, null, null, null }, 13))
/*      */         
/* 1003 */         .register();
/* 1004 */     } catch (Exception e) {
/* 1005 */       e.printStackTrace();
/*      */     } 
/*      */   }
/*      */   
/*      */   public static ItemStack getItem(String id) {
/* 1010 */     SlimefunItem item = SlimefunItem.getByName(id);
/* 1011 */     return (item != null) ? item.getItem() : null;
/*      */   }
/*      */   
/*      */   private ItemStack getSkull(Material material, String texture) {
/* 1015 */     return getSkull(new MaterialData(material), texture);
/*      */   }
/*      */   
/*      */   public static ItemStack getSkull(MaterialData material, String texture) {
/*      */     try {
/* 1020 */       if (texture.equals("NO_SKULL_SPECIFIED")) return material.toItemStack(1); 
/* 1021 */       return skullitems ? CustomSkull.getItem(texture) : material.toItemStack(1);
/* 1022 */     } catch (Exception e) {
/* 1023 */       e.printStackTrace();
/* 1024 */       return material.toItemStack(1);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void registerPlant(String rawName, String color, Material material, PlantType type, PlantData data) {
/* 1029 */     String name = getTranlateName(rawName);
/* 1030 */     Berry berry = new Berry(name.toUpperCase().replace(" ", "_"), type, data);
/* 1031 */     berries.add(berry);
/*      */     
/* 1033 */     items.put(name.toUpperCase() + "_BUSH", new CustomItem(Material.SAPLING, color + rawName + "苗", 0));
/*      */     
/* 1035 */     (new SlimefunItem(category_main, (ItemStack)new CustomItem(Material.SAPLING, color + rawName + "苗", 0), name.toUpperCase().replace(" ", "_") + "_BUSH", new RecipeType((ItemStack)new CustomItem(Material.LONG_GRASS, "&7破坏杂草获得", 1)), new ItemStack[] { null, null, null, null, (ItemStack)new CustomItem(Material.LONG_GRASS, 1), null, null, null, null
/*      */         
/* 1037 */         })).register();
/*      */     
/* 1039 */     (new EGPlant(category_main, (ItemStack)new CustomItem(getSkull(material, data.getTexture()), color + rawName), name.toUpperCase().replace(" ", "_"), new RecipeType((ItemStack)new CustomItem(Material.LEAVES, "&7从特定的植物上收获", 0)), true, new ItemStack[] { null, null, null, null, 
/* 1040 */           getItem(name.toUpperCase().replace(" ", "_") + "_BUSH"), null, null, null, null
/* 1041 */         })).register();
/*      */   }
/*      */   
/*      */   public void registerTechPlant(String rawName, String color, Material material, PlantType type, PlantData data) {
/* 1045 */     String name = getTranlateName(rawName);
/* 1046 */     Berry berry = new Berry(name.toUpperCase().replace(" ", "_"), type, data);
/* 1047 */     berries.add(berry);
/*      */     
/* 1049 */     (new SlimefunItem(category_main, (ItemStack)new CustomItem(Material.SAPLING, color + rawName + "苗", 0), name.toUpperCase().replace(" ", "_") + "_BUSH", new RecipeType(ExoticItems.SeedAnalyzer_1), new ItemStack[] { null, null, null, null, ExoticItems.MysticSeed, null, null, null, null
/*      */         
/* 1051 */         })).register();
/*      */     
/* 1053 */     (new EGPlant(category_main, (ItemStack)new CustomItem(getSkull(material, data.getTexture()), color + rawName), name.toUpperCase().replace(" ", "_"), new RecipeType((ItemStack)new CustomItem(Material.LEAVES, "&7从特定的植物上收获", 0)), true, new ItemStack[] { null, null, null, null, 
/* 1054 */           getItem(name.toUpperCase().replace(" ", "_") + "_BUSH"), null, null, null, null
/* 1055 */         })).register();
/*      */   }
/*      */   
/*      */   public void registerMagicalPlant(String rawName, ItemStack item, String skull, ItemStack[] recipe) {
/* 1059 */     String name = getTranlateName(rawName);
/* 1060 */     CustomItem customItem = new CustomItem(new MaterialData(Material.SUGAR), "&d异化植物粉末", new String[] { "", "§7" + rawName });
/*      */     
/*      */     try {
/* 1063 */       (new SlimefunItem(category_magic, (ItemStack)new CustomItem(Material.SAPLING, "&d" + rawName + "苗", 0), name.toUpperCase().replace(" ", "_") + "_PLANT", new RecipeType((ItemStack)new CustomItem(
/* 1064 */               CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTI1NmY3ZmY1MmU3YmZkODE4N2I4M2RkMzRkZjM0NTAyOTUyYjhkYjlmYWZiNzI4OGViZWJiNmU3OGVmMTVmIn19fQ=="), "&b种子分析机", new String[] { "", "&7用于分析未知的种子", "&7并将其培养为可种植的苗" })), new ItemStack[] { null, null, null, null, ExoticItems.MysticSeed, null, null, null, null
/*      */           
/* 1066 */           })).register();
/* 1067 */     } catch (Exception e) {
/* 1068 */       e.printStackTrace();
/*      */     } 
/*      */ 
/*      */     
/* 1072 */     Berry berry = new Berry((ItemStack)customItem, name.toUpperCase() + "_ESSENCE", PlantType.ORE_PLANT, new PlantData(skull));
/* 1073 */     berries.add(berry);
/* 1074 */     HandledBlock plant = new HandledBlock(category_magic, (ItemStack)customItem, name.toUpperCase().replace(" ", "_") + "_ESSENCE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { (ItemStack)customItem, (ItemStack)customItem, (ItemStack)customItem, (ItemStack)customItem, null, (ItemStack)customItem, (ItemStack)customItem, (ItemStack)customItem, (ItemStack)customItem });
/*      */ 
/*      */     
/* 1077 */     plant.setRecipeOutput(item.clone());
/* 1078 */     plant.register();
/*      */   }
/*      */   
/*      */   public static Berry getBerry(Block block) {
/* 1082 */     SlimefunItem item = BlockStorage.check(block);
/* 1083 */     if (item != null && item instanceof HandledBlock)
/* 1084 */       for (Berry berry : berries) {
/* 1085 */         if (item.getName().equalsIgnoreCase(berry.getName())) return berry;
/*      */       
/*      */       }  
/* 1088 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public static ItemStack harvestPlant(Block block) {
/* 1093 */     ItemStack itemstack = null;
/* 1094 */     SlimefunItem item = BlockStorage.check(block);
/* 1095 */     if (item != null) {
/* 1096 */       for (Berry berry : berries) {
/* 1097 */         if (item.getName().equalsIgnoreCase(berry.getName())) {
/* 1098 */           Block plant; switch (berry.getType()) {
/*      */             
/*      */             case ORE_PLANT:
/*      */             case DOUBLE_PLANT:
/* 1102 */               if (BlockStorage.check(block.getRelative(BlockFace.DOWN)) == null) {
/* 1103 */                 plant = block;
/* 1104 */                 BlockStorage.retrieve(block.getRelative(BlockFace.UP));
/* 1105 */                 block.getWorld().playEffect(block.getRelative(BlockFace.UP).getLocation(), Effect.STEP_SOUND, Material.LEAVES);
/* 1106 */                 block.getRelative(BlockFace.UP).setType(Material.AIR);
/*      */               } else {
/*      */                 
/* 1109 */                 plant = block.getRelative(BlockFace.DOWN);
/* 1110 */                 BlockStorage.retrieve(block);
/* 1111 */                 block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, Material.LEAVES);
/* 1112 */                 block.setType(Material.AIR);
/*      */               } 
/* 1114 */               plant.setType(Material.SAPLING);
/* 1115 */               plant.setData((byte)0);
/* 1116 */               itemstack = berry.getItem();
/* 1117 */               BlockStorage.store(plant, getItem(berry.toBush()));
/*      */               continue;
/*      */           } 
/*      */           
/* 1121 */           block.setType(Material.SAPLING);
/* 1122 */           block.setData((byte)0);
/* 1123 */           itemstack = berry.getItem();
/* 1124 */           BlockStorage.store(block, getItem(berry.toBush()));
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1131 */     return itemstack;
/*      */   }
/*      */   
/*      */   public static boolean isPlant(Block block) {
/* 1135 */     ItemStack itemstack = null;
/* 1136 */     SlimefunItem item = BlockStorage.check(block);
/* 1137 */     if (item != null) {
/* 1138 */       for (Berry berry : berries) {
/* 1139 */         if (item.getName().equalsIgnoreCase(berry.getName())) {
/* 1140 */           switch (berry.getType()) {
/*      */             case ORE_PLANT:
/*      */             case DOUBLE_PLANT:
/* 1143 */               itemstack = berry.getItem();
/*      */               continue;
/*      */           } 
/*      */           
/* 1147 */           itemstack = berry.getItem();
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1154 */     return (itemstack != null);
/*      */   }
/*      */   
/*      */   private String getTranlateName(String name) {
/* 1158 */     if (this.traslateNames.get(name) != null) {
/* 1159 */       return this.traslateNames.get(name);
/*      */     }
/* 1161 */     return name;
/*      */   }
/*      */   
/*      */   private void initTransNames() {
/* 1165 */     this.traslateNames.put("葡萄", "Grape");
/* 1166 */     this.traslateNames.put("蓝莓", "Blueberry");
/* 1167 */     this.traslateNames.put("接骨木果", "Elderberry");
/* 1168 */     this.traslateNames.put("覆盆子", "Raspberry");
/* 1169 */     this.traslateNames.put("黑莓", "Blackberry");
/* 1170 */     this.traslateNames.put("蔓越莓", "Cranberry");
/* 1171 */     this.traslateNames.put("越桔", "Cowberry");
/* 1172 */     this.traslateNames.put("草莓", "Strawberry");
/* 1173 */     this.traslateNames.put("番茄", "Tomato");
/* 1174 */     this.traslateNames.put("生菜", "Lettuce");
/* 1175 */     this.traslateNames.put("茶叶", "Tea Leaf");
/* 1176 */     this.traslateNames.put("卷心菜", "Cabbage");
/* 1177 */     this.traslateNames.put("番薯", "Sweet Potato");
/* 1178 */     this.traslateNames.put("芥菜籽", "Mustard Seed");
/* 1179 */     this.traslateNames.put("玉米", "Corn");
/* 1180 */     this.traslateNames.put("菠萝", "Pineapple");
/* 1181 */     this.traslateNames.put("苹果", "Apple Oak");
/* 1182 */     this.traslateNames.put("椰子", "Coconut");
/* 1183 */     this.traslateNames.put("樱桃", "Cherry");
/* 1184 */     this.traslateNames.put("石榴", "Pomegranate");
/* 1185 */     this.traslateNames.put("柠檬", "Lemon");
/* 1186 */     this.traslateNames.put("李子", "Plum");
/* 1187 */     this.traslateNames.put("酸橙", "Lime");
/* 1188 */     this.traslateNames.put("橙子", "Orange");
/* 1189 */     this.traslateNames.put("桃子", "Peach");
/* 1190 */     this.traslateNames.put("香梨", "Pear");
/*      */     
/* 1192 */     this.traslateNames.put("煤炭", "Coal");
/* 1193 */     this.traslateNames.put("铁", "Iron");
/* 1194 */     this.traslateNames.put("黄金", "Gold");
/* 1195 */     this.traslateNames.put("红石", "RedStone");
/* 1196 */     this.traslateNames.put("青金石", "Lapis");
/* 1197 */     this.traslateNames.put("末影", "Ender");
/* 1198 */     this.traslateNames.put("石英", "Quartz");
/* 1199 */     this.traslateNames.put("钻石", "Diamond");
/* 1200 */     this.traslateNames.put("绿宝石", "Emerald");
/* 1201 */     this.traslateNames.put("萤石", "Glowstone");
/* 1202 */     this.traslateNames.put("黑曜石", "Obsidian");
/* 1203 */     this.traslateNames.put("史莱姆", "Slime");
/* 1204 */     this.traslateNames.put("潜影壳", "Shulker_Shell");
/* 1205 */     this.traslateNames.put("咖啡豆", "Coffeebean");
/* 1206 */     this.traslateNames.put("仙馐果", "DreamFruit");
/* 1207 */     this.traslateNames.put("酒香果", "WineFruit");
/*      */   }
/*      */   
/*      */   private void createDefaultConfiguration(File actual, String defaultName) {
/* 1211 */     File parent = actual.getParentFile();
/* 1212 */     if (!parent.exists()) {
/* 1213 */       parent.mkdirs();
/*      */     }
/* 1215 */     if (actual.exists()) {
/*      */       return;
/*      */     }
/* 1218 */     InputStream input = null;
/*      */     
/*      */     try {
/* 1221 */       JarFile file = new JarFile(getFile());
/* 1222 */       ZipEntry copy = file.getEntry("resources/" + defaultName);
/* 1223 */       if (copy == null) {
/* 1224 */         throw new FileNotFoundException();
/*      */       }
/* 1226 */       input = file.getInputStream(copy);
/*      */     }
/* 1228 */     catch (IOException iOException) {}
/* 1229 */     if (input != null) {
/*      */       
/* 1231 */       FileOutputStream output = null;
/*      */       
/*      */       try {
/* 1234 */         output = new FileOutputStream(actual);
/* 1235 */         byte[] buf = new byte[32];
/* 1236 */         int length = 0;
/* 1237 */         while ((length = input.read(buf)) > 0) {
/* 1238 */           output.write(buf, 0, length);
/*      */         }
/*      */       }
/* 1241 */       catch (IOException e) {
/*      */         
/* 1243 */         e.printStackTrace();
/*      */       } finally {
/*      */ 
/*      */         
/*      */         try {
/*      */           
/* 1249 */           if (input != null) {
/* 1250 */             input.close();
/*      */           }
/*      */         }
/* 1253 */         catch (IOException iOException) {}
/*      */         
/*      */         try {
/* 1256 */           if (output != null) {
/* 1257 */             output.close();
/*      */           }
/*      */         }
/* 1260 */         catch (IOException iOException) {}
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void initDataFromYAML(File storge) {
/* 1266 */     this.yamlStorge = YamlConfiguration.loadConfiguration(storge);
/* 1267 */     ConfigurationSection section = this.yamlStorge.getConfigurationSection("Players");
/* 1268 */     if (section == null) {
/* 1269 */       this.yamlStorge.set("Players", null);
/*      */       try {
/* 1271 */         this.yamlStorge.save("storge.yml");
/* 1272 */       } catch (IOException e) {
/* 1273 */         e.printStackTrace();
/*      */       } 
/*      */     } else {
/* 1276 */       for (String s : this.yamlStorge.getConfigurationSection("Players").getKeys(false)) {
/* 1277 */         drunkPlayers.put(s, new PlayerAlcohol(s, this.yamlStorge
/* 1278 */               .getInt("Players.%p.Alcohol".replace("%p", s)), this.yamlStorge.getBoolean("Players.%p.Drunk".replace("%p", s))));
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public void initPlayerData(Player player) {
/* 1284 */     String name = player.getName();
/* 1285 */     ConfigurationSection section = this.yamlStorge.getConfigurationSection("Players");
/* 1286 */     if (section != null && section.contains(name)) {
/* 1287 */       drunkPlayers.put(name, new PlayerAlcohol(name, this.yamlStorge
/* 1288 */             .getInt("Players.%p.Alcohol".replace("%p", name)), this.yamlStorge.getBoolean("Players.%p.Drunk".replace("%p", name))));
/*      */     } else {
/* 1290 */       drunkPlayers.put(name, new PlayerAlcohol(name, 0));
/* 1291 */       saveDatas(player);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void saveDatas() {
/*      */     try {
/* 1297 */       for (Map.Entry<String, PlayerAlcohol> o : drunkPlayers.entrySet()) {
/* 1298 */         Map.Entry entry = o;
/* 1299 */         String player = "Players." + entry.getKey();
/* 1300 */         this.yamlStorge.set(player + ".Alcohol", Integer.valueOf(((PlayerAlcohol)entry.getValue()).getAlcohol()));
/* 1301 */         this.yamlStorge.set(player + ".Drunk", Boolean.valueOf(((PlayerAlcohol)entry.getValue()).isDrunk()));
/*      */       } 
/* 1303 */       this.yamlStorge.save(new File(getDataFolder() + File.separator + "storge.yml"));
/* 1304 */     } catch (IOException e) {
/* 1305 */       e.printStackTrace();
/*      */     } 
/*      */   }
/*      */   
/*      */   public void saveDatas(Player player) {
/*      */     try {
/* 1311 */       String playerName = "Players." + player.getName();
/* 1312 */       this.yamlStorge.set(playerName + ".Alcohol", Integer.valueOf(((PlayerAlcohol)drunkPlayers.get(player.getName())).getAlcohol()));
/* 1313 */       this.yamlStorge.set(playerName + ".Drunk", Boolean.valueOf(((PlayerAlcohol)drunkPlayers.get(player.getName())).isDrunk()));
/* 1314 */       this.yamlStorge.save(new File(getDataFolder() + File.separator + "storge.yml"));
/* 1315 */     } catch (IOException e) {
/* 1316 */       e.printStackTrace();
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean isSanityEnabled() {
/* 1321 */     return this.sanity;
/*      */   }
/*      */   
/*      */   public boolean isResidenceEnabled() {
/* 1325 */     return this.residence;
/*      */   }
/*      */   
/*      */   public YamlConfiguration getYamlStorge() {
/* 1329 */     return this.yamlStorge;
/*      */   }
/*      */   
/*      */   public static void sendDrunkMessage(Player player) {
/* 1333 */     Random ramdom = new Random();
/* 1334 */     player.chat(((String)drunkMsg.get(ramdom.nextInt(drunkMsg.size()))).replace("%player%", (
/* 1335 */           (Player)Bukkit.getOnlinePlayers().toArray()[ramdom.nextInt(Bukkit.getOnlinePlayers().size())]).getName()));
/*      */   }
/*      */   
/*      */   private void registerDrunkMessage() {
/* 1339 */     drunkMsg.add("§7我还能喝! §8(§c胡言乱语§8)");
/* 1340 */     drunkMsg.add("§7我控计不住我计己啊! §8(§c胡言乱语§8)");
/* 1341 */     drunkMsg.add("§7我...嗝! §8(§c胡言乱语§8)");
/* 1342 */     drunkMsg.add("§7嗝!我...要摸摸我家的苦力怕 §8(§c胡言乱语§8)");
/* 1343 */     drunkMsg.add("§7我要打十个...末影龙! §8(§c胡言乱语§8)");
/* 1344 */     drunkMsg.add("§7@%player%...你怎么扭来扭去的! §8(§c胡言乱语§8)");
/* 1345 */     drunkMsg.add("§7一...一起蛤皮! §8(§c胡言乱语§8)");
/* 1346 */     drunkMsg.add("@%player% §7我给你讲个故事...从前...嗝!蛤蛤蛤蛤蛤蛤蛤! §8(§c胡言乱语§8)");
/* 1347 */     drunkMsg.add("§7%player%...我超喜欢你的!让我揉揉你的肥脸... §8(§c胡言乱语§8)");
/* 1348 */     drunkMsg.add("§7老子...最强! §8(§c胡言乱语§8)");
/* 1349 */     drunkMsg.add("§7看到..这个酒瓶没有!看到了是吧!...怕什么我又不打你!蛤蛤蛤蛤蛤嗝 §8(§c胡言乱语§8)");
/* 1350 */     drunkMsg.add("§7每天吃肉长不胖, 天天喝酒身体棒! §8(§c胡言乱语§8)");
/* 1351 */     drunkMsg.add("§7这个服务器里的玩家超有钱的, 天天氪金, 还送我钱...我超喜欢这里的! §8(§c胡言乱语§8)");
/*      */   }
/*      */   
/*      */   private void checkDrunkers() {
/* 1355 */     for (Map.Entry<String, PlayerAlcohol> o : drunkPlayers.entrySet()) {
/* 1356 */       Map.Entry entry = o;
/* 1357 */       PlayerAlcohol pa = (PlayerAlcohol)entry.getValue();
/* 1358 */       Player player = Bukkit.getPlayer(pa.getPlayer());
/* 1359 */       if (player != null) {
/* 1360 */         if (pa.getAlcohol() > 0) {
/* 1361 */           ((PlayerAlcohol)drunkPlayers.get(pa.getPlayer())).addAlcohol(-1);
/*      */         }
/* 1363 */         if (pa.isDrunk) {
/* 1364 */           if (pa.getAlcohol() <= 0) {
/* 1365 */             ((PlayerAlcohol)drunkPlayers.get(pa.getPlayer())).setDrunk(false); continue;
/*      */           } 
/* 1367 */           player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 120, 1, false));
/*      */           continue;
/*      */         } 
/* 1370 */         if (pa.getAlcohol() >= 100)
/* 1371 */           ((PlayerAlcohol)drunkPlayers.get(pa.getPlayer())).setDrunk(true); 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\hai'man\Desktop\粘液科技1.12\server\plugins\[粘液附属异域花园修复版]ExoticGarden-Nar.jar!\me\mrCookieSlime\ExoticGarden\ExoticGarden.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */