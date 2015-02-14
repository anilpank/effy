package com.effy.main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.tukaani.xz.LZMA2Options;
import org.tukaani.xz.XZOutputStream;

import com.effy.ftl.FTLTemplateEngine;

import freemarker.template.TemplateException;


/**
 * ZIP API for compressing and extracting files
 * @author averma
 *
 */
public class ZipUtils {
	static final int BUFFER = 2048;

	public static void main(String[] args) throws DataFormatException, IOException, TemplateException {
		System.out.println("Anil");
		ZipUtils zu = new ZipUtils();
		//zu.compressAndDecompressViaZLib();
		//zu.unzip("C:/anil/misc/temp/anikl.zip");
		/*
		zu.zip("C:/anil/misc/temp/www/gemh101.pdf", "C:/anil/misc/temp/somewierd.zip");
		zu.compressTo7Z("C:/anil/misc/temp/www/atmel.log", "C:/anil/misc/temp/atmelCom.7z");
		zu.compressTo7Zip("C:/anil/misc/temp/www/atmel.log", "C:/anil/misc/temp/thisOne.7z");


		zu.zip("C:/anil/misc/temp/LotTxWkView_1_20150207-074205-1.xlsx", "C:/anil/misc/temp/normalCompression.zip");
		zu.zipHighCompression("C:/anil/misc/temp/LotTxWkView_1_20150207-074205-1.xlsx", "C:/anil/misc/temp/highCompression.zip");
		zu.compressToGZip("C:/anil/misc/temp/LotTxWkView_1_20150207-074205-1.xlsx", "C:/anil/misc/temp/");		
		zu.compressToXZ("C:/anil/misc/temp/LotTxWkView_1_20150207-074205-1.xlsx", "C:/anil/misc/temp/LotTxWkView_1_20150207-074205-1.xlsx.xz");
		 
		zu.createTarAndThenGZip(new File("C:/anil/misc/temp/LotTxWkView.xlsx"), "C:/anil/misc/temp/wierd.tar.gz");
		
		File file = new File("C:/anil/misc/temp/");
		if (file.isDirectory()) {
			File []files = file.listFiles();
			List<File>fileList = Arrays.asList(files);
			zu.createTarAndThenGZip(fileList, "C:/logs/" + fileList.get(0).getName() + ".tar.gz");
		}
		*/
		File jarFile = new File("C:/anil/code/working/Effy/target/Effy.jar");
		File xzFile = new File("C:/anil/misc/temp/Effy.tar.xz");
		zu.compressToXZ(jarFile, xzFile);
	}



	/**
	 * Unzip the file and write all it's contents and files
	 * @param fileName The zip file name with complete path
	 * @throws IOException
	 */
	public void unzip(String fileName) throws IOException {
		BufferedOutputStream dest = null;
		BufferedInputStream is = null;
		try {

			ZipEntry entry;
			ZipFile zipfile = new ZipFile(fileName);
			Enumeration<? extends ZipEntry> e = zipfile.entries();
			while(e.hasMoreElements()) {
				entry = (ZipEntry) e.nextElement();
				System.out.println("Extracting: " +entry);
				is = new BufferedInputStream
						(zipfile.getInputStream(entry));
				int count;
				byte data[] = new byte[BUFFER];
				FileOutputStream fos = new 
						FileOutputStream(entry.getName());
				dest = new 
						BufferedOutputStream(fos, BUFFER);
				while ((count = is.read(data, 0, BUFFER)) 
						!= -1) {
					dest.write(data, 0, count);
				}				
			}
		} 

		finally {
			if (dest != null) {
				dest.flush();
				dest.close();
			}
			if (is != null) {
				is.close();
			}			
		}
	}

