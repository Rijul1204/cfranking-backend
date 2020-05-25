package com.cfranking.repository;

import com.cfranking.entity.CfUserInfo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PersonsRepository extends CrudRepository<CfUserInfo, String> {

    @Override
    List<CfUserInfo> findAllById(Iterable<String> iterable);
 }
