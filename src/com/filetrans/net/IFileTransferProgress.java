package com.filetrans.net;

public interface IFileTransferProgress {

	public void reportProgress(int now, int total, Object sender);
	
}
