package com.acqu.co.excel.converter.actuator.service.impl;

import com.acqu.co.excel.converter.actuator.exception.ExcelImportException;
import com.acqu.co.excel.converter.actuator.model.AcquUserEntity;
import com.acqu.co.excel.converter.actuator.model.specs.AcquUserEntitySearchParams;
import com.acqu.co.excel.converter.actuator.model.specs.AcquUserEntitySpecification;
import com.acqu.co.excel.converter.actuator.model.specs.BulkStatusRequest;
import com.acqu.co.excel.converter.actuator.repo.AcquUserEntityRepository;
import com.acqu.co.excel.converter.actuator.service.AcquUserEntityService;
import com.acqu.co.excel.converter.actuator.util.ExcelHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class AcquUserEntityServiceImpl implements AcquUserEntityService {

    @Autowired
    private AcquUserEntityRepository acquUserEntityRepository;
    @Autowired
    private ExcelHelper excelHelper;

    @Override
    public List<AcquUserEntity> findAll(Sort sort) {
        return acquUserEntityRepository.findAll(sort);
    }

    @Override
    public InputStreamResource exportXls() throws IOException {
        List<AcquUserEntity> allUsers = acquUserEntityRepository.findAll();
        return excelHelper.createWorkbook(allUsers);
    }

    @Override
    @Transactional
    public List<AcquUserEntity> uploadAcquUserEntityFromExcel(MultipartFile file) throws IOException, ExcelImportException {
        List<AcquUserEntity> users = excelHelper.parseExcelFileForAcquUserEntity(file);
        // Delete existing records by their IDs
        var idsToDelete = users.stream()
                .map(AcquUserEntity::getUserEntityId)
                .toList();
        if (CollectionUtils.isNotEmpty(idsToDelete)) {
            acquUserEntityRepository.deleteByIds(idsToDelete);
        }
        // Save the new records
        return acquUserEntityRepository.saveAll(users);
    }

    @Override
    public Page<AcquUserEntity> findAll(AcquUserEntitySearchParams acquUserEntitySearchParams) {
        return acquUserEntityRepository.findAll(
                new AcquUserEntitySpecification(acquUserEntitySearchParams),
                acquUserEntitySearchParams.getPageable().toPageRequest());
    }

    @Override
    public List<AcquUserEntity> uploadAcquUserEntityFromCsv(MultipartFile file) {
        List<AcquUserEntity> users;
        try {
            users = excelHelper.parseCsvFileForAcquUserEntity(file);
            // Delete existing records by their IDs
            var idsToDelete = users.stream()
                    .map(AcquUserEntity::getUserEntityId)
                    .toList();
            if (CollectionUtils.isNotEmpty(idsToDelete)) {
                acquUserEntityRepository.deleteByIds(idsToDelete);
            }
            // Save the new records
            return acquUserEntityRepository.saveAll(users);
        } catch (ExcelImportException e) {
            log.error("Error uploading AcquUserEntity from CSV", e);
            throw new RuntimeException("Failed to upload AcquUserEntity from CSV", e);
        }
    }

    @Override
    public List<String> getPhoneModels(Sort sorting) {
        return acquUserEntityRepository.getPhoneModels(sorting);
    }

    @Override
    public void deleteById(Long id) {
        acquUserEntityRepository.deleteById(id);
    }

    @Override
    public AcquUserEntity updateUser(Long id, AcquUserEntity user) {
        // Fetch the existing user by ID
        return acquUserEntityRepository.findById(id).map(existingUser -> {
            // Update fields if they are not null (null values are ignored)
            if (user.getUserName() != null) {
                existingUser.setUserName(user.getUserName());
            }
            if (user.getUserEmail() != null) {
                existingUser.setUserEmail(user.getUserEmail());
            }
            if (user.getPhoneModel() != null) {
                existingUser.setPhoneModel(user.getPhoneModel());
            }
            if (user.getUserDescription() != null) {
                existingUser.setUserDescription(user.getUserDescription());
            }
            if (user.getStatus() != null) {
                existingUser.setStatus(user.getStatus());
            }

            // Update the updatedDate to the current date and time
            existingUser.setUpdatedDate(new Date());

            // Save the updated entity
            return acquUserEntityRepository.save(existingUser);
        }).orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    @Override
    public AcquUserEntity createUser(AcquUserEntity user) {
        // Save the new user entity
        return acquUserEntityRepository.save(user);
    }

    @Override
    public void updateBulkStatus(BulkStatusRequest bulkStatusRequest) {
        // Update the status for multiple users
        acquUserEntityRepository.updateStatusByIds(bulkStatusRequest.getUserIds(), bulkStatusRequest.getStatus());
    }

    @Override
    public void deleteDeletedRecords() {
        // Delete records marked as deleted
        acquUserEntityRepository.deleteByStatus("deleted");
    }

    @Override
    public InputStreamResource exportToExcel(AcquUserEntitySearchParams acquUserEntitySearchParams) {
        // Fetch filtered data
        List<AcquUserEntity> filteredUsers = acquUserEntityRepository.findAll(
                new AcquUserEntitySpecification(acquUserEntitySearchParams),
                acquUserEntitySearchParams.getPageable().toPageRequest()).getContent();
        try {
            // Generate Excel file
            return excelHelper.createWorkbook(filteredUsers);
        } catch (IOException e) {
            log.error("Error generating Excel file", e);
            throw new RuntimeException("Failed to export data to Excel", e);
        }
    }

    @Override
    public AcquUserEntity updatePhoneModel(Long id, String phoneModel) {
        // Fetch the existing user by ID
        return acquUserEntityRepository.findById(id).map(user -> {
            // Update the phone model
            user.setPhoneModel(phoneModel);
            // Save the updated user
            return acquUserEntityRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    @Override
    public List<String> getAuditTrail(Long id) {
        // Retrieve audit trail for the specified user
        return acquUserEntityRepository.getAuditTrailByUserId(id);
    }

}