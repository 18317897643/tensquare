package com.tensquare.qa.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ViewInfo implements Serializable {
    private Label label;
    private Problem problem;

    public ViewInfo() {
    }

    public ViewInfo(Label label, Problem problem) {
        this.label = label;
        this.problem = problem;
    }

    public ViewInfo(Problem problem) {
        this.problem = problem;
    }

    public ViewInfo(Label label) {
        this.label = label;
    }
}
