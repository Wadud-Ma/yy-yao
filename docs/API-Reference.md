# 易经数据查询API参考文档

## 概述

本API提供易经原文、注释、译文的查询功能，支持全文搜索、章节浏览等。

## 基础信息

- **Base URL**: `http://localhost:8080/api`
- **数据格式**: JSON
- **字符编码**: UTF-8

## API端点

### 1. 经文查询

#### 1.1 根据ID查询经文详情

获取指定ID的经文详情，包含原文、所有注释和译文。

**请求**
```http
GET /api/classic/text/{id}
```

**参数**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 经文ID |

**响应示例**
```json
{
  "id": 101,
  "title": "《周易·屯卦》",
  "chapter": "屯卦",
  "content": "象曰：屯，逐也。来遇阻也...",
  "author": "周文王",
  "dynasty": "周",
  "annotations": [
    {
      "id": 1,
      "marker": "[1]",
      "content": "不司与长：犹言"不可以与行为不当的女子相处太长时间"...",
      "type": "注释"
    }
  ],
  "translations": [
    {
      "id": 1,
      "content": "屯卦象征万物初生，艰难困顿。当守正道，不宜轻举妄动。",
      "translator": "朱熹"
    }
  ]
}
```

#### 1.2 根据章节查询经文

**请求**
```http
GET /api/classic/chapter/{chapter}
```

**参数**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| chapter | String | 是 | 章节名称 (如: 屯卦, 乾卦) |

**响应**
```json
[
  {
    "id": 101,
    "title": "《周易·屯卦》",
    "chapter": "屯卦",
    "content": "...",
    "annotations": [...],
    "translations": [...]
  }
]
```

#### 1.3 搜索经文

全文搜索经文内容，包括标题、章节、原文、作者等字段。

**请求**
```http
GET /api/classic/search?keyword={keyword}
```

**参数**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| keyword | String | 是 | 搜索关键词 |

**响应**
```json
[
  {
    "id": 101,
    "title": "《周易·屯卦》",
    "chapter": "屯卦",
    "content": "象曰：屯，逐也...",
    "author": "周文王",
    "dynasty": "周",
    "createdAt": "2026-01-19T10:30:00",
    "updatedAt": "2026-01-19T10:30:00"
  }
]
```

#### 1.4 分页搜索经文

**请求**
```http
GET /api/classic/search/page?keyword={keyword}&page={page}&size={size}
```

**参数**
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| keyword | String | 是 | - | 搜索关键词 |
| page | int | 否 | 0 | 页码(从0开始) |
| size | int | 否 | 20 | 每页数量 |

**响应**
```json
{
  "content": [...],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20
  },
  "totalElements": 64,
  "totalPages": 4,
  "last": false,
  "first": true
}
```

### 2. 注释查询

#### 2.1 查询指定注释

**请求**
```http
GET /api/classic/annotation/{textId}/{marker}
```

**参数**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| textId | Long | 是 | 经文ID |
| marker | String | 是 | 注释标记 (如: [1]) |

**响应**
```json
{
  "id": 1,
  "textId": 101,
  "marker": "[1]",
  "content": "不司与长：犹言"不可以与行为不当的女子相处太长时间"...",
  "type": "注释",
  "createdAt": "2026-01-19T10:30:00"
}
```

### 3. 章节管理

#### 3.1 获取所有章节列表

**请求**
```http
GET /api/classic/chapters
```

**响应**
```json
[
  "乾卦",
  "坤卦",
  "屯卦",
  "蒙卦",
  "需卦",
  ...
]
```

### 4. 数据导入

#### 4.1 从Excel导入数据

**请求**
```http
POST /api/classic/import?filePath={filePath}
```

**参数**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| filePath | String | 是 | Excel文件的完整路径 |

**Excel格式要求**

| 列名 | 说明 | 必填 |
|------|------|------|
| 章节/chapter | 章节名称 | 否 |
| 标题/title | 标题 | 否 |
| 原文/content | 原文内容 | 是 |
| 作者/author | 作者 | 否 |
| 朝代/dynasty | 朝代 | 否 |
| 注释/annotation | 注释内容 | 否 |
| 标记/marker | 注释标记 | 否 |
| 注释类型/type | 注释类型 | 否 |
| 译文/translation | 译文内容 | 否 |
| 译者/translator | 译者 | 否 |

**响应**
```json
{
  "success": true,
  "count": 64,
  "message": "导入成功"
}
```

**错误响应**
```json
{
  "success": false,
  "count": 0,
  "message": "导入失败: 文件不存在"
}
```

## 使用示例

### cURL示例

#### 查询经文详情
```bash
curl http://localhost:8080/api/classic/text/101
```

#### 搜索经文
```bash
curl "http://localhost:8080/api/classic/search?keyword=屯"
```

#### 分页搜索
```bash
curl "http://localhost:8080/api/classic/search/page?keyword=亨&page=0&size=10"
```

#### 查询章节
```bash
curl http://localhost:8080/api/classic/chapter/乾卦
```

#### 导入Excel
```bash
curl -X POST "http://localhost:8080/api/classic/import?filePath=/path/to/易经.xlsx"
```

### JavaScript示例

```javascript
// 查询经文详情
async function getTextDetail(id) {
  const response = await fetch(`http://localhost:8080/api/classic/text/${id}`);
  const data = await response.json();
  console.log(data);
}

// 搜索经文
async function searchTexts(keyword) {
  const response = await fetch(
    `http://localhost:8080/api/classic/search?keyword=${encodeURIComponent(keyword)}`
  );
  const data = await response.json();
  return data;
}

// 分页搜索
async function searchTextsPage(keyword, page = 0, size = 20) {
  const response = await fetch(
    `http://localhost:8080/api/classic/search/page?keyword=${encodeURIComponent(keyword)}&page=${page}&size=${size}`
  );
  const data = await response.json();
  return data;
}
```

### Python示例

```python
import requests

BASE_URL = "http://localhost:8080/api/classic"

# 查询经文详情
def get_text_detail(text_id):
    response = requests.get(f"{BASE_URL}/text/{text_id}")
    return response.json()

# 搜索经文
def search_texts(keyword):
    response = requests.get(f"{BASE_URL}/search", params={"keyword": keyword})
    return response.json()

# 获取章节列表
def get_all_chapters():
    response = requests.get(f"{BASE_URL}/chapters")
    return response.json()

# 使用示例
if __name__ == "__main__":
    # 查询乾卦
    texts = search_texts("乾")
    for text in texts:
        print(f"{text['title']}: {text['content'][:50]}...")
```

## 错误码

| 状态码 | 说明 |
|--------|------|
| 200 | 成功 |
| 404 | 资源未找到 |
| 400 | 请求参数错误 |
| 500 | 服务器内部错误 |

## 数据库访问

开发环境可以通过H2 Console访问数据库：

- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:file:./data/yy-yao`
- Username: `sa`
- Password: (空)

## 注意事项

1. **搜索性能**: 全文搜索使用LIKE查询，大数据量时建议使用专门的全文搜索引擎
2. **Excel导入**: 导入大文件时可能需要较长时间，建议分批导入
3. **字符编码**: 确保Excel文件使用UTF-8编码，避免中文乱码
4. **数据库**: 默认使用H2内存数据库，生产环境建议切换到MySQL或PostgreSQL

## 扩展功能建议

1. **添加分类标签**: 为经文添加标签系统，便于分类检索
2. **版本对比**: 支持不同版本/注释者的对比查看
3. **收藏功能**: 用户可以收藏常用的经文
4. **评论系统**: 允许用户添加个人理解和笔记
5. **导出功能**: 支持导出为PDF、Word等格式
