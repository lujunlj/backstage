package com.tencent.backstage.modules.system.service.impl;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.backstage.common.base.BaseServiceImpl;
import com.tencent.backstage.common.exception.BadRequestException;
import com.tencent.backstage.common.utils.Constants;
import com.tencent.backstage.common.utils.PageUtil;
import com.tencent.backstage.common.utils.ValidationUtil;
import com.tencent.backstage.modules.models.dto.UserDto;
import com.tencent.backstage.modules.models.mapper.UserDtoAndEntityMapper;
import com.tencent.backstage.modules.monitor.service.RedisService;
import com.tencent.backstage.modules.system.dao.UserDao;
import com.tencent.backstage.modules.system.entity.User;
import com.tencent.backstage.modules.system.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserServiceImpl extends BaseServiceImpl<UserDao,User> implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserDtoAndEntityMapper userDtoAndEntityMapper;

    @Override
    public User findByUserName(String username) throws UsernameNotFoundException {
        User user = null;
        if(ValidationUtil.isEmail(username)){
            user = userDao.findByEmail(username);
        } else {
            user = userDao.loadUserByUsername(username);
        }
        if (user == null) {
            throw new UsernameNotFoundException("用户名不对!");
        }
        return user;
    }

    @Override
    public UserDto findById(String userid) {
        Optional<User> user = Optional.ofNullable(userDao.selectById(userid));
        ValidationUtil.isNull(user,"User","uuid",userid);
        return userDtoAndEntityMapper.toDto(user.get());
    }

    private User beforeSaveOrUpdate(User resources){
        if(StringUtils.isBlank(resources.getDeptId())&& resources.getDept()!=null && StringUtils.isNotBlank(resources.getDept().getUuid())){
            resources.setDeptId(resources.getDept().getUuid());
        }
        if(StringUtils.isBlank(resources.getJobId())&& resources.getJob()!=null && StringUtils.isNotBlank(resources.getJob().getUuid())){
            resources.setJobId(resources.getJob().getUuid());
        }
        return resources;
    }

    //添加角色
    @Transactional(rollbackFor = Exception.class)
    public User saveUserRole(User resources){
        if(resources.getRoles() != null){
            Map<String ,Object> map = new HashMap<>();
            map.put("userid",resources.getUuid());
            map.put("roles",resources.getRoles());
            userDao.addUserRoleBatch(map);
        }
        return resources;
    }
    //修改角色
    @Transactional(rollbackFor = Exception.class)
    public void updateUserRole(User resources){
            userDao.deleteUserRoleByUserId(resources.getUuid());
            saveUserRole(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDto create(User resources) {
        if(resources == null) throw new BadRequestException("用户为空!");
        if(userDao.loadUserByUsername(resources.getUsername())!=null){
//            throw new EntityExistException(User.class,"username",resources.getUsername());
            throw new BadRequestException("用户名已经存在!");
        }
        if(userDao.findByEmail(resources.getEmail())!=null){
//            throw new EntityExistException(User.class,"email",resources.getEmail());
            throw new BadRequestException("邮箱已经存在!");
        }
        // 默认密码 123，此密码是加密后的字符
        resources.setPassword("$2a$10$ySG2lkvjFHY5O0./CPIE1OI8VJsuKYEzOYzqIa7AJR6sEgSzUFOAm");
        resources.setAvatar("https://aurora-1255840532.cos.ap-chengdu.myqcloud.com/8918a306ea314404835a9196585c4b75.jpeg");
        //最后修改密码日期
        resources.setLastPasswordResetTime(LocalDateTime.now());
        resources = beforeSaveOrUpdate(resources);
        if(super.save(resources)){
            return userDtoAndEntityMapper.toDto(saveUserRole(resources));
        }
        return userDtoAndEntityMapper.toDto(resources);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(User resources) {
        Optional<User> userOptional = Optional.ofNullable(userDao.selectById(resources.getUuid()));
        ValidationUtil.isNull(userOptional,"User","uuid",resources.getUuid());
        User user = userOptional.get();
        User user1 = userDao.loadUserByUsername(resources.getUsername());
        User user2 = userDao.findByEmail(resources.getEmail());

        if(user1 !=null&&!user.getUuid().equals(user1.getUuid())){
//            throw new EntityExistException(User.class,"username",resources.getUsername());
            throw new BadRequestException("用户名已经存在!");
        }
        if(user2!=null&&!user.getUuid().equals(user2.getUuid())){
//            throw new EntityExistException(User.class,"email",resources.getEmail());
            throw new BadRequestException("邮箱已经存在!");
        }
        // 如果用户的角色改变了，需要手动清理下缓存
        if (!resources.getRoles().equals(user.getRoles())) {
            String key = "role::loadPermissionByUser:" + user.getUsername();
            redisService.delete(key);
            key = "role::findByUserId:" + user.getUuid();
            redisService.delete(key);
        }
        resources = beforeSaveOrUpdate(resources);
        if(super.updateById(resources)){
            updateUserRole(resources);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String userid) {
        userDao.deleteById(userid);
    }

    @Override
    public Object queryAll(UserDto userDto, Set<String> deptIds, Page<User> pageInfo) {
        userDto.setDeptids(deptIds);
        pageInfo = super.findWithPage(pageInfo,Wrappers.<User>lambdaQuery()
                .and(obj ->obj.eq(userDto.getEnabled()!= null,User::getEnabled,userDto.getEnabled())
                               .eq(User::getDelFlag, Constants.NODELETE)
                               .in(userDto.getDeptids() != null && !userDto.getDeptids().isEmpty(),User::getDeptId,userDto.getDeptids())
                               .like(StringUtils.isNotBlank(userDto.getUsername()),User::getUsername,userDto.getName())
                               .like(StringUtils.isNotBlank(userDto.getEmail()),User::getEmail,userDto.getEmail())
                               ).orderByDesc(User::getCreateTime));
        return PageUtil.toPage(pageInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePass(String username, String pass) {
        userDao.updatePass(username,pass);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAvatar(String username, String url) {
        userDao.updateAvatar(username,url);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEmail(String username, String email) {
        userDao.updateEmail(username,email);
    }

}
