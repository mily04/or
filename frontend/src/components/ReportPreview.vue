<script setup lang="ts">
import { computed } from 'vue'
import { Download } from 'lucide-vue-next'
import html2pdf from 'html2pdf.js'

const props = defineProps<{
  courseInfo: any,
  objectives: any[],
  assessments: any[],
  mappings: any[],
  students: any[],
  surveyItems: any[]
}>()

// 1. 计算直接评价达成度
const directAnalysis = computed(() => {
  const objAad: Record<string, number> = {}

  props.objectives.forEach(obj => {
    let totalWeightedAad = 0
    let totalWeight = 0

    const objMappings = props.mappings.filter(m => m.objectiveId === obj.id && m.weight > 0)
    objMappings.forEach(m => {
      const key = `${obj.id}_${m.assessmentId}`
      const scores = props.students.map(s => Number(s.scores[key] || 0))
      const avg = scores.length > 0 ? scores.reduce((a, b) => a + b, 0) / scores.length : 0
      const aad = m.targetScore > 0 ? avg / m.targetScore : 0

      totalWeightedAad += aad * m.weight
      totalWeight += m.weight
    })
    objAad[obj.id] = totalWeight > 0 ? totalWeightedAad / totalWeight : 0
  })
  return objAad
})

// 2. 计算间接评价达成度
const indirectAnalysis = computed(() => {
  const objResults: Record<string, number> = {}
  props.objectives.forEach(obj => {
    const items = props.surveyItems.filter(i => i.objectiveId === obj.id)
    if (items.length === 0) {
      objResults[obj.id] = 0
      return
    }
    const totalAad = items.reduce((sum, item) => {
      const itemAad = (Number(item.percentages.excellent || 0) + Number(item.percentages.good || 0) +
          Number(item.percentages.medium || 0) + Number(item.percentages.pass || 0)) / 100
      return sum + (itemAad * item.weight)
    }, 0)
    const totalWeight = items.reduce((sum, item) => sum + item.weight, 0)
    objResults[obj.id] = totalWeight > 0 ? totalAad / totalWeight : 0
  })
  return objResults
})

// 3. 计算总达成度 (修复 TS2532 报错：增加 || 0 兜底)
const finalAchievement = computed(() => {
  const results: Record<string, number> = {}
  props.objectives.forEach(obj => {
    const direct = directAnalysis.value[obj.id] || 0;
    const indirect = indirectAnalysis.value[obj.id] || 0;
    results[obj.id] = (direct * 0.9) + (indirect * 0.1)
  })
  return results
})

// 修复 TS2345 报错：增加元素是否为 null 的检查
const exportPDF = () => {
  const element = document.getElementById('report-content')
  if (!element) {
    alert("无法生成报表：未找到页面元素！")
    return
  }

  html2pdf().from(element).set({
    margin: 10,
    filename: `${props.courseInfo.courseName || '课程'}_达成度报告.pdf`,
    html2canvas: { scale: 2 },
    jsPDF: { unit: 'mm', format: 'a4', orientation: 'portrait' }
  }).save()
}
</script>

<template>
  <div class="space-y-6">
    <div class="flex justify-between items-center print:hidden">
      <h3 class="font-bold text-slate-700">预览区域</h3>
      <button @click="exportPDF" class="flex items-center px-3 py-1.5 bg-indigo-600 text-white rounded-lg text-sm transition-colors hover:bg-indigo-700">
        <Download class="w-4 h-4 mr-2" /> 导出 PDF
      </button>
    </div>

    <div id="report-content" class="bg-white p-6 border text-black min-h-[500px]">
      <div class="text-center mb-8">
        <h1 class="text-xl font-bold">《{{ courseInfo.courseName }}》</h1>
        <h2 class="text-lg font-bold">课程目标达成情况评价报告</h2>
      </div>

      <table class="w-full border-collapse border border-black text-sm mb-6">
        <thead>
        <tr class="bg-slate-50">
          <th class="border border-black p-2">课程目标</th>
          <th class="border border-black p-2">直接评价 (90%)</th>
          <th class="border border-black p-2">间接评价 (10%)</th>
          <th class="border border-black p-2">总达成度</th>
          <th class="border border-black p-2">结论 (0.65)</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="obj in objectives" :key="obj.id" class="text-center">
          <td class="border border-black p-2 font-medium">{{ obj.name }}</td>
          <td class="border border-black p-2">{{ (directAnalysis[obj.id] || 0).toFixed(2) }}</td>
          <td class="border border-black p-2">{{ (indirectAnalysis[obj.id] || 0).toFixed(2) }}</td>
          <td class="border border-black p-2 font-bold">{{ (finalAchievement[obj.id] || 0).toFixed(2) }}</td>
          <td class="border border-black p-2">
              <span :class="(finalAchievement[obj.id] || 0) >= 0.65 ? 'text-green-600 font-bold' : 'text-red-600 font-bold'">
                {{ (finalAchievement[obj.id] || 0) >= 0.65 ? '达成' : '未达成' }}
              </span>
          </td>
        </tr>
        </tbody>
      </table>
      <p class="text-xs text-slate-500 italic">* 报告基于本地数据库实时计算生成</p >
    </div>
  </div>
</template>