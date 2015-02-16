package org.effy.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileTime;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.Checksum;
/**
 * This is utility class where there are lot of file utility methods
 * @author averma
 *
 */
public class InOutUtils {

	public static void main(String[] args) throws IOException {
		InOutUtils cb = new InOutUtils();		
		//cb.nioCopy("C:/anil/misc/temp/serus_app.log.1", "C:/anil/misc/temp/nio.log");
		//cb.nioMove("C:/anil/misc/temp/serus_app.log.1", "C:/anil/deploy/sencha/sameName.log");
		/*
		cb.getAllRootDirs();
		
		cb.deleteIfExisting("C:/anil/misc/temp/small.log");
		System.out.println(cb.size("C:/anil/misc/temp/sameName.log"));
		System.out.println(cb.sizeInKB("C:/anil/misc/temp/sameName.log"));
		//System.out.println(cb.getLastModifiedTime("C:/anil/misc/temp/sameName.log"));
		//cb.createFile("C:/anil/misc/temp/ghi.txt");
		//System.out.println(cb.createTempFile().getFileName());
		//cb.createTempFile();
		//cb.createDir("C:/anil/misc/temp/myDir");
		 * */
		 
		System.out.println(cb.getPathStringSeparator());
		System.out.println(cb.readFileToString("C:/anil/misc/temp/runthis.sql"));
	}

	/**
	 * Copies file by reading byte by byte. Very slow
	 * @param inputFile input file name with complete path.
	 * @param outputFile output file name with complete path. The file may not even exist 
	 * @throws IOException
	 */
	public void copyViaBytes(String inputFile, String outputFile) throws IOException {
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(inputFile);
			out = new FileOutputStream(outputFile);

			int c;
			while ((c = in.read()) != -1) {
				out.write(c);
			}
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}


