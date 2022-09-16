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

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
		// Creating some files to test
		File test = new File("testfile.txt");
		test.createNewFile();
		File tester = new File("testfile2electricboogaloo.txt");
		test.createNewFile();
		
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
	}

	@Test
	void test() {
		fail("Not yet implemented");
	}
}
