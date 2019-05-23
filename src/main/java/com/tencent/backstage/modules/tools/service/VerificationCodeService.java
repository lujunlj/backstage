package com.tencent.backstage.modules.tools.service;


import com.tencent.backstage.modules.models.vo.EmailVo;
import com.tencent.backstage.modules.tools.entity.VerificationCode;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/4
 * Time:12:47
 */
public interface VerificationCodeService {
    /**
     * 发送邮件验证码
     * @param code
     */
    EmailVo sendEmail(VerificationCode code);

    /**
     * 验证
     * @param code
     */
    void validated(VerificationCode code);
}
