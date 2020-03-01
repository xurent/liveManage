package com.xurent.live.controller;


import com.xurent.live.model.FileBean;
import com.xurent.live.model.message.MessageData;
import com.xurent.live.model.out.UserInfo;
import com.xurent.live.service.FileService;
import com.xurent.live.service.TokenService;
import com.xurent.live.utils.UploadFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;


@RequestMapping("/file")
@RestController
public class FileController {


    @Autowired
    private TokenService iToken;


    @Autowired
    private FileService  fileService;

    /**
     *
     * @param file
     * @param type   0 普通，1VR
     * @return
     */
    @ResponseBody
    @PostMapping("/upload")
    public Object uploadImage(@RequestParam("file") MultipartFile file,@RequestParam("type") Integer type,HttpServletRequest request){

        String name =iToken.getUserInfo().getUsername();
        if(file==null||type==null||type<0||type>2){
            return MessageData.ofError("上传失败!");
        }
        UserInfo info=iToken.getUserInfo();
        if(info==null){
            return MessageData.ofError("非法请求!");
        }

        if(type==0)type=3;
        if(type==1)type=4;

        String work=UploadFileUtil.getFileUrl(file,name,type,request);

        FileBean fileBean=new FileBean();
        fileBean.setCreateTime(new Date());
        UploadFileUtil.setOnFileListner(new UploadFileUtil.FileListner() {
            @Override
            public void getFileName(String filename) {
                fileBean.setFileName(filename);
            }
        });
        fileBean.setUrl(work);
        fileBean.setNickName(name);
        fileBean.setType(type);
        fileService.save(fileBean);

        return MessageData.ofSuccess("上传成功!");
    }

    /**
     * 封面和头像
     * @param file
     * @return
     */

    @ResponseBody
    @PostMapping("/uploadImage")  //1封面 //0其它默认头像
    public Object uploadImage(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {

        if(file==null){
            return MessageData.ofError("上传失败!");
        }
        UserInfo info=iToken.getUserInfo();
        if(info==null){
            return MessageData.ofError("非法请求!");
        }

       String path= UploadFileUtil.getFileUrl(file,info.getUsername(),0,request);

        return MessageData.ofSuccess("上传成功",path);
    }


}
