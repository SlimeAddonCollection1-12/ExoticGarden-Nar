/*     */ package me.mrCookieSlime.ExoticGarden.CSCoreLibSetup;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.json.simple.JSONArray;
/*     */ import org.json.simple.JSONObject;
/*     */ import org.json.simple.JSONValue;
/*     */ 
/*     */ 
/*     */ public class CSCoreLibLoader
/*     */ {
/*     */   Plugin plugin;
/*     */   URL url;
/*     */   URL download;
/*     */   File file;
/*     */   
/*     */   public CSCoreLibLoader(Plugin plugin) {
/*  27 */     this.plugin = plugin;
/*     */     try {
/*  29 */       this.url = new URL("https://api.curseforge.com/servermods/files?projectIds=88802");
/*  30 */     } catch (MalformedURLException malformedURLException) {}
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean load() {
/*  35 */     if (this.plugin.getServer().getPluginManager().isPluginEnabled("CS-CoreLib")) return true;
/*     */     
/*  37 */     System.err.println(" ");
/*  38 */     System.err.println("#################### - INFO - ####################");
/*  39 */     System.err.println(" ");
/*  40 */     System.err.println(this.plugin.getName() + " could not be loaded.");
/*  41 */     System.err.println("It appears that you have not installed CS-CoreLib");
/*  42 */     System.err.println("Your Server will now try to download and install");
/*  43 */     System.err.println("CS-CoreLib for you.");
/*  44 */     System.err.println("You will be asked to restart your Server when it's finished.");
/*  45 */     System.err.println("If this somehow fails, please download and install CS-CoreLib manually:");
/*  46 */     System.err.println("https://dev.bukkit.org/projects/cs-corelib");
/*  47 */     System.err.println(" ");
/*  48 */     System.err.println("#################### - INFO - ####################");
/*  49 */     System.err.println(" ");
/*  50 */     this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/*  54 */             if (CSCoreLibLoader.this.connect()) CSCoreLibLoader.this.install(); 
/*     */           }
/*     */         },  10L);
/*  57 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean connect() {
/*     */     try {
/*  63 */       URLConnection connection = this.url.openConnection();
/*  64 */       connection.setConnectTimeout(5000);
/*  65 */       connection.addRequestProperty("User-Agent", "CS-CoreLib Loader (by mrCookieSlime)");
/*  66 */       connection.setDoOutput(true);
/*     */       
/*  68 */       BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
/*  69 */       JSONArray array = (JSONArray)JSONValue.parse(reader.readLine());
/*  70 */       this.download = traceURL(((String)((JSONObject)array.get(array.size() - 1)).get("downloadUrl")).replace("https:", "http:"));
/*  71 */       this.file = new File("plugins/" + (String)((JSONObject)array.get(array.size() - 1)).get("name") + ".jar");
/*     */       
/*  73 */       return true;
/*  74 */     } catch (IOException e) {
/*  75 */       System.err.println(" ");
/*  76 */       System.err.println("#################### - WARNING - ####################");
/*  77 */       System.err.println(" ");
/*  78 */       System.err.println("Could not connect to BukkitDev.");
/*  79 */       System.err.println("Please download & install CS-CoreLib manually:");
/*  80 */       System.err.println("https://dev.bukkit.org/projects/cs-corelib");
/*  81 */       System.err.println(" ");
/*  82 */       System.err.println("#################### - WARNING - ####################");
/*  83 */       System.err.println(" ");
/*  84 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private URL traceURL(String location) throws IOException {
/*  89 */     HttpURLConnection connection = null;
/*     */     while (true) {
/*     */       String loc;
/*  92 */       URL url = new URL(location);
/*  93 */       connection = (HttpURLConnection)url.openConnection();
/*     */       
/*  95 */       connection.setInstanceFollowRedirects(false);
/*  96 */       connection.setConnectTimeout(5000);
/*  97 */       connection.addRequestProperty("User-Agent", "Auto Updater (by mrCookieSlime)");
/*     */       
/*  99 */       switch (connection.getResponseCode()) {
/*     */         case 301:
/*     */         case 302:
/* 102 */           loc = connection.getHeaderField("Location");
/* 103 */           location = (new URL(new URL(location), loc)).toExternalForm();
/*     */           continue;
/*     */       } 
/*     */       
/*     */       break;
/*     */     } 
/* 109 */     return new URL(connection.getURL().toString().replaceAll(" ", "%20"));
/*     */   }
/*     */   
/*     */   private void install() {
/* 113 */     BufferedInputStream input = null;
/* 114 */     FileOutputStream output = null;
/*     */     try {
/* 116 */       input = new BufferedInputStream(this.download.openStream());
/* 117 */       output = new FileOutputStream(this.file);
/*     */       
/* 119 */       byte[] data = new byte[1024];
/*     */       int read;
/* 121 */       while ((read = input.read(data, 0, 1024)) != -1) {
/* 122 */         output.write(data, 0, read);
/*     */       }
/* 124 */     } catch (Exception ex) {
/* 125 */       System.err.println(" ");
/* 126 */       System.err.println("#################### - WARNING - ####################");
/* 127 */       System.err.println(" ");
/* 128 */       System.err.println("Failed to download CS-CoreLib");
/* 129 */       System.err.println("Please download & install CS-CoreLib manually:");
/* 130 */       System.err.println("https://dev.bukkit.org/projects/cs-corelib");
/* 131 */       System.err.println(" ");
/* 132 */       System.err.println("#################### - WARNING - ####################");
/* 133 */       System.err.println(" ");
/*     */     } finally {
/*     */       try {
/* 136 */         if (input != null) input.close(); 
/* 137 */         if (output != null) output.close(); 
/* 138 */         System.err.println(" ");
/* 139 */         System.err.println("#################### - INFO - ####################");
/* 140 */         System.err.println(" ");
/* 141 */         System.err.println("Please restart your Server to finish the Installation");
/* 142 */         System.err.println("of " + this.plugin.getName() + " and CS-CoreLib");
/* 143 */         System.err.println(" ");
/* 144 */         System.err.println("#################### - INFO - ####################");
/* 145 */         System.err.println(" ");
/* 146 */       } catch (IOException e) {
/* 147 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\hai'man\Desktop\粘液科技1.12\server\plugins\[粘液附属异域花园修复版]ExoticGarden-Nar.jar!\me\mrCookieSlime\ExoticGarden\CSCoreLibSetup\CSCoreLibLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */