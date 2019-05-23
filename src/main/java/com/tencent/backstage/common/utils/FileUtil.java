package com.tencent.backstage.common.utils;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/3
 * Time:16:13
 */
@Slf4j
public class FileUtil extends cn.hutool.core.io.FileUtil {

    private static final Config config = (Config) SpringUtil.getBean(Config.class);
    /**
     * 定义GB的计算常量
     */
    private static final int GB = 1024 * 1024 * 1024;
    /**
     * 定义MB的计算常量
     */
    private static final int MB = 1024 * 1024;
    /**
     * 定义KB的计算常量
     */
    private static final int KB = 1024;

    /**
     * 格式化小数
     */
    private static final DecimalFormat DF = new DecimalFormat("0.00");

    /**
     * 临时文件所在文件夹相对路径
     */
    private static final String tmpDir = config.getUploadFileTempPath();
    /**
     * MultipartFile转File
     * @param multipartFile
     * @return
     */
    public static File toFile(MultipartFile multipartFile){
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename();
        // 获取文件后缀
        String prefix="."+getExtensionName(fileName);
        File file = null;
        try {
            // 用uuid作为文件名，防止生成的临时文件重复
            file = File.createTempFile(IdUtil.simpleUUID(), prefix);
            // MultipartFile to File
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 
     * @param multipartFile
     * @return java.io.File
     * @author lujun
     * @description: TODO
     * @date 2019/5/15 9:14
     */
    public static File createTempFile(MultipartFile multipartFile) throws FileNotFoundException {
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename();
        // 获取文件后缀
        String prefix="."+getExtensionName(fileName);
        // 获取文件随机名
        String suffix = IdUtil.simpleUUID();
        if (prefix.length() < 3)
            throw new IllegalArgumentException("Prefix string too short");
        if (StringUtils.isBlank(suffix))
            suffix = ".tmp";
        //springboot环境下, 以jar包(项目根目录)所在目录作为文件资料的根目录, 这里定义 tmpDir 作为文件上传的存放路径
        //!jar包运行时, 这里有 file: 前缀!
        //String savePath = rootFile.getAbsolutePath() + tmpDir;  //不要直接拼接路径  避免 分隔符多或少
        String rootpath = "";
        if(StringUtils.isNotBlank(config.getUploadPath())){
            rootpath = config.getUploadPath();
        }else{
            rootpath = getJarRootPath();
        }
        File savePath = new File(rootpath, tmpDir);
        //判断上传文件的保存目录是否存在
        if (!savePath.exists() && !savePath.isDirectory()) {
            log.info(savePath + "目录不存在，需要创建");
            //创建目录
            boolean created = savePath.mkdirs();
            if (!created) {
                log.error("路径: '" + savePath.getAbsolutePath() + "'创建失败");
                throw new RuntimeException("路径: '" + savePath.getAbsolutePath() + "'创建失败");
            }
        }
        log.info("文件上传的存储路径为: {}", savePath.getAbsolutePath());
        File file = new File(savePath ,suffix+prefix);
        try {
            multipartFile.transferTo(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    private static String getJarRootPath() throws FileNotFoundException {
        String path = ResourceUtils.getURL("classpath:").getPath();
        //=> file:/root/tmp/demo-springboot-0.0.1-SNAPSHOT.jar!/BOOT-INF/classes!/
        log.debug("ResourceUtils.getURL(\"classpath:\").getPath() -> "+path);
        //创建File时会自动处理前缀和jar包路径问题  => /root/tmp
        File rootFile = new File(path);
        if(!rootFile.exists()) {
            log.info("根目录不存在, 重新创建");
            rootFile = new File("");
            log.info("重新创建的根目录: "+rootFile.getAbsolutePath());
        }
        log.debug("项目根目录: "+rootFile.getAbsolutePath());        //获取的字符串末尾没有分隔符 /
        return rootFile.getAbsolutePath();
    }

    /**
     * 删除
     * @param files
     */
    public static void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 获取文件扩展名
     * @param filename
     * @return
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * Java文件操作 获取不带扩展名的文件名
     * @param filename
     * @return
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    /**
     * 文件大小转换
     * @param size
     * @return
     */
    public static String getSize(int size){
        String resultSize = "";
        if (size / GB >= 1) {
            //如果当前Byte的值大于等于1GB
            resultSize = DF.format(size / (float) GB) + "GB   ";
        } else if (size / MB >= 1) {
            //如果当前Byte的值大于等于1MB
            resultSize = DF.format(size / (float) MB) + "MB   ";
        } else if (size / KB >= 1) {
            //如果当前Byte的值大于等于1KB
            resultSize = DF.format(size / (float) KB) + "KB   ";
        } else {
            resultSize = size + "B   ";
        }
        return resultSize;
    }
}
