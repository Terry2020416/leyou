package com.leyou.item.service;

import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Transient;
import java.util.List;

@Service
public class SpecService {

    @Autowired(required = false)
    private SpecGroupMapper specGroupMapper;

    @Autowired(required = false)
    private SpecParamMapper specParamMapper;

    public List<SpecGroup> querySpecGroups(Long cid) {

        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        //查询规格组表
        List<SpecGroup> specGroupList =  specGroupMapper.select(specGroup);
        //System.out.println("specGroupList=" + specGroupList);
        //为以后 查询规格参数封装到规格组
        specGroupList.forEach(record->{
            //select * from tb_spec_param where group_id = #{id}
            SpecParam specParam = new SpecParam();
            specParam.setGroupId(record.getId());
            List<SpecParam> specParams = this.specParamMapper.select(specParam);
            //根据规格组的id查询多条数据(List)，然后封装到规格组里面
            record.setSpecParams(specParams);
        });
        return  specGroupList;
    }

    public List<SpecParam> querySpecParam(Long gid,Long cid,Boolean searching,Boolean generic) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setSearching(searching);
        specParam.setGeneric(generic);

        //select * from tb_spec_param where group_id = ?
        List<SpecParam> specParamList = this.specParamMapper.select(specParam);
        return specParamList;
    }

    @Transactional
    public void insertSpecGroup(SpecGroup specGroup) {
//        SpecGroup specGroup = new SpecGroup();
//        specGroup.setCid(cid);
//        specGroup.setName(name);
        this.specGroupMapper.insertSelective(specGroup);
    }

    public void updateSpecGroup(SpecGroup specGroup) {
        this.specGroupMapper.updateByPrimaryKey(specGroup);
    }

    public void deleteSpecGroup(Long id) {
        this.specGroupMapper.deleteByPrimaryKey(id);
    }

    public void insertSpecParam(SpecParam specParam) {

        this.specParamMapper.insertSelective(specParam);
    }

    public void updateSpecParam(SpecParam specParam) {
        this.specParamMapper.updateByPrimaryKey(specParam);
    }

    public void deleteSpecParam(Long id) {
        this.specParamMapper.deleteByPrimaryKey(id);
    }
}
