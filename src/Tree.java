import java.io.BufferedReader;
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Tree {
	HashMap<String, String> map = new HashMap<String, String>();
	
	public Tree(ArrayList<String> blobList) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		String key, value;
		
		// Put values into a hashMap
		for(String str : blobList)
		{
			key = str.substring(0, str.indexOf(":"));
			value = str.substring(str.indexOf(":") + 2, str.length());
			map.put(key, value);
		}
		
		// Create a giant string of all the strings in the ArrayList
		String allStrings = "";
		for(String str : blobList)
		{
			allStrings += str + "\n";
		}
		
		String sha1 = Blob.getSHA1(allStrings);
		
		
	}
	
	

}
