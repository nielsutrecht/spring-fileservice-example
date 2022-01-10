package com.nibado.example.fileservice;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

public interface FileRepository extends CrudRepository<FileRepository.FileEntity, String> {

    List<FileEntity> findAll();

    @Table("file")
    class FileEntity implements Persistable {
        @Id
        String id;
        String name;
        String path;
        long size;
        String contentType;

        FileEntity(){}
        FileEntity(String id, FileRecord record) {
            this.id = id;
            this.name = record.name();
            this.path = record.path().getPath();
            this.size = record.size();
            this.contentType = record.contentType().toString();
        }

        @Override
        public Object getId() {
            return id;
        }

        @Override
        public boolean isNew() {
            return true;
        }

        public FileRecord toRecord() {
            return new FileRecord(id, name, new File(path), size, MediaType.parseMediaType(contentType));
        }
    }

    @Transactional
    default void create(FileRecord record) {
        var id = nextId();

        var entity = new FileEntity(id, record);

        save(entity);
    }

    default String nextId() {
        String id = null;
        do {
            id = IdGenerator.generate(11);

        } while (existsById(id));

        return id;
    }
}
