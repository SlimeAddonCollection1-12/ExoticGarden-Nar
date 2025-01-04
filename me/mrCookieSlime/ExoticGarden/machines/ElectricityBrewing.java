/*    */ package me.mrCookieSlime.ExoticGarden.machines;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import me.mrCookieSlime.ExoticGarden.Items.ExoticItems;
/*    */ import me.mrCookieSlime.ExoticGarden.gui.ThreeInputGUI;
/*    */ import me.mrCookieSlime.ExoticGarden.recipe.DefaultSubRecipe;
/*    */ import me.mrCookieSlime.Slimefun.Lists.RecipeType;
/*    */ import me.mrCookieSlime.Slimefun.Objects.Category;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ 
/*    */ public abstract class ElectricityBrewing
/*    */   extends ThreeInputGUI
/*    */ {
/*    */   public ElectricityBrewing(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
/* 18 */     super(category, item, name, recipeType, recipe);
/*    */   }
/*    */ 
/*    */   
/*    */   public void registerDefaultRecipes() {
/* 23 */     registerRecipe(80 - getLevel() * 20, new ItemStack[] { ExoticItems.Yeast_1, new ItemStack(Material.WHEAT), new ItemStack(Material.WHEAT) }, new ItemStack[] { getItem("NORMAL_BREW") });
/* 24 */     registerRecipe(80 - getLevel() * 20, new ItemStack[] { ExoticItems.Yeast_1, new ItemStack(Material.APPLE), new ItemStack(Material.SUGAR) }, new ItemStack[] { getItem("APPLE_WINE") });
/* 25 */     registerRecipe(80 - getLevel() * 20, new ItemStack[] { ExoticItems.Yeast_1, new ItemStack(Material.BREAD), new ItemStack(Material.SUGAR) }, new ItemStack[] { getItem("BREAD_WINE") });
/* 26 */     registerRecipe(80 - getLevel() * 20, new ItemStack[] { ExoticItems.Yeast_1, new ItemStack(Material.POTATO_ITEM), new ItemStack(Material.SUGAR) }, new ItemStack[] { getItem("POTATO_WINE") });
/* 27 */     registerRecipe(80 - getLevel() * 20, new ItemStack[] { ExoticItems.Yeast_1, new ItemStack(Material.NETHER_WARTS), new ItemStack(Material.SUGAR) }, new ItemStack[] { getItem("NETHER_WINE") });
/* 28 */     registerRecipe(80 - getLevel() * 20, new ItemStack[] { ExoticItems.Yeast_1, new ItemStack(Material.MILK_BUCKET), new ItemStack(Material.SUGAR) }, new ItemStack[] { getItem("MILK_WINE") });
/* 29 */     registerRecipe(80 - getLevel() * 20, new ItemStack[] { ExoticItems.Yeast_1, new ItemStack(Material.CHORUS_FRUIT), new ItemStack(Material.SUGAR) }, new ItemStack[] { getItem("ENDER_WINE") });
/* 30 */     if (getLevel() >= 2) {
/* 31 */       registerRecipe(80 - getLevel() * 20, new ItemStack[] { ExoticItems.Yeast_2, getItem("NORMAL_BREW"), new ItemStack(Material.WHEAT) }, new ItemStack[] { getItem("WHITE_WINE") });
/* 32 */       registerRecipe(80 - getLevel() * 20, new ItemStack[] { ExoticItems.Yeast_2, getItem("APPLE_WINE"), getItem("APPLE") }, new ItemStack[] { getItem("APPLE_VINEGAR") });
/* 33 */       registerRecipe(80 - getLevel() * 20, new ItemStack[] { ExoticItems.Yeast_2, getItem("NETHER_WINE"), new ItemStack(Material.MAGMA_CREAM) }, new ItemStack[] { getItem("FIRE_WINE") });
/* 34 */       registerRecipe(80 - getLevel() * 20, new ItemStack[] { ExoticItems.Yeast_2, getItem("GRAPE"), getItem("GRAPE") }, new ItemStack[] { getItem("GRAPE_WINE") });
/* 35 */       registerRecipe(80 - getLevel() * 20, new ItemStack[] { ExoticItems.Yeast_2, getItem("CORN"), new ItemStack(Material.SUGAR) }, new ItemStack[] { getItem("CORN_WINE") });
/* 36 */       registerRecipe(80 - getLevel() * 20, new ItemStack[] { ExoticItems.Yeast_2, getItem("SWEET_POTATO"), new ItemStack(Material.SUGAR) }, new ItemStack[] { getItem("YELLOW_WINE") });
/* 37 */       registerRecipe(80 - getLevel() * 20, new ItemStack[] { ExoticItems.Yeast_2, getItem("WINEFRUIT"), getItem("WINEFRUIT") }, new ItemStack[] { getItem("LIGHT_BEER") });
/* 38 */       registerRecipe(80 - getLevel() * 20, new ItemStack[] { ExoticItems.Yeast_2, getItem("WINEFRUIT"), new ItemStack(Material.WHEAT) }, new ItemStack[] { getItem("BEER") });
/* 39 */       registerRecipe(80 - getLevel() * 20, new ItemStack[] { ExoticItems.Yeast_2, getItem("LIME"), new ItemStack(Material.VINE) }, new ItemStack[] { getItem("RUM_WINE") });
/* 40 */       registerRecipe(80 - getLevel() * 20, new ItemStack[] { ExoticItems.Yeast_2, getItem("BEER"), getItem("PINEAPPLE") }, new ItemStack[] { getItem("PINEAPPLE_BEER") });
/*    */     } 
/* 42 */     if (getLevel() >= 3) {
/* 43 */       registerRecipe(80 - getLevel() * 20, new ItemStack[] { ExoticItems.Yeast_3, getItem("DREAMFRUIT"), getItem("LEMON") }, new ItemStack[] { getItem("DREAMFRUIT_WINE") });
/* 44 */       registerRecipe(80 - getLevel() * 20, new ItemStack[] { ExoticItems.Yeast_3, new ItemStack(Material.GOLDEN_APPLE), getItem("NETHER_WINE") }, new ItemStack[] { getItem("GLODAPPLE_WINE") });
/* 45 */       registerRecipe(80 - getLevel() * 20, new ItemStack[] { ExoticItems.Yeast_3, getItem("WHITE_WINE"), getItem("YELLOW_WINE") }, new ItemStack[] { getItem("HERO_WINE") });
/* 46 */       registerRecipe(80 - getLevel() * 20, new ItemStack[] { ExoticItems.Yeast_4, getItem("DREAMFRUIT_WINE"), getItem("YELLOW_WINE") }, new ItemStack[] { getItem("SUPER_WINE") });
/* 47 */       registerRecipe(80 - getLevel() * 20, new ItemStack[] { ExoticItems.Yeast_4, getItem("DREAMFRUIT_WINE"), getItem("WHITE_WINE") }, new ItemStack[] { getItem("DREAMER_WINE") });
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMachineIdentifier() {
/* 54 */     return "ELECTRICITY_BREWING_" + getLevel();
/*    */   }
/*    */ 
/*    */   
/*    */   public List<DefaultSubRecipe> getSubRecipes() {
/* 59 */     List<DefaultSubRecipe> subRecipes = new ArrayList<>();
/* 60 */     subRecipes.add(new DefaultSubRecipe(1000 * getLevel(), ExoticItems.Alcohol));
/* 61 */     return subRecipes;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack getProgressBar() {
/* 67 */     return new ItemStack(Material.FLINT_AND_STEEL);
/*    */   }
/*    */ }


/* Location:              C:\Users\hai'man\Desktop\粘液科技1.12\server\plugins\[粘液附属异域花园修复版]ExoticGarden-Nar.jar!\me\mrCookieSlime\ExoticGarden\machines\ElectricityBrewing.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */