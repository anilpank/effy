package com.effy.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import com.dropbox.core.DbxWriteMode;

/**
 * Dropbox utility methods for usage
 * @author averma
 *
 */
public class DropBoxUtils {

	private final String appKey;
	private final String appSecret;
	private final String accessToken;
	private final DbxClient client;
	private final DbxRequestConfig config;


	public String getAccessToken() {
		return accessToken;
	}

	public DbxClient getClient() {
		return client;
	}

	public DbxRequestConfig getConfig() {
		return config;
	}

	public String getAppKey() {
		return appKey;
	}

	public String getAppSecret() {
		return appSecret;
	}



	public DropBoxUtils(String appKey, String appSecret, String accessToken) {
		this.appKey = appKey;
		this.appSecret = appSecret;
		this.accessToken = accessToken;
		this.config = new DbxRequestConfig(
				"Effy/1.3", Locale.getDefault().toString());
		this.client = new DbxClient(config, accessToken);
	}	

	public static void main(String[] args) throws DbxException, IOException {
		DropBoxUtils boxUtils = new DropBoxUtils("l0npcfhzcb8d2ox", "nr57yswe4uwcr1k","pHhgSzQHChAAAAAAAAAGytZLKKMNlqafEpYtiY9UuY5VLkERm6zbO4ecHWpIfRWF" );		
		System.out.println("Linked account: " + boxUtils.getClient().getAccountInfo().displayName);	
		/*
		File file = new File("C:/Users/averma/Downloads/Elasticsearch and web front end.pdf");
		boxUtils.uploadFile(file);
		boxUtils.uploadFileToFolder(file, "/internet");
		 */
		System.out.println(boxUtils.listFolders("office").get(0));
		boxUtils.downloadFile("/id-docs/anil_pan.JPG", "anil_pan.JPG", "C:/anil/misc/temp");
	}

	/**
	 * Upload file to a dropbox account. File is uploaded to base folder
	 * @param file - file to be uploaded
	 * @throws DbxException
	 * @throws IOException
	 */
	public void uploadFile(File file) throws DbxException, IOException {
		FileInputStream inputStream = new FileInputStream(file);
		try {
			DbxEntry.File uploadedFile = client.uploadFile("/" + file.getName(),
					DbxWriteMode.add(), file.length(), inputStream);		    
		} finally {
			inputStream.close();
		}
	}

	/**
	 * Upload file to given folder
	 * @param file - File to be uploaded
	 * @param folder - Dropbox folder where file has to be uploaded, for example /books
	 * @throws IOException 
	 * @throws DbxException 
	 */
	public void uploadFileToFolder(File file, String folder) throws DbxException, IOException {
		FileInputStream inputStream = new FileInputStream(file);
		if (!folder.startsWith("/")) {
			folder = "/" + folder;
		}		
		if(!folder.endsWith("/")) {
			folder = folder + "/";
		}
		try {
			DbxEntry.File uploadedFile = client.uploadFile(folder + file.getName(),
					DbxWriteMode.add(), file.length(), inputStream);		    
		} finally {
			inputStream.close();
		}
	}

	/**
	 * List all folders in base folder
	 * @return list of all folders/directories
	 * @throws DbxException
	 */
	public List<String> listFolders() throws DbxException {
		List<String> folders = new ArrayList<String>();
		DbxEntry.WithChildren listing = client.getMetadataWithChildren("/");		
		for (DbxEntry child : listing.children) {
			if (child.isFolder()) {				
				folders.add(child.path);
			}		    
		}
		return folders;
	}

	/**
	 * List all folders in the given directory
	 * @param directory - Directory for which all folders to be listed
	 * @return - List of folders
	 * @throws DbxException
	 */
	public List<String> listFolders(String directory) throws DbxException {
		List<String> folders = new ArrayList<String>();
		directory = getDropBoxFolder(directory);
		DbxEntry.WithChildren listing = client.getMetadataWithChildren(directory);
		System.out.println("Files in the root path:");
		for (DbxEntry child : listing.children) {
			if (child.isFolder()) {				
				folders.add(child.path);
			}		    
		}
		return folders;
	}

	/**
	 * Returns list of all files in given directory
	 * @param directory - Directory for which list of files is required
	 * @return - list of all files in given directory
	 * @throws DbxException
	 */
	public List<String> listFiles(String directory) throws DbxException {
		List<String> files = new ArrayList<String>();
		directory = getDropBoxFolder(directory);
		DbxEntry.WithChildren listing = client.getMetadataWithChildren(directory);
		System.out.println("Files in the root path:");
		for (DbxEntry child : listing.children) {
			if (child.isFile()) {				
				files.add(child.path);
			}		    
		}
		return files;
	}

	/**
	 * List all files and folders in a given directory
	 * @param directory - Directory for which List of files is required
	 * @return - list of all files and directories
	 * @throws DbxException
	 */
	public List<String> listFilesAndFolders(String directory) throws DbxException {
		List<String> files = new ArrayList<String>();
		directory = getDropBoxFolder(directory);
		DbxEntry.WithChildren listing = client.getMetadataWithChildren(directory);
		System.out.println("Files in the root path:");
		for (DbxEntry child : listing.children) {			
			files.add(child.path);					    
		}
		return files;		
	}

	/**
	 * Download file from dropbox
	 * @param filePath - Complete path of file to be downloaded, example /id-docs/anil_pan.JPG
	 * @param fileName - Name of the file to be downloaded, example anil_pan.JPG
	 * @param destinationFolder - Destination folder where the file would be written example C:/anil/misc/temp
	 * @throws DbxException
	 * @throws IOException
	 */
	public void downloadFile(String filePath,String fileName, String destinationFolder) throws DbxException, IOException {
		File destinationDir = new File(destinationFolder);
		File targetfile = new File(destinationDir, fileName);		
		FileOutputStream outputStream = new FileOutputStream(targetfile);
		try {
			DbxEntry.File downloadedFile = client.getFile(filePath, null,
					outputStream);			
		} finally {
			outputStream.close();
		}
	}

	private String getDropBoxFolder(String folder) {
		if (!folder.startsWith("/")) {
			folder = "/" + folder;
		}		
		return folder;
	}


}
