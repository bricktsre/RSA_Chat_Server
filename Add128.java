import java.security.SecureRandom;

public class Add128 implements SymCipher {
	private byte[] key;
	
	public Add128() {
		key = new byte[128];
		SecureRandom random = new SecureRandom();
		random.nextBytes(key);
	}
	
	public Add128(byte[] b) {
		if(b.length!=128) return;
		else key= b;
	}
	
	@Override
	public byte[] getKey() {
		return key;
	}

	@Override
	public byte[] encode(String S) {
		byte[] message = S.getBytes();
		int count = 0;
		for(int i =0;i<message.length;i++) {
			message[i]=(byte) (message[i]+key[count]);
			count = (count+1)%count;
		}
		return message;
	}

	@Override
	public String decode(byte[] bytes) {
		int count = 0;
		for(int i =0;i<bytes.length;i++) {
			bytes[i]=(byte) (bytes[i]-key[count]);
			count = (count+1)%count;
		}
		return bytes.toString();
	}

}
