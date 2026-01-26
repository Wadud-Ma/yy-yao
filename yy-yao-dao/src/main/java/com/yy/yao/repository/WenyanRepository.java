package com.yy.yao.repository;

import com.yy.yao.entity.Wenyan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文言传Repository
 */
@Repository
public interface WenyanRepository extends JpaRepository<Wenyan, Integer> {

    /**
     * 根据卦象ID查询文言传
     *
     * @param hexagramId 卦象ID (1=乾, 2=坤)
     * @return 文言传列表
     */
    List<Wenyan> findByHexagramIdOrderBySortOrder(Integer hexagramId);

    /**
     * 查询乾卦文言传
     */
    default List<Wenyan> findQianWenyan() {
        return findByHexagramIdOrderBySortOrder(1);
    }

    /**
     * 查询坤卦文言传
     */
    default List<Wenyan> findKunWenyan() {
        return findByHexagramIdOrderBySortOrder(2);
    }
}
