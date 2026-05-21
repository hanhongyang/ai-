# RAG Chunks from 04_raw_material_operation_faq.md

---

---
chunk_id: 04-001
module: raw_material_sampling
doc_type: faq
role: driver,dispatcher
question_type: 签到与取样计划
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q001：司机已经签到，为什么系统里还显示未取样？

适用角色：司机、调度员
简短回答：签到只是创建了取样计划，"未取样"表示还在等待采样员到现场采集样品。签到和取样是两个不同人做的两件事。
详细说明：司机在一卡通签到后，LIMS系统会自动创建一条原料取样计划，初始状态就是"未取样"（status=1）。从签到到"未取样"状态变化是即时完成的，但到"已取样"需要采样员实际去现场操作。
用户下一步应该做什么：联系现场采样员，确认取样时间安排。如果您是司机，请耐心等待采样员到场取样。
AI回答注意：不要让用户觉得是签到失败或系统故障。"未取样"是正常状态。不要给出具体等待时间。
可引用的技术依据：`01_系统全局架构与状态机字典.md`表10，`02_核心业务主流程.md`第3.2.2节。

---

---
chunk_id: 04-002
module: raw_material_sampling
doc_type: faq
role: admin,dispatcher
question_type: 签到与取样计划
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q002：一卡通签到后，LIMS系统会自动做什么？

适用角色：管理员、调度员
简短回答：一卡通签到后，LIMS会自动创建一条原料取样计划，记录司机信息、车牌号、签到时间，并关联一卡通的签到ID。
详细说明：系统收到一卡通签到通知后自动执行：1）根据目的地编码查找对应的LIMS部门ID；2）创建取样计划记录，状态为"未取样"；3）记录司机姓名、车牌号、签到时间作为进厂时间；4）保存一卡通签到ID（sign_in_id）用于系统关联；5）如有随车检验报告，保存附件。整个过程自动完成，无需人工干预。
用户下一步应该做什么：无需操作，系统已自动完成。后续由采样员处理。
AI回答注意：面向司机时简化为"系统自动创建了取样计划，采样员稍后会处理"。面向管理员时才展开技术细节。
可引用的技术依据：`02_核心业务主流程.md`第3.2.2节，`processSignInNotification()`处理步骤。

---

---
chunk_id: 04-003
module: raw_material_sampling
doc_type: faq
role: driver
question_type: 签到与取样计划
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q003：为什么司机签到后还不能放行？

适用角色：司机
简短回答：签到只是第一步。完整的流程是签到→取样→化验→审核→放行，每个环节都需要时间。放行需要所有检测项目全部合格。
详细说明：司机签到后系统创建了取样计划，但之后还需要：1）采样员到现场采集样品；2）化验员完成检测并录入结果；3）审核员确认检测结果；4）系统自动判定全部合格后才会更新放行状态。任何一个环节未完成，放行状态都不会更新。
用户下一步应该做什么：耐心等待。如果等待时间异常长，请联系现场采样员或化验员确认当前进度。
AI回答注意：这是司机最高频的问题之一。回答要简短直接，说明原因。不要让司机觉得"系统很慢"或"有问题"。
可引用的技术依据：`01_raw_material_business_overview.md`第5节。

---

---
chunk_id: 04-004
module: raw_material_sampling
doc_type: faq
role: sampler
question_type: 签到与取样计划
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q004：为什么找不到原料取样计划？

适用角色：采样员
简短回答：可能原因：1）取样计划还是草稿状态未提交；2）计划已被作废；3）一卡通通知没有成功到达；4）您查看的页面不对（确认是原料取样页面而非成品/库存取样页面）。
详细说明：原料取样计划在两种情况下可能"看不到"：第一，手动创建但没提交（status=0），只在草稿列表中；第二，已被作废（status=3），不出现在正常列表中。此外需确认查看的页面是否正确——饲料厂在"饲料厂检验→原料取样"，牧场在"牧场检验"中的原料取样功能。
用户下一步应该做什么：1）确认查看的是正确页面；2）确认取样计划的状态（是否被作废或未提交）；3）如果计划确实不存在，确认一卡通签到是否成功，或手动创建新计划。
AI回答注意：引导用户先确认页面和状态，不要直接断定是系统问题。
可引用的技术依据：`01_raw_material_business_overview.md`第4.1节和第4.2节。

---

---
chunk_id: 04-005
module: raw_material_sampling
doc_type: faq
role: sampler,dispatcher
question_type: 签到与取样计划
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q005：原料取样计划是怎么生成的？

适用角色：采样员、调度员
简短回答：有两种生成方式。方式一：司机在一卡通签到，系统自动创建。方式二：采样员在钉钉小程序中手动创建。
详细说明：方式一（一卡通自动触发）：司机到厂后在一卡通签到，一卡通调用LIMS接口（/ranch/plan/notifySignIn），LIMS自动创建取样计划并关联签到ID。方式二（手动创建）：采样员在钉钉小程序中手动填写供应商、司机、车牌号、进厂时间等信息后提交。两种方式创建的取样计划后续流程完全一样。
用户下一步应该做什么：一般情况下不需要手动创建，一卡通会自动触发。如果一卡通没有触发，可以手动补建。
AI回答注意：对采样员重点说明两种方式的区别在于签到ID，提醒手动创建的取样计划一卡通可能查不到放行状态。
可引用的技术依据：`01_raw_material_business_overview.md`第4节，`02_核心业务主流程.md`第3.2节。

---

---
chunk_id: 04-006
module: raw_material_sampling
doc_type: faq
role: sampler,admin
question_type: 签到与取样计划
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q006：一卡通自动创建和手动创建有什么区别？

适用角色：采样员、管理员
简短回答：主要区别在于是否有签到ID（sign_in_id）。有签到ID的计划，一卡通系统可以查询放行状态；没有签到ID的计划，一卡通无法关联查询。除此之外后续流程完全一样。
详细说明：一卡通自动创建有sign_in_id，有司机信息、车牌号、签到时间，一卡通可通过签到ID查询放行状态。手动创建没有sign_in_id，需人工填写供应商、司机、车牌号、进厂时间等信息，一卡通无法通过签到ID查询放行状态。两种方式在取样、化验、审核、判定环节的处理方式完全相同。
用户下一步应该做什么：如果一卡通签到正常，优先使用自动创建。如果需补录或临时需求，使用手动创建。
AI回答注意：强调后续流程完全相同，唯一区别是一卡通查询能力。
可引用的技术依据：`01_raw_material_business_overview.md`第4节对比表，`02_核心业务主流程.md`第3.2.2节。

---

---
chunk_id: 04-007
module: raw_material_sampling
doc_type: faq
role: sampler
question_type: 签到与取样计划
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q007：手动创建原料取样计划需要填写什么？

适用角色：采样员
简短回答：需要填写供应商信息（编码和名称）、司机信息（姓名和车牌号）、进厂时间。具体必填字段以移动端页面实际显示为准。
详细说明：手动创建时系统需要知道：1）原料是从哪个供应商来的（供应商编码和名称）；2）是哪辆车运来的（司机姓名和车牌号）；3）什么时候到的（进厂时间）。信息录入完成后提交，系统生成取样计划编号，状态为"未取样"。
用户下一步应该做什么：确认信息填写完整后点击提交。提交后采样员即可在待取样列表中找到该计划。
AI回答注意：【待确认】移动端手动创建页面的完整字段列表。当前仅基于逆向文档中提到的字段（supplier_code、supplier_name、driver_name、driver_code、car_in_time）。
可引用的技术依据：`02_核心业务主流程.md`第3.2.3节。

---

---
chunk_id: 04-008
module: raw_material_sampling
doc_type: faq
role: admin
question_type: 签到与取样计划
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q008：签到信息和取样计划是怎么关联的？

适用角色：管理员
简短回答：通过sign_in_id（签到ID）关联。一卡通签到通知中带有签到ID，LIMS将其存入取样计划的sign_in_id字段。后续一卡通系统通过这个ID查询放行状态。
详细说明：一卡通系统调用/ranch/plan/notifySignIn接口时，请求参数中包含id（签到主键ID）。LIMS系统将此ID存入op_sampling_plan.sign_in_id字段。一卡通后续查询放行状态时调用/ranch/plan/isRelease/{signInId}接口。
用户下一步应该做什么：管理员如需排查一卡通查询不到放行的问题，请检查取样计划的sign_in_id是否为空。
AI回答注意：面向非管理员角色时，不要提sign_in_id和接口路径，只说"系统自动关联"即可。
可引用的技术依据：`02_核心业务主流程.md`第3.2.2节，`01_系统全局架构与状态机字典.md`表10。

---

---
chunk_id: 04-009
module: raw_material_sampling
doc_type: faq
role: driver,sampler
question_type: 签到与取样计划
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q009：sign_in_id是什么，普通用户需要关心吗？

适用角色：司机、采样员
简短回答：sign_in_id是一卡通签到记录的唯一标识，由系统后台自动维护。普通用户不需要关心这个字段。
详细说明：sign_in_id是系统之间关联用的技术字段。司机在一卡通签到后，一卡通系统生成一个签到记录ID，LIMS系统保存这个ID用于后续查询放行状态。普通用户在正常操作中不需要知道这个字段的存在。
用户下一步应该做什么：无需关心。如果一卡通查不到放行状态，请联系管理员检查系统关联。
AI回答注意：不要在面向司机或采样员的回答中主动提到sign_in_id。
可引用的技术依据：`01_系统全局架构与状态机字典.md`表10。

---

---
chunk_id: 04-010
module: raw_material_sampling
doc_type: faq
role: driver,dispatcher,sampler
question_type: 签到与取样计划
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q010：取样计划生成后下一步是谁处理？

适用角色：司机、调度员、采样员
简短回答：下一步由采样员处理。采样员需要在钉钉小程序中查看"未取样"的计划，前往现场采集样品。
详细说明：取样计划生成后（状态为"未取样"），采样员是下一个环节的责任人。采样员在钉钉小程序中看到待处理的取样计划，前往运料车处采集原料样品，并在系统中录入样品信息。如果采样员长时间未处理，请提醒其查看待处理列表。
用户下一步应该做什么：如果是采样员自己提问，请打开钉钉小程序中的原料取样页面查看。如果是其他角色提问，请联系现场采样员。
AI回答注意：明确告知采样员是下一个责任人，不要模糊地说"系统会处理"。
可引用的技术依据：`02_raw_material_role_matrix.md`第3节。

---

---
chunk_id: 04-011
module: raw_material_sampling
doc_type: faq
role: sampler
question_type: 签到与取样计划
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q011：未提交和未取样有什么区别？

适用角色：采样员
简短回答："未提交"是草稿状态（status=0），计划还没有生效，采样员看不到。"未取样"是已生效状态（status=1），采样员可以看到并处理。两者的关键区别是计划是否已经生效。
详细说明：status=0（未提交）：取样计划还是草稿未被正式提交，此时采样员工作列表中看不到这个计划。status=1（未取样）：取样计划已正式生效，采样员可以看到并需要处理。一卡通签到自动创建的计划直接进入status=1，正常流程中用户通常不会见到status=0。
用户下一步应该做什么：如果计划是"未提交"状态，需先提交使其生效。如果是"未取样"状态，采样员正常取样即可。
AI回答注意：【待确认】status=0在原料流程中的实际触发场景，当前文档中主要流程都从status=1开始。
可引用的技术依据：`03_raw_material_status_explanation.md`第2.1节和第2.2节。

