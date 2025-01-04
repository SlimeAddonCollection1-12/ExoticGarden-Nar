/*     */ package org.jnbt;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.List;
/*     */ import java.util.zip.GZIPOutputStream;
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
/*     */ public final class NBTOutputStream
/*     */   implements Closeable
/*     */ {
/*     */   private final DataOutputStream os;
/*     */   
/*     */   public NBTOutputStream(OutputStream os) throws IOException {
/*  68 */     this.os = new DataOutputStream(new GZIPOutputStream(os));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeTag(Tag tag) throws IOException {
/*  77 */     int type = NBTUtils.getTypeCode((Class)tag.getClass());
/*  78 */     String name = tag.getName();
/*  79 */     byte[] nameBytes = name.getBytes(NBTConstants.CHARSET);
/*     */     
/*  81 */     this.os.writeByte(type);
/*  82 */     this.os.writeShort(nameBytes.length);
/*  83 */     this.os.write(nameBytes);
/*     */     
/*  85 */     if (type == 0) {
/*  86 */       throw new IOException("Named TAG_End not permitted.");
/*     */     }
/*     */     
/*  89 */     writeTagPayload(tag);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeTagPayload(Tag tag) throws IOException {
/*  98 */     int type = NBTUtils.getTypeCode((Class)tag.getClass());
/*  99 */     switch (type) {
/*     */       case 0:
/* 101 */         writeEndTagPayload((EndTag)tag);
/*     */         return;
/*     */       case 1:
/* 104 */         writeByteTagPayload((ByteTag)tag);
/*     */         return;
/*     */       case 2:
/* 107 */         writeShortTagPayload((ShortTag)tag);
/*     */         return;
/*     */       case 3:
/* 110 */         writeIntTagPayload((IntTag)tag);
/*     */         return;
/*     */       case 4:
/* 113 */         writeLongTagPayload((LongTag)tag);
/*     */         return;
/*     */       case 5:
/* 116 */         writeFloatTagPayload((FloatTag)tag);
/*     */         return;
/*     */       case 6:
/* 119 */         writeDoubleTagPayload((DoubleTag)tag);
/*     */         return;
/*     */       case 7:
/* 122 */         writeByteArrayTagPayload((ByteArrayTag)tag);
/*     */         return;
/*     */       case 8:
/* 125 */         writeStringTagPayload((StringTag)tag);
/*     */         return;
/*     */       case 9:
/* 128 */         writeListTagPayload((ListTag)tag);
/*     */         return;
/*     */       case 10:
/* 131 */         writeCompoundTagPayload((CompoundTag)tag);
/*     */         return;
/*     */     } 
/* 134 */     throw new IOException("Invalid tag type: " + type + ".");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeByteTagPayload(ByteTag tag) throws IOException {
/* 144 */     this.os.writeByte(tag.getValue().byteValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeByteArrayTagPayload(ByteArrayTag tag) throws IOException {
/* 153 */     byte[] bytes = tag.getValue();
/* 154 */     this.os.writeInt(bytes.length);
/* 155 */     this.os.write(bytes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeCompoundTagPayload(CompoundTag tag) throws IOException {
/* 164 */     for (Tag childTag : tag.getValue().values()) {
/* 165 */       writeTag(childTag);
/*     */     }
/* 167 */     this.os.writeByte(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeListTagPayload(ListTag tag) throws IOException {
/* 176 */     Class<? extends Tag> clazz = tag.getType();
/* 177 */     List<Tag> tags = tag.getValue();
/* 178 */     int size = tags.size();
/*     */     
/* 180 */     this.os.writeByte(NBTUtils.getTypeCode(clazz));
/* 181 */     this.os.writeInt(size);
/* 182 */     for (int i = 0; i < size; i++) {
/* 183 */       writeTagPayload(tags.get(i));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeStringTagPayload(StringTag tag) throws IOException {
/* 193 */     byte[] bytes = tag.getValue().getBytes(NBTConstants.CHARSET);
/* 194 */     this.os.writeShort(bytes.length);
/* 195 */     this.os.write(bytes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeDoubleTagPayload(DoubleTag tag) throws IOException {
/* 204 */     this.os.writeDouble(tag.getValue().doubleValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeFloatTagPayload(FloatTag tag) throws IOException {
/* 213 */     this.os.writeFloat(tag.getValue().floatValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeLongTagPayload(LongTag tag) throws IOException {
/* 222 */     this.os.writeLong(tag.getValue().longValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeIntTagPayload(IntTag tag) throws IOException {
/* 231 */     this.os.writeInt(tag.getValue().intValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeShortTagPayload(ShortTag tag) throws IOException {
/* 240 */     this.os.writeShort(tag.getValue().shortValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeEndTagPayload(EndTag tag) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 254 */     this.os.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\hai'man\Desktop\粘液科技1.12\server\plugins\[粘液附属异域花园修复版]ExoticGarden-Nar.jar!\org\jnbt\NBTOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */