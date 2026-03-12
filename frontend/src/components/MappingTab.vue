<script setup lang="ts">
const props = defineProps<{
  objectives: any[],
  assessments: any[],
  mappings: any[]
}>()

// 获取当前格子对应的映射数据
const getMapping = (objId: string, asmId: string) => {
  return props.mappings.find(m => m.objectiveId === objId && m.assessmentId === asmId) || { weight: 0, targetScore: 0 }
}
</script>

<template>
  <div class="bg-white p-6 rounded-xl shadow-sm border border-slate-200 overflow-x-auto">
    <h2 class="text-xl font-semibold text-slate-800 mb-6">2. 直接评价权重分配</h2>

    <table class="w-full text-sm text-left border-collapse">
      <thead>
      <tr class="bg-slate-50 border-b border-slate-200">
        <th class="py-3 px-4 font-semibold text-slate-700 border-r w-1/3">课程目标 \ 考核方式</th>
        <th v-for="asm in assessments" :key="asm.id" class="py-3 px-4 font-semibold text-slate-700 text-center border-r last:border-0">
          {{ asm.name }}
        </th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="obj in objectives" :key="obj.id" class="border-b border-slate-200 last:border-0 hover:bg-slate-50/50">
        <td class="py-4 px-4 font-medium text-slate-800 border-r">{{ obj.name }}</td>

        <td v-for="asm in assessments" :key="asm.id" class="py-4 px-4 border-r last:border-0">
          <div class="flex flex-col gap-3 items-center">
            <div class="flex items-center justify-between w-24">
              <span class="text-xs text-slate-500 font-medium">权重:</span>
              <input
                  type="number" step="0.01" min="0" max="1"
                  v-model="getMapping(obj.id, asm.id).weight"
                  class="w-14 px-1 py-1 border border-slate-300 rounded text-center focus:ring-1 focus:ring-indigo-500 outline-none"
              />
            </div>
            <div class="flex items-center justify-between w-24">
              <span class="text-xs text-slate-500 font-medium">满分:</span>
              <input
                  type="number" min="0"
                  v-model="getMapping(obj.id, asm.id).targetScore"
                  class="w-14 px-1 py-1 border border-slate-300 rounded text-center focus:ring-1 focus:ring-indigo-500 outline-none"
              />
            </div>
          </div>
        </td>
      </tr>
      </tbody>
    </table>
    <p class="text-xs text-slate-400 mt-4">* 提示：权重为 0 的项目不会参与最终达成度计算。</p >
  </div>
</template>