package com.filetrans.net;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 基础的数据报文
 * 
 * @author Administrator
 * 
 */
public class BaseDataPack {
	
	

	public final static int PACK_ID_None = 0;
	/**
	 * 待发送文件信息报文ID
	 */
	public final static int PACK_ID_FileInfo = 1;
	/**
	 * 待发送文件信息响应报文ID
	 */
	public final static int PACK_ID_ResponeFileInfo = 11;
	/**
	 * 发送文件报文ID
	 */
	public final static int PACK_ID_SendFile = 2;
	/**
	 * 发送文件响应报文ID
	 */
	public final static int PACK_ID_ResponeSendFile = 12;
	
	
	/**
	 * 存储文件的根目录
	 */
	public static String STORAGE_DIR = "E:\\test\\storage\\";

	/**
	 * 报文ID
	 */
	private int packId;

	/**
	 * 扩展字段
	 */
	private String extStr = "";

	/**
	 * 包长度
	 */
	private int bodyLength;

	/**
	 * body数据，为json时候的数据
	 */
	private String bodyJson;

	/**
	 * 待发送文件的路径
	 */
	private String sendFileDir = "";

	public BaseDataPack() {
		
	}

	public BaseDataPack(int packId, String extStr, String bodyJson) {
		super();
		this.packId = packId;
		this.extStr = extStr;
		this.bodyJson = bodyJson;
	}

	public BaseDataPack(int packId, String extStr, int bodyLength, String storageOrSendFileDir) {
		super();
		this.packId = packId;
		this.extStr = extStr;
		this.bodyLength = bodyLength;
		this.sendFileDir = storageOrSendFileDir;
	}

	/**
	 * 打包-包头数据
	 * 
	 * @return
	 * @throws IOException
	 */
	public void writeHead(OutputStream outputstream, IFileTransferProgress progress) throws IOException {
		ByteArrayOutputStream byteOs = new ByteArrayOutputStream(1024);

		byteOs.write(packId);
		IOUtil.writeString(byteOs, extStr);
		if (PACK_ID_SendFile == packId) {
			bodyLength = (int) new File(sendFileDir + extStr).length();
		} else {
			if (!IOUtil.isStringEmpty(bodyJson)) {
				byte[] data = bodyJson.getBytes("utf-8");
				bodyLength = data.length;
			}
		}

		IOUtil.writeInt(byteOs, bodyLength);
		outputstream.write(byteOs.toByteArray());
		
		byte[] buf = byteOs.toByteArray();
		for (int i = 0; i < buf.length; i++) {
			byte b = buf[i];
			System.out.print(String.format("0x%02x,", b));
		}
		System.out.println("");
		
		byteOs.close();
	}

	/**
	 * 解包-包头数据
	 * 
	 * @throws IOException
	 */
	public void readHead(InputStream is) throws IOException {
		packId = is.read();
		extStr = IOUtil.readString(is);
		bodyLength = IOUtil.readInt(is);
	}

	public void recvFileData(InputStream is, IFileTransferProgress progress) {
		FileOutputStream fileOs = null;
		try {
			fileOs = new FileOutputStream(STORAGE_DIR + extStr);

			int readedLen = 0;
			progress.reportProgress(readedLen, bodyLength, this);
			while (readedLen < bodyLength) {
				byte[] buf = new byte[1024];
				// 从网络上读取文件数据
				int len = is.read(buf);
				if (len == -1) {
					break;
				}
				// 写入文件
				fileOs.write(buf, 0, len);
				readedLen += len;
				progress.reportProgress(readedLen, bodyLength, this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileOs != null) {
				try {
					fileOs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public void readBody(InputStream is, IFileTransferProgress progress) throws IOException {
		if (PACK_ID_SendFile == packId) {
			recvFileData(is, progress);
		} else {
			if (bodyLength > 0) {
				byte[] data = new byte[bodyLength];
				is.read(data);
				bodyJson = new String(data, "utf-8");
			}
		}
	}

	private void sendFileData(OutputStream os, IFileTransferProgress progress) {
		FileInputStream fileIs = null;
		try {
			fileIs = new FileInputStream(sendFileDir + extStr);

			int sendedLen = 0;
			progress.reportProgress(sendedLen, bodyLength, this);
			while (sendedLen < bodyLength) {
				byte[] buf = new byte[1024];
				int len = fileIs.read(buf);
				if (len == -1) {
					break;
				}
				os.write(buf, 0, len);
				sendedLen += len;
				progress.reportProgress(sendedLen, bodyLength, this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileIs != null) {
				try {
					fileIs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void writeBody(OutputStream os, IFileTransferProgress progress) throws IOException {
		if (PACK_ID_SendFile == packId) {
			sendFileData(os, progress);
		} else {
			if (!IOUtil.isStringEmpty(bodyJson)) {
				byte[] data = bodyJson.getBytes("utf-8");
				os.write(data);
			}
		}
	}

	public int getPackId() {
		return packId;
	}

	public void setPackId(int packId) {
		this.packId = packId;
	}

	public String getExtStr() {
		return extStr;
	}

	public void setExtStr(String extStr) {
		this.extStr = extStr;
	}

	public int getBodyLength() {
		return bodyLength;
	}

	public void setBodyLength(int bodyLength) {
		this.bodyLength = bodyLength;
	}

	public String getBodyJson() {
		return bodyJson;
	}

	public void setBodyJson(String bodyJson) {
		this.bodyJson = bodyJson;
	}

	public String getSendFileDir() {
		return sendFileDir;
	}

	public void setSendFileDir(String sendFileDir) {
		this.sendFileDir = sendFileDir;
	}

	@Override
	public String toString() {
		return "BaseDataPack [packId=" + packId + ", extStr=" + extStr + ", bodyLength=" + bodyLength + ", bodyJson="
				+ bodyJson + ", sendFileDir=" + sendFileDir + "]";
	}

}
