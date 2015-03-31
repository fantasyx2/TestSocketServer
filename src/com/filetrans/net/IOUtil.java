package com.filetrans.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtil {

	public static boolean isStringEmpty(String str) {
		if (str == null || str.length() == 0)
			return true;
		return false;
	}

	public static void writeShort(OutputStream os, int value) throws IOException {
		byte[] b = new byte[2];
		b[0] = (byte) (value & 0xff);
		b[1] = (byte) ((value >> 8) & 0xff);
		System.out.println(String.format("write short:0x%04x" , value));
		System.out.println(String.format("write short:0x%02x,0x%02x",b[0],b[1]));
		os.write(b);
	}

	public static void writeInt(OutputStream os, int value) throws IOException {
		byte[] b = new byte[4];
		
		b[0] = (byte) (value & 0xff);
		b[1] = (byte) ((value >> 8) & 0xff);
		b[2] = (byte) ((value >> 16) & 0xff);
		b[3] = (byte) ((value >> 24) & 0xff);
		
		System.out.println(String.format("write int:0x%08x" , value));
		System.out.println(String.format("write int:0x%02x,0x%02x,0x%02x,0x%02x",b[0],b[1],b[2],b[3]));
		os.write(b);
	}

	public static void writeString(OutputStream os, String value) throws IOException {
		System.out.println("write string:"+value);
		if (value == null || value.length() == 0) {
			writeShort(os, 0);
			return;
		}
		byte[] data = value.getBytes("utf-8");
		writeShort(os, data.length);
		os.write(data);
		
	}

	public static int readShort(InputStream is) throws IOException {
		byte[] b = new byte[2];
		is.read(b);
		int value = (int) (((int)(b[0]&0xff))|(((int)(b[1]&0xff))<<8));
		
		System.out.println(String.format("read short:0x%02x,0x%02x",b[0],b[1]));
		System.out.println(String.format("read short:0x%04x" , value));
		
		return value;
		
	}

	public static int readInt(InputStream is) throws IOException {
		byte[] b = new byte[4];
		is.read(b);
		int value = (int) (((int)(b[0]&0xff))|(((int)(b[1]&0xff))<<8)|(((int)(b[2]&0xff))<<16)|(((int)(b[3]&0xff))<<24));
		
		System.out.println(String.format("read int:0x%02x,0x%02x,0x%02x,0x%02x",b[0],b[1],b[2],b[3]));
		System.out.println(String.format("read int:0x%08x,%d" , value,value));
		
		return value;
	}

	public static String readString(InputStream is) throws IOException {
		int len = readShort(is);
		if (len == 0) {
			return "";
		}
		byte[] buf = new byte[len];
		is.read(buf);
		String value = new String(buf, "utf-8");
		
		System.out.println("read string:"+value);

		return value;
	}


}
