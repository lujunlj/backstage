package com.tencent.backstage.modules.models.mapper;

import com.tencent.backstage.common.mapper.EntityMapper;
import com.tencent.backstage.modules.models.dto.LogDto;
import com.tencent.backstage.modules.system.entity.Log;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;


/**
 * Created with IDEA
 * author: lujun
 * Date:2019/4/26
 * Time:22:30
 */
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LogDtoAndEntityMapper extends EntityMapper<LogDto, Log> {
    @Mappings({
            @Mapping(source = "uuid", target = "id")
    })
    LogDto toDto(Log entity);
}
