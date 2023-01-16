package net.thrymr.controller;


import net.thrymr.FileDocument;
import net.thrymr.model.FileEntity;
import net.thrymr.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    FileService fileService;

    Logger logger = LoggerFactory.getLogger(FileController.class);

    @PostMapping()
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return new ResponseEntity<>(fileService.addFile(file), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ByteArrayResource> download(@PathVariable String id) throws IOException {
        FileDocument loadFile = fileService.downloadFile(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(loadFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + loadFile.getFileName())
                .body(new ByteArrayResource(loadFile.getFile()));
    }

    @PostMapping("/upload")
    public String uploadFiles(@RequestParam("files") MultipartFile[] files) throws IOException {
        return fileService.uploadFiles(files);
    }

    @GetMapping(value = "video/{sequence}", produces = "video/mp4")
    public Mono<Resource> getVideos(@PathVariable Integer sequence) throws IOException {
        //logger.info("range in bytes() : " + range);
        return fileService.getVideo(sequence);
    }

    @DeleteMapping("/{id}")
    public String deleteFile(@PathVariable String id) {
        return fileService.deleteFile(id);
    }

    @MutationMapping(name = "uploadFiles")
    public String uploadFiles(@Argument(name = "file") MultipartFile file) throws IOException {
        return fileService.addFile(file);
    }

    @QueryMapping(name = "downloads")
    public FileDocument downloads(@Argument String id) throws IOException {
        FileDocument loadFile = fileService.downloadFile(id);
        return loadFile;
    }

    @MutationMapping("deleteFiles")
    public String deleteFiles(@Argument String id) {
        return fileService.deleteFile(id);
    }
}


