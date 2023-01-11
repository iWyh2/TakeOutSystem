package com.wyh.TakeOut.controller;

import com.wyh.TakeOut.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

//文件上传下载
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${wyh.path}")
    private String FilePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        //此处的file必须与前端设置的name一致 不是随便取
        //同时 此时file上传过来还只是一个临时文件
        //给他转存到另外一个位置去

        String originalFilename = file.getOriginalFilename();
        String suffix = null;
        if (originalFilename != null) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String filename = UUID.randomUUID() + suffix;

        File dir = new File(FilePath);
        if (!dir.exists()) {
            boolean mkdirs = dir.mkdirs();
            if (!mkdirs) {
                R.error("目录创建失败");
            }
        }

        try {
            file.transferTo(new File(FilePath + filename));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return R.success(filename);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {

        try {
            //通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(FilePath + name);
            //通过输出流 将文件写回浏览器
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            int len;
            byte[] buffer = new byte[1024];
            while ((len = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer,0,len);
                outputStream.flush();
            }

            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
