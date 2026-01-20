# 数据库初始化步骤

## 1. 创建MySQL数据库

需要先在MySQL中创建 `yy-yao` 数据库。

### 方式1: 使用MySQL客户端命令行

```bash
mysql -h m12183.mars.test.mysql.ljnode.com -P 12183 -u root -pmSBsDMM44OA92MWb -e "CREATE DATABASE IF NOT EXISTS \`yy-yao\` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
```

### 方式2: 使用MySQL客户端交互式

```bash
mysql -h m12183.mars.test.mysql.ljnode.com -P 12183 -u root -pmSBsDMM44OA92MWb
```

然后执行:
```sql
CREATE DATABASE IF NOT EXISTS `yy-yao`
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

SHOW DATABASES LIKE 'yy-yao';
```

### 方式3: 使用MySQL WorkBench或其他GUI工具

1. 连接信息:
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

### 方式4: 执行提供的SQL脚本

```bash
mysql -h m12183.mars.test.mysql.ljnode.com -P 12183 -u root -pmSBsDMM44OA92MWb < ./scripts/create-database.sql
```

## 2. 启动应用

数据库创建完成后，启动应用：

```bash
./mvnw spring-boot:run
```

应用启动时会自动创建以下表：

1. **classic_text** - 原文表
2. **annotation** - 注释表
3. **translation** - 译文表
4. **hexagram_info** - 卦象信息表
5. **hexagram_text** - 卦象文本表
6. **line_text** - 爻辞表
7. **trigram_info** - 八卦信息表

## 3. 验证表创建

```bash
mysql -h m12183.mars.test.mysql.ljnode.com -P 12183 -u root -pmSBsDMM44OA92MWb yy-yao -e "SHOW TABLES;"
```

预期输出:
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
```

## 4. 导入易经数据

应用启动成功后，可以通过API导入数据：

```bash
curl -X POST "http://localhost:8080/api/classic/import?filePath=./book/易经.xlsx"
```

## 5. 测试API

```bash
# 健康检查
curl http://localhost:8080/api/divination/health

# 查看所有章节
curl http://localhost:8080/api/classic/chapters

# 搜索经文
curl "http://localhost:8080/api/classic/search?keyword=乾"
```

## 故障排查

### 问题1: 数据库连接失败

检查MySQL服务是否可访问:
```bash
telnet m12183.mars.test.mysql.ljnode.com 12183
```

或使用nc:
```bash
nc -zv m12183.mars.test.mysql.ljnode.com 12183
```

### 问题2: 字符集问题

确保数据库字符集为utf8mb4:
```sql
SELECT DEFAULT_CHARACTER_SET_NAME, DEFAULT_COLLATION_NAME
FROM information_schema.SCHEMATA
WHERE SCHEMA_NAME = 'yy-yao';
```

### 问题3: 表未自动创建

检查Hibernate配置:
```properties
spring.jpa.hibernate.ddl-auto=update
```

查看启动日志中的SQL语句:
```properties
logging.level.org.hibernate.SQL=DEBUG
```

## 配置说明

当前数据库配置 (application.properties):

```properties
spring.datasource.url=jdbc:mysql://m12183.mars.test.mysql.ljnode.com:12183/yy-yao?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=mSBsDMM44OA92MWb
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

## 注意事项

1. **密码安全**: 生产环境应使用环境变量而非明文密码
2. **自动建表**: `ddl-auto=update` 在生产环境应改为 `validate`
3. **备份**: 定期备份数据库
4. **权限**: 确保root用户有CREATE DATABASE权限
