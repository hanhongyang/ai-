# RAG Chunks from 01_raw_material_business_overview.md

---

---
chunk_id: 01-001
module: raw_material_sampling
doc_type: business_overview
role: all
question_type: module_positioning
visibility: public
source_doc: 01_raw_material_business_overview.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## 模块定位：原料取样化验解决什么业务问题

牧场和饲料厂每天都会接收大量饲料原料（如玉米、豆粕、棉籽等）。这些原料的质量直接影响奶牛健康和乳品品质。原料取样化验模块解决的核心问题是：

每一车运进牧场的原料，在入库使用之前，都必须经过"取样 → 化验 → 判定 → 放行"这一整套质量检验流程，确保不合格原料不会进入生产环节。

---

---
chunk_id: 01-002
module: raw_material_sampling
doc_type: business_overview
role: all
question_type: process_overview
visibility: public
source_doc: 01_raw_material_business_overview.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## 五个关键环节之间的关系

原料取样流程包含五个关键环节：

- 取样：采样员从运料车上物理采集原料样品，记录样品信息。没有取样，后面所有环节都无法进行。
- 化验：化验员对样品进行检测（如水分、蛋白、毒素等指标），录入检测结果。这是质量判断的依据。
- 判定：系统根据预设的质量标准，自动比对化验结果，判定每个检测项目是"合格"还是"不合格"。审核员确认判定结果。
- 放行：当一车原料的所有检测项目全部合格后，系统标记该车原料"已放行"，允许入库使用。一卡通系统可以查询这个放行状态。

---

---
chunk_id: 01-003
module: raw_material_sampling
doc_type: business_overview
role: driver,sampler,tester,reviewer
question_type: role_responsibility
visibility: public
source_doc: 01_raw_material_business_overview.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## 四个角色在原料取样流程中的作用

司机是流程起点：在一卡通系统签到，触发取样计划生成。不签到则系统不会自动创建取样计划（但可通过手动方式补建）。

采样员负责取样环节：到现场从车上采集原料样品，在移动端（钉钉小程序）录入样品信息。不取样则没有样品，无法化验，无法放行。

化验员负责化验环节：按照检测标准对样品进行各项指标检测，在 PC 端管理后台录入结果。不化验则没有结果，无法判定，无法放行。

审核员负责判定环节：确认化验结果的准确性，最终确认合格或不合格。不审核则化验结果未生效，放行状态不会更新。

---

---
chunk_id: 01-004
module: raw_material_sampling
doc_type: business_overview
role: all
question_type: applicable_scope
visibility: public
source_doc: 01_raw_material_business_overview.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## 原料取样模块适用业务范围

适用于以下场景：
- 牧场原料进厂质检：牧场采购的饲料原料（玉米、豆粕、棉籽等）运到牧场时，需要进行质量检验。
- 饲料厂原料进厂质检：饲料厂采购的原料进厂时，同样需要进行质量检验。

关键业务标识：
- 样品类型（sampling_type）= 3，表示"原料"样品，区别于成品（0）、库存（1）、垫料（2）。
- 数据存储在原料取样计划表（op_sampling_plan），每一车原料的取样任务对应一条取样计划记录。
- 样品数据存储在样品表（op_sampling_plan_sample），每个从原料上采集的样品都在这里记录。
- 检测项目存储在检测项目表（op_sampling_plan_item）。

---

---
chunk_id: 01-005
module: raw_material_sampling
doc_type: business_overview
role: all
question_type: exclusion_scope
visibility: public
source_doc: 01_raw_material_business_overview.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## 原料取样模块不适用范围

以下业务场景不属于本模块，请不要用本模块的知识来回答相关问题：

- 中心化验室委托单流程：属于中心化验室（labtest），外部单位送样到检测中心，走"委托单→受理→检测→报告"流程。
- 奶源质检流程：检测对象是生鲜乳（牛奶），不是饲料原料，使用独立的奶样质检表。
- 成品取样：检测对象是饲料成品（生产出来的饲料），不是进货原料。
- 库存取样：检测对象是仓库里存放的物料，不是新进厂的原料。
- 垫料取样：检测对象是牛床垫料，不是饲料原料。
- 饲料报告生成：属于中心化验室委托单的后续环节。
- SAP 复杂推送：属于系统集成模块，不在 MVP 范围。
- 自动审核和自动放行：当前系统不支持，需要人工确认。
- 实时状态查询：当前 AI 不支持直接查询数据库。

---

---
chunk_id: 01-006
module: raw_material_sampling
doc_type: business_overview
role: driver,sampler
question_type: trigger_method
visibility: public
source_doc: 01_raw_material_business_overview.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## 一卡通签到自动触发取样计划

司机驾驶运料车到达牧场/饲料厂门口，在一卡通系统上签到。签到成功后，一卡通系统会自动通知 LIMS 质检系统，LIMS 系统为这车原料自动创建一条原料取样计划。

具体过程：
1. 司机在一卡通系统完成签到。
2. 一卡通系统调用 LIMS 接口（/ranch/plan/notifySignIn），把签到信息发送给 LIMS。
3. LIMS 自动创建原料取样计划记录：记录司机姓名和车牌号、签到时间（作为进厂时间）、一卡通签到 ID（sign_in_id），初始状态为"未取样"（status=1）。
4. 如果司机随车带有检验报告，一卡通系统会把报告文件一并传给 LIMS 保存。

重要：签到只是创建了取样计划，不等于已经取样。采样员仍然需要根据这条取样计划去现场完成实际的样品采集。

---

---
chunk_id: 01-007
module: raw_material_sampling
doc_type: business_overview
role: sampler
question_type: trigger_method
visibility: public
source_doc: 01_raw_material_business_overview.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## 移动端手动创建原料取样计划

采样员或其他工作人员可以在钉钉小程序上手动填写原料取样信息，创建取样计划。

适用情况：一卡通系统未触发自动创建（如签到失败、系统故障）、临时或补录的原料取样需求。

具体过程：
1. 打开钉钉小程序中的原料取样页面：饲料厂人员使用"饲料厂检验 → 原料取样"页面；牧场人员使用"牧场检验"中的原料取样功能。
2. 手动填写供应商编码和名称、司机姓名和车牌号、进厂时间。
3. 提交后系统创建原料取样计划记录，初始状态同样为"未取样"（status=1）。
4. 后续的取样、化验、判定、放行流程与一卡通自动触发完全相同。

---

---
chunk_id: 01-008
module: raw_material_sampling
doc_type: business_overview
role: sampler,admin
question_type: comparison
visibility: public
source_doc: 01_raw_material_business_overview.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## 两种触发方式对比

一卡通签到触发与移动端手动创建的核心区别：

创建者不同：一卡通签到触发由系统自动创建，移动端手动创建由人工在钉钉小程序中创建。

是否有签到 ID：一卡通自动创建的有 sign_in_id，手动创建的没有。这导致一卡通能否查询放行状态不同——有签到 ID 的能查，没有签到 ID 的可能无法查询（待确认）。

适用场景不同：一卡通自动触发适用于正常签到进厂；手动创建适用于补录、故障恢复、临时需求。

后续流程完全相同：两种方式创建的取样计划，在取样、化验、审核、判定、放行环节的处理方式完全一样。

---

---
chunk_id: 01-009
module: raw_material_sampling
doc_type: business_overview
role: all
question_type: process_flow
visibility: public
source_doc: 01_raw_material_business_overview.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## 原料取样完整主流程（8步）

以"一车玉米运到牧场"为例的完整业务流程：

第一步：生成原料取样计划。司机在一卡通签到，系统自动创建取样计划，状态为"未取样"。如果一卡通没有自动生成，采样员可在钉钉上手动创建。

第二步：采样员完成取样。采样员在钉钉小程序上看到"未取样"的计划，前往现场采集原料样品，在系统中录入样品基本信息。此时计划状态从"未取样"变为"已取样"。

第三步：系统生成样品记录。系统为样品创建正式记录，标记样品类型为"原料"（sampling_type=3）。

第四步：系统生成检测项目。系统根据物料编码和标准配置，自动确定需要检测哪些项目（如玉米检测水分、粗蛋白、黄曲霉毒素等）。每个检测项目生成一条待化验任务。

---

---
chunk_id: 01-010
module: raw_material_sampling
doc_type: business_overview
role: tester,reviewer,driver
question_type: process_flow
visibility: public
source_doc: 01_raw_material_business_overview.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## 原料取样流程：化验到放行（第5-8步）

第五步：化验员录入检测结果。化验员在 PC 端勾选同一检测项目的样品，点击"开始化验"，系统生成化验单。化验员逐项录入检测结果后点击"提交"，化验单状态变为"待审核"。

第六步：系统判定合格或不合格。审核员确认数据无误后点击"审核通过"。系统自动将检测结果与质量标准比对——结果在标准范围内判定为"合格"，超出标准范围判定为"不合格"。如有不合格项目，系统自动生成不合格品处理记录。

第七步：全部合格后允许放行。当一车原料的所有检测项目都判定为合格后，系统标记为"已放行"（is_release = 1），原料可以入库使用。只要还有一个样品结果未出或有一个项目不合格，就不能放行。

