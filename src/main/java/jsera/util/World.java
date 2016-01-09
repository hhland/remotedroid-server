/*    */ package jsera.util;
/*    */ 
/*    */ import java.awt.Graphics;
/*    */ 
/*    */ public abstract class World
/*    */   implements Updatable
/*    */ {
/*    */   protected int ID;
/*  7 */   protected static UIDHandler oUID = new UIDHandler();
/*    */   protected String[] aImages;
/*    */ 
/*    */   public World()
/*    */   {
/* 12 */     this.ID = oUID.getUID();
/*    */   }
/*    */ 
/*    */   public void finalize() {
/* 16 */     oUID.releaseUID(this.ID);
/*    */   }
/*    */ 
/*    */   public boolean equals(World o) {
/* 20 */     boolean result = false;
/* 21 */     if (o.ID == this.ID) {
/* 22 */       result = true;
/*    */     }
/* 24 */     return result;
/*    */   }
/*    */ 
/*    */   public void update(float elapsed)
/*    */   {
/*    */   }
/*    */ 
/*    */   public void onEnter()
/*    */   {
/*    */   }
/*    */ 
/*    */   public void onExit()
/*    */   {
/*    */   }
/*    */ 
/*    */   public void onPlay()
/*    */   {
/*    */   }
/*    */ 
/*    */   public void onPause()
/*    */   {
/*    */   }
/*    */ 
/*    */   public void paint(Graphics g)
/*    */   {
/*    */   }
/*    */ 
/*    */   public void init()
/*    */   {
/*    */   }
/*    */ }

/* Location:           G:\RemoteDroidServer.jar
 * Qualified Name:     jsera.util.World
 * JD-Core Version:    0.6.2
 */