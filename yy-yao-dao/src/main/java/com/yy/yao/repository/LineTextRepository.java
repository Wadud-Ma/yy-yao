package com.yy.yao.repository;

import com.yy.yao.entity.LineText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 爻辞文本Repository
 */
@Repository
public interface LineTextRepository extends JpaRepository<LineText, Long> {

    /**
     * 根据卦象ID查询所有爻辞
     */
    @Query("SELECT lt FROM LineText lt WHERE lt.hexagramId = :hexagramId ORDER BY lt.position")
    List<LineText> findByHexagramIdOrderByPosition(@Param("hexagramId") Long hexagramId);

    /**
     * 根据卦象ID和爻位查询
     */
    Optional<LineText> findByHexagramIdAndPosition(Long hexagramId, Integer position);

    /**
     * 查询所有阳爻
     */
    List<LineText> findByIsYang(Boolean isYang);

    /**
     * 根据爻名查询
     */
    List<LineText> findByLineName(String lineName);

    /**
     * 搜索爻辞原文包含关键词
     */
    @Query("SELECT lt FROM LineText lt WHERE " +
           "lt.originalText LIKE %:keyword% OR " +
           "lt.xiangText LIKE %:keyword%")
    List<LineText> searchByKeyword(@Param("keyword") String keyword);

    /**
     * 搜索爻辞译文包含关键词
     */
    @Query("SELECT lt FROM LineText lt WHERE " +
           "lt.translation LIKE %:keyword% OR " +
           "lt.xiangTranslation LIKE %:keyword%")
    List<LineText> searchTranslationByKeyword(@Param("keyword") String keyword);

    /**
     * 全文搜索爻辞
     */
    @Query("SELECT lt FROM LineText lt WHERE " +
           "lt.originalText LIKE %:keyword% OR " +
           "lt.xiangText LIKE %:keyword% OR " +
           "lt.translation LIKE %:keyword% OR " +
           "lt.annotation LIKE %:keyword% OR " +
           "lt.meaning LIKE %:keyword% OR " +
           "lt.application LIKE %:keyword%")
    List<LineText> fullTextSearch(@Param("keyword") String keyword);

    /**
     * 根据卦象ID批量删除
     */
    void deleteByHexagramId(Long hexagramId);
}