---

---
chunk_id: 04-012
module: raw_material_sampling
doc_type: faq
role: sampler
question_type: 签到与取样计划
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q012：取样计划作废后还能继续用吗？

适用角色：采样员
简短回答：不能。作废后的取样计划不再参与任何业务流转。如果需要检测，需要重新创建取样计划。
详细说明：作废（status=3）是取样计划的终态之一。作废后该计划不能继续取样、化验或放行。系统保留作废记录用于追溯审计，但不会出现在正常的工作列表中。作废操作一般不可逆。
用户下一步应该做什么：重新创建取样计划。可以通过一卡通重新签到，或在移动端手动创建。
AI回答注意：不要指导用户通过数据库恢复已作废的记录。
可引用的技术依据：`03_raw_material_status_explanation.md`第2.4节。

---

---
chunk_id: 04-013
module: raw_material_sampling
doc_type: faq
role: sampler
question_type: 签到与取样计划
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q013：为什么同一辆车可能看不到取样计划？

适用角色：采样员
简短回答：可能原因：1）司机签到时填写的目的地编码与当前牧场/饲料厂的部门编码不匹配，计划被创建到了其他组织；2）计划已被他人处理或作废；3）一卡通通知未成功到达；4）查看的页面或筛选条件不对。
详细说明：一卡通签到通知中包含目的地编码（destinationCode），LIMS系统根据此编码查找对应部门ID（dept_id）。如果目的地编码填写有误，取样计划可能被创建到错误的牧场或饲料厂，导致当前组织的采样员看不到。
用户下一步应该做什么：1）确认筛选条件是否正确；2）确认是否被其他采样员处理或已作废；3）联系管理员确认目的地编码是否正确。
AI回答注意：【待确认】目的地编码匹配逻辑的具体处理方式（编码不匹配时是创建失败还是创建到默认部门）。
可引用的技术依据：`02_核心业务主流程.md`第3.2.2节。

---

---
chunk_id: 04-014
module: raw_material_sampling
doc_type: faq
role: admin
question_type: 签到与取样计划
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q014：目的地编码不匹配会有什么影响？

适用角色：管理员
简短回答：如果一卡通传过来的目的地编码与LIMS系统中的部门编码不匹配，取样计划可能创建到错误的牧场/饲料厂，导致正确的采样员看不到这条计划。
详细说明：一卡通签到通知中的destinationCode用于在LIMS系统中查找对应的dept_id。如果编码不匹配（比如司机选了错误的牧场），取样计划会被创建到错误的组织下。由于系统按部门（dept_type）进行业务隔离，不同牧场/饲料厂的采样员只能看到自己组织的取样计划。
用户下一步应该做什么：管理员需检查签到通知中的目的地编码与目标牧场/饲料厂的编码是否一致。如果不匹配，可能需要在数据库中修正取样计划的dept_id，或作废该计划后重新签到。
AI回答注意：【待确认】编码不匹配时系统的具体行为（抛出异常还是创建到默认值）。
可引用的技术依据：`02_核心业务主流程.md`第3.2.2节，`01_系统全局架构与状态机字典.md`第1.2节。

---

---
chunk_id: 04-015
module: raw_material_sampling
doc_type: faq
role: admin
question_type: 签到与取样计划
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q015：随车报告附件上传失败会影响什么？

适用角色：管理员
简短回答：随车报告附件上传失败通常不会影响取样计划的创建和后续化验流程。附件只是辅助资料，取样计划本身不依赖附件存在。
详细说明：一卡通签到通知中可以携带随车检验报告（Base64格式的附件）。LIMS在创建取样计划时会尝试保存该附件。即使附件保存失败，取样计划的主流程（取样→化验→审核→放行）不受影响。
用户下一步应该做什么：确认取样计划是否已成功创建。如需补传附件，联系管理员处理。
AI回答注意：【待确认】附件上传失败时系统的具体处理方式（是否影响取样计划创建的事务）。
可引用的技术依据：`02_核心业务主流程.md`第3.2.2节。

---

---
chunk_id: 04-016
module: raw_material_sampling
doc_type: faq
role: sampler
question_type: 采样操作
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q016：采样员看到"未取样"后下一步做什么？

适用角色：采样员
简短回答：前往现场从运料车上采集原料样品，然后在钉钉小程序中录入样品信息（物料名称、取样位置等），完成后状态自动变为"已取样"。
详细说明：操作步骤：1）打开钉钉小程序，进入原料取样页面；2）在待取样列表中找到对应的取样计划；3）前往运料车现场，从指定位置采集原料样品；4）在系统中录入样品信息（物料编码/名称、取样位置等）；5）提交后系统自动生成样品编号和样品记录。此时取样计划状态从"未取样"（status=1）变为"已取样"（status=2）。
用户下一步应该做什么：操作完成后无需其他动作。化验员后续会在待化验列表中看到该样品。
AI回答注意：给采样员的操作指引要具体但不越界。【待确认】移动端采样页面的完整操作流程和字段。
可引用的技术依据：`01_raw_material_business_overview.md`第5节，`02_raw_material_role_matrix.md`第3.2节。

---

---
chunk_id: 04-017
module: raw_material_sampling
doc_type: faq
role: sampler
question_type: 采样操作
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q017：采样员需要扫码还是手动选择计划？

适用角色：采样员
简短回答：两种方式都可以。采样员可以扫描二维码快速定位取样计划，也可以从待取样列表中手动选择。具体以移动端页面支持的功能为准。
详细说明：移动端通常提供两种方式：1）扫码：扫描取样计划关联的二维码快速跳转到对应计划；2）手动选择：在待取样列表中浏览和筛选，手动点击目标计划进入。两种方式进入后操作流程相同。
用户下一步应该做什么：选择方便的方式进入取样计划详情页，完成样品信息录入。
AI回答注意：【待确认】扫码功能在移动端的具体实现方式和支持范围。
可引用的技术依据：`02_核心业务主流程.md`第3.2.1节。

---

---
chunk_id: 04-018
module: raw_material_sampling
doc_type: faq
role: sampler
question_type: 采样操作
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q018：采样完成后系统会生成什么记录？

适用角色：采样员
简短回答：系统会在样品表中创建一条样品记录（sampling_type=3原料），生成唯一的样品编号，同时根据物料类型自动生成该样品需要检测的项目。
详细说明：采样员提交后系统执行：1）在op_sampling_plan_sample表中创建记录，标记为"原料"（sampling_type=3）；2）自动生成唯一样品编号（sample_no）；3）根据物料编码和组织标准配置，自动生成检测项目列表（存入op_sampling_plan_item表）；4）更新取样计划状态为"已取样"（status=2）。
用户下一步应该做什么：采样员工作到此完成。化验员在PC端可以看到新生成的待化验任务。
AI回答注意：向采样员解释时，重点是"系统会自动生成样品编号和检测项目，您不需要手动创建"。
可引用的技术依据：`01_系统全局架构与状态机字典.md`表11、表12，`01_raw_material_business_overview.md`第5节。

---

---
chunk_id: 04-019
module: raw_material_sampling
doc_type: faq
role: sampler,tester
question_type: 采样操作
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q019：sampling_type=3是什么意思？

适用角色：采样员、化验员
简短回答：表示这是从新进厂的饲料原料上采集的样品，区别于成品（0）、库存（1）或垫料（2）样品。系统通过这个标识区分样品的业务来源。
详细说明：系统中所有样品都用同一张表存储，通过sampling_type字段区分业务来源。3代表"原料"（新进厂的玉米、豆粕等），0是成品（生产出来的饲料），1是库存（仓库存放的物料），2是垫料（牛床垫料）。不同类型的样品在计划来源、部分流程环节上有所不同。
用户下一步应该做什么：采样员不需要手动设置这个值，系统会根据操作入口自动标记。如果发现类型不对，请联系管理员。
AI回答注意：普通用户不需要关心这个字段。只有当用户问"为什么我的样品和别人的不一样"时才解释。
可引用的技术依据：`01_系统全局架构与状态机字典.md`表11，`03_raw_material_status_explanation.md`第3节。

---

---
chunk_id: 04-020
module: raw_material_sampling
doc_type: faq
role: sampler,tester,dispatcher
question_type: 采样操作
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q020：样品编号是做什么用的？

适用角色：采样员、化验员、调度员
简短回答：样品编号是每个样品在系统中的唯一标识，用于在整个化验流程中追踪这个样品——从取样、化验、到判定、放行，都通过样品编号来关联。
详细说明：样品编号（sample_no）由系统在采样员提交样品信息时自动生成。化验员通过样品编号在化验单中识别样品，审核员通过它查看检测结果，系统通过它关联检测项目和判定结果。如果需要追溯历史数据，都可以通过样品编号查到。
用户下一步应该做什么：采样员不需要手动设置样品编号。如果需要在系统中查询某个样品，输入样品编号即可快速定位。
AI回答注意：不要让用户手动修改样品编号。
可引用的技术依据：`01_系统全局架构与状态机字典.md`表11。

---

---
chunk_id: 04-021
module: raw_material_sampling
doc_type: faq
role: driver,dispatcher
question_type: 采样操作
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q021：取样完成是否等于检测完成？

适用角色：司机、调度员
简短回答：不等于。取样完成只是把样品从车上采集下来。样品还需要送到化验室，由化验员排队检测。从取样完成到检测完成可能需要一段时间。
详细说明："已取样"（status=2）表示采样员的工作完成了，但此时检测还没有开始——化验员需要在PC端看到待化验任务、点击"开始化验"、逐项录入检测结果、提交审核。取样和检测之间可能存在排队等待时间。
用户下一步应该做什么：耐心等待。如需了解检测进度，请联系化验员。
AI回答注意：对司机和调度员，重点是"取样不等于检测完成，还需要等化验"。
可引用的技术依据：`03_raw_material_status_explanation.md`第7节混淆3。

---

---
chunk_id: 04-022
module: raw_material_sampling
doc_type: faq
role: driver,dispatcher
question_type: 采样操作
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q022：取样完成是否等于可以放行？

适用角色：司机、调度员
简短回答：远远不是。取样完成距离放行还有化验录入→提交→审核→合格判定四个环节。任何环节未完成或有不合格项，都不能放行。
详细说明：从取样完成到放行还需要：1）化验员开始化验并录入检测结果；2）化验员提交化验单；3）审核员审核通过；4）系统自动判定所有项目合格。只有这四个环节全部完成且全部合格，放行状态才会更新。
用户下一步应该做什么：耐心等待。可以联系化验员了解检测进度，或联系审核员了解审核进度。
AI回答注意：这是另一个高频误解，需要明确告知取样完成距离放行还很远。
可引用的技术依据：`01_raw_material_business_overview.md`第5节，`03_raw_material_status_explanation.md`第7节混淆4/5/6。

---

---
chunk_id: 04-023
module: raw_material_sampling
doc_type: faq
role: sampler
question_type: 采样操作
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q023：采样信息填错了怎么办？

