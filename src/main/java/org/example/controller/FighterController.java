package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.FighterDto;
import org.example.dto.StringResponse;
import org.example.service.FighterService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fighters")
@RequiredArgsConstructor
public class FighterController {

    private final FighterService fighterService;

    @GetMapping
    public List<FighterDto> getAll() {
        return fighterService.getAll();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody FighterDto dto) {
        String result = fighterService.create(dto);
        return ResponseEntity.ok(new StringResponse(result));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody FighterDto dto) {
        String result = fighterService.update(id, dto);
        return ResponseEntity.ok(new StringResponse(result));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fighterService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
