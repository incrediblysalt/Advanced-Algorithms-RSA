import java.io.FileWriter;
import java.math.BigInteger;
import java.util.Random;

public class generate {

	public static void main(String [] args) {
		try {
			generateNum();
			//test();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void generateNum() throws Exception {	// {E, N, D}
		Random rand = new Random();
		
		// 1. Select a random pair of large prime numbers P and Q,
		//	  such that P != Q
		
		BigInteger p = new BigInteger(1024, rand);
		while(!pseudoprime(p)) {
			p = new BigInteger(1024, rand);
		}
		
		// Now P is random AND prime. Let's make Q now
		BigInteger q = new BigInteger(1024, rand);
		while(!pseudoprime(q) || q.equals(p)) {
			q = new BigInteger(1024, rand);
		}
		
		// 2. Compute N = PQ
		
		BigInteger n = p.multiply(q);
		
		// 3. Select a small odd integer E that is relatively prime
		//	  to phiN = (P-1)(Q-1)
		//	  -> 65537 is a common E value that is large enough,
		//		 yet also small enough
		
		BigInteger phiN = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
		
		BigInteger e = BigInteger.valueOf(65537);
		
		if(!extEuclid(e, phiN)[0].equals(BigInteger.ONE)) {
			throw new Exception("ERROR: e is not relatively prime to phiN");
		}
		
		// 4. Compute D as the multiplicative inverse of E,
		//	  modulo phiN.
		//	  -> use Extended-Euclid as we need the X
		//		 If the result returns negative, add phiN
		//	  -> E^-1 mod phiN = multiplicative inverse = D
		
		BigInteger d = e.modPow(BigInteger.valueOf(-1), phiN);
		
		// Public P = {E, N} -> PUBLISH
		// Private S = {D, N} -> SECRET
		
		/*
		System.out.println("e: " + e);
		System.out.println("n: " + n);
		System.out.println("d: " + d);
		*/
		
		// Make files: e.txt, n.txt, d.txt
		
		FileWriter writer = new FileWriter("e.txt", false);
		writer.write(e.toString());
		writer.close();
		
		writer = new FileWriter("n.txt", false);
		writer.write(n.toString());
		writer.close();
		
		writer = new FileWriter("d.txt", false);
		writer.write(d.toString());
		writer.close();
		
		writer = new FileWriter("message.txt", false);
		writer.write("This was an ambush. Roll for initiative.");
		writer.close();
		
		System.out.println("Number Generation complete!");
	}
	
	public static void test() throws Exception {
		BigInteger p = new BigInteger("101");
		System.out.println("P: " + p);			// P: 101
		
		BigInteger q = new BigInteger("103");
		System.out.println("Q: " + q);			// Q: 103
		
		BigInteger n = p.multiply(q);
		System.out.println("N: " + n);			// N: 10403
		
		BigInteger phiN = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
		System.out.println("PHI(N): " + phiN);	// PHI(N): 10200
		
		BigInteger e = new BigInteger("13");
		System.out.println("E: " + e);			// E: 13
		
		BigInteger d = e.modPow(BigInteger.valueOf(-1), phiN);
		System.out.println("D: " + d);			// D: 6277
		
		String message = "A";
		System.out.println("Message: " + message);	// Message: A
		
		System.out.println("Number Generation complete!\n");
		
		// Let's start on Encryption
		byte [] temp = message.getBytes();
		BigInteger msg = new BigInteger(temp);
		System.out.println("Message BigInt: " + msg);	// Message BigInt: 65
		
		BigInteger cipher = msg.modPow(d, n);
		System.out.println("Encrypted: " + cipher);	// Encrypted: 8692
		
		BigInteger decrypt = cipher.modPow(e, n);
		System.out.println("Decrypted: " + decrypt);	// Decrypted: 65
		
		FileWriter writer = new FileWriter("e.txt", false);
		writer.write(e.toString());
		writer.close();
		
		writer = new FileWriter("n.txt", false);
		writer.write(n.toString());
		writer.close();
		
		writer = new FileWriter("d.txt", false);
		writer.write(d.toString());
		writer.close();
		
		writer = new FileWriter("secret.txt", false);
		writer.write("A");
		writer.close();
	}
	
	public static BigInteger[] extEuclid(BigInteger a, BigInteger b) {
		if(b.equals(BigInteger.ZERO)) {
			return new BigInteger[] {a, BigInteger.ONE, BigInteger.ZERO};
		}
		else {
			BigInteger[] primes = extEuclid(b, a.mod(b));
			BigInteger[] val = new BigInteger[3];
			val[0] = primes[0];
			val[1] = primes[2];
			val[2] = primes[1].subtract(a.divide(b).multiply(primes[2]));
			return val;
		}
	}
	
	public static BigInteger modularExp(BigInteger a, BigInteger b, BigInteger n) {
		BigInteger d = BigInteger.ONE;
		int [] binary = binaryRep(b);
		for(int i=0; i<binary.length; i++) {
			d = (d.multiply(d)).mod(n);
			if(binary[i] == 1) {
				d = (d.multiply(a)).mod(n);
			}
		}
		return d;
	}
	
	public static int[] binaryRep(BigInteger a) {
		String num = a.toString(2);
		String[] numArr = num.split("");
		int[] bin = new int[numArr.length];
		for(int i=0; i<bin.length; i++) {
			bin[i] = Integer.valueOf(numArr[i]);
		}
		
		return bin;
	}
	
	public static boolean pseudoprime(BigInteger n) {
		// FALSE -> Composite
		// TRUE  -> Prime
		BigInteger num = modularExp(new BigInteger("2"), n.subtract(BigInteger.ONE), n).mod(n);
		
		if(num.max(BigInteger.ZERO).equals(BigInteger.ZERO)) {
			// If the MAX between num and 0 is 0, our num must be negative
			num.add(n);
		}
		
		if(!num.equals(BigInteger.ONE)) {
			return false;
		}
		else {
			return true;
		}
	}
}