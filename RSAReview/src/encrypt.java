import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;

public class encrypt {
	
	public static void main(String [] args) throws IOException {
		// P(M) = M^D mod N
		// We need: secret.txt, d.txt, and n.txt
		// ENCRYPT WITH SECRET KEY
		
		// Encrypt with Secret Key: TRUE
		// Encrypt with Public Key: FALSE
		boolean encrypt = true;
		BigInteger value;
		String name;
		
		if(encrypt) {
			BufferedReader read = Files.newBufferedReader(Paths.get("d.txt"));
			value = new BigInteger(read.readLine());
			name = "encryptedWithPrivate.txt";
		}
		else {
			BufferedReader read = Files.newBufferedReader(Paths.get("e.txt"));
			value = new BigInteger(read.readLine());
			name = "encryptedWithPublic.txt";
		}
		
		byte [] temp = Files.readAllBytes(Paths.get("message.txt"));
		BigInteger msg = new BigInteger(temp);
		
		BufferedReader read = Files.newBufferedReader(Paths.get("n.txt"));
		BigInteger n = new BigInteger(read.readLine());
		
		System.out.println("Msg: " + msg);
		System.out.println("D: " + value);
		System.out.println("N: " + n);
		
		BigInteger cipher = msg.modPow(value, n);
		
		System.out.println("TEST encrypted: " + cipher);
		
		Files.write(Paths.get(name), cipher.toByteArray());
		
		System.out.println("Encryption complete!");
	}
}