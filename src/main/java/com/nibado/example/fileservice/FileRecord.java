package com.nibado.example.fileservice;

import org.springframework.http.MediaType;

import java.io.File;

public record FileRecord(String id, String name, File path, long size, MediaType contentType) {
}
