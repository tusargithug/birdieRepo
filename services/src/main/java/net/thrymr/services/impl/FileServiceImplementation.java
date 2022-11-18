package net.thrymr.services.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import net.thrymr.FileDocument;
import net.thrymr.FileDocumentRepo;
import net.thrymr.model.FileDetails;
import net.thrymr.model.Groups;
import net.thrymr.repository.FileRepo;
import net.thrymr.repository.GroupRepo;
import net.thrymr.services.FileService;
import net.thrymr.utils.Validator;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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


    @Override
    public String addFile(MultipartFile upload) throws IOException {
        DBObject metadata = new BasicDBObject();
        FileDetails fileEntity = new FileDetails();
        Groups groups = new Groups();
        List<FileDetails> imageList = new ArrayList<>();


        if (upload.getSize() <= 12000000) {
            metadata.put("fileSize", upload.getSize());
            Object fileID = template.store(upload.getInputStream(), upload.getOriginalFilename(), upload.getContentType(), metadata);
            fileEntity.setFileId(fileID.toString());
            fileEntity.setFileName(upload.getOriginalFilename());
            fileEntity.setFileType(upload.getContentType());
            fileEntity.setFileSize(upload.getSize());
            fileEntity.setFileContentType(upload.getContentType());
            FileDocument fileDocument = new FileDocument();
            fileDocument.setFileEntity(fileEntity);
            fileDocumentRepo.save(fileDocument);
            if (Validator.isValid(upload.getContentType()) && upload.getContentType().contains("image")) {
                Optional<Groups> optionalGroups = groupRepo.findById(1l);
                if (optionalGroups.isPresent()) {
                    fileEntity.setGroups(optionalGroups.get());
                }
            }
            if (Validator.isValid(upload.getContentType()) && upload.getContentType().contains("pdf") || upload.getContentType().contains("zip")) {
                Optional<Groups> optionalGroups = groupRepo.findById(2l);
                if (optionalGroups.isPresent()) {
                    fileEntity.setGroups(optionalGroups.get());
                }
            }
            if (Validator.isValid(upload.getContentType()) && upload.getContentType().contains("video") || upload.getContentType().contains("audio")) {
                Optional<Groups> optionalGroups = groupRepo.findById(3l);
                if (optionalGroups.isPresent()) {
                    fileEntity.setGroups(optionalGroups.get());
                }
            }
            fileRepo.save(fileEntity);
            return "file uploaded successfully file token is: " + fileID.toString();
        }
        return "uploaded file should not exceed 12 MB";
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
        FileDetails fileDetails = null;
        if (id != null) {
            Optional<FileDocument> optionalFileDocument = fileDocumentRepo.findByFileId(id);
            if (optionalFileDocument.isPresent()) {
                fileDocument = optionalFileDocument.get();
                fileDocument.setIsActive(Boolean.FALSE);
                fileDocument.setIsDeleted(Boolean.TRUE);
                fileDocumentRepo.save(fileDocument);
            }
            Optional<FileDetails> optionalFileDetails = fileRepo.findByFileId(id);
            if (optionalFileDocument.isPresent()) {
                fileDetails = optionalFileDetails.get();
                fileDetails.setIsActive(Boolean.FALSE);
                fileDetails.setIsDeleted(Boolean.TRUE);
                fileRepo.save(fileDetails);
            }
            return "file delete successfully";
        }
        return "this id not present in database";
    }
}
