/*    */ package org.jnbt;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.Map;
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
/*    */ public final class CompoundTag
/*    */   extends Tag
/*    */ {
/*    */   private final Map<String, Tag> value;
/*    */   
/*    */   public CompoundTag(String name, Map<String, Tag> value) {
/* 57 */     super(name);
/* 58 */     this.value = Collections.unmodifiableMap(value);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Tag> getValue() {
/* 63 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 68 */     String name = getName();
/* 69 */     String append = "";
/* 70 */     if (name != null && !name.equals("")) {
/* 71 */       append = "(\"" + getName() + "\")";
/*    */     }
/* 73 */     StringBuilder bldr = new StringBuilder();
/* 74 */     bldr.append("TAG_Compound" + append + ": " + this.value.size() + " entries\r\n{\r\n");
/* 75 */     for (Map.Entry<String, Tag> entry : this.value.entrySet()) {
/* 76 */       bldr.append("   " + ((Tag)entry.getValue()).toString().replaceAll("\r\n", "\r\n   ") + "\r\n");
/*    */     }
/* 78 */     bldr.append("}");
/* 79 */     return bldr.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\hai'man\Desktop\粘液科技1.12\server\plugins\[粘液附属异域花园修复版]ExoticGarden-Nar.jar!\org\jnbt\CompoundTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */