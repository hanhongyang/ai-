# RAG Chunks from 02_raw_material_role_matrix.md

---

---
chunk_id: 02-001
module: raw_material_sampling
doc_type: role_matrix
role: all
question_type: role_overview
visibility: public
source_doc: 02_raw_material_role_matrix.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## 原料取样模块涉及的全部6个角色

本模块涉及以下6个角色，覆盖从原料进厂签到到最终放行的完整链路：

| 序号 | 角色 | 来源 | 使用端 | 在流程中的位置 |
|------|------|------|--------|--------------|
| 1 | 司机 | 代码明确：`driver_name`、`driver_code`字段+一卡通签到接口 | 一卡通系统 | 流程起点：签到触发取样计划 |
| 2 | 采样员 | 代码明确：`sampler`字段+移动端采样页面 | 钉钉小程序（移动端） | 取样环节：现场采集样品 |
| 3 | 化验员 | 代码明确：`tester`/`test_user`字段+化验单操作 | PC端管理后台 | 化验环节：录入检测结果 |
| 4 | 审核员/质检主管 | 代码明确：`reviewer`/`examine_user`字段+审核接口 | PC端管理后台 | 判定环节：审核结果、确认放行 |
| 5 | 调度员 | 【业务推测】：文档未明确命名此角色，但"车辆不能放行时需要排查卡点"的场景在业务中常见 | 一卡通系统/LIMS | 协调环节：跟进车辆放行进度 |
| 6 | 系统管理员 | 代码明确：若依框架`sys_user`角色体系+基础数据配置 | PC端管理后台 | 支撑环节：配置标准、维护系统 |

---

---
chunk_id: 02-002
module: raw_material_sampling
doc_type: role_matrix
role: driver
question_type: role_concern
visibility: public
source_doc: 02_raw_material_role_matrix.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## 司机关心的问题

司机是流程的起点。司机最关心的是"我什么时候可以走"，具体问题包括：

- 我已经在一卡通签到了，为什么还不能卸车/离开？
- 为什么还没有放行？还要等多久？
- 签到之后还要做什么？不是签到就行了吗？
- 为什么还要等人来取样？能不能快一点？
- 取样完了为什么还要等化验结果？
- 我的原料检测结果合不合格？谁告诉我？
- 一卡通查不到放行状态是怎么回事？
- 不放行我能不能先把货卸了？

以上是司机在原料取样流程中的核心关切点，所有问题最终都指向一个目标：尽快完成放行、可以卸车离开。司机的问题本质上是流程进度问题，而非技术操作问题。

---

---
chunk_id: 02-003
module: raw_material_sampling
doc_type: role_matrix
role: driver
question_type: ai_scope
visibility: public
source_doc: 02_raw_material_role_matrix.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## 司机可以问AI什么——AI可以回答的内容

针对司机，AI可以回答的内容聚焦在流程解释和下一步指引：

关于签到后还要做什么：您在一卡通签到后，系统已经自动创建了这车原料的取样计划。接下来需要等采样员到现场采集样品，然后化验员进行检测。所有检测项目都合格后，系统才会标记"已放行"，届时您才能卸车离开。

关于为什么还没放行：没有放行可能原因包括采样员还没有到现场取样、样品已取但化验结果还没出来、化验结果已出但审核员还没有审核、检测结果中有不合格项目需要处理。建议联系现场采样员或化验员确认卡在哪一步。

关于放行状态的含义："未放行"表示取样、化验或审核环节尚未全部完成。"已放行"表示所有检测合格，可以正常卸车入库。

关于流程顺序：签到只是第一步。完整流程是签到→取样→化验→审核→放行。每个环节都需要相应工作人员完成操作。

---

---
chunk_id: 02-004
module: raw_material_sampling
doc_type: role_matrix
role: driver
question_type: ai_boundary
visibility: internal
source_doc: 02_raw_material_role_matrix.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## 对司机AI不应回答的内容及推荐风格

对司机角色，AI不应回答以下内容：
- 具体的接口和技术细节：不要告诉司机`/ranch/plan/notifySignIn`接口、`sign_in_id`字段等技术信息。
- 数据库表名和字段名：不要提`op_sampling_plan`、`op_sampling_plan_sample`等表名。
- 判定标准和计算公式：不要解释水分含量上限是多少、如何计算不合格等细节。
- 其他角色的操作页面路径：不要告诉司机化验员在哪里录入结果、审核员用哪个页面。
- 系统配置和管理内容：不要告诉司机检测标准如何配置。
- 推测放行时间：不要承诺"大概还要等30分钟"——AI无法知道化验进度。
- 承诺操作结果：不要说"已经放行了"、"马上就能走"——AI不连接实时数据。
- 中心化验室/奶源等其他模块内容：司机问的如果是送样到检测中心，那属于另一个模块。

推荐回答风格：短、直接、告诉下一步。一句结论说清当前情况，一个原因解释为什么会这样，一个下一步告诉司机应该找谁做什么。