第八步：一卡通系统可以查询放行状态。一卡通通过接口查询某次签到对应的原料是否已放行。

---

---
chunk_id: 01-011
module: raw_material_sampling
doc_type: business_overview
role: all
question_type: business_object
visibility: public
source_doc: 01_raw_material_business_overview.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## 核心业务对象：原料取样计划

每来一车新原料，系统就会生成一份"取样计划"，相当于一个"取样任务单"。它记录了这车原料的基本信息——是谁送来的、哪辆车运的、什么时候到的，以及当前处理到了什么阶段。一份取样计划代表"一车原料的一次进厂质检任务"。

系统中对应数据表：op_sampling_plan。

取样计划的关键状态：
- 未提交（0）：草稿，还没正式生效。
- 未取样（1）：计划已生效，采样员需要去取样。这是签到后或手动创建后的初始状态。
- 已取样（2）：采样员已经完成了现场采样。
- 已作废（3）：这个计划被取消了，不再需要处理。

常见用户问题："取样计划显示未取样是什么意思？"说明采样员还没有去现场采样。"取样计划已经作废了怎么办？"需要重新创建新的取样计划。

---

---
chunk_id: 01-012
module: raw_material_sampling
doc_type: business_overview
role: sampler,tester
question_type: business_object
visibility: public
source_doc: 01_raw_material_business_overview.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## 核心业务对象：样品与检测项目

样品：采样员从运料车上实际采集的那份原料就是"样品"。一个取样计划下可以有多个样品（不同位置、不同深度）。每个样品都有唯一的样品编号（sample_no），后续化验都围绕样品进行。系统中对应数据表：op_sampling_plan_sample。样品类型为"原料"（sampling_type=3）。合格状态（whether_qualified）：A1 表示合格，R1 表示不合格。

检测项目：对一份原料样品需要检测的指标——如水分含量、粗蛋白含量、黄曲霉毒素。不同原料的检测项目不同：玉米主要关注水分和毒素，豆粕主要关注蛋白含量。检测项目由系统根据标准配置自动确定。系统中对应数据表：op_sampling_plan_item。每个检测项目都包含项目名称、检测结果、上下限标准。

---

---
chunk_id: 01-013
module: raw_material_sampling
doc_type: business_overview
role: tester,reviewer
question_type: business_object
visibility: public
source_doc: 01_raw_material_business_overview.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## 核心业务对象：检测结果与合格判定

检测结果：化验员在实验室完成检测后录入系统的具体数值（如"水分：14.5%"）或定性结论（如"黄曲霉毒素：阴性"）。系统中化验单主表（op_test_result_base）记录一次化验的整体信息，化验结果明细表（op_test_result_info）记录每个样品每个检测项目的具体结果。

化验单状态流转：待提交（2）=化验员正在录入，未提交；待审核（3）=化验员已提交，等待审核员确认；已审核（4）=审核员已确认，结果最终有效。

合格判定：审核员点击"审核通过"时系统自动将检测结果与质量标准比对。判定依据存储在 bs_invbill_item_standard 表中。A1=合格（在标准范围内），R1=不合格（超出标准范围）。A2（可接收）和 R2（不合格退货）在原料流程中是否使用待确认。

---

---
chunk_id: 01-014
module: raw_material_sampling
doc_type: business_overview
role: all
question_type: business_object
visibility: public
source_doc: 01_raw_material_business_overview.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## 核心业务对象：放行状态

当一车原料的所有检测项目全部合格后，系统标记这车原料为"已放行"，意思是这车原料可以卸车入库、投入使用了。放行是原料进厂质检流程的最终目标。

放行状态的值：未放行（0）=还不满足放行条件（可能还没取样、还没化验完成、有不合格项）；已放行（1）=所有检测合格，可以入库使用。

谁可以查询放行状态：一卡通系统可以通过签到 ID 查询对应车辆的放行状态；LIMS 系统内可以查看取样计划的放行状态。

常见问题："我的原料什么时候能放行？"需要等所有检测项目完成且全部合格后才会放行。"为什么还没放行？"可能原因包括还没取样、化验未完成、结果未审核、或有不合格项。"谁负责放行？"待确认是系统自动放行还是需要人工点击放行。

---

---
chunk_id: 01-015
module: raw_material_sampling
doc_type: business_overview
role: driver,sampler,dispatcher
question_type: misconception
visibility: public
source_doc: 01_raw_material_business_overview.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## 常见误解（第1-5条）

误解1：司机签到就等于已经放行。实际情况：签到只是触发了取样计划创建，还需要经过取样、化验录入、结果审核、合格判定等多个环节。签到只代表"开始处理"，放行才代表"处理完成"。

