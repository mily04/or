<script setup lang="ts">
import { Plus, Trash2 } from 'lucide-vue-next'

const props = defineProps<{
  objectives: any[],
  surveyItems: any[]
}>()

const emit = defineEmits(['update:surveyItems'])

const addSurveyItem = () => {
  const newItem = {
    id: 'item' + Date.now(),
    objectiveId: props.objectives[0]?.id || '',
    description: '',
    weight: 0.5,
    percentages: { excellent: 0, good: 0, medium: 0, pass: 0 }
  }
  emit('update:surveyItems', [...props.surveyItems, newItem])
}

const removeItem = (id: string) => {
  emit('update:surveyItems', props.surveyItems.filter(i => i.id !== id))
}
</script>

<template>
  <div class="bg-white p-6 rounded-xl shadow-sm border border-slate-200">
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-xl font-semibold text-slate-800">4. 间接评价（问卷调查）</h2>
      <button @click="addSurveyItem" class="flex items-center px-3 py-1.5 bg-indigo-50 text-indigo-600 rounded-lg hover:bg-indigo-100 text-sm font-medium">
        <Plus class="w-4 h-4 mr-1" /> 添加调查项
      </button>
    </div>

    <div class="space-y-4">
      <div v-for="item in surveyItems" :key="item.id" class="p-4 bg-slate-50 rounded-lg border border-slate-100 space-y-3">
        <div class="flex gap-4">
          <select v-model="item.objectiveId" class="px-2 py-1 border rounded bg-white text-sm">
            <option v-for="obj in objectives" :key="obj.id" :value="obj.id">{{ obj.name }}</option>
          </select>
          <input v-model="item.description" placeholder="调查项目描述..." class="flex-1 px-3 py-1 border rounded text-sm" />
          <button @click="removeItem(item.id)" class="text-slate-400 hover:text-red-500"><Trash2 class="w-4 h-4" /></button>
        </div>
        <div class="grid grid-cols-4 gap-4">
          <div v-for="key in ['excellent', 'good', 'medium', 'pass']" :key="key">
            <label class="block text-xs text-slate-500 mb-1">
              {{ key === 'excellent' ? '优 (%)' : key === 'good' ? '良 (%)' : key === 'medium' ? '中 (%)' : '及格 (%)' }}
            </label>
            <input type="number" v-model="item.percentages[key]" class="w-full px-2 py-1 border rounded text-sm text-center" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>