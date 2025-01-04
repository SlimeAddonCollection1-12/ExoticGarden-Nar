/*     */ package me.mrCookieSlime.ExoticGarden;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
/*     */ import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
/*     */ import me.mrCookieSlime.Slimefun.api.BlockStorage;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.SkullType;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.BlockFace;
/*     */ import org.bukkit.block.Skull;
/*     */ import org.jnbt.ByteArrayTag;
/*     */ import org.jnbt.CompoundTag;
/*     */ import org.jnbt.NBTInputStream;
/*     */ import org.jnbt.ShortTag;
/*     */ import org.jnbt.Tag;
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
/*     */ public class Schematic
/*     */ {
/*     */   private short[] blocks;
/*     */   private byte[] data;
/*     */   private short width;
/*     */   private short lenght;
/*     */   private short height;
/*     */   private String name;
/*     */   
/*     */   public Schematic(String name, short[] blocks, byte[] data, short width, short lenght, short height) {
/*  59 */     this.blocks = blocks;
/*  60 */     this.data = data;
/*  61 */     this.width = width;
/*  62 */     this.lenght = lenght;
/*  63 */     this.height = height;
/*  64 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short[] getBlocks() {
/*  72 */     return this.blocks;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  76 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getData() {
/*  84 */     return this.data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getWidth() {
/*  92 */     return this.width;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getLenght() {
/* 100 */     return this.lenght;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getHeight() {
/* 108 */     return this.height;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void pasteSchematic(Location loc, Tree tree) {
/* 114 */     Schematic schematic = null;
/*     */     try {
/* 116 */       schematic = tree.getSchematic();
/* 117 */     } catch (IOException iOException) {}
/*     */     
/* 119 */     BlockFace[] bf = { BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST };
/* 120 */     short[] blocks = schematic.getBlocks();
/* 121 */     byte[] blockData = schematic.getData();
/*     */     
/* 123 */     short length = schematic.getLenght();
/* 124 */     short width = schematic.getWidth();
/* 125 */     short height = schematic.getHeight();
/*     */     
/* 127 */     for (int x = 0; x < width; x++) {
/* 128 */       for (int y = 0; y < height; y++) {
/* 129 */         for (int z = 0; z < length; z++) {
/* 130 */           int index = y * width * length + z * width + x;
/* 131 */           Block block = (new Location(loc.getWorld(), x + loc.getX() - (length / 2), y + loc.getY(), z + loc.getZ() - (width / 2))).getBlock();
/* 132 */           if ((block.getType().equals(null) || block.getType().equals(Material.AIR) || block.getType().isTransparent()) && 
/* 133 */             Material.getMaterial(blocks[index]) != null && 
/* 134 */             !(block.getState() instanceof org.bukkit.inventory.InventoryHolder)) {
/* 135 */             if (blocks[index] != 0) block.setTypeIdAndData(blocks[index], blockData[index], false); 
/* 136 */             if (Material.getMaterial(blocks[index]) == Material.LEAVES || Material.getMaterial(blocks[index]) == Material.LEAVES_2) {
/* 137 */               if (CSCoreLib.randomizer().nextInt(100) < 25) BlockStorage.store(block, tree.getItem()); 
/* 138 */               block.setData((byte)0);
/*     */             }
/* 140 */             else if (Material.getMaterial(blocks[index]) == Material.SKULL && block.getState() instanceof Skull) {
/* 141 */               Skull s = (Skull)block.getState();
/* 142 */               s.setSkullType(SkullType.PLAYER);
/* 143 */               s.setRotation(bf[(new Random()).nextInt(bf.length)]);
/* 144 */               s.setRawData((byte)1);
/* 145 */               s.update();
/*     */               
/*     */               try {
/* 148 */                 CustomSkull.setSkull(s.getBlock(), tree.getTexture());
/* 149 */               } catch (Exception e) {
/* 150 */                 e.printStackTrace();
/*     */               } 
/* 152 */               BlockStorage.store(s.getBlock(), tree.getFruit());
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Schematic loadSchematic(File file) throws IOException {
/* 165 */     FileInputStream stream = new FileInputStream(file);
/* 166 */     NBTInputStream nbtStream = new NBTInputStream(stream);
/*     */     
/* 168 */     CompoundTag schematicTag = (CompoundTag)nbtStream.readTag();
/* 169 */     if (!schematicTag.getName().equals("Schematic")) {
/* 170 */       throw new IllegalArgumentException("Tag \"Schematic\" does not exist or is not first");
/*     */     }
/*     */     
/* 173 */     Map<String, Tag> schematic = schematicTag.getValue();
/* 174 */     if (!schematic.containsKey("Blocks")) {
/* 175 */       throw new IllegalArgumentException("Schematic file is missing a \"Blocks\" tag");
/*     */     }
/*     */     
/* 178 */     short width = ((ShortTag)getChildTag(schematic, "Width", ShortTag.class)).getValue().shortValue();
/* 179 */     short length = ((ShortTag)getChildTag(schematic, "Length", ShortTag.class)).getValue().shortValue();
/* 180 */     short height = ((ShortTag)getChildTag(schematic, "Height", ShortTag.class)).getValue().shortValue();
/*     */ 
/*     */     
/* 183 */     byte[] blockId = ((ByteArrayTag)getChildTag(schematic, "Blocks", ByteArrayTag.class)).getValue();
/* 184 */     byte[] blockData = ((ByteArrayTag)getChildTag(schematic, "Data", ByteArrayTag.class)).getValue();
/* 185 */     byte[] addId = new byte[0];
/* 186 */     short[] blocks = new short[blockId.length];
/*     */ 
/*     */ 
/*     */     
/* 190 */     if (schematic.containsKey("AddBlocks")) {
/* 191 */       addId = ((ByteArrayTag)getChildTag(schematic, "AddBlocks", ByteArrayTag.class)).getValue();
/*     */     }
/*     */ 
/*     */     
/* 195 */     for (int index = 0; index < blockId.length; index++) {
/* 196 */       if (index >> 1 >= addId.length) {
/* 197 */         blocks[index] = (short)(blockId[index] & 0xFF);
/*     */       }
/* 199 */       else if ((index & 0x1) == 0) {
/* 200 */         blocks[index] = (short)(((addId[index >> 1] & 0xF) << 8) + (blockId[index] & 0xFF));
/*     */       } else {
/* 202 */         blocks[index] = (short)(((addId[index >> 1] & 0xF0) << 4) + (blockId[index] & 0xFF));
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 207 */     return new Schematic(file.getName().replace(".schematic", ""), blocks, blockData, width, length, height);
/*     */   }
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
/*     */   private static <T extends Tag> T getChildTag(Map<String, Tag> items, String key, Class<T> expected) throws IllegalArgumentException {
/* 222 */     if (!items.containsKey(key)) {
/* 223 */       throw new IllegalArgumentException("Schematic file is missing a \"" + key + "\" tag");
/*     */     }
/* 225 */     Tag tag = items.get(key);
/* 226 */     if (!expected.isInstance(tag)) {
/* 227 */       throw new IllegalArgumentException(key + " tag is not of tag type " + expected.getName());
/*     */     }
/* 229 */     return expected.cast(tag);
/*     */   }
/*     */ }


/* Location:              C:\Users\hai'man\Desktop\粘液科技1.12\server\plugins\[粘液附属异域花园修复版]ExoticGarden-Nar.jar!\me\mrCookieSlime\ExoticGarden\Schematic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */