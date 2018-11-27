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
		System.out.print("Encrypting: String: " + S + " ");
		byte[] message = S.getBytes();
		System.out.print("Unencrypted bytes: ");
		for(byte a: message)
			System.out.print(a+" ");
		int count = 0;
		for(int i =0;i<message.length;i++) {
			message[i]=(byte) (message[i]+key[count]);
			count = (count+1)%128;
		}
		System.out.print("Encrypted bytes: ");
		for(byte a: message)
			System.out.print(a+" ");
		System.out.println("");
		return message;
	}

	@Override
	public String decode(byte[] bytes) {
		System.out.print("Decrypting: Encrypted Bytes: ");
		for(byte a: bytes)
			System.out.print(a+" ");
		int count = 0;
		for(int i =0;i<bytes.length;i++) {
			bytes[i]=(byte) (bytes[i]-key[count]);
			count = (count+1)%128;
		}
		System.out.print("Decrypted Bytes: ");
		for(byte a: bytes)
			System.out.print(a+" ");
		String s = new String(bytes);
		System.out.println("String: " +s);
		return s;
	}

}
