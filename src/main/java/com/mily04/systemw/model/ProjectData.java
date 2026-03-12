package com.mily04.systemw.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ProjectData {
    @Id
    private String id = "main_project";

    @Column(columnDefinition = "TEXT")
    private String courseInfo;   // 课程基本信息

    @Column(columnDefinition = "TEXT")
    private String objectives;   // 课程目标

    @Column(columnDefinition = "TEXT")
    private String assessments;  // 考核方式

    @Column(columnDefinition = "TEXT")
    private String mappings;     // 权重与分值映射

    @Column(columnDefinition = "TEXT")
    private String students;     // 学生名单与成绩

    @Column(columnDefinition = "TEXT")
    private String surveyItems;  // 间接评价（调查问卷）数据
}