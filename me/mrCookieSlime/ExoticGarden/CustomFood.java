/*    */ package me.mrCookieSlime.ExoticGarden;
/*    */ 
/*    */ import com.narcissu14.sanity.Sanity;
/*    */ import me.mrCookieSlime.Slimefun.Lists.RecipeType;
/*    */ import me.mrCookieSlime.Slimefun.Objects.Category;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ public class CustomFood
/*    */   extends EGPlant
/*    */ {
/*    */   float food;
/*    */   float sanity;
/*    */   
/*    */   public CustomFood(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe, int food, float sanity) {
/* 16 */     super(category, item, name, recipeType, true, recipe);
/* 17 */     this.food = food;
/* 18 */     this.sanity = sanity;
/*    */   }
/*    */   
/*    */   public CustomFood(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe, int food) {
/* 22 */     super(category, item, name, recipeType, true, recipe);
/* 23 */     this.food = food;
/* 24 */     this.sanity = food;
/*    */   }
/*    */ 
/*    */   
/*    */   public void restoreHunger(Player p) {
/* 29 */     int level = p.getFoodLevel() + (int)this.food;
/* 30 */     p.setFoodLevel((level > 20) ? 20 : level);
/* 31 */     p.setSaturation(this.food);
/* 32 */     if (ExoticGarden.instance.isSanityEnabled())
/* 33 */       Sanity.getInstance().addSanity(p, this.sanity); 
/*    */   }
/*    */ }


/* Location:              C:\Users\hai'man\Desktop\粘液科技1.12\server\plugins\[粘液附属异域花园修复版]ExoticGarden-Nar.jar!\me\mrCookieSlime\ExoticGarden\CustomFood.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */