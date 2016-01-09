/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.Frame;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.MediaTracker;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.URL;
/*     */ import java.net.UnknownHostException;
/*     */ import java.security.CodeSource;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.jar.JarFile;
/*     */ import javax.swing.Timer;
/*     */ 
/*     */ public class AppFrame extends Frame
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   public static JarFile jar;
/*  18 */   public static String basePath = "";
/*     */   public static InetAddress localAddr;
/*  22 */   private String[] textLines = new String[6];
/*     */   private Image imLogo;
/*     */   private Image imHelp;
/*     */   private Font fontTitle;
/*     */   private Font fontText;
/*     */   private Timer timer;
/*  31 */   private int height = 510;
/*  32 */   private int width = 540;
/*     */   private OSCWorld world;
/*  36 */   private String appName = "RemoteDroid Server R2";
/*     */   private Toolkit toolkit;
/*     */   private MediaTracker tracker;
/*     */ 
/*     */   public AppFrame()
/*     */   {
/*  43 */     GlobalData.oFrame = this;
/*  44 */     setSize(this.width, this.height);
/*     */ 
/*  46 */     this.toolkit = Toolkit.getDefaultToolkit();
/*  47 */     this.tracker = new MediaTracker(this);
/*     */ 
/*  51 */     String sHost = "";
/*     */     try {
/*  53 */       localAddr = InetAddress.getLocalHost();
/*  54 */       if (localAddr.isLoopbackAddress()) {
/*  55 */         localAddr = LinuxInetAddress.getLocalHost();
/*     */       }
/*  57 */       sHost = localAddr.getHostAddress();
/*     */     } catch (UnknownHostException ex) {
/*  59 */       sHost = "Error finding local IP.";
/*     */     }
/*     */ 
/*  62 */     this.textLines[0] = "The RemoteDroid server application is now running.";
/*  63 */     this.textLines[1] = "";
/*  64 */     this.textLines[2] = ("Your IP address is: " + sHost);
/*  65 */     this.textLines[3] = "";
/*  66 */     this.textLines[4] = "Enter this IP address on the start screen of the";
/*  67 */     this.textLines[5] = "RemoteDroid application on your phone to begin.";
/*     */     try
/*     */     {
/*  70 */       URL fileURL = getClass().getProtectionDomain().getCodeSource().getLocation();
/*  71 */       String sBase = fileURL.toString();
/*  72 */       if ("jar".equals(sBase.substring(sBase.length() - 3, sBase.length()))) {
/*  73 */         jar = new JarFile(new File(fileURL.toURI()));
/*     */       }
/*     */       else
/*  76 */         basePath = System.getProperty("user.dir") + "\\res\\";
/*     */     }
/*     */     catch (Exception ex) {
/*  79 */       this.textLines[1] = ("exception: " + ex.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public Image getImage(String sImage)
/*     */   {
/*  86 */     Image imReturn = null;
/*     */     try {
/*  88 */       if (jar == null) {
/*  89 */         imReturn = this.toolkit.createImage(getClass().getClassLoader().getResource(sImage));
/*     */       }
/*     */       else {
/*  92 */         BufferedInputStream bis = new BufferedInputStream(jar.getInputStream(jar.getEntry(sImage)));
/*  93 */         ByteArrayOutputStream buffer = new ByteArrayOutputStream(4096);
/*     */         int b;
/*  95 */         while ((b = bis.read()) != -1)
/*     */         {
/*     */           
/*  96 */           buffer.write(b);
/*     */         }
/*  98 */         byte[] imageBuffer = buffer.toByteArray();
/*  99 */         imReturn = this.toolkit.createImage(imageBuffer);
/* 100 */         bis.close();
/* 101 */         buffer.close();
/*     */       }
/*     */     }
/*     */     catch (IOException localIOException) {
/*     */     }
/* 106 */     return imReturn;
/*     */   }
/*     */ 
/*     */   public void init()
/*     */   {
/*     */     try {
/* 112 */       this.imLogo = getImage("icon.gif");
/* 113 */       this.tracker.addImage(this.imLogo, 0);
/* 114 */       this.tracker.waitForID(0);
/*     */     }
/*     */     catch (InterruptedException localInterruptedException)
/*     */     {
/*     */     }
/*     */     try {
/* 120 */       this.imHelp = getImage("helpphoto.jpg");
/* 121 */       this.tracker.addImage(this.imHelp, 1);
/* 122 */       this.tracker.waitForID(1);
/*     */     }
/*     */     catch (InterruptedException localInterruptedException1) {
/*     */     }
/* 126 */     this.fontTitle = new Font("Verdana", 1, 16);
/* 127 */     this.fontText = new Font("Verdana", 0, 11);
/* 128 */     setBackground(Color.BLACK);
/* 129 */     setForeground(Color.WHITE);
/*     */ 
/* 131 */     this.timer = new Timer(500, new ActionListener() {
/*     */       public void actionPerformed(ActionEvent ev) {
/* 133 */         AppFrame.this.world = new OSCWorld();
/* 134 */         AppFrame.this.world.onEnter();
/*     */ 
/* 136 */         AppFrame.this.repaint();
/* 137 */         AppFrame.this.timer.stop();
/*     */       }
/*     */     });
/* 140 */     this.timer.start();
/*     */   }
/*     */ 
/*     */   public void paint(Graphics g)
/*     */   {
/* 145 */     g.setColor(getBackground());
/* 146 */     g.fillRect(0, 0, this.width, this.height);
/* 147 */     g.setColor(getForeground());
/*     */ 
/* 149 */     g.drawImage(this.imLogo, 10, 30, this);
/* 150 */     g.setFont(this.fontTitle);
/* 151 */     g.drawString(this.appName, 70, 55);
/*     */ 
/* 153 */     g.setFont(this.fontText);
/* 154 */     int startY = 90;
/* 155 */     int l = 6;
/* 156 */     for (int i = 0; i < l; i++) {
/* 157 */       g.drawString(this.textLines[i], 10, startY);
/* 158 */       startY += 13;
/*     */     }
/*     */ 
/* 161 */     g.drawImage(this.imHelp, 20, startY + 10, this);
/*     */   }
/*     */ }

/* Location:           G:\RemoteDroidServer.jar
 * Qualified Name:     AppFrame
 * JD-Core Version:    0.6.2
 */