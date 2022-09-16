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
	
	public Tree(ArrayList<String> blobList)
	{
		String key, value;
		
		for(String str : blobList)
		{
			key = str.substring(0, str.indexOf(":"));
			value = str.substring(str.indexOf(":") + 1, str.length());
			map.put(key, value);
		}
	}
	
	

}
