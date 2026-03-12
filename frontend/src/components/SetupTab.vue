<script setup lang="ts">
import { Plus, Trash2 } from 'lucide-vue-next'

// 直接使用 defineProps，不需要赋值给 const props
defineProps<{
  courseInfo: {
    courseName: string;
    term: string;
    targetStudents: string;
    teacher: string;
  },
  assessments: any[]
}>()

// 定义发送的事件
defineEmits(['update:courseInfo', 'add-asm', 'remove-asm'])
</script>

<template>
  <div class="space-y-8">
    <section class="bg-white p-6 rounded-xl shadow-sm border border-slate-200">
      <h2 class="text-xl font-semibold text-slate-800 mb-4">课程基本情况</h2>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label class="block text-sm font-medium text-slate-700 mb-1">课程名称</label>
          <input
              type="text"
              :value="courseInfo.courseName"
              @input="$emit('update:courseInfo', { ...courseInfo, courseName: ($event.target as HTMLInputElement).value })"
              class="w-full px-3 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-slate-700 mb-1">任课教师</label>
          <input
              type="text"
              :value="courseInfo.teacher"
              @input="$emit('update:courseInfo', { ...courseInfo, teacher: ($event.target as HTMLInputElement).value })"
              class="w-full px-3 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none"
          />
        </div>
      </div>
    </section>

    <section class="bg-white p-6 rounded-xl shadow-sm border border-slate-200">
      <div class="flex justify-between items-center mb-4">
        <h2 class="text-xl font-semibold text-slate-800">考核方式设置</h2>
        <button @click="$emit('add-asm')" class="flex items-center px-3 py-1.5 bg-indigo-50 text-indigo-600 rounded-lg hover:bg-indigo-100 text-sm font-medium transition-colors">
          <Plus class="w-4 h-4 mr-1" /> 添加考核方式
        </button>
      </div>
      <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-3">
        <div v-for="asm in assessments" :key="asm.id" class="flex items-center gap-2 p-3 bg-slate-50 rounded-lg border border-slate-100">
          <input v-model="asm.name" type="text" class="flex-1 px-3 py-1.5 border border-slate-300 rounded-lg bg-white text-sm" />
          <button @click="$emit('remove-asm', asm.id)" class="p-1.5 text-slate-400 hover:text-red-500 transition-colors">
            <Trash2 class="w-4 h-4" />
          </button>
        </div>
      </div>
    </section>
  </div>
</template>