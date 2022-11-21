package net.thrymr.services.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import net.thrymr.FileDocument;
import net.thrymr.FileDocumentRepo;
import net.thrymr.model.FileEntity;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
        FileEntity fileEntity = new FileEntity();
        Groups groups = new Groups();
        List<FileEntity> imageList = new ArrayList<>();


        if (upload.getSize() <= 12000000) {
            metadata.put("fileSize", upload.getSize());
            Object fileID = template.store(upload.getInputStream(), upload.getOriginalFilename(), upload.getContentType(), metadata);
            fileEntity.setFileId(fileID.toString());
            fileEntity.setFileName(upload.getOriginalFilename());
            fileEntity.setFileSize(upload.getSize());
            fileEntity.setFileContentType(upload.getContentType());
            FileDocument fileDocument = new FileDocument();
            fileDocument.setFileEntity(fileEntity);
            fileDocumentRepo.save(fileDocument);
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
        FileEntity fileDetails = null;
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
                fileRepo.save(fileDetails);
            }
            return "file delete successfully";
        }
        return "this id not present in database";
    }
}
