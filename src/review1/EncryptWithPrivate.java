package review1;

public class EncryptWithPrivate{
	public static void main(String[] args){
		try{
		String messagefilename = "message.txt";//Default
		if(args != null && args.length>=1){messagefilename = args[0];}//get from commandline
		
		NumberTheoryAlgs.encryptMessageWithPrivate(messagefilename);
		}
		catch(Exception E){
			E.printStackTrace();
		}
	}
}