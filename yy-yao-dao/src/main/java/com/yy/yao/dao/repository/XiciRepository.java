package com.yy.yao.dao.repository;

import com.yy.yao.dao.entity.Xici;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系辞传Repository
 */
@Repository
public interface XiciRepository extends JpaRepository<Xici, Integer> {

    /**
     * 根据篇章查询系辞传
     *
     * @param part 篇章 (upper/lower)
     * @return 系辞传列表
     */
    List<Xici> findByPartOrderBySortOrder(Xici.Part part);

    /**
     * 根据篇章和章节查询
     *
     * @param part    篇章
     * @param chapter 章节号
     * @return 系辞传列表
     */
    List<Xici> findByPartAndChapterOrderBySortOrder(Xici.Part part, Integer chapter);

    /**
     * 查询上篇
     */
    default List<Xici> findUpperPart() {
        return findByPartOrderBySortOrder(Xici.Part.upper);
    }

    /**
     * 查询下篇
     */
    default List<Xici> findLowerPart() {
        return findByPartOrderBySortOrder(Xici.Part.lower);
    }
}
