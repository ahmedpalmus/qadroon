package com.example.qadroon;

import java.io.Serializable;

public class Memo implements Serializable {
    private String  memo_id, username,salary,fullname,qualification, title, accepted, notes, details,status, memo_date, add_date,image, memo_type;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getAccepted() {
        return accepted;
    }

    public void setAccepted(String accepted) {
        this.accepted = accepted;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMemo_id() {
        return memo_id;
    }

    public void setMemo_id(String memo_id) {
        this.memo_id = memo_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getMemo_date() {
        return memo_date;
    }

    public void setMemo_date(String memo_date) {
        this.memo_date = memo_date;
    }

    public String getAdd_date() {
        return add_date;
    }

    public void setAdd_date(String add_date) {
        this.add_date = add_date;
    }

    public String getMemo_type() {
        return memo_type;
    }

    public void setMemo_type(String memo_type) {
        this.memo_type = memo_type;
    }
}
