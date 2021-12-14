package com.prgrms.needit.common.domain.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UploadService {

	private static final String BUCKET = "needit-image";

	private final AmazonS3Client amazonS3Client;

	public String upload(MultipartFile file, String dirName) throws IOException {

		SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");
		String fileName =
			dirName + "/" + file.getOriginalFilename() + "-" + date.format(new Date());

		amazonS3Client.putObject(
			new PutObjectRequest(BUCKET, fileName, file.getInputStream(), null)
				.withCannedAcl(CannedAccessControlList.PublicRead)
		);

		return amazonS3Client
			.getUrl(BUCKET, fileName)
			.toString();
	}

	public void deleteImage(List<String> curImages, String dirName) {
		for (String curImage : curImages) {
			String[] images = curImage.split("/");
			String image = dirName + "/" + images[images.length - 1];

			boolean isExistObject = amazonS3Client.doesObjectExist(BUCKET, image);

			if (isExistObject) {
				amazonS3Client.deleteObject(BUCKET, image);
			}
		}
	}
}
