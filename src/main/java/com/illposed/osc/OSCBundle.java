/*     */ package com.illposed.osc;
/*     */ 
/*     */ import com.illposed.osc.utility.OSCJavaToByteArrayConverter;
/*     */ import java.math.BigInteger;
/*     */ import java.util.Date;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class OSCBundle extends OSCPacket
/*     */ {
/*  34 */   public static final BigInteger SECONDS_FROM_1900_to_1970 = new BigInteger("2208988800");
/*     */ 
/*  39 */   public static final Date TIMESTAMP_IMMEDIATE = new Date(0L);
/*     */   protected Date timestamp;
/*     */   protected Vector packets;
/*     */ 
/*     */   public OSCBundle()
/*     */   {
/*  49 */     this(null, TIMESTAMP_IMMEDIATE);
/*     */   }
/*     */ 
/*     */   public OSCBundle(Date timestamp)
/*     */   {
/*  57 */     this(null, timestamp);
/*     */   }
/*     */ 
/*     */   public OSCBundle(OSCPacket[] packets)
/*     */   {
/*  65 */     this(packets, TIMESTAMP_IMMEDIATE);
/*     */   }
/*     */ 
/*     */   public OSCBundle(OSCPacket[] packets, Date timestamp)
/*     */   {
/*  75 */     if (packets != null) {
/*  76 */       this.packets = new Vector(packets.length);
/*  77 */       for (int i = 0; i < packets.length; i++)
/*  78 */         this.packets.add(packets[i]);
/*     */     }
/*     */     else {
/*  81 */       this.packets = new Vector();
/*  82 */     }this.timestamp = timestamp;
/*  83 */     init();
/*     */   }
/*     */ 
/*     */   public Date getTimestamp()
/*     */   {
/*  91 */     return this.timestamp;
/*     */   }
/*     */ 
/*     */   public void setTimestamp(Date timestamp)
/*     */   {
/*  99 */     this.timestamp = timestamp;
/*     */   }
/*     */ 
/*     */   public void addPacket(OSCPacket packet)
/*     */   {
/* 107 */     this.packets.add(packet);
/*     */   }
/*     */ 
/*     */   public OSCPacket[] getPackets()
/*     */   {
/* 115 */     OSCPacket[] packetArray = new OSCPacket[this.packets.size()];
/* 116 */     this.packets.toArray(packetArray);
/* 117 */     return packetArray;
/*     */   }
/*     */ 
/*     */   protected void computeTimeTagByteArray(OSCJavaToByteArrayConverter stream)
/*     */   {
/* 124 */     if ((this.timestamp == null) || (this.timestamp == TIMESTAMP_IMMEDIATE)) {
/* 125 */       stream.write(0);
/* 126 */       stream.write(1);
/* 127 */       return;
/*     */     }
/*     */ 
/* 130 */     long millisecs = this.timestamp.getTime();
/* 131 */     long secsSince1970 = millisecs / 1000L;
/* 132 */     long secs = secsSince1970 + SECONDS_FROM_1900_to_1970.longValue();
/*     */ 
/* 134 */     long fraction = millisecs % 1000L * 4294967296L / 1000L;
/*     */ 
/* 136 */     stream.write((int)secs);
/* 137 */     stream.write((int)fraction);
/*     */   }
/*     */ 
/*     */   protected void computeByteArray(OSCJavaToByteArrayConverter stream)
/*     */   {
/* 145 */     stream.write("#bundle");
/* 146 */     computeTimeTagByteArray(stream);
/* 147 */     Enumeration en = this.packets.elements();
/*     */ 
/* 150 */     while (en.hasMoreElements()) {
/* 151 */       OSCPacket nextElement = (OSCPacket)en.nextElement();
/* 152 */       byte[] packetBytes = nextElement.getByteArray();
/* 153 */       stream.write(packetBytes.length);
/* 154 */       stream.write(packetBytes);
/*     */     }
/* 156 */     this.byteArray = stream.toByteArray();
/*     */   }
/*     */ }

/* Location:           G:\RemoteDroidServer.jar
 * Qualified Name:     com.illposed.osc.OSCBundle
 * JD-Core Version:    0.6.2
 */