/*     */ package com.illposed.osc;
/*     */ 
/*     */ import com.illposed.osc.utility.OSCJavaToByteArrayConverter;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class OSCMessage extends OSCPacket
/*     */ {
/*     */   protected String address;
/*     */   protected Vector arguments;
/*     */ 
/*     */   public OSCMessage()
/*     */   {
/*  35 */     this.arguments = new Vector();
/*     */   }
/*     */ 
/*     */   public OSCMessage(String newAddress)
/*     */   {
/*  43 */     this(newAddress, null);
/*     */   }
/*     */ 
/*     */   public OSCMessage(String newAddress, Object[] newArguments)
/*     */   {
/*  53 */     this.address = newAddress;
/*  54 */     if (newArguments != null) {
/*  55 */       this.arguments = new Vector(newArguments.length);
/*  56 */       for (int i = 0; i < newArguments.length; i++)
/*  57 */         this.arguments.add(newArguments[i]);
/*     */     }
/*     */     else {
/*  60 */       this.arguments = new Vector();
/*  61 */     }init();
/*     */   }
/*     */ 
/*     */   public String getAddress()
/*     */   {
/*  69 */     return this.address;
/*     */   }
/*     */ 
/*     */   public void setAddress(String anAddress)
/*     */   {
/*  77 */     this.address = anAddress;
/*     */   }
/*     */ 
/*     */   public void addArgument(Object argument)
/*     */   {
/*  85 */     this.arguments.add(argument);
/*     */   }
/*     */ 
/*     */   public Object[] getArguments()
/*     */   {
/*  93 */     return this.arguments.toArray();
/*     */   }
/*     */ 
/*     */   protected void computeAddressByteArray(OSCJavaToByteArrayConverter stream)
/*     */   {
/* 101 */     stream.write(this.address);
/*     */   }
/*     */ 
/*     */   protected void computeArgumentsByteArray(OSCJavaToByteArrayConverter stream)
/*     */   {
/* 109 */     stream.write(',');
/* 110 */     if (this.arguments == null)
/* 111 */       return;
/* 112 */     stream.writeTypes(this.arguments);
/* 113 */     Enumeration en = this.arguments.elements();
/* 114 */     while (en.hasMoreElements())
/* 115 */       stream.write(en.nextElement());
/*     */   }
/*     */ 
/*     */   protected void computeByteArray(OSCJavaToByteArrayConverter stream)
/*     */   {
/* 124 */     computeAddressByteArray(stream);
/* 125 */     computeArgumentsByteArray(stream);
/* 126 */     this.byteArray = stream.toByteArray();
/*     */   }
/*     */ }

/* Location:           G:\RemoteDroidServer.jar
 * Qualified Name:     com.illposed.osc.OSCMessage
 * JD-Core Version:    0.6.2
 */