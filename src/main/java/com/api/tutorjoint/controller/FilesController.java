package com.api.tutorjoint.controller;

import java.io.File;
import java.util.List;

import com.api.tutorjoint.service.FileStorageService;
import com.google.api.services.storage.model.StorageObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.api.tutorjoint.model.FileInfo;
import com.api.tutorjoint.dto.ResponseMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/storage")
public class FilesController {
    @Autowired
    FileStorageService storageService;

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> putFile(@RequestPart(value = "file", required = true) MultipartFile file,
                                                   @RequestPart(value = "party_id", required = true) String party_id,
                                                   @RequestPart(value = "course_id", required = true) String course_id) {
        String message = "";
        try {
            storageService.put(file, Long.parseLong(party_id), Long.parseLong(course_id));
            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<FileInfo>> getMetaData() {
        List<FileInfo> fileInfos = storageService.getMetadata();
        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    @RequestMapping(produces = "application/json",
            method = RequestMethod.DELETE,
            path = {"files/{id}"})
    public ResponseEntity deleteFile(@PathVariable @RequestBody Long id) {
        storageService.drop(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(path = "/download",
            method = RequestMethod.GET)
    public ResponseEntity<?> fileDownload(HttpServletRequest request,
                                          @RequestParam(value = "file", required = false) Long Id,
                                          HttpServletResponse response
    ) {
        try {
            StorageObject object = storageService.get(Id);

            byte[] array = com.google.common.io.Files.toByteArray((File) object.get("file"));
            ByteArrayResource resource = new ByteArrayResource(array);

            return ResponseEntity.ok()
                    .contentLength(array.length)
                    .header("Content-type", "application/octet-stream")
                    .header("Content-disposition", "attachment; filename=\"" + object.getName() + "\"").body(resource);
        } catch (Exception exception) {
            return new ResponseEntity<>(
                    exception,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}