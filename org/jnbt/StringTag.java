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
/*    */ public final class StringTag
/*    */   extends Tag
/*    */ {
/*    */   private final String value;
/*    */   
/*    */   public StringTag(String name, String value) {
/* 54 */     super(name);
/* 55 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getValue() {
/* 60 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 65 */     String name = getName();
/* 66 */     String append = "";
/* 67 */     if (name != null && !name.equals("")) {
/* 68 */       append = "(\"" + getName() + "\")";
/*    */     }
/* 70 */     return "TAG_String" + append + ": " + this.value;
/*    */   }
/*    */ }


/* Location:              C:\Users\hai'man\Desktop\粘液科技1.12\server\plugins\[粘液附属异域花园修复版]ExoticGarden-Nar.jar!\org\jnbt\StringTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */