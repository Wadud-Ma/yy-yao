package com.yy.yao.repository;

import com.yy.yao.entity.HexagramText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 卦象文本Repository
 */
@Repository
public interface HexagramTextRepository extends JpaRepository<HexagramText, Long> {

    /**
     * 根据卦象ID查询文本
     */
    Optional<HexagramText> findByHexagramId(Long hexagramId);

    /**
     * 搜索原文包含关键词的卦象
     */
    @Query("SELECT ht FROM HexagramText ht WHERE " +
           "ht.originalText LIKE %:keyword% OR " +
           "ht.tuanText LIKE %:keyword% OR " +
           "ht.xiangText LIKE %:keyword%")
    List<HexagramText> searchByKeyword(@Param("keyword") String keyword);

    /**
     * 搜索译文包含关键词的卦象
     */
    @Query("SELECT ht FROM HexagramText ht WHERE " +
           "ht.overallTranslation LIKE %:keyword% OR " +
           "ht.tuanTranslation LIKE %:keyword% OR " +
           "ht.xiangTranslation LIKE %:keyword%")
    List<HexagramText> searchTranslationByKeyword(@Param("keyword") String keyword);

    /**
     * 搜索注释包含关键词的卦象
     */
    @Query("SELECT ht FROM HexagramText ht WHERE " +
           "ht.overallAnnotation LIKE %:keyword% OR " +
           "ht.tuanAnnotation LIKE %:keyword% OR " +
           "ht.xiangAnnotation LIKE %:keyword%")
    List<HexagramText> searchAnnotationByKeyword(@Param("keyword") String keyword);

    /**
     * 全文搜索
     */
    @Query("SELECT ht FROM HexagramText ht WHERE " +
           "ht.originalText LIKE %:keyword% OR " +
           "ht.tuanText LIKE %:keyword% OR " +
           "ht.xiangText LIKE %:keyword% OR " +
           "ht.overallTranslation LIKE %:keyword% OR " +
           "ht.tuanTranslation LIKE %:keyword% OR " +
           "ht.xiangTranslation LIKE %:keyword% OR " +
           "ht.overallAnnotation LIKE %:keyword% OR " +
           "ht.tuanAnnotation LIKE %:keyword% OR " +
           "ht.xiangAnnotation LIKE %:keyword% OR " +
           "ht.comprehensiveMeaning LIKE %:keyword% OR " +
           "ht.applicationGuidance LIKE %:keyword%")
    List<HexagramText> fullTextSearch(@Param("keyword") String keyword);
}
