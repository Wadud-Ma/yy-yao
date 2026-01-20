# 快速启动指南 - 前后端集成

本指南帮助您快速启动 Spring Boot 后端,并与前端 React Native 应用集成。

**目标**: 将前端的 `backend-api.ts` 与 Spring Boot 后端完全集成

---

## 📋 前置条件

### 后端需求
- ✅ Java 21
- ✅ Maven 3.8+
- ✅ MySQL 5.7+ 或其他数据库
- ⚠️ (可选) LLM API Key (OpenAI/通义千问)

### 前端需求
- ✅ Node.js 18+
- ✅ pnpm/npm/yarn
- ✅ React Native 开发环境

---

## 🚀 第一步: 启动后端服务

### 1.1 配置环境变量

创建 `.env` 文件:

```bash
cd /Users/juvenile/code/github.com/spring-ai-community/yy-yao

# 复制示例配置
cp .env.example .env

# 编辑配置
vim .env
```

必需的配置:

```bash
# JWT 密钥 (必须至少32字符)
JWT_SECRET="your-random-secret-key-at-least-32-characters-long"

# 数据库配置
DATABASE_URL="jdbc:mysql://localhost:3306/yy-yao?useUnicode=true&characterEncoding=UTF-8"
DB_USERNAME="root"
DB_PASSWORD="your_password"

# 应用配置
SERVER_PORT=8080
```

可选配置:

```bash
# LLM API (用于 AI 解读功能)
LLM_API_URL="https://api.openai.com/v1/chat/completions"
LLM_API_KEY="sk-your-openai-api-key"
LLM_MODEL="gpt-3.5-turbo"

# 或使用通义千问
# LLM_API_URL="https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions"
# LLM_API_KEY="sk-your-qwen-api-key"
# LLM_MODEL="qwen-turbo"
```

### 1.2 创建数据库

```bash
# 连接 MySQL
mysql -u root -p

# 创建数据库
CREATE DATABASE yy_yao CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 退出
exit;
```

### 1.3 编译并启动

```bash
# 编译项目
./mvnw clean package -DskipTests

# 启动服务
./mvnw spring-boot:run
```

或者直接运行 JAR:

```bash
java -jar yy-yao-start/target/yy-yao-start-0.0.1-SNAPSHOT.jar
```

### 1.4 验证服务

```bash
# 健康检查
curl http://localhost:8080/api/health

# 系统信息
curl http://localhost:8080/api/system/info
```

预期响应:

```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "status": "UP",
    "timestamp": "2026-01-20T14:00:00"
  }
}
```

---

## 🎨 第二步: 配置前端

### 2.1 配置 API 地址

在前端项目根目录创建 `.env`:

```bash
cd /Users/juvenile/code/github.com/Wadud-Ma/yi-jing-divination-app

# 创建 .env 文件
cat > .env << 'EOF'
# Spring Boot 后端地址
EXPO_PUBLIC_API_BASE_URL=http://localhost:8080

# 如果使用真机测试,改为你的电脑 IP
# EXPO_PUBLIC_API_BASE_URL=http://192.168.1.100:8080
EOF
```

### 2.2 使用 backend-api.ts

前端已经有了统一的 API 客户端: `lib/backend-api.ts`

**示例1: 用户登录**

```typescript
import { BackendAPI } from '@/lib/backend-api';

// 注册新用户
const registerResult = await BackendAPI.auth.register({
  username: 'testuser',
  password: 'password123',
  email: 'test@example.com',
});

// 登录
const loginResult = await BackendAPI.auth.login({
  username: 'testuser',
  password: 'password123',
});

// 保存 token
const token = loginResult.token;
// await SecureStore.setItemAsync('auth_token', token);
```

**示例2: 卜卦**

```typescript
import { BackendAPI } from '@/lib/backend-api';

// 铜钱法卜卦
const result = await BackendAPI.divination.consult({
  question: '今年事业运势如何？',
  method: 'COIN',
  needAiInterpretation: true,
});

console.log('卦象:', result.originalHexagram.name);
console.log('解读:', result.interpretation);
console.log('AI 解读:', result.aiInterpretation);
```

**示例3: AI 问卦**

```typescript
import { BackendAPI } from '@/lib/backend-api';

// AI 解读卦象
const aiResult = await BackendAPI.divination.interpretWithAI({
  hexagramId: 1,
  hexagramName: '乾',
  hexagramMeaning: '元亨利贞',
  hexagramSymbolism: '天、创造、刚健',
  hexagramAdvice: '发奋进取',
  userQuestion: '这个卦象对我的事业有什么启示？',
  conversationHistory: [],
});

console.log('AI 回复:', aiResult.response);
```

### 2.3 使用 React Query (推荐)

安装依赖 (如果还没有):

```bash
pnpm add @tanstack/react-query
```

配置 QueryClient:

```typescript
// app/_layout.tsx
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

export default function RootLayout() {
  const [queryClient] = useState(() => new QueryClient({
    defaultOptions: {
      queries: {
        refetchOnWindowFocus: false,
        retry: 1,
        staleTime: 5 * 60 * 1000, // 5分钟
      },
    },
  }));

  return (
    <QueryClientProvider client={queryClient}>
      {/* 应用内容 */}
    </QueryClientProvider>
  );
}
```

在组件中使用:

```typescript
import { useQuery, useMutation } from '@tanstack/react-query';
import { BackendAPI } from '@/lib/backend-api';

// 查询卦象
function HexagramDetailScreen({ id }: { id: number }) {
  const { data, isLoading, error } = useQuery({
    queryKey: ['hexagram', id],
    queryFn: () => BackendAPI.divination.getHexagram(id),
  });

  if (isLoading) return <Skeleton />;
  if (error) return <ErrorView message={error.message} />;

  return <HexagramView hexagram={data} />;
}

// 卜卦
function DivinationScreen() {
  const consultMutation = useMutation({
    mutationFn: BackendAPI.divination.consult,
    onSuccess: (data) => {
      // 处理成功
      console.log('卜卦成功:', data);
    },
    onError: (error) => {
      // 处理错误
      console.error('卜卦失败:', error);
    },
  });

  const handleConsult = () => {
    consultMutation.mutate({
      question: '今年事业运势如何？',
      method: 'COIN',
      needAiInterpretation: true,
    });
  };

  return (
    <Button onPress={handleConsult} loading={consultMutation.isPending}>
      开始卜卦
    </Button>
  );
}
```

---

## 🧪 第三步: 测试集成

### 3.1 使用测试脚本

我们提供了一个完整的 API 测试脚本:

```bash
cd /Users/juvenile/code/github.com/spring-ai-community/yy-yao

# 运行测试
./scripts/test-api.sh

# 指定不同的 API 地址
API_BASE_URL=http://192.168.1.100:8080 ./scripts/test-api.sh
```

测试内容包括:
- ✅ 服务健康检查
- ✅ 用户注册
- ✅ 用户登录
- ✅ 获取当前用户
- ✅ 查询卦象
- ✅ 铜钱法卜卦
- ✅ 时间起卦法
- ✅ LLM 调用
- ✅ 更新用户信息
- ✅ 用户登出

### 3.2 手动测试流程

**Step 1: 注册用户**

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123",
    "email": "test@example.com"
  }'
```

返回:

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "testuser",
  "role": "USER"
}
```

**Step 2: 保存 Token**

```bash
TOKEN="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

**Step 3: 卜卦**

```bash
curl -X POST http://localhost:8080/api/divination/consult \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "question": "今年事业运势如何？",
    "method": "COIN",
    "needAiInterpretation": false
  }'
```

**Step 4: 查询卦象**

```bash
curl http://localhost:8080/api/divination/hexagram/1 \
  -H "Authorization: Bearer $TOKEN"
```

---

## 📱 第四步: 在前端应用中使用

### 4.1 修改认证流程

由于后端使用的是用户名/密码认证,而非 OAuth,需要适配:

**修改 `lib/backend-api.ts` 中的 LoginRequest**:

```typescript
export interface LoginRequest {
  username: string;  // 改为 username
  password: string;  // 改为 password
}
```

**在登录组件中使用**:

```typescript
// 之前 (OAuth)
const loginResult = await BackendAPI.auth.login({
  code: authCode,
  redirectUri: 'http://localhost:8081/oauth/callback',
});

// 现在 (用户名密码)
const loginResult = await BackendAPI.auth.login({
  username: 'testuser',
  password: 'password123',
});
```

### 4.2 实现完整的登录流程

```typescript
import { BackendAPI } from '@/lib/backend-api';
import * as SecureStore from 'expo-secure-store';

// 注册
export async function register(username: string, password: string, email: string) {
  const result = await BackendAPI.auth.register({ username, password, email });

  // 保存 token
  await SecureStore.setItemAsync('auth_token', result.token);

  return result.user;
}

// 登录
export async function login(username: string, password: string) {
  const result = await BackendAPI.auth.login({ username, password });

  // 保存 token
  await SecureStore.setItemAsync('auth_token', result.token);

  return result.user;
}

// 登出
export async function logout() {
  await BackendAPI.auth.logout();

  // 删除 token
  await SecureStore.deleteItemAsync('auth_token');
}

// 获取当前用户
export async function getCurrentUser() {
  return await BackendAPI.auth.me();
}
```

### 4.3 在 backend-api.ts 中实现 getAuthToken

修改 `lib/backend-api.ts` 中的 `getAuthToken` 函数:

```typescript
import * as SecureStore from 'expo-secure-store';

async function getAuthToken(): Promise<string | null> {
  // 从 SecureStore 读取 token
  return await SecureStore.getItemAsync('auth_token');
}
```

---

## ⚠️ 重要注意事项

### 1. 响应格式适配

后端部分接口返回 `ApiResponse` 包装:

```json
{
  "code": 200,
  "message": "成功",
  "data": { ... }
}
```

前端可以在 `request` 函数中统一处理:

```typescript
async function request<T>(endpoint: string, options: RequestInit = {}): Promise<T> {
  const response = await fetch(`${API_CONFIG.baseUrl}${endpoint}`, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...options.headers,
    },
  });

  if (!response.ok) {
    throw new Error(`API Error: ${response.status}`);
  }

  const json = await response.json();

  // 适配后端的 ApiResponse 包装
  if (json.code !== undefined && json.data !== undefined) {
    // 检查业务错误
    if (json.code !== 200) {
      throw new Error(json.message || 'API 错误');
    }
    return json.data as T;
  }

  return json as T;
}
```

### 2. CORS 配置

后端已经配置了 CORS,允许所有来源:

```java
configuration.setAllowedOriginPatterns(List.of("*"));
```

**生产环境建议**: 改为具体的前端域名:

```java
configuration.setAllowedOriginPatterns(List.of(
    "http://localhost:*",
    "https://yourdomain.com"
));
```

### 3. AI 解读功能

如果需要使用 AI 解读功能,必须配置 LLM API:

```bash
export LLM_API_URL="https://api.openai.com/v1/chat/completions"
export LLM_API_KEY="sk-your-api-key"
export LLM_MODEL="gpt-3.5-turbo"
```

未配置时,调用会返回错误,前端需要优雅处理:

```typescript
try {
  const result = await BackendAPI.divination.consult({
    question: '今年事业运势如何？',
    method: 'COIN',
    needAiInterpretation: true,
  });
} catch (error) {
  if (error.message.includes('LLM')) {
    // AI 服务不可用,回退到传统解读
    console.log('AI 服务暂不可用,使用传统解读');
  }
}
```

---

## 🔍 故障排查

### 问题1: 连接被拒绝

**症状**: `ECONNREFUSED` 或 `Network Error`

**解决方案**:
1. 确认后端服务已启动: `curl http://localhost:8080/api/health`
2. 检查前端的 API 地址配置
3. 如果使用真机测试,确保使用电脑 IP 而非 localhost

### 问题2: 401 Unauthorized

**症状**: 所有需要认证的接口返回 401

**解决方案**:
1. 确认已登录并保存了 token
2. 检查 token 是否正确设置在 `Authorization` 头中
3. 检查 token 是否过期 (默认24小时)

### 问题3: CORS 错误

**症状**: `Access-Control-Allow-Origin` 错误

**解决方案**:
1. 确认后端 CORS 配置正确
2. 检查前端请求是否设置了正确的 `Content-Type`
3. 确认 `credentials` 设置为 `true`

### 问题4: 数据库连接失败

**症状**: 启动时报错 `Unable to connect to database`

**解决方案**:
1. 确认 MySQL 已启动
2. 检查数据库配置 (URL, 用户名, 密码)
3. 确认数据库已创建

### 问题5: JWT 密钥错误

**症状**: 启动时报错 `JWT secret key is too short`

**解决方案**:
```bash
# 生成安全的密钥
export JWT_SECRET=$(openssl rand -base64 32)
```

---

## 📚 相关文档

- [API 实现状态对比](./API_IMPLEMENTATION_STATUS.md)
- [后端 API 规范](../BACKEND_API_SPEC.md)
- [前端迁移指南](../FRONTEND_MIGRATION_GUIDE.md)
- [项目 README](../README.md)
- [前端统一 API 客户端](../../yi-jing-divination-app/lib/backend-api.ts)

---

## 🎯 下一步

### 开发阶段
- [ ] 完成用户认证流程的前端实现
- [ ] 实现所有卜卦功能
- [ ] 集成 AI 解读功能
- [ ] 添加文件上传功能 (如头像)

### 测试阶段
- [ ] 使用 `test-api.sh` 测试所有接口
- [ ] 在真机上测试前后端集成
- [ ] 性能测试和优化

### 生产部署
- [ ] 配置生产环境数据库
- [ ] 设置 CORS 白名单
- [ ] 配置 HTTPS
- [ ] 部署到服务器

---

**文档版本**: v1.0.0
**最后更新**: 2026-01-20
**维护者**: Claude Code
