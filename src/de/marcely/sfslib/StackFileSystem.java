/**
 * 
 * 
 * @author Marcel S.
 * @version 1.0
 * @website http://marcely.de/
 * 
 */

package de.marcely.sfslib;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nullable;

import lombok.Getter;

public class StackFileSystem {
	
	@Getter private final File file;
	
	public StackFileSystem(File file){
		this.file = file;
	}
	
	/**
	 * 
	 * @return Returns true if the file exists
	 */
	public boolean exists(){
		return this.file.exists();
	}
	
	/**
	 * 
	 * Creates an empty file
	 * @return Returns false if the file already exists or it failed (out of space)
	 */
	public boolean create(){
		if(file.exists())
			return false;
		
		try{
			return file.createNewFile();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * 
	 * Opens a read stream
	 * @return Returns null if the file doesn't exists or if it failed
	 */
	@Nullable
	public BufferedSFSReader openReadStream(){
		try{
			return new BufferedSFSReader(file);
		}catch(IOException e){
			return null;
		}
	}
	
	/**
	 * 
	 * Opens a write stream with the flag 'APPEND'
	 * @return Returns null if the file doesn't exists
	 */
	@Nullable
	public BufferedSFSWriter openWriteStream(){
		return openWriteStream(WriteStreamFlag.APPEND);
	}
	
	/**
	 * 
	 * Opens a write stream
	 * @return Returns null if the file doesn't exists or file isn't a correct sfs file
	 */
	@Nullable
	public BufferedSFSWriter openWriteStream(WriteStreamFlag flag){
		try{
			
			byte[] bytesAppend = null;
			
			if(flag == WriteStreamFlag.APPEND){
				final BufferedSFSReader reader = new BufferedSFSReader(file);
				
				bytesAppend = new byte[(int) reader.left()];
				reader.read(bytesAppend);
				reader.close();
			}
			
			final BufferedSFSWriter writer = new BufferedSFSWriter(file);
			
			if(bytesAppend != null)
				writer.write(bytesAppend);
			
			return writer;
			
		}catch(IOException e){
			return null;
		}
	}
	
	
	
	public static enum WriteStreamFlag {
		CLEAR,
		APPEND;
	}
}
