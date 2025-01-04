/*    */ package me.mrCookieSlime.ExoticGarden.recipe;
/*    */ 
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ public class DefaultSubRecipe {
/*    */   int chance;
/*    */   ItemStack item;
/*    */   
/*    */   public DefaultSubRecipe(int chance, ItemStack item) {
/* 10 */     this.chance = chance;
/* 11 */     this.item = item;
/*    */   }
/*    */   
/*    */   public int getChance() {
/* 15 */     return this.chance;
/*    */   }
/*    */   
/*    */   public ItemStack getItem() {
/* 19 */     return this.item;
/*    */   }
/*    */ }


/* Location:              C:\Users\hai'man\Desktop\粘液科技1.12\server\plugins\[粘液附属异域花园修复版]ExoticGarden-Nar.jar!\me\mrCookieSlime\ExoticGarden\recipe\DefaultSubRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */