package com.springboot.tools.repo;

import com.springboot.tools.entity.IdInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: User
 * @date: 2023/4/25
 * @Description:此类用于xxx
 */
@Repository
public interface IdResp extends JpaRepository<IdInfo, Long> {
}
