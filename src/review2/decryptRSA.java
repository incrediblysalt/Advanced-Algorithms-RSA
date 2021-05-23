package review2;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;

public class decryptRSA {

    public static void main(String[] args){
        BigInteger[] keys = getKeys();//e,n,d
        String message = decrypt(keys);
        System.out.println("Decrypted message:");
        System.out.println(message);
    }

    public static String decrypt(BigInteger[] keys){
        File file = new File("encryptedWithPublic.txt"); //change this file name depending on private or public key encryption
        //String target = "encryptedWithPrivate.txt";
        try {
        	
        	// READING NUMBERS:
            BufferedReader reader = new BufferedReader(new FileReader(file));
            BigInteger encrypted = new BigInteger(reader.readLine());
        	
            /*
        	// READING CHARACTERS:
        	byte [] temp = Files.readAllBytes(Paths.get(target));
    		BigInteger encrypted = new BigInteger(temp);
    		*/
            BigInteger decrypted = modExpo(encrypted, keys[0], keys[1]);
            byte[] byteArray = decrypted.toByteArray();
            String message = new String(byteArray);
            reader.close();
            return message;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String binaryRep(BigInteger a) {
        return a.toString(2);
    }

    public static BigInteger modExpo(BigInteger a, BigInteger b, BigInteger n) {
        BigInteger d = BigInteger.ONE;
        String binaryString = binaryRep(b);
        for(int i = 0; i < binaryString.length(); i++) {
            d = (d.multiply(d)).mod(n);
            if(binaryString.charAt(i) == '1') {
                d = d.multiply(a).mod(n);
            }
        }
        return d;
    }

    public static BigInteger[] getKeys(){ //gets e,n,d
        BigInteger[] keys = new BigInteger[3];
        File f1 = new File("d.txt"); //change this file name depending on private or public key encryption
        File f2 = new File("n.txt");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(f1));
            keys[0] = new BigInteger(reader.readLine());
            reader = new BufferedReader(new FileReader(f2));
            keys[1] = new BigInteger(reader.readLine());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return keys;
    }
}
