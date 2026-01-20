# 小程序 API 集成文档

## 概述

本文档描述后端为微信小程序提供的专用 API 接口。这些接口无需认证，简化了小程序的集成流程。

**基础 URL**: `http://localhost:8080/api/mini`

**所有接口无需认证**，适合小程序直接调用。

---

## API 接口列表

### 1. 获取所有卦象

获取全部 64 卦的基本信息（小程序格式）。

**请求**

```
GET /api/mini/hexagrams
```

**响应**

```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "name": "乾",
      "symbol": "111111",
      "statement": "乾: 元亨,利贞。",
      "symbolism": "天、创造、刚健、阳气、领导力、积极进取",
      "interpretation": "乾卦象征天,代表刚健、进取。元亨利贞,万事亨通,利于正道。",
      "advice": "此时正是发奋进取的好时机，但需警惕过度自信。",
      "fullName": "乾为天",
      "unicodeSymbol": "☰☰"
    },
    ...
  ],
  "timestamp": "2026-01-19T10:30:00"
}
```

---

### 2. 根据 ID 获取卦象

根据卦象序号（1-64）获取单个卦象的详细信息。

**请求**

```
GET /api/mini/hexagram/{id}
```

**路径参数**

| 参数 | 类型 | 说明 |
|-----|------|------|
| id | Integer | 卦象序号 (1-64) |

**示例**

```
GET /api/mini/hexagram/1
```

**响应**

```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "name": "乾",
    "symbol": "111111",
    "statement": "乾: 元亨,利贞。",
    "symbolism": "天、创造、刚健、阳气、领导力、积极进取",
    "interpretation": "乾卦象征天,代表刚健、进取。元亨利贞,万事亨通,利于正道。",
    "advice": "此时正是发奋进取的好时机，但需警惕过度自信。",
    "fullName": "乾为天",
    "unicodeSymbol": "☰☰"
  },
  "timestamp": "2026-01-19T10:30:00"
}
```

---

### 3. 根据符号获取卦象

根据六爻二进制符号（6位，0=阴爻，1=阳爻）获取卦象。

**请求**

```
GET /api/mini/hexagram/symbol/{symbol}
```

**路径参数**

| 参数 | 类型 | 说明 |
|-----|------|------|
| symbol | String | 六爻二进制表示，例如 "111111" 表示乾卦 |

**示例**

```
GET /api/mini/hexagram/symbol/111111
```

**响应**

同上（根据 ID 获取卦象）

---

### 4. 执行占卜

执行占卜并返回卦象及解读（支持 AI 智能解读）。

**请求**

```
POST /api/mini/divination
Content-Type: application/json
```

**请求体**

```json
{
  "question": "今年事业运势如何？",
  "method": "COIN",
  "needAiInterpretation": true,
  "customLines": "789678"
}
```

**请求参数说明**

| 字段 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| question | String | 是 | 占卜的问题 |
| method | String | 是 | 起卦方式：COIN（摇卦）/ PLUM（梅花）/ MANUAL（手动）/ TIME（时间） |
| needAiInterpretation | Boolean | 否 | 是否需要 AI 解读，默认 false |
| customLines | String | 否 | 手动指定六爻（6位数字，6=老阴 7=少阳 8=少阴 9=老阳） |

**响应**

```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "timestamp": "2026-01-19T10:30:00",
    "question": "今年事业运势如何？",
    "hexagram": {
      "id": 1,
      "name": "乾",
      "symbol": "111111",
      "statement": "乾: 元亨,利贞。",
      "symbolism": "天、创造、刚健、阳气、领导力、积极进取",
      "interpretation": "乾卦象征天,代表刚健、进取。元亨利贞,万事亨通,利于正道。",
      "advice": "此时正是发奋进取的好时机，但需警惕过度自信。",
      "fullName": "乾为天",
      "unicodeSymbol": "☰☰"
    },
    "changedHexagram": {
      "id": 44,
      "name": "姤",
      "symbol": "111110",
      ...
    },
    "changingLines": [0, 2],
    "method": "COIN",
    "interpretation": "传统解读内容...",
    "aiInterpretation": "AI 智能解读内容..."
  },
  "timestamp": "2026-01-19T10:30:00"
}
```

**响应字段说明**

| 字段 | 类型 | 说明 |
|-----|------|------|
| hexagram | Object | 本卦（原始卦象） |
| changedHexagram | Object | 变卦（有变爻时才有） |
| changingLines | Array<Integer> | 变爻位置数组（0-5，从下到上） |
| method | String | 起卦方式 |
| interpretation | String | 传统解读 |
| aiInterpretation | String | AI 智能解读（需要配置 LLM API） |

