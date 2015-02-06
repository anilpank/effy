package com.effy.main;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import static java.nio.file.FileVisitResult.*;


public class PrintFiles extends SimpleFileVisitor<Path> {

    public static void main(String[] args) throws IOException {
    	Path startingDir = Paths.get("C:\\Apps\\ext-5.0.1-commercial\\ext-5.0.1");
    	PrintFiles pf = new PrintFiles();
    	Files.walkFileTree(startingDir, pf);
    }

	/**
	 * Print information about
	 */
	@Override
	public FileVisitResult visitFile(Path file,
			BasicFileAttributes attr) {
		if (attr.isSymbolicLink()) {
			System.out.format("Symbolic link: %s ", file);
		} else if (attr.isRegularFile()) {
			System.out.format("Regular file: %s ", file);
		} else {
			System.out.format("Other: %s ", file);
		}
		System.out.println("(" + attr.size() + "bytes)");
		return CONTINUE;
	}

	// Print each directory visited.
	@Override
	public FileVisitResult postVisitDirectory(Path dir,
			IOException exc) {
		System.out.format("Directory: %s%n", dir);
		return CONTINUE;
	}

	// If there is some error accessing
	// the file, let the user know.
	// If you don't override this method
	// and an error occurs, an IOException 
	// is thrown.
	@Override
	public FileVisitResult visitFileFailed(Path file,
			IOException exc) {
		System.err.println(exc);
		return CONTINUE;
	}
}
