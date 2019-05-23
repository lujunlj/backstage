package com.tencent.backstage.common.mapper;

import java.util.List;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/4/26
 * Time:21:50
 */
public interface VoMapper<Vo,Dto> {
    Dto toDto(Vo vo);

    Vo toVo(Dto dto);

    List <Dto> toDto(List<Vo> voList);

    List <Vo> toVo(List<Dto> dtoList);
}
