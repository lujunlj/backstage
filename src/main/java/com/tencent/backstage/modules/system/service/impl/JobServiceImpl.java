package com.tencent.backstage.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.backstage.common.base.BaseServiceImpl;
import com.tencent.backstage.common.exception.BadRequestException;
import com.tencent.backstage.common.utils.Constants;
import com.tencent.backstage.common.utils.PageUtil;
import com.tencent.backstage.common.utils.ValidationUtil;
import com.tencent.backstage.modules.models.dto.JobDto;
import com.tencent.backstage.modules.models.mapper.JobDtoAndEntityMapper;
import com.tencent.backstage.modules.system.dao.JobDao;
import com.tencent.backstage.modules.system.entity.Job;
import com.tencent.backstage.modules.system.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/4/24
 * Time:17:52
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class JobServiceImpl extends BaseServiceImpl<JobDao,Job> implements JobService {

    @Autowired
    private JobDao jobDao;

    @Autowired
    private JobDtoAndEntityMapper jobDtoAndEntityMapper;

    @Override
    public JobDto findById(String uuid) {
        Optional<Job> job = Optional.ofNullable(jobDao.selectById(uuid));
        ValidationUtil.isNull(job,"Job","uuid",uuid);
        return jobDtoAndEntityMapper.toDto(job.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JobDto create(Job resources) {
        if(super.save(resources)){
            return jobDtoAndEntityMapper.toDto(resources);
        }else{
            throw new BadRequestException(Constants.Save.ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Job resources) {
        Optional<Job> optionalJob =  Optional.ofNullable(super.getById(resources.getUuid()));
        ValidationUtil.isNull( optionalJob,"Job","uuid",resources.getUuid());

        Job job = optionalJob.get();
        // 此处需自己修改
        resources.setUuid(job.getUuid());
        super.updateById(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String uuid) {
        jobDao.deleteById(uuid);
    }

    @Override
    public Object queryAll(String name, Boolean enabled, Set<String> deptIds, String deptId, Page<Job> pageInfo) {
        Map<String ,Object> map = new HashMap<String,Object>();
        map.put("name",name);
        map.put("enabled",enabled);
        map.put("deptIds",deptIds);
        map.put("deptId",deptId);
        pageInfo = super.findWithPageByMap(pageInfo,map);
        return PageUtil.toPage(jobDtoAndEntityMapper.toDto(pageInfo.getRecords()),pageInfo.getRecords().size());
    }
}
