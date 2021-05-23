import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;

public class decrypt {
	
	public static void main(String [] args) throws IOException {
		// S(C) = C^D mod N
		// We need: encryptedWithPrivate.txt, e.txt, n.txt
		
		// Decrypt with Public Key: TRUE
		// Decrypt with Secret Key: FALSE
		boolean decrypt = true;
		BigInteger value;
		String name;
		String target;
		
		if(decrypt) {
			BufferedReader read = Files.newBufferedReader(Paths.get("e.txt"));
			value = new BigInteger(read.readLine());
			name = "decryptedWithPublic.txt";
			target = "encryptedWithPrivate.txt";
		}
		else {
			BufferedReader read = Files.newBufferedReader(Paths.get("d.txt"));
			value = new BigInteger(read.readLine());
			name = "decryptedWithPrivate.txt";
			target = "encryptedWithPublic.txt";
		}
		
		byte [] temp = Files.readAllBytes(Paths.get(target));
		BigInteger cipher = new BigInteger(temp);
		
		BufferedReader read = Files.newBufferedReader(Paths.get("n.txt"));
		BigInteger n = new BigInteger(read.readLine());
		
		System.out.println("Cipher text: " + cipher);
		System.out.println("D: " + value);
		System.out.println("N: " + n);
		
		BigInteger msg = cipher.modPow(value, n);
		
		System.out.println("TEST decrypted: " + msg);
		
		Files.write(Paths.get(name), msg.toByteArray());
		
		System.out.println("Decryption complete!");
	}
}