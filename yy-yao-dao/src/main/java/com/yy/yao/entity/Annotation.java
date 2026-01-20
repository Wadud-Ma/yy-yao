package com.yy.yao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 注释表 - 存储对原文中特定标记的注释
 *
 * 示例:
 * text_id: 101
 * marker: [1]
 * content: 不司与长：犹言"不可以与行为不当的女子相处太长时间"...
 * type: 注释/评论
 */
@Entity
@Table(name = "annotation", indexes = {
        @Index(name = "idx_annotation_text_id", columnList = "text_id"),
        @Index(name = "idx_annotation_marker", columnList = "marker"),
        @Index(name = "idx_annotation_type", columnList = "type")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Annotation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联的原文ID
     */
    @Column(name = "text_id", nullable = false)
    private Long textId;

    /**
     * 标记符号 (如: [1], [2], 【注】)
     */
    @Column(nullable = false, length = 20)
    private String marker;

    /**
     * 注释内容
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    /**
     * 注释类型 (注释/评论/考证/引用等)
     */
    @Column(length = 20)
    private String type;

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
