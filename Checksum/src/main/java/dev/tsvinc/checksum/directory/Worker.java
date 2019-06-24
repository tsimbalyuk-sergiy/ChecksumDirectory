package dev.tsvinc.checksum.directory;

import dev.tsvinc.checksum.directory.hash.HashCRC32;
import dev.tsvinc.checksum.directory.hash.HashMD5;
import dev.tsvinc.checksum.directory.hash.HashSHA1;
import dev.tsvinc.checksum.directory.hash.HashSHA256;
import dev.tsvinc.checksum.directory.hash.HashSHA512;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Observable;
import java.util.zip.CRC32;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Worker extends Observable implements Runnable {
  private static final Logger logger = LogManager.getLogger(Gui.class.getName());
  private static final int BLOCK_SIZE = 1024;
  private String inputFilename;
  private Long fileSize = null;
  private HashCRC32 outputCRC32 = new HashCRC32();
  private HashMD5 outputMD5 = new HashMD5();
  private HashSHA1 outputSHA1 = new HashSHA1();
  private HashSHA256 outputSHA256 = new HashSHA256();
  private HashSHA512 outputSHA512 = new HashSHA512();

  Worker() {}

  @Override
  public void run() {
    fileSize = getSize();
    sendSize();

    hashCRC32();
    sendCRC32();

    hashMD5();
    sendMD5();

    hashSHA1();
    sendSHA1();

    hashSHA256();
    sendSHA256();

    hashSHA512();
    sendSHA512();
  }

  void setInputFilename(String inputFilename) {
    this.inputFilename = inputFilename;
  }

  private Long getSize() {
    Long l = null;
    try {
      l = Files.size(Paths.get(inputFilename));
    } catch (IOException e) {
      logger.error(e);
    }
    return l;
  }

  private void sendSize() {
    setChanged();
    notifyObservers(fileSize);
  }

  private void hashCRC32() {
    final CRC32 crc = new CRC32();
    try (InputStream is = Files.newInputStream(Paths.get(inputFilename))) {
      byte[] buffer = new byte[BLOCK_SIZE];
      int iLen;
      while ((iLen = is.read(buffer)) > -1) {
        crc.update(buffer, 0, iLen);
      }
    } catch (IOException e) {
      logger.error(e);
    }
    outputCRC32.setHash(crc.getValue());
  }

  private void sendCRC32() {
    setChanged();
    notifyObservers(outputCRC32);
  }

  private void hashMD5() {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
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
