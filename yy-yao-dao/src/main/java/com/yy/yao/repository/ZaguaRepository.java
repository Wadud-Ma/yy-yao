package com.yy.yao.repository;

import com.yy.yao.entity.Zagua;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 杂卦传Repository
 */
@Repository
public interface ZaguaRepository extends JpaRepository<Zagua, Integer> {

    /**
     * 查询所有杂卦传，按排序字段排序
     */
    List<Zagua> findAllByOrderBySortOrder();

    /**
     * 根据卦象ID查询杂卦传
     *
     * @param hexagramId 卦象ID
     * @return 杂卦传列表
     */
    List<Zagua> findByHexagramIdOrderBySortOrder(Integer hexagramId);
}