示例：您的签到已成功，取样计划已生成。目前还在等待采样员到现场取样。取样完成后还需要化验和审核，全部通过后才能放行。如有疑问，请联系现场采样员。

---

---
chunk_id: 02-005
module: raw_material_sampling
doc_type: role_matrix
role: sampler
question_type: role_concern
visibility: public
source_doc: 02_raw_material_role_matrix.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## 采样员关心的问题

采样员负责现场采集样品，在移动端（钉钉小程序）上操作。采样员关心的问题包括：

- 司机签到后，我在哪里能看到新的取样计划？
- 为什么我在钉钉上看不到某条取样计划？是不是一卡通没通知过来？
- "未取样"状态是什么意思？我需要做什么？
- 原料取样需要填写哪些信息？哪些是必填的？
- 手动创建取样计划和签到自动创建有什么区别？
- 我取样完成后，系统会自动通知化验员吗？
- 样品编号是怎么生成的？我可以自己修改吗？
- 取样完成后发现有错误，还能修改样品信息吗？
- 一个取样计划可以取几个样品？
- 饲料厂的原料取样和牧场的原料取样操作一样吗？

采样员的核心关注点是：如何高效完成现场采样任务、如何理解取样计划的状态、以及操作完成后的流程衔接。

---

---
chunk_id: 02-006
module: raw_material_sampling
doc_type: role_matrix
role: sampler
question_type: ai_scope
visibility: public
source_doc: 02_raw_material_role_matrix.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## 采样员可以问AI什么——操作指引和状态解释

针对采样员，AI可以回答的内容聚焦在操作指引和状态解释：

关于取样计划来源：原料取样计划有两种来源。一是司机在一卡通签到后系统自动创建，可在钉钉小程序中查看；二是可在钉钉小程序中手动创建，适用于补录或一卡通未触发的情况。两种方式创建的取样计划后续流程完全一样。

关于"未取样"状态："未取样"（显示为状态1）表示取样计划已经生效，等待去现场采集样品。采集完成并录入样品信息后，状态会变为"已取样"（状态2）。

关于操作页面：饲料厂采样员请在钉钉小程序中进入"饲料厂检验→原料取样"页面。牧场采样员请在钉钉小程序中进入"牧场检验"中的原料取样功能。

关于样品编号：样品编号由系统自动生成，用于唯一标识每个样品。建议不要手动修改样品编号，以免影响后续化验结果和放行状态的关联。

关于取样完成后的流程：取样完成后，系统会为样品生成检测项目。化验员在PC端可以看到待化验的任务并开始检测。取样完成后不会自动通知化验员，化验员需要主动查看待化验列表。

---

---
chunk_id: 02-007
module: raw_material_sampling
doc_type: role_matrix
role: sampler
question_type: ai_boundary
visibility: internal
source_doc: 02_raw_material_role_matrix.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## 对采样员AI不应回答的内容及推荐风格

对采样员角色，AI不应回答以下内容：
- 后台数据库操作：不要告诉采样员如何直接修改数据库中的样品信息。如需修改，应通过正常操作流程。
- 化验员的操作细节：不要解释化验员如何使用PC端录入结果的具体步骤。
- 审核员的判定规则：不要解释审核员如何审核、审核标准是什么。
- 系统接口调用细节：不要解释一卡通接口的参数格式和调用方式。
- 化验不合格的具体原因：采样员通常不需要知道某个样品为什么不合格的技术细节。
- 放行操作的具体方法：放行不在采样员的职责范围内。
- 中心化验室委托单流程：如果采样员问的是送样到检测中心，那属于另一个模块。
- 实时查询数据库：不要说"我帮你查一下这条取样计划的状态"——AI不能查询实时数据。

推荐回答风格：当前状态 + 操作入口 + 下一步。先告诉采样员取样计划现在处于什么状态，然后告诉采样员在哪个页面、哪个功能中操作，最后告诉采样员操作完成后会发生什么。

示例：该取样计划当前状态为"未取样"。请在钉钉小程序中进入原料取样页面，找到该计划，完成现场采样并录入样品信息。完成后状态会自动变为"已取样"，化验员即可开始检测。

---

---
chunk_id: 02-008
module: raw_material_sampling
doc_type: role_matrix
role: tester
question_type: role_concern
visibility: public
source_doc: 02_raw_material_role_matrix.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## 化验员关心的问题

化验员在PC端管理后台操作，负责录入检测结果。化验员关心的问题包括：

- 我在哪里可以看到待化验的任务？
- 为什么我只能看到部分样品的待化验任务？
- 开始化验时为什么要选择相同检测项目的样品？
- 化验单的状态流转是怎样的？
- 我录入检测结果后，是直接提交还是可以先保存草稿？
- 提交后还能修改检测结果吗？
- 为什么我的化验单不能提交？
- 化验单提交后谁审核？审核多久？
- 什么情况下检测项目会被判为不合格？
- 不合格的结果会触发什么？
- 检测结果录入正确但判定不合格，我该怎么办？
- 审核退回后我需要做什么？

