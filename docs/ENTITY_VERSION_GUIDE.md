# 实体类版本说明

## 概述

项目中存在两套实体类系统，分别对应不同的表结构和使用场景。

---

## 实体类版本对照表

### 占卜记录实体

| 实体类 | 表名 | 说明 | 状态 |
|--------|------|------|------|
| `DivinationRecord` | `divination_record` (单数) | 旧版，使用openid，适用于微信小程序 | ✅ 当前使用 |
| `DivinationRecordV2` | `divination_records` (复数) | 新版，使用userId，功能更完整 | 📦 已准备，待迁移 |

### 卦象实体

| 实体类 | 表名 | 说明 | 状态 |
|--------|------|------|------|
| `HexagramInfo` | `hexagram_info` | 旧版V1 | ⚠️  保留兼容 |
| `HexagramText` | `hexagram_text` | 旧版V1 | ⚠️  保留兼容 |
| `LineText` | `line_text` | 旧版V1 | ⚠️  保留兼容 |
| `TrigramInfo` | `trigram_info` | 旧版V1 | ⚠️  保留兼容 |
| **Hexagram** | **hexagrams** | **新版V2/V3，推荐使用** | ✅ 推荐 |
| **HexagramLine** | **hexagram_lines** | **新版V2/V3，推荐使用** | ✅ 推荐 |
| **Trigram** | **trigrams** | **新版V2/V3，推荐使用** | ✅ 推荐 |

### 十翼实体（新增）

| 实体类 | 表名 | 说明 | 状态 |
|--------|------|------|------|
| `Wenyan` | `wenyan` | 文言传 | ✅ V3新增 |
| `Xici` | `xici` | 系辞传 | ✅ V3新增 |
| `Xugua` | `xugua` | 序卦传 | ✅ V3新增 |
| `Shuogua` | `shuogua` | 说卦传 | ✅ V3新增 |
| `Zagua` | `zagua` | 杂卦传 | ✅ V3新增 |

---

## 详细说明

### 1. DivinationRecord (旧版)

**表名**: `divination_record` (单数)

**核心字段**:
```java
- openid (String) - 微信小程序用户标识
- originalHexagramId (Integer) - 本卦ID
- originalHexagramName (String) - 本卦名称
- originalHexagramBinary (String) - 本卦二进制
- changingLines (String) - JSON数组
- linesDetail (String) - JSON对象
- isFavorite (Boolean) - 是否收藏
```

**适用场景**:
- 微信小程序
- 现有业务代码
- 使用openid作为用户标识

**Repository**: `DivinationRecordRepository`

---

### 2. DivinationRecordV2 (新版)

**表名**: `divination_records` (复数)

**核心字段**:
```java
- userId (Long) - 统一用户ID
- method (DivinationMethod枚举) - 占卜方式
- category (String) - 问题分类
- lines (String) - JSON: [7,8,9,7,6,8]
- changingLineCount (Integer) - 变爻数量
- aiSummary (String) - AI解读摘要
- aiModel (String) - AI模型名称
- userRating (Integer) - 用户评分 1-5
- isAccurate (Boolean) - 是否应验
- status (RecordStatus枚举) - 记录状态
```

**适用场景**:
- 新功能开发
- 多平台用户系统
- 更完整的功能需求（评分、反馈、应验等）

**Repository**: `DivinationRecordV2Repository`

**新增功能**:
- ✅ 占卜方式枚举（铜钱、梅花易数等）
- ✅ 问题分类
- ✅ 完整的6爻数值记录
- ✅ 用户评分和反馈
- ✅ 应验情况跟踪
- ✅ 记录状态管理（活跃/归档/已删除）
- ✅ AI模型信息追踪
- ✅ 更细粒度的时间记录

---

## 使用建议

### 现有代码（不需要改动）

如果你的代码已经在使用旧版实体类，**无需修改**，继续使用：

```java
// 旧版 - 继续使用
@Autowired
private DivinationRecordRepository recordRepository;

@Autowired
private HexagramInfoRepository hexagramInfoRepository;

@Autowired
private LineTextRepository lineTextRepository;
```

### 新功能开发（推荐使用新版）

如果你要开发新功能，推荐使用新版实体类：

