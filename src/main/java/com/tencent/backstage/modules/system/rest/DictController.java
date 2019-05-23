package com.tencent.backstage.modules.system.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.backstage.common.annotation.Log;
import com.tencent.backstage.common.exception.BadRequestException;
import com.tencent.backstage.common.utils.StringUtils;
import com.tencent.backstage.modules.models.dto.DictDto;
import com.tencent.backstage.modules.system.entity.Dict;
import com.tencent.backstage.modules.system.service.DictService;
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
public class DictController {

    @Autowired
    private DictService dictService;

    private static final String ENTITY_NAME = "dict";

    @Log("查询字典")
    @GetMapping(value = "/dict")
    @PreAuthorize("hasAnyRole('ADMIN','DICT_ALL','DICT_SELECT')")
    public ResponseEntity getDicts(DictDto resources, Page<Dict> pageInfo){
        return new ResponseEntity(dictService.queryAll(resources,pageInfo), HttpStatus.OK);
    }

    @Log("新增字典")
    @PostMapping(value = "/dict")
    @PreAuthorize("hasAnyRole('ADMIN','DICT_ALL','DICT_CREATE')")
    public ResponseEntity create(@Validated @RequestBody Dict resources){
        if (StringUtils.isNotBlank(resources.getUuid())) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        return new ResponseEntity(dictService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改字典")
    @PutMapping(value = "/dict")
    @PreAuthorize("hasAnyRole('ADMIN','DICT_ALL','DICT_EDIT')")
    public ResponseEntity update(@Validated @RequestBody Dict resources){
        if(StringUtils.isBlank(resources.getUuid())){
            throw new BadRequestException("this"+ENTITY_NAME+"have no id");
        }
        dictService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除字典")
    @DeleteMapping(value = "/dict/{uuid}")
    @PreAuthorize("hasAnyRole('ADMIN','DICT_ALL','DICT_DELETE')")
    public ResponseEntity delete(@PathVariable @NotNull String uuid){
        dictService.delete(uuid);
        return new ResponseEntity(HttpStatus.OK);
    }

}
