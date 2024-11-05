
package com.acqu.co.excel.converter.actuator.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.acqu.co.excel.converter.actuator.jpa.entity.AcquUserEntity;
 

@Service
public interface AcquUserEntityService {

	List<AcquUserEntity> findAll(Sort sort);

	InputStreamResource exportXls() throws IOException;

	@Transactional
	List<AcquUserEntity> uploadAcquUserEntityFromExcel(MultipartFile file) throws IOException, ExcelImportException;
	Page<AcquUserEntity> findAll(String search, Pageable pageable) ;
}
