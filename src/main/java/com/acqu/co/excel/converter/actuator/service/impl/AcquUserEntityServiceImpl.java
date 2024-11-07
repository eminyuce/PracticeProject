package com.acqu.co.excel.converter.actuator.service.impl;

import com.acqu.co.excel.converter.actuator.exception.ExcelImportException;
import com.acqu.co.excel.converter.actuator.model.AcquUserEntity;
import com.acqu.co.excel.converter.actuator.repo.AcquUserEntityRepository;
import com.acqu.co.excel.converter.actuator.service.AcquUserEntityService;
import com.acqu.co.excel.converter.actuator.util.ExcelHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
        List<String> idsToDelete = users.stream()
                .map(AcquUserEntity::getUserEntityId) // Use the correct getter
                .toList();
        if (!idsToDelete.isEmpty()) {
            acquUserEntityRepository.deleteByIds(idsToDelete);
        }

// Save the new records
        return acquUserEntityRepository.saveAll(users);
    }

    @Override
    public Page<AcquUserEntity> findAll(String search, Pageable pageable) {
        Specification<AcquUserEntity> spec = Specification.where(null);

        if (search != null && !search.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("name"), "%" + search + "%"));
        }
        //return acquUserEntityRepository.findAll(spec, pageable);
        return null;
    }
}
