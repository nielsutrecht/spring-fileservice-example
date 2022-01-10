package com.nibado.example.fileservice;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {
    private final FileService service;

    public IndexController(FileService service) {
        this.service = service;
    }

    @GetMapping
    public ModelAndView allFiles() {
        var mav = new ModelAndView("index");

        var files = service.findAll().stream()
                .map(f -> new FileDto(f.id(), f.name(), mapSize(f.size()), f.contentType().toString())).toList();

        mav.addObject("files", files);
        return mav;
    }

    private SizeDto mapSize(long size) {
        var scaled = (double)size;
        var unit = "B";
        if(size >= 1_000_000_000) {
            scaled /= 1_000_000_000;
            unit = "GB";
        } else if(size >= 1_000_000) {
            scaled /= 1_000_000;
            unit = "MB";
        } else if(size >= 1_000) {
            scaled /= 1_000;
            unit = "kB";
        }

        return new SizeDto(scaled, unit);
    }

    private record FileDto(String id, String name, SizeDto size, String type){}
    private record SizeDto(double size, String unit){
        @Override
        public String toString() {
            return String.format("%.2f %s", size, unit);
        }
    }
}