化验员的核心关注点是：如何正确完成检测结果的录入与提交、化验单的状态如何流转、提交后出现问题如何处理。

---

---
chunk_id: 02-009
module: raw_material_sampling
doc_type: role_matrix
role: tester
question_type: ai_scope
visibility: public
source_doc: 02_raw_material_role_matrix.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## 化验员可以问AI什么——化验流程指引和状态解释

针对化验员，AI可以回答的内容聚焦在化验流程指引和状态解释：

关于待化验任务：当采样员完成取样后，系统会根据物料类型和所在牧场的标准配置，自动为该样品生成需要检测的项目。这些检测项目会出现在待化验列表中，需要在PC端管理后台查看。

关于开始化验：在待化验列表中，需要勾选属于同一检测项目的样品，然后点击"开始化验"。系统会为这些样品创建一张化验单。注意：同一次开始化验的样品必须属于相同的检测项目。

关于化验单状态流转：点击"开始化验"后，化验单状态为"待提交"，此时可以录入检测结果。录入完成并确认无误后点击"提交"，状态变为"待审核"，结果已提交但尚未生效。审核员审核通过后，状态变为"已审核"，结果正式生效，系统会自动判定合格与否。

关于提交后能否修改：提交后（状态为"待审核"）如果发现数据有误，需要等待审核员审核退回或联系审核员进行退回处理。退回后化验单状态回到"待提交"，可修改后重新提交。审核通过后如需修改，需通过"结果变更"功能，变更会记录日志。

关于不合格判定：不合格判定是在审核员审核通过时由系统自动进行的。系统将录入的检测结果与预设的质量标准范围进行比对，超出标准范围则标记为不合格，并自动生成不合格品记录。

关于审核退回：如果审核员退回化验单，状态从"待审核"回到"待提交"。需根据审核员填写的退回原因，检查并修正检测结果后重新提交。

---

---
chunk_id: 02-010
module: raw_material_sampling
doc_type: role_matrix
role: tester
question_type: ai_boundary
visibility: internal
source_doc: 02_raw_material_role_matrix.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## 对化验员AI不应回答的内容及推荐风格

对化验员角色，AI不应回答以下内容：
- 具体的判定标准和阈值：不要告诉化验员某种物料的水分上限是多少——应引导化验员在系统中查看标准配置。
- 审核员的审核依据和判断逻辑：不要把审核员的专业判断过程解释给化验员。
- 后台数据表结构和SQL逻辑：不要解释`op_test_result_info`的字段映射和存储机制。
- SQL查询操作：不要说"你可以通过SQL查询来检查"。
- 系统间接口调用：不要解释SAP推送、一卡通查询等接口的技术细节。
- 修改不合格判定结果的方法：不要暗示可以通过非正常流程修改判定结果。
- 中心化验室的化验流程：中心化验室使用不同的化验单表（`op_jczx_feed_result_base`），流程不同，不要混淆。
- 实时数据查询承诺：不要承诺"我帮你查一下这个样品的结果"。

推荐回答风格：检测项状态 + 录入/提交条件 + 注意事项。告诉化验员当前任务处于什么阶段，说明什么情况下可以做什么操作，提醒可能影响判定结果的因素。

示例：您的化验单当前状态为"待提交"，检测结果尚未生效。请确认所有检测项目的结果已录入完毕且数据无误后，点击"提交"。提交后化验单将进入"待审核"状态。如果审核退回，请根据退回原因修正结果后重新提交。

---

---
chunk_id: 02-011
module: raw_material_sampling
doc_type: role_matrix
role: reviewer
question_type: role_concern
visibility: public
source_doc: 02_raw_material_role_matrix.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## 审核员/质检主管关心的问题

审核员负责审核化验结果并确认判定。审核员关心的问题包括：

- 我在哪里可以看到待审核的化验单？
- 什么情况下可以审核通过？我需要检查哪些内容？
- 审核通过后系统会自动做什么？
- 审核通过后如果发现判错了，还能改吗？
- 不合格的检测结果会触发什么后续流程？
- 审核退回后化验员会收到通知吗？
- 什么时候算是"放行"？放行是审核通过就自动放行，还是需要我再操作一次？
- 所有检测项目都合格后，放行状态是自动更新的吗？
- 一卡通系统什么时候能查到放行状态？

审核员的核心关注点是：审核通过后系统会触发哪些自动化操作、放行的时机和机制是什么、审核出现问题时如何纠正。

---

---
chunk_id: 02-012
module: raw_material_sampling
doc_type: role_matrix
role: reviewer
question_type: ai_scope
visibility: public
source_doc: 02_raw_material_role_matrix.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## 审核员可以问AI什么——审核影响和流程衔接

针对审核员，AI可以回答的内容聚焦在审核影响和流程衔接：

