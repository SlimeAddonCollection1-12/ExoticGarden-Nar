/*    */ package me.mrCookieSlime.ExoticGarden.recipe;
/*    */ 
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ public class DefaultMachineRecipe
/*    */ {
/*    */   int ticks;
/*    */   ItemStack[] input;
/*    */   ItemStack[] output;
/*    */   
/*    */   public DefaultMachineRecipe(int seconds, ItemStack[] input, ItemStack[] output) {
/* 12 */     this.ticks = seconds * 2;
/* 13 */     this.input = input;
/* 14 */     this.output = output;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack[] getInput() {
/* 19 */     return this.input;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack[] getOutput() {
/* 24 */     return this.output;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setTicks(int ticks) {
/* 29 */     this.ticks = ticks;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getTicks() {
/* 34 */     return this.ticks;
/*    */   }
/*    */ }


/* Location:              C:\Users\hai'man\Desktop\粘液科技1.12\server\plugins\[粘液附属异域花园修复版]ExoticGarden-Nar.jar!\me\mrCookieSlime\ExoticGarden\recipe\DefaultMachineRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */