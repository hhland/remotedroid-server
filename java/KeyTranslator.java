/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.jar.JarFile;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class KeyTranslator
/*     */ {
/*     */   public HashMap codes;
/*     */   private int[] modifiers;
/*     */   private int[] shifts;
/*     */   private int[] ctrls;
/*     */   private int[] leftClicks;
/*     */   protected Document myDoc;
/*     */ 
/*     */   public KeyTranslator()
/*     */   {
/*  26 */     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/*  27 */     String sPath = "config.xml";
/*     */     try {
/*  29 */       DocumentBuilder builder = factory.newDocumentBuilder();
/*  30 */       if (AppFrame.jar == null)
/*  31 */         this.myDoc = builder.parse(new File((AppFrame.basePath + sPath).replace('\\', '/')));
/*     */       else
/*  33 */         this.myDoc = builder.parse(AppFrame.jar.getInputStream(AppFrame.jar.getJarEntry(sPath)));
/*     */     }
/*     */     catch (SAXException sxe)
/*     */     {
/*  37 */       Exception x = sxe;
/*  38 */       if (sxe.getException() != null)
/*  39 */         x = sxe.getException();
/*  40 */       x.printStackTrace();
/*     */     }
/*     */     catch (ParserConfigurationException pce)
/*     */     {
/*  44 */       pce.printStackTrace();
/*     */     }
/*     */     catch (IOException ioe)
/*     */     {
/*  48 */       ioe.printStackTrace();
/*     */     }
/*  50 */     Element config = this.myDoc.getDocumentElement();
/*     */ 
/*  52 */     NodeList mods = config.getElementsByTagName("modifier");
/*  53 */     int l = mods.getLength();
/*     */ 
/*  55 */     this.modifiers = new int[l];
/*  56 */     for (int i = 0; i < l; i++) {
/*  57 */       this.modifiers[i] = Integer.parseInt(((Element)mods.item(i)).getAttribute("code"));
/*     */     }
/*     */ 
/*  60 */     mods = config.getElementsByTagName("shift");
/*  61 */     l = mods.getLength();
/*  62 */     this.shifts = new int[l];
/*  63 */     for (int i = 0; i < l; i++) {
/*  64 */       this.shifts[i] = Integer.parseInt(((Element)mods.item(i)).getAttribute("code"));
/*     */     }
/*     */ 
/*  67 */     mods = config.getElementsByTagName("ctrl");
/*  68 */     l = mods.getLength();
/*  69 */     this.ctrls = new int[l];
/*  70 */     for (int i = 0; i < l; i++) {
/*  71 */       this.ctrls[i] = Integer.parseInt(((Element)mods.item(i)).getAttribute("code"));
/*     */     }
/*     */ 
/*  74 */     this.codes = new HashMap();
/*     */ 
/*  78 */     mods = config.getElementsByTagName("key");
/*  79 */     l = mods.getLength();
/*  80 */     for (int i = 0; i < l; i++) {
/*  81 */       KeyCodeData data = new KeyCodeData();
/*  82 */       Element keydata = (Element)mods.item(i);
/*  83 */       data.name = keydata.getAttribute("name");
/*  84 */       data.modshifted = ("1".compareTo(keydata.getAttribute("modshift")) == 0);
/*  85 */       data.localcode = Integer.parseInt(keydata.getAttribute("localcode"));
/*  86 */       data.modifiedcode = Integer.parseInt(keydata.getAttribute("modified"));
/*  87 */       data.shifted = ("1".compareTo(keydata.getAttribute("shifted")) == 0);
/*  88 */       data.shiftedcode = Integer.parseInt(keydata.getAttribute("shiftedcode"));
/*  89 */       int keycode = Integer.parseInt(keydata.getAttribute("code"));
/*     */ 
/*  91 */       this.codes.put(new Integer(keycode), data);
/*     */     }
/*     */ 
/*  94 */     mods = config.getElementsByTagName("leftclick");
/*  95 */     l = mods.getLength();
/*  96 */     this.leftClicks = new int[l];
/*  97 */     for (int i = 0; i < l; i++)
/*  98 */       this.leftClicks[i] = Integer.parseInt(((Element)mods.item(i)).getAttribute("code"));
/*     */   }
/*     */ 
/*     */   public boolean isModifier(int keycode)
/*     */   {
/* 104 */     int l = this.modifiers.length;
/* 105 */     for (int i = 0; i < l; i++) {
/* 106 */       if (keycode == this.modifiers[i]) {
/* 107 */         return true;
/*     */       }
/*     */     }
/* 110 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isShift(int keycode)
/*     */   {
/* 115 */     int l = this.shifts.length;
/* 116 */     for (int i = 0; i < l; i++) {
/* 117 */       if (keycode == this.shifts[i]) {
/* 118 */         return true;
/*     */       }
/*     */     }
/* 121 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isCtrl(int keycode)
/*     */   {
/* 126 */     int l = this.ctrls.length;
/* 127 */     for (int i = 0; i < l; i++) {
/* 128 */       if (keycode == this.ctrls[i]) {
/* 129 */         return true;
/*     */       }
/*     */     }
/* 132 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isLeftClick(int keycode)
/*     */   {
/* 137 */     int l = this.leftClicks.length;
/* 138 */     for (int i = 0; i < l; i++) {
/* 139 */       if (keycode == this.leftClicks[i]) {
/* 140 */         return true;
/*     */       }
/*     */     }
/* 143 */     return false;
/*     */   }
/*     */ }

/* Location:           G:\RemoteDroidServer.jar
 * Qualified Name:     KeyTranslator
 * JD-Core Version:    0.6.2
 */