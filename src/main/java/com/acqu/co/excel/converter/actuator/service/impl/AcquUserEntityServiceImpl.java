package com.acqu.co.excel.converter.actuator.service.impl;

import com.acqu.co.excel.converter.actuator.exception.ExcelImportException;
import com.acqu.co.excel.converter.actuator.model.AcquUserEntity;
import com.acqu.co.excel.converter.actuator.model.specs.AcquUserEntitySearchParams;
import com.acqu.co.excel.converter.actuator.model.specs.AcquUserEntitySpecification;
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
}