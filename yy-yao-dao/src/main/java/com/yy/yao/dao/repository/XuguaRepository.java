package com.yy.yao.dao.repository;

import com.yy.yao.dao.entity.Xugua;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 序卦传Repository
 */
@Repository
public interface XuguaRepository extends JpaRepository<Xugua, Integer> {

    /**
     * 查询所有序卦传，按排序字段排序
     */
    List<Xugua> findAllByOrderBySortOrder();

    /**
     * 根据卦象ID查询序卦传
     *
     * @param hexagramId 卦象ID
     * @return 序卦传列表
     */
    List<Xugua> findByHexagramIdOrderBySortOrder(Integer hexagramId);
}
