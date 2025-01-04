/*    */ package me.mrCookieSlime.ExoticGarden;
/*    */ 
/*    */ import me.mrCookieSlime.Slimefun.Lists.RecipeType;
/*    */ import me.mrCookieSlime.Slimefun.Objects.Category;
/*    */ import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.HandledBlock;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ public class EGPlant
/*    */   extends HandledBlock {
/*    */   boolean edible;
/*    */   private static final int food = 2;
/*    */   
/*    */   public EGPlant(Category category, ItemStack item, String name, RecipeType recipeType, boolean edible, ItemStack[] recipe) {
/* 15 */     super(category, item, name, recipeType, recipe);
/* 16 */     this.edible = edible;
/*    */   }
/*    */   
/*    */   public boolean isEdible() {
/* 20 */     return this.edible;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void restoreHunger(Player p) {
/* 26 */     int level = p.getFoodLevel() + 2;
/* 27 */     p.setFoodLevel((level > 20) ? 20 : level);
/* 28 */     p.setSaturation(2.0F);
/*    */   }
/*    */ }


/* Location:              C:\Users\hai'man\Desktop\粘液科技1.12\server\plugins\[粘液附属异域花园修复版]ExoticGarden-Nar.jar!\me\mrCookieSlime\ExoticGarden\EGPlant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */