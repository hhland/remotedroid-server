/*     */ package com.illposed.osc;
/*     */ 
/*     */ import com.illposed.osc.utility.OSCByteArrayToJavaConverter;
/*     */ import com.illposed.osc.utility.OSCPacketDispatcher;
/*     */ import java.io.IOException;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.DatagramSocket;
/*     */ import java.net.InetAddress;
/*     */ import java.net.SocketException;
/*     */ 
/*     */ public class OSCPortIn extends OSCPort
/*     */   implements Runnable
/*     */ {
/*     */   protected boolean isListening;
/*  40 */   protected OSCByteArrayToJavaConverter converter = new OSCByteArrayToJavaConverter();
/*  41 */   protected OSCPacketDispatcher dispatcher = new OSCPacketDispatcher();
/*     */ 
/*     */   public OSCPortIn(int port)
/*     */     throws SocketException
/*     */   {
/*  49 */     this.socket = new DatagramSocket(port);
/*  50 */     this.port = port;
/*     */   }
/*     */ 
/*     */   public OSCPortIn(int port, InetAddress addr) throws SocketException {
/*  54 */     this.socket = new DatagramSocket(port, addr);
/*  55 */     this.port = port;
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/*  65 */     byte[] buffer = new byte[1536];
/*  66 */     DatagramPacket packet = new DatagramPacket(buffer, 1536);
/*  67 */     while (this.isListening)
/*     */       try {
/*  69 */         this.socket.receive(packet);
/*  70 */         OSCPacket oscPacket = this.converter.convert(buffer, packet.getLength());
/*  71 */         this.dispatcher.dispatchPacket(oscPacket);
/*     */       } catch (IOException e) {
/*  73 */         e.printStackTrace();
/*     */       }
/*     */   }
/*     */ 
/*     */   public void startListening()
/*     */   {
/*  82 */     this.isListening = true;
/*  83 */     Thread thread = new Thread(this);
/*  84 */     thread.start();
/*     */   }
/*     */ 
/*     */   public void stopListening()
/*     */   {
/*  91 */     this.isListening = false;
/*     */   }
/*     */ 
/*     */   public boolean isListening()
/*     */   {
/*  98 */     return this.isListening;
/*     */   }
/*     */ 
/*     */   public void addListener(String anAddress, OSCListener listener)
/*     */   {
/* 107 */     this.dispatcher.addListener(anAddress, listener);
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/* 115 */     this.socket.close();
/*     */   }
/*     */ }

/* Location:           G:\RemoteDroidServer.jar
 * Qualified Name:     com.illposed.osc.OSCPortIn
 * JD-Core Version:    0.6.2
 */