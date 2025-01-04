/*    */ package me.mrCookieSlime.ExoticGarden;
/*    */ 
/*    */ public class PlantData
/*    */ {
/*    */   byte data;
/*    */   String texture;
/*    */   
/*    */   public PlantData(byte data) {
/*  9 */     this.data = data;
/* 10 */     this.texture = "NO_SKULL_SPECIFIED";
/*    */   }
/*    */   
/*    */   public PlantData(String texture) {
/* 14 */     this.texture = texture;
/*    */   }
/*    */   
/*    */   public byte toByte() {
/* 18 */     return this.data;
/*    */   }
/*    */   
/*    */   public String getTexture() {
/* 22 */     return this.texture;
/*    */   }
/*    */ }


/* Location:              C:\Users\hai'man\Desktop\粘液科技1.12\server\plugins\[粘液附属异域花园修复版]ExoticGarden-Nar.jar!\me\mrCookieSlime\ExoticGarden\PlantData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */