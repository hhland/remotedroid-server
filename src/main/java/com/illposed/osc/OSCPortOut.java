/*    */ package com.illposed.osc;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.DatagramPacket;
/*    */ import java.net.DatagramSocket;
/*    */ import java.net.InetAddress;
/*    */ import java.net.SocketException;
/*    */ import java.net.UnknownHostException;
/*    */ 
/*    */ public class OSCPortOut extends OSCPort
/*    */ {
/*    */   protected InetAddress address;
/*    */ 
/*    */   public OSCPortOut(InetAddress newAddress, int newPort)
/*    */     throws SocketException
/*    */   {
/* 45 */     this.socket = new DatagramSocket();
/* 46 */     this.address = newAddress;
/* 47 */     this.port = newPort;
/*    */   }
/*    */ 
/*    */   public OSCPortOut(InetAddress newAddress)
/*    */     throws SocketException
/*    */   {
/* 57 */     this(newAddress, defaultSCOSCPort());
/*    */   }
/*    */ 
/*    */   public OSCPortOut()
/*    */     throws UnknownHostException, SocketException
/*    */   {
/* 66 */     this(InetAddress.getLocalHost(), defaultSCOSCPort());
/*    */   }
/*    */ 
/*    */   public void send(OSCPacket aPacket)
/*    */     throws IOException
/*    */   {
/* 74 */     byte[] byteArray = aPacket.getByteArray();
/* 75 */     DatagramPacket packet = 
/* 76 */       new DatagramPacket(byteArray, byteArray.length, this.address, this.port);
/* 77 */     this.socket.send(packet);
/*    */   }
/*    */ }

/* Location:           G:\RemoteDroidServer.jar
 * Qualified Name:     com.illposed.osc.OSCPortOut
 * JD-Core Version:    0.6.2
 */