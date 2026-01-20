package com.yy.yao.repository;

import com.yy.yao.entity.Translation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 白话释义表Repository
 */
@Repository
public interface TranslationRepository extends JpaRepository<Translation, Long> {

    /**
     * 根据原文ID查询所有翻译
     */
    List<Translation> findByTextId(Long textId);

    /**
     * 根据原文ID查询第一个翻译
     */
    Optional<Translation> findFirstByTextId(Long textId);

    /**
     * 根据译者查询
     */
    List<Translation> findByTranslator(String translator);

    /**
     * 根据原文ID和译者查询
     */
    Optional<Translation> findByTextIdAndTranslator(Long textId, String translator);

    /**
     * 搜索翻译内容包含关键词
     */
    @Query("SELECT t FROM Translation t WHERE t.content LIKE %:keyword%")
    List<Translation> searchByContent(@Param("keyword") String keyword);

    /**
     * 统计某原文的翻译数量
     */
    long countByTextId(Long textId);

    /**
     * 批量删除某原文的所有翻译
     */
    void deleteByTextId(Long textId);

    /**
     * 获取所有译者列表
     */
    @Query("SELECT DISTINCT t.translator FROM Translation t WHERE t.translator IS NOT NULL ORDER BY t.translator")
    List<String> findAllTranslators();
}
