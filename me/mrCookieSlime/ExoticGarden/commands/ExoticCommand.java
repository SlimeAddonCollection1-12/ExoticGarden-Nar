/*    */ package me.mrCookieSlime.ExoticGarden.commands;
/*    */ 
/*    */ import me.mrCookieSlime.ExoticGarden.ExoticGarden;
/*    */ import me.mrCookieSlime.ExoticGarden.PlayerAlcohol;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandExecutor;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class ExoticCommand implements CommandExecutor {
/*    */   public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
/* 13 */     if (strings.length == 3) {
/* 14 */       if (hasPermission(commandSender, "exoticgarden.admin") && 
/* 15 */         strings[0].equalsIgnoreCase("alo") && 
/* 16 */         strings[1].equalsIgnoreCase("info")) {
/* 17 */         if (Bukkit.getPlayer(strings[2]).isOnline()) {
/* 18 */           commandSender.sendMessage("§8[§b异域森林§8] §7玩家§e" + strings[2] + "§7的酒精度为§e" + ((PlayerAlcohol)ExoticGarden.drunkPlayers.get(strings[2])).getAlcohol());
/*    */         } else {
/* 20 */           commandSender.sendMessage("§8[§b异域森林§8] §c指定的玩家不在线！");
/*    */         } 
/*    */       }
/*    */ 
/*    */       
/* 25 */       return true;
/* 26 */     }  if (strings.length == 4) {
/* 27 */       if (hasPermission(commandSender, "exoticgarden.admin")) {
/* 28 */         if (strings[1].equalsIgnoreCase("add")) {
/* 29 */           if (Bukkit.getPlayer(strings[2]).isOnline()) {
/* 30 */             ((PlayerAlcohol)ExoticGarden.drunkPlayers.get(strings[2])).addAlcohol(Integer.valueOf(strings[3]).intValue());
/* 31 */             commandSender.sendMessage("§8[§b异域森林§8] §7为玩家§e" + strings[2] + "§7增加了§e" + strings[3] + "§酒精度");
/*    */           } else {
/* 33 */             commandSender.sendMessage("§8[§b异域森林§8] §c指定的玩家不在线！");
/*    */           } 
/* 35 */         } else if (strings[1].equalsIgnoreCase("set")) {
/* 36 */           if (Bukkit.getPlayer(strings[2]).isOnline()) {
/* 37 */             ((PlayerAlcohol)ExoticGarden.drunkPlayers.get(strings[2])).setAlcohol(Integer.valueOf(strings[3]).intValue());
/* 38 */             commandSender.sendMessage("§8[§b异域森林§8] §7将玩家§e" + strings[2] + "§7的酒精度设置为§e" + strings[3]);
/*    */           } else {
/* 40 */             commandSender.sendMessage("§8[§b异域森林§8] §c指定的玩家不在线！");
/*    */           } 
/*    */         } 
/*    */       }
/* 44 */       return true;
/*    */     } 
/* 46 */     sendHelp(commandSender);
/* 47 */     return true;
/*    */   }
/*    */   
/*    */   private void sendHelp(CommandSender sender) {
/* 51 */     if (hasPermission(sender, "exoticgarden.admin")) {
/* 52 */       String[] help = { "        §7--------§8====§e[ §b异域森林 §e]§8====§7--------", "§b/exotic help                         §7显示帮助信息", "§b/exotic alo info <玩家名>            §7查看指定玩家酒精度", "§b/exotic alo add <玩家名> <值>        §增加/减少 酒精度", "§b/exotic alo set <玩家名> <值>        §7设定 酒精度" };
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 57 */       sender.sendMessage(help);
/*    */     } else {
/* 59 */       sender.sendMessage("§c你没有权限这么做!");
/*    */     } 
/*    */   }
/*    */   
/*    */   private boolean hasPermission(CommandSender sender, String perms) {
/* 64 */     if (!(sender instanceof Player)) {
/* 65 */       return true;
/*    */     }
/* 67 */     Player player = (Player)sender;
/* 68 */     return (player.hasPermission(perms) || player.isOp());
/*    */   }
/*    */ }


/* Location:              C:\Users\hai'man\Desktop\粘液科技1.12\server\plugins\[粘液附属异域花园修复版]ExoticGarden-Nar.jar!\me\mrCookieSlime\ExoticGarden\commands\ExoticCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */