package com.springboot.service;

import com.springboot.domain.Country;
import org.apache.ignite.springdata22.repository.IgniteRepository;
import org.apache.ignite.springdata22.repository.config.RepositoryConfig;
import org.springframework.stereotype.Repository;

import javax.cache.Cache;
import java.util.List;

@RepositoryConfig (cacheName = "Country",autoCreateCache = true)
@Repository
public interface CountryRepository extends IgniteRepository<Country, String> {

    public List<Cache.Entry<String, Country>> findByPopulationGreaterThanEqualOrderByPopulationDesc(int population);
}