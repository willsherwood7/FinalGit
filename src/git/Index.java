package git;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;



public class Index {
	HashMap<String, String> tracker = new HashMap<String, String>();
	HashMap<String, String> toDeleteMap = new HashMap<String, String>();
	
	public Index() {
		initialize();
	}
	
	public void initialize() {
		new File("objects").mkdir();
		Path p = Paths.get("index");
        try {
            Files.writeString(p, "", StandardCharsets.ISO_8859_1);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
	}
	
	public void add(String fileName) throws IOException, NoSuchAlgorithmException {
		Blob blobby = new Blob(fileName);
		tracker.put(fileName, blobby.getUseSHA1());
		toDeleteMap.put(fileName, fileName + " " + blobby.getUseSHA1());
		
		Path p = Paths.get("index");
		String previousContent = Files.readString(p);
		String newContent;
		if (previousContent.length() == 0) {
			newContent = previousContent + fileName + " " + blobby.getUseSHA1();
		}
		else {
			newContent = previousContent + "\n" + fileName + " " + blobby.getUseSHA1();
		}
				
		PrintWriter writer = new PrintWriter("index");
		writer.print(newContent);
		writer.close();
	}
	
	public void removeLineFromFile(String lineContent) throws IOException
	{
	    File file = new File("index");
	    List<String> out = Files.lines(file.toPath())
	                        .filter(line -> !line.contains(lineContent))
	                        .collect(Collectors.toList());
	    Files.write(file.toPath(), out, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
	}
	
	public void remove(String fileName) throws NoSuchAlgorithmException, IOException {
		Path pathToFile = Paths.get("objects/" + tracker.get(fileName));
		Files.delete(pathToFile);
		removeLineFromFile(toDeleteMap.get(fileName));
		tracker.remove(fileName);
	}
	
	public void delete(String fileName) throws IOException {
		Path p = Paths.get("index");
		String previousContent = Files.readString(p);
		String newContent;
		if (previousContent.length() == 0) {
			newContent = previousContent + "*delete*" + fileName + " " + tracker.get(fileName);
		}
		else {
			newContent = previousContent + "\n*delete*" + fileName + " " + tracker.get(fileName);
		}
				
		PrintWriter writer = new PrintWriter("index");
		writer.print(newContent);
		writer.close();
	}
	
	//edit file should be *edit*file1.txt 293749824793487 : filenew.txt 3298409823
	public void edit(String fileName) throws IOException, NoSuchAlgorithmException {
		String oldSha1 = tracker.remove(fileName);
		toDeleteMap.remove(fileName);
		
		Blob blobby = new Blob(fileName);
		tracker.put(fileName, blobby.getUseSHA1());
		toDeleteMap.put(fileName, fileName + " " + blobby.getUseSHA1());
		
		Path p = Paths.get("index");
		String previousContent = Files.readString(p);
		String newContent;
		if (previousContent.length() == 0) {
			newContent = previousContent + "*edit*" + fileName + " " + oldSha1 + " : " + fileName + " " + tracker.get(fileName);
		}
		else {
			newContent = previousContent + "\n*edit*" + fileName + " " + oldSha1 + " : " + fileName + " " + tracker.get(fileName);
		}
				
		PrintWriter writer = new PrintWriter("index");
		writer.print(newContent);
//		System.out.println(newContent);
		writer.close();
	}
	
	public void editTest(String fileName) throws IOException {
		PrintWriter writer = new PrintWriter(fileName);
		String n = "" + (Math.random() * 100);
		writer.println(n);
		writer.close();
	}
	
	public static void main(String []args) throws NoSuchAlgorithmException, IOException {
		Index myGit = new Index();
		myGit.add("test1.txt");
		myGit.add("test2.txt");
		myGit.add("test3.txt");
		myGit.remove("test2.txt");
	}
}