	/**
	 * Copy files character by character. Very slow
	 * @param inputFile input file name with complete path.
	 * @param outputFile output file name with complete path. The file may not even exist 
	 * @throws IOException
	 */
	public void copyByCharacters(String inputFile, String outputFile) throws IOException {
		FileReader inputStream = null;
		FileWriter outputStream = null;

		try {
			inputStream = new FileReader(inputFile);
			outputStream = new FileWriter(outputFile);

			int c;
			while ((c = inputStream.read()) != -1) {
				outputStream.write(c);
			}
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}

	/**
	 * Copy file line by line
	 * CopyLines outputs each line using println, which appends the line terminator for the current operating system.
	 * This might not be the same line terminator that was used in the input file.
	 * @param inputFile input file name with complete path.
	 * @param outputFile output file name with complete path. The file may not even exist 
	 * @throws IOException
	 */
	public void copyLines(String inputFile, String outputFile) throws IOException {
		BufferedReader inputStream = null;
		PrintWriter outputStream = null;

		try {
			inputStream = new BufferedReader(new FileReader(inputFile));
			outputStream = new PrintWriter(new FileWriter(outputFile));

			String l;
			while ((l = inputStream.readLine()) != null) {
				outputStream.println(l);
			}
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}
	
	/**
	 * Copy files character by character via buffer streams
	 * @param inputFile input file name with complete path.
	 * @param outputFile output file name with complete path. The file may not even exist 
	 * @throws IOException
	 */
	public void copyByCharactersViaBuffer(String inputFile, String outputFile) throws IOException {
		BufferedReader inputStream = null;
		BufferedWriter outputStream = null;

		try {
			inputStream = new BufferedReader(new FileReader(inputFile));
			outputStream = new BufferedWriter(new FileWriter(outputFile));

			int c;
			while ((c = inputStream.read()) != -1) {
				outputStream.write(c);
			}
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}
	
	/**
	 * Does an NIO copy of the file. Preferably use this method for all your file copies.
	 * @param inputFile
	 * @param outputFile
	 * @throws IOException
	 */
	public void nioCopy(String inputFile, String outputFile) throws IOException {
		Path source = Paths.get(inputFile);
		Path target = Paths.get(outputFile);
		Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
	}
	
	/**
	 * Moves a file to the target file. Preferably use this method for all your file moves
	 * @param sourceFile Source file name that has to be moved.
	 * @param targetFile Target file name with full path where to is moving
	 * @throws IOException
	 */
	public void nioMove(String sourceFile, String targetFile) throws IOException {
		Path source = Paths.get(sourceFile);
		Path target = Paths.get(targetFile);
		Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
	}	
		
	/**
	 * Use this method for copying small files. Ideally smaller than 50  MB
	 * @param inputFile
	 * @param outputFile
	 * @throws IOException
	 */
	public void copySmallFiles(String inputFile, String outputFile) throws IOException {
		Path inputPath = Paths.get(inputFile);
		byte[] fileArray;
		fileArray = Files.readAllBytes(inputPath);		
		Path outputPath = Paths.get(outputFile);
		Files.write(outputPath, fileArray, StandardOpenOption.CREATE);				
	}
	
	/**
	 * Get all root directories. Get all root directories for the system 
	 * @return returns a list of root directories
	 */
	public List<String> getAllRootDirs() {
		List<String>rootDirs = new ArrayList<String>();
		Iterable<Path> dirs = FileSystems.getDefault().getRootDirectories();
		for (Path name: dirs) {
			rootDirs.add(name.toString());
		}
		return rootDirs;
	}
	
	/**
	 * Delete a file from system.
	 * @param fileName The file name to be deleted with complete path
	 * @throws IOException
	 */
	public void deleteFile(String fileName) throws IOException {
		Path path = Paths.get(fileName);
		Files.delete(path);	
	}
	
	/**
	 * Deletes the file if it exists. No exception thrown in file does not exist
	 * @param fileName The file name to be deleted with complete path
	 * @throws IOException
	 */
	public void deleteIfExisting(String fileName) throws IOException {
		Path path = Paths.get(fileName);
		Files.deleteIfExists(path);
	}
	
	/**
	 * Get File size in bytes
	 * @param fileName The file name with full path
	 * @return file size in bytes
	 * @throws IOException
	 */
	public long size(String fileName) throws IOException {
		Path path = Paths.get(fileName);
		return Files.size(path);
	}
	
	/**
	 * Get file size in KiloBytes with double precision
	 * @param fileName The file name with full path
	 * @return file size in KiloBytes
	 * @throws IOException
	 */
	public double sizeInKB(String fileName) throws IOException {
		Path path = Paths.get(fileName);
		long bytes = Files.size(path);
		return bytes/1024.0;
	}
	
	/**
	 * Get file size in MegaBytes with double precision
	 * @param fileName The file name with full path
	 * @return file size in MegaBytes
	 * @throws IOException
	 */
	public double sizeInMB(String fileName) throws IOException {
		Path path = Paths.get(fileName);
		long bytes = Files.size(path);
		return bytes/(1024.0*1024.0);
	}
	
	/**
	 * Get file size in GB with double precision
	 * @param fileName The file name with full path.
	 * @return file size in GigaBytes
	 * @throws IOException
	 */
	public double sizeInGB(String fileName) throws IOException {
		Path path = Paths.get(fileName);
		long bytes = Files.size(path);
		return bytes/(1024.0*1024.0*1024.0);
	}
	
	/**
	 * Check if the file is a directory
	 * @param fileName The file name with full path
	 * @return true if the file is a directory (folder in windows)
	 */
	public boolean isDirectory(String fileName) {
		Path path = Paths.get(fileName);
		return Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS);
	}
	
	/**
	 * Check if the file is a symbolic link.
	 * @param fileName The file name with full path
	 * @return true if the file is a symbolic link
	 */
	public boolean isSymbolicLink(String fileName) {
		Path path = Paths.get(fileName);
		return Files.isSymbolicLink(path);
	}
	
	/**
	 * Get the last modified time as timestamp
	 * @param fileName File name with full file path
	 * @return last modified time as timestamp
	 * @throws IOException
	 */
	public Timestamp getLastModifiedTime(String fileName) throws IOException {
		Path path = Paths.get(fileName);
		FileTime fileTime = Files.getLastModifiedTime(path, LinkOption.NOFOLLOW_LINKS);
		Timestamp timestamp = new Timestamp(fileTime.toMillis()) ;
		return timestamp;
	}
	
	/**
	 * Create a file
	 * @param fileName File name with full file path
	 * @throws IOException
	 */
	public void createFile (String fileName) throws IOException {
		Path path = Paths.get(fileName);
		Files.createFile(path);
	}
	
	/**
	 * Create a temporary file.
	 * File created is very platform specific
	 * @throws IOException
	 */
	public Path createTempFile() throws IOException {
		Path tempFile = Files.createTempFile(null, ".myapp");
		System.out.format("The temporary file" +
		        " has been created: %s%n", tempFile);
		return tempFile;
	}
	
	/**
	 * Create a directory
	 * @param dirName directory name with full path
	 * @throws IOException
	 */
	public void createDir(String dirName) throws IOException {
		Path dir = Paths.get(dirName);
		Files.createDirectory(dir);
	}
	
	/**
	 * Get all directory contents
	 * @param dirName
	 * @return List of file names as String
	 */
	public List<String> getDirectoryContents(String dirName) {
		Path dirPath = Paths.get(dirName);
		List<String> contents = new ArrayList<String>();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath)) {
		    for (Path file: stream) {
		    	contents.add(file.getFileName().toString());
		        
		    }
		} catch (IOException | DirectoryIteratorException x) {
		    // IOException can never be thrown by the iteration.
		    // In this snippet, it can only be thrown by newDirectoryStream.
		    System.err.println(x);
		}
		return contents;
	}
	
	/**
	 * Get the path separator for the given operating system
	 * The path separator for POSIX file systems is the forward slash, /, and for Microsoft Windows is the backslash, \. 
	 * Other file systems might use other delimiters.
	 * @return the path separator as String
	 */
	public String getPathStringSeparator() {
		return FileSystems.getDefault().getSeparator();
	}
	
	
	/**
	 * Reads file content to String
	 * @param inputFile Input file name with complete path
	 * @return content of the file as String
	 * @throws IOException
	 */
	public String readFileToString(String inputFile) throws IOException {		
		return new String(Files.readAllBytes(Paths.get(inputFile)));
	}
	
		

}
