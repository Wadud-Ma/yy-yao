package com.yy.yao.dao.repository;

import com.yy.yao.dao.entity.HexagramLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 爻辞数据访问接口
 */
@Repository
public interface HexagramLineRepository extends JpaRepository<HexagramLine, Integer> {

    /**
     * 查询某个卦象的所有爻辞（按位置排序）
     *
     * @param hexagramId 卦象ID
     * @return 爻辞列表（从下到上）
     */
    List<HexagramLine> findByHexagramIdOrderByPosition(Integer hexagramId);

    /**
     * 查询某个卦象的特定爻位
     *
     * @param hexagramId 卦象ID
     * @param position   爻位 (1-6)
     * @return 爻辞信息
     */
    Optional<HexagramLine> findByHexagramIdAndPosition(Integer hexagramId, Integer position);

    /**
     * 查询特定类型的爻
     *
     * @param type 爻性类型
     * @return 爻辞列表
     */
    List<HexagramLine> findByType(HexagramLine.LineType type);

    /**
     * 统计某个卦象的爻辞数量
     *
     * @param hexagramId 卦象ID
     * @return 爻辞数量
     */
    long countByHexagramId(Integer hexagramId);

    /**
     * 全文搜索爻辞
     *
     * @param keyword 关键词
     * @return 匹配的爻辞列表
     */
    @Query("SELECT hl FROM HexagramLine hl " +
           "WHERE hl.lineText LIKE %:keyword% " +
           "OR hl.lineAnnotation LIKE %:keyword% " +
           "OR hl.lineTranslation LIKE %:keyword%")
    List<HexagramLine> searchByKeyword(@Param("keyword") String keyword);

    /**
     * 删除某个卦象的所有爻辞
     *
     * @param hexagramId 卦象ID
     */
    void deleteByHexagramId(Integer hexagramId);

    /**
     * 批量查询多个卦象的爻辞
     *
     * @param hexagramIds 卦象ID列表
     * @return 爻辞列表
     */
    @Query("SELECT hl FROM HexagramLine hl " +
           "WHERE hl.hexagramId IN :hexagramIds " +
           "ORDER BY hl.hexagramId, hl.position")
    List<HexagramLine> findByHexagramIdIn(@Param("hexagramIds") List<Integer> hexagramIds);
}
