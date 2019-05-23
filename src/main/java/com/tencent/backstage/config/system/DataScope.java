package com.tencent.backstage.config.system;

import com.tencent.backstage.common.utils.SecurityContextHolder;
import com.tencent.backstage.modules.system.entity.Dept;
import com.tencent.backstage.modules.system.entity.Role;
import com.tencent.backstage.modules.system.entity.User;
import com.tencent.backstage.modules.system.service.DeptService;
import com.tencent.backstage.modules.system.service.RoleService;
import com.tencent.backstage.modules.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 数据权限配置
 * @author jie
 * @date 2019-4-1
 */
@Component
public class DataScope {

    private final String[] scopeType = {"全部","本级","自定义"};

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private DeptService deptService;

    public Set<String> getDeptIds() {

        User user = userService.findByUserName(SecurityContextHolder.getUserDetails().getUsername());

        // 用于存储部门id
        Set<String> deptIds = new HashSet<>();

        // 查询用户角色
        List<Role> roleSet = roleService.findByUserId(user.getUuid());

        for (Role role : roleSet) {

            if (scopeType[0].equals(role.getDataScope())) {
                return new HashSet<>() ;
            }

            // 存储本级的数据权限
            if (scopeType[1].equals(role.getDataScope())) {
                deptIds.add(user.getDept().getUuid());
            }

            // 存储自定义的数据权限
            if (scopeType[2].equals(role.getDataScope())) {
                Set<Dept> deptList = role.getDepts();
                for (Dept dept : deptList) {
                    deptIds.add(dept.getUuid());
                    List<Dept> deptChildren = deptService.findByPid(dept.getPid());
                    if (deptChildren != null && deptChildren.size() != 0) {
                        deptIds.addAll(getDeptChildren(deptChildren));
                    }
                }
            }
        }
        return deptIds;
    }


    public List<String> getDeptChildren(List<Dept> deptList) {
        List<String> list = new ArrayList<>();
        deptList.forEach(dept -> {
                    if (dept!=null && dept.getEnabled()){
                        List<Dept> depts = deptService.findByPid(dept.getUuid());
                        if(deptList!=null && deptList.size()!=0){
                            list.addAll(getDeptChildren(depts));
                        }
                        list.add(dept.getUuid());
                    }
                }
        );
        return list;
    }
}
