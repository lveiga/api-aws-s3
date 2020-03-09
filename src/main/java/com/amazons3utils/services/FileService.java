package com.amazons3utils.services;

import com.amazons3utils.entities.s3Object;
import com.amazons3utils.interfaces.services.IFileService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;



@Service
public class FileService implements IFileService {
    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";

    private static final String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;
	
    private static SecureRandom random = new SecureRandom();
	
	public String createFinalFile(Collection<s3Object> patients) {

		try {
			
			String property = "java.io.tmpdir";
		    String tempDir = System.getProperty(property);

		    File dir = new File(tempDir);
		    File filename = File.createTempFile(generateRandomString(5), ".csv", dir);
		    FileWriter writer = new FileWriter(filename, true);
		    
		    String collect = writePatient(patients);
		    System.out.println(collect);

		    writer.write(collect);
		    writer.close();
		    
		    return filename.getAbsolutePath();

		} catch (IOException e) {
			return e.getMessage();
		}  
	}
	
	public String encoder(String filePath) {
        String base64File = "";
        File file = new File(filePath);
        try (FileInputStream imageInFile = new FileInputStream(file)) {
            byte fileData[] = new byte[(int) file.length()];
            imageInFile.read(fileData);
            base64File = Base64.getEncoder().encodeToString(fileData);
        } catch (FileNotFoundException e) {
            System.out.println("File not found" + e);
        } catch (IOException ioe) {
            System.out.println("Exception while reading the file " + ioe);
        }
        return base64File;
    }
	
	private String writePatient(Collection<s3Object> patients) throws FileNotFoundException {

	        try {
	        	PrintWriter writer = new PrintWriter("test");
	            ColumnPositionMappingStrategy<s3Object> mapStrategy
	                    = new ColumnPositionMappingStrategy<>();

	            mapStrategy.setType(s3Object.class);

	            String[] columns = new String[]{"cip", "url"};
	            mapStrategy.setColumnMapping(columns);

	            StatefulBeanToCsv<s3Object> btcsv = new StatefulBeanToCsvBuilder<s3Object>(writer)
	                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
	                    .withMappingStrategy(mapStrategy)
	                    .withSeparator(',')
	                    .build();

	            btcsv.write((ArrayList<s3Object>)patients);
	            
	            String result = btcsv.toString();
	            
	            return result;

	        } catch (CsvException ex) {

	            //LOGGER.error("Error mapping Bean to CSV", ex);
	        	return null;
	        }
	    }
	 
    public static String generateRandomString(int length) {
        if (length < 1) throw new IllegalArgumentException();

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {

			// 0-62 (exclusive), random returns 0-61
            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);

            // debug
            System.out.format("%d\t:\t%c%n", rndCharAt, rndChar);

            sb.append(rndChar);

        }

        return sb.toString();

    }
}