关于审核操作的影响：当点击"审核通过"后，系统会执行以下操作：
1. 化验单状态变为"已审核"，检测结果正式生效。
2. 系统自动将每项检测结果与质量标准比对，判定合格或不合格。
3. 如果有不合格项目，系统会自动生成不合格品处理记录。
4. 全部项目合格后，取样计划的放行状态会更新为"已放行"。
5. 一卡通系统此时可以通过签到ID查询到放行状态。

关于审核退回：如果发现化验数据有问题，可以点击"审核退回"，填写退回原因。退回后化验单状态回到"待提交"，化验员需要修正数据后重新提交。

关于放行的时机：【待确认】放行是审核通过后系统自动更新，还是需要手动点击"放行"按钮。当前文档显示"所有样品检测合格后，更新is_release='1'"，但未明确触发方式。建议关注取样计划的放行状态字段变化。

关于一卡通查询放行：一卡通系统通过调用LIMS的查询接口（根据签到ID查询）来获取放行状态。只要LIMS系统中的放行状态已更新，一卡通系统就能查到。

关于审核通过后发现问题：审核通过后如需修改检测结果，需使用"结果变更"功能。变更操作会记录完整的变更日志（包括原值、新值、变更原因），以便追溯。

---

---
chunk_id: 02-013
module: raw_material_sampling
doc_type: role_matrix
role: reviewer
question_type: ai_boundary
visibility: internal
source_doc: 02_raw_material_role_matrix.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## 对审核员AI不应回答的内容及推荐风格

对审核员角色，AI不应回答以下内容：
- 具体的质量标准数值：不要告诉审核员某个物料的标准阈值是多少——应引导在系统标准配置中查看。
- 化验员的操作指导：不要解释化验员如何录入结果的操作细节。
- 后台数据库操作：不要建议通过直接修改数据库来改变审核结果。
- 系统代码实现逻辑：不要解释Spring事务、分布式锁等技术机制。
- 中心化验室的审核流程：中心化验室有独立的审核机制，不要在原料流程中混入。
- SAP推送的技术细节：原料流程中SAP推送不属于MVP范围。
- 代为判断审核结果：不要帮审核员判断"这个结果应该判合格还是不合格"。
- 承诺已执行的操作：不要说"已经审核通过了"——AI不能确认系统操作是否完成。

推荐回答风格：审核影响 + 自动联动 + 注意事项。说明审核通过/退回后会发生什么，说明系统会自动触发哪些操作（判定、不合格品、放行），提醒审核前需要确认的关键点。

示例：审核通过后，系统会自动将检测结果与质量标准比对。全部合格则更新放行状态为"已放行"，一卡通系统可查询到。如有不合格项目，系统会自动生成不合格品记录。审核前请确认所有检测数据无误。

---

---
chunk_id: 02-014
module: raw_material_sampling
doc_type: role_matrix
role: dispatcher
question_type: role_concern
visibility: public
source_doc: 02_raw_material_role_matrix.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## 调度员关心的问题

调度员【业务推测】是负责协调车辆进出和卸货安排的角色，需要了解放行进度。调度员关心的问题包括：

- 为什么这辆车还不能放行？卡在哪一步了？
- 当前流程是卡在取样、化验还是审核？
- 应该联系采样员还是化验员来推进？
- 一卡通为什么查不到这辆车的放行状态？
- 一天有多少车已经放行、多少还没放行？
- 某车签到很久了，为什么取样计划还是"未取样"？
- 系统能不能设置自动提醒（比如超时未取样就通知）？

调度员的核心关注点是：快速定位车辆未放行的具体卡点、知道应该联系谁来推动流程、了解整体放行进度。调度员的角色基于业务场景推断，代码文档中未明确命名此角色。

---

---
chunk_id: 02-015
module: raw_material_sampling
doc_type: role_matrix
role: dispatcher
question_type: ai_scope
visibility: public
source_doc: 02_raw_material_role_matrix.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## 调度员可以问AI什么——卡点排查和角色对应

针对调度员，AI可以回答的内容聚焦在卡点排查和角色对应：

关于流程卡点排查：车辆未放行，可以按以下顺序排查卡点：
1. 确认取样计划状态：如果是"未取样"→ 联系采样员。
2. 确认化验单状态：如果是"待提交"→ 联系化验员。
3. 确认化验单状态：如果是"待审核"→ 联系审核员。
4. 如果已经审核通过但仍未放行 → 可能有不合格项目，需要查看不合格品记录。

关于各状态对应的角色：
- "未取样"状态 → 采样员需要去现场采样。
- 化验单"待提交" → 化验员正在录入或尚未提交结果。
- 化验单"待审核" → 审核员需要审核确认。
- 有不合格项目 → 需要质检主管决定如何处理。

关于一卡通查询不到放行：一卡通查询不到放行状态，可能是因为一卡通的签到ID与LIMS取样计划未正确关联、LIMS系统中该取样计划尚未达到"已放行"状态、或者是手动创建的取样计划没有签到ID导致一卡通无法查询。

---

---
chunk_id: 02-016
module: raw_material_sampling
doc_type: role_matrix
role: dispatcher
question_type: ai_boundary
visibility: internal
source_doc: 02_raw_material_role_matrix.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## 对调度员AI不应回答的内容及推荐风格

对调度员角色，AI不应回答以下内容：
- 实时统计数据：不要承诺"今天有50车已放行、10车未放行"——AI不能查询实时数据库。
- 具体操作指导：不要告诉调度员如何在系统中执行取样、化验、审核操作。
- 系统配置和接口：不要解释一卡通接口的技术参数。
- 承诺催促结果：不要说"我会通知采样员尽快取样"——AI不能自动发送通知。
- 预测完成时间：不要估算"大概还要2小时"。
- 中心化验室或其他模块：如果问的是送样到检测中心的结果，不属于本模块。

推荐回答风格：可能卡点 + 排查顺序 + 找谁。根据取样计划和化验单的状态列出可能原因，建议从取样→化验→审核→放行依次排查，每个卡点对应哪个角色。

示例：该车原料可能卡在取样环节——取样计划状态为"未取样"，说明采样员尚未到现场采集样品。建议联系采样员确认取样时间。如果是化验环节，请联系化验员；如果是审核环节，请联系审核员。

---

---
chunk_id: 02-017
module: raw_material_sampling
doc_type: role_matrix
role: admin
question_type: role_concern
visibility: internal
source_doc: 02_raw_material_role_matrix.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## 系统管理员关心的问题

系统管理员负责系统配置、数据维护和问题排查。管理员关心的问题包括：

- 原料取样涉及哪些接口？接口路径和功能是什么？
- 取样计划的状态码（0/1/2/3）分别代表什么含义？
- 化验单的状态码（2/3/4）分别代表什么？
- 放行状态（0/1）如何定义？
- 一卡通签到通知接口（`/ranch/plan/notifySignIn`）如何工作？
- 一卡通如何查询放行状态（`/ranch/plan/isRelease/{signInId}`）？
- 原料取样涉及哪些数据库表？表之间如何关联？
- 样品类型sampling_type中3（原料）的定义是什么？
- 检测标准（`bs_invbill_item_standard`）如何配置？
- 软删除（delete_id/is_delete）机制如何工作？
- 如果取样计划或化验单需要作废，应该如何操作？

管理员的核心关注点是：系统接口和状态码等技术信息、数据库表结构及关联关系、系统配置和运维机制。

---

---
chunk_id: 02-018
module: raw_material_sampling
doc_type: role_matrix
role: admin
question_type: ai_scope
visibility: internal
source_doc: 02_raw_material_role_matrix.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## 管理员可以问AI什么——接口和状态码

针对系统管理员，AI可以回答来自知识库的技术信息，但须明确说明信息来源和边界。

关于接口（来自逆向文档）：
- `POST /ranch/plan/notifySignIn`：接收一卡通签到通知，自动创建取样计划。由`OpSamplingPlanController.receiveSignInNotification()`处理。
- `POST /ranch/plan/add`（路径待确认）：手动新增原料取样计划。
- `GET /ranch/plan/isRelease/{signInId}`（路径待确认）：一卡通查询放行状态。
- `POST /ranch/testResult/startTest`：开始化验，批量转化待检任务为化验单。
- `PUT /ranch/testResult/submit`：提交化验单（待提交→待审核）。
- `PUT /ranch/testResult/approve/{id}`：PC端审核通过（待审核→已审核）。
- `PUT /ranch/testResult/reject`：审核退回（待审核→待提交）。
- `PUT /ranch/testResult/resultChange`：检测结果变更（含变更日志）。
以上信息来自逆向技术文档，具体路径以实际代码为准。

关于状态码：
- 取样计划状态（`op_sampling_plan.status`）：0=未提交，1=未取样，2=已取样，3=已作废。
- 化验单状态（`op_test_result_base.status`）：2=待提交，3=待审核，4=已审核。
- 放行状态（`op_sampling_plan.is_release`）：0=未放行，1=已放行。
- 样品合格判定（`op_sampling_plan_sample.whether_qualified`）：A1=合格，R1=不合格。

---

---
chunk_id: 02-019
module: raw_material_sampling
doc_type: role_matrix
role: admin
question_type: ai_scope
visibility: internal
source_doc: 02_raw_material_role_matrix.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## 管理员可以问AI什么——数据库表和一卡通关联

关于数据库表：原料取样核心表包括：
- `op_sampling_plan`：原料取样计划主表。
- `op_sampling_plan_sample`：样品表（sampling_type='3'表示原料）。
- `op_sampling_plan_item`：样品检测项目明细表。
- `op_test_result_base`：化验单主表。
- `op_test_result_info`：化验结果明细表。
- `bs_invbill_item_standard`：物料项目标准表（判定依据）。

表之间通过ID字段关联：`op_sampling_plan` → `op_sampling_plan_sample`（通过sampling_plan_id）→ `op_sampling_plan_item`（通过sampling_plan_sample_id）→ `op_test_result_info`（通过plan_item_id）→ `op_test_result_base`（通过base_id）。

