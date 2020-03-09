package com.amazons3utils.services;



import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazons3utils.entities.s3Object;
import com.amazons3utils.interfaces.services.IAWSService;

@Service
public class AWSService implements IAWSService{
			
    private static String clientRegion = System.getenv("AWS_REGION");
    private static String bucketName = System.getenv("AWS_BUCKET");
    private static String accessKey = System.getenv("AWS_ACCESS_KEY");
    private static String secretKey = System.getenv("AWS_SECRET_KEY");

	public List<s3Object> getPatientList(String[] pathList) {
        
        List<s3Object> patients = new ArrayList<s3Object>();
        int count = 0;

        try {
            BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                                    .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                                    .withRegion(clientRegion)
                                    .build();
            
            for(String s3Path : pathList) {            	
            	String urlS3Object = s3Client.getUrl(bucketName, s3Path).toExternalForm();
            	
               if(urlS3Object != null && !urlS3Object.isEmpty()) {
                	s3Object object = new s3Object();
                	object.Path = s3Path;
                	object.URL = urlS3Object;         	
                	patients.add(object);
                	
               } else {
                	s3Object object = new s3Object();
                	object.Path = s3Path;
                	object.URL = "NÃO ENCONTRADO!!!";
                	patients.add(object);
                }
                
                count++;	                
            }
            
            return patients;       
            
        } catch (AmazonClientException e) {
        	throw new AmazonClientException("Failed to access object : " + pathList[count] + "exception: " + e.getMessage());
        }
    }	
}
