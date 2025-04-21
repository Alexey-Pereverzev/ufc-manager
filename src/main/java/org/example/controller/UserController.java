package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.StringResponse;
import org.example.dto.UserAuthDto;
import org.example.dto.UserDto;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable Long id, @RequestBody Map<String, String> role) {
        String roleUpdate = role.get("role");
        String username = userService.updateUserRole(id, roleUpdate);
        return ResponseEntity.ok(new StringResponse("Role of user '" + username + "' successfully updated"));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateUserStatus(@PathVariable Long id, @RequestBody Map<String, String> status) {
        String statusUpdate = status.get("status");
        String username = userService.updateUserStatus(id, statusUpdate);
        return ResponseEntity.ok(new StringResponse("Status of user '" + username + "' successfully updated"));
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserAuthDto dto) {
        userService.register(dto);
        return ResponseEntity.ok(new StringResponse("User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody UserAuthDto dto) {
        return ResponseEntity.ok(userService.auth(dto));
    }
}