	/**
	 * Compress the file with highest compression ratio ever possible
	 * @param fileName
	 * @param outputZipFile
	 * @throws IOException
	 */
	public void zipHighCompression(String fileName, String outputZipFile) throws IOException {
		BufferedInputStream origin = null;
		FileOutputStream dest = new 
				FileOutputStream(outputZipFile);
		ZipOutputStream out = new ZipOutputStream(new 
				BufferedOutputStream(dest));
		out.setLevel(Deflater.BEST_COMPRESSION);
		byte data[] = new byte[BUFFER];
		// get a list of files from current directory
		File f = new File(fileName);
		if (f.isFile()) {
			FileInputStream fi = new 
					FileInputStream(f);
			origin = new 
					BufferedInputStream(fi, BUFFER);
			ZipEntry entry = new ZipEntry(f.getName());
			out.putNextEntry(entry);
			int count;
			while((count = origin.read(data, 0, 
					BUFFER)) != -1) {
				out.write(data, 0, count);
			}
			origin.close();
		}

		else {
			File[] actualFiles = f.listFiles();
			for (File file : actualFiles) {
				if (file.isDirectory()) {
					continue;
				}
				FileInputStream fi = new 
						FileInputStream(file);
				origin = new 
						BufferedInputStream(fi, BUFFER);
				ZipEntry entry = new ZipEntry(file.getName());
				out.putNextEntry(entry);
				int count;
				while((count = origin.read(data, 0, 
						BUFFER)) != -1) {
					out.write(data, 0, count);
				}
				origin.close();
			}
		}

		out.close();
	}



	/**
	 * Compresses contents of directory into a zip file
	 * Ignores any sub-directories
	 * @param directory Full name of the directory including the path
	 * @param outputZipFile Full name of zip file including the path
	 * @throws IOException
	 */
	public void zipAllFilesInDir(String directory, String outputZipFile) throws IOException {
		BufferedInputStream origin = null;
		FileOutputStream dest = new 
				FileOutputStream(outputZipFile);
		ZipOutputStream out = new ZipOutputStream(new 
				BufferedOutputStream(dest));		
		byte data[] = new byte[BUFFER];
		// get a list of files from current directory
		File f = new File(directory);
		if (f.isFile()) {
			FileInputStream fi = new 
					FileInputStream(f);
			origin = new 
					BufferedInputStream(fi, BUFFER);
			ZipEntry entry = new ZipEntry(f.getName());
			out.putNextEntry(entry);
			int count;
			while((count = origin.read(data, 0, 
					BUFFER)) != -1) {
				out.write(data, 0, count);
			}
			origin.close();
		}

		else {
			File[] actualFiles = f.listFiles();
			for (File file : actualFiles) {
				if (file.isDirectory()) {
					continue;
				}
				FileInputStream fi = new 
						FileInputStream(file);
				origin = new 
						BufferedInputStream(fi, BUFFER);
				ZipEntry entry = new ZipEntry(file.getName());
				out.putNextEntry(entry);
				int count;
				while((count = origin.read(data, 0, 
						BUFFER)) != -1) {
					out.write(data, 0, count);
				}
				origin.close();
			}
		}

		out.close();
	}



	private void compressTo7Zip(String inputFile, String output7zFile) throws IOException {
		File outputFile = new File(output7zFile);
		SevenZOutputFile sevenZOutput = new SevenZOutputFile(outputFile);		
		File inFile = new File(inputFile);
		SevenZArchiveEntry entry = sevenZOutput.createArchiveEntry(inFile, inFile.getName());
		sevenZOutput.putArchiveEntry(entry);		
		sevenZOutput.write(Files.readAllBytes(Paths.get(inputFile)));
		sevenZOutput.closeArchiveEntry();
	}

