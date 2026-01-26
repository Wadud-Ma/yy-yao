package com.yy.yao.dao.repository;

import com.yy.yao.dao.entity.Trigram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 八卦数据访问接口
 */
@Repository
public interface TrigramRepository extends JpaRepository<Trigram, Integer> {

    /**
     * 根据卦名查询
     *
     * @param name 卦名 (乾、坤、震等)
     * @return 八卦信息
     */
    Optional<Trigram> findByName(String name);

    /**
     * 根据三爻符号查询
     *
     * @param symbol 三爻符号 (111, 000等)
     * @return 八卦信息
     */
    Optional<Trigram> findBySymbol(String symbol);

    /**
     * 根据自然属性查询
     *
     * @param nature 自然属性 (天、地、雷等)
     * @return 八卦信息
     */
    Optional<Trigram> findByNature(String nature);

    /**
     * 检查卦名是否存在
     *
     * @param name 卦名
     * @return 是否存在
     */
    boolean existsByName(String name);
}
