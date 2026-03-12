package com.mily04.systemw.controller;

import com.mily04.systemw.model.ProjectData;
import com.mily04.systemw.repository.ProjectDataRepository; // 需新建对应的 Repository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/data")
@CrossOrigin(origins = "*")
public class DataController {

    @Autowired
    private ProjectDataRepository repository;

    @GetMapping
    public ProjectData getData() {
        return repository.findById("main_project").orElse(new ProjectData());
    }

    @PostMapping
    public ProjectData saveData(@RequestBody ProjectData data) {
        data.setId("main_project");
        return repository.save(data);
    }
}
