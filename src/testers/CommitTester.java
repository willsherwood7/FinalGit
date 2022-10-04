package testers;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;

import git.Commit;
import git.Index;

class CommitTester {

	@Test
	void test() throws NoSuchAlgorithmException, IOException {
		File file = new File("HEAD");
		if(file.exists()) { 
			file.delete();
		}
		
		Index myGit = new Index();
		myGit.add("test1.txt");
		myGit.add("test2.txt");
		Commit commit1 = new Commit("1st Commit", "WillSherwood");
		myGit.add("test3.txt");
		Commit commit2 = new Commit("2nd Commit", "Charles");
		myGit.delete("test1.txt");
		Commit commit3 = new Commit("3rd Commit", "Eliza");
		myGit.add("test4.txt");
		myGit.delete("test3.txt");
		myGit.add("test5.txt");
		myGit.add("test6.txt");
		Commit commit4 = new Commit("4th Commit", "Ava");
		myGit.editTest("test2.txt");
		myGit.edit("test2.txt");
		myGit.add("test8.txt");
		Commit commit5 = new Commit("5th Commit", "Sydney");
		
		PrintWriter pw = new PrintWriter("HEAD");
		pw.print("");
		pw.close();
		
		//ALL OF THIS FUNCTIONS TOO! Proof that my deletes and edits work	
//		Index myGit = new Index();
//		myGit.add("test1.txt");
//		myGit.add("test2.txt");
//		Commit commit1 = new Commit("1st Commit", "WillSherwood");
//		myGit.add("test3.txt");
//		Commit commit2 = new Commit("2nd Commit", "Charles");
//		myGit.delete("test2.txt");
//		Commit commit3 = new Commit("3rd Commit", "Eliza");
//		myGit.add("test4.txt");
//		myGit.delete("test3.txt");
//		myGit.add("test5.txt");
//		myGit.add("test6.txt");
//		Commit commit4 = new Commit("4th Commit", "Ava");
//		myGit.editTest("test1.txt");
//		myGit.edit("test1.txt");
//		myGit.add("test7.txt");
//		Commit commit5 = new Commit("5th Commit", "Sydney");
//		
//		PrintWriter p = new PrintWriter("HEAD");
//		p.print("");
//		p.close();	
//		Index myGit = new Index();
//		myGit.add("test1.txt");
//		myGit.add("test2.txt");
//		Commit commit1 = new Commit("1st Commit", "WillSherwood");
//		myGit.add("test3.txt");
//		Commit commit2 = new Commit("2nd Commit", "Charles");
//		myGit.editTest("test3.txt");
//		myGit.edit("test3.txt");
//		myGit.add("test4.txt");
//		Commit commit3 = new Commit("3rd Commit", "Eliza");
//		
//		PrintWriter p = new PrintWriter("HEAD");
//		p.print("");
//		p.close();
//		Index myGit = new Index();
//		myGit.add("test1.txt");
//		myGit.add("test2.txt");
//		Commit commit1 = new Commit("1st Commit", "WillSherwood");
//		myGit.add("test3.txt");
//		Commit commit2 = new Commit("2nd Commit", "Charles");
//		myGit.editTest("test1.txt");
//		myGit.edit("test1.txt");
//		myGit.add("test4.txt");
//		Commit commit3 = new Commit("3rd Commit", "Eliza");
//		
//		PrintWriter p = new PrintWriter("HEAD");
//		p.print("");
//		p.close();
//		Index myGit = new Index();
//		myGit.add("test1.txt");
//		myGit.add("test2.txt");
//		Commit commit1 = new Commit("1st Commit", "WillSherwood");
//		myGit.add("test3.txt");
//		Commit commit2 = new Commit("2nd Commit", "Charles");
//		myGit.delete("test3.txt");
//		myGit.add("test4.txt");
//		Commit commit3 = new Commit("3rd Commit", "Eliza");
//		
//		PrintWriter p = new PrintWriter("HEAD");
//		p.print("");
//		p.close();
//		Index myGit = new Index();
//		myGit.add("test1.txt");
//		myGit.add("test2.txt");
//		Commit commit1 = new Commit("1st Commit", "WillSherwood");
//		myGit.add("test3.txt");
//		Commit commit2 = new Commit("2nd Commit", "Charles");
//		myGit.delete("test1.txt");
//		myGit.add("test4.txt");
//		Commit commit3 = new Commit("3rd Commit", "Eliza");
//		
	}

}
