package com.tencent.backstage.modules.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.backstage.common.base.BaseServiceImpl;
import com.tencent.backstage.common.exception.BadRequestException;
import com.tencent.backstage.common.utils.Constants;
import com.tencent.backstage.common.utils.StringUtils;
import com.tencent.backstage.common.utils.ValidationUtil;
import com.tencent.backstage.modules.models.dto.DeptDto;
import com.tencent.backstage.modules.models.mapper.DeptDtoAndEntityMapper;
import com.tencent.backstage.modules.system.dao.DeptDao;
import com.tencent.backstage.modules.system.entity.Dept;
import com.tencent.backstage.modules.system.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/4/24
 * Time:17:52
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DeptServiceImpl extends BaseServiceImpl<DeptDao,Dept> implements DeptService {

    @Autowired
    private DeptDao deptDao;

    @Autowired
    private DeptDtoAndEntityMapper deptDtoAndEntityMapper;

    @Override
    public DeptDto findById(String uuid) {
        Optional<Dept> dept = Optional.ofNullable(deptDao.selectById(uuid));
        ValidationUtil.isNull(dept,"Dept","uuid",uuid);
        return deptDtoAndEntityMapper.toDto(dept.get());
    }

    @Override
    public List<Dept> findByPid(String pid) {
        return deptDao.findByPid(pid);
    }

    @Override
    public Object buildTree(List<DeptDto> deptDTOS) {
        Set<DeptDto> trees = new LinkedHashSet<>();
        Set<DeptDto> depts= new LinkedHashSet<>();
        Boolean isChild;
        for (DeptDto deptDTO : deptDTOS) {
            isChild = false;
            if (Constants.Dept.rootid.equals(deptDTO.getPid().toString())) {
                trees.add(deptDTO);
            }
            for (DeptDto it : deptDTOS) {
                if (it.getPid().equals(deptDTO.getUuid())) {
                    isChild = true;
                    if (deptDTO.getChildren() == null) {
                        deptDTO.setChildren(new ArrayList<DeptDto>());
                    }
                    deptDTO.getChildren().add(it);
                }
            }
            if(isChild) {
                depts.add(deptDTO);
            }
        }

        if (CollectionUtils.isEmpty(trees)) {
            trees = depts;
        }

        Integer totalElements = deptDTOS!=null?deptDTOS.size():0;

        Map map = new HashMap();
        map.put("totalElements",totalElements);
        map.put("content", CollectionUtils.isEmpty(trees)?deptDTOS:trees);
        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeptDto create(Dept resources) {
        if(super.save(resources)){
            return deptDtoAndEntityMapper.toDto(resources);
        }else{
            throw new BadRequestException(Constants.Save.ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Dept resources) {
        if(resources.getUuid().equals(resources.getPid())) {
            throw new BadRequestException("上级不能为自己!");
        }
        Optional<Dept> optionalDept = Optional.ofNullable(deptDao.selectById(resources.getUuid()));
        ValidationUtil.isNull( optionalDept,"Dept","uuid",resources.getUuid());

        Dept dept = optionalDept.get();
        // 此处需自己修改
        resources.setUuid(dept.getUuid());
        super.updateById(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String uuid) {
        deptDao.deleteById(uuid);
    }

    @Override
    public List<DeptDto> queryAll(DeptDto dept, Set<String> deptIds) {
        dept.setDeptids(deptIds);
        List<Dept> depts =  deptDao.findAll(Wrappers.<Dept>lambdaQuery()
                .and(obj->obj.eq(dept.getEnabled()!= null,Dept::getEnabled,dept.getEnabled())
                            .eq(StringUtils.isNotBlank(dept.getPid()),Dept::getPid,dept.getPid())
                            .eq(Dept::getDelFlag,Constants.NODELETE)
                            .like(StringUtils.isNotBlank(dept.getName()),Dept::getName,dept.getName())
                            .in(dept.getDeptids()!= null && !dept.getDeptids().isEmpty(),Dept::getUuid,dept.getDeptids())
                            ).orderByDesc(Dept::getCreateTime));
        return deptDtoAndEntityMapper.toDto(depts);
    }
}
