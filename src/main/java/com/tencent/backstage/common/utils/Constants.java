package com.tencent.backstage.common.utils;


/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/1
 * Time:23:26
 */
public interface Constants{

    static final Config config = (Config) SpringUtil.getBean(Config.class);

    public static final String RESET_PASS = "重置密码";

    public static final String RESET_MAIL = "重置邮箱";

    public static final String NODELETE ="0";
    public static final String DELETE ="1";

    //保存
    interface Save{
        public static final String ERROR ="保存失败";
        public static final String SUCCESS ="保存成功";
    }
    //菜单
    interface Menu{
        //根节点
        public static final String rootid ="0";
    }
    //权限
    interface Permission{
        //根节点
        public static final String rootid ="0";
    }
    //部门
    interface Dept{
        //根节点
        public static final String rootid ="0";
    }

    //上传头像URL
    interface AvatarUrl{
        public static final String SM_MS_URL = config.getSmMsUrl();
    }
}
