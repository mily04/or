<template>
  <div class="min-h-screen bg-slate-50 font-sans text-slate-900">
    <header class="bg-white border-b sticky top-0 z-20 print:hidden">
      <div class="max-w-[1600px] mx-auto px-6 h-16 flex justify-between items-center">
        <div class="flex items-center gap-3">
          <div class="w-8 h-8 bg-indigo-600 rounded flex items-center justify-center">
            <FileBarChart class="text-white w-5 h-5" />
          </div>
          <span class="text-xl font-bold tracking-tight">
            成绩分析系统 <small class="text-slate-400 font-normal ml-2">v2.0 (JDK17 + Vue3)</small>
          </span>
        </div>
        <button
            @click="handleSave"
            class="flex items-center gap-2 bg-indigo-600 text-white px-4 py-2 rounded-lg hover:bg-indigo-700 transition-all shadow-md active:scale-95"
        >
          <Save class="w-4 h-4" /> 保存并同步到本地数据库
        </button>
      </div>
    </header>

    <main class="max-w-[1600px] mx-auto p-6">
      <div :class="['flex flex-col xl:flex-row gap-8', isReversedLayout ? 'xl:flex-row-reverse' : '']">

        <div class="flex-1 min-w-0">
          <div class="flex space-x-1 bg-slate-200/50 p-1 rounded-xl mb-6 w-fit print:hidden">
            <button
                v-for="(label, tab) in tabs"
                :key="tab"
                @click="activeTab = tab"
                :class="[
                'px-4 py-2 rounded-lg text-sm font-medium transition-all',
                activeTab === tab ? 'bg-white text-indigo-600 shadow-sm' : 'text-slate-600 hover:bg-slate-200'
              ]"
            >
              {{ label }}
            </button>
          </div>

          <div class="transition-all duration-300">
            <SetupTab
                v-if="activeTab === 'setup'"
                v-model:courseInfo="courseInfo"
                :assessments="assessments"
                @add-asm="addAssessment"
            />

            <MappingTab
                v-if="activeTab === 'mapping'"
                :objectives="objectives"
                :assessments="assessments"
                :mappings="mappings"
            />

            <DataEntryTab
                v-if="activeTab === 'data'"
                :objectives="objectives"
                :assessments="assessments"
                :mappings="mappings"
                v-model:students="students"
            />

            <IndirectEvalTab
                v-if="activeTab === 'indirect'"
                :objectives="objectives"
                v-model:surveyItems="surveyItems"
            />
          </div>
        </div>

        <div class="hidden xl:flex items-center print:hidden">
          <button
              @click="isReversedLayout = !isReversedLayout"
              class="p-2 bg-white border border-slate-200 rounded-full hover:shadow-lg transition-all text-slate-400 hover:text-indigo-600"
              title="切换左右布局"
          >
            <ArrowLeftRight class="w-5 h-5" />
          </button>
        </div>

        <div class="flex-1 min-w-0">
          <div class="bg-white rounded-xl shadow-sm border border-slate-200 p-6 sticky top-24">
            <h2 class="text-lg font-bold text-slate-800 mb-6 flex items-center border-b pb-4">
              <ClipboardCheck class="w-5 h-5 mr-2 text-indigo-600" />
              达成度报告实时预览
            </h2>
            <ReportPreview
                :courseInfo="courseInfo"
                :objectives="objectives"
                :assessments="assessments"
                :mappings="mappings"
                :students="students"
                :surveyItems="surveyItems"
            />
          </div>
        </div>

      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import {
  FileBarChart, ArrowLeftRight, Save, ClipboardCheck
} from 'lucide-vue-next'

// 导入 API 模块
import { courseApi } from './api'

// 导入子组件
import SetupTab from './components/SetupTab.vue'
import MappingTab from './components/MappingTab.vue'
import DataEntryTab from './components/DataEntryTab.vue'
import IndirectEvalTab from './components/IndirectEvalTab.vue'
import ReportPreview from './components/ReportPreview.vue'

// --- 状态与数据定义 ---
const activeTab = ref('setup')
const isReversedLayout = ref(true)
const tabs = {
  setup: '1. 基础设置',
  mapping: '2. 权重分配',
  data: '3. 成绩录入',
  indirect: '4. 问卷调查'
}

// 课程基本信息 (对应 CourseInfo)
const courseInfo = ref({
  courseName: '单片机原理及接口技术',
  term: '2024 年（秋）',
  targetStudents: '自动化 221-225',
  teacher: '郭栋'
})

// 课程目标 (对应 Objective)
const objectives = ref([
  { id: 'obj1', name: 'Obj 1', description: '能够运用单片机及接口知识...' },
  { id: 'obj2', name: 'Obj 2', description: '能够进行单片机系统常用接口设计...' }
])

// 考核方式 (对应 Assessment)
const assessments = ref([
  { id: 'a1', name: '期末考试' },
  { id: 'a2', name: '平时成绩' }
])

// 权重映射 (对应 Mapping)
const mappings = ref([
  { objectiveId: 'obj1', assessmentId: 'a1', weight: 0.9, targetScore: 100 },
  { objectiveId: 'obj1', assessmentId: 'a2', weight: 0.1, targetScore: 100 }
])

// 学生数据 (对应 Student)
const students = ref([])

// 问卷调查数据 (对应 SurveyItem)
const surveyItems = ref([])

// --- 生命周期：初始化加载 ---
onMounted(async () => {
  try {
    const res = await courseApi.getAllData()
    if (res.data && res.data.courseInfo) {
      // 解析后端存入的文本字段
      courseInfo.value = JSON.parse(res.data.courseInfo)
      objectives.value = JSON.parse(res.data.objectives)
      assessments.value = JSON.parse(res.data.assessments)
      mappings.value = JSON.parse(res.data.mappings)
      students.value = JSON.parse(res.data.students)
      surveyItems.value = JSON.parse(res.data.surveyItems || '[]')
    }
  } catch (error) {
    console.warn("未发现历史数据，已载入默认模板")
  }
})

// --- 业务逻辑函数 ---

// 保存并同步到后端
const handleSave = async () => {
  const payload = {
    courseInfo: JSON.stringify(courseInfo.value),
    objectives: JSON.stringify(objectives.value),
    assessments: JSON.stringify(assessments.value),
    mappings: JSON.stringify(mappings.value),
    students: JSON.stringify(students.value),
    surveyItems: JSON.stringify(surveyItems.value)
  }
  try {
    await courseApi.saveAllData(payload)
    alert("✅ 数据已同步到本地 SQLite 数据库！")
  } catch (error) {
    alert("❌ 保存失败，请检查后端程序是否正在运行。")
  }
}

// 动态添加考核方式
const addAssessment = () => {
  const newId = 'a' + Date.now()
  assessments.value.push({ id: newId, name: '新考核方式' })
  // 为所有课程目标初始化该考核方式的映射
  objectives.value.forEach(obj => {
    mappings.value.push({
      objectiveId: obj.id,
      assessmentId: newId,
      weight: 0,
      targetScore: 0
    })
  })
}
</script>

<style>
/* 针对报表导出的简单打印样式 */
@media print {
  .print\:hidden {
    display: none !important;
  }
}
</style>