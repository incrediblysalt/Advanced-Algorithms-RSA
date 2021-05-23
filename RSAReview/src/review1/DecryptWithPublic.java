package review1;

public class DecryptWithPublic{
	public static void main(String[] args){
		try{
		String messagefilename = "encryptedWithPublic.txt";//Default
		if(args != null && args.length>=1){messagefilename = args[0];}//get from commandline
		
		NumberTheoryAlgs.decryptMessageWithPublic(messagefilename);
		}
		catch(Exception E){
			E.printStackTrace();
		}
	}
}