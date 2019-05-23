package com.tencent.backstage.modules.tools.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.backstage.modules.tools.entity.Picture;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/4
 * Time:10:40
 */
@CacheConfig(cacheNames = "picture")
public interface PictureService {
    /**
     * 上传图片
     * @param file
     * @param username
     * @return
     */
    @CacheEvict(allEntries = true)
    Picture upload(MultipartFile file, String username) throws FileNotFoundException;

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Cacheable(key = "#p0")
    Picture findById(String uuid);

    /**
     * 删除图片
     * @param picture
     */
    @CacheEvict(allEntries = true)
    void delete(Picture picture);

    @Cacheable(keyGenerator = "keyGenerator")
    public Object queryAll(Picture picture, Page<Picture> pageInfo);

}
