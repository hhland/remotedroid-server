/*    */ package com.illposed.osc;
/*    */ 
/*    */ import java.net.DatagramSocket;
/*    */ 
/*    */ public abstract class OSCPort
/*    */ {
/*    */   protected DatagramSocket socket;
/*    */   protected int port;
/*    */ 
/*    */   public static int defaultSCOSCPort()
/*    */   {
/* 27 */     return 57110;
/*    */   }
/*    */ 
/*    */   public static int defaultSCLangOSCPort()
/*    */   {
/* 34 */     return 57120;
/*    */   }
/*    */ 
/*    */   protected void finalize()
/*    */     throws Throwable
/*    */   {
/* 42 */     super.finalize();
/* 43 */     this.socket.close();
/*    */   }
/*    */ 
/*    */   public void close()
/*    */   {
/* 51 */     this.socket.close();
/*    */   }
/*    */ }

/* Location:           G:\RemoteDroidServer.jar
 * Qualified Name:     com.illposed.osc.OSCPort
 * JD-Core Version:    0.6.2
 */