适用角色：采样员
简短回答：如果刚刚提交且化验员还没有开始化验，可以尝试联系相关人员修改。如果化验员已开始化验或已生成化验单，修改会更复杂，需联系管理员评估。
详细说明：采样信息修改的难易程度取决于流程进展：1）化验员还没开始化验→修改相对简单；2）化验员已开始化验（化验单已创建）→修改需同时处理化验单中的数据；3）化验单已审核通过→需通过"结果变更"功能处理。
用户下一步应该做什么：1）确认当前流程进展到哪一步；2）联系化验员或管理员说明修改内容；3）不要自行通过非常规方式修改数据。
AI回答注意：【待确认】采样信息修改的具体操作路径和权限。
可引用的技术依据：`02_核心业务主流程.md`第3.3.5节Step7和Step7B。

---

---
chunk_id: 04-024
module: raw_material_sampling
doc_type: faq
role: sampler,tester
question_type: 采样操作
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q024：为什么样品没有检测项目？

适用角色：采样员、化验员
简短回答：可能原因：1）采样员刚提交，系统还在生成检测项目；2）该物料类型在系统中没有配置对应的检测标准，导致无法自动生成；3）检测项目生成出现了异常。
详细说明：系统根据样品的物料编码和所在组织的标准配置（bs_invbill_item_standard表）自动生成检测项目。如果该物料在系统中没有维护检测标准（没有配置需要检测哪些项目），系统就无法自动生成。这种情况通常发生在新增物料类型或标准配置不完整时。
用户下一步应该做什么：1）等待片刻后刷新页面；2）如果仍然没有，联系管理员确认该物料类型是否已在系统中配置了检测标准。
AI回答注意：【待确认】检测项目自动生成的具体规则（是否按"必检"筛选）。
可引用的技术依据：`01_系统全局架构与状态机字典.md`表14，`01_raw_material_business_overview.md`第5节。

---

---
chunk_id: 04-025
module: raw_material_sampling
doc_type: faq
role: tester
question_type: 采样操作
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q025：为什么生成了样品但还不能化验？

适用角色：化验员
简短回答：样品已生成，对应的检测项目应该出现在您的待化验列表中。如果看不到，可能是检测项目还没生成完成，或者筛选条件不对。
详细说明：采样员提交样品信息后，系统需根据物料类型自动生成检测项目，这个过程通常是即时的。如果样品已生成但在待化验列表看不到，请检查：1）页面是否已刷新；2）筛选条件是否正确；3）是否选对了检测项目类别。
用户下一步应该做什么：刷新待化验列表，确认筛选条件。如果仍看不到，联系管理员确认检测标准配置。
AI回答注意：面向化验员时，直接告知排查步骤。
可引用的技术依据：`01_raw_material_business_overview.md`第5节。

---

---
chunk_id: 04-026
module: raw_material_sampling
doc_type: faq
role: sampler
question_type: 采样操作
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q026：采样员能不能直接放行？

适用角色：采样员
简短回答：不能。采样员的职责是采集样品和录入样品信息。放行需要所有检测项目合格且审核通过，采样员没有权限执行放行操作。
详细说明：系统中不同角色有不同职责和权限。采样员负责取样环节，化验员负责检测环节，审核员负责审核环节。放行是流程的最后一步，通常由系统在全部合格后自动更新放行状态，或由质检主管确认放行。采样员不能跳过检测和审核直接放行。
用户下一步应该做什么：完成取样后流程自动进入化验环节。如需了解放行进度，请联系化验员或审核员。
AI回答注意：明确告知角色的权限边界。不要让别人误以为采样员可以跳过流程。
可引用的技术依据：`02_raw_material_role_matrix.md`第3.3节。

---

---
chunk_id: 04-027
module: raw_material_sampling
doc_type: faq
role: sampler
question_type: 采样操作
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q027：采样员能不能修改检测结果？

适用角色：采样员
简短回答：不能。检测结果的录入和修改是化验员的职责。采样员只能操作取样相关的内容（样品信息），没有权限修改化验数据。
详细说明：系统按角色进行了功能隔离。采样员使用的移动端页面（钉钉小程序）中没有化验数据录入功能。化验结果只能在PC端管理后台由化验员录入和修改。如果采样员发现样品信息有误，可在权限范围内修改样品信息，但不能修改检测结果。
用户下一步应该做什么：如果发现检测结果有问题，请联系化验员或审核员处理。
AI回答注意：清晰说明角色边界，避免越权期望。
可引用的技术依据：`02_raw_material_role_matrix.md`第3.3节。

---

---
chunk_id: 04-028
module: raw_material_sampling
doc_type: faq
role: sampler
question_type: 采样操作
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q028：移动端原料取样入口在哪里？

适用角色：采样员
简短回答：饲料厂采样员请进入钉钉小程序→"饲料厂检验"→"原料取样"。牧场采样员请进入钉钉小程序→"牧场检验"中的原料取样功能。
详细说明：原料取样功能在移动端的入口按业务主体区分：饲料厂在lims-app/pages/Feedmillassay/ylqy/路径下操作，牧场在lims-app/pages/Ranchassay/路径下操作。两个入口的表单和操作流程类似，但背后的组织归属不同。
用户下一步应该做什么：确认自己的组织归属（牧场还是饲料厂），进入对应的页面。
AI回答注意：【待确认】牧场原料取样的具体页面路径和功能入口名称。当前文档只明确了饲料厂的路径。
可引用的技术依据：`02_核心业务主流程.md`第3.2节，`01_系统全局架构与状态机字典.md`第2.2节。

---

---
chunk_id: 04-029
module: raw_material_sampling
doc_type: faq
role: sampler
question_type: 采样操作
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q029：牧场原料取样和饲料厂原料取样有什么区别？

适用角色：采样员
简短回答：操作流程基本相同。主要区别在于：1）使用的移动端页面入口不同（牧场vs饲料厂）；2）取样计划归属的组织不同（dept_type=2 vs 3）；3）检测标准和项目配置可能不同。核心流程一样。
详细说明：牧场和饲料厂的原料取样在核心业务逻辑上没有本质区别，都使用相同的数据库表和状态机。区别主要体现在组织归属、页面入口、以及各自配置的检测标准可能不同。
用户下一步应该做什么：确认自己的组织归属，使用对应的页面入口。
AI回答注意：如果用户问具体操作差异，当前文档未覆盖前端细节，建议引导用户在对应页面中查看。
可引用的技术依据：`01_系统全局架构与状态机字典.md`第1.2节，`01_raw_material_business_overview.md`第2节。

---

---
chunk_id: 04-030
module: raw_material_sampling
doc_type: faq
role: sampler
question_type: 采样操作
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q030：原料取样和库存/垫料/成品取样有什么区别？

适用角色：采样员
简短回答：核心区别在于触发方式和计划来源。原料取样由一卡通签到触发或移动端手动创建，使用op_sampling_plan作为计划主表。成品/库存/垫料取样使用PC端Excel导入或移动端直接采样，使用op_finished_product_sampling_plan。
详细说明：原料取样（sampling_type=3）是新进厂原料；成品取样（sampling_type=0）仅饲料厂，检测成品饲料；库存取样（sampling_type=1）牧场+饲料厂，检测仓储物料；垫料取样（sampling_type=2）仅牧场，检测牛床垫料。后续流程不完全相同。
用户下一步应该做什么：确认您要处理的是哪种取样类型。本知识库主要覆盖原料取样（sampling_type=3）。
AI回答注意：如果用户问的是成品/库存/垫料相关问题，告知这不属于原料取样模块。
可引用的技术依据：`01_raw_material_business_overview.md`第3节，`02_核心业务主流程.md`第3.3节。

---

---
chunk_id: 04-031
module: raw_material_sampling
doc_type: faq
role: tester
question_type: 化验录入
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q031：化验员什么时候可以开始化验？

适用角色：化验员
简短回答：当采样员完成取样后，系统会自动生成检测项目。这些检测项目会出现在您的PC端待化验列表中，此时您就可以勾选并点击"开始化验"。
详细说明：前提条件：1）采样员已完成取样（取样计划status=2）；2）系统已根据物料类型自动生成检测项目；3）检测项目出现在待化验列表中。满足条件后，在待化验列表中勾选属于同一检测项目的样品，点击"开始化验"。
用户下一步应该做什么：登录PC端管理后台，在待化验列表中查看是否有新的检测任务。勾选同一检测项目的样品，点击"开始化验"。
AI回答注意：化验员需要知道"同一检测项目"这个约束条件。
可引用的技术依据：`02_核心业务主流程.md`第3.3.5节Step1，`03_raw_material_status_explanation.md`第6.1节。

---

---
chunk_id: 04-032
module: raw_material_sampling
doc_type: faq
role: tester
question_type: 化验录入
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q032：开始化验后会生成什么化验单？

适用角色：化验员
简短回答：系统会生成一张化验单主表记录和对应的多条化验明细记录。化验单主表记录检测项目、化验员、化验时间等信息；化验明细记录每个样品每个检测项目的具体结果。
详细说明：点击"开始化验"后系统执行：1）创建化验单主表记录（op_test_result_base），含化验单号、检测项目名称、化验员信息、创建时间等，初始状态为"待提交"（status=2）；2）为每个勾选的样品创建化验明细记录（op_test_result_info），关联样品信息和检测项目。系统使用分布式锁防止重复点击。
用户下一步应该做什么：在生成的化验单中逐项录入检测结果。
AI回答注意：面向化验员时，重点讲化验单状态和操作步骤，不展开技术锁机制。
可引用的技术依据：`02_核心业务主流程.md`第3.3.5节Step1，`03_raw_material_status_explanation.md`第6.2节。

---

---
chunk_id: 04-033
module: raw_material_sampling
doc_type: faq
role: tester
question_type: 化验录入
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q033：为什么检测项目必须属于同一个检测项目才能批量开始化验？

适用角色：化验员
简短回答：因为一张化验单对应一个检测项目（如一张化验单只做"水分"检测）。如果您勾选了不同检测项目的样品，系统无法将它们放在同一张化验单中。
详细说明：化验单按检测项目组织。化验单主表（op_test_result_base）有item_id字段记录检测项目。化验明细（op_test_result_info）记录不同样品在该项目上的结果。这样设计是因为不同检测项目的检测方法、计算公式、页面模板可能不同。
用户下一步应该做什么：在待化验列表中按检测项目分组，每次勾选属于同一检测项目的样品，分别开始化验。
AI回答注意：用业务语言解释——"一张化验单做一个检测项目，不同项目分开做"。
可引用的技术依据：`02_核心业务主流程.md`第3.3.5节Step1前置校验。

---

---
chunk_id: 04-034
module: raw_material_sampling
doc_type: faq
role: tester
question_type: 化验录入
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q034：为什么重复点击"开始化验"不会生成重复化验单？

适用角色：化验员
简短回答：系统有防重复机制（分布式锁），会检测同一批检测项目是否已经生成过化验单。如果已经生成，不会重复创建。
详细说明：系统在"开始化验"操作中使用了分布式锁机制防止快速点击或网络延迟导致的重复请求。获取锁后，系统还会查询数据库确认这批检测项目是否已存在对应的化验单，双重检查确保不会创建重复记录。
用户下一步应该做什么：如果点击后没有反应，请检查是否已生成化验单（刷新待化验列表或化验单列表）。不要反复快速点击。
AI回答注意：面向化验员时，说"系统有防重复机制"即可，不展开说Redisson和分布式锁。
可引用的技术依据：`02_核心业务主流程.md`第3.3.5节Step1。

---

---
chunk_id: 04-035
module: raw_material_sampling
doc_type: faq
role: tester,dispatcher
question_type: 化验录入
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q035：待提交是什么意思？

适用角色：化验员、调度员
简短回答："待提交"（status=2）表示化验单已创建，化验员正在录入或已录入检测结果但还没有点击"提交"。此时检测结果尚未生效，化验员可以自由编辑。
详细说明：化验员点击"开始化验"后，化验单初始状态就是"待提交"。在此状态下化验员可以自由录入和修改检测结果；审核员看不到或不操作这张化验单；检测结果还没有正式生效。化验员确认数据无误后点击"提交"，状态变为"待审核"。
用户下一步应该做什么：如果是化验员，确认是否已录入完所有检测结果，完成后点击"提交"。如果是其他角色，请联系化验员确认预计提交时间。
AI回答注意：向非化验员解释时，重点说"化验员还在录入结果，还没正式提交"。
可引用的技术依据：`03_raw_material_status_explanation.md`第6.3节。

---

---
chunk_id: 04-036
module: raw_material_sampling
doc_type: faq
role: tester,dispatcher,reviewer
question_type: 化验录入
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q036：待审核是什么意思？

适用角色：化验员、调度员、审核员
简短回答："待审核"（status=3）表示化验员已提交检测结果，等待审核员确认。此时化验员不能直接修改数据（需要审核员退回），检测结果仍未正式生效。
详细说明：化验员提交后化验单进入"待审核"状态。在此状态下：审核员可以在PC端看到待审核的化验单；化验员不能直接修改数据（需审核员先退回）；检测结果尚未生效。审核员通过后状态变为"已审核"，审核退回后状态回到"待提交"。
用户下一步应该做什么：如果是审核员，请在PC端查看待审核的化验单。如果是化验员，请等待审核员处理。如果是其他角色，请联系审核员确认审核时间。
AI回答注意：向非审核员解释时，重点说"结果已提交，等待审核员确认"。
可引用的技术依据：`03_raw_material_status_explanation.md`第6.4节，`02_核心业务主流程.md`第3.3.5节Step2。

---

---
chunk_id: 04-037
module: raw_material_sampling
doc_type: faq
role: reviewer,tester
question_type: 化验录入
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q037：审核通过后状态怎么变？

适用角色：审核员、化验员
简短回答：化验单状态从"待审核"（status=3）变为"已审核"（status=4），检测结果正式生效。同时系统自动进行合格判定，将结果与质量标准比对，全部合格则更新放行状态为"已放行"。
详细说明：审核员点击"审核通过"后系统执行：1）化验单主表和明细表状态更新为"已审核"（status=4）；2）自动比对检测结果与质量标准，判定每个项目合格/不合格；3）更新样品的合格状态（A1合格/R1不合格）；4）如有不合格项目，自动生成不合格品记录；5）如全部合格，更新放行状态。
用户下一步应该做什么：审核通过后无需额外操作。系统会自动执行判定和放行更新。
AI回答注意：强调"审核通过不等于全部合格"，审核通过后系统才进行合格判定。
可引用的技术依据：`03_raw_material_status_explanation.md`第6.5节，`02_核心业务主流程.md`第3.3.5节Step3。

---

---
chunk_id: 04-038
module: raw_material_sampling
doc_type: faq
role: tester
question_type: 化验录入
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q038：审核退回后怎么办？

适用角色：化验员
简短回答：审核员退回了您的化验单，状态从"待审核"回到"待提交"。请查看退回原因，修正有问题的检测结果，然后重新提交。
详细说明：审核员发现数据问题点击"审核退回"并填写退回原因后：化验单状态从"待审核"回到"待提交"；之前录入的检测数据还在不会丢失；您可根据退回原因修正有问题的数据项；修正后重新点击"提交"，化验单会再次进入"待审核"状态。
用户下一步应该做什么：1）在系统中查看审核员填写的退回原因；2）针对性地修正检测数据；3）确认无误后重新提交。
AI回答注意：让化验员知道数据没有丢失只需修正问题项，不要暗示"被否定"。
可引用的技术依据：`03_raw_material_status_explanation.md`第6.6节，`02_核心业务主流程.md`第3.3.5节Step4。

---

---
chunk_id: 04-039
module: raw_material_sampling
doc_type: faq
role: tester
question_type: 化验录入
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q039：检测结果录入后是否自动判定？

适用角色：化验员
简短回答：不会在录入时自动判定。检测结果的合格判定是在审核员点击"审核通过"时由系统自动执行的，不是在化验员录入结果时实时判定的。
详细说明：化验员在"待提交"状态下录入检测结果时，系统不会实时判定合格与否。原因：1）化验员可能还在修改数据，判定结果可能会变；2）检测结果需审核员确认数据有效性后才能正式生效。只有审核员审核通过后，系统才会以生效的检测结果与质量标准比对进行最终判定。
用户下一步应该做什么：录入完检测结果后正常提交即可，判定会在审核通过时自动完成。
AI回答注意：化验员不用担心录入时的即时反馈，判定发生在审核环节。
可引用的技术依据：`02_核心业务主流程.md`第3.3.5节Step3，`03_raw_material_status_explanation.md`第6.5节。

---

---
chunk_id: 04-040
module: raw_material_sampling
doc_type: faq
role: tester,reviewer
question_type: 化验录入
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q040：不合格结果会发生什么？

适用角色：化验员、审核员
简短回答：审核通过时，如果系统判定有检测项目不合格，会自动生成一条不合格品处理记录（op_sample_unquality），包含不合格的检测项目、检测结果、物料信息等。同时该车原料不能放行。
详细说明：不合格品记录包含：物料编码/名称、检测项目名称、检测结果、判定结论、关联的样品ID和签到ID（如果有）、处理类型等信息。初始化状态为"待处理"，后续由质检主管决定处理方案（如退货、降级使用等）。
用户下一步应该做什么：化验员和采样员不需要操作。审核员/质检主管需要关注不合格品记录并决定处理方案。
AI回答注意：【待确认】不合格品的完整后续处理流程不在本次MVP范围。
可引用的技术依据：`02_核心业务主流程.md`第3.3.5节Step3，`03_raw_material_status_explanation.md`第5节。

---

---
chunk_id: 04-041
module: raw_material_sampling
doc_type: faq
role: tester
question_type: 化验录入
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q041：化验员能不能审核自己的结果？

适用角色：化验员
简短回答：一般情况下不能。化验和审核是两个独立的环节，需要由不同的人完成，以保证检测结果的公正性和准确性。化验员录入结果后必须由审核员审核。
详细说明：这是质量控制体系的基本要求——检测人和审核人必须分离。化验员负责录入结果，审核员负责确认结果的有效性。如果允许化验员审核自己的结果，就失去了审核的意义。
用户下一步应该做什么：提交化验单后等待审核员审核。如果审核长时间未完成，请联系审核员。
AI回答注意：【待确认】系统中是否有技术限制阻止化验员审核自己的结果，还是仅靠流程规范。
可引用的技术依据：`02_raw_material_role_matrix.md`第4.3节和第5节。

---

---
chunk_id: 04-042
module: raw_material_sampling
doc_type: faq
role: tester,driver,dispatcher
question_type: 化验录入
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q042：化验没完成能不能放行？

适用角色：化验员、司机、调度员
简短回答：绝对不能。放行的前提条件之一就是所有检测项目全部完成且审核通过。化验没完成意味着检测结果还没生效，无法判定合格与否，自然不能放行。
详细说明：放行需要同时满足：1）所有样品已完成取样；2）所有检测项目已化验；3）所有化验单已审核通过；4）所有检测项目判定为合格。以上任何一条不满足，放行状态都不会更新为"已放行"。
用户下一步应该做什么：等待化验完成。可以联系化验员了解预计完成时间。
AI回答注意：明确告知"绝对不能"，不要给用户任何可能跳过化验的期待。
可引用的技术依据：`03_raw_material_status_explanation.md`第4.1节。

---

---
chunk_id: 04-043
module: raw_material_sampling
doc_type: faq
role: tester
question_type: 化验录入
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q043：化验结果为空为什么不能推到下一步？

适用角色：化验员
简短回答：检测结果是合格判定的唯一依据。如果结果为空，系统无法判定合格与否，因此不能提交或进入审核环节。必须先完成所有检测项目的录入。
详细说明：化验单提交时，系统通常会校验必填的检测结果是否已录入完整。如果存在空的检测项目，提交会被阻止。这是数据完整性的基本要求——不能基于不完整的数据做质量判定。
用户下一步应该做什么：检查化验单中是否有检测项目的结果为空，补充完整后重新提交。
AI回答注意：【待确认】系统在提交时是否有明确的前端或后端校验逻辑，以及校验的具体规则。
可引用的技术依据：`02_核心业务主流程.md`第3.3.5节Step2。

---

---
chunk_id: 04-044
module: raw_material_sampling
doc_type: faq
role: tester,reviewer
question_type: 化验录入
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q044：检测结果变更是否有日志？

适用角色：化验员、审核员
简短回答：有。审核通过后如果通过"结果变更"功能修改检测数据，系统会自动记录变更日志，包括原值、新值、变更原因、操作人和操作时间。
详细说明：系统在op_test_result_change_log表中记录每次结果变更的完整信息：result_id（关联化验明细）、origin_result（原值）、new_result（新值）、origin_check_result（原判定）、new_check_result（新判定）、change_reason（变更原因）。这些日志用于审计追溯。
用户下一步应该做什么：如审核通过后发现数据有误，通过"结果变更"功能修改。变更操作会自动记录日志。
AI回答注意：强调所有变更都有记录，不要暗示可通过非正常方式修改数据。
可引用的技术依据：`02_核心业务主流程.md`第3.3.5节B，`01_系统全局架构与状态机字典.md`第2.2节。

---

---
chunk_id: 04-045
module: raw_material_sampling
doc_type: faq
role: tester,admin
question_type: 化验录入
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q045：化验作废和样品作废有什么区别？

适用角色：化验员、管理员
简短回答：化验单作废是将化验数据标记为无效，样品本身不受影响；样品作废是将整个样品记录标记为删除，其下的化验数据也会受影响。两者影响范围不同。
详细说明：系统提供了不同阶段的作废机制：1）待化验作废：只标记检测项目，不影响已生成的化验单；2）化验中作废：软删除化验明细和化验单主表；3）已接收作废：软删除所有关联的化验明细和样品记录。作废都是软删除（标记而非物理删除），数据仍保留在数据库中。
用户下一步应该做什么：确认需要作废的范围——是只作废化验数据，还是要作废整个样品。根据需求选择对应的作废操作。
AI回答注意：向非管理员解释时用"作废"而非"软删除"。
可引用的技术依据：`02_核心业务主流程.md`第3.3.5节Step7，`01_系统全局架构与状态机字典.md`第2.7节。

---

---
chunk_id: 04-046
module: raw_material_sampling
doc_type: faq
role: driver,dispatcher,reviewer
question_type: 合格判定与放行
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q046：什么情况下可以放行？

