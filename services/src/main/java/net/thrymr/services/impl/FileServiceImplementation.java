package net.thrymr.services.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import net.thrymr.FileDocument;
import net.thrymr.FileDocumentRepo;
import net.thrymr.constant.Constants;
import net.thrymr.model.FileEntity;
import net.thrymr.model.Groups;
import net.thrymr.repository.FileRepo;
import net.thrymr.repository.GroupRepo;
import net.thrymr.services.FileService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FileServiceImplementation implements FileService {
    @Autowired
    GridFsTemplate template;

    @Autowired
    GridFsOperations operations;
    @Autowired
    FileRepo fileRepo;
    @Autowired
    FileDocumentRepo fileDocumentRepo;

    @Autowired
    GroupRepo groupRepo;

    @Autowired
    private ResourceLoader resourceLoader;

    private static final String FORMAT="classpath:videos/%s.mp4";


    @Override
    public String addFile(MultipartFile upload) throws IOException {
        DBObject metadata = new BasicDBObject();
        FileEntity fileEntity = new FileEntity();
        Groups groups = new Groups();
        List<FileEntity> imageList = new ArrayList<>();


        if (upload.getSize() <= 30000000) {
            metadata.put("fileSize", upload.getSize());
            Object fileID = template.store(upload.getInputStream(), upload.getOriginalFilename(), upload.getContentType(), metadata);
            fileEntity.setFileId(fileID.toString());
            fileEntity.setFileName(upload.getOriginalFilename());
            fileEntity.setFileSize(upload.getSize());
            if(upload.getContentType().contains("audio")){
                fileEntity.setFileType("AUDIO");
            }
            if(upload.getContentType().contains("video")){
                fileEntity.setFileType("VIDEO");
            }
            if(upload.getContentType().contains("pdf")){
                fileEntity.setFileType("PDF");
            }
            if(upload.getContentType().contains("document")){
                fileEntity.setFileType("DOCUMENT");
            }
            if(upload.getContentType().contains("image")){
                fileEntity.setFileType("IMAGE");
            }
            if(upload.getContentType().contains("zip")){
                fileEntity.setFileType("ZIP");
            }
            fileEntity.setFileContentType(upload.getContentType());
            fileEntity.setSearchKey(getFileEntitySearchKey(fileEntity));
            FileDocument fileDocument = new FileDocument();
            fileDocument.setFileEntity(fileEntity);
            fileDocumentRepo.save(fileDocument);
            fileRepo.save(fileEntity);
            return "file uploaded successfully file token is: " + fileID.toString();
        }else {
            return "uploaded file should not exceed 12 MB";
        }
    }

    @Override
    public FileDocument downloadFile(String id) throws IOException {
        GridFSFile gridFSFile = template.findOne(new Query(Criteria.where("_id").is(id)));
        FileDocument loadFile = new FileDocument();
        if (gridFSFile != null && gridFSFile.getMetadata() != null && loadFile.getIsActive().equals(Boolean.TRUE)) {
            loadFile.setFileName(gridFSFile.getFilename());
            loadFile.setFileType(gridFSFile.getMetadata().get("_contentType").toString());
            loadFile.setFileSizeInBytes(gridFSFile.getMetadata().get("fileSize").toString());
            loadFile.setFile(IOUtils.toByteArray(operations.getResource(gridFSFile).getInputStream()));
        }
        return loadFile;

    }


    @Override
    public String deleteFile(String id) {
        FileDocument fileDocument = null;
        FileEntity fileDetails=null;
        if (id != null) {
            Optional<FileDocument> optionalFileDocument = fileDocumentRepo.findByFileId(id);
            if (optionalFileDocument.isPresent()) {
                fileDocument = optionalFileDocument.get();
                fileDocument.setIsActive(Boolean.FALSE);
                fileDocument.setIsDeleted(Boolean.TRUE);
                fileDocumentRepo.save(fileDocument);
            }
            Optional<FileEntity> optionalFileDetails = fileRepo.findByFileId(id);
            if (optionalFileDocument.isPresent()) {
                fileDetails = optionalFileDetails.get();
                fileDetails.setIsActive(Boolean.FALSE);
                fileDetails.setIsDeleted(Boolean.TRUE);
                fileDetails.setSearchKey(getFileEntitySearchKey(fileDetails));
                fileRepo.save(fileDetails);
            }
            return "file delete successfully";
        }
        return "this id not present in database";
    }

    @Override
    public String uploadFiles(MultipartFile[] files) throws IOException {
        DBObject metadata = new BasicDBObject();
        List<FileEntity> fileEntities = new ArrayList<>();
        if (files != null) {
            for (MultipartFile upload : files) {
                if (upload.getSize() <= 12000000) {
                    FileEntity fileEntity = new FileEntity();
                    metadata.put("fileSize", upload.getSize());
                    Object fileID = template.store(upload.getInputStream(), upload.getOriginalFilename(), upload.getContentType(), metadata);
                    fileEntity.setFileId(fileID.toString());
                    fileEntity.setFileName(upload.getOriginalFilename());
                    fileEntity.setFileSize(upload.getSize());
                    if (upload.getContentType().contains("audio")) {
                        fileEntity.setFileType("AUDIO");
                    }
                    if (upload.getContentType().contains("video")) {
                        fileEntity.setFileType("VIDEO");
                    }
                    if (upload.getContentType().contains("pdf")) {
                        fileEntity.setFileType("PDF");
                    }
                    if (upload.getContentType().contains("document")) {
                        fileEntity.setFileType("DOCUMENT");
                    }
                    if (upload.getContentType().contains("image")) {
                        fileEntity.setFileType("IMAGE");
                    }
                    if (upload.getContentType().contains("zip")) {
                        fileEntity.setFileType("ZIP");
                    }
                    fileEntity.setFileContentType(upload.getContentType());
                    fileEntity.setSearchKey(getFileEntitySearchKey(fileEntity));
                    FileDocument fileDocument = new FileDocument();
                    fileDocument.setFileEntity(fileEntity);
                    fileEntities.add(fileEntity);
                    fileDocumentRepo.save(fileDocument);
                    fileRepo.save(fileEntity);
                }else {
                    return "uploaded file should not exceed 12 MB";
                }
            }
            return "file uploaded successfully " + fileEntities.stream()
                    .collect
                            (Collectors.toMap(FileEntity::getId, FileEntity::getFileId));
        }
        return "File is empty";
    }

    @Override
    public Mono<Resource> getVideo(Integer sequence){
        String fileName = Constants.getVideoFiles().get(sequence);
        return Mono.fromSupplier(()->resourceLoader.
                getResource(String.format(FORMAT, fileName)));
    }

    public String getFileEntitySearchKey(FileEntity fileEntity) {
        String searchKey = "";
        if (fileEntity.getFileId() != null) {
            searchKey = searchKey + " " + fileEntity.getFileId();
        }
        if (fileEntity.getFileName() != null) {
            searchKey = searchKey + " " + fileEntity.getFileName();
        }
        if (fileEntity.getFileType() != null) {
            searchKey = searchKey + " " + fileEntity.getFileType();
        }
        if (fileEntity.getFileSize() != null) {
            searchKey = searchKey + " " + fileEntity.getFileSize();
        }
        if (fileEntity.getFileContentType() != null) {
            searchKey = searchKey + " " + fileEntity.getFileContentType();
        }
        if (fileEntity.getIsActive() != null && fileEntity.getIsActive().equals(Boolean.FALSE)) {
            searchKey = searchKey + " " + "Inactive";
        } else {
            searchKey = searchKey + " " + "Active";
        }
        return searchKey;
    }


}
