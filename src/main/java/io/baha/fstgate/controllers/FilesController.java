package io.baha.fstgate.controllers;

import io.baha.fstgate.exception.ResourceNotFoundException;
import io.baha.fstgate.models.Comment;
import io.baha.fstgate.models.Ppic;
import io.baha.fstgate.models.User;
import io.baha.fstgate.repository.CommentRepository;
import io.baha.fstgate.repository.PostRepository;
import io.baha.fstgate.repository.PpicRepository;
import io.baha.fstgate.repository.UserRepository;
import io.baha.fstgate.security.CurrentUser;
import io.baha.fstgate.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class FilesController {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    PpicRepository ppicRepository;


    @PostMapping("/api/file/upload")
    public String uploadMultipartFile(@CurrentUser UserPrincipal currentUser, @RequestPart("file") MultipartFile file) {
        try {

            Ppic filemode = new Ppic(file.getOriginalFilename(), file.getContentType(), file.getBytes());
            ppicRepository.save(filemode);
            userRepository.updateprofilepic(filemode.getId(), currentUser.getId());


            return "File uploaded successfully! -> filename = " + file.getOriginalFilename();


        } catch (Exception e) {
            return "FAIL! Maybe You had uploaded the file before or the file's size > 500KB";
        }
    }

    @GetMapping("/api/file/all")
    public List<Ppic> getListFiles() {
        return ppicRepository.findAll();
    }


    @GetMapping("/api/file/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id) {
        Optional<Ppic> fileOptional = ppicRepository.findById(id);
        String contentType = null;
        if (fileOptional.isPresent()) {
            System.out.println("gello");
            Ppic file = fileOptional.get();
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("image/png"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .body(file.getPic());
        }
        System.out.println("mello");
        return ResponseEntity.status(404).body(null);
    }


}