	/**
	 * Compress input file in GZIP (gz) format
	 * @param inputFile Input file name to be compressed with full path
	 * @param outputDir Output directory where the compressed file will be written
	 */
	public void compressToGZip(String inputFile, String outputDir) {
		byte[] buffer = new byte[1024];

		try {

			GZIPOutputStream gzos = 
					new GZIPOutputStream(new FileOutputStream( outputDir +
							FileSystems.getDefault().getSeparator() +
							Paths.get(inputFile).getFileName() +".gz"));
			FileInputStream in = 
					new FileInputStream(inputFile);
			int len;
			while ((len = in.read(buffer)) > 0) {
				gzos.write(buffer, 0, len);
			}
			in.close();
			gzos.finish();
			gzos.close();

		}catch(IOException ex){
			ex.printStackTrace();   
		}
	}	

	/**
	 * Creates XZ compressed file
	 * @param inputFile The input file to be zipped with complete path
	 * @param outputFile The output file generated (preferably with name .xz)
	 * @throws IOException 
	 */
	public static void compressToXZ(String inputFile, String outputFile) throws IOException {
		FileInputStream inFile = new FileInputStream(inputFile);
		FileOutputStream outfile = new FileOutputStream(outputFile);
		LZMA2Options options = new LZMA2Options();
		options.setPreset(9); // play with this number: 6 is default but 7 works better for mid sized archives ( > 8mb)
		XZOutputStream out = new XZOutputStream(outfile, options);

		byte[] buf = new byte[8192];
		int size;
		while ((size = inFile.read(buf)) != -1)
			out.write(buf, 0, size);

		out.finish();
	}

	/**
	 * Compress the file into XZ format. Compression is based on LZMA2 algorithm. 
	 * Use this method when you need highest compression ratio.
	 * @param inputFile
	 * @param outputZippedFile
	 * @throws IOException 
	 */
	public static void compressToXZ(File inputFile, File outputZippedFile) throws IOException {
		FileInputStream inFile = new FileInputStream(inputFile);
		FileOutputStream outfile = new FileOutputStream(outputZippedFile);
		LZMA2Options options = new LZMA2Options();
		options.setPreset(9); // play with this number: 6 is default but 7 works better for mid sized archives ( > 8mb)
		XZOutputStream out = new XZOutputStream(outfile, options);
		byte[] buf = new byte[8192];
		int size;
		while ((size = inFile.read(buf)) != -1)
			out.write(buf, 0, size);

		out.finish();
	}

	/**
	 * Create a tar file and then gzip it
	 * @param inputFile The input file to be tarred and then gzipped
	 * @param outputFile Output file generated with complete path (format should be tar.gz)
	 * @throws IOException
	 */
	public void createTarAndThenGZip(File inputFile, String outputFile) throws IOException {
		TarArchiveOutputStream out = null;
		try {
			out = new TarArchiveOutputStream(
					new GZIPOutputStream(
							new BufferedOutputStream(new FileOutputStream(outputFile))));		     
			// Add data to out and flush stream
			TarArchiveEntry entry = new TarArchiveEntry(inputFile, inputFile.getName());
			out.putArchiveEntry(entry);
			IOUtils.copy(new FileInputStream(inputFile), out);
			out.closeArchiveEntry();

		} finally {
			if(out != null) out.close();
		}
	}

	/**
	 * Archives list of inputFiles into a tar and then gzips it into tar.gz
	 * @param inputFiles List of input Files to be archived
	 * @param outputFile Output file to be generated (full path with .tar.gz extension)
	 * @throws IOException
	 */
	public void createTarAndThenGZip(List<File>inputFiles, String outputFile) throws IOException {		
		TarArchiveOutputStream out = null;
		try {
			out = new TarArchiveOutputStream(
					new GZIPOutputStream(
							new BufferedOutputStream(new FileOutputStream(outputFile))));			
			for (File inputFile : inputFiles) {
				if (inputFile.isFile()) {
					TarArchiveEntry entry = new TarArchiveEntry(inputFile, inputFile.getName());
					out.putArchiveEntry(entry);
					IOUtils.copy(new FileInputStream(inputFile), out);
					out.closeArchiveEntry();
				}
				
			}			
			

		}
		finally {
			if(out != null) out.close();
		}
	}

}
