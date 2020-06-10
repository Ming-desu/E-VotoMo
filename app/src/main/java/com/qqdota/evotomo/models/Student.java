package com.qqdota.evotomo.models;

import com.google.gson.annotations.SerializedName;

public class Student {
    @SerializedName("id")
    private int id;
    @SerializedName("student_number")
    private String studentNumber;
    @SerializedName("password")
    private String password;
    @SerializedName("firstname")
    private String firstName;
    @SerializedName("middlename")
    private String middleName;
    @SerializedName("lastname")
    private String lastName;
    @SerializedName("birthdate")
    private String birthDate;
    @SerializedName("sex")
    private int sex;
    @SerializedName("image")
    private String image;

    public Student(String studentNumber, String password, String firstName, String middleName, String lastName, String birthDate, int sex, String image) {
        this.studentNumber = studentNumber;
        this.password = password;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.sex = sex;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public int getSex() {
        return sex;
    }

    public String getImage() {
        return image;
    }
}
