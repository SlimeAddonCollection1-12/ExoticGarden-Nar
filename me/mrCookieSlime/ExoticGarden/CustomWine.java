/*    */ package me.mrCookieSlime.ExoticGarden;
/*    */ 
/*    */ import com.narcissu14.sanity.Sanity;
/*    */ import me.mrCookieSlime.Slimefun.Lists.RecipeType;
/*    */ import me.mrCookieSlime.Slimefun.Objects.Category;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.bukkit.potion.PotionEffect;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CustomWine
/*    */   extends EGPlant
/*    */ {
/*    */   float food;
/*    */   float sanity;
/*    */   int alcohol;
/* 21 */   PotionEffect[] effects = null;
/*    */   
/*    */   public CustomWine(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe, int food, float sanity, int alcohol, PotionEffect[] effects) {
/* 24 */     super(category, item, name, recipeType, true, recipe);
/* 25 */     this.food = food;
/* 26 */     this.sanity = sanity;
/* 27 */     this.alcohol = alcohol;
/* 28 */     this.effects = effects;
/*    */   }
/*    */   
/*    */   public CustomWine(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe, int food, float sanity, int alcohol) {
/* 32 */     super(category, item, name, recipeType, true, recipe);
/* 33 */     this.food = food;
/* 34 */     this.sanity = sanity;
/* 35 */     this.alcohol = alcohol;
/*    */   }
/*    */   
/*    */   public CustomWine(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe, int food, int alcohol) {
/* 39 */     super(category, item, name, recipeType, true, recipe);
/* 40 */     this.food = food;
/* 41 */     this.sanity = food;
/* 42 */     this.alcohol = alcohol;
/*    */   }
/*    */ 
/*    */   
/*    */   public void restoreHunger(Player p) {
/* 47 */     int level = p.getFoodLevel() + (int)this.food;
/* 48 */     p.setFoodLevel((level > 20) ? 20 : level);
/* 49 */     p.setSaturation(this.food);
/* 50 */     if (ExoticGarden.instance.isSanityEnabled()) {
/* 51 */       Sanity.getInstance().addSanity(p, this.sanity);
/*    */     }
/* 53 */     ((PlayerAlcohol)ExoticGarden.drunkPlayers.get(p.getName())).addAlcohol(this.alcohol);
/* 54 */     if (this.effects != null) {
/* 55 */       for (PotionEffect potion : this.effects) {
/* 56 */         p.addPotionEffect(potion);
/*    */       }
/*    */     }
/* 59 */     int alcohol = ((PlayerAlcohol)ExoticGarden.drunkPlayers.get(p.getName())).getAlcohol();
/* 60 */     if (alcohol < 100 && alcohol > 50) {
/* 61 */       p.sendMessage("§8[§a异域森林§8] §e你已经半醉了，请适度饮酒！");
/* 62 */     } else if (alcohol >= 100) {
/* 63 */       p.sendMessage("§8[§a异域森林§8] §e你醉了！可以尝试食用一些可以§b解酒§e的消耗品");
/* 64 */       ExoticGarden.sendDrunkMessage(p);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\hai'man\Desktop\粘液科技1.12\server\plugins\[粘液附属异域花园修复版]ExoticGarden-Nar.jar!\me\mrCookieSlime\ExoticGarden\CustomWine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */