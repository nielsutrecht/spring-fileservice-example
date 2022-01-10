package com.nibado.example.fileservice;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
@RequestMapping("/file")
public class FileController {
    private final FileService service;

    public FileController(FileService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public void download(@PathVariable("id") String id, HttpServletResponse response) throws IOException  {
        var record = service.findById(id).orElseThrow(() -> new IllegalArgumentException(id + " not found"));

        response.setContentType(record.contentType().toString());
        response.setHeader("Content-Length", Long.toString(record.size()));
        response.setHeader("Content-Disposition", "attachment; filename=\"" + record.name() +"\"");

        var ins = new FileInputStream(record.path());

        IOUtils.copy(ins, response.getOutputStream());
        IOUtils.closeQuietly(ins);
        IOUtils.closeQuietly(response.getOutputStream());
    }

    @PostMapping("/upload")
    public ResponseEntity<Void> upload(HttpServletRequest request) throws Exception {
        if(!ServletFileUpload.isMultipartContent(request)) {
            return ResponseEntity.badRequest().build();
        }

        service.upload(new ServletFileUpload().getItemIterator(request));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/");

        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
    }
}