```java
// 新版 - 推荐用于新功能
@Autowired
private DivinationRecordV2Repository recordV2Repository;

@Autowired
private HexagramRepository hexagramRepository;

@Autowired
private HexagramLineRepository hexagramLineRepository;

@Autowired
private TrigramRepository trigramRepository;

// 十翼
@Autowired
private WenyanRepository wenyanRepository;

@Autowired
private XiciRepository xiciRepository;
```

---

## 迁移指南

### 从旧版迁移到新版

如果你想将现有代码迁移到新版实体类：

#### 步骤1: 字段映射

```java
// 旧版字段 → 新版字段
openid → userId (需要映射转换)
isFavorite → isFavorited
divinationMethod → method
divinationTime → divinationDate + divinationTime
```

#### 步骤2: 数据迁移SQL

```sql
-- 从旧表迁移到新表
INSERT INTO divination_records (
    user_id,
    method,
    question,
    lines,
    original_hexagram_id,
    changed_hexagram_id,
    changing_lines,
    ai_interpretation,
    is_favorited,
    status,
    created_at
)
SELECT
    -- openid需要映射到user_id，这里假设有users表
    u.id AS user_id,
    dr.divination_method AS method,
    dr.question,
    dr.lines_detail AS lines,
    dr.original_hexagram_id,
    dr.changed_hexagram_id,
    dr.changing_lines,
    dr.ai_interpretation,
    dr.is_favorite AS is_favorited,
    'active' AS status,
    dr.created_at
FROM divination_record dr
LEFT JOIN users u ON u.openid = dr.openid;
```

#### 步骤3: 更新Service代码

```java
// 旧代码
public DivinationRecord save(String openid, DivinationResult result) {
    return DivinationRecord.builder()
        .openid(openid)
        .question(result.getQuestion())
        .build();
}

// 新代码
public DivinationRecordV2 save(Long userId, DivinationResult result) {
    return DivinationRecordV2.builder()
        .userId(userId)
        .question(result.getQuestion())
        .method(DivinationRecordV2.DivinationMethod.coin)
        .status(DivinationRecordV2.RecordStatus.active)
        .build();
}
```

---

## 版本演进历史

### V1 (旧版)
- `HexagramInfo`, `LineText`, `TrigramInfo`
- `DivinationRecord` (单数表名)
- 面向微信小程序
- 基础功能

### V2 (过渡版)
- `Hexagram`, `HexagramLine`, `Trigram`
- 规范化设计
- 支持卦辞、象辞、爻辞

### V3 (完整版)
- 包含V2所有内容
- 新增十翼实体 (`Wenyan`, `Xici`, `Xugua`, `Shuogua`, `Zagua`)
- `DivinationRecordV2` - 功能更完整的占卜记录
- 支持完整的易经内容

---

## 常见问题

### Q1: 为什么不删除旧版实体类？

**答**: 为了保持向后兼容，避免影响现有业务代码。旧版实体类仍然可以正常使用。

### Q2: 新老版本可以同时使用吗？

**答**: 可以。它们对应不同的数据库表，互不冲突。但建议新功能统一使用新版。

### Q3: DivinationRecord 和 DivinationRecordV2 有什么本质区别？

**答**: 主要区别：
1. **用户标识**: 旧版用openid（微信），新版用userId（通用）
2. **功能完整性**: 新版支持评分、反馈、应验跟踪等
3. **状态管理**: 新版有完整的状态枚举
4. **表名**: 旧版单数，新版复数

### Q4: 如何选择使用哪个版本？

**答**:
- **修改现有代码**: 使用旧版，保持兼容
- **新功能开发**: 使用新版，功能更强大
- **微信小程序**: 旧版更方便（直接用openid）
- **多平台应用**: 新版更合适（统一userId）

---

## 总结

- ✅ **旧版继续使用**: 现有代码无需改动
- ✅ **新版推荐使用**: 新功能开发建议使用V2/V3
- ✅ **共存无冲突**: 两套系统可以并存
- ✅ **逐步迁移**: 可以按需逐步迁移到新版

---

**文档版本**: 1.0
**最后更新**: 2026-01-26
**维护者**: Claude Code
