package com.example.task2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ==========================================
// 🎨 主题配色：提取截图中的酒红色与边框灰
// ==========================================
val PrimaryRed = Color(0xFFB71C1C)      // 按钮与复选框的高亮酒红色
val BorderGray = Color(0xFFE0E0E0)      // 卡片外轮廓浅灰边框
val BackgroundColor = Color(0xFFF9F9FB) // 整体极淡灰白底色

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // 开启沉浸式状态栏体验
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = BackgroundColor
            ) {
                TaskScreen()
            }
        }
    }
}

/**
 * 🌟 主界面：相当于 Vue 的主页面组件 <template>
 */
@Composable
fun TaskScreen() {
    // -------------------------------------------------------------------------
    // 1. 响应式数据状态定义（对应 Vue 3 的 ref / reactive）
    // -------------------------------------------------------------------------

    // 输入框文字：相当于 const inputText = ref("")
    var inputText by remember { mutableStateOf("") }

    // 任务自增 ID 计数器，保证每一条数据有唯一 key
    var nextId by remember { mutableStateOf(4) }

    // 核心任务列表：相当于 Vue 的 const taskList = reactive([...])
    // 初始状态：3 项任务，其中第 1 项完成
    val taskList = remember {
        mutableStateListOf(
            TaskItem(id = 1, title = "学习 Column 和 Row", initialCompleted = true),
            TaskItem(id = 2, title = "学习状态管理", initialCompleted = false),
            TaskItem(id = 3, title = "完成 Compose 实验", initialCompleted = false)
        )
    }

    // 动态计算已完成的数量：相当于 Vue 的 computed(() => taskList.filter(...).length)
    val completedCount = taskList.count { it.isCompleted }

    // -------------------------------------------------------------------------
    // 2. 界面排版骨架：从上往下垂直布局（Column 相当于 flex-direction: column）
    // -------------------------------------------------------------------------
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding() // 避开手机顶部时间、电量栏
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // [A] 大标题：课程学习任务
        Text(
            text = "课程学习任务",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // [B] 输入区域：水平横排（Row 相当于 flex-direction: row）
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 输入文本框
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it }, // 输入时双向同步
                placeholder = {
                    Text(
                        text = "请输入学习任务",
                        fontSize = 13.sp,
                        color = Color(0xFF9E9E9E)
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                modifier = Modifier
                    .weight(1f) // flex: 1 撑满左侧可用空间
                    .height(50.dp)
            )

            Spacer(modifier = Modifier.width(10.dp)) // 间距占位

            // 红色“添加”按钮
            Button(
                onClick = {
                    if (inputText.isNotBlank()) {
                        // 往响应式列表追加新项，UI 会自动察觉并重画
                        taskList.add(
                            TaskItem(
                                id = nextId++,
                                title = inputText.trim(),
                                initialCompleted = false
                            )
                        )
                        inputText = "" // 清空输入框
                    }
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed),
                modifier = Modifier.height(50.dp)
            ) {
                Text(
                    text = "添加",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // [C] 统计信息显示：已完成: X / Y
        Text(
            text = "已完成: $completedCount / ${taskList.size}",
            fontSize = 13.sp,
            color = Color(0xFF757575),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // [D] 任务滚动列表：LazyColumn 相当于高效的带虚拟滚动的 v-for
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp) // 每一张卡片之间留白 10dp
        ) {
            // items 循环列表，相当于 v-for="task in taskList" :key="task.id"
            items(items = taskList, key = { it.id }) { task ->
                TaskCardItem(
                    task = task,
                    onDelete = { taskList.remove(task) } // 点击删除时从列表中移除
                )
            }
        }
    }
}

/**
 * 🌟 单个任务项卡片子组件：带圆角边框、Checkbox、删除线以及删除按钮
 */
@Composable
fun TaskCardItem(
    task: TaskItem,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = BorderGray, shape = RoundedCornerShape(8.dp)) // 浅灰圆角边框
            .background(color = Color.White, shape = RoundedCornerShape(8.dp)) // 白底
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. 复选框（打勾联动）
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = { task.isCompleted = it }, // 改了状态，自动触发重绘与计数变化
            colors = CheckboxDefaults.colors(
                checkedColor = PrimaryRed,        // 选中时深红色
                uncheckedColor = Color.LightGray, // 未选中浅灰边框
                checkmarkColor = Color.White      // 对勾白色
            )
        )

        Spacer(modifier = Modifier.width(4.dp))

        // 2. 任务标题（如果已勾选，显示中划线并置灰）
        Text(
            text = task.title,
            modifier = Modifier.weight(1f), // 撑满中间空间
            style = TextStyle(
                fontSize = 14.sp,
                color = if (task.isCompleted) Color(0xFF9E9E9E) else Color(0xFF212121),
                textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
            )
        )

        // 3. 右侧“删除”操作文字
        Text(
            text = "删除",
            color = PrimaryRed,
            fontSize = 13.sp,
            modifier = Modifier
                .clickable { onDelete() } // 监听点击事件
                .padding(horizontal = 8.dp, vertical = 6.dp)
        )
    }
}

// 预览函数：可以在 Android Studio 右侧 Design 窗口直接查看界面效果
@Preview(showBackground = true)
@Composable
fun TaskScreenPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundColor
    ) {
        TaskScreen()
    }
}
