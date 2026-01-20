#!/bin/bash

# 易经卜卦系统 - 数据库初始化脚本

# MySQL连接信息
MYSQL_HOST="m12183.mars.test.mysql.ljnode.com"
MYSQL_PORT="12183"
MYSQL_USER="root"
MYSQL_PASSWORD="mSBsDMM44OA92MWb"
DATABASE_NAME="yy-yao"

echo "=========================================="
echo "易经卜卦系统 - 数据库初始化"
echo "=========================================="
echo ""
echo "MySQL服务器: $MYSQL_HOST:$MYSQL_PORT"
echo "数据库名称: $DATABASE_NAME"
echo ""

# 检查MySQL客户端是否可用
if ! command -v mysql &> /dev/null; then
    echo "❌ MySQL客户端未安装"
    echo ""
    echo "请手动执行以下SQL语句:"
    echo ""
    echo "CREATE DATABASE IF NOT EXISTS \`$DATABASE_NAME\`"
    echo "CHARACTER SET utf8mb4"
    echo "COLLATE utf8mb4_unicode_ci;"
    echo ""
    echo "连接信息:"
    echo "  Host: $MYSQL_HOST"
    echo "  Port: $MYSQL_PORT"
    echo "  User: $MYSQL_USER"
    echo "  Password: $MYSQL_PASSWORD"
    echo ""
    exit 1
fi

# 测试MySQL连接
echo "🔍 测试MySQL连接..."
if mysql -h "$MYSQL_HOST" -P "$MYSQL_PORT" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" -e "SELECT 1;" &> /dev/null; then
    echo "✅ MySQL连接成功"
else
    echo "❌ MySQL连接失败"
    echo ""
    echo "请检查:"
    echo "1. 网络连接"
    echo "2. MySQL服务器是否运行"
    echo "3. 用户名和密码是否正确"
    exit 1
fi

# 创建数据库
echo ""
echo "📦 创建数据库 $DATABASE_NAME ..."
mysql -h "$MYSQL_HOST" -P "$MYSQL_PORT" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" <<EOF
CREATE DATABASE IF NOT EXISTS \`$DATABASE_NAME\`
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
EOF

if [ $? -eq 0 ]; then
    echo "✅ 数据库创建成功"
else
    echo "❌ 数据库创建失败"
    exit 1
fi

# 验证数据库
echo ""
echo "🔍 验证数据库..."
RESULT=$(mysql -h "$MYSQL_HOST" -P "$MYSQL_PORT" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" -e "SHOW DATABASES LIKE '$DATABASE_NAME';" -sN)

if [ -n "$RESULT" ]; then
    echo "✅ 数据库验证成功: $DATABASE_NAME"
else
    echo "❌ 数据库验证失败"
    exit 1
fi

# 显示数据库信息
echo ""
echo "📊 数据库信息:"
mysql -h "$MYSQL_HOST" -P "$MYSQL_PORT" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" <<EOF
SELECT
    SCHEMA_NAME as '数据库名',
    DEFAULT_CHARACTER_SET_NAME as '字符集',
    DEFAULT_COLLATION_NAME as '排序规则'
FROM information_schema.SCHEMATA
WHERE SCHEMA_NAME = '$DATABASE_NAME';
EOF

echo ""
echo "=========================================="
echo "✅ 数据库初始化完成！"
echo "=========================================="
echo ""
echo "下一步:"
echo "1. 启动应用: ./mvnw spring-boot:run"
echo "2. 应用会自动创建所有表"
echo "3. 导入数据: curl -X POST \"http://localhost:8080/api/classic/import?filePath=./book/易经.xlsx\""
echo ""
