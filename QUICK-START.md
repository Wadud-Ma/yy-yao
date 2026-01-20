# 快速启动指南 - 易经卜卦系统

## ⚠️ 首次启动前必须操作

### 步骤1: 创建MySQL数据库

**方式A: 使用MySQL命令行客户端**

```bash
mysql -h m12183.mars.test.mysql.ljnode.com -P 12183 -u root -pmSBsDMM44OA92MWb -e "CREATE DATABASE IF NOT EXISTS \`yy-yao\` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
```

**方式B: 使用DBeaver/Navicat等GUI工具**

1. 创建新连接:
   - Host: `m12183.mars.test.mysql.ljnode.com`
   - Port: `12183`
   - Username: `root`
   - Password: `mSBsDMM44OA92MWb`

2. 执行SQL:
```sql
CREATE DATABASE IF NOT EXISTS `yy-yao`
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
```

**方式C: 使用MySQL Workbench**

1. 新建连接，填入上述信息
2. 右键 → Create Schema
3. Schema Name: `yy-yao`
4. Charset: `utf8mb4`
5. Collation: `utf8mb4_unicode_ci`

---

### 步骤2: 启动应用

数据库创建完成后：

```bash
./mvnw spring-boot:run
```

**启动成功标志**:
```
Started YyYaoApplication in X.XXX seconds
```

应用会自动创建以下7个表:
- ✅ classic_text (原文表)
- ✅ annotation (注释表)
- ✅ translation (译文表)
- ✅ hexagram_info (卦象信息)
- ✅ hexagram_text (卦象文本)
- ✅ line_text (爻辞)
- ✅ trigram_info (八卦信息)

---

### 步骤3: 验证启动

**健康检查**:
```bash
curl http://localhost:8080/api/divination/health
# 预期输出: 易经卜卦服务运行正常
```

**查看章节列表**:
```bash
curl http://localhost:8080/api/classic/chapters
# 预期输出: [] (初始为空)
```

---

### 步骤4: 导入易经数据

```bash
curl -X POST "http://localhost:8080/api/classic/import?filePath=./book/易经.xlsx"
```

**预期响应**:
```json
{
  "success": true,
  "count": 64,
  "message": "导入成功"
}
```

---

## 🎯 核心功能测试

### 1. 卜卦功能

```bash
curl -X POST http://localhost:8080/api/divination/consult \
  -H "Content-Type: application/json" \
  -d '{
    "question": "今年事业运势如何？",
    "method": "COIN",
    "needAiInterpretation": false
  }'
```

### 2. 搜索经文

```bash
curl "http://localhost:8080/api/classic/search?keyword=乾"
```

### 3. 查询章节

```bash
curl http://localhost:8080/api/classic/chapter/乾卦
```

### 4. 查询经文详情

```bash
curl http://localhost:8080/api/classic/text/1
```

---

## 📊 验证数据库表

**使用MySQL客户端**:
```bash
mysql -h m12183.mars.test.mysql.ljnode.com -P 12183 -u root -pmSBsDMM44OA92MWb yy-yao -e "SHOW TABLES;"
```

**预期输出**:
```
+-------------------+
| Tables_in_yy-yao  |
+-------------------+
| annotation        |
| classic_text      |
| hexagram_info     |
| hexagram_text     |
| line_text         |
| translation       |
| trigram_info      |
+-------------------+
7 rows in set
```

---

## 🔧 故障排查

### 问题1: 启动失败 - Unknown database 'yy-yao'

**原因**: 数据库未创建

**解决**: 执行步骤1创建数据库

### 问题2: 无法连接MySQL

**检查连接**:
```bash
telnet m12183.mars.test.mysql.ljnode.com 12183
# 或
nc -zv m12183.mars.test.mysql.ljnode.com 12183
```

**检查防火墙**: 确保端口12183开放

### 问题3: AI功能不可用

**说明**: Spring AI与Spring Boot 4.0存在兼容性问题，AI功能暂时禁用

**解决**: 设置 `needAiInterpretation: false`，使用传统解卦

### 问题4: Excel导入失败

**检查**:
1. 文件路径是否正确
2. 文件格式是否为 .xlsx
3. 查看应用日志获取详细错误

---

## 📚 完整文档

- **API文档**: `docs/API-Reference.md`
- **数据库设计**: `docs/database-design.md`
- **使用指南**: `docs/Database-Usage.md`
- **初始化详细步骤**: `scripts/SETUP.md`

---

## 🎉 快速命令总结

```bash
# 1. 创建数据库 (使用MySQL客户端)
mysql -h m12183.mars.test.mysql.ljnode.com -P 12183 -u root -pmSBsDMM44OA92MWb \
  -e "CREATE DATABASE IF NOT EXISTS \`yy-yao\` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# 2. 启动应用
./mvnw spring-boot:run

# 3. 健康检查 (新终端)
curl http://localhost:8080/api/divination/health

# 4. 导入数据
curl -X POST "http://localhost:8080/api/classic/import?filePath=./book/易经.xlsx"

# 5. 测试卜卦
curl -X POST http://localhost:8080/api/divination/consult \
  -H "Content-Type: application/json" \
  -d '{"question":"测试","method":"COIN","needAiInterpretation":false}'
```

---

## ⚠️ 重要提示

1. ✅ 数据库字符集必须是 **utf8mb4**，否则中文会乱码
2. ✅ 首次启动时表会自动创建，需要等待几秒
3. ⚠️ AI功能暂时不可用（版本兼容性问题）
4. ✅ 所有其他功能（卜卦、查询、导入）完全正常

---

**需要帮助？** 查看 `scripts/SETUP.md` 获取详细步骤
