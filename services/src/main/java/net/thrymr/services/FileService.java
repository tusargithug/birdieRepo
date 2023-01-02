package net.thrymr.services;
import net.thrymr.FileDocument;
import net.thrymr.model.FileEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface FileService {
    public String addFile(MultipartFile upload) throws IOException ;

    public FileDocument downloadFile(String id) throws IOException ;

    String deleteFile(String id);

    String uploadFiles(MultipartFile[] files) throws IOException;

    FileDocument getVideo(String id) throws IOException;
}