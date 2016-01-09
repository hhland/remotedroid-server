/*     */ package com.illposed.osc.utility;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class OSCJavaToByteArrayConverter
/*     */ {
/*  33 */   protected ByteArrayOutputStream stream = new ByteArrayOutputStream();
/*  34 */   private byte[] intBytes = new byte[4];
/*  35 */   private byte[] longintBytes = new byte[8];
/*     */ 
/*  37 */   private char[] stringChars = new char[2048];
/*  38 */   private byte[] stringBytes = new byte[2048];
/*     */ 
/*     */   private byte[] alignBigEndToFourByteBoundry(byte[] bytes)
/*     */   {
/*  50 */     int mod = bytes.length % 4;
/*     */ 
/*  53 */     if (mod == 0)
/*  54 */       return bytes;
/*  55 */     int pad = 4 - mod;
/*  56 */     byte[] newBytes = new byte[pad + bytes.length];
/*  57 */     for (int i = 0; i < pad; i++)
/*  58 */       newBytes[i] = 0;
/*  59 */     for (int i = 0; i < bytes.length; i++)
/*  60 */       newBytes[(pad + i)] = bytes[i];
/*  61 */     return newBytes;
/*     */   }
/*     */ 
/*     */   public void appendNullCharToAlignStream()
/*     */   {
/*  68 */     int mod = this.stream.size() % 4;
/*  69 */     int pad = 4 - mod;
/*  70 */     for (int i = 0; i < pad; i++)
/*  71 */       this.stream.write(0);
/*     */   }
/*     */ 
/*     */   public byte[] toByteArray()
/*     */   {
/*  79 */     return this.stream.toByteArray();
/*     */   }
/*     */ 
/*     */   public void write(byte[] bytes)
/*     */   {
/*  87 */     writeUnderHandler(bytes);
/*     */   }
/*     */ 
/*     */   public void write(int i)
/*     */   {
/*  95 */     writeInteger32ToByteArray(i);
/*     */   }
/*     */ 
/*     */   public void write(Float f)
/*     */   {
/* 103 */     writeInteger32ToByteArray(Float.floatToIntBits(f.floatValue()));
/*     */   }
/*     */ 
/*     */   public void write(Integer i)
/*     */   {
/* 110 */     writeInteger32ToByteArray(i.intValue());
/*     */   }
/*     */ 
/*     */   public void write(BigInteger i)
/*     */   {
/* 117 */     writeInteger64ToByteArray(i.longValue());
/*     */   }
/*     */ 
/*     */   public void write(String aString)
/*     */   {
/* 125 */     int stringLength = aString.length();
/*     */ 
/* 129 */     aString.getChars(0, stringLength, this.stringChars, 0);
/*     */ 
/* 131 */     int mod = stringLength % 4;
/* 132 */     int pad = 4 - mod;
/* 133 */     for (int i = 0; i < pad; i++) {
/* 134 */       this.stringChars[(stringLength++)] = '\000';
/*     */     }
/* 136 */     for (int i = 0; i < stringLength; i++) {
/* 137 */       this.stringBytes[i] = ((byte)(this.stringChars[i] & 0xFF));
/*     */     }
/* 139 */     this.stream.write(this.stringBytes, 0, stringLength);
/*     */   }
/*     */ 
/*     */   public void write(char c)
/*     */   {
/* 147 */     this.stream.write(c);
/*     */   }
/*     */ 
/*     */   public void write(Object anObject)
/*     */   {
/* 156 */     if (anObject == null)
/* 157 */       return;
/* 158 */     if ((anObject instanceof Object[])) {
/* 159 */       Object[] theArray = (Object[])anObject;
/* 160 */       for (int i = 0; i < theArray.length; i++) {
/* 161 */         write(theArray[i]);
/*     */       }
/* 163 */       return;
/*     */     }
/* 165 */     if ((anObject instanceof Float)) {
/* 166 */       write((Float)anObject);
/* 167 */       return;
/*     */     }
/* 169 */     if ((anObject instanceof String)) {
/* 170 */       write((String)anObject);
/* 171 */       return;
/*     */     }
/* 173 */     if ((anObject instanceof Integer)) {
/* 174 */       write((Integer)anObject);
/* 175 */       return;
/*     */     }
/* 177 */     if ((anObject instanceof BigInteger)) {
/* 178 */       write((BigInteger)anObject);
/* 179 */       return;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeType(Class c)
/*     */   {
/* 194 */     if (Integer.class.equals(c)) {
/* 195 */       this.stream.write(105);
/* 196 */       return;
/*     */     }
/* 198 */     if (BigInteger.class.equals(c)) {
/* 199 */       this.stream.write(104);
/* 200 */       return;
/*     */     }
/* 202 */     if (Float.class.equals(c)) {
/* 203 */       this.stream.write(102);
/* 204 */       return;
/*     */     }
/* 206 */     if (Double.class.equals(c)) {
/* 207 */       this.stream.write(100);
/* 208 */       return;
/*     */     }
/* 210 */     if (String.class.equals(c)) {
/* 211 */       this.stream.write(115);
/* 212 */       return;
/*     */     }
/* 214 */     if (Character.class.equals(c)) {
/* 215 */       this.stream.write(99);
/* 216 */       return;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeTypesArray(Object[] array)
/*     */   {
/* 228 */     for (int i = 0; i < array.length; i++)
/* 229 */       if (array[i] != null)
/*     */       {
/* 232 */         if (Boolean.TRUE.equals(array[i])) {
/* 233 */           this.stream.write(84);
/*     */         }
/* 236 */         else if (Boolean.FALSE.equals(array[i])) {
/* 237 */           this.stream.write(70);
/*     */         }
/*     */         else
/*     */         {
/* 241 */           writeType(array[i].getClass());
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   public void writeTypes(Vector vector)
/*     */   {
/* 253 */     Enumeration enm = vector.elements();
/*     */ 
/* 255 */     while (enm.hasMoreElements()) {
/* 256 */       Object nextObject = enm.nextElement();
/* 257 */       if (nextObject != null)
/*     */       {
/* 261 */         if (nextObject.getClass().isArray()) {
/* 262 */           this.stream.write(91);
/*     */ 
/* 265 */           writeTypesArray((Object[])nextObject);
/*     */ 
/* 267 */           this.stream.write(93);
/*     */         }
/* 271 */         else if (Boolean.TRUE.equals(nextObject)) {
/* 272 */           this.stream.write(84);
/*     */         }
/* 275 */         else if (Boolean.FALSE.equals(nextObject)) {
/* 276 */           this.stream.write(70);
/*     */         }
/*     */         else
/*     */         {
/* 281 */           writeType(nextObject.getClass());
/*     */         }
/*     */       }
/*     */     }
/* 284 */     appendNullCharToAlignStream();
/*     */   }
/*     */ 
/*     */   private void writeUnderHandler(byte[] bytes)
/*     */   {
/*     */     try
/*     */     {
/* 294 */       this.stream.write(alignBigEndToFourByteBoundry(bytes));
/*     */     } catch (IOException e) {
/* 296 */       throw new RuntimeException("You're screwed: IOException writing to a ByteArrayOutputStream");
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeInteger32ToByteArray(int value)
/*     */   {
/* 308 */     this.intBytes[3] = ((byte)value); value >>>= 8;
/* 309 */     this.intBytes[2] = ((byte)value); value >>>= 8;
/* 310 */     this.intBytes[1] = ((byte)value); value >>>= 8;
/* 311 */     this.intBytes[0] = ((byte)value);
/*     */     try
/*     */     {
/* 314 */       this.stream.write(this.intBytes);
/*     */     } catch (IOException e) {
/* 316 */       throw new RuntimeException("You're screwed: IOException writing to a ByteArrayOutputStream");
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeInteger64ToByteArray(long value)
/*     */   {
/* 325 */     this.longintBytes[7] = ((byte)(int)value); value >>>= 8;
/* 326 */     this.longintBytes[6] = ((byte)(int)value); value >>>= 8;
/* 327 */     this.longintBytes[5] = ((byte)(int)value); value >>>= 8;
/* 328 */     this.longintBytes[4] = ((byte)(int)value); value >>>= 8;
/* 329 */     this.longintBytes[3] = ((byte)(int)value); value >>>= 8;
/* 330 */     this.longintBytes[2] = ((byte)(int)value); value >>>= 8;
/* 331 */     this.longintBytes[1] = ((byte)(int)value); value >>>= 8;
/* 332 */     this.longintBytes[0] = ((byte)(int)value);
/*     */     try
/*     */     {
/* 335 */       this.stream.write(this.longintBytes);
/*     */     } catch (IOException e) {
/* 337 */       throw new RuntimeException("You're screwed: IOException writing to a ByteArrayOutputStream");
/*     */     }
/*     */   }
/*     */ }

/* Location:           G:\RemoteDroidServer.jar
 * Qualified Name:     com.illposed.osc.utility.OSCJavaToByteArrayConverter
 * JD-Core Version:    0.6.2
 */