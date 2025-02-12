package git;
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
	
	public Commit(String summary, String author) throws IOException, NoSuchAlgorithmException {
		this.summary = summary;
		this.author = author;
		File f = new File("HEAD");
		if(!f.exists()) { 
			createHead();
		}
		if (getHead() == null) {
			parent = null;
		}
		else {
			this.parent = "./objects/" + getHead();
		}
		child = null;
		date = getDate();
		sha1 = getCommitName();
		updateHead(sha1);
		
		if (parent != null) {
			this.setFilesChildToMe(parent);
		}
		
		createMyTree();
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
	
	public void createMyTree() throws IOException, NoSuchAlgorithmException {
		ArrayList<String> indexList = new ArrayList<String>();//adding previous Commit's tree
		ArrayList<String> deleteList = new ArrayList<String>();
		ArrayList<String> editList = new ArrayList<String>();
		
//		System.out.println("Index before " + summary);
//		Path p = Paths.get("index");
//		System.out.println(Files.readString(p));
		
		BufferedReader reader = new BufferedReader(new FileReader("index"));
		String line = reader.readLine();
		while (line != null && !line.equals("")) {
			if (line.substring(0, 6).equals("*edit*")) {
				deleteList.add(line.substring(line.indexOf(" ") + 1, line.indexOf(" ") + 41));
				editList.add(line.substring(6, line.indexOf(" ")));
			}
			else if (line.substring(0, 8).equals("*delete*")) {
				System.out.println("Found a delete.");
				deleteList.add(line.substring(line.indexOf(" ") + 1));
			}
			line = reader.readLine();
		}
		reader.close();
		System.out.println(deleteList);
		
		if (parent != null) {
			if (deleteList.size() == 0) {
				indexList.add("tree : " + getTreeLocation(parent));
				parentTreeLocation  =  getTreeLocation(parent);
			}
			else {
				String newParentTreeLocation  = getLatestPossibleTree(deleteList, getTreeLocation(parent));
				System.out.println(newParentTreeLocation);
				if (!newParentTreeLocation.equals("") && !newParentTreeLocation.equals("*")) {
					indexList.add("tree : " + newParentTreeLocation);
					parentTreeLocation = newParentTreeLocation;
					System.out.println(parentTreeLocation);
					System.out.println("Shouldnt be here");
				}
				else if (newParentTreeLocation.equals("*")) {
					indexList.add("tree : " + getTreeLocation(parent));
					parentTreeLocation  =  getTreeLocation(parent);
					System.out.println("Shouldnt be here either");
				}
				addEditedFiles(editList);
				System.out.println(editList);
			}
		}
		
		System.out.println("Index before I actually add" + summary);
		Path p = Paths.get("index");
		System.out.println(Files.readString(p));
		
		BufferedReader reader2 = new BufferedReader(new FileReader("index"));
		String fileName;
		String line2 = reader2.readLine();
		while (line2 != null && !line2.equals("")) {
			if (!line2.substring(0,1).equals("*")) {
				fileName = line2.substring(0, line2.indexOf(' '));
				String sha1 = line2.substring(line2.indexOf(' ') + 1);
				String toAdd = "blob : " + sha1 + " " + fileName; 
				indexList.add(toAdd);
			}
			line2 = reader2.readLine();
		}
		reader2.close();
		System.out.println("this is going into commit 3");
		System.out.println(indexList);
		Tree newTree = new Tree(indexList);
		myTree = newTree.getAddress();
	}

	public String getTreeLocation(String parent) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(parent));
		String line = reader.readLine();
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
		return getSHA1(toSHA1);
	}
	
	//gets the tree right before the earliest file you want to change
	public String getLatestPossibleTree(ArrayList<String> ShasToDelete, String parentName) throws IOException {
		ArrayList<String> linesToAdd = new ArrayList<String>();
		String currentTreeName = parentName;
		System.out.println(parentName);
		System.out.println("PARENT");
		Boolean found = false;
		String futureTree = "";
		
		while (ShasToDelete.size() > 0) {
			
			if (currentTreeName.equals("") && ShasToDelete.size() != 0) {
				System.out.println("no tree included");
				return "*";
			}
			BufferedReader reader = new BufferedReader(new FileReader("./objects/" + currentTreeName));
			String line = reader.readLine();
			if (line.substring(0, 4).equals("tree")) {
				futureTree = line.substring(7, 47);
				System.out.println("Found tree");
			}
			else if (line.substring(0, 4).equals("blob")) {
				System.out.println(line.substring(0, 4));
				futureTree = "";
			}
			System.out.println(futureTree);
			while (line != null && !line.equals("")) {	
				System.out.println(line);
				if (!line.substring(0, 4).equals("tree")) {
					String sha1 = line.substring(7, 47);
					if (ShasToDelete.contains(sha1)) {
						System.out.println("DELETING");
						System.out.println("CURRENT TREE");
						System.out.println(currentTreeName);
						System.out.println("FUTURE TREE");
						System.out.println(futureTree);
						ShasToDelete.remove(sha1);
						if (ShasToDelete.size() == 0) {
							found = true;
						}
					}
					else {
						//SOMETHING WRONG HERE
						String formattedForIndex = line.substring(48) + " " + line.substring(7, 47);
						linesToAdd.add(formattedForIndex);
					}
				}
				line = reader.readLine();
			}
			currentTreeName = futureTree;
			reader.close();
		}
		System.out.println(linesToAdd);
		addFilesNeeded(linesToAdd);
		if (found) {
			System.out.println("HERE");
			return futureTree;
		}
		else {
			return "";
		}
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
	
	public void updateHead(String head) throws IOException {
		PrintWriter writer = new PrintWriter("HEAD");
		writer.println(head);
		writer.close();
	}
	
	public String getHead() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("HEAD"));
		return reader.readLine();
	}
	
	public void createHead() throws IOException {
		File file = new File("HEAD");
		file.createNewFile();
	}
	
	public static void main(String []args) throws NoSuchAlgorithmException, IOException {
		// THIS IS THE TEST ASSOCIATED WITH THE SCREENSHOTS - PROVES I HAVE FIRST PART OF PROJECT DONE
//		Index myGit = new Index();
//		myGit.add("test1.txt");
//		myGit.add("test2.txt");
//		Commit commit1 = new Commit("1st Commit", "WillSherwood", null);
//		myGit.add("test3.txt");
//		Commit commit2 = new Commit("2nd Commit", "Charles", "./objects/" + commit1.getCommitName());
//		Commit commit3 = new Commit("3rd Commit", "Eliza",  "./objects/" + commit2.getCommitName());
//		myGit.add("test4.txt");
//		myGit.add("test5.txt");
//		Commit commit4 = new Commit("4th Commit", "Ava",  "./objects/" + commit3.getCommitName());
		
		
//		File file = new File("HEAD");
//		if(file.exists()) { 
//			file.delete();
//		}
//		Index myGit = new Index();
//		myGit.add("test1.txt");
//		myGit.editTest("test1.txt");
//		myGit.add("test2.txt");
//		Commit commit1 = new Commit("1st Commit", "WillSherwood");
//		myGit.add("test3.txt");
//		Commit commit2 = new Commit("2nd Commit", "Charles");
//		Commit commit3 = new Commit("3rd Commit", "Eliza");
//		myGit.add("test4.txt");
//		myGit.add("test5.txt");
//		myGit.delete("test3.txt");
//		Commit commit4 = new Commit("4th Commit", "Ava");
//		PrintWriter p = new PrintWriter("HEAD");
//		p.print("");
//		p.close();
	}
}
