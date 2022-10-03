import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class TreeTester {
	
	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		ArrayList<String> list = new ArrayList<String>();
		list.add("62X : d99ed7744a09c6078ef391a07eca50cd2d3622da");
		list.add("Anika : 01670a0aee5d4289cb1c8231ac7cd6db7b4e4be8");
		list.add("Audrey : 9f56d9b292b55162ca3999b56f054b407a2ae67e");
		list.add("Eric : 6d0e5951f2a9d928c1d17b25d57f0461296048e6");
		list.add("Karen : 961c4ce5692d8f0f612b4cd6d8665865aac3ace4");
		list.add("Kensuke : 2fa6905f03507615399918510f07657b129890fb");
		Tree tree = new Tree(list);
	}
}
