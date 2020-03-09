package com.amazons3utils.interfaces.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.amazons3utils.entities.s3Object;

public interface IFileService {
	String createFinalFile(Collection<s3Object> patients);
	String encoder(String filePath);
}
