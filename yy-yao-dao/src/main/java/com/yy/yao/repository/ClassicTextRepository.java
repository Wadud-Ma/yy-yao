package com.yy.yao.repository;

import com.yy.yao.entity.ClassicText;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 原文表Repository
 */
@Repository
public interface ClassicTextRepository extends JpaRepository<ClassicText, Long> {

    /**
     * 根据标题查询
     */
    Optional<ClassicText> findByTitle(String title);

    /**
     * 根据章节查询
     */
    List<ClassicText> findByChapter(String chapter);

    /**
     * 根据章节模糊查询
     */
    List<ClassicText> findByChapterContaining(String chapter);

    /**
     * 根据朝代查询
     */
    List<ClassicText> findByDynasty(String dynasty);

    /**
     * 根据作者查询
     */
    List<ClassicText> findByAuthor(String author);

    /**
     * 搜索标题包含关键词
     */
    List<ClassicText> findByTitleContaining(String keyword);

    /**
     * 搜索内容包含关键词
     */
    @Query("SELECT ct FROM ClassicText ct WHERE ct.content LIKE %:keyword%")
    List<ClassicText> searchByContent(@Param("keyword") String keyword);

    /**
     * 全文搜索
     */
    @Query("SELECT ct FROM ClassicText ct WHERE " +
           "ct.title LIKE %:keyword% OR " +
           "ct.chapter LIKE %:keyword% OR " +
           "ct.content LIKE %:keyword% OR " +
           "ct.author LIKE %:keyword%")
    List<ClassicText> fullTextSearch(@Param("keyword") String keyword);

    /**
     * 分页全文搜索
     */
    @Query("SELECT ct FROM ClassicText ct WHERE " +
           "ct.title LIKE %:keyword% OR " +
           "ct.chapter LIKE %:keyword% OR " +
           "ct.content LIKE %:keyword% OR " +
           "ct.author LIKE %:keyword%")
    Page<ClassicText> fullTextSearchPage(@Param("keyword") String keyword, Pageable pageable);

    /**
     * 获取所有章节列表
     */
    @Query("SELECT DISTINCT ct.chapter FROM ClassicText ct WHERE ct.chapter IS NOT NULL ORDER BY ct.chapter")
    List<String> findAllChapters();

    /**
     * 统计某章节的文本数量
     */
    long countByChapter(String chapter);
}