关于一卡通关联：一卡通签到通知接口收到请求后，LIMS会将一卡通的签到ID存入`op_sampling_plan.sign_in_id`，根据`destinationCode`查询LIMS的部门ID，将司机的姓名、车牌号、签到时间存入取样计划。如有随车检验报告附件，保存到服务器。一卡通后续通过签到ID查询放行状态。

关于软删除机制：系统采用软删除设计，所有"删除"操作都是逻辑标记而非物理删除。`op_sampling_plan`和`op_test_result_base`使用`delete_id`字段（未删除='0'，已删除=记录自身ID）。`op_sampling_plan_sample`使用`is_delete`字段（'0'=未删除，'1'=已删除）。这样的设计允许保留完整历史记录，同时支持删除后重建同名记录。

以上信息来自逆向技术文档，具体以实际系统代码为准。

---

---
chunk_id: 02-020
module: raw_material_sampling
doc_type: role_matrix
role: admin
question_type: ai_boundary
visibility: internal
source_doc: 02_raw_material_role_matrix.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## 对管理员AI不应回答的内容及推荐风格

对系统管理员角色，AI不应回答以下内容：
- 编造未经确认的字段：逆向文档中标记了【待确认】的内容，不要当成已确认的事实。如遇待确认项，应如实告知。
- 提供没有代码依据的接口路径：只回答逆向文档中明确出现的接口。
- 承诺系统已执行某操作：不要帮管理员判断"这个操作已经完成了"。
- 实时数据库查询结果：不要承诺"现在数据库里有100条未取样记录"。
- 猜测未在文档中出现的配置：如果文档没提到，不要编造。
- 中心化验室模块的内容：管理员的labtest相关问题应明确告知属于另一个模块。
- 前端页面组件的具体代码：逆向文档未覆盖前端代码细节，不要编造。
- SAP/ERP系统的具体对接配置：不属于MVP范围。

推荐回答风格：技术信息 + 来源说明 + 边界提示。给出状态码、接口、表结构的明确答案，明确指出信息来自逆向文档不代表系统实际运行状态，对于文档中未覆盖或标记待确认的内容主动说明。

示例：根据逆向技术文档，原料取样计划的状态码定义如下：0=未提交，1=未取样，2=已取样，3=已作废。`op_sampling_plan.status`字段存储该状态。注意：此信息来自系统代码逆向分析文档，`status='1'`（待化验状态）是否实际存在于`op_test_result_base`表中，目前标记为【待确认】。

---

---
chunk_id: 02-021
module: raw_material_sampling
doc_type: role_matrix
role: all
question_type: isolation_rule
visibility: internal
source_doc: 02_raw_material_role_matrix.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## 角色隔离规则表

AI在回答原料取样相关问题时，必须遵守以下角色隔离规则：

| 角色 | 可以回答的内容 | 不应回答的内容 | 回答粒度 |
|------|-------------|-------------|---------|
| 司机 | 流程顺序解释、未放行可能原因、下一步找谁 | 接口路径、表名、状态码、判定标准、操作页面、放行时间承诺、实时数据 | 一句话结论 |
| 采样员 | 取样计划状态含义、操作页面入口、手动/自动创建区别、样品信息填写要求 | 化验细节、审核规则、数据库操作、接口参数、中心化验室流程 | 状态+操作入口+下一步 |
| 化验员 | 化验单状态流转、提交条件、审核退回处理、不合格触发机制 | 判定标准数值、审核依据、SQL操作、SAP推送、中心化验室化验流程 | 状态+条件+注意事项 |
| 审核员 | 审核通过后的自动操作、不合格品生成机制、放行更新时机、退回流程 | 质量标准数值、化验操作细节、数据库操作、代为判断结果 | 审核影响+自动联动+注意事项 |
| 调度员 | 卡点排查顺序、状态对应角色、一卡通查询不到放行的可能原因 | 实时统计数据、操作指导、系统配置、预测时间 | 卡点+排查顺序+找谁 |
| 系统管理员 | 接口列表、状态码定义、表结构和关联关系、一卡通对接机制、软删除设计 | 编造未确认字段、承诺操作结果、实时查询、未覆盖前端代码细节 | 技术信息+来源说明+边界提示 |

核心原则：对低权限用户说业务语言，对高权限用户控制技术信息边界。不向非管理员用户暴露数据库表名、接口路径、状态码和技术实现细节。所有角色都不承诺实时数据和操作结果。

---

---
chunk_id: 02-022
module: raw_material_sampling
doc_type: role_matrix
role: all
question_type: overreach_risk
visibility: internal
source_doc: 02_raw_material_role_matrix.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## AI越权回答风险（第1-4项）

以下是AI在回答原料取样相关问题时必须严格避免的越权行为：

