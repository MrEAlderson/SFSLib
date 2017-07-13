/**
 * 
 * 
 * @author Marcel S.
 * @version 1.0
 * @website http://marcely.de/
 * 
 */

package de.marcely.sfslib;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import lombok.Getter;

public class BufferedSFSWriter implements Closeable {
	
	@Getter private final File file;
	private final ByteArrayOutputStream stream;
	
	public BufferedSFSWriter(File file) throws FileNotFoundException {
		this.file = file;
		
		this.stream = new ByteArrayOutputStream();
	}
	
	public void write(byte[] array){
		try{
			this.stream.write(array);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void write(byte[] array, int offset, int length){
		this.stream.write(array, offset, length);
	}
	
	public void writeByte(byte b){
		final byte[] array = new byte[1];
		array[0] = b;
		
		write(array);
	}
	
	public void writeByteArray(byte[] b){
		writeSignedInt(b.length);
		write(b);
	}
	
	public void writeSignedInt(int i){
		write(ByteBuffer.allocate(4).putInt(i).array());
	}
	
	public void writeUnsignedInt(long i){
		writeSignedInt((int) ((long) i & 0x7FFFFFFF));
	}
	
	public void writeSignedShort(short s){
		write(ByteBuffer.allocate(2).putShort(s).array());
	}
	
	public void writeUnsignedShort(int s){
		writeSignedInt(s & 0x3FFF);
	}
	
	public void writeSignedFloat(float f){
		write(ByteBuffer.allocate(4).putFloat(f).array());
	}
	
	public void writeSignedDouble(double d){
		write(ByteBuffer.allocate(8).putDouble(d).array());
	}
	
	public void writeSignedLong(long l){
		write(ByteBuffer.allocate(8).putLong(l).array());
	}
	
	public void writeString(String s){
		writeByteArray(s.getBytes());
	}
	
	public void writeBoolean(boolean b){
		writeByte(b == true ? (byte) 1 : (byte) 0);
	}

	@Override
	public void close(){
		try{
			byte[] buffer = this.stream.toByteArray();
			
			this.stream.flush();
			this.stream.close();
			
			buffer = ZLib.encode(buffer);
			
			final BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
			stream.write(buffer);
			stream.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
