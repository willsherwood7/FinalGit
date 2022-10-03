package git;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Formatter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Tree {
	
	ArrayList<String> list = new ArrayList<String>();
	String address;
	
	public Tree(ArrayList<String> blobList) throws NoSuchAlgorithmException, IOException
	{
		String allStrings = "";
		for(int i = 0; i < blobList.size(); i++)
		{
			if (i < blobList.size() - 1) { 
				allStrings += blobList.get(i) + "\n";
			}
			else {
				allStrings += blobList.get(i);
			}
		}
		String sha1 = getSha1(allStrings);
		address = sha1;
		// This code is from my own Blob method. Convenient how that works out. Basically creates the file with the sha1 as the name.
		Path p = Paths.get("./objects/" + sha1);
        try {
            Files.writeString(p, allStrings, StandardCharsets.ISO_8859_1);
        } catch (IOException e) {
            e.printStackTrace();
        }		
        
	}
	
	public ArrayList<String> getList() {
		return list;
	}
	
	public String getAddress() {
		return address;
	}
	
	private static String getSha1(String starter)
	{
	    String sha1 = "";
	    try
	    {
	        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
	        crypt.reset();
	        crypt.update(starter.getBytes("UTF-8"));
	        sha1 = byteToHex(crypt.digest());
	    }
	    catch(NoSuchAlgorithmException e)
	    {
	        e.printStackTrace();
	    }
	    catch(UnsupportedEncodingException e)
	    {
	        e.printStackTrace();
	    }
	    return sha1;
	}

	private static String byteToHex(final byte[] hash)
	{
	    Formatter formatter = new Formatter();
	    for (byte b : hash)
	    {
	        formatter.format("%02x", b);
	    }
	    String result = formatter.toString();
	    formatter.close();
	    return result;
	}
	
	public static void main (String [] args) throws NoSuchAlgorithmException, IOException {
		ArrayList<String> test = new ArrayList<String>();
		test.add("blob : da39a3ee5e6b4b0d3255bfef95601890afd80709 test1.txt");
		test.add("blob : da39a3ee5e6b4b0d3255bfef95601890afd80709 test2.txt");
		Tree myTree = new Tree(test);
	}
}
