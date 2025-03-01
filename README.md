> QQ联系我：1938667363
>
> **注意：以超级管理员身份登录系统时，若出现接口提示 “不允许访问”，请点击 “系统管理/系统权限/接口管理” 中的 “接口采集/校验” 按钮进行接口权限校验。**

### 1. 项目技术路线

`JDK8` + `Spring-Boot` + `Vue3` + `ElementUI-plus`



### 2. 项目介绍

本项目是一个在线考试/培训平台，但不仅仅是在线考试/培训平台。

```sh
hanjiang-etm                      # 项目主目录
  -- hanjiang-etm-admin           # 后端：启动模块
  -- hanjiang-etm-biz             # 后端：业务目录
    -- hanjiang-etm-etm           # 后端：考试管理/培训管理/任务中心模块
    -- hanjiang-etm-paper         # 后端：试卷管理模块
    -- hanjiang-etm-question      # 后端：题目管理模块
    -- hanjiang-etm-statistics    # 后端：统计分析模块
  -- hanjiang-etm-common          # 后端：公共依赖模块
  -- hanjiang-etm-system          # 后端：系统目录
    -- hanjiang-etm-auth          # 后端：系统权限模块
    -- hanjiang-etm-menu          # 后端：系统菜单模块
    -- hanjiang-etm-org           # 后端：组织机构模块
    -- hanjiang-etm-params        # 后端：系统参数模块
    -- hanjiang-etm-resource      # 后端：资源管理模块
    -- hanjiang-etm-user          # 后端：用户管理模块
  -- hanjiang-etm-web             # 前端：前端项目
  -- db.sql                       # 数据库：数据库初始化脚本
```

#### 2.1. 考试培训模块

**① 支持 5 种试题类型**

单选题、多选题、判断题、填空题、解答题，支持题目的批量导入。

**② 支持配置试卷模版**

以便在考试模块直接选择试卷。试卷模版与试题之间是解耦的，即试题的二次修改/删除不会影响到试卷模版。

**③ 支持 2 种考试类型**

系统内部考试：参加考试的人员需要选择，且只能是本平台的用户。

扫码开放考试：参加考试的人员无需选择，用户通过钉钉扫码参加考试。

**④ 支持 3 种题目规则**

题目固定，试卷相同：试卷配置需要选择已有的试卷，且每一个参加考试的用户试卷相同的。

题目随机，试卷不同：试卷配置只需配置题目类型、数量、分数，试卷随机生成，且每一个参加考试的用户试卷都不同。

题目随机，试卷相同：试卷配置只需配置题目类型、数量、分数，试卷随机生成, 且每一个参加考试的用户试卷相同。

**⑤ 支持 2 种批改方式**

机器批改：用户提交试卷后，系统将完成试卷的自动批改。

人工批改：用户提交试卷后，系统将自动批改单选、多选、判断题（人工可以二次批改），其他题型需人工批改。人工提交批改后用户考试成绩才有效。

**⑥ 支持防作弊**

开启防作弊后，当用户在WEB端参加考试时，系统将采取一定的检测手段，有但不限于：检测切换浏览器标签、切换应用、最小化浏览器窗口等其他违规行为。

**⑦ 支持发起培训**

支持上传培训材料（因为不同项目文件预览方案的不同，文件预览暂实现，前端已经预留相关方法），支持配置培训测试。



#### 2.2. 系统管理模块

**① 组织机构管理**

用户可随意编排组织机构树，支持单位/部门，支持有效/无效，支持排序。

**② 用户信息管理**

支持常见的用户信息的录入，支持重置密码、解封/禁用账号、支持配置用户角色。

**③ 按钮管理**

支持根据菜单录入按钮，支持有效/无效，以便对菜单中按钮的权限进行控制。

**④ 接口管理**

支持一键采集/校验接口配置（此操作将会自动扫描后端接口，并完成插入/更新/删除操作），支持有效/无效，以便对菜单中接口的权限进行控制。

**⑤ 角色管理**

用户可随意编排角色树，支持配置角色权限，权限分为 3 部分。

菜单权限：控制角色可访问的菜单。

按钮权限：控制角色可访问的菜单中的按钮。

接口权限：控制角色可访问的后端接口。

**⑥ 存储配置**

附件上传支持三种存储协议：S3（对象存储）、LOCAL（本地存储）、FTP（ftp远程存储）。

**⑦ 附件管理**

支持管理（删除、下载）已经上传的附件。

**⑧ 附件日志**

记录用户对附件的操作，如：上传、下载、删除。

**⑨ 系统参数**

可配置/修改系统所需要的参数，例如：允许上传的文件类型、单个文件的 MaxSiz、TokenTTL 等。

**⑩ 系统菜单**

管理系统的所有菜单，支持菜单大部分配置，例如：层级关系、图标、名称、组件位置（仅限前端 views 文件下）、排序、是否有效、路由参数等。



### 3. 项目截图

登录页：

![image-20250208171003488](image/image-20250208171003488.png)

用户的任务中心：

![image-20250208170926249](image/image-20250208170926249.png)



![image-20250208171039308](image/image-20250208171039308.png)

题目管理：

![image-20250208171131360](image/image-20250208171131360.png)

![image-20250208171109821](image/image-20250208171109821.png)

![image-20250208171156331](image/image-20250208171156331.png)

试卷管理：

![image-20250208171358186](image/image-20250208171358186.png)

![image-20250208171454158](image/image-20250208171454158.png)

![image-20250208171242567](image/image-20250208171242567.png)

![image-20250208171315161](image/image-20250208171315161.png)

考试管理：

![image-20250208171517628](image/image-20250208171517628.png)

![image-20250208171604689](image/image-20250208171604689.png)

![image-20250208171536419](image/image-20250208171536419.png)

培训管理：

![image-20250208171645118](image/image-20250208171645118.png)

![image-20250208210604780](image/image-20250208210604780.png)

![image-20250208171930384](image/image-20250208171930384.png)

组织机构：

![image-20250208172008790](image/image-20250208172008790.png)

![image-20250208213930486](image/image-20250208213930486.png)

用户管理：

![image-20250208172304211](image/image-20250208172304211.png)

![image-20250208213901919](image/image-20250208213901919.png)

![image-20250208172328888](image/image-20250208172328888.png)

按钮管理：

![image-20250208172412554](image/image-20250208172412554.png)

接口管理：

![image-20250208213808004](image/image-20250208213808004.png)

![image-20250208213824981](image/image-20250208213824981.png)

![image-20250208172551338](image/image-20250208172551338.png)

角色管理：

![image-20250208213716022](image/image-20250208213716022.png)

![image-20250208213734191](image/image-20250208213734191.png)

![image-20250208172809915](image/image-20250208172809915.png)

存储配置

![image-20250208212628785](image/image-20250208212628785.png)

![image-20250208211244138](image/image-20250208211244138.png)

![image-20250208211130059](image/image-20250208211130059.png)

附件管理：

![image-20250208211525877](image/image-20250208211525877.png)

附件日志：

![image-20250208211650468](image/image-20250208211650468.png)

系统参数：

![image-20250208211711847](image/image-20250208211711847.png)

![image-20250208213643075](image/image-20250208213643075.png)

系统菜单：

![image-20250208213613937](image/image-20250208213613937.png)

![image-20250208173336743](image/image-20250208173336743.png)

用户信息：

![image-20250209170554768](image/image-20250209170554768.png)

统计分析：

![image-20250208214322675](image/image-20250208214322675.png)