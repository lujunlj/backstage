package com.tencent.backstage.modules.system.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.backstage.common.annotation.Log;
import com.tencent.backstage.common.exception.BadRequestException;
import com.tencent.backstage.common.utils.StringUtils;
import com.tencent.backstage.modules.models.dto.DictDetailDto;
import com.tencent.backstage.modules.system.entity.DictDetail;
import com.tencent.backstage.modules.system.service.DictDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/3
 * Time:15:07
 */
@RestController
@RequestMapping("api")
public class DictDetailController {
    @Autowired
    private DictDetailService dictDetailService;

    private static final String ENTITY_NAME ="dictDetail";

    @Log("查询字典详情")
    @GetMapping(value = "/dictDetail")
    public ResponseEntity getDictDetails(DictDetailDto resources, Page<DictDetail> pageInfo){
        return new ResponseEntity(dictDetailService.queryAll(resources,pageInfo), HttpStatus.OK);
    }

    @Log("新增字典详情")
    @PostMapping(value = "/dictDetail")
    @PreAuthorize("hasAnyRole('ADMIN','DICT_ALL','DICT_CREATE')")
    public ResponseEntity create(@Validated @RequestBody DictDetail resources){
        if (StringUtils.isNotBlank(resources.getUuid())) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        return new ResponseEntity(dictDetailService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改字典详情")
    @PutMapping(value = "/dictDetail")
    @PreAuthorize("hasAnyRole('ADMIN','DICT_ALL','DICT_EDIT')")
    public ResponseEntity update(@Validated @RequestBody DictDetail resources){
        if(StringUtils.isBlank(resources.getUuid())){
            throw new BadRequestException("this"+ENTITY_NAME+"have no id");
        }
        dictDetailService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除字典详情")
    @DeleteMapping(value = "/dictDetail/{uuid}")
    @PreAuthorize("hasAnyRole('ADMIN','DICT_ALL','DICT_DELETE')")
    public ResponseEntity delete(@PathVariable @NotNull String uuid){
        dictDetailService.delete(uuid);
        return new ResponseEntity(HttpStatus.OK);
    }
}
