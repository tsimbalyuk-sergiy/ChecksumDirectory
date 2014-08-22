package directory.checksum;

public abstract class HashBase {
	protected String hash;

	private final String bin2hex(byte[] aBytes) {
		StringBuilder sb = new StringBuilder(2*aBytes.length);
        for(byte b : aBytes){
            sb.append(String.format("%02x", b&0xff));
        }

        return sb.toString();
	}

	public final String getHash() {
		return hash;
	}

	public final void setHash(byte[] aHash) {
		this.hash = bin2hex(aHash);
	}

	public final void setHash(String hash) {
		this.hash = hash;
	}
	
}