适用角色：司机、调度员、审核员
简短回答：只有同时满足以下所有条件才能放行：1）取样计划已完成取样（status=2）；2）所有检测项目已化验完成；3）所有化验单已审核通过（status=4）；4）所有检测项目判定为合格（A1）。
详细说明：放行是原料进厂质检流程的最终目标。任何一个条件不满足，放行状态都会保持"未放行"（is_release=0）。最常见的未放行原因不是不合格，而是流程还没走完——取样、化验或审核环节还在进行中。
用户下一步应该做什么：如果不确定为什么没放行，按取样→化验→审核→判定的顺序排查当前卡在哪一步。
AI回答注意：这是放行类最高频的问题，回答要清晰列出四个条件，建议排查顺序。
可引用的技术依据：`03_raw_material_status_explanation.md`第4.2节，`01_raw_material_business_overview.md`第5节。

---

---
chunk_id: 04-047
module: raw_material_sampling
doc_type: faq
role: all
question_type: 合格判定与放行
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q047：is_release=0是什么意思？

适用角色：所有角色
简短回答：表示这车原料还没有被放行，不能入库使用。原因可能是取样、化验或审核还没完成，或者有检测项目不合格。
详细说明：is_release=0（未放行）是一个结果状态，意味着放行的四个条件（取样完成+化验完成+审核通过+全部合格）没有全部满足。大多数情况下是因为流程还在进行中，而不是因为不合格。需要通过查看取样计划状态和化验单状态来确定具体卡在哪一步。
用户下一步应该做什么：在系统中查看取样计划状态和化验单状态。如果不会查看，联系采样员、化验员或审核员确认进度。
AI回答注意：不要直接断定"因为没有放行所以有问题"。未放行大多数是流程进行中。
可引用的技术依据：`03_raw_material_status_explanation.md`第4.1节。

---

---
chunk_id: 04-048
module: raw_material_sampling
doc_type: faq
role: all
question_type: 合格判定与放行
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q048：is_release=1是什么意思？

适用角色：所有角色
简短回答：表示这车原料已经放行，所有检测项目合格，可以卸车入库投入使用。这是原料进厂质检流程的最终目标状态。
详细说明：is_release=1（已放行）意味着：取样完成、所有检测项目已完成化验、所有化验单已审核通过、所有检测项目判定为合格。一卡通系统可以通过签到ID查询到这个放行状态。
用户下一步应该做什么：可以正常卸车入库。如果一卡通仍显示未放行，可能是系统间查询延迟或签到ID关联问题。
AI回答注意：不要承诺"已放行就一切OK了"——现场操作仍需遵守常规流程。
可引用的技术依据：`03_raw_material_status_explanation.md`第4.2节。

---

---
chunk_id: 04-049
module: raw_material_sampling
doc_type: faq
role: all
question_type: 合格判定与放行
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q049：A1是什么意思？

适用角色：所有角色
简短回答：A1表示该样品的所有检测项目都合格，检测结果都在质量标准允许的范围内。A1是放行的必要条件——一个取样计划下的所有样品都必须是A1，才能放行。
详细说明：A1是系统在审核通过后自动判定的结果。它来自系统的审核结果字典（examine_result_type），是正式的合格标识。一个取样计划下可能有多个样品，每个样品有多个检测项目，只有全部都为A1才能放行。
用户下一步应该做什么：如果所有样品都是A1，等待放行状态更新即可。
AI回答注意：A1是系统内部标识，向司机等一线用户解释时直接说"合格"即可。
可引用的技术依据：`01_系统全局架构与状态机字典.md`第2.6节，`03_raw_material_status_explanation.md`第5节。

---

---
chunk_id: 04-050
module: raw_material_sampling
doc_type: faq
role: all
question_type: 合格判定与放行
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q050：R1是什么意思？

适用角色：所有角色
简短回答：R1表示该样品至少有一个检测项目不合格，检测结果超出了质量标准允许的范围。这不是系统错误，而是质量检测的结论。
详细说明：R1表示质量不达标。审核通过时系统会自动比对检测结果与质量标准，如果任何一项超出标准范围，样品即被判为R1。判定为R1后：该车原料不能放行；系统自动生成不合格品处理记录；需要质检主管决定后续处理方案。R1是正常的业务结果，不是系统故障。
用户下一步应该做什么：联系审核员或质检主管了解具体哪个项目不合格，以及后续处理方案。
AI回答注意：司机可能会焦虑。先说明"这是质量检测结论，不是系统故障"，再告诉下一步找谁。
可引用的技术依据：`01_系统全局架构与状态机字典.md`第2.6节，`03_raw_material_status_explanation.md`第5节。

---

---
chunk_id: 04-051
module: raw_material_sampling
doc_type: faq
role: driver,dispatcher
question_type: 合格判定与放行
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q051：不合格是不是系统错误？

适用角色：司机、调度员
简短回答：不是。不合格是系统根据检测结果和质量标准自动判定的正常业务结果，说明该批原料的某项检测指标确实没有达到质量要求。这不是系统故障或误判。
详细说明：系统将化验员录入并经审核员确认的检测结果，与预设的质量标准（bs_invbill_item_standard）进行比对。如果结果超出标准范围，系统判定为不合格。整个过程由系统自动执行，判定依据是明确的质量标准。如果对判定结果有疑问，可以：1）核实检测结果是否准确；2）核实质量标准是否正确配置；3）通过"结果变更"流程申请复核。
用户下一步应该做什么：联系审核员或质检主管了解具体不合格项目和超标幅度。如需复核，通过正规流程申请。
AI回答注意：决不要暗示"可能是系统判错了"或"可能是误判"。不合格是正常业务结果。
可引用的技术依据：`02_核心业务主流程.md`第3.3.5节Step3，`03_raw_material_status_explanation.md`第7节混淆8。

---

---
chunk_id: 04-052
module: raw_material_sampling
doc_type: faq
role: dispatcher,reviewer
question_type: 合格判定与放行
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q052：为什么全部流程做完了还没放行？

适用角色：调度员、审核员
简短回答：最可能的原因是有检测项目不合格。审核通过不代表全部合格——审核通过后系统才进行合格判定。如果有不合格项目，放行状态不会更新。
详细说明：即使化验单已审核通过（status=4），如果存在不合格的检测项目（R1），系统不会更新放行状态。此外也需检查：1）取样计划下是否所有样品都已化验和审核；2）放行状态是否因某些原因没有自动更新。【待确认】放行是自动还是手动触发。
用户下一步应该做什么：1）检查所有样品是否都已完成化验和审核；2）检查是否存在不合格项目（R1）；3）如一切合格但放行状态仍是0，联系管理员检查系统。
AI回答注意：排查顺序：先确认是否有不合格，再确认是否所有样品都处理完，最后才考虑系统问题。
可引用的技术依据：`03_raw_material_status_explanation.md`第4.1节。

---

---
chunk_id: 04-053
module: raw_material_sampling
doc_type: faq
role: reviewer,admin
question_type: 合格判定与放行
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q053：放行状态是由谁更新的？

适用角色：审核员、管理员
简短回答：【待确认】当前文档描述为"所有样品检测合格后更新is_release='1'"，暗示由系统自动更新。但未明确是审核通过时自动触发，还是需要人工手动点击放行按钮。
详细说明：文档中描述的逻辑是：审核通过→系统自动判定合格/不合格→全部合格则更新is_release='1'。如描述准确，放行是审核通过时自动触发的。但放行的具体触发方式（自动还是手动）尚未完全确认。
用户下一步应该做什么：在审核通过后检查放行状态是否已更新。如果没有自动更新，可能需要手动操作或联系管理员。
AI回答注意：如实告知这是【待确认】项，不要给出确定性的"是自动的"或"需要手动"的回答。
可引用的技术依据：`02_核心业务主流程.md`第3.2.4节，`03_raw_material_status_explanation.md`第4节。

---

---
chunk_id: 04-054
module: raw_material_sampling
doc_type: faq
role: admin,dispatcher
question_type: 合格判定与放行
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q054：一卡通怎么知道LIMS是否放行？

适用角色：管理员、调度员
简短回答：一卡通系统通过调用LIMS的查询接口（/ranch/plan/isRelease/{signInId}），传入签到ID，获取该签到对应的原料是否已放行。
详细说明：这是一卡通和LIMS两个系统之间的接口调用。一卡通在需要查询放行状态时向LIMS发送查询请求，请求参数是签到ID（sign_in_id），LIMS返回该签到对应取样计划的放行状态。请注意：如果取样计划是手动创建的（没有签到ID），一卡通无法通过此方式查询。
用户下一步应该做什么：如果一卡通查不到放行状态，请确认取样计划是否为一卡通自动创建（有签到ID），以及签到ID是否正确关联。
AI回答注意：面向非管理员时，不要说接口路径，只说"一卡通系统会自动查询LIMS的放行结果"。
可引用的技术依据：`02_核心业务主流程.md`第3.2.4节。

---

---
chunk_id: 04-055
module: raw_material_sampling
doc_type: faq
role: admin
question_type: 合格判定与放行
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q055：/ranch/plan/isRelease/{signInId} 是做什么的？

适用角色：管理员
简短回答：这是一卡通系统查询放行状态的接口。一卡通通过传入签到ID，LIMS返回该签到对应取样计划是否已放行的结果。
详细说明：接口路径为/ranch/plan/isRelease/{signInId}，由OpSamplingPlanController处理。一卡通系统在需要时主动调用此接口（非LIMS推送），传入司机签到时获取的签到ID。LIMS根据签到ID查找对应的op_sampling_plan记录，返回is_release字段的值。
用户下一步应该做什么：管理员如需测试此接口，使用有效的签到ID调用即可。
AI回答注意：此接口信息仅面向管理员。不要对普通用户暴露接口路径。
可引用的技术依据：`02_核心业务主流程.md`第3.2.4节，`00_raw_material_scope_analysis.md`第2.4节。

---

---
chunk_id: 04-056
module: raw_material_sampling
doc_type: faq
role: driver
question_type: 合格判定与放行
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q056：司机能不能自己操作放行？

适用角色：司机
简短回答：不能。放行是系统在检测全部合格后自动更新（或由质检主管确认）的。司机没有权限操作放行，也不需要操作放行。
详细说明：司机的角色职责是在一卡通系统签到、运送原料到厂。放行是质量判定流程的最终结果，司机不需要也不能在系统中操作放行。司机可以通过一卡通系统查询放行状态。
用户下一步应该做什么：在一卡通系统查询放行状态，或联系现场工作人员确认进度。无需自行操作。
AI回答注意：简单直接告知"不能"，不要让司机觉得需要自己操作什么。
可引用的技术依据：`02_raw_material_role_matrix.md`第2.3节。

---

---
chunk_id: 04-057
module: raw_material_sampling
doc_type: faq
role: sampler
question_type: 合格判定与放行
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q057：采样员能不能操作放行？

适用角色：采样员
简短回答：不能。采样员的职责是采集样品和录入样品信息。放行环节不在采样员的权限范围内。
详细说明：采样员完成取样后，后续的化验、审核、判定、放行由其他角色负责。采样员在移动端（钉钉小程序）上操作，而放行相关的操作在PC端管理后台。采样员不需要也不能操作放行。
用户下一步应该做什么：完成取样后流程自动进入化验环节。如需了解放行进度，联系化验员或审核员。
AI回答注意：清晰告知角色权限边界。
可引用的技术依据：`02_raw_material_role_matrix.md`第3.3节。

