package com.acqu.co.excel.converter.actuator.service;

import com.acqu.co.excel.converter.actuator.model.AcquUserEntity;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
public interface AcquUserEntityService {
    List<AcquUserEntity> findAll(Sort sort);

    InputStreamResource exportXls() throws IOException;

    @Transactional
    List<AcquUserEntity> uploadAcquUserEntityFromExcel(MultipartFile file) throws IOException;

    Page<AcquUserEntity> findAll(String search, Pageable pageable);

}
