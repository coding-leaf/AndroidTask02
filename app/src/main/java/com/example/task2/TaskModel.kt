package com.example.task2

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * 任务数据模型
 * @param id 任务的唯一编号，用来在列表中精确区分每一项
 * @param title 任务的名字，比如 "学习Column和Row"
 * @param initialCompleted 初始是否完成，默认为 false
 */
class TaskItem(
    val id: Int,
    val title: String,
    initialCompleted: Boolean = false
)
{
    // 💡 知识点 1：mutableStateOf
    // 把普通变量包装成 Compose 可观察的“状态”。一旦它改变，界面上打勾/划线会自动刷新！


    // 💡 知识点 2：Kotlin 的 "by" 关键字（委托）
    // 加上 by 之后，你操作 isCompleted 就像操作普通布尔值一样直接（例如 isCompleted = true），
    // 如果不加 by，你每次都得写 isCompleted.value = true，会很啰嗦。
    var isCompleted by mutableStateOf(initialCompleted)
}