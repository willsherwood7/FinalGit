import java.security.DigestInputStream;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.util.HashMap;
//Takes in a filename and whatever is in the file you use SHA1 on that to make a new file with the name of the file is whatever SHA1 spits out then inside that new file you have what was in the original file. 

public class Blob {
	private String fileName;
	private static String useSHA1;
	private static String content;
	
	public String getSHA1(String fileName) throws IOException, NoSuchAlgorithmException {
        FileInputStream fileInputStream = new FileInputStream(fileName);
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        DigestInputStream digestInputStream = new DigestInputStream(fileInputStream, digest);
        byte[] bytes = new byte[1024];
        // read all file content
        while (digestInputStream.read(bytes) > 0);

//        digest = digestInputStream.getMessageDigest();
        digestInputStream.close();
        byte[] resultByteArry = digest.digest();
        return bytesToHexString(resultByteArry);
    }

	
	 public static String bytesToHexString(byte[] bytes) {
	        StringBuilder sb = new StringBuilder();
	        for (byte b : bytes) {
	            int value = b & 0xFF;
	            if (value < 16) {
	                sb.append("0");
	            }
	            sb.append(Integer.toHexString(value));
	        }
	        return sb.toString();
	 }
	
	
	public void makeNewFile() throws IOException {
		File source = new File(fileName);
		File dest = new File("objects/" + useSHA1);
		copyFileUsingStream(source, dest);
        
	}
	
	public Blob(String fileName) throws IOException, NoSuchAlgorithmException {
        this.fileName = fileName;
		//System.out.println(content);
        String SHA1 = getSHA1(fileName);
        //System.out.println(SHA1);
        useSHA1 = SHA1;
        //System.out.println(useSHA1);
        makeNewFile();
	}

	public static void main(String []args) throws IOException, NoSuchAlgorithmException {
		Blob blobby = new Blob("test1.txt");
		Blob blobby2 = new Blob("test2.txt");
		System.out.println(blobby.getUseSHA1());
		System.out.println(blobby2.getUseSHA1());
		//blobby.makeNewFile();
		//System.out.println(blobby.getSHA1("Hello"));
		
	}
	
	private static void copyFileUsingStream(File source, File dest) throws IOException {
	    InputStream is = null;
	    OutputStream os = null;
	    try {
	        is = new FileInputStream(source);
	        os = new FileOutputStream(dest);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	    }
	    finally {
	    	is.close();
	        os.close();
	    }
	}
	
	public String getUseSHA1() {
		return useSHA1;
	}
	
	public void createFile() throws IOException {
		File file = new File("index.txt");
		file.createNewFile();
	}
}




	


