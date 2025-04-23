package com.myfirst.microservice.controller;

import com.myfirst.microservice.dto.UserDTO;
import com.myfirst.microservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService service;


    @PostMapping("/{ttlInSeconds}")
    public ResponseEntity<UserDTO> create(@RequestBody UserDTO dto,@PathVariable long ttlInSeconds) {
        return ResponseEntity.ok(service.create(dto,ttlInSeconds));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody UserDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }


    @GetMapping("/name/{username}")
    public ResponseEntity<UserDTO> getByName(@PathVariable String username) {
        return ResponseEntity.ok(service.getByName(username));
    }



    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<UserDTO>> getAllPaged(Pageable pageable) {
        return ResponseEntity.ok(service.getAllPaged(pageable));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDTO> patch(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(service.patch(id, updates));
    }
}
