package com.filetrans.net;

import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.ArrayList;

public class SendThread extends Thread {

	private Connetor connetor;
	private ArrayList<BaseDataPack> datapackList;
	private OutputStream os;

	public SendThread(Connetor connetor,OutputStream os) {
		this.connetor = connetor;
		this.os = os;
		this.datapackList = new ArrayList<BaseDataPack>(10);
	}

	/**
	 * 发送数据报文
	 * 
	 * @param aDataPack
	 */
	public void sendDataPack(BaseDataPack aDataPack) {
		datapackList.add(aDataPack);
	}

	@Override
	public void run() {
		while (connetor.isRunning()) {
			if (datapackList.size() == 0) {
				try {
					sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}

			try {
				BaseDataPack datapck = datapackList.remove(0);
				datapck.writeHead(os, connetor.progress);
				datapck.writeBody(os, connetor.progress);
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
