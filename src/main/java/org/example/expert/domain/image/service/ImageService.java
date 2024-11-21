package org.example.expert.domain.image.service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

	private final S3Client s3Client;

	@Value("${cloud.aws.s3.bucket-name}")
	private String bucketName;

	public String uploadImage(MultipartFile file) throws IOException {
		validateFile(file);

		// 고유한 파일 이름 생성
		String fileName = generateUniqueFileName(file.getOriginalFilename());

		// 로컬 임시 디렉토리에 파일 저장
		File tempFile = File.createTempFile("upload-", fileName);
		file.transferTo(tempFile); // MultipartFile -> File 변환

		try {
			// S3에 업로드
			s3Client.putObject(
				PutObjectRequest.builder()
					.bucket(bucketName)
					.key(fileName)
					.contentType(file.getContentType())
					.build(),
				tempFile.toPath() // File -> Path
			);

			return fileName; // 업로드된 파일 이름 리턴
		} catch (S3Exception e) {
			throw new RuntimeException("Failed to upload file to S3", e);
		} finally {
			// 업로드 후 임시 파일 삭제
			tempFile.delete();
		}
	}

	private void validateFile(MultipartFile file) {
		// 파일 타입 확인
		if (!file.getContentType().matches("image/(jpeg|png)")) {
			throw new IllegalArgumentException("Only PNG and JPG images are allowed.");
		}
		// 파일 크기 확인 (2MB 이하)
		if (file.getSize() > 2 * 1024 * 1024) {
			throw new IllegalArgumentException("File size must be less than 2MB.");
		}
	}

	private String generateUniqueFileName(String originalFilename) {
		String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
		return UUID.randomUUID() + extension;
	}
}

