/*    */ package me.mrCookieSlime.ExoticGarden.machines;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import me.mrCookieSlime.ExoticGarden.EGPlant;
/*    */ import me.mrCookieSlime.ExoticGarden.Items.ExoticItems;
/*    */ import me.mrCookieSlime.ExoticGarden.gui.DefaultGUI;
/*    */ import me.mrCookieSlime.ExoticGarden.recipe.DefaultSubRecipe;
/*    */ import me.mrCookieSlime.Slimefun.Lists.RecipeType;
/*    */ import me.mrCookieSlime.Slimefun.Objects.Category;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ 
/*    */ public abstract class SeedAnalyzer
/*    */   extends DefaultGUI
/*    */ {
/*    */   public SeedAnalyzer(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
/* 19 */     super(category, item, name, recipeType, recipe);
/*    */   }
/*    */ 
/*    */   
/*    */   public void registerDefaultRecipes() {
/* 24 */     registerRecipe(140 - getLevel() * 40, new ItemStack[] { ExoticItems.MysticSeed }, new ItemStack[] { new ItemStack(Material.GOLD_NUGGET) });
/*    */   }
/*    */   
/*    */   public String getMachineIdentifier() {
/* 28 */     return "SEED_ANALYZER_" + getLevel();
/*    */   }
/*    */ 
/*    */   
/*    */   public List<DefaultSubRecipe> getSubRecipes() {
/* 33 */     List<DefaultSubRecipe> subRecipes = new ArrayList<>();
/*    */ 
/*    */     
/* 36 */     subRecipes.add(new DefaultSubRecipe(2500 + 1000 * getLevel(), EGPlant.getByName("GRAPE_BUSH").getItem()));
/* 37 */     subRecipes.add(new DefaultSubRecipe(2500 + 1000 * getLevel(), EGPlant.getByName("BLUEBERRY_BUSH").getItem()));
/* 38 */     subRecipes.add(new DefaultSubRecipe(2500 + 1000 * getLevel(), EGPlant.getByName("ELDERBERRY_BUSH").getItem()));
/* 39 */     subRecipes.add(new DefaultSubRecipe(2500 + 1000 * getLevel(), EGPlant.getByName("RASPBERRY_BUSH").getItem()));
/* 40 */     subRecipes.add(new DefaultSubRecipe(2500 + 1000 * getLevel(), EGPlant.getByName("BLACKBERRY_BUSH").getItem()));
/* 41 */     subRecipes.add(new DefaultSubRecipe(2500 + 1000 * getLevel(), EGPlant.getByName("CRANBERRY_BUSH").getItem()));
/* 42 */     subRecipes.add(new DefaultSubRecipe(2500 + 1000 * getLevel(), EGPlant.getByName("COWBERRY_BUSH").getItem()));
/* 43 */     subRecipes.add(new DefaultSubRecipe(2500 + 1000 * getLevel(), EGPlant.getByName("STRAWBERRY_BUSH").getItem()));
/* 44 */     subRecipes.add(new DefaultSubRecipe(2500 + 1000 * getLevel(), EGPlant.getByName("PEAR_SAPLING").getItem()));
/* 45 */     subRecipes.add(new DefaultSubRecipe(2500 + 1000 * getLevel(), EGPlant.getByName("PEACH_SAPLING").getItem()));
/* 46 */     subRecipes.add(new DefaultSubRecipe(2500 + 1000 * getLevel(), EGPlant.getByName("ORANGE_SAPLING").getItem()));
/* 47 */     subRecipes.add(new DefaultSubRecipe(2500 + 1000 * getLevel(), EGPlant.getByName("LIME_SAPLING").getItem()));
/* 48 */     subRecipes.add(new DefaultSubRecipe(2500 + 1000 * getLevel(), EGPlant.getByName("PLUM_SAPLING").getItem()));
/* 49 */     subRecipes.add(new DefaultSubRecipe(2500 + 1000 * getLevel(), EGPlant.getByName("LEMON_SAPLING").getItem()));
/* 50 */     subRecipes.add(new DefaultSubRecipe(2500 + 1000 * getLevel(), EGPlant.getByName("POMEGRANATE_SAPLING").getItem()));
/* 51 */     subRecipes.add(new DefaultSubRecipe(2500 + 1000 * getLevel(), EGPlant.getByName("CHERRY_SAPLING").getItem()));
/* 52 */     subRecipes.add(new DefaultSubRecipe(2500 + 1000 * getLevel(), EGPlant.getByName("COCONUT_SAPLING").getItem()));
/* 53 */     subRecipes.add(new DefaultSubRecipe(2000 + 1000 * getLevel(), EGPlant.getByName("WINEFRUIT_BUSH").getItem()));
/*    */     
/* 55 */     subRecipes.add(new DefaultSubRecipe(500 + 500 * getLevel(), EGPlant.getByName("COAL_PLANT").getItem()));
/* 56 */     subRecipes.add(new DefaultSubRecipe(500 + 500 * getLevel(), EGPlant.getByName("IRON_PLANT").getItem()));
/* 57 */     subRecipes.add(new DefaultSubRecipe(500 + 500 * getLevel(), EGPlant.getByName("GOLD_PLANT").getItem()));
/* 58 */     subRecipes.add(new DefaultSubRecipe(500 + 500 * getLevel(), EGPlant.getByName("REDSTONE_PLANT").getItem()));
/* 59 */     subRecipes.add(new DefaultSubRecipe(500 + 500 * getLevel(), EGPlant.getByName("LAPIS_PLANT").getItem()));
/* 60 */     subRecipes.add(new DefaultSubRecipe(500 + 500 * getLevel(), EGPlant.getByName("QUARTZ_PLANT").getItem()));
/* 61 */     subRecipes.add(new DefaultSubRecipe(500 + 500 * getLevel(), EGPlant.getByName("OBSIDIAN_PLANT").getItem()));
/* 62 */     subRecipes.add(new DefaultSubRecipe(500 + 500 * getLevel(), EGPlant.getByName("ENDER_PLANT").getItem()));
/* 63 */     subRecipes.add(new DefaultSubRecipe(500 + 500 * getLevel(), EGPlant.getByName("GLOWSTONE_PLANT").getItem()));
/* 64 */     subRecipes.add(new DefaultSubRecipe(500 + 500 * getLevel(), EGPlant.getByName("DIAMOND_PLANT").getItem()));
/* 65 */     subRecipes.add(new DefaultSubRecipe(500 + 500 * getLevel(), EGPlant.getByName("EMERALD_PLANT").getItem()));
/* 66 */     subRecipes.add(new DefaultSubRecipe(500 + 500 * getLevel(), EGPlant.getByName("SLIME_PLANT").getItem()));
/* 67 */     subRecipes.add(new DefaultSubRecipe(500 + 500 * getLevel(), EGPlant.getByName("SHULKER_SHELL_PLANT").getItem()));
/* 68 */     subRecipes.add(new DefaultSubRecipe(500 + 500 * getLevel(), EGPlant.getByName("COFFEEBEAN_BUSH").getItem()));
/* 69 */     subRecipes.add(new DefaultSubRecipe(500 + 500 * getLevel(), EGPlant.getByName("DREAMFRUIT_BUSH").getItem()));
/* 70 */     return subRecipes;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack getProgressBar() {
/* 76 */     return new ItemStack(Material.FLINT_AND_STEEL);
/*    */   }
/*    */ }


/* Location:              C:\Users\hai'man\Desktop\粘液科技1.12\server\plugins\[粘液附属异域花园修复版]ExoticGarden-Nar.jar!\me\mrCookieSlime\ExoticGarden\machines\SeedAnalyzer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */