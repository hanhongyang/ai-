# AI Gateway

钉钉机器人 Stream 模式 + FastGPT 集成网关。

## 项目说明

本项目是一个 Spring Boot 3.x 应用，通过**钉钉机器人 Stream 模式**接收群聊中 @机器人的消息，调用 FastGPT API 获取 AI 回答，再将答案回复到钉钉群，并记录问答日志到 MySQL。

### 核心链路

```
用户 @机器人 提问
  → 钉钉 Stream 推送消息
  → Spring Boot 接收并解析
  → 调用 FastGPT API (OpenAI Chat Completions 兼容)
  → 通过 sessionWebhook 回复群聊
  → 保存问答日志到 MySQL
```

### 技术栈

- Java 17
- Spring Boot 3.x
- Maven
- Lombok
- MyBatis Plus
- MySQL
- Spring Web
- RestClient (JDK HttpClient)
- 钉钉 Stream SDK (app-stream-client)

### 架构特点

- **无需公网回调 URL**：使用钉钉 Stream 模式，服务端主动连接钉钉
- **不依赖 HTTP 回调**：所有消息处理通过 Stream SDK 完成
- **模块化设计**：AI 调用、钉钉回复、日志保存各层独立
- **配置驱动**：所有关键参数通过 `application.yml` 和环境变量配置

## 快速开始

### 前置条件

1. Java 17+
2. Maven 3.8+
3. MySQL 8.0+
4. 钉钉开放平台已创建机器人应用，获取 ClientId / ClientSecret
5. FastGPT 服务已部署，获取 API Key

### 1. 初始化数据库

执行项目根目录下的 `schema.sql`：

```bash
mysql -u root -p < schema.sql
```

### 2. 配置环境变量

```bash
# 钉钉机器人配置
export DINGTALK_CLIENT_ID=your-client-id
export DINGTALK_CLIENT_SECRET=your-client-secret

# FastGPT 配置
export FASTGPT_BASE_URL=https://your-fastgpt-domain
export FASTGPT_API_KEY=your-fastgpt-api-key
```

或在 `application.yml` 中直接填写（不推荐用于生产环境）。

### 3. 编译运行

```bash
# 编译
mvn clean package -DskipTests

# 运行
java -jar target/ai-gateway-1.0.0.jar

# 或使用 Maven 直接启动
mvn spring-boot:run
```

### 4. 验证

```bash
curl http://localhost:8080/health
# 预期返回: {"status":"ok","service":"ai-gateway"}
```

## 配置说明

### 钉钉配置

| 配置项 | 环境变量 | 说明 |
|--------|---------|------|
| `dingtalk.stream.client-id` | `DINGTALK_CLIENT_ID` | 钉钉应用 Client ID |
| `dingtalk.stream.client-secret` | `DINGTALK_CLIENT_SECRET` | 钉钉应用 Client Secret |
| `dingtalk.robot.max-question-length` | - | 问题最大长度，默认 500 |
| `dingtalk.robot.enable-at-check` | - | 是否仅响应 @机器人 消息，默认 true |
| `dingtalk.robot.fallback-answer` | - | AI 不可用时的备用回复 |

### FastGPT 配置

| 配置项 | 环境变量 | 说明 |
|--------|---------|------|
| `fastgpt.base-url` | `FASTGPT_BASE_URL` | FastGPT 服务地址 |
| `fastgpt.api-key` | `FASTGPT_API_KEY` | FastGPT API Key |
| `fastgpt.timeout-seconds` | - | 请求超时时间，默认 30 秒 |

## 项目结构

```
com.example.aigateway
├── AiGatewayApplication.java          # 启动类
├── config/
│   ├── DingTalkStreamConfig.java      # 钉钉 Stream 客户端配置
│   ├── FastGptConfig.java             # FastGPT 配置类
│   └── AiGatewayProperties.java       # 钉钉相关配置类
├── controller/
│   └── HealthController.java          # 健康检查接口
├── listener/
│   └── DingTalkStreamMessageListener.java  # 钉钉 Stream 消息监听器
├── service/
│   ├── DingTalkMessageService.java    # 消息处理服务接口
│   ├── DingTalkReplyService.java      # 钉钉回复服务接口
│   ├── FastGptService.java            # FastGPT 调用服务接口
│   └── ChatLogService.java            # 日志保存服务接口
│   └── impl/
│       ├── DingTalkMessageServiceImpl.java
│       ├── DingTalkReplyServiceImpl.java
│       ├── FastGptServiceImpl.java
│       └── ChatLogServiceImpl.java
├── dto/
│   ├── DingTalkBotMessageDTO.java     # 钉钉机器人消息 DTO
│   ├── DingTalkReplyRequest.java      # 钉钉回复请求 DTO
│   ├── FastGptChatRequest.java        # FastGPT 请求 DTO
│   ├── FastGptChatResponse.java       # FastGPT 响应 DTO
│   └── AiAnswerResult.java            # AI 回答结果 DTO
├── entity/
│   └── AiChatLog.java                # 问答日志实体
├── mapper/
│   └── AiChatLogMapper.java          # 日志 MyBatis Mapper
├── util/
│   ├── JsonUtils.java                # JSON 序列化工具
│   └── TextUtils.java                # 文本清洗工具
└── exception/
    └── AiGatewayException.java       # 业务异常
```

## 注意事项

### 钉钉 Stream SDK 版本

Maven 依赖使用：

```xml
<dependency>
    <groupId>com.dingtalk.open</groupId>
    <artifactId>app-stream-client</artifactId>
    <version>1.3.6</version>
</dependency>
```

如果该版本无法解析，请替换为钉钉官方最新版本。您可以访问 [钉钉开放平台文档](https://open.dingtalk.com/) 获取最新 SDK 版本。

### FastGPT 兼容性

本项目调用 FastGPT 的 `/v1/chat/completions` 接口（兼容 OpenAI Chat Completions 格式）。请确保您的 FastGPT 版本支持该接口。

### 安全

- API Key 和 Client Secret 通过环境变量注入，请勿硬编码在配置文件中
- 日志中不会打印 API Key 和 Client Secret
- 敏感信息请妥善保管
