import java.util.ArrayList;

public class Substitute implements SymCipher {
	private byte[] key;
	private byte[] decode;
	
	public Substitute() {
		ArrayList<Byte> temp = new ArrayList<Byte>();
		for(int i =0;i<256;i++)
			temp.add((byte)i);
		key = new byte[256];
		for(int i =0;i<256;i++) {
			byte b = (byte)(Math.random()*(temp.size()-2));
			key[i]=b;
			decode[b]=(byte)i;
		}
			
	}
	
	public Substitute(byte[] b) {
		if(b.length!=256) return;
		key= b;
		for(int i=0;i<key.length;i++)
			decode[i]=key[i];
	}
	
	@Override
	public byte[] getKey() {
		return key;
	}

	@Override
	public byte[] encode(String S) {
		byte[] message = S.getBytes();
		for(int i =0;i<message.length;i++)
			message[i]=key[i];
		return message;
	}

	@Override
	public String decode(byte[] bytes) {
		for(int i=0;i<bytes.length;i++)
			bytes[i]=decode[i];
		return bytes.toString();
	}

}
