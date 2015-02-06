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
import java.util.Enumeration;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;

import SevenZip.Compression.LZMA.Encoder;


/**
 * ZIP API for compressing and extracting files
 * @author averma
 *
 */
public class ZipUtils {
	static final int BUFFER = 2048;

	public static void main(String[] args) throws DataFormatException, IOException {
		System.out.println("Anil");
		ZipUtils zu = new ZipUtils();
		//zu.compressAndDecompressViaZLib();
		//zu.unzip("C:/anil/misc/temp/anikl.zip");
		/*
		zu.zip("C:/anil/misc/temp/www/gemh101.pdf", "C:/anil/misc/temp/somewierd.zip");
		zu.compressTo7Z("C:/anil/misc/temp/www/atmel.log", "C:/anil/misc/temp/atmelCom.7z");
		zu.compressTo7Zip("C:/anil/misc/temp/www/atmel.log", "C:/anil/misc/temp/thisOne.7z");
		*/
		zu.compressToGZip("C:/anil/misc/temp/www/images.html", "C:/anil/misc/temp/");
	}

	private void compressAndDecompressViaZLib() throws UnsupportedEncodingException, DataFormatException {
		String inputString = "blahblahblah";
		byte[] input = inputString.getBytes("UTF-8");

		// Compress the bytes
		byte[] output = new byte[100];
		Deflater compresser = new Deflater();
		compresser.setInput(input);
		compresser.finish();
		int compressedDataLength = compresser.deflate(output);
		compresser.end();

		// Decompress the bytes
		Inflater decompresser = new Inflater();
		decompresser.setInput(output, 0, compressedDataLength);
		byte[] result = new byte[100];
		int resultLength = decompresser.inflate(result);
		decompresser.end();
		// Decode the bytes into a String
		String outputString = new String(result, 0, resultLength, "UTF-8");
		System.out.println(outputString);	     
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
	 * Compresses contents of directory into a zip file
	 * Ignores any sub-directories
	 * @param fileName Full name of the directory including the path
	 * @param outputZipFile Full name of zip file including the path
	 * @throws IOException
	 */
	public void zip(String fileName, String outputZipFile) throws IOException {
		BufferedInputStream origin = null;
		FileOutputStream dest = new 
				FileOutputStream(outputZipFile);
		ZipOutputStream out = new ZipOutputStream(new 
				BufferedOutputStream(dest));			
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


	private void compressTo7Z(String inputFile, String outputDir) throws IOException {
		/* Read the input file to be compressed */
		File inputToCompress = new File(inputFile);
		
		BufferedInputStream inStream  = new BufferedInputStream(new java.io.FileInputStream(inputToCompress));
		/* Create output file 7z File */
		File compressedOutput = new File(outputDir +  FileSystems.getDefault().getSeparator() + 
				inputToCompress.getName());
		BufferedOutputStream outStream = new BufferedOutputStream(new java.io.FileOutputStream(compressedOutput));
		/* Create LZMA Encoder Object / Write Header Information */
		Encoder encoder = new Encoder();
		encoder.SetAlgorithm(2);
		encoder.SetDictionarySize(8388608);
		encoder.SetNumFastBytes(128);
		encoder.SetMatchFinder(1);
		encoder.SetLcLpPb(3,0,2);
		encoder.SetEndMarkerMode(false);
		encoder.WriteCoderProperties(outStream);
		long fileSize;
		fileSize = inputToCompress.length();
		for (int i = 0; i < 8; i++)                                     
		{
			outStream.write((int)(fileSize >>> (8 * i)) & 0xFF);
		}                               
		/* Write Compressed Data to File */     
		encoder.Code(inStream, outStream, -1, -1, null);
		/* Close Output Streams*/
		outStream.flush();
		outStream.close();
		inStream.close();
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


}
