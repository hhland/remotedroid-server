/*     */ import com.illposed.osc.OSCListener;
/*     */ import com.illposed.osc.OSCMessage;
/*     */ import com.illposed.osc.OSCPort;
/*     */ import com.illposed.osc.OSCPortIn;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.GraphicsDevice;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.Label;
/*     */ import java.awt.MouseInfo;
/*     */ import java.awt.Point;
/*     */ import java.awt.PointerInfo;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Robot;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.io.PrintStream;
/*     */ import java.net.InetAddress;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import jsera.util.World;
/*     */ 
/*     */ public class OSCWorld extends World
/*     */ {
/*     */   private static final float sensitivity = 1.6F;
/*     */   private OSCPortIn receiver;
/*     */   private Robot robot;
/*  36 */   private boolean shifted = false;
/*  37 */   private boolean modified = false;
/*     */   private KeyTranslator translator;
/*     */   private GraphicsDevice[] gDevices;
/*     */   private Rectangle[] gBounds;
/*     */   private Label lbDebug;
/*  46 */   private int scrollMod = -1;
/*     */ 
/*  48 */   private float xLeftover = 0.0F;
/*  49 */   private float yLeftover = 0.0F;
/*     */   private DiscoverableThread discoverable;
/*     */ 
/*     */   public void onEnter()
/*     */   {
/*     */     try
/*     */     {
/*  60 */       this.robot = new Robot();
/*  61 */       this.robot.setAutoDelay(5);
/*     */ 
/*  63 */       this.translator = new KeyTranslator();
/*     */ 
/*  65 */       InetAddress local = InetAddress.getLocalHost();
/*  66 */       if (local.isLoopbackAddress())
/*  67 */         this.receiver = new OSCPortIn(OSCPort.defaultSCOSCPort());
/*     */       else {
/*  69 */         this.receiver = new OSCPortIn(OSCPort.defaultSCOSCPort());
/*     */       }
/*  71 */       OSCListener listener = new OSCListener() {
/*     */         public void acceptMessage(Date time, OSCMessage message) {
/*  73 */           Object[] args = message.getArguments();
/*  74 */           if (args.length == 3)
/*  75 */             OSCWorld.this.mouseEvent(Integer.parseInt(args[0].toString()), Float.parseFloat(args[1]
/*  76 */               .toString()), Float.parseFloat(args[2].toString()));
/*     */         }
/*     */       };
/*  80 */       this.receiver.addListener("/mouse", listener);
/*     */ 
/*  82 */       listener = new OSCListener() {
/*     */         public void acceptMessage(Date time, OSCMessage message) {
/*  84 */           Object[] args = message.getArguments();
/*  85 */           if (args.length == 1)
/*  86 */             OSCWorld.this.buttonEvent(Integer.parseInt(args[0].toString()), 0);
/*     */         }
/*     */       };
/*  90 */       this.receiver.addListener("/leftbutton", listener);
/*     */ 
/*  92 */       listener = new OSCListener() {
/*     */         public void acceptMessage(Date time, OSCMessage message) {
/*  94 */           Object[] args = message.getArguments();
/*  95 */           if (args.length == 1)
/*  96 */             OSCWorld.this.buttonEvent(Integer.parseInt(args[0].toString()), 2);
/*     */         }
/*     */       };
/* 100 */       this.receiver.addListener("/rightbutton", listener);
/*     */ 
/* 102 */       listener = new OSCListener() {
/*     */         public void acceptMessage(Date time, OSCMessage message) {
/* 104 */           Object[] args = message.getArguments();
/* 105 */           if (args.length == 3) {
/* 106 */             OSCWorld.this.keyboardEvent(Integer.parseInt(args[0].toString()), 
/* 107 */               Integer.parseInt(args[1].toString()), args[2].toString());
/*     */           }
/* 109 */           if (args.length == 2)
/* 110 */             OSCWorld.this.keyboardEvent(Integer.parseInt(args[0].toString()), 
/* 111 */               Integer.parseInt(args[1].toString()));
/*     */         }
/*     */       };
/* 115 */       this.receiver.addListener("/keyboard", listener);
/*     */ 
/* 117 */       listener = new OSCListener() {
/*     */         public void acceptMessage(Date time, OSCMessage message) {
/* 119 */           Object[] args = message.getArguments();
/* 120 */           if (args.length == 1)
/* 121 */             OSCWorld.this.scrollEvent(Integer.parseInt(args[0].toString()));
/*     */         }
/*     */       };
/* 125 */       this.receiver.addListener("/wheel", listener);
/*     */ 
/* 127 */       listener = new OSCListener() {
/*     */         public void acceptMessage(Date time, OSCMessage message) {
/* 129 */           Object[] args = message.getArguments();
/* 130 */           if (args.length == 6)
/* 131 */             OSCWorld.this.orientEvent(Float.parseFloat(args[0].toString()), Float.parseFloat(args[1]
/* 132 */               .toString()), Float.parseFloat(args[2].toString()), 
/* 133 */               Float.parseFloat(args[3].toString()), Float.parseFloat(args[4]
/* 134 */               .toString()), Float.parseFloat(args[5].toString()));
/*     */         }
/*     */       };
/* 138 */       this.receiver.addListener("/orient", listener);
/*     */ 
/* 140 */       this.receiver.startListening();
/*     */ 
/* 142 */       GlobalData.oFrame.addKeyListener(new KeyListener() {
/*     */         public void keyReleased(KeyEvent e) {
/* 144 */           OSCWorld.this.nativeKeyEvent(e);
/*     */         }
/*     */ 
/*     */         public void keyPressed(KeyEvent e)
/*     */         {
/*     */         }
/*     */ 
/*     */         public void keyTyped(KeyEvent e)
/*     */         {
/*     */         }
/*     */       });
/* 156 */       this.gDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
/* 157 */       int l = this.gDevices.length;
/* 158 */       this.gBounds = new Rectangle[l];
/* 159 */       for (int i = 0; i < l; i++) {
/* 160 */         this.gBounds[i] = this.gDevices[i].getDefaultConfiguration().getBounds();
/*     */       }
/*     */ 
/* 163 */       initUI();
/*     */ 
/* 165 */       if (System.getProperty("os.name").compareToIgnoreCase("Mac OS X") == 0)
/*     */       {
/* 167 */         this.scrollMod = 1;
/*     */       }
/*     */ 
/* 170 */       this.discoverable = new DiscoverableThread(OSCPort.defaultSCOSCPort() + 1);
/* 171 */       this.discoverable.start();
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   private void nativeKeyEvent(KeyEvent ev)
/*     */   {
/*     */   }
/*     */ 
/*     */   private void mouseEvent(int type, float xOffset, float yOffset)
/*     */   {
/* 185 */     if (type == 2) {
/* 186 */       PointerInfo info = MouseInfo.getPointerInfo();
/* 187 */       if (info != null) {
/* 188 */         Point p = info.getLocation();
/*     */ 
/* 190 */         float ox = xOffset * 1.6F + this.xLeftover;
/* 191 */         float oy = yOffset * 1.6F + this.yLeftover;
/* 192 */         int ix = Math.round(ox);
/* 193 */         int iy = Math.round(oy);
/* 194 */         this.xLeftover = (ox - ix);
/* 195 */         this.yLeftover = (oy - iy);
/*     */ 
/* 197 */         p.x += ix;
/* 198 */         p.y += iy;
/* 199 */         int l = this.gBounds.length;
/* 200 */         for (int i = 0; i < l; i++) {
/* 201 */           if (this.gBounds[i].contains(p)) {
/* 202 */             this.robot.mouseMove(p.x, p.y);
/* 203 */             break;
/*     */           }
/*     */         }
/*     */         try
/*     */         {
/* 208 */           this.robot.mouseMove(p.x, p.y);
/*     */         } catch (Exception localException) {
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void buttonEvent(int type, int button) {
/* 216 */     if (button == 0)
/* 217 */       button = 16;
/* 218 */     else if (button == 2) {
/* 219 */       button = 4;
/*     */     }
/* 221 */     switch (type)
/*     */     {
/*     */     case 0:
/* 224 */       this.robot.mousePress(button);
/* 225 */       this.robot.waitForIdle();
/* 226 */       break;
/*     */     case 1:
/* 229 */       this.robot.mouseRelease(button);
/* 230 */       this.robot.waitForIdle();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void scrollEvent(int dir)
/*     */   {
/* 236 */     this.robot.mouseWheel(-dir * this.scrollMod);
/*     */   }
/*     */ 
/*     */   private void keyboardEvent(int type, int keycode)
/*     */   {
/* 241 */     switch (type)
/*     */     {
/*     */     case 0:
/* 244 */       if (this.translator.isShift(keycode)) {
/* 245 */         this.shifted = true;
/* 246 */         keyPress(16);
/*     */       } else {
/* 248 */         keyPress(keycode);
/*     */       }
/* 250 */       break;
/*     */     case 1:
/* 253 */       if (this.translator.isShift(keycode)) {
/* 254 */         this.shifted = false;
/* 255 */         keyRelease(16);
/*     */       } else {
/* 257 */         keyRelease(keycode);
/*     */       }
/*     */       break;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void keyboardEvent(int type, int keycode, String value)
/*     */   {
	
	           KeyCodeData data=null;
/* 267 */     switch (type)
/*     */     {
/*     */     case 0:
/* 270 */       System.out.println("Key down, code:" + String.valueOf(keycode));
/*     */ 
/* 272 */       if (this.translator.isLeftClick(keycode)) {
/* 273 */         buttonEvent(0, 0);
/* 274 */         return;
/*     */       }
/*     */ 
/* 277 */       data = (KeyCodeData)this.translator.codes.get(new Integer(keycode));
/*     */ 
/* 279 */       if (this.translator.isModifier(keycode)) {
/* 280 */         this.modified = true;
/*     */       }
/* 282 */       if (this.translator.isShift(keycode)) {
/* 283 */         this.shifted = true;
/* 284 */         keyPress(16);
/*     */       }
/* 286 */       if (this.translator.isCtrl(keycode)) {
/* 287 */         keyPress(17);
/*     */       }
/* 289 */       if (data != null)
/*     */       {
/* 291 */         if ((!this.shifted) && (data.shifted)) {
/* 292 */           keyPress(16);
/*     */         }
/* 294 */         if (this.modified) {
/* 295 */           if ((data.modshifted) && (!this.shifted)) {
/* 296 */             keyPress(16);
/*     */           }
/*     */ 
/* 299 */           if ((!data.modshifted) && (this.shifted)) {
/* 300 */             keyRelease(16);
/*     */           }
/*     */ 
/* 303 */           if (data.modifiedcode != -1) {
/* 304 */             keyPress(data.modifiedcode);
/*     */           }
/* 306 */           if ((data.modshifted) && (!this.shifted)) {
/* 307 */             keyRelease(16);
/*     */           }
/* 309 */           if ((!data.modshifted) && (this.shifted))
/* 310 */             keyPress(16);
/*     */         }
/*     */         else {
/*     */           try {
/* 314 */             if ((this.shifted) && (data.shiftedcode != -1))
/* 315 */               keyPress(data.shiftedcode);
/*     */             else
/* 317 */               keyPress(data.localcode);
/*     */           }
/*     */           catch (IllegalArgumentException e) {
/* 320 */             System.out.println("Invalid key code: " + data.localcode);
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 1:
/* 327 */       System.out.println("Key up, code:" + String.valueOf(keycode));
/*     */ 
/* 329 */       if (this.translator.isLeftClick(keycode)) {
/* 330 */         buttonEvent(1, 0);
/* 331 */         return;
/*     */       }
/*     */ 
/* 334 */        data = (KeyCodeData)this.translator.codes.get(new Integer(keycode));
/*     */ 
/* 336 */       if (this.translator.isModifier(keycode)) {
/* 337 */         this.modified = false;
/*     */       }
/* 339 */       if (this.translator.isShift(keycode)) {
/* 340 */         this.shifted = false;
/* 341 */         keyRelease(16);
/*     */       }
/* 343 */       if (this.translator.isCtrl(keycode)) {
/* 344 */         keyRelease(17);
/*     */       }
/* 346 */       if (data != null)
/*     */       {
/* 348 */         if ((!this.shifted) && (data.shifted)) {
/* 349 */           keyRelease(16);
/*     */         }
/* 351 */         if (this.modified) {
/* 352 */           if ((data.modshifted) && (!this.shifted)) {
/* 353 */             keyPress(16);
/*     */           }
/* 355 */           if ((!data.modshifted) && (this.shifted)) {
/* 356 */             keyRelease(16);
/*     */           }
/*     */ 
/* 359 */           if (data.modifiedcode != -1) {
/* 360 */             keyRelease(data.modifiedcode);
/*     */           }
/* 362 */           if ((data.modshifted) && (!this.shifted)) {
/* 363 */             keyRelease(16);
/*     */           }
/* 365 */           if ((!data.modshifted) && (this.shifted)) {
/* 366 */             keyPress(16);
/*     */           }
/*     */         }
/* 369 */         else if ((this.shifted) && (data.shiftedcode != -1)) {
/* 370 */           keyRelease(data.shiftedcode);
/*     */         } else {
/* 372 */           keyRelease(data.localcode);
/*     */         }
/*     */       }
/*     */       break;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void keyPress(int localcode)
/*     */   {
/*     */     try
/*     */     {
/* 387 */       this.robot.keyPress(localcode);
/*     */     } catch (IllegalArgumentException e) {
/* 389 */       System.out.println("Invalid keyPress code: " + localcode);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void keyRelease(int localcode)
/*     */   {
/*     */     try
/*     */     {
/* 400 */       this.robot.keyRelease(localcode);
/*     */     } catch (IllegalArgumentException e) {
/* 402 */       System.out.println("Invalid keyRelease code: " + localcode);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void orientEvent(float z, float x, float y, float rawz, float rawx, float rawy) {
/* 407 */     StringBuilder builder = new StringBuilder();
/* 408 */     addValue(builder, "z", z);
/* 409 */     addValue(builder, "x", x);
/* 410 */     addValue(builder, "y", y);
/* 411 */     addValue(builder, "rawz", rawz);
/* 412 */     addValue(builder, "rawx", rawx);
/* 413 */     addValue(builder, "rawy", rawy);
/*     */ 
/* 415 */     double len = Math.sqrt(x * x + y * y + z * z);
/* 416 */     addValue(builder, "len", (float)len);
/*     */ 
/* 418 */     this.lbDebug.setText(builder.toString());
/*     */   }
/*     */ 
/*     */   private void initUI()
/*     */   {
/*     */   }
/*     */ 
/*     */   private void addValue(StringBuilder builder, String name, float value)
/*     */   {
/* 428 */     builder.append(name);
/* 429 */     builder.append(": ");
/* 430 */     builder.append(value);
/* 431 */     builder.append("\n");
/*     */   }
/*     */ 
/*     */   public void update(float elapsed)
/*     */   {
/*     */   }
/*     */ }

/* Location:           G:\RemoteDroidServer.jar
 * Qualified Name:     OSCWorld
 * JD-Core Version:    0.6.2
 */