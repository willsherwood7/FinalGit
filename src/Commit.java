import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
		ArrayList<String> deleteList = new ArrayList<String>();
		ArrayList<String> editList = new ArrayList<String>();
		
		BufferedReader reader = new BufferedReader(new FileReader("index"));
		String line = reader.readLine();
		while (line != null) {
			if (line.substring(0, 6).equals("*edit*")) {
				deleteList.add(line.substring(line.indexOf(" ")));
				editList.add(line.substring(5, line.indexOf(" ") - 1));
			}
			else if (line.substring(0, 8).equals("*delete*")) {
				deleteList.add(line.substring(line.indexOf(" ")));
			}
			line = reader.readLine();
		}
		reader.close();
		
		if (parent != null) {
			if (deleteList.size() > 0) {
				indexList.add("tree : " + getTreeLocation(parent));
				parentTreeLocation  =  getTreeLocation(parent);
			}
			else {
				String newParentTreeLocation  = getLatestPossibleTree(deleteList, getTreeLocation(parent));
				indexList.add("tree : " + parentTreeLocation);
				parentTreeLocation = newParentTreeLocation;
				addEditedFiles(editList);
			}
		}
		
		//This part only adds stuff from Index
		//Going to create a file
		BufferedReader reader2 = new BufferedReader(new FileReader("index"));
		String fileName;
		String line2 = reader2.readLine();
		while (line2 != null) {
			if (!line2.substring(0,1).equals("*")) {
				fileName = line2.substring(0, line2.indexOf(' '));
				String sha1 = line2.substring(line2.indexOf(' ') + 1);
				String toAdd = "blob : " + sha1 + " " + fileName; 
				indexList.add(toAdd);
			}
			line2 = reader2.readLine();
		}
		reader2.close();
		
		Tree newTree = new Tree(indexList);
		myTree = newTree.getAddress();
	}

	public String getTreeLocation(String parentTree) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(parentTree));
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
	public String getLatestPossibleTree(ArrayList<String> ShasToDelete, String parentName) throws IOException {
		ArrayList<String> linesToAdd = new ArrayList<String>();
		String currentTreeName = parentName;
		while (ShasToDelete.size() > 0) {
			BufferedReader reader = new BufferedReader(new FileReader(currentTreeName));
			String line = reader.readLine();
			String futureTree = "";
			if (line.substring(0, 4).equals("tree")) {
				futureTree = line.substring(7, 48);
			}
			else if (line.substring(0, 4).equals("blob")) {
				futureTree = "";
			}
			line = reader.readLine();
			while (line != null) {
				String sha1 = line.substring(7, 48);
				if (ShasToDelete.contains(sha1)) {
					ShasToDelete.remove(sha1);
					if (ShasToDelete.size() == 0) {
						return futureTree;
					}
				}
				else {
					String formattedForindex = line.substring(49) + line.substring(7, 48);
					linesToAdd.add(formattedForindex);
				}
				line = reader.readLine();
			}
			reader.close();
		}
		return "";
	}
	
	public void addEditedFiles(ArrayList<String> fileNamesToUpdate) throws NoSuchAlgorithmException, IOException {
		for (int i = 0; i < fileNamesToUpdate.size(); i++) {
			Blob blobby = new Blob(fileNamesToUpdate.get(i));
			
			Path p = Paths.get("index");
			String previousContent = Files.readString(p);
			String newContent;
			if (previousContent.length() == 0) {
				newContent = previousContent + fileNamesToUpdate.get(i) + " " + blobby.getUseSHA1();
			}
			else {
				newContent = previousContent + "\n" + fileNamesToUpdate.get(i) + " " + blobby.getUseSHA1();
			}
			PrintWriter writer = new PrintWriter("index");
			writer.print(newContent);
			writer.close();
		}
	}
	
	//HAVE TO WRITE
	public void addFilesNeeded(ArrayList<String> linesToAdd) throws IOException {
		for (int i = 0; i < linesToAdd.size(); i++) {
			
			Path p = Paths.get("index");
			String previousContent = Files.readString(p);
			String newContent;
			if (previousContent.length() == 0) {
				newContent = previousContent + linesToAdd.get(i);
			}
			else {
				newContent = previousContent + "\n" + linesToAdd.get(i);
			}
			PrintWriter writer = new PrintWriter("index");
			writer.print(newContent);
			writer.close();
		}
	}
	
	public static void main(String []args) throws NoSuchAlgorithmException, IOException {
		Index myGit = new Index();
		myGit.add("test1.txt");
		myGit.add("test2.txt");
		Commit commit1 = new Commit("Booblah", "WillSherwood", null);
		myGit.add("test3.txt");
//		Commit commit2 = new Commit("Welcome", "Charles", "./objects/" + commit1.getCommitName());
//		Commit commit3 = new Commit("3rd Commit", "Eliza",  "./objects/" + commit2.getCommitName());
//		myGit.add("test4.txt");
//		myGit.add("test5.txt");
//		Commit commit4 = new Commit("Last Commit", "Eliza",  "./objects/" + commit2.getCommitName());

	}
}
