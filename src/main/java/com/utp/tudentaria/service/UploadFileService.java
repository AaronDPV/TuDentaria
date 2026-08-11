package com.utp.tudentaria.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UploadFileService {

    private final String folder = "uploads";

    public String guardarImagen(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }

        File directorio = new File(folder);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        String nombreUnico = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        byte[] bytes = file.getBytes();
        Path path = Paths.get(folder + "//" + nombreUnico);
        Files.write(path, bytes);

        return nombreUnico;
    }

    public void eliminarImagen(String nombreImagen) {
        if (nombreImagen != null && !nombreImagen.isEmpty()) {
            Path path = Paths.get(folder + "//" + nombreImagen);
            File file = path.toFile();
            if (file.exists()) {
                file.delete();
            }
        }
    }
}