/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ import java.net.DatagramPacket;
/*    */ import java.net.DatagramSocket;
/*    */ import java.net.InetAddress;
/*    */ import java.net.MulticastSocket;
/*    */ 
/*    */ public class DiscoverableThread extends Thread
/*    */ {
/*  6 */   private static int BUFFER_LENGTH = 1024;
/*  7 */   public static String MULTICAST_ADDRESS = "230.6.6.6";
/*    */   private static final String ID_REQUEST = "RemoteDroid:AnyoneHome";
/*    */   private static final String ID_REQUEST_RESPONSE = "RemoteDroid:ImHome";
/* 12 */   private int port = 57111;
/*    */   private MulticastSocket socket;
/*    */ 
/*    */   public DiscoverableThread()
/*    */   {
/*    */   }
/*    */ 
/*    */   public DiscoverableThread(int port)
/*    */   {
/* 20 */     this.port = port;
/*    */   }
/*    */ 
/*    */   public DiscoverableThread(Runnable target) {
/* 24 */     super(target);
/*    */   }
/*    */ 
/*    */   public DiscoverableThread(String name)
/*    */   {
/* 29 */     super(name);
/*    */   }
/*    */ 
/*    */   public DiscoverableThread(ThreadGroup group, Runnable target)
/*    */   {
/* 34 */     super(group, target);
/*    */   }
/*    */ 
/*    */   public DiscoverableThread(ThreadGroup group, String name)
/*    */   {
/* 39 */     super(group, name);
/*    */   }
/*    */ 
/*    */   public DiscoverableThread(Runnable target, String name)
/*    */   {
/* 44 */     super(target, name);
/*    */   }
/*    */ 
/*    */   public DiscoverableThread(ThreadGroup group, Runnable target, String name)
/*    */   {
/* 49 */     super(group, target, name);
/*    */   }
/*    */ 
/*    */   public DiscoverableThread(ThreadGroup group, Runnable target, String name, long stackSize)
/*    */   {
/* 55 */     super(group, target, name, stackSize);
/*    */   }
/*    */ 
/*    */   public void run()
/*    */   {
/*    */     try
/*    */     {
/* 63 */       byte[] b = new byte[BUFFER_LENGTH];
/* 64 */       DatagramPacket packet = new DatagramPacket(b, b.length);
/* 65 */       this.socket = new MulticastSocket(this.port);
/* 66 */       this.socket.joinGroup(InetAddress.getByName(MULTICAST_ADDRESS));
/*    */       while (true) {
/* 68 */         this.socket.receive(packet);
/* 69 */         handlePacket(packet);
/*    */       }
/*    */     }
/*    */     catch (IOException localIOException) {
/*    */     }
/*    */     catch (InterruptedException localInterruptedException) {
/*    */     }
/*    */   }
/*    */ 
/*    */   private void handlePacket(DatagramPacket packet) throws IOException, InterruptedException {
/* 79 */     String data = new String(packet.getData());
/* 80 */     System.out.println("Got data:" + data);
/* 81 */     if (data.substring(0, "RemoteDroid:AnyoneHome".length()).equals("RemoteDroid:AnyoneHome")) {
/* 82 */       System.out.println("Request message!");
/*    */ 
/* 84 */       byte[] b = "RemoteDroid:ImHome".getBytes();
/* 85 */       DatagramPacket p = new DatagramPacket(b, b.length);
/* 86 */       p.setAddress(packet.getAddress());
/* 87 */       p.setPort(this.port + 1);
/*    */ 
/* 89 */       Thread.sleep(500L);
/* 90 */       DatagramSocket outSocket = new DatagramSocket();
/*    */ 
/* 92 */       outSocket.send(p);
/*    */     }
/*    */   }
/*    */ }

/* Location:           G:\RemoteDroidServer.jar
 * Qualified Name:     DiscoverableThread
 * JD-Core Version:    0.6.2
 */