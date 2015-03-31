package com.filetrans.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

public class RecvThread extends Thread {

	private Connetor connetor;
	private InputStream is;
	
	public RecvThread(Connetor connetor,InputStream is) {
		this.connetor = connetor;
		this.is = is;
	}

	@Override
	public void run() {
		while (connetor.isRunning()) {
			try {
				BaseDataPack datapack = new BaseDataPack();
				datapack.readHead(is);
				datapack.readBody(is, connetor.progress);
				connetor.notifyRecvDataPack(datapack);
			} catch (SocketException e) {
				e.printStackTrace();
				break;// 出现异常,退出循环
			} catch (IOException e) {
				e.printStackTrace();
				break;// 出现异常,退出循环
			}
		}
		connetor.stop();
		
	}

	

}
