/*    */ package com.illposed.osc;
/*    */ 
/*    */ import com.illposed.osc.utility.OSCJavaToByteArrayConverter;
/*    */ 
/*    */ public abstract class OSCPacket
/*    */ {
/*    */   protected boolean isByteArrayComputed;
/*    */   protected byte[] byteArray;
/*    */ 
/*    */   protected void computeByteArray()
/*    */   {
/* 33 */     OSCJavaToByteArrayConverter stream = new OSCJavaToByteArrayConverter();
/* 34 */     computeByteArray(stream);
/*    */   }
/*    */ 
/*    */   protected abstract void computeByteArray(OSCJavaToByteArrayConverter paramOSCJavaToByteArrayConverter);
/*    */ 
/*    */   public byte[] getByteArray()
/*    */   {
/* 49 */     if (!this.isByteArrayComputed)
/* 50 */       computeByteArray();
/* 51 */     return this.byteArray;
/*    */   }
/*    */ 
/*    */   protected void init()
/*    */   {
/*    */   }
/*    */ }

/* Location:           G:\RemoteDroidServer.jar
 * Qualified Name:     com.illposed.osc.OSCPacket
 * JD-Core Version:    0.6.2
 */