---

---
chunk_id: 04-058
module: raw_material_sampling
doc_type: faq
role: tester
question_type: 合格判定与放行
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q058：化验员能不能直接放行？

适用角色：化验员
简短回答：不能。化验员的职责是录入检测结果。放行需要审核员审核通过且所有项目合格，化验员没有权限直接放行。
详细说明：化验和审核是两个独立环节，必须由不同的人完成。化验员录入并提交检测结果后，必须由审核员审核。即使所有检测结果都显示合格，化验员也不能跳过审核环节直接放行。
用户下一步应该做什么：提交化验单后等待审核员审核。审核通过且全部合格后，系统会自动更新放行状态。
AI回答注意：清晰告知角色权限边界。
可引用的技术依据：`02_raw_material_role_matrix.md`第4.3节。

---

---
chunk_id: 04-059
module: raw_material_sampling
doc_type: faq
role: dispatcher
question_type: 合格判定与放行
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q059：调度员看到未放行应该先排查什么？

适用角色：调度员
简短回答：按以下顺序排查：1）取样是否已完成（status=2）？→找采样员；2）化验是否已提交（status=2→3）？→找化验员；3）是否已审核（status=3→4）？→找审核员；4）是否有不合格项目（R1）？→找质检主管。
详细说明：这是从流程起点到终点的完整排查链。每一步对应一个角色和一种可能卡住的原因。大多数情况下问题出在第2或第3步（化验或审核环节进行中）。排查时需要在LIMS系统中查看对应的状态。
用户下一步应该做什么：登录LIMS系统，按上述顺序查看状态，找到卡点后联系对应角色。
AI回答注意：给调度员的排查指引要简洁、有步骤、可操作。
可引用的技术依据：`03_raw_material_status_explanation.md`第4.1节，`02_raw_material_role_matrix.md`第6.2节。

---

---
chunk_id: 04-060
module: raw_material_sampling
doc_type: faq
role: driver,dispatcher
question_type: 合格判定与放行
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: true
ai_can_answer: false
---

## Q060：AI能不能帮我查询某辆车是否已经放行？

适用角色：司机、调度员
简短回答：抱歉，AI当前不能查询实时数据库，无法直接告诉您某辆车的实时放行状态。您可以在一卡通系统或LIMS系统中自行查看放行状态。
详细说明：AI客服没有连接实时数据库的权限，无法查询具体的"这辆车现在有没有放行"。AI可以帮您：解释放行状态的含义、说明什么条件下会放行、告诉您如果没放行可能是卡在哪一步、以及在哪个系统页面可以查看状态。
用户下一步应该做什么：1）在一卡通系统中查询放行状态（仅限一卡通签到的计划）；2）登录LIMS系统查看取样计划的放行状态；3）联系现场工作人员协助查询。
AI回答注意：明确拒绝执行实时查询，但同步给出替代方案。
可引用的技术依据：`02_raw_material_role_matrix.md`第8节。

---

---
chunk_id: 04-061
module: raw_material_sampling
doc_type: faq
role: sampler,admin
question_type: 异常与排查
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q061：签到后没有生成取样计划，怎么办？

适用角色：采样员、管理员
简短回答：可能原因：1）一卡通通知没有成功到达LIMS（网络或接口问题）；2）通知中的目的地编码在LIMS中找不到对应的部门。请先确认一卡通侧签到是否成功，然后决定手动补建还是排查接口问题。
详细说明：一卡通调用LIMS接口时可能出现接口超时、网络不通、参数格式错误、目的地编码不匹配等情况。如果LIMS没有收到通知或处理失败，就不会创建取样计划。
用户下一步应该做什么：1）确认一卡通侧签到是否成功；2）如果是偶发情况，采样员可在移动端手动创建取样计划作为补建；3）如果频繁发生，联系管理员检查接口状态。
AI回答注意：优先建议手动补建（快速解决），再建议排查接口（长期方案）。
可引用的技术依据：`02_核心业务主流程.md`第3.2.2节，`01_raw_material_business_overview.md`第4.2节。

---

---
chunk_id: 04-062
module: raw_material_sampling
doc_type: faq
role: sampler
question_type: 异常与排查
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q062：取样计划有了，但不能取样，为什么？

适用角色：采样员
简短回答：可能原因：1）取样计划已被作废（status=3）；2）取样计划是"未提交"状态（status=0），还没有生效；3）取样计划被分配到了其他组织，您没有权限操作。
详细说明：只有状态为"未取样"（status=1）的取样计划才能进行取样操作。如果计划已作废、还是草稿、或者属于其他组织，您可能看不到或无法操作。
用户下一步应该做什么：确认取样计划的状态。如果是"未提交"，先提交使其生效。如果已作废，重新创建新的取样计划。如果在正确的页面看不到该计划，可能是组织归属问题，联系管理员。
AI回答注意：引导用户先确认状态，不要跳过排查直接手动创建。
可引用的技术依据：`03_raw_material_status_explanation.md`第2.1-2.4节。

---

---
chunk_id: 04-063
module: raw_material_sampling
doc_type: faq
role: tester,sampler
question_type: 异常与排查
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q063：取样后没有生成检测项目，怎么办？

适用角色：化验员、采样员
简短回答：最可能的原因是系统中没有为该物料类型配置检测标准，导致系统无法自动生成检测项目。请联系管理员确认该物料的检测标准是否已配置。
详细说明：系统根据样品关联的物料编码和所在组织的标准配置自动生成检测项目。如果该物料在系统中没有维护"必检项目"的配置，系统就没有依据来生成检测项目，结果为空。
用户下一步应该做什么：1）刷新页面确认；2）联系管理员检查该物料类型的检测标准配置；3）如确认为配置缺失，管理员补充配置后，可能需要重新创建样品或手动关联检测项目。
AI回答注意：【待确认】如果检测项目未生成，是否有手动补救机制（如手动添加检测项目）。
可引用的技术依据：`01_系统全局架构与状态机字典.md`表14，FAQ Q024。

---

---
chunk_id: 04-064
module: raw_material_sampling
doc_type: faq
role: tester
question_type: 异常与排查
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q064：检测项目不能提交，怎么办？

适用角色：化验员
简短回答：可能原因：1）存在必填的检测结果为空；2）化验单状态不是"待提交"（可能已被审核或退回）；3）当前用户不是该化验单的化验员。请逐一排查。
详细说明：提交化验单需要满足：化验单当前状态为"待提交"（status=2），所有必填检测项均有结果，且当前用户是化验单的化验员或具有操作权限。如果有任何一个条件不满足，提交按钮可能灰色或点击后报错。
用户下一步应该做什么：1）检查所有检测项目是否都有结果值；2）确认化验单状态是否为"待提交"；3）确认是否用自己的账号创建的化验单。
AI回答注意：【待确认】系统在提交时的具体校验规则。
可引用的技术依据：`02_核心业务主流程.md`第3.3.5节Step2，`03_raw_material_status_explanation.md`第6.3节。

---

---
chunk_id: 04-065
module: raw_material_sampling
doc_type: faq
role: tester
question_type: 异常与排查
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q065：审核被退回，不理解退回原因怎么办？

适用角色：化验员
简短回答：在系统中查看审核员填写的退回原因。如果原因描述不清楚，直接联系审核员沟通，确认需要修正的具体内容和原因。
详细说明：审核员退回时会填写退回原因（return_reason）字段。您可在化验单详情页或退回记录中查看这个原因。如果审核员填写的原因不够具体，建议直接联系审核员当面或电话沟通，确认问题所在后再修正数据。
用户下一步应该做什么：1）查看退回原因；2）如果原因不明确，联系审核员沟通；3）修正数据后重新提交。
AI回答注意：AI不能代替审核员解释退回原因。建议化验员直接联系审核员。
可引用的技术依据：`02_核心业务主流程.md`第3.3.5节Step4，`03_raw_material_status_explanation.md`第6.6节。

---

---
chunk_id: 04-066
module: raw_material_sampling
doc_type: faq
role: reviewer,quality_manager,dispatcher
question_type: 异常与排查
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q066：检测结果不合格，司机不接受怎么办？

适用角色：审核员、质检主管、调度员
简短回答：不合格判定是系统根据质量标准自动执行的，不是人为判断。如果对检测结果或判定标准有异议，可以通过正规流程申请复核或重新检测。不建议绕过标准直接修改判定结果。
详细说明：如果司机质疑检测结果，可能的处理方式包括：1）核实检测过程是否规范（可查看变更日志）；2）核实质量标准配置是否正确；3）申请复检。请注意：复检机制不在本次MVP范围，具体复检流程需参考完整系统文档。
用户下一步应该做什么：1）向司机解释不合格判定的依据（具体哪个项目超标）；2）如果司机坚持异议，按标准流程申请复核；3）不合格品处理完成前该车原料不能放行。
AI回答注意：不要暗示"可以通融"或"修改判定结果"。坚持"按标准判定，按流程处理"。
可引用的技术依据：`03_raw_material_status_explanation.md`第5节，`02_核心业务主流程.md`第3.3.5节Step3。

---

---
chunk_id: 04-067
module: raw_material_sampling
doc_type: faq
role: reviewer,dispatcher
question_type: 异常与排查
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q067：放行状态一直不更新，但所有化验都完成了，怎么办？

适用角色：审核员、调度员
简短回答：请检查：1）是否所有样品（不是所有化验单）都已完成并审核；2）是否有任何样品的判定结果为R1（不合格）；3）放行状态的更新是否存在延迟或需要手动触发。
详细说明：放行状态的更新依赖于所有样品都合格，而不仅仅是化验单都审核通过。一个取样计划下可能有多个样品，每个样品有多个检测项目。需确认取样计划下所有样品、所有检测项目都已审核通过且全部合格。
用户下一步应该做什么：1）确认取样计划下所有样品的审核和判定状态；2）如果全部合格但仍未放行，联系管理员确认放行机制（是否自动）。
AI回答注意：【待确认】放行的触发方式和可能的延迟机制。
可引用的技术依据：`03_raw_material_status_explanation.md`第4.1节，FAQ Q052、Q053。

---

---
chunk_id: 04-068
module: raw_material_sampling
doc_type: faq
role: admin,dispatcher
question_type: 异常与排查
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q068：一卡通查不到放行，但LIMS显示已放行，怎么办？

适用角色：管理员、调度员
简短回答：可能原因：1）取样计划是手动创建的，没有签到ID（sign_in_id为空），一卡通无法查询；2）签到ID关联有误；3）一卡通查询接口调用参数或时机有问题。
详细说明：一卡通查询放行状态依赖签到ID，接口路径为/ranch/plan/isRelease/{signInId}。如果取样计划是手动创建的（没有签到ID），一卡通没有可用的查询参数。如果有签到ID但查询不到，可能是ID关联错误或接口调用问题。
用户下一步应该做什么：1）确认取样计划是否有sign_in_id；2）如果有，检查该ID是否与一卡通签到时生成的ID一致；3）如果一致但仍查不到，联系管理员检查接口。
AI回答注意：优先检查签到ID是否存在，这是最常见的原因。
可引用的技术依据：`02_核心业务主流程.md`第3.2.4节，FAQ Q008、Q016。

