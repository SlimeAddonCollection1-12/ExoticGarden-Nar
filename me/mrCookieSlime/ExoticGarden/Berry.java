/*    */ package me.mrCookieSlime.ExoticGarden;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Berry
/*    */ {
/*    */   ItemStack item;
/*    */   String name;
/*    */   PlantData data;
/*    */   PlantType type;
/*    */   
/*    */   public Berry(String name, PlantType type, PlantData data) {
/* 20 */     this.name = name;
/* 21 */     this.data = data;
/* 22 */     this.type = type;
/*    */   }
/*    */   
/*    */   public Berry(ItemStack item, String name, PlantType type, PlantData data) {
/* 26 */     this.item = item;
/* 27 */     this.name = name;
/* 28 */     this.data = data;
/* 29 */     this.type = type;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 33 */     return this.name;
/*    */   }
/*    */   
/*    */   public ItemStack getItem() {
/* 37 */     switch (this.type) { case ORE_PLANT:
/* 38 */         return this.item; }
/* 39 */      return SlimefunItem.getByName(this.name).getItem();
/*    */   }
/*    */ 
/*    */   
/*    */   public PlantData getData() {
/* 44 */     return this.data;
/*    */   }
/*    */   
/*    */   public PlantType getType() {
/* 48 */     return this.type;
/*    */   }
/*    */   
/*    */   public String toBush() {
/* 52 */     switch (this.type) { case ORE_PLANT:
/* 53 */         return this.name.replace("_ESSENCE", "_PLANT"); }
/* 54 */      return this.name + "_BUSH";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSoil(Material type) {
/* 59 */     List<Material> soils = Arrays.asList(new Material[] { Material.GRASS, Material.DIRT });
/* 60 */     return soils.contains(type);
/*    */   }
/*    */ }


/* Location:              C:\Users\hai'man\Desktop\粘液科技1.12\server\plugins\[粘液附属异域花园修复版]ExoticGarden-Nar.jar!\me\mrCookieSlime\ExoticGarden\Berry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */