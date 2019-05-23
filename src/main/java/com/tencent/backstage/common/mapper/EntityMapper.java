package com.tencent.backstage.common.mapper;


import java.util.List;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/4/26
 * 常用的接口工具类 : BasicObjectMapper包含了4个基本方法，单个和集合以及反转的单个和集合。开发中如需要对象转换操作可直接新建 interface 并继承 BasicObjectMapper，并在新建的接口上加上 @Mapper(componentModel = "spring")，如果是属性中包含其它类以及该类已经存在 Mapper 则注解中加上 users = {类名.class}。componentModel = "spring" 该配置表示生成的实现类默认加上 spring @Component 注解，使用时可直接通过 @Autowire 进行注入
 * https://blog.csdn.net/jtf8525140/article/details/78130601
 * Time:21:50
 */
public interface EntityMapper<DTO,E> {
    /**
     * DTO转Entity
     * @param dto
     * @return
     */
    E toEntity(DTO dto);

    /**
     * Entity转DTO
     * @param entity
     * @return
     */
    DTO toDto(E entity);

    /**
     * DTO集合转Entity集合
     * @param dtoList
     * @return
     */
    List <E> toEntity(List<DTO> dtoList);

    /**
     * Entity集合转DTO集合
     * @param entityList
     * @return
     */
    List <DTO> toDto(List<E> entityList);
}
