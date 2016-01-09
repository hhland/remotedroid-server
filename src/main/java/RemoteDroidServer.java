/*    */ import java.awt.event.WindowAdapter;
/*    */ import java.awt.event.WindowEvent;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class RemoteDroidServer
/*    */ {
/*    */   private static AppFrame f;
/*    */ 
/*    */   public static void main(String[] args)
/*    */   {
/* 16 */     f = new AppFrame();
/* 17 */     f.setVisible(true);
/* 18 */     f.setResizable(false);
/* 19 */     f.setTitle("RemoteDroid Server");
/*    */ 
/* 21 */     f.addWindowListener(new WindowAdapter() {
/*    */       public void windowClosing(WindowEvent e) {
/* 23 */         RemoteDroidServer.f.setVisible(false);
/* 24 */         RemoteDroidServer.f.dispose();
/* 25 */         System.exit(0);
/*    */       }
/*    */     });
/* 32 */     f.init();
/*    */ 
/* 34 */     System.out.println(System.getProperty("os.name"));
/*    */   }
/*    */ }

/* Location:           G:\RemoteDroidServer.jar
 * Qualified Name:     RemoteDroidServer
 * JD-Core Version:    0.6.2
 */