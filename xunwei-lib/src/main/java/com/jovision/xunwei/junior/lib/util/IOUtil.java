package com.jovision.xunwei.junior.lib.util;

import java.io.Closeable;

/**
 * @author xujiashuai@jovision.com
 * 
 */
public class IOUtil {
	
	public static void closeQuietly(Closeable... closeables) {
		if(closeables == null || closeables.length <= 0){
			return;
		}
		for(Closeable c : closeables){
			try{
				if(c != null){
					c.close();
				}
			}catch(Exception e){
				//
			}
		}
	}
	
}
