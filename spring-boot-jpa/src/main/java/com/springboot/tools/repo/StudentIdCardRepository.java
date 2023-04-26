package com.springboot.tools.repo;

import com.springboot.tools.entity.StudentIdCard;
import org.springframework.data.repository.CrudRepository;

public interface StudentIdCardRepository
        extends CrudRepository<StudentIdCard, Long> {
}
