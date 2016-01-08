Effy
====

[TOC]

# About the developer
I am Anil, software development manager at [Serus](http://www.serus.com/), we create SAAS applications for supply chain domain. You can find me on [LinkedIn](http://in.linkedin.com/in/aniliitk). I keep coding day in and day out. During office timings,  I code for my company, and during after hours I work on open source projects. I have around 11 private repositories and 3 public repositories. 
## Why another IO Utility java library
There are couple of really good libraries in this field (Apache IOUtils comes to mind). But off late while doing a lot of development, I have always had to make choice between the right way of doing things and the faster way of doing things. Often due to initial time constraints, the faster way would win. But after some time I would have to revisit and then redo the things the right way. This was the motivation behind creating Effy (for those wondering about the name, Effy is a short form of Efficient). So you could use Effy API methods without worrying about the performance.
## Mission Statement
Creating better technology the open source way. Effy is and will always be open source for usage.

## How to use
For now just download the latest jar [Effy.jar](https://sourceforge.net/projects/effy/files/latest/download?source=files) at the moment, add it to your project in Eclipse/Idea and start coding.

## Release Info
Latest stable release version is [effy1.3.jar](https://sourceforge.net/projects/effy/files/latest/download?source=files)
Latest source code is available at this [repository](https://github.com/anilpank/effy)
Latest javadoc is available here at [Effy JavaDoc](http://anilpank.bitbucket.org/)

## File Compression APIs
### Unzipping a zip file
You can Unzip the file and write all it's contents and files.
```java
ZipUtils zipUtil = new ZipUtils();
zipUtil.unzip("C:/anil/misc/temp/anikl.zip");
```

### Compress file into zip with highest compression.
If the need is to create smallest zip file for a give file, please use the below implementation. This will compress file myFile.xlsx and write it to to the below temp folder. 
```java
ZipUtils zipUtil = new ZipUtils();
zipUtil .zipHighCompression("C:/misc/myFile.xlsx", "C:/anil/misc/temp/");
```

### Compress directory contents to a zip file.
If you want to compress directory contents into a zip file, use below implementation. Please note that this method ignores any sub-directory present in the directory.
In below implementation, all the files present in logs folder are compressed and zip file log.zip is created.
```java
ZipUtils zipUtil = new ZipUtils();
zipUtil .zipAllFilesInDir("C:/misc/logs", "C:/anil/misc/temp/log.zip");
```

### Compress files to GZIP format
There are scenarios when you do not want a zip format generated but are on the look out for GZIP (.gz) file generation. 
The below implementation will take the file myFile.xlsx, compress it into gz format and write it in temp folder.
```java
ZipUtils zipUtil = new ZipUtils();
zipUtil.compressToGZip("C:/misc/myFile.xlsx", "C:/anil/misc/temp/");
```

### Compress file to XZ format
XZ compression is based on LZMA2 algorithm. From trying out various formats, we found that XZ format gives maximum compression.
So if you want maximum compression (and not bothered about format), use this method.
```java
ZipUtils zipUtil = new ZipUtils();
zipUtil.compressToXZ("C:/anil/misc/temp/myFile.xlsx", "C:/anil/misc/temp/myFile.xlsx.xz");
```

### Archive multiple files into tar and then gZIP
Archives list of inputFiles into a tar and then gzips it into tar.gz
```java
ZipUtils zipUtil = new ZipUtils();
File directory = new File("C:/anil/misc/temp/");
File []files = directory.listFiles();
List<File>fileList = Arrays.asList(files);
zipUtil.createTarAndThenGZip(fileList, "C:/logs/" + fileList.get(0).getName() + ".tar.gz");		
```

### Unzip the zipped file
This will unzip a zipped(compressed) file and write all it's contents.
```java
ZipUtils zipUtil = new ZipUtils();
zipUtil.unzip("C:/temp/anikl.zip");
```


## File IO APIs
### TODO

## Freemarker APIs
### Processing a freemarker String
If you have a string input with freemarker syntax and you want to process it, please see the below example for implementation
```java
HashMap root = new HashMap();
HashMap employeeMap = new HashMap();
employeeMap.put("name", "John Mayer");
employeeMap.put("id", 23);
employeeMap.put("manager", "Scott Johnson");
employeeMap.put("designation", "Director of Engineering");
employeeMap.put("yearsOfExp", 15);		
root.put("employee",employeeMap);
String template = "Hi Mr. ${employee.name}, your id is ${employee.id} and your current manager is ${employee.manager}";		
String output = FTLTemplateEngine.INSTANCE.process(template, root);
```
This would give the following output.
<blockquote>
Hi Mr. John Mayer, your id is 23 and your current manager is Scott Johnson
</blockquote>

## Further Reading
We maintain the javadoc actively 

### LICENSE ###

* Distributed under the MIT License. (See accompanying file LICENSE or copy at http://opensource.org/licenses/MIT )

# [Bespoke Solutions](http://www.ojblabs.com)
Brought to you by [OjbLabs](http://www.ojblabs.com) 

