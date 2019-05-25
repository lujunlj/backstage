package com.tencent.backstage.modules.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tencent.backstage.common.base.BaseServiceImpl;
import com.tencent.backstage.common.exception.BadRequestException;
import com.tencent.backstage.common.utils.Constants;
import com.tencent.backstage.common.utils.StringUtils;
import com.tencent.backstage.common.utils.ValidationUtil;
import com.tencent.backstage.modules.models.dto.MenuDto;
import com.tencent.backstage.modules.models.mapper.MenuDtoAndEntityMapper;
import com.tencent.backstage.modules.models.vo.MenuMetaVo;
import com.tencent.backstage.modules.models.vo.MenuVo;
import com.tencent.backstage.modules.system.dao.MenuDao;
import com.tencent.backstage.modules.system.entity.Menu;
import com.tencent.backstage.modules.system.entity.Role;
import com.tencent.backstage.modules.system.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/4/30
 * Time:18:03
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MenuServiceImpl extends BaseServiceImpl<MenuDao,Menu> implements MenuService {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuDtoAndEntityMapper menuDtoAndEntityMapper;

    @Override
    public MenuDto findById(String uuid) {
        Optional<Menu> menu = Optional.ofNullable(menuDao.selectById(uuid));
        ValidationUtil.isNull(menu,"Menu","uuid",uuid);
        return menuDtoAndEntityMapper.toDto(menu.get());
    }

    @Override
    public List<MenuDto> findByRoles(List<Role> roles) {
        Set<Menu> menus = new LinkedHashSet<>();
        for (Role role : roles) {
            List<Menu> menus1 = menuDao.findByRoles_IdOrderBySortAsc(role.getUuid()).stream().collect(Collectors.toList());
            menus.addAll(menus1);
        }
        return menus.stream().map(menuDtoAndEntityMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public MenuDto create(Menu resources) {
        if(menuDao.findByName(resources.getName()) != null){
//            throw new EntityExistException(Menu.class,"name",resources.getName());
            throw new BadRequestException("【"+resources.getName()+"】菜单已存在!");
        }
        if(resources.getIFrame()){
            if (!(resources.getPath().toLowerCase().startsWith("http://")||resources.getPath().toLowerCase().startsWith("https://"))) {
                throw new BadRequestException("外链必须以http://或者https://开头");
            }
        }
        return menuDtoAndEntityMapper.toDto(super.saveNew(resources));
    }

    @Override
    public void update(Menu resources) {
        if(resources.getUuid().equals(resources.getPid())) {
            throw new BadRequestException("上级不能为自己");
        }
        Optional<Menu> optionalPermission = Optional.ofNullable(menuDao.selectById(resources.getUuid()));
        ValidationUtil.isNull(optionalPermission,"Permission","uuid",resources.getUuid());

        if(resources.getIFrame()){
            if (!(resources.getPath().toLowerCase().startsWith("http://")||resources.getPath().toLowerCase().startsWith("https://"))) {
                throw new BadRequestException("外链必须以http://或者https://开头");
            }
        }
        Menu menu = optionalPermission.get();
        Menu menu1 = menuDao.findByName(resources.getName());

        if(menu1 != null && !menu1.getUuid().equals(menu.getUuid())){
//            throw new EntityExistException(Menu.class,"name",resources.getName());
            throw new BadRequestException("【"+resources.getName()+"】菜单已存在!");
        }
        super.updateById(resources);
    }

    @Override
    public void delete(String uuid) {
        menuDao.deleteById(uuid);
    }

    @Override
    public Object getMenuTree(List<Menu> menus) {
        List<Map<String,Object>> list = new LinkedList<>();
        menus.forEach(menu -> {
                    if (menu!=null){
                        List<Menu> menuList = menuDao.findByPid(menu.getUuid());
                        Map<String,Object> map = new HashMap<>();
                        map.put("id",menu.getUuid());
                        map.put("label",menu.getName());
                        if(menuList!=null && menuList.size()!=0){
                            map.put("children",getMenuTree(menuList));
                        }
                        list.add(map);
                    }
                }
        );
        return list;
    }

    @Override
    public List<Menu> findByPid(String pid) {
        return menuDao.findByPid(pid);
    }

    @Override
    public Map buildTree(List<MenuDto> menuDTOS) {
        List<MenuDto> trees = new ArrayList<MenuDto>();

        for (MenuDto menuDTO : menuDTOS) {

            if (Constants.Menu.rootid.equals(menuDTO.getPid().toString())) {
                trees.add(menuDTO);
            }

            for (MenuDto it : menuDTOS) {
                if (it.getPid().equals(menuDTO.getUuid())) {
                    if (menuDTO.getChildren() == null) {
                        menuDTO.setChildren(new ArrayList<MenuDto>());
                    }
                    menuDTO.getChildren().add(it);
                }
            }
        }
        Map map = new HashMap();
        map.put("content",trees.size() == 0?menuDTOS:trees);
        map.put("totalElements",menuDTOS!=null?menuDTOS.size():0);
        return map;
    }

    @Override
    public List<MenuVo> buildMenus(List<MenuDto> menuDTOS) {
        List<MenuVo> list = new LinkedList<>();
        menuDTOS.forEach(menuDTO -> {
                    if (menuDTO!=null){
                        List<MenuDto> menuDTOList = menuDTO.getChildren();
                        MenuVo menuVo = new MenuVo();
                        menuVo.setName(menuDTO.getName());
                        menuVo.setPath(menuDTO.getPath());

                        // 如果不是外链
                        if(!menuDTO.getIFrame()){
                            if(menuDTO.getPid().equals(Constants.Menu.rootid)){
                                //一级目录需要加斜杠，不然访问 会跳转404页面
                                menuVo.setPath("/" + menuDTO.getPath());
                                menuVo.setComponent(StrUtil.isEmpty(menuDTO.getComponent())?"Layout":menuDTO.getComponent());
                            }else if(!StrUtil.isEmpty(menuDTO.getComponent())){
                                menuVo.setComponent(menuDTO.getComponent());
                            }
                        }
                        menuVo.setMeta(new MenuMetaVo(menuDTO.getName(),menuDTO.getIcon()));
                        if(menuDTOList!=null && menuDTOList.size()!=0){
                            menuVo.setAlwaysShow(true);
                            menuVo.setRedirect("noredirect");
                            menuVo.setChildren(buildMenus(menuDTOList));
                            // 处理是一级菜单并且没有子菜单的情况
                        } else if(menuDTO.getPid().equals(Constants.Menu.rootid)){
                            MenuVo menuVo1 = new MenuVo();
                            menuVo1.setMeta(menuVo.getMeta());
                            // 非外链
                            if(!menuDTO.getIFrame()){
                                menuVo1.setPath("index");
                                menuVo1.setName(menuVo.getName());
                                menuVo1.setComponent(menuVo.getComponent());
                            } else {
                                menuVo1.setPath(menuDTO.getPath());
                            }
                            menuVo.setName(null);
                            menuVo.setMeta(null);
                            menuVo.setComponent("Layout");
                            List<MenuVo> list1 = new ArrayList<MenuVo>();
                            list1.add(menuVo1);
                            menuVo.setChildren(list1);
                        }
                        list.add(menuVo);
                    }
                }
        );
        return list;
    }

    @Override
    public Menu findOne(String uuid) {
        Optional<Menu> menu = Optional.ofNullable(menuDao.selectById(uuid));
        ValidationUtil.isNull(menu,"Menu","id",uuid);
        return menu.get();
    }

    /**
     * 不分页
     */
    @Override
    public List queryAll(String name){
        Menu menu  = new Menu();
        menu.setName(name);
        return menuDtoAndEntityMapper.toDto(menuDao.findAll(Wrappers.<Menu>lambdaQuery()
                    .like(StringUtils.isNotBlank(menu.getName()),Menu::getName,menu.getName())
                    .orderByAsc(Menu::getSort)));
    }

}