package com.cfranking.repository;

import com.cfranking.entity.CfContest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;

public interface ContestRepository extends CrudRepository<CfContest, String> {

    @Override
    @Cacheable
    Iterable<CfContest> findAll();
}