风险1——对司机暴露数据库表名。错误示例："您的取样计划在op_sampling_plan表中，状态字段是status。"正确做法是用业务语言解释："系统已经为您创建了取样计划，当前状态是'等待取样'。"

风险2——对普通用户解释后台接口。错误示例："一卡通系统调用了/ranch/plan/notifySignIn接口，参数包含destinationCode。"正确做法是只解释结果："一卡通签到后，系统自动收到了通知并创建了取样计划。"

风险3——承诺已经放行。错误示例："您的原料已经放行了，可以卸车了。"正确做法是说明放行条件："放行需要所有检测项目合格且审核通过。AI无法查询实时状态，建议您通过一卡通系统确认。"

风险4——编造实时检测结果。错误示例："根据系统数据，您的样品水分含量是14.5%，合格的。"正确做法是说明查询途径："检测结果请在LIMS系统中查看，或联系化验员获取。AI无法查询实时数据。"

---

---
chunk_id: 02-023
module: raw_material_sampling
doc_type: role_matrix
role: all
question_type: overreach_risk
visibility: internal
source_doc: 02_raw_material_role_matrix.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## AI越权回答风险（第5-8项）

以下是AI在回答原料取样相关问题时必须严格避免的越权行为（续）：

风险5——把管理员配置当成用户操作步骤。错误示例：对采样员说"您需要在bs_invbill_item_standard表中配置检测标准。"正确做法：采样员不需要关心配置，"检测项目的标准由系统管理员在后台配置，您只需按页面提示完成采样即可。"

风险6——把中心化验室流程混入原料取样流程。错误示例："您的委托单状态是待受理，需要检测中心接收样品。"正确做法是区分模块："原料取样和中心化验室委托单是两个不同的流程。如果您是送样到检测中心，那属于委托单流程，不在原料取样模块范围内。"

风险7——暗示AI可以执行系统操作。错误示例："我帮您把这条取样计划作废。"或"我帮您审核通过这个化验单。"正确做法是明确边界："AI不能执行任何系统操作。如需作废取样计划，请在LIMS系统中操作。"

风险8——透露系统内部判定规则细节。错误示例：对司机说"玉米水分的上限是15%，您的这批检测结果是16.2%，所以不合格。"正确做法是只给出结论性信息："检测结果中有项目超出了标准范围，具体项目和标准请咨询化验员或审核员。"

---

---
chunk_id: 02-024
module: raw_material_sampling
doc_type: role_matrix
role: all
question_type: overreach_risk
visibility: internal
source_doc: 02_raw_material_role_matrix.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## AI越权回答风险（第9-12项）

以下是AI在回答原料取样相关问题时必须严格避免的越权行为（续）：

风险9——对采样员解释化验员操作。错误示例："化验员需要先点击开始化验，然后录入m0、m1、m2等字段的值。"正确做法是聚焦采样员自己的操作："您取样完成后，样品会自动进入化验员的待化验列表。"

风险10——承诺其他角色的响应时间。错误示例："审核员通常会在30分钟内完成审核的。"正确做法是只说明流程不承诺时间："审核的完成时间取决于审核员的工作安排，AI无法预估具体时间。"

风险11——对非管理员用户解释软删除和retest_flag。错误示例：对化验员说"retest_flag='1'表示已归档，系统通过SQL LEFT JOIN过滤。"正确做法是使用业务语言："如果需要重新化验，请通过复检流程申请。"

风险12——把【待确认】的内容当作已确认事实。错误示例："放行是审核通过后系统自动执行的，不需要人工操作。"正确做法是如实说明："放行的触发方式目前标记为待确认，可能是审核通过后自动放行，也可能需要手动操作。"

---

---
chunk_id: 02-025
module: raw_material_sampling
doc_type: role_matrix
role: driver
question_type: answer_strategy
visibility: internal
source_doc: 02_raw_material_role_matrix.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## AI回答策略——面向司机

策略：一句结论 + 原因 + 找谁处理。

模板：
【结论】您的签到已成功，取样计划已生成。目前还在等待【采样/化验/审核】。
【原因】完整的流程是签到→取样→化验→审核→放行，当前环节尚未完成。
【下一步】建议您联系【采样员/化验员/审核员】确认当前进展。

原则：
- 一句话说明白。
- 不要展开解释系统内部逻辑。
- 始终告诉司机应该找谁。

核心定位：司机最关心"什么时候能走"，回答要直接给出状态结论，避免让司机接触任何系统内部细节。始终引导司机联系具体的现场人员而不是等待系统自动处理。

---

---
chunk_id: 02-026
module: raw_material_sampling
doc_type: role_matrix
role: sampler
question_type: answer_strategy
visibility: internal
source_doc: 02_raw_material_role_matrix.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## AI回答策略——面向采样员

策略：当前状态 + 操作入口 + 下一步。

模板：
该取样计划当前状态为【未取样/已取样】。
如需操作，请在钉钉小程序中进入【饲料厂检验→原料取样/牧场检验→原料取样】页面。
操作完成后，【状态会变为已取样/化验员可以开始检测】。

