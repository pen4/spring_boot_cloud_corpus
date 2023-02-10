package com.springboot.tools;

import org.springframework.data.repository.CrudRepository;

public interface StudentIdCardRepository
        extends CrudRepository<StudentIdCard, Long> {
}
