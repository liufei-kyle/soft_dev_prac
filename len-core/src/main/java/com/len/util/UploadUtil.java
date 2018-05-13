package com.len.util;

import com.len.exception.MyException;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by meng on 2018/5/8.
 * 文件上传工具类
 */
@Getter
@Setter
@ConfigurationProperties
@Component
public class UploadUtil {

    /**
     * 上传请求资源
     */
    private MultipartFile multipartFile = null;

    /**
     * 按照当日创建文件夹
     */
    @Value("${lenosp.isDayType}")
    private boolean isDayType;
    /**
     * 自定义文件路径
     */
    @Value("${lenosp.uploadPath}")
    private String uploadPath;

    @Value("${lenosp.imagePath}")
    private String imagePath;

    public static final String IMAGE_SUFFIX = "bmp,jpg,png,gif,jpeg";

    private File currentFile;

    public UploadUtil(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public UploadUtil() {
    }

    public String upload() {
        if (isNull()) {
            throw new MyException("上传数据/地址获取异常");
        }

        String fileName = fileNameStyle();
        try {
            FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), currentFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    /**
     * 格式化文件名 默认采用UUID
     *
     * @return
     */
    public String fileNameStyle() {
        String curr = multipartFile.getOriginalFilename();
        int suffixLen = curr.lastIndexOf(".");
        if (suffixLen == -1) {
            throw new MyException("文件获取异常");
        }
        String suffix = curr.substring(suffixLen, curr.length());
        int index = Arrays.binarySearch(IMAGE_SUFFIX.split(","),
                suffix.replace(".", ""));

        curr = UUID.randomUUID() + suffix;
        String filename=curr;
        //image 情况
        curr = StringUtils.isEmpty(imagePath) || index == -1 ?
                uploadPath + File.separator + curr : imagePath +File.separator  + curr;
        currentFile = new File(curr);

        return filename;
    }

    private boolean isNull() {
        if (null != multipartFile) {
            return false;
        }
        return true;
    }


}
