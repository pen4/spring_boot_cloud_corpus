package com.springboot.service;

import com.springboot.entity.Organization;
import org.apache.ignite.springdata22.repository.IgniteRepository;
import org.apache.ignite.springdata22.repository.config.RepositoryConfig;

@RepositoryConfig(cacheName = "ORG_CACHE",autoCreateCache = true)
public interface OrganizationRepository extends IgniteRepository<Organization,Long> {
}
