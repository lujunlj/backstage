package com.tencent.backstage.modules.models.mapper;


import com.tencent.backstage.common.mapper.EntityMapper;
import com.tencent.backstage.modules.models.dto.DictDetailDto;
import com.tencent.backstage.modules.system.entity.DictDetail;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/4/26
 * Time:22:30
 */
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DictDetailDtoAndEntityMapper extends EntityMapper<DictDetailDto, DictDetail> {
}