---

---
chunk_id: 04-069
module: raw_material_sampling
doc_type: faq
role: all
question_type: 异常与排查
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q069：用户在系统中找不到操作按钮，怎么办？

适用角色：所有角色
简短回答：可能原因：1）当前状态不允许该操作（按钮仅在特定状态下显示）；2）您的角色没有该操作的权限；3）页面需要刷新；4）查看的页面或Tab不对。
详细说明：系统中的操作按钮通常根据当前记录的状态和用户角色动态显示。例如，化验单的"审核通过"按钮仅在status=3（待审核）时对审核员显示。如果状态不对或角色不对，按钮就不会出现或呈灰色。
用户下一步应该做什么：1）确认当前记录的状态；2）确认您的角色是否有该操作的权限；3）确认是否在正确的页面和Tab中；4）刷新页面。
AI回答注意：引导用户从状态、权限、页面三个方向排查。
可引用的技术依据：`02_raw_material_role_matrix.md`第8节。

---

---
chunk_id: 04-070
module: raw_material_sampling
doc_type: faq
role: all
question_type: 异常与排查
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q070：按钮是灰色的，点不了，怎么办？

适用角色：所有角色
简短回答：按钮灰色通常表示当前不满足操作的前置条件。常见原因：1）当前状态不允许该操作；2）存在必填项未填写；3）需要先完成其他步骤。
详细说明：按钮灰色是系统的前端校验机制，防止在不满足条件时执行操作。比如：化验单提交按钮在"待提交"状态下可用，在"待审核"或"已审核"状态下灰色；开始化验按钮在选择了不同检测项目的样品时可能灰色。
用户下一步应该做什么：1）确认当前记录的状态是否允许该操作；2）检查是否有必填信息未填写；3）确认是否完成了前置操作。
AI回答注意：AI看不到用户的实际界面，只能根据状态逻辑推测可能原因。
可引用的技术依据：`03_raw_material_status_explanation.md`第9节模板3，FAQ Q069。

---

---
chunk_id: 04-071
module: raw_material_sampling
doc_type: faq
role: all
question_type: 异常与排查
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q071：数据看起来重复了，是系统bug吗？

适用角色：所有角色
简短回答：不一定。可能原因：1）同一车原料多次签到创建了多条取样计划；2）同一样品关联了多条检测项目（每个检测项目对应一个化验单明细）；3）复检产生了新的化验记录。建议确认数据的具体内容后再判断。
详细说明：系统中"看起来重复"的数据通常有其业务含义。例如：一个取样计划下多个样品看起来类似（实际是不同的样品）；同一样品的多个检测项目都需要录入结果；如果启动了复检流程，会产生新的化验记录而旧记录保留。
用户下一步应该做什么：确认重复的数据是真正的重复还是正常的多个记录。如果确认是异常重复，联系管理员处理。
AI回答注意：不要轻易说"是bug"。先解释可能的正常原因。
可引用的技术依据：`02_核心业务主流程.md`第3.3.5节Step1防重复机制。

---

---
chunk_id: 04-072
module: raw_material_sampling
doc_type: faq
role: sampler,tester
question_type: 异常与排查
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q072：作废后看不到记录了，正常吗？

适用角色：采样员、化验员
简短回答：正常。作废后的记录不会出现在正常的工作列表中，但数据仍在数据库中保留（软删除）。如果您需要查看作废的记录，可以在系统的历史查询或全部记录中查看。
详细说明：系统使用软删除设计。作废操作将记录标记为无效（如status=3或delete_id标记），而不是物理删除。因此作废的记录在工作列表中"消失"了，但在数据库中是保留的，确保完整的操作审计轨迹。
用户下一步应该做什么：如需查看作废记录，使用系统的查询功能（如选择"全部状态"或"已作废"筛选条件）。如需恢复，联系管理员评估。
AI回答注意：明确告知"数据还在，只是不在工作列表显示了"。这是正常设计，不是bug。
可引用的技术依据：`01_系统全局架构与状态机字典.md`第2.7节，`03_raw_material_status_explanation.md`第2.4节。

---

---
chunk_id: 04-073
module: raw_material_sampling
doc_type: faq
role: all
question_type: 异常与排查
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q073：提示权限不足，怎么办？

适用角色：所有角色
简短回答：说明您的当前账号没有执行该操作的权限。请联系您的直属上级或系统管理员申请相应的权限。
详细说明：系统按角色和部门进行了权限隔离。例如：采样员不能执行化验操作，化验员不能执行审核操作，不同牧场/饲料厂之间的数据不能互看。如果您需要执行某项操作但提示权限不足，可能是因为您的角色不匹配、或试图操作其他组织的数据。
用户下一步应该做什么：1）确认这个操作是否属于您的职责范围；2）如果确实需要该权限，联系管理员申请开通。
AI回答注意：AI不能帮用户绕过权限控制。引导用户走正规权限申请流程。
可引用的技术依据：`02_raw_material_role_matrix.md`第8节，`01_系统全局架构与状态机字典.md`第1.2节。

---

---
chunk_id: 04-074
module: raw_material_sampling
doc_type: faq
role: sampler
question_type: 异常与排查
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q074：找不到原料取样页面入口，怎么办？

适用角色：采样员
简短回答：饲料厂用户请在钉钉小程序中进入"饲料厂检验"→"原料取样"。牧场用户请在钉钉小程序中进入"牧场检验"→原料取样相关功能。如果还是找不到，请联系管理员确认账号是否有原料取样权限。
详细说明：原料取样功能在钉钉小程序中按业务主体分为两个入口。饲料厂路径为Feedmillassay/ylqy/，牧场路径为Ranchassay/。如果看不到这些入口，可能是：钉钉小程序的菜单配置中没有开放给您，或您的账号没有分配对应角色权限。
用户下一步应该做什么：1）确认自己的组织归属；2）在钉钉小程序中仔细查找对应入口；3）如果找不到，联系管理员确认权限配置。
AI回答注意：【待确认】牧场原料取样的具体入口名称和路径。
可引用的技术依据：`01_系统全局架构与状态机字典.md`第2.2节，`01_raw_material_business_overview.md`第4.2节。

---

---
chunk_id: 04-075
module: raw_material_sampling
doc_type: faq
role: ai_reference
question_type: 异常与排查
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q075：AI无法查询实时数据，我被问到实时查询类问题怎么办？

适用角色：AI客服自身参考
简短回答：当用户要求查询实时数据时，明确告知"AI不能查询实时数据库"，同时提供替代方案——告诉用户应该在哪个系统、哪个页面、找哪个角色来获取实时信息。
详细说明：AI客服的知识来自预先分析的技术文档，不能实时连接数据库。遇到以下类型的问题时必须拒绝并引导：1）查询具体车辆/样品/单据的当前状态；2）查询实时统计数据（今天有多少车已放行）；3）查询具体检测结果数值；4）查询具体操作人信息。
用户下一步应该做什么：引导用户通过LIMS系统、一卡通系统自行查询，或联系现场工作人员。
AI回答注意：拒绝时要同步给出替代方案，不要让用户觉得"AI没用"。参考模板见第9节模板5。
可引用的技术依据：`02_raw_material_role_matrix.md`第8节和第9节。

---

---
chunk_id: 04-076
module: raw_material_sampling
doc_type: faq
role: admin
question_type: 异常与排查
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q076：一卡通系统和LIMS系统的数据对不上，可能是什么原因？

适用角色：管理员
简短回答：可能原因：1）两个系统之间的数据不是实时同步的（一卡通通过接口查询LIMS，不是LIMS主动推送）；2）手动创建的取样计划没有签到ID，一卡通无法查询；3）目的地编码不匹配导致取样计划创建到了错误的组织。
详细说明：一卡通和LIMS的交互有两个关键节点：1）签到通知（一卡通→LIMS）：如果通知失败，LIMS不会创建取样计划；2）放行查询（一卡通←LIMS）：一卡通在需要时主动查询，如果时机不对或签到ID不匹配，可能查不到最新状态。
用户下一步应该做什么：1）确认取样计划是否存在以及状态是否正确；2）确认签到ID是否匹配；3）如果数据长期对不上，检查接口日志。
AI回答注意：面向管理员时可详细说明接口和故障点。面向普通用户时只说"两个系统可能需要一点时间同步"。
可引用的技术依据：`02_核心业务主流程.md`第3.2.2节和第3.2.4节。

---

---
chunk_id: 04-077
module: raw_material_sampling
doc_type: faq
role: sampler
question_type: 异常与排查
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q077：多个采样员同时操作同一个取样计划会冲突吗？

适用角色：采样员
简短回答：【待确认】当前文档未明确描述取样环节的并发控制机制。化验环节有分布式锁防重复，但取样环节是否有类似机制需要确认。建议采样员之间事先沟通分配任务，避免多人同时处理同一条计划。
详细说明：在化验环节系统使用了分布式锁防止重复创建化验单，但取样环节是否有类似的防重复机制，当前逆向文档中未明确说明。实际工作中建议采样员之间协调分工。
用户下一步应该做什么：采样员之间事先沟通，明确各自负责的取样计划。如果出现冲突，联系管理员处理。
AI回答注意：如实说明这是【待确认】项，同时给出实用建议。
可引用的技术依据：`02_核心业务主流程.md`第3.3.5节Step1化验环节的分布式锁。

---

---
chunk_id: 04-078
module: raw_material_sampling
doc_type: faq
role: sampler,admin
question_type: 异常与排查
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q078：系统显示的进厂时间和实际不符，怎么办？

适用角色：采样员、管理员
简短回答：如果是一卡通自动创建的取样计划，进厂时间（car_in_time）来自一卡通传过来的签到时间（signInTime）。如果是手动创建的，进厂时间由创建人手动填写。请确认数据来源后再判断是否需要修正。
详细说明：一卡通自动触发时进厂时间等于司机签到时间，通常较准确。手动创建时进厂时间由创建人手动填写，可能存在输入错误。如果需要修正，请联系管理员评估修改方式。
用户下一步应该做什么：确认取样计划的创建方式（一卡通自动还是手动）。如果是手动创建且时间填错，联系管理员修正。
AI回答注意：【待确认】进厂时间字段是否可以在创建后修改，以及修改权限。
可引用的技术依据：`02_核心业务主流程.md`第3.2.2节。

---

---
chunk_id: 04-079
module: raw_material_sampling
doc_type: faq
role: sampler
question_type: 异常与排查
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: true
---

## Q079：取样计划创建后为什么看不到司机信息？

适用角色：采样员
简短回答：如果取样计划是手动创建的，创建人需要手动填写司机姓名和车牌号。如果忘记填写或填写不完整，这些字段就会为空。一卡通自动创建的计划通常会自动填充司机信息。
详细说明：一卡通签到通知中包含司机的姓名（driverName）和车牌号（driverCode），LIMS自动创建时会将这些信息填入取样计划。但手动创建时这些信息需要人工填写，如果漏填就会显示为空。
用户下一步应该做什么：如果司机信息为空且影响了后续操作，采样员可以修改取样计划补充司机信息。如果无法修改，联系管理员。
AI回答注意：【待确认】手动创建的取样计划在创建后是否可以补充司机信息。
可引用的技术依据：`02_核心业务主流程.md`第3.2.2节和第3.2.3节。

