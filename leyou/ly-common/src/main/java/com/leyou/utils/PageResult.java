package com.leyou.utils;

import java.util.List;

public class PageResult<T> {

    private Long total; //总条数

    private Long totalPage;//总页数

    private List<T> items;//当前页面数据

    public PageResult() {
    }

    public PageResult(Long total, List<T> items) {
        this.total = total;
        this.items = items;
    }

    public PageResult(Long total, long totalPage, List<T> items) {
        this.total = total;
        this.totalPage = totalPage;
        this.items = items;
    }

    public Long getTotal() {
        return total;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public List<T> getItems() {
        return items;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "PageResult{" +
                "total=" + total +
                ", totalPage=" + totalPage +
                ", items=" + items +
                '}';
    }
}
