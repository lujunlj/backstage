package com.tencent.backstage.modules.tools.service.impl;

import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.tencent.backstage.common.base.BaseServiceImpl;
import com.tencent.backstage.common.exception.BadRequestException;
import com.tencent.backstage.common.utils.EncryptUtils;
import com.tencent.backstage.common.utils.StringUtils;
import com.tencent.backstage.modules.models.vo.EmailVo;
import com.tencent.backstage.modules.tools.dao.EmailConfigDao;
import com.tencent.backstage.modules.tools.entity.EmailConfig;
import com.tencent.backstage.modules.tools.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/4
 * Time:21:55
 */
@Service
public class EmailServiceImpl extends BaseServiceImpl<EmailConfigDao, EmailConfig> implements EmailService {
    @Autowired
    private EmailConfigDao emailConfigDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EmailConfig update(EmailConfig emailConfig, EmailConfig old) {
        try {
            if(!emailConfig.getPass().equals(old.getPass())){
                // 对称加密
                emailConfig.setPass(EncryptUtils.desEncrypt(emailConfig.getPass()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(super.updateById(emailConfig)){
            return emailConfig;
        }else{
            throw new BadRequestException("更新失败");
        }
    }

    @Override
    public EmailConfig find() {
        Optional<EmailConfig> emailConfig = Optional.ofNullable(emailConfigDao.selectByPrimaryKey(""));
        if(emailConfig.isPresent()){
            return emailConfig.get();
        } else {
            return new EmailConfig();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void send(EmailVo emailVo, EmailConfig emailConfig){
        if(emailConfig == null || StringUtils.isBlank(emailConfig.getUuid())){
            throw new BadRequestException("请先配置，再操作");
        }
        /**
         * 封装
         */
        MailAccount account = new MailAccount();
        account.setHost(emailConfig.getHost());
        account.setPort(Integer.parseInt(emailConfig.getPort()));
        account.setAuth(true);
        try {
            // 对称解密
            account.setPass(EncryptUtils.desDecrypt(emailConfig.getPass()));
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
        account.setFrom(emailConfig.getUser()+"<"+emailConfig.getFromUser()+">");
        //ssl方式发送
        account.setStartttlsEnable(true);
        String content = emailVo.getContent();
        /**
         * 发送
         */
        try {
            Mail.create(account)
                                .setTos(emailVo.getTos().stream().toArray(String[]::new))
                                .setTitle(emailVo.getSubject())
                                .setContent(content)
                                .setHtml(true)
                                .send();
//            MailUtil.send(account,emailVo.getTos(),emailVo.getSubject(),content,true);
        }catch (Exception e){
            throw new BadRequestException(e.getMessage());
        }
    }
}
