<script setup lang="ts">
import { computed } from 'vue'
import { Plus, Trash2 } from 'lucide-vue-next'

const props = defineProps<{
  objectives: any[],
  assessments: any[],
  mappings: any[],
  students: any[]
}>()

const emit = defineEmits(['update:students'])

// 只显示权重 > 0 的有效考核项
const activeMappings = computed(() => {
  return props.mappings.filter(m => m.weight > 0)
})

const addStudent = () => {
  const newStudent = {
    id: 's' + Date.now(),
    studentId: '',
    name: '',
    scores: {}
  }
  emit('update:students', [...props.students, newStudent])
}

const removeStudent = (id: string) => {
  emit('update:students', props.students.filter(s => s.id !== id))
}
</script>

<template>
  <div class="bg-white p-6 rounded-xl shadow-sm border border-slate-200 overflow-x-auto">
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-xl font-semibold text-slate-800">3. 学生成绩录入</h2>
      <button @click="addStudent" class="flex items-center px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition-colors text-sm font-medium">
        <Plus class="w-4 h-4 mr-2" /> 添加学生
      </button>
    </div>

    <table class="w-full text-sm text-center border-collapse">
      <thead>
      <tr class="bg-slate-50 border border-slate-200">
        <th class="p-3 border border-slate-200 w-16">序号</th>
        <th class="p-3 border border-slate-200 w-32">学号</th>
        <th class="p-3 border border-slate-200 w-24">姓名</th>
        <th v-for="m in activeMappings" :key="m.objectiveId + m.assessmentId" class="p-3 border border-slate-200 bg-indigo-50/50">
          <div class="font-bold text-indigo-900">{{ objectives.find(o => o.id === m.objectiveId)?.name }}</div>
          <div class="text-xs text-indigo-600 mt-1">{{ assessments.find(a => a.id === m.assessmentId)?.name }}</div>
          <div class="text-xs text-slate-400 font-normal">(满分: {{ m.targetScore }})</div>
        </th>
        <th class="p-3 border border-slate-200 w-16">操作</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(student, index) in students" :key="student.id" class="hover:bg-slate-50 transition-colors">
        <td class="p-2 border border-slate-200 text-slate-500">{{ index + 1 }}</td>
        <td class="p-2 border border-slate-200">
          <input v-model="student.studentId" class="w-full p-1.5 border border-slate-300 rounded text-center outline-none focus:border-indigo-500" placeholder="如: 2201" />
        </td>
        <td class="p-2 border border-slate-200">
          <input v-model="student.name" class="w-full p-1.5 border border-slate-300 rounded text-center outline-none focus:border-indigo-500" placeholder="姓名" />
        </td>
        <td v-for="m in activeMappings" :key="m.objectiveId + m.assessmentId" class="p-2 border border-slate-200">
          <input
              type="number" min="0" :max="m.targetScore"
              v-model="student.scores[`${m.objectiveId}_${m.assessmentId}`]"
              class="w-16 p-1.5 border border-slate-300 rounded text-center outline-none focus:border-indigo-500"
          />
        </td>
        <td class="p-2 border border-slate-200">
          <button @click="removeStudent(student.id)" class="text-slate-400 hover:text-red-600 hover:bg-red-50 p-2 rounded transition-colors">
            <Trash2 class="w-4 h-4" />
          </button>
        </td>
      </tr>
      <tr v-if="students.length === 0">
        <td :colspan="activeMappings.length + 4" class="p-8 text-slate-400 text-center">暂无学生数据，请点击上方按钮添加</td>
      </tr>
      </tbody>
    </table>
  </div>
</template>