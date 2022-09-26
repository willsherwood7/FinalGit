
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



public class Index {
	HashMap<String, String> tracker = new HashMap<String, String>();
	HashMap<String, String> toDeleteMap = new HashMap<String, String>();
	
	public Index() {
		initialize();
	}
	
	public void initialize() {
		new File("objects").mkdir();
		Path p = Paths.get("index.txt");
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
		createFile();
		PrintWriter writer = new PrintWriter("index.txt");
		toDeleteMap.put(fileName, fileName + " " + blobby.getUseSHA1());
		for (Map.Entry<String, String> entry : tracker.entrySet()) {
		    writer.println(entry.getKey() + " : " + entry.getValue());
		}
		writer.close();
		
	}
	
	public void removeLine(String lineContent) throws IOException
	{
	    File file = new File("index.txt");
	    List<String> out = Files.lines(file.toPath())
	                        .filter(line -> !line.contains(lineContent))
	                        .collect(Collectors.toList());
	    Files.write(file.toPath(), out, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
	}
	
	
	
	public void remove(String fileName) throws NoSuchAlgorithmException, IOException {
		
		//Path pathToFile = Paths.get("Objects/" + blobby.getSHA1(fileName) + ".txt");
		//Files.delete(pathToFile);
		//tracker.remove(fileName, blobby.getSHA1(fileName));
		
		removeLine(toDeleteMap.get(fileName));
		File toDelete = new File(tracker.get(fileName) + ".txt");
		toDelete.delete();
		tracker.remove(fileName);
		PrintWriter writer = new PrintWriter("index.txt");
		for (Map.Entry<String, String> entry : tracker.entrySet()) {
		    writer.println(entry.getKey() + " " + entry.getValue());
		}
		writer.close();
	}
	
	public static void main(String []args) throws NoSuchAlgorithmException, IOException {
		Index myGit = new Index();
		myGit.add("test1.txt");
		myGit.add("test2.txt");
	}
	
	public void createFile() throws IOException {
		File file = new File("index.txt");
		file.createNewFile();
	}
}
