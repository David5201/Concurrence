package io.github.viscent.mtia.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import sun.misc.Unsafe;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class Tools {
	
	private static final Random rnd = new Random();
    private static final Logger LOGGER = Logger.getAnonymousLogger();
    
    
    public static void startAndWaitTerminated(Thread... threads)
            throws InterruptedException {
    	
    }
    
    public static void startThread(Thread... threads) {
    	
    }
    
    public static void startAndWaitTerminated(Iterable<Thread> threads)
            throws InterruptedException {
    	
    }
    
    
    public static void randomPause(int maxPauseTime) {
    	int sleepTime = rnd.nextInt(maxPauseTime);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    
    public static void randomPause(int maxPauseTime, int minPauseTime) {
    	 int sleepTime = maxPauseTime == minPauseTime ? minPauseTime : rnd
                 .nextInt(maxPauseTime - minPauseTime) + minPauseTime;
         try {
             Thread.sleep(sleepTime);
         } catch (InterruptedException e) {
             Thread.currentThread().interrupt();
         }
    }
    
    public static Unsafe getUnsafe() {
    	try {
			Field f = Unsafe.class.getDeclaredField("theUnsafe");
			f.setAccessible(true);
			try {
				return (Unsafe) f.get(null);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 return null;
    }
    
    public static void silentClose(Closeable... closeable) {
    	
    }
    
    public static void split(String str, String[] result, char delimeter) {
    	
    }
    
    public static void log(String message) {
    	LOGGER.log(Level.INFO, message);
    }
   
    public static String md5sum(final InputStream in) throws NoSuchAlgorithmException, IOException {
    	MessageDigest md = MessageDigest.getInstance("MD5");
    	byte[] buf = new byte[1024];
    	try (DigestInputStream dis = new DigestInputStream(in, md)) {
            while (-1 != dis.read(buf))
                ;
        }
    	byte[] digest = md.digest();
    	BigInteger bigInt = new BigInteger(1, digest);
    	String checkSum = bigInt.toString(16);
    	while (checkSum.length() < 32) {
            checkSum = "0" + checkSum;
        }
    	return checkSum;
    }
    
    public static String md5sum(final File file) throws NoSuchAlgorithmException, IOException {
    	return md5sum(new BufferedInputStream(new FileInputStream(file)));
    }
    
    public static String md5sum(String str) throws NoSuchAlgorithmException, IOException {
    	ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes("UTF-8"));
    	return md5sum(in);
    }
    
    public static void delayedAction(String prompt, Runnable action, int delay/* seconds */) {
    	log("%s in %d seconds."+ prompt+ delay);
    	try {
            Thread.sleep(delay * 1000);
        } catch (InterruptedException ignored) {
        }
    	action.run();
    }
    
    public static Object newInstanceOf(String className) throws InstantiationException,
    IllegalAccessException, ClassNotFoundException {
    	 return Class.forName(className).newInstance();
    }

}
