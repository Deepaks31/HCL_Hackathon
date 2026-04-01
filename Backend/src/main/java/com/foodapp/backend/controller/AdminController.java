package com.foodapp.backend.controller;

import com.foodapp.backend.entity.User;
import com.foodapp.backend.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/pending-owners")
    public ResponseEntity<List<User>> getPendingOwners() {
        return ResponseEntity.ok(adminService.getPendingOwners());
    }

    @PutMapping("/approve-owner/{id}")
    public ResponseEntity<String> approveOwner(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(adminService.approveOwner(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/reject-owner/{id}")
    public ResponseEntity<String> rejectOwner(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(adminService.rejectOwner(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
