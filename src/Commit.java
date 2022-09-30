import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Commit {
	String summary;
	String author;
	String date;
	String myTree;
	String parent;
	String child;
	String sha1;
	String parentTreeLocation;
	
	public Commit(String summary, String author, String parentFileName) throws IOException, NoSuchAlgorithmException {
		this.summary = summary;
		this.author = author;
		//this.pTree = pTree;
		this.parent = parentFileName;
		child = null;
		date = getDate();
		sha1 = getCommitName();
		if (parent != null) {
			this.setFilesChildToMe(parent);
		}
		createTree();
		clearIndex();
		writeFile();
		
		//clearIndex();
	}
	
	//parent of the next commit is the file name of the current commit
	//child of current commit is the file name of the next commit
	//
	public void setFilesChildToMe(String fileName) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		String newContent = "";
		for (int i = 0; i < 6; i++) {
			String line = reader.readLine();
			if (i == 2) {
				newContent += "./objects/" + sha1;
			}
			else {
				newContent += line;
			}
		}
		reader.close();
		
		File toWrite = new File("./objects/" + sha1);
		toWrite.createNewFile();
		PrintWriter writer = new PrintWriter(toWrite);
		writer.print(newContent);
		writer.close();
	}
	
	public String getDate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		return dtf.format(now);  
	}
	
	public void createTree() throws IOException, NoSuchAlgorithmException {
		ArrayList<String> indexList = new ArrayList<String>();//adding previous Commit's tree
		if (parent != null) {
			indexList.add("tree : " + getTreeLocation(parent));
			parentTreeLocation  =  getTreeLocation(parent);
		}
		BufferedReader reader = new BufferedReader(new FileReader("index"));
		String line = reader.readLine();
		while (line != null) {
			String fileName = line.substring(0, line.indexOf(' '));
			String sha1 = line.substring(line.indexOf(' ') + 1);
			String toAdd = "blob : " + sha1 + " " + fileName; 
			indexList.add(toAdd);
			line = reader.readLine();
		}
		Tree newTree = new Tree(indexList);
		myTree = newTree.getAddress();
	}

	public String getTreeLocation(String fileName) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		String newContent = "";
		String line = reader.readLine();
		reader.close();
		return line;
	}
	
	public void clearIndex() throws IOException {
		new PrintWriter("index").close();
	}
	
	public static String getSHA1(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {

	    MessageDigest crypt = MessageDigest.getInstance("SHA-1");
	    crypt.reset();
	    crypt.update(password.getBytes("UTF-8"));

	    return new BigInteger(1, crypt.digest()).toString(16);
	}
	
	
	public void writeFile() throws NoSuchAlgorithmException, IOException {
		File toWrite = new File("./objects/" + sha1);
		System.out.println(sha1);
		toWrite.createNewFile();
		
		PrintWriter writer = new PrintWriter(toWrite);
		writer.println(myTree);
		writer.println(parent);
		writer.println(child);
		writer.println(author);
		writer.println(date);
		writer.println(summary);
		writer.close();
	}
	
	public String getCommitName() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String toSHA1 = summary + date + author + parent;
		System.out.println(getSHA1(toSHA1));
		return getSHA1(toSHA1);
	}
	
	//gets the tree right before the earliest file you want to change
	public String getCorrectTree(ArrayList<String> blobNames) {
		
	}
	
	public static void main(String []args) throws NoSuchAlgorithmException, IOException {
		Index myGit = new Index();
		myGit.add("test1.txt");
		myGit.add("test2.txt");
		Commit commit1 = new Commit("Booblah", "WillSherwood", null);
		myGit.add("test3.txt");
		Commit commit2 = new Commit("Welcome", "Charles", "./objects/" + commit1.getCommitName());
	}
}
