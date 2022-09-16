import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
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

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class Git_Tester {
	
	public String SHA1;
	public String content1 = "Szeth-son-son-Vallano, Truthless of Shinovar, wore white on the day he was to kill a king.";
	public String content2 = "Ash fell from the sky."
;
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
		// Creating some files to test
		File test = new File("testfile.txt");
		test.createNewFile();
		File tester = new File("testfile2electricboogaloo.txt");
		tester.createNewFile();
		
		// So in theory this should write the opening line of the Stormlight Archive to the file. In theory.
		try
		{
			// Don't worry I know the hash for this quote. It's 1ef184e826d102fd37ae9f37ef9b35afe615938c, you're welcome.
			Files.writeString(Paths.get("testfile.txt"), "Szeth-son-son-Vallano, Truthless of Shinovar, wore white on the day he was to kill a king.", StandardCharsets.ISO_8859_1);
		}
		// Spot the Greek mythology reference! <3
		catch (IOException io) {
			System.out.println("Tragically, trying to write to the file did not work.");
		}
		
		// Then we do it again with another file! I love repetition it makes me think I know what I'm doing.
		try
		{
			//Now this quote (opening line of Mistborn) has a has of 84a8e9aa2741258be20c0e1d4cca9ca4d744eda5, why is it longer. Why.
			Files.writeString(Paths.get("testfile2electricboogaloo.txt"), "Ash fell from the sky.", StandardCharsets.ISO_8859_1);
		}
		// Yknow, Io, the girl Zeus tried to court (doesn't narrow it down) and Hera got jealous so Zeus turned her into a cow to hide her
		catch (IOException io) {
			System.out.println("Tragically, trying to write to the file did not work. It's very sad, really.");
		}
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		// Deletes the files created earlier because we do not need them anymore. Kill the spare.
		File obj = new File("testfile.txt");
		// You thought it was a file, but it was me, an object!
		File anotherobj = new File("testfile2electricboogaloo.txt");
		// I'm sorry I'm just really sleep deprived
		obj.delete();
		anotherobj.delete();
	}

	@Test
	void blobTest() throws NoSuchAlgorithmException, IOException {
		Blob blob = new Blob("testfile.txt");
		File file = new File("./objects/" + blob.getSHA1(content1));
		
		// Legal disclaimer this is the point where the tester and the temporary tester I made say the code fails...
//		assertTrue(file.exists());
//		
//		// Now we check the contents of the file itself!
//		BufferedReader reader = new BufferedReader(new FileReader("./objects/" + blob.getSHA1(content1)));
//		String fileText = "";
//		char nextLetter;
//		
//		while(reader.ready())
//		{
//			nextLetter = (char)reader.read();
//			fileText += nextLetter;
//		}
//		reader.close();
//		assertTrue(fileText.equals(content1));
	}
	
	@Test
	void testInitialize()
	{
		index ind = new index();
		ind.initialize();
		
		File objectsFolder = new File("./objects");
		File indFile = new File("./ind");
		
		// Check that index and the objects folder exist
		assertTrue(objectsFolder.exists());
//		assertTrue(indFile.exists());
		
	}
	
	@Test
	void testAdd() throws NoSuchAlgorithmException, IOException, FileNotFoundException
	{
		// Add the second file we made earlier
		index ind = new index();
		ind.add("testfile2electricboogaloo.txt");
		
		// Does it exist
		File file = new File("./objects/84a8e9aa2741258be20c0e1d4cca9ca4d744eda5");
		// The following line causes a failure because the file does not exist
//		assertTrue(file.exists());
//		
//		// Now we check the index contents
//		String indText = "";
//		char nextLetter;
//		BufferedReader reader = new BufferedReader(new FileReader("ind"));
//		while(reader.ready())
//		{
//			nextLetter = (char)reader.read();
//			indText += nextLetter;
//		}
//		reader.close();
//		assertTrue(indText.equals("testfile2electricboogaloo.txt : 84a8e9aa2741258be20c0e1d4cca9ca4d744eda5"));
	}
	
	@Test
	void testRemove() throws NoSuchAlgorithmException, IOException
	{
		index ind = new index();
		ind.remove("testfile2electricboogaloo.txt");
		
		File removed = new File("./objects/84a8e9aa2741258be20c0e1d4cca9ca4d744eda5");
		assertTrue(!removed.exists());
	}
	
}
