/*    */ package org.jnbt;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class DoubleTag
/*    */   extends Tag
/*    */ {
/*    */   private final double value;
/*    */   
/*    */   public DoubleTag(String name, double value) {
/* 54 */     super(name);
/* 55 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public Double getValue() {
/* 60 */     return Double.valueOf(this.value);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 65 */     String name = getName();
/* 66 */     String append = "";
/* 67 */     if (name != null && !name.equals("")) {
/* 68 */       append = "(\"" + getName() + "\")";
/*    */     }
/* 70 */     return "TAG_Double" + append + ": " + this.value;
/*    */   }
/*    */ }


/* Location:              C:\Users\hai'man\Desktop\粘液科技1.12\server\plugins\[粘液附属异域花园修复版]ExoticGarden-Nar.jar!\org\jnbt\DoubleTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */