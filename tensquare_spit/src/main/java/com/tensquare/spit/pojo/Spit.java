package com.tensquare.spit.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Spit implements Serializable  {
    private String _id;
    private String content;
    private Date publishtime;
    private String userid;
    private String nickname;
    private Integer visits;
    private Integer thumbup;
    private Integer share;
    private Integer comment;
    private String state;
    private String parentid;
}
