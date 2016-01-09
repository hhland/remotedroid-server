/*    */ import java.net.InetAddress;
/*    */ import java.net.NetworkInterface;
/*    */ import java.net.SocketException;
/*    */ import java.net.UnknownHostException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Enumeration;
/*    */ 
/*    */ public class LinuxInetAddress
/*    */ {
/*    */   public static InetAddress getLocalHost()
/*    */     throws UnknownHostException
/*    */   {
/* 16 */     InetAddress localHost = InetAddress.getLocalHost();
/* 17 */     if (!localHost.isLoopbackAddress()) return localHost;
/* 18 */     InetAddress[] addrs = getAllLocalUsingNetworkInterface();
/* 19 */     for (int i = 0; i < addrs.length; i++)
/*    */     {
/* 21 */       if ((!addrs[i].isLoopbackAddress()) && (addrs[i].getHostAddress().contains("."))) return addrs[i];
/*    */     }
/* 23 */     return localHost;
/*    */   }
/*    */ 
/*    */   public static InetAddress[] getAllLocal()
/*    */     throws UnknownHostException
/*    */   {
/* 36 */     InetAddress[] iAddresses = InetAddress.getAllByName("127.0.0.1");
/* 37 */     if (iAddresses.length != 1) return iAddresses;
/* 38 */     if (!iAddresses[0].isLoopbackAddress()) return iAddresses;
/* 39 */     return getAllLocalUsingNetworkInterface();
/*    */   }
/*    */ 
/*    */   private static InetAddress[] getAllLocalUsingNetworkInterface()
/*    */     throws UnknownHostException
/*    */   {
/* 50 */     ArrayList addresses = new ArrayList();
/* 51 */     Enumeration e = null;
/*    */     try {
/* 53 */       e = NetworkInterface.getNetworkInterfaces();
/*    */     } catch (SocketException ex) {
/* 55 */       throw new UnknownHostException("127.0.0.1");
/*    */     }
/*    */     Enumeration e2;
/* 57 */     for (; e.hasMoreElements(); 
/* 59 */       e2.hasMoreElements())
/*    */     {
/* 58 */       NetworkInterface ni = (NetworkInterface)e.nextElement();
/* 59 */       e2 = ni.getInetAddresses(); 
/* 60 */       addresses.add(e2.nextElement());
/*    */     }
/*    */ 
/* 63 */     InetAddress[] iAddresses = new InetAddress[addresses.size()];
/* 64 */     for (int i = 0; i < iAddresses.length; i++) {
/* 65 */       iAddresses[i] = ((InetAddress)addresses.get(i));
/*    */     }
/* 67 */     return iAddresses;
/*    */   }
/*    */ }

/* Location:           G:\RemoteDroidServer.jar
 * Qualified Name:     LinuxInetAddress
 * JD-Core Version:    0.6.2
 */