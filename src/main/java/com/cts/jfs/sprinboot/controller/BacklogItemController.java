package com.cts.jfs.sprinboot.controller;

import com.cts.jfs.sprinboot.model.BacklogItem;
import com.cts.jfs.sprinboot.service.BacklogItemService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/backlog")
@CrossOrigin(origins = "http://localhost:3000")
public class BacklogItemController {

    @Autowired
    private BacklogItemService backlogItemService;

    @GetMapping("/all")
    public ResponseEntity<List<BacklogItem>> getAllBacklogItems() {
        return ResponseEntity.ok(backlogItemService.getAllBacklogItems());
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<BacklogItem> getBacklogItemById(@PathVariable Long id) {
        return ResponseEntity.ok(backlogItemService.getBacklogItemById(id));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<BacklogItem>> getBacklogByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(backlogItemService.getBacklogByProduct(productId));
    }

    @PostMapping("/save")
    public ResponseEntity<BacklogItem> saveBacklogItem(@RequestBody BacklogItem item) {
        return ResponseEntity.status(HttpStatus.CREATED).body(backlogItemService.saveBacklogItem(item));
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<BacklogItem> updateBacklogItem(@PathVariable Long id,
                                                         @RequestBody BacklogItem item) {
        return ResponseEntity.ok(backlogItemService.updateBacklogItem(id, item));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBacklogItem(@PathVariable Long id) {
        backlogItemService.deleteBacklogItem(id);
        return ResponseEntity.ok("Backlog item deleted successfully");
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<BacklogItem>> getByPriority(@PathVariable String priority) {
        return ResponseEntity.ok(backlogItemService.getByPriority(
                BacklogItem.Priority.valueOf(priority)));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<BacklogItem>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(backlogItemService.getByStatus(
                BacklogItem.ItemStatus.valueOf(status)));
    }
}
