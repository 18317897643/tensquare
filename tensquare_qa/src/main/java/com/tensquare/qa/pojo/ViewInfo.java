package com.tensquare.qa.pojo;

import lombok.Data;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "v_p_l")
public class ViewInfo implements Serializable {
    @Id
    private String id;
    @Embedded
    private Label label;
    @Embedded
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
