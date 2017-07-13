/**
 * 
 * 
 * @author Marcel S.
 * @version 1.0
 * @website http://marcely.de/
 * 
 */

package de.marcely.sfslib;

import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import javax.annotation.Nullable;

public class ZLib {
	
	public static byte[] encode(byte[] bytes){
		final Deflater compresser = new Deflater();
		final byte[] output = new byte[bytes.length];
		
		compresser.setInput(bytes);
		compresser.finish();
		final int length = compresser.deflate(output);
		compresser.end();
		
		return Arrays.copyOfRange(output, 0, length);
	}
	
	@Nullable
	public static byte[] decode(byte[] bytes){
		try {
			final Inflater decompresser = new Inflater();
			final byte[] result = new byte[bytes.length + 2024];
			
			decompresser.setInput(bytes);
			int length = decompresser.inflate(result);
			decompresser.end();
			
			return Arrays.copyOfRange(result, 0, length);
		}catch(DataFormatException e){
			e.printStackTrace();
		}
		
		return null;
	}
}
