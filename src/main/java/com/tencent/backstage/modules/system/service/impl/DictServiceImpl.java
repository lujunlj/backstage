package com.tencent.backstage.modules.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.backstage.common.base.BaseServiceImpl;
import com.tencent.backstage.common.exception.BadRequestException;
import com.tencent.backstage.common.utils.Constants;
import com.tencent.backstage.common.utils.PageUtil;
import com.tencent.backstage.common.utils.StringUtils;
import com.tencent.backstage.common.utils.ValidationUtil;
import com.tencent.backstage.modules.models.dto.DictDto;
import com.tencent.backstage.modules.models.mapper.DictDtoAndEntityMapper;
import com.tencent.backstage.modules.system.dao.DictDao;
import com.tencent.backstage.modules.system.entity.Dict;
import com.tencent.backstage.modules.system.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/4/24
 * Time:17:52
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DictServiceImpl extends BaseServiceImpl<DictDao,Dict> implements DictService {

    @Autowired
    private DictDao dictDao;

    @Autowired
    private DictDtoAndEntityMapper dictDtoAndEntityMapper;

    @Override
    public DictDto findById(String uuid) {
        Optional<Dict> dict = Optional.ofNullable(dictDao.selectById(uuid));
        ValidationUtil.isNull(dict,"Dict","uuid",uuid);
        return dictDtoAndEntityMapper.toDto(dict.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DictDto create(Dict resources) {
        if(super.save(resources)){
            return dictDtoAndEntityMapper.toDto(resources);
        }else{
            throw new BadRequestException(Constants.Save.ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Dict resources) {
        Optional<Dict> optionalDict = Optional.ofNullable(dictDao.selectById(resources.getUuid()));
        ValidationUtil.isNull( optionalDict,"Dict","uuid",resources.getUuid());
        Dict dict = optionalDict.get();
        // 此处需自己修改
        resources.setUuid(dict.getUuid());
        super.updateById(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String uuid) {
        dictDao.deleteById(uuid);
    }

    @Override
    public Object queryAll(DictDto dictDto, Page<Dict> pageInfo){
        pageInfo = super.findWithPage(pageInfo, Wrappers.<Dict>lambdaQuery()
                .like(StringUtils.isNotBlank(dictDto.getName()),Dict::getName,dictDto.getName())
                .like(StringUtils.isNotBlank(dictDto.getRemark()),Dict::getRemark,dictDto.getRemark())
                .orderByDesc(Dict::getCreateTime));
        return PageUtil.toPage(pageInfo);
    }
}