---

---
chunk_id: 04-080
module: raw_material_sampling
doc_type: faq
role: sampler,admin
question_type: 异常与排查
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: true
realtime_required: false
ai_can_answer: true
---

## Q080：样品类型被标记为原料，但实际上取的是库存样品，能改吗？

适用角色：采样员、管理员
简短回答：【待确认】样品类型通常由系统根据操作入口自动标记，不建议直接修改。如果确实标记错误，联系管理员评估是否需要作废当前样品并重新创建。
详细说明：样品类型（sampling_type）决定了样品后续的检测流程关联——原料样品关联op_sampling_plan，库存样品关联op_finished_product_sampling_plan_detail。如果类型标记错误，后续的关联链路可能出问题，因此不建议简单修改字段值。
用户下一步应该做什么：如果确认类型标记错误且尚未开始化验，联系管理员处理。如果已经开始化验，情况更复杂，需要管理员评估影响范围。
AI回答注意：不鼓励用户自行修改系统标记字段。引导联系管理员。
可引用的技术依据：`01_系统全局架构与状态机字典.md`表11sampling_type说明。

---

---
chunk_id: 04-081
module: raw_material_sampling
doc_type: faq
role: driver,dispatcher
question_type: AI拒答与边界
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: true
ai_can_answer: false
---

## Q081：帮我查这辆车现在是否放行

适用角色：司机、调度员
问题类型：实时数据查询 - AI不能执行
简短回答：抱歉，我无法查询实时系统数据，不能直接告诉您某辆车当前是否已放行。
详细说明：AI客服不能连接实时数据库。我可以帮您理解"放行"的含义、说明什么条件下会放行、告诉您如何自行查询放行状态。您可以：1）在一卡通系统中输入车牌号或签到号查询放行状态；2）登录LIMS系统查看对应取样计划的放行状态；3）联系现场工作人员协助查询。
替代方案：如果您告诉我您看到的当前状态，我可以帮您解释这个状态的含义以及下一步应该做什么。
AI回答注意：简洁拒绝+给出替代查询方式+提供可做的其他帮助。
可引用的技术依据：`02_raw_material_role_matrix.md`第9节。

---

---
chunk_id: 04-082
module: raw_material_sampling
doc_type: faq
role: sampler,driver,dispatcher
question_type: AI拒答与边界
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: true
ai_can_answer: false
---

## Q082：帮我查这个样品检测结果

适用角色：采样员、司机、调度员
问题类型：实时数据查询 - AI不能执行
简短回答：抱歉，我无法查询实时数据库，不能直接告诉您某个样品的具体检测结果数值。
详细说明：检测结果数据存储在LIMS数据库中，AI没有权限直接查询。您可以通过以下方式获取：1）登录LIMS系统，通过样品编号查询对应的化验单和检测结果；2）联系化验员获取检测结果；3）联系审核员查看已审核的检测报告。
替代方案：如果您想了解检测流程是怎样的、检测结果出来后如何判定合格/不合格，我可以帮您解释。
AI回答注意：直接拒绝查询请求，但提供获取结果的正确途径。
可引用的技术依据：`02_raw_material_role_matrix.md`第9节。

---

---
chunk_id: 04-083
module: raw_material_sampling
doc_type: faq
role: driver,sampler,tester
question_type: AI拒答与边界
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: false
---

## Q083：帮我直接放行

适用角色：司机、采样员、化验员
问题类型：业务操作 - AI不能执行
简短回答：抱歉，AI不能执行放行操作。放行需要所有检测项目合格且审核通过，由系统自动更新或由质检主管确认。
详细说明：AI客服只能提供信息咨询，不能代替人工执行任何业务操作。放行是原料质检流程的最后一步，涉及质量判定和审核确认，必须由有权限的人员在系统中操作或由系统自动执行。
替代方案：如果您想知道为什么还没放行，请告诉我当前取样计划和化验单的状态，我可以帮您排查可能卡在哪一步。
AI回答注意：坚定拒绝+解释放行条件+提供排查帮助。
可引用的技术依据：`02_raw_material_role_matrix.md`第9节风险7。

---

---
chunk_id: 04-084
module: raw_material_sampling
doc_type: faq
role: tester,sampler
question_type: AI拒答与边界
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: false
---

## Q084：帮我审核通过

适用角色：化验员、采样员
问题类型：业务操作 - AI不能执行
简短回答：抱歉，AI不能执行审核操作。审核需要审核员在PC端管理后台查看化验数据后人工确认，不能由AI代替。
详细说明：审核是质量控制的关键环节，需要审核员运用专业判断确认检测数据的有效性。AI不能也不应该代替人工审核。如果审核长时间未完成，建议直接联系审核员了解情况。
替代方案：如果您是化验员且化验单在"待审核"状态，请联系审核员处理。如果您想知道审核的流程和标准，我可以帮您解释。
AI回答注意：强调审核需要人工专业判断，AI不能代替。
可引用的技术依据：`02_raw_material_role_matrix.md`第9节风险7。

---

---
chunk_id: 04-085
module: raw_material_sampling
doc_type: faq
role: tester,sampler
question_type: AI拒答与边界
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: false
---

## Q085：帮我修改检测结果

适用角色：化验员、采样员
问题类型：数据修改 - AI不能执行
简短回答：抱歉，AI不能修改系统中的任何数据。检测结果的修改需要通过LIMS系统的正规流程——化验单在"待提交"状态下可直接修改，审核通过后需通过"结果变更"功能修改。
详细说明：检测结果的修改是敏感操作，系统有完整的权限控制和变更日志机制。如果确实需要修改，请：1）化验单在"待提交"状态→直接在化验单中修改；2）已提交但未审核→联系审核员退回后修改；3）已审核→使用"结果变更"功能（会记录变更日志）。
替代方案：如果您需要了解如何正确修改数据，我可以告诉您对应的操作路径和状态条件。
AI回答注意：拒绝修改请求+告知正规修改路径。
可引用的技术依据：`02_核心业务主流程.md`第3.3.5节Step7，`02_raw_material_role_matrix.md`第9节风险7。

---

---
chunk_id: 04-086
module: raw_material_sampling
doc_type: faq
role: tester,admin
question_type: AI拒答与边界
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: false
---

## Q086：帮我重新推送到SAP

适用角色：化验员、管理员
问题类型：系统操作 - 不在MVP范围
简短回答：抱歉，SAP推送功能不在当前AI知识库的覆盖范围内，AI无法协助SAP推送相关操作。
详细说明：SAP推送属于系统集成模块，当前AI客服的MVP知识库主要涵盖原料取样、化验、审核、放行的核心流程。SAP相关的推送逻辑、批次绑定等问题不在本次知识库范围内。
替代方案：关于SAP推送的问题，请咨询系统管理员或查阅SAP集成相关文档。
AI回答注意：明确说明不在范围，不要尝试猜测SAP相关的内容。
可引用的技术依据：`00_raw_material_scope_analysis.md`第3.4节。

---

---
chunk_id: 04-087
module: raw_material_sampling
doc_type: faq
role: sampler,tester
question_type: AI拒答与边界
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: false
---

## Q087：帮我删掉这个样品

适用角色：采样员、化验员
问题类型：数据删除 - AI不能执行
简短回答：抱歉，AI不能执行删除操作。系统中的删除是软删除（标记为作废而非物理删除），需要由有权限的人员在LIMS系统中操作。
详细说明：系统使用软删除设计，不会真正物理删除数据。如果您需要作废一个样品，需要在LIMS系统中按照正常的作废流程操作。不同类型和阶段的作废有不同的影响范围（可能同时作废关联的化验数据）。
替代方案：如果您想了解作废的操作方法和影响范围，我可以帮您解释。
AI回答注意：面向管理员时可以提"软删除"，面向一线用户说"作废"，告知数据仍在系统中保留。
可引用的技术依据：`01_系统全局架构与状态机字典.md`第2.7节，`02_核心业务主流程.md`第3.3.5节Step7。

---

---
chunk_id: 04-088
module: raw_material_sampling
doc_type: faq
role: dispatcher,driver
question_type: AI拒答与边界
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: true
ai_can_answer: false
---

## Q088：帮我查询实时排队号/还有多少车在等待

适用角色：调度员、司机
问题类型：实时统计 - AI不能执行
简短回答：抱歉，AI不能查询实时统计数据，无法告诉您当前有多少车在等待取样或化验。这类信息需要在LIMS系统中通过报表或列表筛选查看。
详细说明：AI客服不能连接实时数据库，无法提供任何实时统计数据（如排队数量、今日放行数、平均等待时间等）。这些数据可以通过LIMS系统的报表功能或列表筛选功能获取。
替代方案：登录LIMS系统，在取样计划列表或化验任务列表中使用状态筛选查看当前各状态的记录数量。
AI回答注意：明确说明AI不能提供实时统计，提供替代查询方式。
可引用的技术依据：`02_raw_material_role_matrix.md`第9节。

---

---
chunk_id: 04-089
module: raw_material_sampling
doc_type: faq
role: dispatcher,admin
question_type: AI拒答与边界
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: true
ai_can_answer: false
---

## Q089：帮我判断责任人是谁

适用角色：调度员、管理员
问题类型：责任判断 - AI不能执行
简短回答：抱歉，AI不能判定某次延迟或问题的具体责任人。但我可以帮您分析：1）当前状态卡在哪个环节；2）每个环节的通常责任人是谁；3）建议联系哪个角色推进。
详细说明：AI可以根据流程知识告诉您每个环节对应哪个角色（如"未取样→采样员"、"待审核→审核员"），但不能判断具体是哪个人的问题。实际责任判定需要结合排班信息、操作日志等实时数据，AI无法获取这些信息。
替代方案：我可以帮您按状态排查当前卡在哪个环节，以及这个环节通常由谁负责处理。
AI回答注意：区分"角色"和"具体人"。AI可以告诉对应角色，不能指认具体个人。
可引用的技术依据：`02_raw_material_role_matrix.md`第6.2节。

---

---
chunk_id: 04-090
module: raw_material_sampling
doc_type: faq
role: driver,dispatcher
question_type: AI拒答与边界
visibility: public
source_doc: 04_raw_material_operation_faq.md
status_related: false
realtime_required: false
ai_can_answer: false
---

## Q090：帮我绕过审核流程，这车原料很急

适用角色：司机、调度员
问题类型：流程违规 - AI不能协助
简短回答：抱歉，审核是质量控制的关键环节，不能跳过。所有原料必须经过完整的取样→化验→审核→放行流程。"很急"不能成为跳过质量检验的理由。
详细说明：审核环节的设计目的是确保检测数据的准确性和有效性。跳过审核直接放行，意味着使用了未经确认的检测结果，可能导致不合格原料进入生产环节。这在质量管理和食品安全方面存在严重风险。AI不能协助任何试图绕过标准流程的行为。
替代方案：如果确实紧急，建议联系审核员优先处理该化验单的审核，而不是跳过审核。
AI回答注意：坚定拒绝，说明原因（质量安全），不要给任何绕过流程的暗示或替代方案（除了"联系审核员优先处理"）。
可引用的技术依据：`01_raw_material_business_overview.md`第1节。
