package com.tencent.backstage.modules.system.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.backstage.common.annotation.Log;
import com.tencent.backstage.common.exception.BadRequestException;
import com.tencent.backstage.common.utils.Constants;
import com.tencent.backstage.common.utils.PageUtil;
import com.tencent.backstage.common.utils.SecurityContextHolder;
import com.tencent.backstage.common.utils.StringUtils;
import com.tencent.backstage.config.system.DataScope;
import com.tencent.backstage.modules.models.dto.UserDto;
import com.tencent.backstage.modules.system.entity.User;
import com.tencent.backstage.modules.system.service.DeptService;
import com.tencent.backstage.modules.system.service.UserService;
import com.tencent.backstage.modules.tools.entity.Picture;
import com.tencent.backstage.modules.tools.entity.VerificationCode;
import com.tencent.backstage.modules.tools.service.PictureService;
import com.tencent.backstage.modules.tools.service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/3
 * Time:19:10
 */
@RestController
@RequestMapping("api")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private PictureService pictureService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private DataScope dataScope;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private static final String ENTITY_NAME = "user";

    @Log("查询用户")
    @GetMapping(value = "/users")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_SELECT')")
    public ResponseEntity getUsers(UserDto userDTO, Page<User> pageInfo){
        Set<String> deptSet = new HashSet<>();
        Set<String> result = new HashSet<>();

        if (!ObjectUtils.isEmpty(userDTO.getDeptId())) {
            deptSet.add(userDTO.getDeptId());
            deptSet.addAll(dataScope.getDeptChildren(deptService.findByPid(userDTO.getDeptId())));
        }

        // 数据权限
        Set<String> deptIds = dataScope.getDeptIds();

        // 查询条件不为空并且数据权限不为空则取交集
        if (!CollectionUtils.isEmpty(deptIds) && !CollectionUtils.isEmpty(deptSet)){

            // 取交集
            result.addAll(deptSet);
            result.retainAll(deptIds);

            // 若无交集，则代表无数据权限
            if(result.size() == 0){
                return new ResponseEntity(PageUtil.toPage(null,0),HttpStatus.OK);
            } else return new ResponseEntity(userService.queryAll(userDTO,result,pageInfo),HttpStatus.OK);
            // 否则取并集
        } else {
            result.addAll(deptSet);
            result.addAll(deptIds);
            return new ResponseEntity(userService.queryAll(userDTO,result,pageInfo),HttpStatus.OK);
        }
    }

    @Log("新增用户")
    @PostMapping(value = "/users")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_CREATE')")
    public ResponseEntity create(@Validated @RequestBody User resources){
        if (StringUtils.isNotBlank(resources.getUuid())) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        return new ResponseEntity(userService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改用户")
    @PutMapping(value = "/users")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_EDIT')")
    public ResponseEntity update(@Validated @RequestBody User resources){
        if(StringUtils.isBlank(resources.getUuid())){
            throw new BadRequestException("this"+ENTITY_NAME+"have no id");
        }
        userService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除用户")
    @DeleteMapping(value = "/users/{uuid}")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_DELETE')")
    public ResponseEntity delete(@PathVariable String uuid){
        userService.delete(uuid);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 验证密码
     * @param pass
     * @return
     */
    @GetMapping(value = "/users/validPass/{pass}")
    public ResponseEntity validPass(@PathVariable String pass){
        UserDetails userDetails = SecurityContextHolder.getUserDetails();
        Map map = new HashMap();
        map.put("status",200);
        if(!passwordEncoder.matches(pass,userDetails.getPassword())){
            map.put("status",400);
        }
        return new ResponseEntity(map,HttpStatus.OK);
    }

    /**
     * 修改密码
     * @param pass
     * @return
     */
    @Log("修改密码")
    @GetMapping(value = "/users/updatePass/{pass}")
    public ResponseEntity updatePass(@PathVariable String pass){
        UserDetails userDetails = SecurityContextHolder.getUserDetails();
//        if(userDetails.getPassword().equals(EncryptUtils.encryptPassword(pass))){
        if(passwordEncoder.matches(pass,userDetails.getPassword())){
            throw new BadRequestException("新密码不能与旧密码相同");
        }
        userService.updatePass(userDetails.getUsername(),passwordEncoder.encode(pass));
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 修改头像
     * @param file
     * @return
     */
    @Log("修改头像")
    @PostMapping(value = "/users/updateAvatar")
    public ResponseEntity updateAvatar(@RequestParam MultipartFile file){
        UserDetails userDetails = SecurityContextHolder.getUserDetails();
        Picture picture = null;
        try {
            picture = pictureService.upload(file,userDetails.getUsername());
        } catch (FileNotFoundException e) {
            throw new BadRequestException("上传错误!");
        }
        userService.updateAvatar(userDetails.getUsername(),picture.getUrl());
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 修改邮箱
     * @param user
     * @param user
     * @return
     */
    @Log("修改邮箱")
    @PostMapping(value = "/users/updateEmail/{code}")
    public ResponseEntity updateEmail(@PathVariable String code, @RequestBody User user){
        UserDetails userDetails = SecurityContextHolder.getUserDetails();
        if(!passwordEncoder.matches(user.getPassword(),userDetails.getPassword())){
            throw new BadRequestException("密码错误");
        }
        VerificationCode verificationCode = new VerificationCode(code, Constants.RESET_MAIL,"email",user.getEmail());
        verificationCodeService.validated(verificationCode);
        userService.updateEmail(userDetails.getUsername(),user.getEmail());
        return new ResponseEntity(HttpStatus.OK);
    }

    
}
