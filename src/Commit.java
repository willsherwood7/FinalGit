import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Commit {
	String summary;
	String author;
	String date;
	String pTree;
	File parent;
	File next;
	
	public Commit(String pTree, String summary, String author, File parent) {
		this.summary = summary;
		this.author = author;
		this.pTree = pTree;
		this.parent = parent;
		next = null;
		date = "1/2/2020";
	}
	
	
	public String getParent() {
		if(parent ==  null) {
			return null;
		}
		return parent.getAbsolutePath();
		
	}
	//parent of the next commit is the file name of the current commit
	//child of current commit is the file name of the next commit
	//
	
	public String getDate() {
		return date;
		
	}
	

	
	
	public static String getSHA1(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {

	    MessageDigest crypt = MessageDigest.getInstance("SHA-1");
	    crypt.reset();
	    crypt.update(password.getBytes("UTF-8"));

	    return new BigInteger(1, crypt.digest()).toString(16);
	}
	
	
	
	public void writeFile() throws NoSuchAlgorithmException, IOException {
		
		String toSHA1 = summary + date + author + parent;
		String str = "";
		File toWrite = new File("./objects/" + getSHA1(str) + ".txt");
		toWrite.createNewFile();
		PrintWriter writer = new PrintWriter(toWrite);
		writer.println(pTree);
		writer.println(parent);
		writer.println();
		writer.println(author);
		writer.println(getDate());
		writer.println(summary);
		writer.close();
		
	}
	
	public static void main(String []args) throws NoSuchAlgorithmException, IOException {
		File exam = new File("test.txt");
		File parent = new File("test1.txt");
		Commit first = new Commit(parent.getPath(), "First commit", "Andrew",null);
		first.writeFile();
	}
	
	
	
	
}
