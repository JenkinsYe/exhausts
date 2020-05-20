package com.csdn.xs.exhausts.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @author YJJ
 * @Date: Created in 11:14 2019-12-12
 */
public class FileUtils {

    public static String saveImg(MultipartFile multipartFile) throws IOException {
        String savePath = ConstantUtils.IMG_PATH + "/" + multipartFile.getOriginalFilename();
        File file = new File(savePath);
        FileInputStream inputStream = (FileInputStream) multipartFile.getInputStream();
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(savePath));
        byte[] bs = new byte[1024];
        int len;

        while ((len = inputStream.read(bs)) != -1) {
            outputStream.write(bs, 0, len);
        }
        outputStream.flush();
        outputStream.close();
        return savePath;
    }
}
