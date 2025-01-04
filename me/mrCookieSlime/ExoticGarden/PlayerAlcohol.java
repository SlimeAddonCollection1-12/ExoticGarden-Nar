/*    */ package me.mrCookieSlime.ExoticGarden;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import org.bukkit.configuration.ConfigurationSection;
/*    */ import org.bukkit.configuration.file.YamlConfiguration;
/*    */ 
/*    */ public class PlayerAlcohol
/*    */ {
/*    */   String player;
/*    */   int alcohol;
/*    */   boolean isDrunk;
/*    */   
/*    */   public PlayerAlcohol(String player, int alcohol) {
/* 15 */     this.player = player;
/* 16 */     this.alcohol = alcohol;
/* 17 */     this.isDrunk = false;
/*    */     try {
/* 19 */       YamlConfiguration storge = ExoticGarden.instance.getYamlStorge();
/* 20 */       ConfigurationSection section = storge.createSection("Players");
/* 21 */       section.set(player + ".Alcohol", Integer.valueOf(0));
/* 22 */       section.set(player + ".Drunk", Boolean.valueOf(false));
/* 23 */       storge.save(new File(ExoticGarden.instance.getDataFolder() + File.separator + "storge.yml"));
/* 24 */     } catch (IOException e) {
/* 25 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */   
/*    */   public PlayerAlcohol(String player, int alcohol, boolean isDrunk) {
/* 30 */     this.player = player;
/* 31 */     this.alcohol = alcohol;
/* 32 */     this.isDrunk = isDrunk;
/*    */   }
/*    */   
/*    */   public int getAlcohol() {
/* 36 */     return this.alcohol;
/*    */   }
/*    */   
/*    */   public void setAlcohol(int alcohol) {
/* 40 */     if (alcohol < 0) {
/* 41 */       this.alcohol = 0;
/*    */       return;
/*    */     } 
/* 44 */     this.alcohol = alcohol;
/*    */   }
/*    */   
/*    */   public void addAlcohol(int alcohol) {
/* 48 */     int result = this.alcohol + alcohol;
/* 49 */     if (result < 0) {
/* 50 */       this.alcohol = 0;
/*    */       return;
/*    */     } 
/* 53 */     this.alcohol = result;
/*    */   }
/*    */   
/*    */   public boolean isDrunk() {
/* 57 */     return this.isDrunk;
/*    */   }
/*    */   
/*    */   public void setDrunk(boolean drunk) {
/* 61 */     this.isDrunk = drunk;
/*    */   }
/*    */   
/*    */   public String getPlayer() {
/* 65 */     return this.player;
/*    */   }
/*    */   
/*    */   public boolean check() {
/* 69 */     if (this.alcohol >= 100) {
/* 70 */       if (!this.isDrunk) {
/* 71 */         this.isDrunk = true;
/*    */       }
/* 73 */       return true;
/*    */     } 
/* 75 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\hai'man\Desktop\粘液科技1.12\server\plugins\[粘液附属异域花园修复版]ExoticGarden-Nar.jar!\me\mrCookieSlime\ExoticGarden\PlayerAlcohol.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */