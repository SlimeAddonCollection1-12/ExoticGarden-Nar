/*     */ package org.jnbt;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class NBTInputStream
/*     */   implements Closeable
/*     */ {
/*     */   private final DataInputStream is;
/*     */   
/*     */   public NBTInputStream(InputStream is) throws IOException {
/*  71 */     this.is = new DataInputStream(new GZIPInputStream(is));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Tag readTag() throws IOException {
/*  80 */     return readTag(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Tag readTag(int depth) throws IOException {
/*     */     String name;
/*  90 */     int type = this.is.readByte() & 0xFF;
/*     */ 
/*     */     
/*  93 */     if (type != 0) {
/*  94 */       int nameLength = this.is.readShort() & 0xFFFF;
/*  95 */       byte[] nameBytes = new byte[nameLength];
/*  96 */       this.is.readFully(nameBytes);
/*  97 */       name = new String(nameBytes, NBTConstants.CHARSET);
/*     */     } else {
/*  99 */       name = "";
/*     */     } 
/*     */     
/* 102 */     return readTagPayload(type, name, depth);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Tag readTagPayload(int type, String name, int depth) throws IOException {
/*     */     int length;
/*     */     byte[] bytes;
/*     */     int childType;
/*     */     List<Tag> tagList;
/*     */     int i;
/*     */     Map<String, Tag> tagMap;
/* 114 */     switch (type) {
/*     */       case 0:
/* 116 */         if (depth == 0) {
/* 117 */           throw new IOException("TAG_End found without a TAG_Compound/TAG_List tag preceding it.");
/*     */         }
/* 119 */         return new EndTag();
/*     */       
/*     */       case 1:
/* 122 */         return new ByteTag(name, this.is.readByte());
/*     */       case 2:
/* 124 */         return new ShortTag(name, this.is.readShort());
/*     */       case 3:
/* 126 */         return new IntTag(name, this.is.readInt());
/*     */       case 4:
/* 128 */         return new LongTag(name, this.is.readLong());
/*     */       case 5:
/* 130 */         return new FloatTag(name, this.is.readFloat());
/*     */       case 6:
/* 132 */         return new DoubleTag(name, this.is.readDouble());
/*     */       case 7:
/* 134 */         length = this.is.readInt();
/* 135 */         bytes = new byte[length];
/* 136 */         this.is.readFully(bytes);
/* 137 */         return new ByteArrayTag(name, bytes);
/*     */       case 8:
/* 139 */         length = this.is.readShort();
/* 140 */         bytes = new byte[length];
/* 141 */         this.is.readFully(bytes);
/* 142 */         return new StringTag(name, new String(bytes, NBTConstants.CHARSET));
/*     */       case 9:
/* 144 */         childType = this.is.readByte();
/* 145 */         length = this.is.readInt();
/*     */         
/* 147 */         tagList = new ArrayList<>();
/* 148 */         for (i = 0; i < length; i++) {
/* 149 */           Tag tag = readTagPayload(childType, "", depth + 1);
/* 150 */           if (tag instanceof EndTag) {
/* 151 */             throw new IOException("TAG_End not permitted in a list.");
/*     */           }
/* 153 */           tagList.add(tag);
/*     */         } 
/*     */         
/* 156 */         return new ListTag(name, NBTUtils.getTypeClass(childType), tagList);
/*     */       case 10:
/* 158 */         tagMap = new HashMap<>();
/*     */         while (true) {
/* 160 */           Tag tag = readTag(depth + 1);
/* 161 */           if (tag instanceof EndTag) {
/*     */             break;
/*     */           }
/* 164 */           tagMap.put(tag.getName(), tag);
/*     */         } 
/*     */ 
/*     */         
/* 168 */         return new CompoundTag(name, tagMap);
/*     */     } 
/* 170 */     throw new IOException("Invalid tag type: " + type + ".");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 176 */     this.is.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\hai'man\Desktop\粘液科技1.12\server\plugins\[粘液附属异域花园修复版]ExoticGarden-Nar.jar!\org\jnbt\NBTInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */