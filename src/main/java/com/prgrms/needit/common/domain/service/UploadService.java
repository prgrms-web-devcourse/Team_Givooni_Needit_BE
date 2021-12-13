package com.prgrms.needit.common.domain.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UploadService {

	private static final String BUCKET = "needit-image";

	private final AmazonS3Client amazonS3Client;

	public String upload(MultipartFile file, String dirName) throws IOException {
		InputStream uploadFile = file.getInputStream();
		String fileName = dirName + "/" + uploadFile;

		ObjectMetadata objectMetadata = new ObjectMetadata();
		byte[] bytes = IOUtils.toByteArray(file.getInputStream());
		objectMetadata.setContentLength(bytes.length);

		amazonS3Client.putObject(
			new PutObjectRequest(BUCKET, fileName, file.getInputStream(), objectMetadata)
				.withCannedAcl(CannedAccessControlList.PublicRead)
		);

		return amazonS3Client
			.getUrl(BUCKET, fileName)
			.toString();
	}
}
