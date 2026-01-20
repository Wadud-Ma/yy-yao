# API示例

这个文件包含了易经卜卦服务的各种API调用示例。

## 1. 基础卜卦 - 铜钱法

```bash
curl -X POST http://localhost:8080/api/divination/consult \
  -H "Content-Type: application/json" \
  -d '{
    "question": "今年事业运势如何？",
    "method": "COIN",
    "needAiInterpretation": true
  }'
```

## 2. 数字法卜卦

```bash
curl -X POST http://localhost:8080/api/divination/consult \
  -H "Content-Type: application/json" \
  -d '{
    "question": "近期适合换工作吗？",
    "method": "NUMBER",
    "needAiInterpretation": true
  }'
```

## 3. 时间起卦法

```bash
curl -X POST http://localhost:8080/api/divination/consult \
  -H "Content-Type: application/json" \
  -d '{
    "question": "这次投资是否可行？",
    "method": "TIME",
    "needAiInterpretation": false
  }'
```

## 4. 自定义卦象

如果你已经通过其他方式得到了卦象，可以直接输入:

```bash
curl -X POST http://localhost:8080/api/divination/consult \
  -H "Content-Type: application/json" \
  -d '{
    "question": "感情发展如何？",
    "customLines": "777777",
    "needAiInterpretation": true
  }'
```

### 爻值说明
- 6: 老阴(动爻，会变化)
- 7: 少阳(静爻)
- 8: 少阴(静爻)
- 9: 老阳(动爻，会变化)

### 常见卦象示例
- "777777": 乾卦(纯阳)
- "888888": 坤卦(纯阴)
- "777888": 泰卦(天地交泰)
- "888777": 否卦(天地不交)

## 5. 查询单个卦象

```bash
# 查询乾卦
curl http://localhost:8080/api/divination/hexagram/1

# 查询坤卦
curl http://localhost:8080/api/divination/hexagram/2

# 查询泰卦
curl http://localhost:8080/api/divination/hexagram/11
```

## 6. 获取所有卦象列表

```bash
curl http://localhost:8080/api/divination/hexagrams
```

## 7. 健康检查

```bash
curl http://localhost:8080/api/divination/health
```

## 响应示例

### 成功的卜卦响应

```json
{
  "timestamp": "2026-01-19T10:30:00",
  "question": "今年事业运势如何？",
  "originalHexagram": {
    "number": 1,
    "name": "乾",
    "symbol": "☰☰",
    "binary": "111111",
    "upperTrigram": "乾",
    "lowerTrigram": "乾",
    "statement": "乾: 元亨,利贞。",
    "interpretation": "乾卦象征天,代表刚健、进取。元亨利贞,万事亨通,利于正道。",
    "lineStatements": [
      {
        "position": 1,
        "name": "初九",
        "text": "潜龙勿用。",
        "interpretation": "时机未到,应当潜藏待时。"
      }
      // ... 其他爻辞
    ]
  },
  "changedHexagram": {
    "number": 44,
    "name": "姤",
    // ... 变卦信息
  },
  "changingLines": [5],
  "lines": [
    {
      "position": 1,
      "yang": true,
      "changing": false,
      "value": 7
    },
    {
      "position": 2,
      "yang": true,
      "changing": false,
      "value": 7
    },
    // ... 其他爻
    {
      "position": 5,
      "yang": true,
      "changing": true,
      "value": 9
    }
  ],
  "method": "COIN",
  "interpretation": "【本卦】乾卦\n乾: 元亨,利贞。\n解释: 乾卦象征天...",
  "aiInterpretation": "根据您得到的乾卦，当前事业运势整体向好..."
}
```

## 使用技巧

### 1. 不使用AI解读 (更快、免费)

```bash
curl -X POST http://localhost:8080/api/divination/consult \
  -H "Content-Type: application/json" \
  -d '{
    "question": "测试问题",
    "method": "COIN",
    "needAiInterpretation": false
  }'
```

### 2. 使用jq格式化输出

```bash
curl -X POST http://localhost:8080/api/divination/consult \
  -H "Content-Type: application/json" \
  -d '{
    "question": "今年事业运势如何？",
    "method": "COIN",
    "needAiInterpretation": false
  }' | jq '.'
```

### 3. 只查看卦名和卦辞

```bash
curl -X POST http://localhost:8080/api/divination/consult \
  -H "Content-Type: application/json" \
  -d '{
    "question": "今年事业运势如何？",
    "method": "COIN",
    "needAiInterpretation": false
  }' | jq '{
    question: .question,
    hexagram: .originalHexagram.name,
    statement: .originalHexagram.statement,
    interpretation: .interpretation
  }'
```

## Python示例

```python
import requests
import json

url = "http://localhost:8080/api/divination/consult"
headers = {"Content-Type": "application/json"}

data = {
    "question": "今年事业运势如何？",
    "method": "COIN",
    "needAiInterpretation": True
}

response = requests.post(url, headers=headers, json=data)
result = response.json()

print(f"本卦: {result['originalHexagram']['name']}")
print(f"卦辞: {result['originalHexagram']['statement']}")
if result.get('aiInterpretation'):
    print(f"\nAI解读:\n{result['aiInterpretation']}")
```

## JavaScript/Node.js示例

```javascript
const fetch = require('node-fetch');

async function consult(question) {
  const response = await fetch('http://localhost:8080/api/divination/consult', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      question: question,
      method: 'COIN',
      needAiInterpretation: true
    })
  });

  const result = await response.json();

  console.log(`本卦: ${result.originalHexagram.name}`);
  console.log(`卦辞: ${result.originalHexagram.statement}`);

  if (result.aiInterpretation) {
    console.log(`\nAI解读:\n${result.aiInterpretation}`);
  }
}

consult('今年事业运势如何？');
```

## 常见问题

### Q: AI解读失败怎么办？
A: 确保设置了正确的OPENAI_API_KEY环境变量，或者设置 `needAiInterpretation: false` 只使用传统解读。

### Q: 如何理解动爻？
A: 动爻代表变化的爻位。老阳(9)和老阴(6)为动爻，会发生阴阳转换，形成变卦。

### Q: 本卦和变卦如何解读？
A: 本卦代表当前状态，变卦代表未来趋势。有动爻时需要结合两者综合分析。

### Q: 支持哪些卜卦方法？
A: 支持COIN(铜钱法)、YARROW(蓍草法)、NUMBER(数字法)、TIME(时间起卦法)四种方法。
