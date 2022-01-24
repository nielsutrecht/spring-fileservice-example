package com.nibado.example.fileservice;

import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;

@Service
public class FileService {
    private static final Logger LOG = LoggerFactory.getLogger(FileService.class);

    private final FileRepository repository;
    private final File saveDir;

    public FileService(FileRepository repository, @Value("${save-dir}") File saveDir) {
        this.repository = repository;
        this.saveDir = saveDir;
        saveDir.mkdirs();
    }

    public void upload(FileItemIterator iter) throws IOException {
        while (iter.hasNext()) {
            FileItemStream item = iter.next();
            if(item.isFormField()) {
                continue;
            }
            upload(item);
        }
    }

    private void upload(FileItemStream item) throws IOException {
        var fileName = item.getName();
        var type = item.getContentType();
        var ins = item.openStream();
        var destination = new File(saveDir, format("%s-%s", UUID.randomUUID(), fileName));
        var outs = new FileOutputStream(destination);

        IOUtils.copy(ins, outs);
        IOUtils.closeQuietly(ins);
        IOUtils.closeQuietly(outs);

        var file = new FileRecord(null, fileName, destination, destination.length(), MediaType.parseMediaType(type));

        repository.create(file);

        LOG.info("Saved {} with type {} to {}", fileName, type, destination);
    }

    public List<FileRecord> findAll() {
        var records = repository.findAll().stream()
                .map(FileRepository.FileEntity::toRecord)
                .toList();

        records.stream().filter(r -> !r.path().exists()).map(FileRecord::id).forEach(repository::deleteById);

        return records.stream().filter(r -> r.path().exists()).toList();
    }

    public Optional<FileRecord> findById(String id) {
        return repository.findById(id).map(FileRepository.FileEntity::toRecord);
    }

}
