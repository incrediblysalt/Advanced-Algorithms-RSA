package review1;

import java.math.BigInteger;
import java.util.Random;
import java.io.*;
import java.util.*;
import java.nio.*;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class NumberTheoryAlgs{
	//public static void main(String[] args){}
	
	public static BigInteger[] generateKeys(){
		//pp96
		//generate two large primes P,Q
		int primebitlength = 1024;
		int ebitlength = 16;
		BigInteger P = GeneratePseudoprime(primebitlength);
		BigInteger Q = GeneratePseudoprime(primebitlength);
		
		BigInteger N = P.multiply(Q);
		BigInteger PhiN = (P.subtract(BigInteger.ONE)).multiply(Q.subtract(BigInteger.ONE));
		
		BigInteger E = new BigInteger("65537");
		Random R = new Random();
		while(true){
		//verift E is coprime to phi(n)=(P-1)(Q-1)
		BigInteger GCD = Euclid(PhiN,E);
		if(GCD.equals(BigInteger.ONE)){break;}
		//if not, pick a new odd E
		E = (new BigInteger(ebitlength,R)).multiply(new BigInteger("2")).add(BigInteger.ONE);
		}
		//D is equal to E^-1 mod PhiN
		BigInteger D = MultiplicativeInverse(E,PhiN);
		
		BigInteger[] Keys = new BigInteger[]{E,D,N};
		return Keys;
	}
	
	public static void writeNewKeysToFiles(){
		BigInteger[] keys = generateKeys();
		try {
			PrintWriter writerE = new PrintWriter("e.txt", "UTF-8");
			writerE.print(keys[0].toString());
			writerE.close();
			PrintWriter writerD = new PrintWriter("d.txt", "UTF-8");
			writerD.print(keys[1].toString());
			writerD.close();
			PrintWriter writerN = new PrintWriter("n.txt", "UTF-8");
			writerN.print(keys[2].toString());
			writerN.close();
		}catch (IOException x) {
			System.out.println("Error, IO exception");
		}
	}
	
	public static BigInteger Euclid(BigInteger A, BigInteger B){
		//Assume A>B
		if (B.equals(BigInteger.ZERO)){return A;}
		else{
		BigInteger MOD = A.mod(B);
		return Euclid(B,MOD);
		}
	}
	
	public static BigInteger[] ExtendedEuclid(BigInteger A, BigInteger B)
	{
		if (B.equals(BigInteger.ZERO)){return new BigInteger[]{A,BigInteger.ONE,BigInteger.ZERO};}
		
		else{
		BigInteger MOD = A.mod(B);
		BigInteger[] previousTriple = ExtendedEuclid(B,MOD);
		BigInteger nextY = previousTriple[1].subtract( A.divide(B).multiply(previousTriple[2]) );
		BigInteger[] newTriple = new BigInteger[]{previousTriple[0],previousTriple[2],nextY};
		return newTriple;
		}
	}
	
	public static BigInteger MultiplicativeInverse (BigInteger A, BigInteger N){
		//N and A must be coprime for a unique inverse to exist.
		return ExtendedEuclid(A,N)[1];
		//ax+ny=1 -> ax=1 mod n -> a,x are inverses
	}
	
	public static BigInteger ModularExponentiation(BigInteger A, BigInteger B, BigInteger N){
		int c=0;
		BigInteger D=BigInteger.ONE;
		int blength=B.bitLength();
		boolean[] Bbits = new boolean[blength];
		
		//convert B to bitarray (1:true, 0:false)
		for(int i=0;i<blength;i++){
			Bbits[i]=B.testBit(i);
		}
		
		//compute the exponentiation by repeated squaring
		for (int i=blength-1;i>=0;i--){
			c*=2;
			D=(D.multiply(D)).mod(N);
			if(Bbits[i]){
				c++;
				D=D.multiply(A).mod(N);
			}
		}
		
		return D;		
	}
	
	
	public static boolean Pseudoprime(BigInteger N){
		BigInteger two = new BigInteger("2");
		if (! ModularExponentiation(two,N.subtract(BigInteger.ONE),N).equals(BigInteger.ONE)){
			return false; //composite
		}
		else return true; //pseudoprime or prime
	}
	
	public static BigInteger GeneratePseudoprime(int numbits){
		Random R = new Random();
		BigInteger rndm;
		do{
			rndm = new BigInteger(numbits, R);
		}
		while(!Pseudoprime(rndm));
		return rndm;
	}

	public static BigInteger RSAWithPrivate(BigInteger C){
		try{Scanner scanD = new Scanner(new File("e.txt"));
		//Scanner scanD = new Scanner(new File("d.txt"));
		BigInteger D  = scanD.nextBigInteger();
		Scanner scanN = new Scanner(new File("n.txt"));
		BigInteger N  = scanN.nextBigInteger();
		
		BigInteger M = ModularExponentiation(C,D,N);
		return M;}
		catch(Exception exc){exc.printStackTrace();return null;}
	}
	public static BigInteger RSAWithPublic(BigInteger C){
		try{Scanner scanE = new Scanner(new File("d.txt"));
		//Scanner scanE = new Scanner(new File("e.txt"));
		BigInteger E  = scanE.nextBigInteger();
		Scanner scanN = new Scanner(new File("n.txt"));
		BigInteger N  = scanN.nextBigInteger();
		
		BigInteger M = ModularExponentiation(C,E,N);
		return M;}
		catch(Exception exc){exc.printStackTrace();return null;}
	}
	
	public static void encryptMessageWithPrivate(String messagefilename) throws Exception{
		File message = new File(messagefilename);
		
		//read in message as bytes
		byte[] messagebytes = Files.readAllBytes(message.toPath());
		
		//perform encryption
		BigInteger messageInt = new BigInteger(messagebytes);
		BigInteger Encrypted = RSAWithPrivate(messageInt);
		
		//write encrypted bytes to file
		byte[]encryptedBytes=Encrypted.toByteArray();
		Files.write(Paths.get("encryptedWithPrivate.txt"),encryptedBytes,StandardOpenOption.CREATE);
		
		/*File outputfile = new File("encryptedWithPrivate.txt");
		FileOutputStream outstream = new FileOutputStream(outputfile);
		outstream.write(encryptedBytes);*/
	}
	public static void decryptMessageWithPublic(String messagefilename) throws Exception{
		//read in encrypted file
		File message = new File(messagefilename);
		byte[] messageBytes = Files.readAllBytes(message.toPath());
		BigInteger encrypted = new BigInteger(messageBytes);
		//Decrypt message
		BigInteger decrypted = RSAWithPublic(encrypted);
		//write message to new file
		byte[] retrievedBytes = decrypted.toByteArray();
		Files.write(Paths.get("retrievedMessage.txt"),retrievedBytes,StandardOpenOption.CREATE);
		/*File outputfile = new File("retrievedMessage.txt");
		FileOutputStream outstream = new FileOutputStream(outputfile);
		outstream.write(retrievedBytes);*/
	}
}

