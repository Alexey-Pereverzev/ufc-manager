package org.example.controller;


import lombok.RequiredArgsConstructor;
import org.example.service.WeightClassService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/weight-classes")
@RequiredArgsConstructor
public class WeightClassController {

    private final WeightClassService weightClassService;

    @GetMapping
    public ResponseEntity<List<String>> getAllWeightClasses() {
        return ResponseEntity.ok(weightClassService.getAll());
    }
}
