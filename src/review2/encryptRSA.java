package review2;

import java.io.*;
import java.math.BigInteger;

public class encryptRSA {
    public static void main(String[] args){
        BigInteger[] keys = getKeys(); //e,n
        encrypt(keys);
        System.out.println("Encrypted!");
    }

    public static void encrypt(BigInteger[] keys){ //makes encryptedWith---.txt
        File fileE = new File("encryptedWithPublic.txt"); //change this file name depending on private or public key encryption
        File fileS = new File("message.txt");
        try {
            if(fileE.createNewFile()){
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileE));
                BufferedReader reader = new BufferedReader(new FileReader(fileS));
                String message = reader.readLine();
                BigInteger encrypted = modExpo(new BigInteger(message.getBytes()), keys[0], keys[1]);
                writer.write(encrypted.toString()); //write to encrypted.txt
                writer.close();
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public static BigInteger[] getKeys(){ //gets e, n
        BigInteger[] keys = new BigInteger[2];
        File f1 = new File("e.txt"); //change this file name depending on private or public key encryption
        File f2 = new File("n.txt");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(f1));
            keys[0] = new BigInteger(reader.readLine());
            reader = new BufferedReader(new FileReader(f2));
            keys[1] = new BigInteger(reader.readLine());
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return keys;
    }

}
