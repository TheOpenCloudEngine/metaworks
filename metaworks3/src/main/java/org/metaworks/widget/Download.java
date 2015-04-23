package org.metaworks.widget;

import org.directwebremoting.io.FileTransfer;

public class Download {
	
	FileTransfer fileTransfer;
		public FileTransfer getFileTransfer() {
			return fileTransfer;
		}
		public void setFileTransfer(FileTransfer fileTransfer) {
			this.fileTransfer = fileTransfer;
		}


	public Download(FileTransfer fileTransfer){
		setFileTransfer(fileTransfer);
	}
}