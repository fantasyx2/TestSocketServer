package com.filetrans.net;

import java.io.File;
import java.net.Socket;

public class FTClient implements IConnetorChangeListener, IFileTransferProgress {

	private boolean isRunning = false;
	private Connetor connetor;

	public FTClient() {
	}

	public void Start(String host, int port) {
		try {
			isRunning = true;
			Socket socket = new Socket(host, port);
			System.out.println("client socket:" + socket.toString());

			connetor = new Connetor(socket, this, this);
			connetor.start();
		} catch (Exception e) {
			e.printStackTrace();
			Stop();
		}
	}

	public void Stop() {
		System.out.println("client:stop...");
		isRunning = false;
	}

	/**
	 * 发送文件
	 * @param dir
	 * @param fileName
	 * @return 如果文件不存在，返回失败，如果存在返回成功
	 */
	public boolean sendFile(String dir, String fileName) {
		File file = new File(dir + fileName);
		if (file.exists()) {
			int bodyLength = (int) file.length();
			BaseDataPack aDataPack = new BaseDataPack(2, fileName, bodyLength, dir);
			connetor.sendDataPack(aDataPack);
			return true;
		}
		System.out.println(file.getName() + " is not exist.");
		return false;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	@Override
	public void OnChange(int eventId, Object obj) {
		switch (eventId) {
		case IConnetorChangeListener.EVENT_ID_OnDisconnet:
			Stop();
			break;
		case IConnetorChangeListener.EVENT_ID_OnConneted:
			System.out.println("client: connected....");
			break;
			
		default:
			break;
		}
	}

	@Override
	public void reportProgress(int now, int total, Object sender) {
		System.out.println("client:report,progress:"+now+"/"+total);
	}

	public static void main(String[] args) {
		
		String host = "localhost";
		int port = 8001;
		String dir = "E:\\test\\send\\";
		String filename = "tidy.txt";
		BaseDataPack.STORAGE_DIR = "E:\\test\\storage\\";
		 
		if (args.length >= 5) {
			System.out.println("will connect :" + args[0] + ", port:" + args[1] + ", dir:" + args[2] + ", filename:" + args[3]);
			host = args[0];
			port = Integer.parseInt(args[1]);
			dir = args[2];
			filename = args[3];
			BaseDataPack.STORAGE_DIR = args[4];
		}
		
		
		FTClient client = new FTClient();
		client.Start(host, port);

		client.sendFile(dir, filename);

		while (client.isRunning()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
				// break;
			}
		}
		// client.setRunning(false);
		client.Stop();

	}

}
