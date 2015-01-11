import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TestSHA{
	static String SHA(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		MessageDigest m = MessageDigest.getInstance("SHA1");
		byte[] output = m.digest(str.getBytes("UTF-8"));
		
		StringBuilder s = new StringBuilder();
		for(byte b : output){
			s.append(String.format("%02x", b));
			
		}
		return s.toString();
		
		
	}
	public static void main(String[] args){
		FileInputStream fileInputStream = null;
		File file  = new File(args[0]);
		byte[] byteArray = new byte[(int)file.length()];
		
		try{
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(byteArray);
			fileInputStream.close();
			String s = new String(byteArray,"UTF-8");
		System.out.println(SHA(s));
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

}

