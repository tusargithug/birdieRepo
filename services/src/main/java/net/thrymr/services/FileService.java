package net.thrymr.services;
import net.thrymr.FileDocument;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface FileService {

    public String addFile(MultipartFile upload) throws IOException ;

    public FileDocument downloadFile(String id) throws IOException ;

    String deleteFile(String id);
}