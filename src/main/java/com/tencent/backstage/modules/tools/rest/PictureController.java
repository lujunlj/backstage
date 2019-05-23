package com.tencent.backstage.modules.tools.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.backstage.common.annotation.Log;
import com.tencent.backstage.common.exception.BadRequestException;
import com.tencent.backstage.common.utils.SecurityContextHolder;
import com.tencent.backstage.modules.tools.entity.Picture;
import com.tencent.backstage.modules.tools.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/4
 * Time:21:41
 */
@Slf4j
@RestController
@RequestMapping("api")
public class PictureController {
    @Autowired
    private PictureService pictureService;

    @Log("查询图片")
    @PreAuthorize("hasAnyRole('ADMIN','PICTURE_ALL','PICTURE_SELECT')")
    @GetMapping(value = "/pictures")
    public ResponseEntity getRoles(Picture resources, Page<Picture> pageinfo){
        return new ResponseEntity(pictureService.queryAll(resources,pageinfo),HttpStatus.OK);
    }

    /**
     * 上传图片
     * @param file
     * @return
     * @throws Exception
     */
    @Log("上传图片")
    @PreAuthorize("hasAnyRole('ADMIN','PICTURE_ALL','PICTURE_UPLOAD')")
    @PostMapping(value = "/pictures")
    public ResponseEntity upload(@RequestParam MultipartFile file){
        UserDetails userDetails = SecurityContextHolder.getUserDetails();
        String userName = userDetails.getUsername();
        Picture picture = null;
        try {
            picture = pictureService.upload(file,userName);
        } catch (FileNotFoundException e) {
            throw new BadRequestException("图片上传失败!");
        }
        Map map = new HashMap();
        map.put("errno",0);
        map.put("uuid",picture.getUuid());
        map.put("data",new String[]{picture.getUrl()});
        return new ResponseEntity(map, HttpStatus.OK);
    }

    /**
     * 删除图片
     * @param uuid
     * @return
     */
    @Log("删除图片")
    @PreAuthorize("hasAnyRole('ADMIN','PICTURE_ALL','PICTURE_DELETE')")
    @DeleteMapping(value = "/pictures/{uuid}")
    public ResponseEntity delete(@PathVariable String uuid) {
        pictureService.delete(pictureService.findById(uuid));
        return new ResponseEntity(HttpStatus.OK);
    }
}
