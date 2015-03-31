package com.filetrans.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Connetor {

	protected Socket socket;
	protected IConnetorChangeListener listener;
	protected IFileTransferProgress progress;

	private SendThread sender;
	private RecvThread receiver;

	private boolean isRunning;
	
	private OutputStream os;
	private InputStream is;
	
	public Connetor(Socket socket, IConnetorChangeListener listener, IFileTransferProgress progress) {
		this.socket = socket;
		this.listener = listener;
		this.progress = progress;
	}

	/**
	 * 发送数据报文
	 * 
	 * @param aDataPack
	 */
	public void sendDataPack(BaseDataPack aDataPack) {
		sender.sendDataPack(aDataPack);
	}

	protected void notifyRecvDataPack(BaseDataPack aDataPack) {
		switch (aDataPack.getPackId()) {
		case BaseDataPack.PACK_ID_FileInfo: {
			System.out.println("收到发送文件请求，文件信息：" + aDataPack.toString());
			// 可以传输文件
			String body = "{\"result\":0,\"reason\": \"ok\"}";
			BaseDataPack responsePack = new BaseDataPack(BaseDataPack.PACK_ID_ResponeFileInfo, "", body);
			sendDataPack(responsePack);
		}
			break;
		case BaseDataPack.PACK_ID_SendFile: {
			System.out.println("收到发送文件的内容：" + aDataPack.toString());
			// 传输文件成功
			String body = "{\"result\":0,\"reason\": \"ok\"}";
			BaseDataPack responsePack = new BaseDataPack(BaseDataPack.PACK_ID_ResponeSendFile, "", body);
			sendDataPack(responsePack);
		}
			break;
		case BaseDataPack.PACK_ID_ResponeFileInfo: {
			System.out.println("收到发送文件请求回应报文：" + aDataPack.toString());
			// 解读包体json内容，如果result = 0,发送文件

		}
			break;
		case BaseDataPack.PACK_ID_ResponeSendFile: {
			System.out.println("收到发送文件的内容回应报文：" + aDataPack.toString());
			// 解读包体json内容，如果result = 0,表示发送成功，给出用户提示；
		}
			break;
		default:
			break;
		}
	}

	public void start() {
		try {
			setRunning(true);
			os = socket.getOutputStream();
			is = socket.getInputStream();
			sender = new SendThread(this, os);
			receiver = new RecvThread(this, is);

			sender.start();
			receiver.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		
		setRunning(false);
		
		System.out.println("connetor stop.....");
		if(os != null){
			try {
				os.close();
				os = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(is != null){
			try {
				is.close();
				is = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(socket != null){
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		if (this.isRunning != isRunning) {
			this.isRunning = isRunning;

			if (listener == null) {
				return;
			}
			if (isRunning) {
				listener.OnChange(IConnetorChangeListener.EVENT_ID_OnConneted,this);
			} else {
				listener.OnChange(IConnetorChangeListener.EVENT_ID_OnDisconnet,this);
			}
		}
	}

}
