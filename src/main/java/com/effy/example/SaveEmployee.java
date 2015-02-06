package com.effy.example;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.zip.GZIPOutputStream;

public class SaveEmployee {
	public static void main(String argv[]) {
		
	}


	public void writeObject(List<Employee> eList) throws IOException {
		FileOutputStream fos = new 
				FileOutputStream("db");
		GZIPOutputStream gz = new GZIPOutputStream(fos);
		ObjectOutputStream oos = new 
				ObjectOutputStream(gz);
		for (Employee employee : eList) {
			oos.writeObject(employee);
		}
		oos.flush();
		oos.close();
		fos.close();
	}
}
