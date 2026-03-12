import axios from 'axios';

const api = axios.create({
    // 后端 Spring Boot 默认端口
    baseURL: '/api',
});

export const courseApi = {
    // 获取所有项目数据（包含课程、目标、成绩等）
    getAllData: () => api.get('/data'),

    // 保存所有项目数据
    saveAllData: (data: any) => api.post('/data', data),
};

export default api;
