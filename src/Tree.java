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
	
	public Tree(ArrayList<String> blobList) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		
		// Create a giant string of all the strings in the ArrayList
		String allStrings = "";
		for(String str : blobList)
		{
			allStrings += str + "\n";
		}
		
		String sha1 = Blob.getSHA1(allStrings);
		System.out.println(sha1);
		
		// This code is from my own Blob method. Convenient how that works out. Basically creates the file with the sha1 as the name.
		Path p = Paths.get(".\\objects\\" + sha1);
        try {
            Files.writeString(p, allStrings, StandardCharsets.ISO_8859_1);
        } catch (IOException e) {
            e.printStackTrace();
        }		
        
	}	
}
