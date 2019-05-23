package com.tencent.backstage.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.backstage.common.base.BaseServiceImpl;
import com.tencent.backstage.common.exception.BadRequestException;
import com.tencent.backstage.common.utils.Constants;
import com.tencent.backstage.common.utils.ObjectUtil;
import com.tencent.backstage.common.utils.PageUtil;
import com.tencent.backstage.common.utils.ValidationUtil;
import com.tencent.backstage.modules.models.dto.DictDetailDto;
import com.tencent.backstage.modules.models.mapper.DictDetailDtoAndEntityMapper;
import com.tencent.backstage.modules.system.dao.DictDetailDao;
import com.tencent.backstage.modules.system.entity.DictDetail;
import com.tencent.backstage.modules.system.service.DictDetailService;
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
public class DictDetailServiceImpl extends BaseServiceImpl<DictDetailDao, DictDetail> implements DictDetailService {

    @Autowired
    private DictDetailDao dictDetailDao;

    @Autowired
    private DictDetailDtoAndEntityMapper dictDetailDtoAndEntityMapper;

    @Override
    public DictDetailDto findById(String uuid) {
        Optional<DictDetail> dictDetail = Optional.ofNullable(dictDetailDao.selectById(uuid));
        ValidationUtil.isNull(dictDetail,"DictDetail","uuid",uuid);
        return dictDetailDtoAndEntityMapper.toDto(dictDetail.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DictDetailDto create(DictDetail resources) {
        if(super.save(resources)){
            return dictDetailDtoAndEntityMapper.toDto(resources);
        }else{
            throw new BadRequestException(Constants.Save.ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(DictDetail resources) {
        Optional<DictDetail> optionalDictDetail = Optional.ofNullable(dictDetailDao.selectById(resources.getUuid()));
        ValidationUtil.isNull( optionalDictDetail,"DictDetail","id",resources.getUuid());

        DictDetail dictDetail = optionalDictDetail.get();
        // 此处需自己修改
        resources.setUuid(dictDetail.getUuid());
        super.updateById(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String uuid) {
        dictDetailDao.deleteById(uuid);
    }

    @Override
    public Object queryAll(DictDetailDto dictDetailDto, Page<DictDetail> pageInfo){
        pageInfo = super.findWithPageByMap(pageInfo,ObjectUtil.obj2Map(dictDetailDto));
        return PageUtil.toPage(pageInfo);
    }


}
