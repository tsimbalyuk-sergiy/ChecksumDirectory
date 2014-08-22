package directory.checksum;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Observable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Worker extends Observable implements Runnable {
	static final Logger logger = LogManager.getLogger(Gui.class.getName());
	private String inputFilename;

	private Long filesize = null;
	private HashMD5 outputMD5 = new HashMD5();
	private HashSHA1 outputSHA1 = new HashSHA1();
	private HashSHA256 outputSHA256 = new HashSHA256();
	private HashSHA512 outputSHA512 = new HashSHA512();

	private static int BLOCKSIZE = 1024;
	
	public Worker(String inputFilename) {
		this.setInputFilename(inputFilename);
	}

	public Worker() {
	}
	@Override
	public void run() {
		filesize = getSize();
		sendSize();
		
		hashMD5();
		sendMD5();

		hashSHA1();
		sendSHA1();

		hashSHA256();
		sendSHA256();

		hashSHA512();
		sendSHA512();
	}

	public String getInputFilename() {
		return inputFilename;
	}

	public void setInputFilename(String inputFilename) {
		this.inputFilename = inputFilename;
	}

	private Long getSize() {
		Long l = null;
		try {
			l = new Long(Files.size(Paths.get(inputFilename)));
		} catch (IOException e) {
			logger.error(e);
		}
		return l;
	}
	
	private void sendSize() {
		setChanged();
		notifyObservers(filesize);
	}
	
	private void hashMD5() {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			try (InputStream is = Files.newInputStream(Paths.get(inputFilename))) {
				  DigestInputStream dis = new DigestInputStream(is, md);
				  byte[] buffer = new byte[BLOCKSIZE];
				  while (dis.read(buffer) > -1);
				} catch (IOException e) {
					logger.error(e);
				}
			byte[] aBytes = md.digest();
			outputMD5.setHash(aBytes);
		} catch (NoSuchAlgorithmException e) {
			logger.error(e);
		}
	}

	private void sendMD5() {
		setChanged();
		notifyObservers(outputMD5);
	}

	private void hashSHA1() {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			try (InputStream is = Files.newInputStream(Paths.get(inputFilename))) {
				  DigestInputStream dis = new DigestInputStream(is, md);
				  byte[] buffer = new byte[BLOCKSIZE];
				  while (dis.read(buffer) > -1);
				} catch (IOException e) {
					logger.error(e);
				}
			byte[] aBytes = md.digest();
			outputSHA1.setHash(aBytes);
		} catch (NoSuchAlgorithmException e) {
			logger.error(e);
		}
	}

	private void sendSHA1() {
		setChanged();
		notifyObservers(outputSHA1);
	}
	
	private void hashSHA256() {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			try (InputStream is = Files.newInputStream(Paths.get(inputFilename))) {
				  DigestInputStream dis = new DigestInputStream(is, md);
				  byte[] buffer = new byte[BLOCKSIZE];
				  while (dis.read(buffer) > -1);
				} catch (IOException e) {
					logger.error(e);
				}
			byte[] aBytes = md.digest();
			outputSHA256.setHash(aBytes);
		} catch (NoSuchAlgorithmException e) {
			logger.error(e);
		}
	}

	private void sendSHA256() {
		setChanged();
		notifyObservers(outputSHA256);
	}

	private void hashSHA512() {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			try (InputStream is = Files.newInputStream(Paths.get(inputFilename))) {
				  DigestInputStream dis = new DigestInputStream(is, md);
				  byte[] buffer = new byte[BLOCKSIZE];
				  while (dis.read(buffer) > -1);
				} catch (IOException e) {
					logger.error(e);
				}
			byte[] aBytes = md.digest();
			outputSHA512.setHash(aBytes);
		} catch (NoSuchAlgorithmException e) {
			logger.error(e);
		}
	}

	private void sendSHA512() {
		setChanged();
		notifyObservers(outputSHA512);
	}

}