---

### 5. 健康检查

检查小程序 API 服务是否正常运行。

**请求**

```
GET /api/mini/health
```

**响应**

```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "status": "UP",
    "timestamp": 1705647000000,
    "message": "小程序 API 正常运行"
  },
  "timestamp": "2026-01-19T10:30:00"
}
```

---

## 数据模型

### MiniProgramHexagramDTO（卦象）

```typescript
interface MiniProgramHexagramDTO {
  id: number;              // 卦序号 (1-64)
  name: string;            // 卦名，如 "乾"
  symbol: string;          // 二进制表示，如 "111111"
  statement: string;       // 卦辞
  symbolism: string;       // 象征意义
  interpretation: string;  // 卦辞解释
  advice: string;          // 行事建议
  fullName: string;        // 完整卦名，如 "乾为天"
  unicodeSymbol: string;   // Unicode 符号，如 "☰☰"
}
```

### MiniProgramDivinationResponse（占卜结果）

```typescript
interface MiniProgramDivinationResponse {
  timestamp: string;                      // 占卜时间
  question: string;                       // 用户问题
  hexagram: MiniProgramHexagramDTO;       // 本卦
  changedHexagram?: MiniProgramHexagramDTO; // 变卦（可选）
  changingLines: number[];                // 变爻位置（0-5）
  method: string;                         // 起卦方式
  interpretation: string;                 // 传统解读
  aiInterpretation?: string;              // AI 解读（可选）
}
```

---

## 错误响应

所有接口在出错时返回统一格式：

```json
{
  "code": 404,
  "message": "未找到卦象: 99",
  "data": null,
  "timestamp": "2026-01-19T10:30:00"
}
```

**常见错误码**

| 错误码 | 说明 |
|-------|------|
| 400 | 请求参数错误 |
| 404 | 资源未找到 |
| 500 | 服务器内部错误 |

---

## 小程序集成示例

### 获取所有卦象

```javascript
// 小程序代码示例
wx.request({
  url: 'http://localhost:8080/api/mini/hexagrams',
  method: 'GET',
  success: (res) => {
    if (res.data.code === 200) {
      console.log('卦象列表:', res.data.data);
      // 处理卦象数据
    }
  }
});
```

### 执行占卜

```javascript
wx.request({
  url: 'http://localhost:8080/api/mini/divination',
  method: 'POST',
  header: {
    'content-type': 'application/json'
  },
  data: {
    question: '今年事业运势如何？',
    method: 'COIN',
    needAiInterpretation: true
  },
  success: (res) => {
    if (res.data.code === 200) {
      const result = res.data.data;
      console.log('占卜结果:', result);
      console.log('本卦:', result.hexagram.name);
      console.log('AI解读:', result.aiInterpretation);
    }
  }
});
```

---

## 配置说明

### 启用 AI 解读

要启用 AI 智能解读功能，需要在后端配置 LLM API：

**application.properties**

```properties
# OpenAI
llm.api.url=https://api.openai.com/v1/chat/completions
llm.api.key=your-api-key
llm.model=gpt-3.5-turbo

# 或使用阿里通义千问
llm.api.url=https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions
llm.api.key=your-dashscope-key
llm.model=qwen-turbo
```

### 跨域配置

后端已配置允许所有来源的跨域请求（CORS），小程序可以直接调用。

### 服务器域名配置

小程序正式版需要在微信公众平台配置服务器域名：

1. 登录微信公众平台
2. 进入"开发" -> "开发管理" -> "开发设置"
3. 在"服务器域名"中添加：
   - request合法域名：`https://your-domain.com`

**注意**：小程序只能请求 HTTPS 域名（开发工具可以关闭校验）。

---

## 下一步

1. **部署后端**：将后端部署到生产服务器
2. **配置 HTTPS**：为后端配置 SSL 证书
3. **配置域名**：在微信公众平台添加服务器域名
4. **测试集成**：使用小程序开发工具测试所有接口
5. **启用 AI**：配置 LLM API 密钥启用智能解读

---

## 技术支持

如有问题，请参考：
- 后端项目：`/Users/juvenile/code/github.com/spring-ai-community/yy-yao`
- API 参考文档：`docs/API-Reference.md`
- 小程序项目：`/Users/juvenile/WeChatProjects/yunguashi`