原则：
- 告诉采样员在哪个页面操作。
- 区分饲料厂和牧场的不同页面入口。
- 不要越界解释化验和审核的细节。

核心定位：采样员是取样环节的执行者，回答应聚焦于帮助采样员快速找到操作入口和明确当前任务状态。饲料厂和牧场使用不同的钉钉小程序页面路径，回答时须区分。

---

---
chunk_id: 02-027
module: raw_material_sampling
doc_type: role_matrix
role: tester
question_type: answer_strategy
visibility: internal
source_doc: 02_raw_material_role_matrix.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## AI回答策略——面向化验员

策略：检测项状态 + 录入/提交条件 + 注意事项。

模板：
当前化验单状态为【待提交/待审核/已审核】。
【如为待提交】：您可以录入检测结果，确认无误后点击"提交"，状态将变为"待审核"。
【如为待审核】：结果已提交但尚未生效，等待审核员审核。如果被退回，请根据退回原因修正。
【如为已审核】：结果已生效。如需修改，请使用"结果变更"功能。
注意：审核通过时系统会自动进行合格判定。

原则：
- 聚焦化验单的状态流转。
- 明确什么状态下能做什么操作。
- 提醒审核退回和结果变更的正确流程。

核心定位：化验员关心的是化验单在不同状态下可以做什么操作。回答应紧扣三种状态的差异（待提交可录入、待审核不可改、已审核走变更流程），并提醒审核通过时系统自动判定合格与否。

---

---
chunk_id: 02-028
module: raw_material_sampling
doc_type: role_matrix
role: reviewer
question_type: answer_strategy
visibility: internal
source_doc: 02_raw_material_role_matrix.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## AI回答策略——面向审核员

策略：可能卡点 + 排查顺序 + 找谁。

模板：
该车原料未放行，建议按以下顺序排查：
1. 检查取样计划状态。如为"未取样"→ 联系采样员。
2. 检查化验单状态。如为"待提交"→ 联系化验员。
3. 检查化验单状态。如为"待审核"→ 联系审核员。
4. 如已审核通过但仍未放行 → 可能有不合格项目。

请注意：AI无法查询实时状态，以上排查建议需要您在系统中实际查看。

原则：
- 给出结构化的排查顺序。
- 每个卡点对应明确的角色。
- 强调AI不能查实时数据，排查需要人工在系统中操作。

核心定位：面向审核员的策略与调度员类似——按取样→化验→审核→放行的顺序给出结构化的排查路径，帮助审核员理清遇到放行问题时的处理逻辑。注意这不是审核员日常的审核操作策略，而是当放行状态异常或被询问放行进度的应对策略。

---

---
chunk_id: 02-029
module: raw_material_sampling
doc_type: role_matrix
role: dispatcher
question_type: answer_strategy
visibility: internal
source_doc: 02_raw_material_role_matrix.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## AI回答策略——面向调度员

策略：可能卡点 + 排查顺序 + 找谁。

模板：
该车原料未放行，建议按以下顺序排查：
1. 检查取样计划状态。如为"未取样"→ 联系采样员。
2. 检查化验单状态。如为"待提交"→ 联系化验员。
3. 检查化验单状态。如为"待审核"→ 联系审核员。
4. 如已审核通过但仍未放行 → 可能有不合格项目。

请注意：AI无法查询实时状态，以上排查建议需要您在系统中实际查看。

原则：
- 给出结构化的排查顺序。
- 每个卡点对应明确的角色。
- 强调AI不能查实时数据，排查需要人工在系统中操作。

核心定位：调度员需要快速定位车辆未放行的原因。回答应沿着取样→化验→审核→放行的链路逐环节排查，每一步都明确指出对应的责任角色。同时必须强调AI不能查询实时数据，排查需要调度员在系统中自行查看。

---

---
chunk_id: 02-030
module: raw_material_sampling
doc_type: role_matrix
role: admin
question_type: answer_strategy
visibility: internal
source_doc: 02_raw_material_role_matrix.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## AI回答策略——面向管理员

策略：技术信息 + 来源说明 + 边界提示。

模板：
根据逆向技术文档：
- 状态码定义：【具体值】
- 涉及接口：【路径和功能】
- 涉及表：【表名和关联】
- 一卡通对接：【机制说明】

注意：以上信息来自系统代码逆向分析，不代表系统实际运行状态。其中【待确认项】尚未在文档中明确。如需查询实时数据，请在LIMS系统或数据库中直接查看。

原则：
- 提供准确的技术信息，但标注来源。
- 明确区分"已确认"和"待确认"的内容。
- 提醒信息来自知识库，不是实时查询结果。
- 涉及待确认内容时，主动说明不确定性。

核心定位：管理员是唯一可以接收技术细节的角色，但AI仍须严格区分知识库信息和实时系统状态。回答应始终标注信息来源是逆向文档、明确指出待确认项、并引导管理员到实际系统中验证。不可将待确认内容当作已确认事实陈述。

---
