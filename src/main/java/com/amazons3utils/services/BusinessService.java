package com.amazons3utils.services;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazons3utils.entities.s3Object;
import com.amazons3utils.interfaces.services.IAWSService;
import com.amazons3utils.interfaces.services.IBusinessService;
import com.amazons3utils.interfaces.services.IFileService;

@Service
public class BusinessService implements IBusinessService {

	@Autowired
	private IAWSService awsService;
	
	@Autowired
	private IFileService fileService;
	
	public BusinessService ( IAWSService awsService, IFileService fileService ) {
		this.awsService = awsService;
		this.fileService = fileService;
		
		
		};
		
		
		
		
	
		
	public String getGlosaFile(String base64File) {
		try {
			BufferedReader fileBuffer = getFileBuffer(base64File);
			String[] cipPatients = readBufferFile(fileBuffer);
			List<s3Object> patients = awsService.getPatientList(cipPatients);
			String pathTempFile = fileService.createFinalFile(patients);
			
			return fileService.encoder(pathTempFile);
			
		} catch (Exception ex) {
			System.err.format("Exception: %s%n", ex);
		}
		
		return "Erro ao obter Dados dos pacientes";
	}

	private BufferedReader getFileBuffer(String base64File) throws FileNotFoundException {
		byte[] decodedBytes = Base64.getDecoder().decode(base64File);
		InputStream is = new ByteArrayInputStream(decodedBytes);
		BufferedReader fileBuffer = new BufferedReader(new InputStreamReader(is));
		return fileBuffer;
	}

	private String[] readBufferFile(BufferedReader fileBuffer) {
		String[] result = null;
		
		try {
			String line = fileBuffer.readLine();
			StringBuilder sb = new StringBuilder();
			
			while(line != null){ 
				sb.append(line).append(","); 
				line = fileBuffer.readLine();
			}
			
			result = sb.toString().split(",");
			
		} catch (Exception ex) {
			System.err.format("Exception: %s%n", ex);
		}
		 
		return result;
	}
	

}
