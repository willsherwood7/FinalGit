import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.HashMap;
//Takes in a filename and whatever is in the file you use SHA1 on that to make a new file with the name of the file is whatever SHA1 spits out then inside that new file you have what was in the original file. 

public class Blob {
	private String fileName;
	private String useSHA1;
	private String content;
	
	public static String getSHA1(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {

	    MessageDigest crypt = MessageDigest.getInstance("SHA-1");
	    crypt.reset();
	    crypt.update(password.getBytes("UTF-8"));

	    return new BigInteger(1, crypt.digest()).toString(16);
	}
	
	
	public void makeNewFile() {
		//PrintWriter printer = new PrintWriter();
		
		Path p = Paths.get("Objects/" + useSHA1 + ".txt");
        try {
            Files.writeString(p, content, StandardCharsets.ISO_8859_1);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
	}
	
	
	
	public Blob(String fileName) throws IOException, NoSuchAlgorithmException {
		this.fileName = fileName;
		String str = "";
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        while(br.ready()){
        	
        	str = str + (char)br.read();
        }
       
        content = str;
        String SHA1 = getSHA1(str);
        useSHA1 = SHA1;
      
        
        
        
	}

	

public static void main(String []args) throws IOException, NoSuchAlgorithmException {
	Blob blobby = new Blob("Getter.txt");
	blobby.makeNewFile();
	//System.out.println(blobby.getSHA1("Hello"));
	
}

}




	
//ifconfig
//ifconfig/all

