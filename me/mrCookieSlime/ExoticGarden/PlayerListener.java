/*    */ package me.mrCookieSlime.ExoticGarden;
/*    */ 
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.player.PlayerJoinEvent;
/*    */ import org.bukkit.event.player.PlayerMoveEvent;
/*    */ import org.bukkit.event.player.PlayerQuitEvent;
/*    */ import org.bukkit.util.Vector;
/*    */ 
/*    */ public class PlayerListener
/*    */   implements Listener {
/*    */   @EventHandler
/*    */   public void onPlayerJoin(PlayerJoinEvent event) {
/* 15 */     ExoticGarden.instance.initPlayerData(event.getPlayer());
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   public void onPlayerQuit(PlayerQuitEvent event) {
/* 20 */     ExoticGarden.instance.saveDatas(event.getPlayer());
/* 21 */     ExoticGarden.drunkPlayers.remove(event.getPlayer().getName());
/*    */   }
/*    */ 
/*    */   
/*    */   @EventHandler
/*    */   public void move(PlayerMoveEvent event) {
/* 27 */     PlayerAlcohol playerAlcohol = ExoticGarden.drunkPlayers.get(event.getPlayer().getName());
/* 28 */     if (playerAlcohol.getAlcohol() >= 30 && playerAlcohol.isDrunk())
/*    */     {
/* 30 */       if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getZ() != event.getTo().getZ()) {
/* 31 */         Player player = event.getPlayer();
/*    */         
/* 33 */         if (player.isOnGround()) {
/*    */ 
/*    */           
/* 36 */           Vector push = new Vector(0, 0, 0);
/* 37 */           push.setX((Math.random() - 0.5D) / 2.0D);
/* 38 */           push.setZ((Math.random() - 0.5D) / 2.0D);
/* 39 */           player.setVelocity(push);
/*    */         } 
/*    */       } 
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\hai'man\Desktop\粘液科技1.12\server\plugins\[粘液附属异域花园修复版]ExoticGarden-Nar.jar!\me\mrCookieSlime\ExoticGarden\PlayerListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */