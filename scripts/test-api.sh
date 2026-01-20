#!/bin/bash

# ======================================
# 易经卜卦 API 测试脚本
# ======================================

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 配置
API_BASE_URL="${API_BASE_URL:-http://localhost:8080}"
TEST_USERNAME="testuser_$(date +%s)"
TEST_PASSWORD="password123"
TEST_EMAIL="test_$(date +%s)@example.com"
TOKEN=""

# 工具函数
print_section() {
    echo -e "\n${BLUE}========================================${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}========================================${NC}\n"
}

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

print_info() {
    echo -e "${YELLOW}ℹ $1${NC}"
}

# API 请求函数
make_request() {
    local method=$1
    local endpoint=$2
    local data=$3
    local use_token=${4:-false}

    local headers="Content-Type: application/json"
    if [ "$use_token" = true ] && [ -n "$TOKEN" ]; then
        headers="$headers"$'\n'"Authorization: Bearer $TOKEN"
    fi

    echo -e "${YELLOW}>>> $method $endpoint${NC}"

    if [ -n "$data" ]; then
        echo -e "${YELLOW}>>> Request Body:${NC}"
        echo "$data" | jq '.' 2>/dev/null || echo "$data"
    fi

    response=$(curl -s -X "$method" \
        -H "$headers" \
        ${data:+-d "$data"} \
        "$API_BASE_URL$endpoint")

    echo -e "${YELLOW}<<< Response:${NC}"
    echo "$response" | jq '.' 2>/dev/null || echo "$response"
    echo ""

    echo "$response"
}

# 检查服务是否运行
check_service() {
    print_section "检查服务状态"

    response=$(make_request GET "/api/health")

    if echo "$response" | grep -q "\"ok\": *true\|\"status\": *\"UP\""; then
        print_success "服务运行正常"
        return 0
    else
        print_error "服务未运行或不可用"
        return 1
    fi
}

# 测试用户注册
test_register() {
    print_section "测试用户注册"

    local data=$(cat <<EOF
{
  "username": "$TEST_USERNAME",
  "password": "$TEST_PASSWORD",
  "email": "$TEST_EMAIL"
}
EOF
)

    response=$(make_request POST "/api/auth/register" "$data")

    if echo "$response" | grep -q "token"; then
        print_success "用户注册成功"

        # 提取 token
        TOKEN=$(echo "$response" | jq -r '.token // .data.token // empty' 2>/dev/null)

        if [ -n "$TOKEN" ]; then
            print_info "Token: ${TOKEN:0:20}..."
            return 0
        fi
    fi

    print_error "用户注册失败"
    return 1
}

# 测试用户登录
test_login() {
    print_section "测试用户登录"

    local data=$(cat <<EOF
{
  "username": "$TEST_USERNAME",
  "password": "$TEST_PASSWORD"
}
EOF
)

    response=$(make_request POST "/api/auth/login" "$data")

    if echo "$response" | grep -q "token"; then
        print_success "用户登录成功"

        # 提取 token
        TOKEN=$(echo "$response" | jq -r '.token // .data.token // empty' 2>/dev/null)

        if [ -n "$TOKEN" ]; then
            print_info "Token: ${TOKEN:0:20}..."
            return 0
        fi
    fi

    print_error "用户登录失败"
    return 1
}

# 测试获取当前用户
test_get_current_user() {
    print_section "测试获取当前用户"

    response=$(make_request GET "/api/auth/me" "" true)

    if echo "$response" | grep -q "$TEST_USERNAME\|authenticated.*true"; then
        print_success "获取当前用户成功"
        return 0
    fi

    print_error "获取当前用户失败"
    return 1
}

# 测试卜卦 - 铜钱法
test_divination_coin() {
    print_section "测试卜卦 - 铜钱法"

    local data=$(cat <<EOF
{
  "question": "今年事业运势如何？",
  "method": "COIN",
  "needAiInterpretation": false
}
EOF
)

    response=$(make_request POST "/api/divination/consult" "$data" true)

    if echo "$response" | grep -q "originalHexagram\|timestamp"; then
        print_success "铜钱法卜卦成功"

        hexagram_name=$(echo "$response" | jq -r '.originalHexagram.name // .data.originalHexagram.name // empty' 2>/dev/null)
        if [ -n "$hexagram_name" ]; then
            print_info "得到卦象: $hexagram_name"
        fi

        return 0
    fi

    print_error "铜钱法卜卦失败"
    return 1
}

# 测试卜卦 - 时间起卦
test_divination_time() {
    print_section "测试卜卦 - 时间起卦法"

    local data=$(cat <<EOF
{
  "question": "近期财运如何？",
  "method": "TIME",
  "needAiInterpretation": false
}
EOF
)

    response=$(make_request POST "/api/divination/consult" "$data" true)

    if echo "$response" | grep -q "originalHexagram\|timestamp"; then
        print_success "时间起卦法成功"

        hexagram_name=$(echo "$response" | jq -r '.originalHexagram.name // .data.originalHexagram.name // empty' 2>/dev/null)
        if [ -n "$hexagram_name" ]; then
            print_info "得到卦象: $hexagram_name"
        fi

        return 0
    fi

    print_error "时间起卦法失败"
    return 1
}

# 测试查询单个卦象
test_get_hexagram() {
    print_section "测试查询单个卦象"

    response=$(make_request GET "/api/divination/hexagram/1" "" true)

    if echo "$response" | grep -q "乾\|name"; then
        print_success "查询单个卦象成功"

        hexagram_name=$(echo "$response" | jq -r '.name // .data.name // empty' 2>/dev/null)
        if [ -n "$hexagram_name" ]; then
            print_info "卦象名称: $hexagram_name"
        fi

        return 0
    fi

    print_error "查询单个卦象失败"
    return 1
}

# 测试获取所有卦象
test_get_all_hexagrams() {
    print_section "测试获取所有卦象"

    response=$(make_request GET "/api/divination/hexagrams" "" true)

    if echo "$response" | grep -q "乾\|\["; then
        count=$(echo "$response" | jq 'if type=="array" then length elif .data | type=="array" then .data | length else 0 end' 2>/dev/null)

        if [ "$count" -gt 0 ]; then
            print_success "获取所有卦象成功"
            print_info "卦象数量: $count"
            return 0
        fi
    fi

    print_error "获取所有卦象失败"
    return 1
}

# 测试 LLM 调用
test_llm_invoke() {
    print_section "测试 LLM 调用"

    local data=$(cat <<EOF
{
  "messages": [
    {
      "role": "system",
      "content": "你是一个助手"
    },
    {
      "role": "user",
      "content": "请用一句话介绍易经"
    }
  ],
  "maxTokens": 100,
  "temperature": 0.7
}
EOF
)

    response=$(make_request POST "/api/llm/invoke" "$data" true)

    if echo "$response" | grep -q "choices\|content"; then
        print_success "LLM 调用成功"

        content=$(echo "$response" | jq -r '.choices[0].message.content // .data.choices[0].message.content // empty' 2>/dev/null)
        if [ -n "$content" ]; then
            print_info "AI 回复: ${content:0:50}..."
        fi

        return 0
    fi

    print_error "LLM 调用失败（可能未配置 LLM API）"
    return 1
}

# 测试更新用户信息
test_update_profile() {
    print_section "测试更新用户信息"

    local data=$(cat <<EOF
{
  "name": "Updated User",
  "email": "updated_$TEST_EMAIL"
}
EOF
)

    response=$(make_request PUT "/api/auth/profile" "$data" true)

    if echo "$response" | grep -q "Updated User\|email"; then
        print_success "更新用户信息成功"
        return 0
    fi

    print_error "更新用户信息失败"
    return 1
}

# 测试登出
test_logout() {
    print_section "测试用户登出"

    response=$(make_request POST "/api/auth/logout" "" true)

    if echo "$response" | grep -q "success.*true\|成功"; then
        print_success "用户登出成功"
        TOKEN=""
        return 0
    fi

    print_error "用户登出失败"
    return 1
}

# 测试系统信息
test_system_info() {
    print_section "测试系统信息"

    response=$(make_request GET "/api/system/info")

    if echo "$response" | grep -q "applicationName\|version"; then
        print_success "获取系统信息成功"

        app_name=$(echo "$response" | jq -r '.applicationName // .data.applicationName // empty' 2>/dev/null)
        if [ -n "$app_name" ]; then
            print_info "应用名称: $app_name"
        fi

        return 0
    fi

    print_error "获取系统信息失败"
    return 1
}

# 主测试流程
main() {
    echo -e "${BLUE}"
    echo "========================================"
    echo "  易经卜卦 API 测试工具"
    echo "========================================"
    echo -e "${NC}\n"

    print_info "API 地址: $API_BASE_URL"
    print_info "测试用户: $TEST_USERNAME"
    echo ""

    # 检查依赖
    if ! command -v jq &> /dev/null; then
        print_error "需要安装 jq 工具: brew install jq"
        exit 1
    fi

    if ! command -v curl &> /dev/null; then
        print_error "需要安装 curl 工具"
        exit 1
    fi

    # 运行测试
    local total=0
    local passed=0

    tests=(
        "check_service"
        "test_register"
        "test_get_current_user"
        "test_get_hexagram"
        "test_get_all_hexagrams"
        "test_divination_coin"
        "test_divination_time"
        "test_llm_invoke"
        "test_update_profile"
        "test_system_info"
        "test_logout"
    )

    for test in "${tests[@]}"; do
        ((total++))
        if $test; then
            ((passed++))
        fi
    done

    # 测试总结
    print_section "测试总结"
    echo -e "${BLUE}总计测试数: $total${NC}"
    echo -e "${GREEN}通过测试数: $passed${NC}"
    echo -e "${RED}失败测试数: $((total - passed))${NC}"

    if [ $passed -eq $total ]; then
        echo -e "\n${GREEN}🎉 所有测试通过！${NC}\n"
        exit 0
    else
        echo -e "\n${RED}⚠️  部分测试失败${NC}\n"
        exit 1
    fi
}

# 运行主函数
main "$@"
