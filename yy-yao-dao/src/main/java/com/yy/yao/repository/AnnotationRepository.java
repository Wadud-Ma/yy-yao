package com.yy.yao.repository;

import com.yy.yao.entity.Annotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 注释表Repository
 */
@Repository
public interface AnnotationRepository extends JpaRepository<Annotation, Long> {

    /**
     * 根据原文ID查询所有注释
     */
    List<Annotation> findByTextId(Long textId);

    /**
     * 根据原文ID和标记查询注释
     */
    Optional<Annotation> findByTextIdAndMarker(Long textId, String marker);

    /**
     * 根据标记查询所有注释
     */
    List<Annotation> findByMarker(String marker);

    /**
     * 根据注释类型查询
     */
    List<Annotation> findByType(String type);

    /**
     * 根据原文ID和类型查询
     */
    List<Annotation> findByTextIdAndType(Long textId, String type);

    /**
     * 搜索注释内容包含关键词
     */
    @Query("SELECT a FROM Annotation a WHERE a.content LIKE %:keyword%")
    List<Annotation> searchByContent(@Param("keyword") String keyword);

    /**
     * 根据原文ID按标记排序查询
     */
    @Query("SELECT a FROM Annotation a WHERE a.textId = :textId ORDER BY a.marker")
    List<Annotation> findByTextIdOrderByMarker(@Param("textId") Long textId);

    /**
     * 统计某原文的注释数量
     */
    long countByTextId(Long textId);

    /**
     * 批量删除某原文的所有注释
     */
    void deleteByTextId(Long textId);
}
