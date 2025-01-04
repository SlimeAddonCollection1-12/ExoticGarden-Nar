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
/*    */ public abstract class Tag
/*    */ {
/*    */   private final String name;
/*    */   
/*    */   public Tag(String name) {
/* 53 */     this.name = name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final String getName() {
/* 61 */     return this.name;
/*    */   }
/*    */   
/*    */   public abstract Object getValue();
/*    */ }


/* Location:              C:\Users\hai'man\Desktop\粘液科技1.12\server\plugins\[粘液附属异域花园修复版]ExoticGarden-Nar.jar!\org\jnbt\Tag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */