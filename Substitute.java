import java.util.ArrayList;

public class Substitute implements SymCipher {
	private byte[] key;
	private byte[] decode = new byte[256];
	
	public Substitute() {
		ArrayList<Byte> temp = new ArrayList<Byte>();
		for(int i =0;i<256;i++)
			temp.add((byte)i);
		key = new byte[256];
		for(int i =0;i<256;i++) {
			byte b = temp.remove((int) (Math.random()*(temp.size()-2)));
			key[i]=b;
			decode[b & 0xFF]=(byte)i;
			//System.out.println((b&0xFF) +" " + decode[b & 0xFF] );
		}
			
	}
	
	public Substitute(byte[] b) {
		if(b.length!=256) return;
		key= b;
		for(int i=0;i<256;i++)
			decode[key[i] & 0xFF]=(byte)i;
	}
	
	@Override
	public byte[] getKey() {
		return key;
	}

	@Override
	public byte[] encode(String S) {
		System.out.print("Encrypting: String: "+S + " ");
		byte[] message = S.getBytes();
		System.out.print("Unencrypted bytes: ");
		for(byte a: message)
			System.out.print(a+" ");
		for(int i =0;i<message.length;i++)
			message[i]=key[message[i]];
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
		for(int i=0;i<bytes.length;i++)
			bytes[i]=decode[bytes[i] & 0xFF];
		System.out.print("Decrypted Bytes: ");
		for(byte a: bytes)
			System.out.print(a+" ");
		String s = new String(bytes);
		System.out.println("String: " +s);
		return s;
	}

}
