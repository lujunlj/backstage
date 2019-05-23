package com.tencent.backstage.modules.models.mapper;


import com.tencent.backstage.common.mapper.EntityMapper;
import com.tencent.backstage.modules.models.dto.UserDto;
import com.tencent.backstage.modules.system.entity.User;
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
public interface UserDtoAndEntityMapper extends EntityMapper<UserDto, User> {

    @Mappings({
            @Mapping(source = "uuid", target = "id")
    })
    UserDto toDto(User entity);
}
