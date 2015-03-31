package com.filetrans.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class FTServer implements IConnetorChangeListener, IFileTransferProgress {

	private boolean isRunning = false;
	ArrayList<Connetor> connetorList;

	public FTServer() {
		connetorList = new ArrayList<Connetor>();
	}

	public void Start(int port) {
		try {
			isRunning = true;
			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("started server");
			while (isRunning) {
				Socket socket = serverSocket.accept();
				System.out.println("accpent a client:" + socket.toString());
				Connetor connetor = new Connetor(socket, this, this);
				connetorList.add(connetor);
				connetor.start();

				Thread.sleep(100);
			}
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			Stop();
		}
	}

	public void Stop() {
		isRunning = false;
		for (Connetor connetor : connetorList) {
			connetor.stop();
		}
		connetorList.clear();
		System.out.println("server: stop.");
	}

	@Override
	public void OnChange(int eventId, Object obj) {

	}

	public static void main(String[] args) {
		int port = 8001;
		if(args.length>0){
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		if(args.length>1){
			BaseDataPack.STORAGE_DIR = args[1];
		}
		
		System.out.println("will start server on:"+port);
		FTServer server = new FTServer();
		server.Start(port);
	}

	@Override
	public void reportProgress(int now, int total, Object sender) {
		System.out.println("report,progress:"+now+"/"+total);
	}

}
