package net.thrymr.services;
import net.thrymr.FileDocument;
import net.thrymr.model.FileEntity;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;


public interface FileService {
    public String addFile(MultipartFile upload) throws IOException ;

    public FileDocument downloadFile(String id) throws IOException ;

    String deleteFile(String id);

    String uploadFiles(MultipartFile[] files) throws IOException;

    Mono<Resource> getVideo(Integer sequence) throws IOException;
}