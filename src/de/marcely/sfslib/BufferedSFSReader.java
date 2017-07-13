/**
 * 
 * 
 * @author Marcel S.
 * @version 1.0
 * @website http://marcely.de/
 * 
 */

package de.marcely.sfslib;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import lombok.Getter;


public class BufferedSFSReader implements Closeable {
	
	@Getter private final File file;
	private final BufferedInputStream stream;
	
	public BufferedSFSReader(File file) throws IOException {
		this.file = file;
		
		try{
			final BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file));
			final byte[] buffer = new byte[(int) file.length()];
			stream.read(buffer);
			stream.close();
			
			this.stream = new BufferedInputStream(new ByteArrayInputStream(ZLib.decode(buffer)));
		}catch(IOException e){
			throw e;
		}
	}
	
	/**
	 * 
	 * Reads until the array is filled
	 */
	public void read(byte[] array){
		try{
			this.stream.read(array);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * Reads up to length of bytes. It'll start in the array at offset
	 */
	public void read(byte[] array, int offset, int length){
		try{
			
			this.stream.read(array, offset, length);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private byte[] read(int length){
		final byte[] b = new byte[length];
		read(b);
		
		return b;
	}
	
	/**
	 * 
	 * @return Returns true if there are no bytes left
	 */
	public boolean empty(){
		try{
			return this.stream.available() == 0;
		}catch(IOException e){
			e.printStackTrace();
			return true;
		}
	}
	
	/**
	 * 
	 * @return Returns the amount of bytes which are left in the buffer (may not correct)
	 */
	public int left(){
		try{
			return this.stream.available();
		}catch(IOException e){
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 
	 * @return Reads a byte
	 */
	public byte readByte(){
		return read(1)[0];
	}
	
	/**
	 * 
	 * @return Reads a byte array
	 */
	public byte[] readByteArray(){
		final int length = readSignedInt();
			
		return read(length);
	}
	
	/**
	 * 
	 * @return Reads a signed integer (-2147483648 - 2147483648)
	 */
	public int readSignedInt(){
		return ByteBuffer.wrap(read(4)).getInt();
	}
	
	/**
	 * 
	 * @return Reads a unsigned integer (0 - 4294967295)
	 */
	public long readUnsignedInt(){
		return readSignedInt() & 0x00000000ffffffffL;
	}
	
	/**
	 * 
	 * @return Reads a signed short (-128 - 128)
	 */
	public short readSignedShort(){
		return ByteBuffer.wrap(read(2)).getShort();
	}
	
	/**
	 * 
	 * @return Reads a unsigned short (0 - 255)
	 */
	public int readUnsignedShort(){
		return readSignedShort() & 0xffff;
	}
	
	/**
	 * 
	 * @return Reads a signed float
	 */
	public float readSignedFloat(){
		return ByteBuffer.wrap(read(4)).getFloat();
	}
	
	/**
	 * 
	 * @return Reads a signed double
	 */
	public double readSignedDouble(){
		return ByteBuffer.wrap(read(8)).getDouble();
	}
	
	/**
	 * 
	 * @return Reads a signed long
	 */
	public long readSignedLong(){
		return ByteBuffer.wrap(read(8)).getLong();
	}
	
	/**
	 * 
	 * @return Reads a byte array which will get converted to a string
	 */
	public String readString(){
		final byte[] bytes = readByteArray();
		
		return bytes != null ? new String(bytes) : null;
	}
	
	/**
	 * 
	 * @return Reads a byte and returns it as a boolean
	 */
	public boolean readBoolean(){
		return readByte() == (byte) 1;
	}
	
	/**
	 * 
	 * Closes the stream
	 */
	@Override
	public void close(){
		try{
			this.stream.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}