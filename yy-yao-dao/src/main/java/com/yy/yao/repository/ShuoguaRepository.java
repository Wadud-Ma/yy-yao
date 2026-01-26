package com.yy.yao.repository;

import com.yy.yao.entity.Shuogua;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 说卦传Repository
 */
@Repository
public interface ShuoguaRepository extends JpaRepository<Shuogua, Integer> {

    /**
     * 查询所有说卦传，按排序字段排序
     */
    List<Shuogua> findAllByOrderBySortOrder();

    /**
     * 根据章节查询说卦传
     *
     * @param chapter 章节号
     * @return 说卦传列表
     */
    List<Shuogua> findByChapterOrderBySortOrder(Integer chapter);
}
