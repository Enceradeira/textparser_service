package uk.co.jennius.textparser;

import java.io.File;


public class DisplayDirectoryAndFile{
	 
	public static void display (String root) {
 
		displayIt(new File(root));
	}
 
	public static void displayIt(File node){
 
		System.err.println(node.getAbsoluteFile());
 
		if(node.isDirectory()){
			String[] subNote = node.list();
			for(String filename : subNote){
				displayIt(new File(node, filename));
			}
		}
 
	}
}