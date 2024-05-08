package com.example.whatsapp.service;

import com.example.whatsapp.model.Media;
import com.example.whatsapp.model.enums.FileType;
import com.example.whatsapp.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class Utils {

    private final MediaRepository mediaRepository;

    public boolean removeFile(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                if (file.delete()) {
                    Media media = mediaRepository.findFirstByLocation(path);
                    mediaRepository.delete(media);
                    System.out.println("Deleted " + path);
                } else {
                    System.out.println("Can not delete");
                    return false;
                }
            } else {
                System.out.println("File doesn't exist");
                return false;
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public Media saveFile(MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                throw new Exception();
            }
            String[] fileName = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");

            if (fileName.length > 0) {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                String name = timestamp.getTime() + "." + fileName[fileName.length - 1];
                File folder = Objects.requireNonNull(file.getContentType()).toLowerCase().contains("image")
                        ? new File("/picture")
                        : new File("/video");
                if (!folder.exists() && !folder.isDirectory()) {
                    if (folder.mkdir()) {
                        System.out.println("Folder created");
                    } else {
                        System.out.println("Unable to create folder");
                    }
                }
                File saved = new File(folder, name);
                System.out.println("path " + saved.getPath());
                System.out.println("path " + saved.getAbsolutePath());
                if (!saved.exists()) {
                    if (saved.createNewFile()) {
                        InputStream inputStream = file.getInputStream();
                        Files.copy(inputStream, saved.toPath(),
                                StandardCopyOption.REPLACE_EXISTING);
                    } else {
                        System.out.println("Unable to create file");

                    }
                }
                Media media = Media
                        .builder()
                        .location(saved.getAbsolutePath())
                        .size(file.getSize())
                        .type(Objects.requireNonNull(file.getContentType())
                                .toLowerCase().contains("image") ? FileType.PICTURE : FileType.VIDEO)
                        .build();

                return mediaRepository.save(media);
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            return null;
        }
    }
}
