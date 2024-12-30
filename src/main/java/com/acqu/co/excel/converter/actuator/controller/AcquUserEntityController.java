package com.acqu.co.excel.converter.actuator.controller;

import com.acqu.co.excel.converter.actuator.exception.ServiceStatus;
import com.acqu.co.excel.converter.actuator.model.AcquUserEntity;
import com.acqu.co.excel.converter.actuator.model.specs.AcquUserEntitySearchParams;
import com.acqu.co.excel.converter.actuator.model.specs.BulkStatusRequest;
import com.acqu.co.excel.converter.actuator.service.AcquUserEntityService;
import com.acqu.co.excel.converter.actuator.util.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.acqu.co.excel.converter.actuator.util.Constants.*;

@RestController
@RequestMapping("/api/acqu-users")
@Slf4j
@AllArgsConstructor
public class AcquUserEntityController {

    private final AcquUserEntityService acquUserEntityService;


    @PostMapping(value = "/upload-csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Upload a CSV file with AcquUserEntity data",
            description = "Upload a file containing user data in CSV format.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            )
    )
    public ResponseEntity<?> uploadAcquUserEntityFromCsv(
            @Parameter(description = "CSV file to upload", required = true)
            @RequestPart("file") MultipartFile file) {
        try {
            List<AcquUserEntity> users = acquUserEntityService.uploadAcquUserEntityFromCsv(file);
            return new ResponseEntity<>(users, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<List<AcquUserEntity>>build();
        }
    }

    @GetMapping
    public ResponseEntity<List<AcquUserEntity>> getAllAcquUsers(@RequestParam(required = false) String sort) {
        Sort sorting = Sort.by(sort != null ? sort : "userEntityId");
        List<AcquUserEntity> users = acquUserEntityService.findAll(sorting);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    @PostMapping(value = "/export_all", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> exportAcquUserEntities() {
        try {
            InputStreamResource resource = acquUserEntityService.exportXls();
            HttpHeaders headers = new HttpHeaders();

            String fileName = "AcquUserEntities_" + DateUtil.getFormattedDateStr() + ".xlsx";

            headers.add(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + fileName);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (IOException e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<InputStreamResource>build();
        }
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Upload an Excel file with AcquUserEntity data",
            description = "Upload a file containing user data in Excel format.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            )
    )
    public ResponseEntity<?> uploadAcquUserEntityFromExcel(
            @Parameter(description = "Excel file to upload", required = true)
            @RequestPart("file") MultipartFile file, @RequestHeader HttpHeaders requestHeader) {
        try {
            List<AcquUserEntity> users = acquUserEntityService.uploadAcquUserEntityFromExcel(file);
            return new ResponseEntity<>(users, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<List<AcquUserEntity>>build();
        }
    }

    @PostMapping(value = "/paging", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "paging with AcquUserEntity data",
            description = "paging user data in Excel format."
    )
    public ResponseEntity<Page<AcquUserEntity>> getAcquUserEntityBySearch(
            @RequestBody AcquUserEntitySearchParams acquUserEntitySearchParams) {
        Page<AcquUserEntity> users = acquUserEntityService.findAll(acquUserEntitySearchParams);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    @GetMapping("/phone-models")
    @Operation(
            summary = "paging with AcquUserEntity phone-models",
            description = "paging phone-models data "
    )
    public ResponseEntity<List<String>> getPhoneModels(@RequestParam(required = false) String sort) {
        Sort sorting = Sort.by(sort != null ? sort : "phoneModel");
        return new ResponseEntity<>(acquUserEntityService.getPhoneModels(sorting), HttpStatus.OK);
    }

    @PostMapping("/paging")
    @Operation(summary = "Get paginated AcquUserEntities", description = "Retrieve users based on search parameters.")
    public ResponseEntity<Page<AcquUserEntity>> getUsers(@RequestBody AcquUserEntitySearchParams params) {
        Page<AcquUserEntity> users = acquUserEntityService.findAll(params);
        return ResponseEntity.ok(users);
    }

    @PostMapping
    @Operation(summary = "Create a new user", description = "Create a new AcquUserEntity.")
    public ResponseEntity<AcquUserEntity> createUser(@RequestBody AcquUserEntity user) {
        AcquUserEntity createdUser = acquUserEntityService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing user", description = "Update an AcquUserEntity by ID.")
    public ResponseEntity<AcquUserEntity> updateUser(@PathVariable Long id, @RequestBody AcquUserEntity user) {
        AcquUserEntity updatedUser = acquUserEntityService.updateUser(id,user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user", description = "Delete an AcquUserEntity by ID.")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        acquUserEntityService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/phone-model")
    @Operation(summary = "Update a user's phone model", description = "Update the phone model of a user by ID.")
    public ResponseEntity<AcquUserEntity> updatePhoneModel(
            @PathVariable Long id, @RequestBody String phoneModel) {
        AcquUserEntity updatedUser = acquUserEntityService.updatePhoneModel(id, phoneModel);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/bulk-status")
    @Operation(summary = "Update bulk status", description = "Update the status for multiple users.")
    public ResponseEntity<Void> updateBulkStatus(
            @RequestBody BulkStatusRequest bulkStatusRequest) {
        acquUserEntityService.updateBulkStatus(bulkStatusRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleted")
    @Operation(summary = "Delete all deleted records", description = "Remove all records marked as deleted.")
    public ResponseEntity<Void> deleteDeletedRecords() {
        acquUserEntityService.deleteDeletedRecords();
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/export", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Export to Excel", description = "Export all users to an Excel file.")
    public ResponseEntity<InputStreamResource> exportToExcel(@RequestBody AcquUserEntitySearchParams acquUserEntitySearchParams) {
        InputStreamResource resource = acquUserEntityService.exportToExcel(acquUserEntitySearchParams);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=users.xlsx");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("/{id}/audit")
    @Operation(summary = "Get audit trail", description = "Retrieve the audit trail for a user by ID.")
    public ResponseEntity<List<String>> getAuditTrail(@PathVariable Long id) {
        List<String> auditTrail = acquUserEntityService.getAuditTrail(id);
        return ResponseEntity.ok(auditTrail);
    }

}