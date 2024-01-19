package com.training.socialnetwork.util.image;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageUtils {

	private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));
	
	public String saveImage(MultipartFile image) throws IOException {
		Path staticPath = Paths.get("static");
		Path imagePath = Paths.get("images");
		
		if(!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath))) {
			Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath));	
		}
		Path file = CURRENT_FOLDER.resolve(staticPath).resolve(imagePath).resolve(image.getOriginalFilename());
		try (OutputStream os = Files.newOutputStream(file)) {
			os.write(image.getBytes());
		}
		
		return imagePath.resolve(image.getOriginalFilename()).toString();
	}
}
