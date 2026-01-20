package com.yy.yao.repository;

import com.yy.yao.entity.TrigramInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 八卦基础信息Repository
 */
@Repository
public interface TrigramInfoRepository extends JpaRepository<TrigramInfo, Long> {

    /**
     * 根据卦名查询
     */
    Optional<TrigramInfo> findByName(String name);

    /**
     * 根据二进制表示查询
     */
    Optional<TrigramInfo> findByBinaryCode(String binaryCode);

    /**
     * 根据自然属性查询
     */
    Optional<TrigramInfo> findByNature(String nature);

    /**
     * 检查卦名是否存在
     */
    boolean existsByName(String name);
}
