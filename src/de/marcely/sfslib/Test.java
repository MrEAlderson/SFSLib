package de.marcely.sfslib;

import java.io.File;

public class Test {

	public static void main(String[] args){
		StackFileSystem sfs = new StackFileSystem(new File("test.sfs"));
		sfs.create();
		
		BufferedSFSReader reader = sfs.openReadStream();
		
		final long startTime = System.currentTimeMillis();
		int amount = 0;
		while(!reader.empty()){
			reader.readString();
			amount += 1;
		}
		System.out.println((float) ((System.currentTimeMillis() - startTime)/1000F) + "s for " + amount); 
		
		reader.close();
		
		final BufferedSFSWriter writer = sfs.openWriteStream();
		for(int i=0; i<50; i++)
		writer.writeString("Test string! 1234❥ ❤ ❣ ❦ ❧ ღ");
		writer.close();
	}
}
