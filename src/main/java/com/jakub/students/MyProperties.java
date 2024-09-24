package com.jakub.students;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;


@Validated
public class MyProperties {
    @NotNull
    private String mark;
    @NotNull
    private String model;

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
