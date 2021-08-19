package com.api.tutorjoint.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.api.tutorjoint.exception.CourseNotFoundException;
import com.api.tutorjoint.exception.DataNotFoundException;
import com.api.tutorjoint.exception.PartyNotFoundException;
import com.api.tutorjoint.gcp.GoogleStorageClientAdapter;
import com.api.tutorjoint.model.Course;
import com.api.tutorjoint.model.FileInfo;
import com.api.tutorjoint.model.FileResource;
import com.api.tutorjoint.model.Party;
import com.api.tutorjoint.repository.CourseRepository;
import com.api.tutorjoint.repository.FileRepository;
import com.api.tutorjoint.repository.PartyRepository;
import com.google.api.services.storage.model.StorageObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    GoogleStorageClientAdapter googleStorageClientAdapter;

    public void init() {
    }

    public StorageObject get(Long id) throws RuntimeException {
        try {
            FileResource file = fileRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Resource Not Found"));
            StorageObject object = googleStorageClientAdapter.download(file.getId() + "");
            object.setName(file.getFileName());
            return object;
        } catch (Exception e) {
            throw new RuntimeException("Could not download the file. Error: " + e.getMessage());
        }
    }

    public void put(MultipartFile file,
                    long course_id,
                    long party_id) throws RuntimeException {
        try {
            Party party = partyRepository.findById(party_id).orElseThrow(() -> new PartyNotFoundException("Party Not Found"));
            Course course = courseRepository.findById(course_id).orElseThrow(() -> new CourseNotFoundException("Course Not Found"));

            FileResource fModel = new FileResource();
            fModel.setContentType(file.getContentType());
            fModel.setFileName(file.getOriginalFilename());
            fModel.setSize(file.getSize());
            fModel.setUpdatedOn(Instant.now());
            fModel.setCreatedOn(Instant.now());
            fModel.setParty(party);
            fModel.setCourse(course);
            fileRepository.save(fModel);

            googleStorageClientAdapter.upload(file, fModel.getId() + "");

        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public boolean drop(Long id) throws RuntimeException {
        try {
            FileResource file = fileRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Resource Not Found"));
            googleStorageClientAdapter.delete(id + "");
            fileRepository.delete(file);

            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public List<FileInfo> getMetadata(Long party_id) throws RuntimeException {
        try {

            Party party = partyRepository.findById(party_id).orElseThrow(() -> new PartyNotFoundException("Party Not Found"));

            //Get Courses
            List<FileResource> dbFiles = fileRepository.findByParty(party, Sort.by(Sort.Direction.DESC, "createdOn"));

            List<FileInfo> lstFileInfo = new ArrayList<FileInfo>();
            for (int i = 0; i < dbFiles.size(); i++) {
                FileResource resource = dbFiles.get(i);
                FileInfo fileInfo = new FileInfo();
                fileInfo.setId(resource.getId());
                fileInfo.setType(resource.getContentType());
                fileInfo.setName(resource.getFileName());
                fileInfo.setUrl("http://localhost:8080/api/storage/download?file=" + resource.getId());
                fileInfo.setSize(Long.toString(resource.getSize()));
                fileInfo.setDescription(resource.getDescription());
                fileInfo.setCreatedOn(resource.getCreatedOn());
                fileInfo.setCourse_id(resource.getCourse().getId().toString());
                fileInfo.setUsername(resource.getParty().getUser().getUsername());

                lstFileInfo.add(fileInfo);
            }
            return lstFileInfo;
        } catch (Exception e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    public List<FileInfo> getMetadataByCourse(Long course_id) throws RuntimeException {
        try {

            Course course = courseRepository.findById(course_id).orElseThrow(() -> new CourseNotFoundException("Course Not Found"));

            //Get Courses
            List<FileResource> dbFiles = fileRepository.findByCourse(course, Sort.by(Sort.Direction.DESC, "createdOn"));

            List<FileInfo> lstFileInfo = new ArrayList<FileInfo>();
            for (int i = 0; i < dbFiles.size(); i++) {
                FileResource resource = dbFiles.get(i);
                FileInfo fileInfo = new FileInfo();
                fileInfo.setId(resource.getId());
                fileInfo.setType(resource.getContentType());
                fileInfo.setName(resource.getFileName());
                fileInfo.setUrl("http://localhost:8080/api/storage/download?file=" + resource.getId());
                fileInfo.setSize(Long.toString(resource.getSize()));
                fileInfo.setDescription(resource.getDescription());
                fileInfo.setCreatedOn(resource.getCreatedOn());
                fileInfo.setCourse_id(resource.getCourse().getId().toString());
                fileInfo.setUsername(resource.getParty().getUser().getUsername());

                lstFileInfo.add(fileInfo);
            }
            return lstFileInfo;
        } catch (Exception e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    public List<FileInfo> getMetadata() {
        try {
            List<FileResource> dbFiles = fileRepository.findAll();

            List<FileInfo> lstFileInfo = new ArrayList<FileInfo>();
            for (int i = 0; i < dbFiles.size(); i++) {
                FileResource resource = dbFiles.get(i);
                FileInfo fileInfo = new FileInfo();
                fileInfo.setId(resource.getId());
                fileInfo.setType(resource.getContentType());
                fileInfo.setName(resource.getFileName());
                fileInfo.setUrl("http://localhost:8080/api/storage/download?file=" + resource.getId());
                fileInfo.setSize(Long.toString(resource.getSize()));
                fileInfo.setDescription(resource.getDescription());
                fileInfo.setCreatedOn(resource.getCreatedOn());
                fileInfo.setCourse_id(resource.getCourse().getId().toString());
                fileInfo.setUsername(resource.getParty().getUser().getUsername());

                lstFileInfo.add(fileInfo);
            }
            return lstFileInfo;
        } catch (Exception e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
}