package ch.kos.goat.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class StorageService {

    private final Path rootLocation = Paths.get("uploads");

    public StorageService() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    public String store(MultipartFile file) {
        try {
            if (file.isEmpty()) return null;
            String original = file.getOriginalFilename();
            String ext = original != null && original.contains(".")
                    ? original.substring(original.lastIndexOf("."))
                    : ".bin";
            String filename = UUID.randomUUID().toString() + ext;

            Files.copy(file.getInputStream(), this.rootLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            return "uploads/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }
}
