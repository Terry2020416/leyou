package com.leyou.search.utils;

import java.util.Map;

//接受浏览器传来的数据
public class SearchRequest {

    private String key;//搜索的关键字
    private Integer page;//当前页
    private Map<String,String> filter;
    private static final Integer DEFAULT_PAGE = 1; //默认的第几页
    private static final Integer DEFAULT_SIZE = 20; //默认显示多少条数据

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getPage() {
        if(page == null)
            return DEFAULT_PAGE;
        return Math.max(DEFAULT_PAGE,page);//获取页码是验证，page不能小于1
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize(){
        return DEFAULT_SIZE;
    }

    public Map<String, String> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, String> filter) {
        this.filter = filter;
    }

    /*
        返回结果：作为分页结果，
        一般都两个属性：当前页数据、总条数信息，我们可以使用之前定义的PageResult类
     */
}
