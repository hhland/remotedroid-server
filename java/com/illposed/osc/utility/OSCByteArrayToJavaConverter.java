/*     */ package com.illposed.osc.utility;
/*     */ 
/*     */ import com.illposed.osc.OSCBundle;
/*     */ import com.illposed.osc.OSCMessage;
/*     */ import com.illposed.osc.OSCPacket;
/*     */ import java.math.BigInteger;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class OSCByteArrayToJavaConverter
/*     */ {
/*     */   byte[] bytes;
/*     */   int bytesLength;
/*     */   int streamPosition;
/*     */ 
/*     */   public OSCPacket convert(byte[] byteArray, int bytesLength)
/*     */   {
/*  38 */     this.bytes = byteArray;
/*  39 */     this.bytesLength = bytesLength;
/*  40 */     this.streamPosition = 0;
/*  41 */     if (isBundle()) {
/*  42 */       return convertBundle();
/*     */     }
/*  44 */     return convertMessage();
/*     */   }
/*     */ 
/*     */   private boolean isBundle()
/*     */   {
/*  53 */     String bytesAsString = new String(this.bytes, 0, 7);
/*  54 */     return bytesAsString.startsWith("#bundle");
/*     */   }
/*     */ 
/*     */   private OSCBundle convertBundle()
/*     */   {
/*  63 */     this.streamPosition = 8;
/*  64 */     Date timestamp = readTimeTag();
/*  65 */     OSCBundle bundle = new OSCBundle(timestamp);
/*  66 */     OSCByteArrayToJavaConverter myConverter = new OSCByteArrayToJavaConverter();
/*  67 */     while (this.streamPosition < this.bytesLength)
/*     */     {
/*  69 */       int packetLength = ((Integer)readInteger()).intValue();
/*  70 */       byte[] packetBytes = new byte[packetLength];
/*  71 */       for (int i = 0; i < packetLength; i++)
/*  72 */         packetBytes[i] = this.bytes[(this.streamPosition++)];
/*  73 */       OSCPacket packet = myConverter.convert(packetBytes, packetLength);
/*  74 */       bundle.addPacket(packet);
/*     */     }
/*  76 */     return bundle;
/*     */   }
/*     */ 
/*     */   private OSCMessage convertMessage()
/*     */   {
/*  84 */     OSCMessage message = new OSCMessage();
/*  85 */     message.setAddress(readString());
/*  86 */     char[] types = readTypes();
/*  87 */     if (types == null)
/*     */     {
/*  89 */       return message;
/*     */     }
/*  91 */     moveToFourByteBoundry();
/*  92 */     for (int i = 0; i < types.length; i++)
/*  93 */       if ('[' == types[i])
/*     */       {
/*  95 */         message.addArgument(readArray(types, ++i));
/*     */ 
/*  97 */         while (']' != types[i])
/*  98 */           i++;
/*     */       } else {
/* 100 */         message.addArgument(readArgument(types[i]));
/*     */       }
/* 102 */     return message;
/*     */   }
/*     */ 
/*     */   private String readString()
/*     */   {
/* 110 */     int strLen = lengthOfCurrentString();
/* 111 */     char[] stringChars = new char[strLen];
/* 112 */     for (int i = 0; i < strLen; i++)
/* 113 */       stringChars[i] = ((char)this.bytes[(this.streamPosition++)]);
/* 114 */     moveToFourByteBoundry();
/* 115 */     return new String(stringChars);
/*     */   }
/*     */ 
/*     */   private char[] readTypes()
/*     */   {
/* 124 */     if (this.bytes[this.streamPosition] != 44)
/* 125 */       return null;
/* 126 */     this.streamPosition += 1;
/*     */ 
/* 128 */     int typesLen = lengthOfCurrentString();
/* 129 */     if (typesLen == 0) {
/* 130 */       return null;
/*     */     }
/*     */ 
/* 134 */     char[] typesChars = new char[typesLen];
/* 135 */     for (int i = 0; i < typesLen; i++) {
/* 136 */       typesChars[i] = ((char)this.bytes[(this.streamPosition++)]);
/*     */     }
/* 138 */     return typesChars;
/*     */   }
/*     */ 
/*     */   private Object readArgument(char c)
/*     */   {
/* 147 */     switch (c) {
/*     */     case 'i':
/* 149 */       return readInteger();
/*     */     case 'h':
/* 151 */       return readBigInteger();
/*     */     case 'f':
/* 153 */       return readFloat();
/*     */     case 'd':
/* 155 */       return readDouble();
/*     */     case 's':
/* 157 */       return readString();
/*     */     case 'c':
/* 159 */       return readChar();
/*     */     case 'T':
/* 161 */       return Boolean.TRUE;
/*     */     case 'F':
/* 163 */       return Boolean.FALSE;
/*     */     }
/*     */ 
/* 166 */     return null;
/*     */   }
/*     */ 
/*     */   private Object readChar()
/*     */   {
/* 174 */     return new Character((char)this.bytes[(this.streamPosition++)]);
/*     */   }
/*     */ 
/*     */   private Object readDouble()
/*     */   {
/* 182 */     return readFloat();
/*     */   }
/*     */ 
/*     */   private Object readFloat()
/*     */   {
/* 190 */     byte[] floatBytes = new byte[4];
/* 191 */     floatBytes[0] = this.bytes[(this.streamPosition++)];
/* 192 */     floatBytes[1] = this.bytes[(this.streamPosition++)];
/* 193 */     floatBytes[2] = this.bytes[(this.streamPosition++)];
/* 194 */     floatBytes[3] = this.bytes[(this.streamPosition++)];
/*     */ 
/* 200 */     BigInteger floatBits = new BigInteger(floatBytes);
/* 201 */     return new Float(Float.intBitsToFloat(floatBits.intValue()));
/*     */   }
/*     */ 
/*     */   private Object readBigInteger()
/*     */   {
/* 209 */     byte[] longintBytes = new byte[8];
/* 210 */     longintBytes[0] = this.bytes[(this.streamPosition++)];
/* 211 */     longintBytes[1] = this.bytes[(this.streamPosition++)];
/* 212 */     longintBytes[2] = this.bytes[(this.streamPosition++)];
/* 213 */     longintBytes[3] = this.bytes[(this.streamPosition++)];
/* 214 */     longintBytes[4] = this.bytes[(this.streamPosition++)];
/* 215 */     longintBytes[5] = this.bytes[(this.streamPosition++)];
/* 216 */     longintBytes[6] = this.bytes[(this.streamPosition++)];
/* 217 */     longintBytes[7] = this.bytes[(this.streamPosition++)];
/* 218 */     return new BigInteger(longintBytes);
/*     */   }
/*     */ 
/*     */   private Object readInteger()
/*     */   {
/* 226 */     byte[] intBytes = new byte[4];
/* 227 */     intBytes[0] = this.bytes[(this.streamPosition++)];
/* 228 */     intBytes[1] = this.bytes[(this.streamPosition++)];
/* 229 */     intBytes[2] = this.bytes[(this.streamPosition++)];
/* 230 */     intBytes[3] = this.bytes[(this.streamPosition++)];
/* 231 */     BigInteger intBits = new BigInteger(intBytes);
/* 232 */     return new Integer(intBits.intValue());
/*     */   }
/*     */ 
/*     */   private Date readTimeTag()
/*     */   {
/* 242 */     byte[] secondBytes = new byte[8];
/* 243 */     byte[] fractionBytes = new byte[8];
/* 244 */     for (int i = 0; i < 4; i++)
/*     */     {
/* 246 */       secondBytes[i] = 0; fractionBytes[i] = 0;
/*     */     }
/*     */ 
/* 250 */     boolean isImmediate = true;
/* 251 */     for (int i = 4; i < 8; i++) {
/* 252 */       secondBytes[i] = this.bytes[(this.streamPosition++)];
/* 253 */       if (secondBytes[i] > 0)
/* 254 */         isImmediate = false;
/*     */     }
/* 256 */     for (int i = 4; i < 8; i++) {
/* 257 */       fractionBytes[i] = this.bytes[(this.streamPosition++)];
/* 258 */       if (i < 7) {
/* 259 */         if (fractionBytes[i] > 0)
/* 260 */           isImmediate = false;
/*     */       }
/* 262 */       else if (fractionBytes[i] > 1) {
/* 263 */         isImmediate = false;
/*     */       }
/*     */     }
/*     */ 
/* 267 */     if (isImmediate) return OSCBundle.TIMESTAMP_IMMEDIATE;
/*     */ 
/* 269 */     BigInteger secsSince1900 = new BigInteger(secondBytes);
/* 270 */     long secsSince1970 = secsSince1900.longValue() - OSCBundle.SECONDS_FROM_1900_to_1970.longValue();
/* 271 */     if (secsSince1970 < 0L) secsSince1970 = 0L;
/* 272 */     long fraction = new BigInteger(fractionBytes).longValue();
/*     */ 
/* 274 */     fraction = fraction * 1000L / 4294967296L;
/*     */ 
/* 276 */     fraction = fraction > 0L ? fraction + 1L : 0L;
/* 277 */     long millisecs = secsSince1970 * 1000L + fraction;
/* 278 */     return new Date(millisecs);
/*     */   }
/*     */ 
/*     */   private Object[] readArray(char[] types, int i)
/*     */   {
/* 288 */     int arrayLen = 0;
/* 289 */     while (types[(i + arrayLen)] != ']')
/* 290 */       arrayLen++;
/* 291 */     Object[] array = new Object[arrayLen];
/* 292 */     for (int j = 0; j < arrayLen; j++) {
/* 293 */       array[j] = readArgument(types[(i + j)]);
/*     */     }
/* 295 */     return array;
/*     */   }
/*     */ 
/*     */   private int lengthOfCurrentString()
/*     */   {
/* 302 */     int i = 0;
/* 303 */     while (this.bytes[(this.streamPosition + i)] != 0)
/* 304 */       i++;
/* 305 */     return i;
/*     */   }
/*     */ 
/*     */   private void moveToFourByteBoundry()
/*     */   {
/* 313 */     int mod = this.streamPosition % 4;
/* 314 */     this.streamPosition += 4 - mod;
/*     */   }
/*     */ }

/* Location:           G:\RemoteDroidServer.jar
 * Qualified Name:     com.illposed.osc.utility.OSCByteArrayToJavaConverter
 * JD-Core Version:    0.6.2
 */