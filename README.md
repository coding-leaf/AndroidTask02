# 实验报告：Android 界面布局实验

## 一、实验目的与环境
- **实验目的**：
  1. 深入理解 Android 传统 View 体系下常用布局管理器的工作原理，包括线性布局（`LinearLayout`）、表格布局（`TableLayout`）以及约束布局（`ConstraintLayout`）。
  2. 熟练掌握 XML 布局文件中核心属性的应用，如方向控制、权重分配（`layout_weight`）、内边距/外边距（`padding`/`margin`）、列拉伸（`stretchColumns`）、列定位（`layout_column`）以及相对约束锚点（`layout_constraint*`）。
  3. 学习现代 Android 声明式 UI 框架 **Jetpack Compose** 的核心开发范式，掌握 `Column`、`Row`、`LazyColumn` 等常用组件。
  4. 掌握 Compose 中的响应式状态管理（State）与自动重组机制（Recomposition），利用 `remember`、`mutableStateOf`、`mutableStateListOf` 实现动态增删与状态同步联动。
- **实验环境**：
  - 操作系统：Windows 11
  - 开发工具：Android Studio Ladybug / Iguana
  - 开发语言：Kotlin
  - UI 架构：Android View XML + Jetpack Compose (Material Design 3)
  - 构建系统：Gradle 8.x + Kotlin DSL (`build.gradle.kts`)
  - 参考规范：`实验2_Android界面布局.pdf`、[Android 开发者文档](https://developer.android.google.cn/guide/topics/ui/declaring-layout.html)

---

## 二、实验任务

根据实验指导书要求，本次实验分为传统布局（XML）与现代声明式布局（Jetpack Compose）两大板块，共包含以下五个实验任务：

### 任务 1：线性布局（LinearLayout）网格界面实现
- **任务目标**：利用嵌套 `LinearLayout` 实现 4 行 4 列的黑底灰框网格界面。
- **内容要求**：
  - 单元格文字从 `One,One` 递增排列至 `Four,Four`。
  - 界面背景为纯黑，每个单元格具有灰色矩形描边。
  - 第一行、第二行和第四行的第 2 列元素（如 `One,Two`、`Two,Two`、`Four,Two`）使用权重自适应填充剩余空间；第三行 4 个元素均分占据整行宽度。

### 任务 2：表格布局（TableLayout）快捷键菜单实现
- **任务目标**：使用 `TableLayout` 和 `TableRow` 构建经典桌面菜单栏样式的快捷键列表界面。
- **内容要求**：
  - 顶部设置菜单标题 `Hello TableLayout`。
  - 包含 `Open... (Ctrl-O)`、`Save... (Ctrl-S)`、`Save As... (Ctrl-Shift-S)`、`Import...`、`Export... (Ctrl-E)`、`Quit` 等菜单项。
  - 设置 `android:stretchColumns="1"` 实现内容列自适应拉伸，使快捷键右对齐展示。
  - 使用 1dp 高度的细白线条作为分割线。

### 任务 3：约束布局一（ConstraintLayout）计算器界面实现
- **任务目标**：基于 `ConstraintLayout` 实现简易计算器的人机交互布局。
- **内容要求**：
  - 顶部设置墨绿色标题栏 `ConstraintLayoutTest` 及 `Input` 输入提示。
  - 中部设置米黄色背景的计算结果展示区（默认文本 `0.0` 靠右居中显示）。
  - 下方利用相对约束构建 4×4 规则的按键矩阵（`7 8 9 +`、`4 5 6 *`、`1 2 3 -`、`. 0 = /`），按键宽度设为 `0dp` 并通过水平约束链均分屏幕。

### 任务 4：约束布局二（ConstraintLayout）太空票务界面实现
- **任务目标**：使用 `ConstraintLayout` 还原航线票务预订界面。
- **内容要求**：
  - 顶部 Tab 栏：横向三等分放置 `Space Stations`、`Flights`、`Rovers` 及其图标，高亮当前选中的 `Flights`。
  - 航线卡片区：左右两个墨绿卡片标注起始点 `DCA` 和目的地 `MARS`，中间通过双向箭头图标重叠连接（应用负边距与 `elevation` 确保图层层级）。
  - 行程配置区：设置带圆角背景的单向切换开关 `One way` 与单人出行标识 `1 Traveller`。
  - 背景与插图：相对居中放置星系 `galaxy` 插图与火箭 `rocket_icon`。
  - 底部操作区：底部固定全宽深绿色出发操作按钮 `DEPART`。

### 任务 5：Jetpack Compose 声明式 UI 实验（课程学习任务清单）
- **任务目标**：使用现代声明式框架 Jetpack Compose 开发一款课程学习待办任务管理 App。
- **内容要求**：
  - **初始状态**：展示标题“课程学习任务”，初始默认加载 3 项任务，其中第 1 项已完成；完成度统计显示“已完成: 1/3”。
  - **添加任务**：支持单行输入框与红色“添加”按钮，输入新任务（如“复习 LazyColumn”）后点击可即时添加到列表中，并清空输入框。
  - **状态更新**：点击 Checkbox 切换任务勾选状态，任务标题根据状态动态添加/取消中划线（删除线）及置灰效果，顶部已完成数量与完成比例同步实时更新。
  - **删除任务**：每张卡片右侧提供“删除”按钮，点击后从列表中移除该项，总任务数与已完成数同步刷新。

---

## 三、实验步骤与核心实现

### 步骤 1：线性布局（LinearLayout）的设计与实现

1. **单元格边框 Drawable 设计**：
   在 `app/src/main/res/drawable/border_cell.xml` 中使用 `<shape>` 标签创建带浅灰描边的黑色矩形背景：
   ```xml
   <?xml version="1.0" encoding="utf-8"?>
   <shape xmlns:android="http://schemas.android.com/apk/res/android"
       android:shape="rectangle">
       <!-- 内部填充黑色 -->
       <solid android:color="#000000" />
       <!-- 边框描边：1dp 宽，灰色 -->
       <stroke
           android:width="1dp"
           android:color="#888888" />
   </shape>
   ```

2. **多层嵌套与权重排版**：
   在 `app/src/main/res/layout/activity_linear_layout.xml` 中：
   - 根布局为垂直方向的 `LinearLayout`（`android:orientation="vertical"`），设置黑色背景及内边距。
   - 每一行使用一个水平方向的 `LinearLayout`（`android:orientation="horizontal"`）。
   - 第 1、2、4 行中，将第二列 `TextView` 设置 `android:layout_width="0dp"` 并赋予 `android:layout_weight="1"`，使其自动吸收该行剩余宽度；其余列设为 `wrap_content`。
   - 第 3 行中，将全部 4 个 `TextView` 的 `layout_width` 均设为 `0dp` 且 `layout_weight="1"`，使得整行宽度被 4 个单元格完全均分：
   ```xml
   <!-- Row 3: 4 项均分整行宽度 -->
   <LinearLayout
       android:layout_width="match_parent"
       android:layout_height="wrap_content"
       android:orientation="horizontal">

       <TextView
           android:layout_width="0dp"
           android:layout_height="wrap_content"
           android:layout_weight="1"
           android:background="@drawable/border_cell"
           android:text="Three,One"
           android:textColor="#CCCCCC"
           android:gravity="center"
           android:padding="10dp"
           android:layout_margin="2dp"/>
       <!-- Three,Two / Three,Three / Three,Four 配置同上 -->
   </LinearLayout>
   ```

---

### 步骤 2：表格布局（TableLayout）的设计与实现

1. **表格结构与自适应拉伸**：
   在 `app/src/main/res/layout/activity_table_layout.xml` 中以 `TableLayout` 为根容器，设置背景色 `#696969`，并通过 `android:stretchColumns="1"` 声明第 2 列自动拉伸：
   ```xml
   <TableLayout xmlns:android="http://schemas.android.com/apk/res/android"
       android:layout_width="match_parent"
       android:layout_height="match_parent"
       android:background="#696969"
       android:stretchColumns="1">
   ```

2. **菜单项与快捷键布局**：
   - 顶部设置横跨全屏的标题 `TextView`。
   - 使用 `<TableRow>` 组织菜单项：
     - 普通菜单项利用 `android:layout_column="1"` 跳过第 0 列，使功能名占据第 1 列，快捷键置于第 2 列并通过 `android:gravity="right"` 实现靠右对齐。
     - 包含前缀图标/文字项（如 `X Import...`）在第 0 列放置单独的 `TextView` 显示 `"X"`。
   - 使用高度为 `1dp` 的纯白色 `<View>` 组件作为菜单段落之间的分割线。

---

### 步骤 3：约束布局一（ConstraintLayout）计算器界面实现

1. **顶部区域定位**：
   在 `app/src/main/res/layout/activity_constraint_layout1.xml` 中：
   - 标题栏 `tv_title` 锚定在父容器顶部，左右撑满（`app:layout_constraintStart_toStartOf="parent"`、`app:layout_constraintEnd_toEndOf="parent"`）。
   - 输入提示 `tv_input` 锚定在标题栏下方左侧。
   - 结果展示框 `result` 左右约束在父容器两侧，顶部锚定在 `tv_input` 下方，文本靠右居中显示。

2. **4×4 按钮网格约束链构建**：
   - 按钮宽度均设为 `0dp`（`MATCH_CONSTRAINT`），通过相互绑定实现水平链：
     - 第一列按钮左侧锚定 `parent`，右侧锚定第二列按钮；
     - 中间列按钮分别将左右侧锚定到相邻按钮；
     - 最后一列按钮右侧锚定 `parent`。
   - 垂直方向上，每一行按钮以同行的第一个按钮顶部对齐（`layout_constraintTop_toTopOf`），行与行之间通过 `layout_constraintTop_toBottomOf` 进行级联约束：
   ```xml
   <Button
       android:id="@+id/btn_7"
       android:layout_width="0dp"
       android:layout_height="wrap_content"
       android:layout_marginStart="16dp"
       android:layout_marginTop="24dp"
       android:layout_marginEnd="4dp"
       android:text="7"
       app:layout_constraintStart_toStartOf="parent"
       app:layout_constraintEnd_toStartOf="@id/btn_8"
       app:layout_constraintTop_toBottomOf="@id/result" />

   <Button
       android:id="@+id/btn_8"
       android:layout_width="0dp"
       android:layout_height="wrap_content"
       android:layout_marginStart="4dp"
       android:layout_marginEnd="4dp"
       android:text="8"
       app:layout_constraintStart_toEndOf="@id/btn_7"
       app:layout_constraintEnd_toStartOf="@id/btn_9"
       app:layout_constraintTop_toTopOf="@id/btn_7" />
   ```

---

### 步骤 4：约束布局二（ConstraintLayout）太空票务界面实现

1. **顶部导航 Tab 栏**：
   在 `app/src/main/res/layout/activity_constraint_layout2.xml` 中，顶部设置 `tab_bar` 水平容器，包含 `Space Stations`、`Flights`、`Rovers` 三大选项，通过权重均分宽度并居中展示图标与文字。

2. **航线站点卡片与双向箭头重叠层级**：
   - `DCA` 与 `MARS` 卡片采用对称约束，分别靠向中心。
   - 中间放置双向箭头图标 `double_arrows`，左右卡片分别配置负边距（`-15dp`），结合 `android:elevation="4dp"` 使箭头浮于卡片之上，形成无缝衔接的视觉效果。

3. **行程选项与底部操作按钮**：
   - `Switch` 单向切换开关与 `1 Traveller` 标签垂直阶梯约束排列。
   - 页面居中锚定星系 `galaxy` 插图与火箭图标。
   - 底部操作按钮 `btn_depart` 宽度设为 `0dp`，左右贴合父容器，底部锚定在 `layout_constraintBottom_toBottomOf="parent"`。

---

### 步骤 5：Jetpack Compose 响应式任务管理器的设计与实现

1. **响应式数据模型（`TaskModel.kt`）**：
   采用 Kotlin 属性委托 `by mutableStateOf` 将任务完成状态声明为 Compose 响应式可观察属性：
   ```kotlin
   package com.example.task2

   import androidx.compose.runtime.getValue
   import androidx.compose.runtime.mutableStateOf
   import androidx.compose.runtime.setValue

   class TaskItem(
       val id: Int,
       val title: String,
       initialCompleted: Boolean = false
   ) {
       // 当 isCompleted 发生改变时，自动触发依赖该状态的 Compose 组件局部重组
       var isCompleted by mutableStateOf(initialCompleted)
   }
   ```

2. **主页面骨架与状态管理（`MainActivity.kt`）**：
   在 `TaskScreen` 组件中进行状态统筹与布局编排：
   - 输入框状态：`var inputText by remember { mutableStateOf("") }`。
   - 任务列表状态：`val taskList = remember { mutableStateListOf(...) }`，初始填入 3 条数据。
   - 派生计数：`val completedCount = taskList.count { it.isCompleted }` 自动随列表及子项状态变化而响应式更新。
   - 输入与新增交互：
     ```kotlin
     Button(
         onClick = {
             if (inputText.isNotBlank()) {
                 taskList.add(
                     TaskItem(
                         id = nextId++,
                         title = inputText.trim(),
                         initialCompleted = false
                     )
                 )
                 inputText = "" // 提交后清空输入框
             }
         },
         shape = RoundedCornerShape(8.dp),
         colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed),
         modifier = Modifier.height(50.dp)
     ) {
         Text(text = "添加", color = Color.White, fontWeight = FontWeight.Bold)
     }
     ```

3. **任务列表卡片与联动删除线（`TaskCardItem`）**：
   - 列表渲染采用 `LazyColumn`，配置 `key = { it.id }` 提升重组性能。
   - 卡片内部集成复选框、文本和删除操作：
     ```kotlin
     @Composable
     fun TaskCardItem(
         task: TaskItem,
         onDelete: () -> Unit
     ) {
         Row(
             modifier = Modifier
                 .fillMaxWidth()
                 .border(1.dp, BorderGray, RoundedCornerShape(8.dp))
                 .background(Color.White, RoundedCornerShape(8.dp))
                 .padding(horizontal = 8.dp, vertical = 6.dp),
             verticalAlignment = Alignment.CenterVertically
         ) {
             // 1. 复选框状态绑定
             Checkbox(
                 checked = task.isCompleted,
                 onCheckedChange = { task.isCompleted = it },
                 colors = CheckboxDefaults.colors(checkedColor = PrimaryRed)
             )

             // 2. 动态样式绑定（完成时添加删除线并置灰）
             Text(
                 text = task.title,
                 modifier = Modifier.weight(1f),
                 style = TextStyle(
                     fontSize = 14.sp,
                     color = if (task.isCompleted) Color(0xFF9E9E9E) else Color(0xFF212121),
                     textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                 )
             )

             // 3. 删除操作
             Text(
                 text = "删除",
                 color = PrimaryRed,
                 fontSize = 13.sp,
                 modifier = Modifier
                     .clickable { onDelete() }
                     .padding(horizontal = 8.dp, vertical = 6.dp)
             )
         }
     }
     ```

---

## 四、实验结果与效果对照

| 实验任务 | 核心知识点 | 验收效果与功能指标 |
| :--- | :--- | :--- |
| **1. 线性布局** | 嵌套 `LinearLayout`、`layout_weight`、`border_cell.xml` | 4×4 黑底灰框矩阵整齐排列，权重行自适应延展，边框效果一致 |
| **2. 表格布局** | `TableLayout`、`TableRow`、`stretchColumns="1"` | 快捷键列表对齐规范，分割线分段清晰，第 2 列自适应拉伸 |
| **3. 约束布局 1** | `ConstraintLayout`、`MATCH_CONSTRAINT(0dp)`、水平约束链 | 计算器标题、输入输出框及 4×4 按钮网格完美自适应屏幕宽度 |
| **4. 约束布局 2** | 图文混排、负边距层叠、Elevation、底部停靠约束 | 航线站点卡片与居中双向箭头层次分明，底部 DEPART 稳定贴底 |
| **5. Jetpack Compose** | 声明式状态、`mutableStateListOf`、`LazyColumn`、重组联动 | ① 初始 3 项已完成 1 项；② 输入并添加新任务即时上屏；③ 勾选打勾/划线与计数实时联动；④ 点击删除精准移除 |

---

## 五、实验总结与体会

1. **传统 XML 布局机制总结**：
   - `LinearLayout` 简单直观，适合一维线性排版，但在多行多列网格中过度嵌套容易导致布局性能下降。
   - `TableLayout` 简化了多列表格的数据排列，通过列拉伸（`stretchColumns`）和列跳跃（`layout_column`）能快速构建规整结构。
   - `ConstraintLayout` 极大提升了 XML 布局的灵活性，能够以平铺方式解决复杂重叠、比例均分与多组件依赖关系，是传统 View 体系下构建复杂界面的首选。

2. **Jetpack Compose 声明式 UI 总结**：
   - **状态驱动视图（State Driven UI）**：UI 不再通过 `findViewById` 手动调用更新，而是真正实现了“UI = f(State)”。界面根据可观察状态自动触发局部重组（Recomposition），从根本上避免了状态与视图脱节的问题。
   - **极高的开发效率与代码可维护性**：所有布局与逻辑均在 Kotlin 代码中完成，配合组合式函数（`Composable`），组件的拆分、复用与参数传递变得格外简单清晰。
