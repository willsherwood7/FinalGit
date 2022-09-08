
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class index {
	
	
	public index() {
		
	}
	public void initialize() {
		boolean project = new File("/path/project").mkdirs();
		Path p = Paths.get("index.txt");
        try {
            Files.writeString(p, "", StandardCharsets.ISO_8859_1);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
	}
	
	public void add(String fileName) throws IOException {
		//Blob blobby = new Blob(fileName);
	
		
	}
	
	public void remove() {
		
	}
}
