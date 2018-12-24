package com.ccrt.test.dateparsers;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;





import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

public class ExtractTarDemo {

	public static void main(String[] args) throws Exception {
		File file = new File("D:\\verizon\\10.119.8.30.tar.gz");
		String currentHost = null;
		System.out.println("File Name ::"+file.getName());
		if (file.getName().endsWith(".gz")) {
			currentHost = file.getName().split(".tar.gz")[0];
		}
		System.out.println("currentHost ::"+currentHost);
		FileInputStream fileInputStream = new FileInputStream(file);
		extractGZip(fileInputStream);
	}
	
	public static void extractTarGZ(InputStream in) throws IOException {
	    GzipCompressorInputStream gzipIn = new GzipCompressorInputStream(in);
	    try (TarArchiveInputStream tarIn = new TarArchiveInputStream(gzipIn)) {
	        TarArchiveEntry entry;

	        while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {
	        	System.out.println("entered inside while");
	            /** If the entry is a directory, create the directory. **/
	            if (entry.isDirectory()) {
	                File f = new File(entry.getName());
	                boolean created = f.mkdir();
	                if (!created) {
	                    System.out.printf("Unable to create directory '%s', during extraction of archive contents.\n",
	                            f.getAbsolutePath());
	                }
	            } else {
	                int count;
	                byte data[] = new byte[1024];
	                FileOutputStream fos = new FileOutputStream(entry.getName(), false);
	                try (BufferedOutputStream dest = new BufferedOutputStream(fos, 1024)) {
	                    while ((count = tarIn.read(data, 0, 1024)) != -1) {
	                        dest.write(data, 0, count);
	                    }
	                }
	            }
	        }

	        System.out.println("Untar completed successfully!");
	    }
	}

	private static void extractGZip(InputStream is) throws Exception {
		TarArchiveInputStream tais = new TarArchiveInputStream(new GZIPInputStream(is));
		TarArchiveEntry nextEntry = tais.getNextTarEntry();
		while(nextEntry != null) {
			if (nextEntry.getName().equals("cfstats")) {
				extractFileContents(tais);
			}
			nextEntry = tais.getNextTarEntry();
		}
	}
	
	private static void extractFileContents(InputStream inputStream) throws IOException, Exception{

		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		List<String> collector = br.lines().collect(Collectors.toList());
		
		for(String line : collector){
			System.out.println(line);
		}
	}
}
