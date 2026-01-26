package com.yy.yao.dao.repository;

import com.yy.yao.dao.entity.Hexagram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 六十四卦数据访问接口
 * 对应表: hexagrams
 */
@Repository
public interface HexagramRepository extends JpaRepository<Hexagram, Integer> {

    /**
     * 根据卦名查询
     */
    Optional<Hexagram> findByName(String name);

    /**
     * 根据六爻符号查询
     */
    Optional<Hexagram> findBySymbol(String symbol);

    /**
     * 根据上卦ID查询所有卦象
     */
    List<Hexagram> findByUpperTrigramId(Integer upperTrigramId);

    /**
     * 根据下卦ID查询所有卦象
     */
    List<Hexagram> findByLowerTrigramId(Integer lowerTrigramId);

    /**
     * 根据上下卦ID查询卦象
     */
    Optional<Hexagram> findByUpperTrigramIdAndLowerTrigramId(
        Integer upperTrigramId,
        Integer lowerTrigramId
    );

    /**
     * 全文搜索卦象内容
     */
    @Query("SELECT h FROM Hexagram h WHERE " +
           "h.judgment LIKE %:keyword% " +
           "OR h.judgmentAnnotation LIKE %:keyword% " +
           "OR h.judgmentTranslation LIKE %:keyword% " +
           "OR h.symbolism LIKE %:keyword% " +
           "OR h.lifeLesson LIKE %:keyword% " +
           "OR h.advice LIKE %:keyword%")
    List<Hexagram> searchByKeyword(@Param("keyword") String keyword);

    /**
     * 根据卦序号查询（兼容旧代码）
     * @param number 卦序号 (1-64)
     */
    default Optional<Hexagram> findByNumber(int number) {
        return findById(number);
    }

    /**
     * 根据二进制表示查询（兼容旧代码）
     * @param binary 二进制符号，如 "111111"
     */
    default Optional<Hexagram> findByBinary(String binary) {
        return findBySymbol(binary);
    }
}
