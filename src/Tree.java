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
	
	ArrayList<String> list = new ArrayList<String>();
	
	public Tree(ArrayList<String> blobList) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		String allStrings = "";
		for(int i = 0; i < blobList.size(); i++)
		{
			if (i < blobList.size() - 1) { 
				allStrings += blobList.get(i) + "\n";
			}
			else {
				allStrings += blobList.get(i)
			}
		}
		String sha1 = Blob.getSHA1(allStrings);
		System.out.println(sha1);
		// This code is from my own Blob method. Convenient how that works out. Basically creates the file with the sha1 as the name.
		Path p = Paths.get(sha1);
        try {
            Files.writeString(p, allStrings, StandardCharsets.ISO_8859_1);
        } catch (IOException e) {
            e.printStackTrace();
        }		
        
	}
	
	public ArrayList<String> getList() {
		return list;
	}
}
