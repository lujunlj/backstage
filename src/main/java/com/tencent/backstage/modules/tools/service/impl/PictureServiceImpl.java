package com.tencent.backstage.modules.tools.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.backstage.common.base.BaseServiceImpl;
import com.tencent.backstage.common.exception.BadRequestException;
import com.tencent.backstage.common.utils.*;
import com.tencent.backstage.modules.models.mapper.PictureDtoAndEntityMapper;
import com.tencent.backstage.modules.tools.dao.PictureDao;
import com.tencent.backstage.modules.tools.entity.Picture;
import com.tencent.backstage.modules.tools.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
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
public class PictureServiceImpl extends BaseServiceImpl<PictureDao, Picture> implements PictureService {

    @Autowired
    private PictureDao pictureDao;

    @Autowired
    private PictureDtoAndEntityMapper pictureDtoAndEntityMapper;

    private static final String SUCCESS = "success";

    private static final String CODE = "code";

    private static final String MSG = "msg";

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Picture upload(MultipartFile multipartFile, String username) throws FileNotFoundException {
        File file = FileUtil.createTempFile(multipartFile);

        HashMap<String, Object> paramMap = new HashMap<>();
        //参数请见 sm.ms 官网api
        paramMap.put("smfile", file);
        String result= HttpUtil.post(Constants.AvatarUrl.SM_MS_URL, paramMap);

        JSONObject jsonObject = JSONUtil.parseObj(result);
        if(!jsonObject.get(CODE).toString().equals(SUCCESS)){
            throw new BadRequestException(jsonObject.get(MSG).toString());
        }
        Picture picture = null;
        //转成实体类 上传成功返回{code:"",data:{url:"",width:"",......}} 参数请见 sm.ms 官网api
        picture = JSON.parseObject(jsonObject.get("data").toString(), Picture.class);
        picture.setSize(FileUtil.getSize(Integer.valueOf(picture.getSize())));
        picture.setUsername(username);
        picture.setFilename(FileUtil.getFileNameNoEx(multipartFile.getOriginalFilename())+"."+FileUtil.getExtensionName(multipartFile.getOriginalFilename()));
        super.save(picture);
        //删除临时文件
        FileUtil.deleteFile(file);
        return picture;

    }

    @Override
    public Picture findById(String uuid) {
        Optional<Picture> picture = Optional.ofNullable(pictureDao.selectById(uuid));
        ValidationUtil.isNull(picture,"Picture","uuid",uuid);
        return picture.get();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Picture picture) {
        try {
            String result= HttpUtil.get(picture.getDelete());
            super.deletePhysicsById(picture.getUuid());
        } catch(Exception e){
            super.deletePhysicsById(picture.getUuid());
        }

    }

    @Override
    public Object queryAll(Picture picture, Page<Picture> pageInfo) {
        pageInfo = super.findWithPage(pageInfo, Wrappers.<Picture>lambdaQuery()
                            .eq(StringUtils.isNotBlank(picture.getFilename()),Picture::getFilename,picture)
                            .orderByDesc(Picture::getCreateTime));
        return PageUtil.toPage(pageInfo);
    }
}
