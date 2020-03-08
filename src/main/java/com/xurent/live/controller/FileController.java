package com.xurent.live.controller;


import com.xurent.live.model.CollectionWorks;
import com.xurent.live.model.FileBean;
import com.xurent.live.model.message.MessageData;
import com.xurent.live.model.out.UserInfo;
import com.xurent.live.service.FileService;
import com.xurent.live.service.TokenService;
import com.xurent.live.service.UserService;
import com.xurent.live.utils.CookieUtils;
import com.xurent.live.utils.RedisUtil;
import com.xurent.live.utils.UploadFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;


@RequestMapping("/file")
@RestController
public class FileController {


    @Autowired
    private TokenService iToken;



    @Autowired
    private FileService  fileService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserService userService;

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

        if(type==0)type=2;
        if(type==1)type=3;

        String work=UploadFileUtil.getFileUrl(file,name,type,request);

        FileBean fileBean=new FileBean();
        fileBean.setCreateTime(new Date());
        fileBean.setFileName(file.getOriginalFilename());
        fileBean.setUserId(name);
        fileBean.setUrl(work);
        fileBean.setNickName(info.getNickName());
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
        String update_token= CookieUtils.getCookie(request.getCookies(),"update");
        String userID= (String) redisUtil.get(update_token);
        if(userID!=null){
            String path= UploadFileUtil.getFileUrl(file,userID,0,request);

            return MessageData.ofSuccess("上传成功",path);
        }else {


            UserInfo info=iToken.getUserInfo();
            if(info==null){
                return MessageData.ofError("非法请求!");
            }

            String path= UploadFileUtil.getFileUrl(file,info.getUsername(),0,request);

            return MessageData.ofSuccess("上传成功",path);

        }

    }


    @ResponseBody
    @GetMapping("/like")
    public Object collection(@RequestParam("workId") Integer wid,@RequestParam("make") Integer make){

        UserInfo U=iToken.getUserInfo();
        FileBean f=fileService.getById(wid);
        if(f==null){
            return MessageData.ofError();
        }
        if(make==1){
            CollectionWorks works=new CollectionWorks();
            works.setUid(U.getUsername());
            works.setWorkId(f.getId());
            works.setCreateTime(new Date());
            fileService.Collection(works);
        }else if(make==0){
            //取消
            fileService.UnCollection(U.getUsername(),f.getId());
        }

        return  MessageData.ofSuccess();
    }


    @ResponseBody
    @GetMapping("/mylikes")
    public Object getCollection(){

       List<FileBean> files= fileService.getCollectionsByUid(iToken.getUserInfo().getUsername());
       System.out.println(files.size());

        return  MessageData.ofSuccess("获取成功",files);
    }



    @ResponseBody
    @GetMapping("/myworks")
    public Object getMy(){

        List<FileBean> files= fileService.getFilesByUid(iToken.getUserInfo().getUsername());
        System.out.println(files.size());
        return  MessageData.ofSuccess("获取成功",files);
    }

    @ResponseBody
    @GetMapping("/getall")
    public Object getAll(){

        List<FileBean> files= fileService.getAllFiles();
        return  MessageData.ofSuccess("获取成功",files);
    }


}
