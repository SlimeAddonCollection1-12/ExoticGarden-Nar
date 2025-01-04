/*    */ package me.mrCookieSlime.ExoticGarden;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ public class Tree {
/*    */   String sapling;
/*    */   String schematic;
/*    */   String texture;
/*    */   String fruit;
/*    */   List<Material> soils;
/*    */   
/*    */   public Tree(String name, String fruit, String texture, Material... soil) {
/* 19 */     this.sapling = name + "_SAPLING";
/* 20 */     this.schematic = name + "_TREE";
/* 21 */     this.texture = texture;
/* 22 */     this.fruit = fruit;
/* 23 */     this.soils = Arrays.asList(soil);
/*    */   }
/*    */   
/*    */   public Schematic getSchematic() throws IOException {
/* 27 */     return Schematic.loadSchematic(new File("plugins/ExoticGarden/" + this.schematic + ".schematic"));
/*    */   }
/*    */   
/*    */   public ItemStack getItem() {
/* 31 */     return SlimefunItem.getByName(this.sapling).getItem();
/*    */   }
/*    */   
/*    */   public String getTexture() {
/* 35 */     return this.texture;
/*    */   }
/*    */   
/*    */   public ItemStack getFruit() {
/* 39 */     return SlimefunItem.getByName(this.fruit).getItem();
/*    */   }
/*    */   
/*    */   public String getSapling() {
/* 43 */     return this.sapling;
/*    */   }
/*    */   
/*    */   public boolean isSoil(Material material) {
/* 47 */     return this.soils.contains(material);
/*    */   }
/*    */ }


/* Location:              C:\Users\hai'man\Desktop\粘液科技1.12\server\plugins\[粘液附属异域花园修复版]ExoticGarden-Nar.jar!\me\mrCookieSlime\ExoticGarden\Tree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */