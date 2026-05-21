# 牧场/饲料厂 - 原料取样 + 化验 + 放行 MVP 范围分析报告

> 生成时间：2026-05-15
> 源文档：`01_系统全局架构与状态机字典.md`、`02_核心业务主流程.md`
> 目的：界定本次 MVP 知识库的精确范围，区分"应纳入"与"应排除"的内容，为后续 RAG 知识库建设提供边界依据。

---

## 一、扫描到的相关文档

| 序号 | 文件名 | 大小 | 说明 |
|------|--------|------|------|
| 1 | `01_系统全局架构与状态机字典.md` | 21 KB | 系统全局架构、技术栈、核心表与状态机字典、软删除设计 |
| 2 | `02_核心业务主流程.md` | 97 KB | 核心业务主流程：原料取样、成品/库存/垫料取样、奶源质检、中心化验室委托单、饲料报告生成 |

共 2 个 Markdown 文档，均与本次 MVP 范围相关（但各有不同比例的关联度）。

---

## 二、MVP 范围内的关键事实提取

### 2.1 相关业务主体

| 业务主体 | dept_type | 说明 |
|---------|-----------|------|
| **牧场** | 2 | 原料进厂质检（如玉米、豆粕等饲料原料） |
| **饲料厂** | 3 | 原料进厂质检 + 成品/库存取样（本次 MVP 只关注原料部分） |

**明确排除**：中心化验室（dept_type=4）——走委托单流程，不在本次 MVP 范围。

### 2.2 相关用户角色

| 角色 | 职责 | 使用端 | 与 MVP 的关系 |
|------|------|--------|---------------|
| **采样员** | 现场采集原料样品、录入采样信息 | 移动端（钉钉小程序） | ✅ 核心角色 |
| **化验员** | 执行检测、录入检测结果 | PC端（管理后台） | ✅ 核心角色 |
| **审核员/质检主管** | 审核检测结果、判定合格/不合格 | PC端 | ✅ 核心角色 |
| **司机** | 一卡通签到触发取样计划 | 一卡通系统 | ✅ 间接角色（触发源） |

**明确排除**：接样员（中心化验室样品接收）、实验室管理员（仪器/试剂管理）、数据分析员、系统管理员。

### 2.3 相关页面

#### 移动端（钉钉小程序）

| 路径 | 说明 | 业务主体 |
|------|------|---------|
| `lims-app/pages/Feedmillassay/ylqy/` | 饲料厂原料取样页面 | 饲料厂 |
| `lims-app/pages/Ranchassay/` | 牧场检验（含原料取样功能） | 牧场 |

#### PC端管理后台

| 路径 | 说明 |
|------|------|
| 化验任务列表 → 开始化验 → 化验单详情 | 化验员录入检测结果 |
| 化验审核页面 | 审核员审核检测结果 |

**【待确认】**：PC端原料取样计划管理页面的具体路径，文档中未明确给出前端路径。

### 2.4 相关接口

#### 原料取样计划接口

| 接口路径 | 方法 | Controller | 说明 |
|---------|------|-----------|------|
| `/ranch/plan/notifySignIn` | POST | `OpSamplingPlanController.receiveSignInNotification()` | 接收一卡通签到通知，自动创建原料取样计划 |
| `/ranch/plan/add` | POST | `OpSamplingPlanController.add()` | 移动端手动新增原料取样计划 |
| `/ranch/plan/isRelease/{signInId}` | GET | 【待确认具体方法名】 | 一卡通查询放行状态 |

#### 化验流程接口

| 接口路径 | 方法 | Controller | 说明 |
|---------|------|-----------|------|
| `/ranch/testResult/startTest` | POST | `OpTestResultBaseController` → `startTest()` | 开始化验：批量转化待检任务为化验单 |
| `/ranch/testResult/submit` | PUT | `OpTestResultBaseController` → `submitTestResult()` | 提交化验单，状态变为待审核 |
| `/ranch/testResult/approve/{id}` | PUT | `OpTestResultBaseController` → `approveTestResult()` | PC端审核通过 |
| `/ranch/testResult/approveTest` | POST | `OpTestResultBaseController` | 手机端审核通过 |
| `/ranch/testResult/reject` | PUT | `OpTestResultBaseController` → `rejectTestResult()` | 审核退回 |
| `/ranch/testResult/resultChange` | PUT | `OpTestResultBaseController` → `changeResult()` | 检测结果变更（含变更日志） |

**【待确认】**：原料取样计划的其他 CRUD 接口（编辑、删除、列表查询）的完整路径。

### 2.5 相关 Controller / Service

| 层级 | 类名 | 所属模块 | 说明 |
|------|------|---------|------|
| Controller | `OpSamplingPlanController` | `business.samplingplan` 或 `business.ranch` | 原料取样计划 CRUD + 一卡通通知接收 |
| Service | `OpSamplingPlanServiceImpl` | `business.samplingplan` | 原料取样计划业务逻辑 |
| Controller | `OpTestResultBaseController` | `business.ranch` 或 `business.testresult` | 化验单管理（牧场/饲料厂专用） |
| Service | `OpTestResultBaseServiceImpl` | `business.ranch` | 化验单核心业务逻辑（开始化验、提交、审核、结果变更） |
| DTO | `SignInNotificationDTO` | - | 一卡通签到通知的数据传输对象 |

**【待确认】**：Controller 和 Service 的精确包路径，文档中未完全明确。

### 2.6 相关数据库表

#### MVP 核心表（必须纳入）

| 表名 | 作用 | 关键度 |
|------|------|--------|
| `op_sampling_plan` | 原料取样计划主表 | ★★★ 核心 |
| `op_sampling_plan_sample` | 样品表（sampling_type='3' 原料） | ★★★ 核心 |
| `op_sampling_plan_item` | 样品检测项目明细表 | ★★★ 核心 |
| `op_test_result_base` | 化验单主表（牧场/饲料厂专用） | ★★★ 核心 |
| `op_test_result_info` | 化验结果明细表 | ★★★ 核心 |

#### MVP 支撑表（需了解）

| 表名 | 作用 | 关键度 |
|------|------|--------|
| `bs_invbill_item_standard` | 物料项目标准表（判定合格/不合格的依据） | ★★ 支撑 |
| `bs_labtest_items` | 检测项目表（全局共享） | ★★ 支撑 |
| `bs_invbill` | 物料档案 | ★ 参考 |
| `sys_dept` | 部门表（dept_type区分业态） | ★ 参考 |

#### MVP 边界表（仅在特定场景涉及）

| 表名 | 作用 | 何时涉及 |
|------|------|---------|
| `op_sample_unquality` | 不合格品主表 | 审核时检测结果不合格，自动生成 |
| `op_sample_unquality_detail` | 不合格品明细表 | 同上 |
| `op_test_result_change_log` | 检测结果变更日志表 | 结果变更时记录 |

**【待确认】**：`op_sampling_plan` 表与 `op_test_result_base` 表之间的关联字段。文档中描述为通过 `op_sampling_plan_item` 间接关联：`op_sampling_plan` → `op_sampling_plan_sample` → `op_sampling_plan_item` → `op_test_result_info` → `op_test_result_base`。

### 2.7 关键状态字段及含义

#### `op_sampling_plan.status`（取样计划状态）

| 值 | 含义 | 说明 |
|----|------|------|
| `0` | 未提交 | 草稿状态 |
| `1` | 未取样 | 已提交，等待采样员取样 |
| `2` | 已取样 | 采样员已完成取样 |
| `3` | 已作废 | 计划作废 |

#### `op_sampling_plan.is_release`（放行状态）

| 值 | 含义 | 说明 |
|----|------|------|
| `0` | 未放行 | 尚未满足放行条件 |
| `1` | 已放行 | 所有样品检测合格，已放行 |

#### `op_sampling_plan_sample.sampling_type`（样品类型）

| 值 | 含义 | MVP 范围 |
|----|------|---------|
| `0` | 成品 | ❌ 不属于 |
| `1` | 库存 | ❌ 不属于 |
| `2` | 垫料 | ❌ 不属于 |
| `3` | 原料 | ✅ MVP 范围 |

#### `op_sampling_plan_sample.whether_qualified`（是否合格）

| 值 | 含义 |
|----|------|
| `A1` | 合格 |
| `R1` | 不合格 |

#### `op_sampling_plan_sample.is_receive`（是否接收）

| 值 | 含义 |
|----|------|
| `0` | 未接收 |
| `1` | 已接收 |

**【待确认】**：原料样品是否有"接收"环节，还是取样后直接进入化验。文档中提到 `is_receive` 字段但未明确原料流程中该字段的使用方式。

#### `op_test_result_base.status`（化验单状态）

| 值 | 含义 | 说明 |
|----|------|------|
| `1` | 待化验 | 【待确认】文档显示开始化验后直接跳到 status='2' |
| `2` | 待提交 | 化验员正在录入，尚未提交 |
| `3` | 待审核 | 化验员已提交，等待审核 |
| `4` | 已审核 | 审核通过（终态之一） |

**【待确认】**：status='1'（待化验）是否实际存在于 `op_test_result_base` 表中。文档提到 `startTest()` 方法创建的化验单 status='2'（待提交），跳过了 status='1'。

#### `op_test_result_info.check_result`（检测结果判定）

| 值 | 含义 |
|----|------|
| `1` | 合格 |
| `2` | 不合格 |

**【待确认】**：该字段的完整取值枚举，文档中只提到了 '1' 和 '2'。

#### 审核结果类型（`examine_result_type` 字典）

| 值 | 含义 | MVP 范围 |
|----|------|---------|
| `A1` | 合格 | ✅ |
| `R1` | 不合格 | ✅ |
| `A2` | 可接收 | 【待确认】MVP 是否需要 |
| `A3` | 超范围接收 | 【待确认】MVP 是否需要 |
| `R2` | 不合格退货 | 【待确认】MVP 是否需要 |

#### `op_test_result_info.retest_flag`（复检标记）

| 值 | 含义 | MVP 范围 |
|----|------|---------|
| `0` | 正常记录（非复检） | ✅ |
| `1` | 已复检/归档 | ❌ 复检机制不在 MVP |
| `2` | 复检待审核 | ❌ 复检机制不在 MVP |

### 2.8 核心业务流程

#### 流程总览（MVP 范围）

```
【模式1：一卡通自动触发】
一卡通系统司机签到
   ↓
调用接口 /ranch/plan/notifySignIn
   ↓
自动创建 op_sampling_plan 记录（status='1' 未取样）
   ↓
采样员在移动端扫码或手动关联，生成 op_sampling_plan_sample（sampling_type='3'）
   ↓
系统根据 bs_invbill_item_standard 自动生成 op_sampling_plan_item（检测项目）
   ↓
化验员在 PC 端选择待化验项目 → 调用 startTest()
   ↓
生成 op_test_result_base（status='2' 待提交）+ op_test_result_info
   ↓
化验员逐项录入检测结果 → 调用 submit()
   ↓
op_test_result_base status → '3'（待审核）
   ↓
审核员审核 → 调用 approve()
   ↓
op_test_result_base status → '4'（已审核）
   ↓
系统自动判定：更新 op_sampling_plan_sample.whether_qualified
   ↓
如果所有样品合格 → 更新 op_sampling_plan.is_release = '1'
   ↓
一卡通系统调用 /ranch/plan/isRelease/{signInId} 查询放行状态

【模式2：手动创建】
移动端"原料取样"页面
   ↓
手动创建 op_sampling_plan 记录（无 sign_in_id）
   ↓
生成 op_sampling_plan_sample
   ↓
（后续化验 → 判定流程同上）
```

#### 一卡通签到通知处理步骤

1. 接收一卡通系统的签到通知（JSON 数据 + Base64 附件）
2. 根据 `destinationCode` 查询 LIMS 系统的 `dept_id`
3. 创建 `OpSamplingPlan` 对象：
   - `sign_in_id` = notifyDTO.id（关联一卡通签到记录）
   - `driver_name` / `driver_code` = 司机信息
   - `car_in_time` = signInTime
   - `status` = '1'（未取样）
   - `dept_id` = 查到的 LIMS 部门 ID
4. 生成 `sampling_plan_no`（送检单号）
5. 处理 Base64 附件，保存到服务器，生成 `car_file_id`
6. 插入 `op_sampling_plan` 表

#### 化验流程关键步骤

| 步骤 | 接口 | 状态变化 | 关键操作 |
|------|------|---------|---------|
| 开始化验 | `startTest` | item → base(status=2) + info(status=2) | 批量转化，需 Redisson 分布式锁防重复 |
| 提交化验 | `submit` | base/info: 2→3 | 提交待审核 |
| 审核通过 | `approve` | base/info: 3→4 | 自动判定不合格项→生成不合格品处理单 |
| 审核退回 | `reject` | base: 3→2 | 填写退回原因 |
| 结果变更 | `resultChange` | - | 更新结果+记录变更日志+重新判定 |

#### 放行逻辑

- 所有样品检测合格后，更新 `op_sampling_plan.is_release = '1'`（已放行）
- 一卡通系统可通过接口 `/ranch/plan/isRelease/{signInId}` 查询放行状态

**【待确认】**：
1. 放行是手动操作还是自动触发？文档中描述为"所有样品检测合格后"更新，但未明确该操作是由谁触发。
2. 原料样品的检测项目如何自动生成？文档中提到"系统根据 `bs_invbill_item_standard` 自动生成"，但未详细描述生成规则（是按物料编码匹配所有必检项目，还是有其他筛选逻辑）。

---

## 三、模块边界划分

### 3.1 牧场/饲料厂流程 vs 中心化验室 labtest 流程（核心边界）

| 维度 | 牧场/饲料厂（MVP 范围） | 中心化验室 labtest（排除） |
|------|----------------------|--------------------------|
| **业务模式** | 自检自用：牧场/饲料厂对自己的原料进行质检 | 委托制：外部送检样品到检测中心 |
| **核心表** | `op_sampling_plan`、`op_sampling_plan_sample`、`op_test_result_base` | `op_blood_entrust_order`、`op_feed_entrust_order`、`op_pcr_entrust_order`、`op_jczx_*_result_base` |
| **触发方式** | 一卡通签到 OR 移动端手动创建 | PC端委托方填写委托单 |
| **业务主体** | dept_type=2（牧场）、dept_type=3（饲料厂） | dept_type=4（检测中心） |
| **代码模块** | `business.ranch`、`business.samplingplan` | `business.labtest` |
| **前端路径** | `lims-app/pages/Ranchassay/`、`lims-app/pages/Feedmillassay/` | `lims-vue/src/views/CentralLaboratory/` |
| **状态机** | `sampling_plan_status`（0→1→2→3） | `entrust_order_status`（0→1→2→3→4→5→6→7） |
| **化验单状态** | `test_result_status`（2→3→4） | `feed_result_status`（1→2→3→4→5→6） |

**⚠️ 关键认知**：`labtest` 模块是中心化验室专属，不是全系统通用的化验核心。牧场/饲料厂的化验流程使用独立的 `op_test_result_base` 表，与中心化验室的 `op_jczx_feed_result_base` 表完全隔离。

### 3.2 牧场/饲料厂流程 vs 奶源质检流程

| 维度 | 原料取样+化验（MVP 范围） | 奶源质检（排除） |
|------|------------------------|-------------------|
| **业务对象** | 饲料原料（玉米、豆粕等） | 奶源（生鲜乳） |
| **核心表** | `op_sampling_plan` + `op_sampling_plan_sample` | `op_milk_sample_quality_inspection` |
| **表结构** | 主子表模式（计划→样品→检测项目→化验单） | 宽表模式（所有指标直接存在主表字段中） |
| **数据来源** | 一卡通签到 / 手动创建 | 外部奶源管理系统（奶源计划单号） |
| **后续流程** | 放行 | 装奶单 → 工厂质检 → SAP |
| **移动端页面** | `Feedmillassay/ylqy/`、`Ranchassay/` | `Ranchmilksource/` |

**关键边界**：奶源质检使用独立的表结构（`op_milk_sample_quality_inspection`），不使用 `op_sampling_plan_sample` 表，与原料取样流程在代码和数据层面完全隔离。

### 3.3 原料流程 vs 成品/库存/垫料流程

| 维度 | 原料（MVP 范围） | 成品/库存/垫料（排除） |
|------|-----------------|----------------------|
| **触发方式** | 一卡通签到 OR 移动端手动创建 | PC 端 Excel 导入 OR 移动端直接采样 |
| **计划主表** | `op_sampling_plan` | `op_finished_product_sampling_plan` |
| **sampling_type** | `3`（原料） | `0`（成品）、`1`（库存）、`2`（垫料） |
| **SAP推送** | 【待确认】 | 有（SAP批次绑定 + 推送） |
| **业务主体** | 牧场 + 饲料厂 | 成品仅饲料厂、垫料仅牧场、库存两者皆有 |

**关键边界**：原料和成品/库存/垫料在 `op_sampling_plan_sample` 表中通过 `sampling_type` 字段区分，但原料有独立的计划主表（`op_sampling_plan`），而成品/库存/垫料使用另一套计划主表（`op_finished_product_sampling_plan`）。

### 3.4 明确排除的内容及排除原因

| 排除内容 | 原因 |
|---------|------|
| 中心化验室 labtest 委托单流程 | 属于 dept_type=4，委托制模式，与自检流程完全不同 |
| 奶源质检流程 | 使用独立表结构（`op_milk_sample_quality_inspection`），业务对象为奶源而非原料 |
| 饲料报告生成流程 | 属于中心化验室报告模块，操作主体为检测中心 |
| PCR、血样、生化、早孕流程 | 全部属于中心化验室委托单流程的子流程 |
| SAP 复杂推送流程 | 属于饲料厂成品/库存流程，且 MVP 阶段不关注外部系统集成细节 |
| 复检机制 | 审核退回后的重新化验流程，MVP 阶段暂不覆盖 |
| 不合格品完整后续处理流程 | `op_sample_unquality` 表生成后的完整业务流程，MVP 只关注到"生成不合格品记录"为止 |
| 成品/库存/垫料取样流程 | 使用不同的计划主表（`op_finished_product_sampling_plan`），不属于原料 |
| 装奶单流程 | 属于奶源质检的后续流程 |
| 工厂质检结果对比 | 属于奶源流程的工厂侧 |

---

## 四、当前逆向文档中的不确定项 / TODO 标记

### 4.1 文档中明确标记的 TODO

| 序号 | TODO 内容 | 来源 | 对 MVP 的影响 |
|------|----------|------|-------------|
| 1 | 原料化验录入的详细字段和判定规则 | 02_核心业务主流程.md:168 | 高 — 影响化验录入功能的理解 |
| 2 | 检验标准与自动判定引擎 | 02_核心业务主流程.md:772 | 高 — 影响合格/不合格判定的理解 |
| 3 | 不合格样品的后续处理流程（unquality模块） | 02_核心业务主流程.md:963 | 中 — MVP 只到生成不合格品记录 |
| 4 | 路径A的具体Excel导入逻辑和字段映射 | 02_核心业务主流程.md:247 | 低 — 属于成品/库存/垫料流程 |
| 5 | 路径B的样品新增字段和移动端页面逻辑 | 02_核心业务主流程.md:283 | 低 — 属于成品/库存/垫料流程 |
| 6 | 成品/库存/垫料的具体检测指标差异 | 02_核心业务主流程.md:295 | 低 — 不属于 MVP |
| 7 | 铅封号（qfh）的作用和生成规则 | 02_核心业务主流程.md:961 | 低 — 属于奶源质检流程 |

### 4.2 本次分析新增识别的不确定项

| 序号 | 内容 | 优先级 |
|------|------|--------|
| 8 | 【待确认】PC端原料取样计划管理页面的具体前端路径 | 中 |
| 9 | 【待确认】原料取样计划 CRUD 接口（编辑、删除、列表查询）的完整路径 | 中 |
| 10 | 【待确认】Controller 和 Service 的精确包路径 | 低 |
| 11 | 【待确认】`op_sampling_plan` 与 `op_test_result_base` 的直接关联字段 | 高 |
| 12 | 【待确认】`op_test_result_base.status='1'`（待化验）是否实际存在 | 中 |
| 13 | 【待确认】`op_test_result_info.check_result` 的完整枚举值 | 中 |
| 14 | 【待确认】`examine_result_type` 中 A2/A3/R2 在原料流程中是否使用 | 中 |
| 15 | 【待确认】放行是手动还是自动触发 | 高 |
| 16 | 【待确认】原料样品的检测项目自动生成规则 | 高 |
| 17 | 【待确认】原料样品是否有"接收"环节（`is_receive` 字段的使用方式） | 中 |
| 18 | 【待确认】原料流程是否涉及 SAP 推送 | 中 |
| 19 | 【待确认】一卡通查询放行状态的接口方法名和返回值格式 | 高 |
| 20 | 【待确认】移动端原料取样页面的完整字段列表和操作流程 | 高 |

---

## 五、RAG 知识库建设建议

### 5.1 建议的知识条目结构

基于 MVP 范围，建议构建以下知识条目：

1. **业务概览**：牧场/饲料厂原料质检的业务背景、业务主体、核心角色
2. **数据模型**：5 张核心表的结构、字段含义、关联关系
3. **状态机**：取样计划状态、化验单状态、放行状态的完整流转
4. **API 接口**：原料取样计划接口、化验流程接口、放行查询接口
5. **业务流程**：两种触发模式（一卡通/手动）的完整时序
6. **判定规则**：合格/不合格的判定标准和判定时机
7. **模块边界**：原料流程与其他流程（奶源、成品、labtest）的区分

### 5.2 当前文档的覆盖度评估

| 知识领域 | 覆盖度 | 说明 |
|---------|--------|------|
| 数据表结构 | ★★★ 高 | 核心表的字段和作用已明确 |
| 状态机 | ★★★ 高 | 状态值和流转路径已明确 |
| 一卡通触发流程 | ★★★ 高 | 接口、参数、处理步骤已明确 |
| 手动创建流程 | ★★ 中 | 触发方式已知，但移动端页面细节待确认 |
| 化验录入流程 | ★★★ 高 | 完整的 start→submit→approve 流程已明确 |
| 合格/不合格判定 | ★★ 中 | 判定字段和时机已知，但具体规则引擎待确认 |
| 放行机制 | ★★ 中 | 字段和接口已知，但触发方式待确认 |
| 检测项目生成 | ★ 低 | 只提到"系统根据标准自动生成"，细节缺失 |
| 与其他模块的边界 | ★★★ 高 | 已明确区分原料/奶源/成品/labtest |

### 5.3 后续建议优先深挖的方向

1. **放行机制**：手动还是自动？触发条件？与一卡通的交互细节？
2. **检测项目自动生成**：规则引擎的完整逻辑
3. **移动端原料取样页面**：完整字段、操作流程、与一卡通扫码的交互
4. **化验单与取样计划的关联链路**：确保数据模型的完整理解

---

## 附录：源文档中各章节与 MVP 的关系速查表

| 源文档章节 | 与 MVP 关系 | 说明 |
|-----------|------------|------|
| 一、项目目录与技术栈总结 | 参考 | 了解整体架构 |
| 二、核心业务目录标注（牧场/饲料厂部分） | ★★★ 直接相关 | 核心模块定位 |
| 二、核心业务目录标注（中心化验室部分） | ❌ 不纳入 | labtest 模块 |
| 二、核心业务目录标注（基础数据部分） | ★★ 部分相关 | `bs_invbill_item_standard` 等 |
| 二、核心数据模型与状态机（表1-5） | ❌ 不纳入 | 成品/库存/奶源/奶车 |
| 二、核心数据模型与状态机（表6-9） | ❌ 不纳入 | 中心化验室委托单 |
| 二、核心数据模型与状态机（表10-12） | ★★★ 直接相关 | 原料取样计划核心表 |
| 二、核心数据模型与状态机（表13-14） | ★★ 支撑 | 基础配置表 |
| 二、软删除设计 | ★ 参考 | 系统级架构模式 |
| 三、牧场/饲料厂采样与检验主流程（3.1） | ★★★ 直接相关 | 业务主体与页面归属 |
| 三、原料取样流程（3.2） | ★★★ 直接相关 | 核心 MVP 内容 |
| 三、成品/库存/垫料取样流程（3.3） | ❌ 不纳入 | 不属于原料 |
| 三、化验流程状态机（3.3.5 Step1-4） | ★★★ 直接相关 | 核心 MVP 内容 |
| 三、复检机制（3.3.5 Step5） | ❌ 不纳入 | MVP 排除项 |
| 三、SAP批次绑定与推送（3.3.5 Step6） | ❌ 不纳入 | MVP 排除项 |
| 三、检测项目作废（3.3.5 Step7） | ★ 边缘 | 暂不纳入，但需了解存在 |
| 三、牧场奶源采样与质检流程（3.4） | ❌ 不纳入 | 奶源流程 |
| 三、饲料化验单状态3后的被动等待机制（3.5） | ❌ 不纳入 | 属于中心化验室饲料报告 |
| 五、中心化验室业务流程 | ❌ 不纳入 | 全部属于 labtest |
| 六、饲料报告生成与发布流程 | ❌ 不纳入 | 中心化验室报告模块 |
