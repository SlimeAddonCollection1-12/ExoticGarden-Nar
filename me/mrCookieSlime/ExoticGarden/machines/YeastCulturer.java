/*    */ package me.mrCookieSlime.ExoticGarden.machines;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import me.mrCookieSlime.ExoticGarden.Items.ExoticItems;
/*    */ import me.mrCookieSlime.ExoticGarden.gui.DefaultGUI;
/*    */ import me.mrCookieSlime.ExoticGarden.recipe.DefaultSubRecipe;
/*    */ import me.mrCookieSlime.Slimefun.Lists.RecipeType;
/*    */ import me.mrCookieSlime.Slimefun.Objects.Category;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ public abstract class YeastCulturer
/*    */   extends DefaultGUI {
/*    */   public YeastCulturer(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
/* 16 */     super(category, item, name, recipeType, recipe);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMachineIdentifier() {
/* 22 */     return "YEAST_CULTURER";
/*    */   }
/*    */ 
/*    */   
/*    */   public void registerDefaultRecipes() {
/* 27 */     registerRecipe(120, new ItemStack[] { new ItemStack(Material.HAY_BLOCK) }, new ItemStack[] { ExoticItems.Yeast_1 });
/* 28 */     registerRecipe(80, new ItemStack[] { getItem("WINEFRUIT") }, new ItemStack[] { ExoticItems.Yeast_2 });
/* 29 */     registerRecipe(40, new ItemStack[] { getItem("DREAMFRUIT") }, new ItemStack[] { ExoticItems.Yeast_3 });
/*    */   }
/*    */ 
/*    */   
/*    */   public List<DefaultSubRecipe> getSubRecipes() {
/* 34 */     List<DefaultSubRecipe> subRecipes = new ArrayList<>();
/* 35 */     subRecipes.add(new DefaultSubRecipe(750 + getLevel() * 250, ExoticItems.Yeast_4));
/* 36 */     return subRecipes;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack getProgressBar() {
/* 42 */     return new ItemStack(Material.FLINT_AND_STEEL);
/*    */   }
/*    */ }


/* Location:              C:\Users\hai'man\Desktop\粘液科技1.12\server\plugins\[粘液附属异域花园修复版]ExoticGarden-Nar.jar!\me\mrCookieSlime\ExoticGarden\machines\YeastCulturer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */