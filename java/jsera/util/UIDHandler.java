/*    */ package jsera.util;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.util.Vector;
/*    */ 
/*    */ public class UIDHandler
/*    */ {
/*  6 */   private Vector openSlots = new Vector();
/*  7 */   private int cID = 0;
/*    */ 
/*    */   public synchronized int getUID()
/*    */   {
/*    */     int nId;
/*    */     
/* 14 */     if (this.openSlots.size() == 0)
/* 15 */       nId = this.cID++;
/*    */     else {
/* 17 */       nId = ((Integer)this.openSlots.remove(this.openSlots.size() - 1)).intValue();
/*    */     }
/* 19 */     return nId;
/*    */   }
/*    */ 
/*    */   public synchronized void releaseUID(int nID) {
/* 23 */     if (nID < this.cID)
/* 24 */       this.openSlots.add(new Integer(nID));
/*    */     else
/* 26 */       System.out.println("UIDHandler tried to release a UID that's higher than cID");
/*    */   }
/*    */ }

/* Location:           G:\RemoteDroidServer.jar
 * Qualified Name:     jsera.util.UIDHandler
 * JD-Core Version:    0.6.2
 */