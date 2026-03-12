package com.mily04.systemw.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class CourseInfo {
    @Id
    private String id = "default";
    private String courseName;
    private String term;
    private String targetStudents;
    private String teacher;

    // 为了简单，我们手动写一下构造函数和 Getter/Setter
    public CourseInfo() {}

    public CourseInfo(String id, String courseName, String term, String targetStudents, String teacher) {
        this.id = id;
        this.courseName = courseName;
        this.term = term;
        this.targetStudents = targetStudents;
        this.teacher = teacher;
    }

    // Getter 和 Setter (右键选择 Generate -> Getter and Setter 可以自动生成)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public String getTerm() { return term; }
    public void setTerm(String term) { this.term = term; }
    public String getTargetStudents() { return targetStudents; }
    public void setTargetStudents(String targetStudents) { this.targetStudents = targetStudents; }
    public String getTeacher() { return teacher; }
    public void setTeacher(String teacher) { this.teacher = teacher; }
}