import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Commit {
	String summary;
	String author;
	String date;
	String pTree;
	Commit parent;
	Commit child;
	
	public Commit(String pTree, String summary, String author, Commit parent) {
		this.summary = summary;
		this.author = author;
		this.pTree = pTree;
		this.parent = parent;
		if (parent != null) {
			this.parent.setChild(this);
		}
		child = null;
		date = getDate();
	}
	
	//parent of the next commit is the file name of the current commit
	//child of current commit is the file name of the next commit
	//
	public void setChild(Commit child) {
		this.child = child;
	}
	
	public String getDate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		return dtf.format(now);  
	}
	
	public static String getSHA1(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {

	    MessageDigest crypt = MessageDigest.getInstance("SHA-1");
	    crypt.reset();
	    crypt.update(password.getBytes("UTF-8"));

	    return new BigInteger(1, crypt.digest()).toString(16);
	}
	
	public void writeFile() throws NoSuchAlgorithmException, IOException {
		String toSHA1 = summary + date + author + parent;
		File toWrite = new File("./objects/" + getSHA1(toSHA1));
		toWrite.createNewFile();
		
		PrintWriter writer = new PrintWriter(toWrite);
		writer.println(pTree);
		writer.println(parent);
		writer.println(child);
		writer.println(author);
		writer.println(date);
		writer.println(summary);
		writer.close();
		
	}
	
	public static void main(String []args) throws NoSuchAlgorithmException, IOException {
		Commit commit1 = new Commit("./objects/59c4dd553b054c2028eb5179b3d2c3238f9ae84a", "Booblah", "WillSherwood", null);
		commit1.writeFile();
		Commit commit2 = new Commit("./objects/59c4de553b054c2028eb5179b3d2c3238f9ae84a", "Welcome", "Charles", commit1);
		commit2.writeFile();
	}
}
