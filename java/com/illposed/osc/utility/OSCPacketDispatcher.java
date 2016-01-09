/*    */ package com.illposed.osc.utility;
/*    */ 
/*    */ import com.illposed.osc.OSCBundle;
/*    */ import com.illposed.osc.OSCListener;
/*    */ import com.illposed.osc.OSCMessage;
/*    */ import com.illposed.osc.OSCPacket;
/*    */ import java.util.Date;
/*    */ import java.util.Enumeration;
/*    */ import java.util.Hashtable;
/*    */ 
/*    */ public class OSCPacketDispatcher
/*    */ {
/* 26 */   private Hashtable addressToClassTable = new Hashtable();
/*    */ 
/*    */   public void addListener(String address, OSCListener listener)
/*    */   {
/* 36 */     this.addressToClassTable.put(address, listener);
/*    */   }
/*    */ 
/*    */   public void dispatchPacket(OSCPacket packet) {
/* 40 */     if ((packet instanceof OSCBundle))
/* 41 */       dispatchBundle((OSCBundle)packet);
/*    */     else
/* 43 */       dispatchMessage((OSCMessage)packet);
/*    */   }
/*    */ 
/*    */   public void dispatchPacket(OSCPacket packet, Date timestamp) {
/* 47 */     if ((packet instanceof OSCBundle))
/* 48 */       dispatchBundle((OSCBundle)packet);
/*    */     else
/* 50 */       dispatchMessage((OSCMessage)packet, timestamp);
/*    */   }
/*    */ 
/*    */   private void dispatchBundle(OSCBundle bundle) {
/* 54 */     Date timestamp = bundle.getTimestamp();
/* 55 */     OSCPacket[] packets = bundle.getPackets();
/* 56 */     for (int i = 0; i < packets.length; i++)
/* 57 */       dispatchPacket(packets[i], timestamp);
/*    */   }
/*    */ 
/*    */   private void dispatchMessage(OSCMessage message)
/*    */   {
/* 62 */     dispatchMessage(message, null);
/*    */   }
/*    */ 
/*    */   private void dispatchMessage(OSCMessage message, Date time) {
/* 66 */     Enumeration keys = this.addressToClassTable.keys();
/* 67 */     while (keys.hasMoreElements()) {
/* 68 */       String key = keys.nextElement().toString();
/*    */ 
/* 72 */       if (key.equals(message.getAddress())) {
/* 73 */         OSCListener listener = (OSCListener)this.addressToClassTable.get(key);
/* 74 */         listener.acceptMessage(time, message);
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           G:\RemoteDroidServer.jar
 * Qualified Name:     com.illposed.osc.utility.OSCPacketDispatcher
 * JD-Core Version:    0.6.2
 */