误解2：有取样计划就等于已经取样。实际情况：状态为"未取样（status=1）"时，表示计划已创建但采样员还没有去现场采样。

误解3：已取样就等于已经化验完成。实际情况：取样只是把样品采集下来，还需要送到化验室由化验员排队检测。

误解4：化验完成不等于一定合格。实际情况：化验完成只说明结果出来了，但结果可能合格也可能不合格。

误解5：合格判定不等于一定已经同步到外部系统。实际情况：一卡通系统需要主动查询才能获取最新放行状态，不是实时推送。

---

---
chunk_id: 01-016
module: raw_material_sampling
doc_type: business_overview
role: all
question_type: misconception
visibility: public
source_doc: 01_raw_material_business_overview.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## 常见误解（第6-10条）

误解6：AI 当前不能查询某辆车的实时放行状态。AI 没有连接实时数据库的权限，只能告诉你放行状态的含义和放行条件。

误解7：AI 当前不能自动放行。放行涉及质量判定和审核确认，AI 不能代替人工操作。

误解8：原料流程不要和中心化验室委托单流程混淆。原料取样是牧场/饲料厂自己检验自己进的原料；中心化验室是外部单位送样到检测中心委托检验。两者使用不同系统模块、不同数据表、不同操作页面。

误解9：一车原料只需要取一个样品。实际可能需要取多个样品（不同位置、不同深度），每个独立编号、独立化验，全部合格才能放行。实际取样份数规则待确认。

误解10：化验结果出来后不需要审核就能放行。化验员提交后化验单为"待审核"，必须审核员确认后结果才生效。

---

---
chunk_id: 01-017
module: raw_material_sampling
doc_type: business_overview
role: all
question_type: ai_boundary
visibility: public
source_doc: 01_raw_material_business_overview.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## AI 客服可以回答的问题类型

在原料取样模块中，AI 可以回答以下类型的问题：

- 状态含义：解释"未取样""待审核""已放行"等状态值的业务含义。
- 操作流程：说明原料进厂后要经过哪些步骤、谁负责取样、如何操作。
- 为什么不能放行：列举可能导致未放行的原因（未取样、化验中、待审核、有不合格项等）。
- 为什么还要等待：说明取样后还需要经过化验、审核等环节。
- 下一步找谁：根据当前状态建议联系采样员、化验员或审核员。
- 概念区分：帮助区分原料取样和中心化验室等不同模块。
- 一卡通关系：说明一卡通签到触发取样计划的机制。
- 放行条件：说明全部检测合格才能放行的条件。

---

---
chunk_id: 01-018
module: raw_material_sampling
doc_type: business_overview
role: all
question_type: ai_boundary
visibility: public
source_doc: 01_raw_material_business_overview.md
status_related: false
realtime_required: true
ai_can_answer: false
---

## AI 客服不能回答的问题类型

以下问题 AI 不能回答：

- 实时状态查询：如"帮我查车牌沪A12345现在放行了没有？"AI 不连接实时数据库。
- 实时检测结果：如"32号样品的粗蛋白含量是多少？"AI 无法查询具体检测数值。
- 自动审核、自动修改数据、自动放行：这些操作需要人工判断和确认。
- 实时数据库查询：如"现在还有多少车原料没放行？"无法提供实时统计。
- 中心化验室问题：如"我的委托单状态是什么？"属于另一个模块。
- 奶源质检问题：如"这批牛奶的质检结果出来了吗？"使用独立系统。
- 预测时间：如"化验什么时候能完成？"取决于化验员工作安排，AI 无法预测。
- SAP/外部系统问题：外部系统集成不在 AI 当前覆盖范围。

---

---
chunk_id: 01-019
module: raw_material_sampling
doc_type: business_overview
role: all
question_type: status_reference
visibility: public
source_doc: 01_raw_material_business_overview.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## 原料取样关键状态速查表

原料取样计划状态（op_sampling_plan.status）：
0=未提交（草稿，未生效）→ 提交后变为"未取样"
1=未取样（等待采样员取样）→ 采样员去现场取样
2=已取样（样品已采集）→ 化验员进行检测
3=已作废（计划取消）→ 如需检测需重新创建

放行状态（op_sampling_plan.is_release）：
0=未放行（不满足放行条件）
1=已放行（全部检测合格，可以入库使用）

化验单状态（op_test_result_base.status）：
2=待提交（化验员正在录入结果）
3=待审核（化验员已提交，等待审核员确认）
4=已审核（审核通过，结果生效，系统自动判定合格与否）

合格判定（op_sampling_plan_sample.whether_qualified）：
A1=合格（可以放行）
R1=不合格（不能放行，需要处理不合格品）

---

Now processing document 02...
