package com.leyou.item.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "tb_category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//类目id

    private String name;//类目名称

    private Long parentId;//父类目id,顶级类目填0

    private Boolean isParent;//是否为父节点，0为否，1为是

    private Integer sort; //排序指数，越小越靠前
}
