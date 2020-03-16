package com.xurent.live.controller;


import com.xurent.live.model.CollectionWorks;
import com.xurent.live.model.FileBean;
import com.xurent.live.model.LiveRoom;
import com.xurent.live.model.message.MessageData;
import com.xurent.live.model.out.UserInfo;
import com.xurent.live.service.FileService;
import com.xurent.live.service.LiveRoomService;
import com.xurent.live.service.TokenService;
import com.xurent.live.service.UserService;
import com.xurent.live.utils.CookieUtils;
import com.xurent.live.utils.RedisUtil;
import com.xurent.live.utils.UploadFileUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;


@RequestMapping("/file")
@RestController
@Api(tags = "文件接口")
public class FileController {


    @Autowired
    private TokenService iToken;



    @Autowired
    private FileService  fileService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private LiveRoomService liveRoomService;



    /**
     *
     * @param file
     * @param type   0 普通，1VR
     * @return
     */
    @ResponseBody
    @PostMapping("/upload")
    public Object uploadImage(@RequestParam("file") MultipartFile file,@RequestParam(value = "image",required = false) MultipartFile image,@RequestParam("type") Integer type,HttpServletRequest request){

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
        if(image!=null){
          String  img=UploadFileUtil.getFileUrl(image,name,type,request);
          fileBean.setThumbleImg(img);
        }

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

    /**
     *
     * @param wid
     * @param make  0取消，1关注
     * @return
     */
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
            System.out.println(works.toString());
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


    /**
     *
     * @param workid
     * @return  1已经关注，0未关注
     */

    @ResponseBody
    @GetMapping("/iscollect")
    public Object isCollect(@RequestParam("wid") Integer workid){

       boolean exit= fileService.isCollect(iToken.getUserInfo().getUsername(),workid);

       if(exit){
           return  MessageData.ofSuccess("成功",1);
       }

        return  MessageData.ofSuccess("成功",0);
    }

    @ResponseBody
    @GetMapping("/get_files_by_type")
    public Object getFilesByType(@RequestParam("type") Integer type){

       List<FileBean> fileBeans=  fileService.getFilesByType(type);

        return  MessageData.ofSuccess("成功",fileBeans);
    }


    @ResponseBody
    @GetMapping("/works_by_uid")
    public Object getHer(@RequestParam("uid")String uid){

        List<FileBean> files= fileService.getFilesByUid(uid);
        System.out.println(files.size());
        return  MessageData.ofSuccess("获取成功",files);
    }

    @ResponseBody
    @GetMapping("/Herlikes")
    public Object getCollectionHer(@RequestParam("uid")String uid){

        List<FileBean> files= fileService.getCollectionsByUid(uid);
        System.out.println(files.size());

        return  MessageData.ofSuccess("获取成功",files);
    }


    @ResponseBody
    @PostMapping("/uploadCut")
    public Object uploadLiveCut(@RequestParam("file") MultipartFile file,HttpServletRequest request){

       String uid= iToken.getUserInfo().getUsername();
       LiveRoom r= liveRoomService.getRoomByUserName(uid);
        if(r.getRoomImg()!=null&&!r.getRoomImg().isEmpty()){
            return  MessageData.ofSuccess("ok");
        }
       if(file==null)return  MessageData.ofError("失败");
       String url=UploadFileUtil.getFileUrl(file,uid,1,request);
       r.setRoomImg(url);
       liveRoomService.updateRoom(r);

        return  MessageData.ofSuccess("上传成功");
    }




    @ResponseBody
    @DeleteMapping("/delete")
    public Object deleteFile(@RequestParam("fid") Integer fid){

     FileBean fileBean=   fileService.getById(fid);

     if(fileBean==null){
         return  MessageData.ofError("文件不存在");
     }
    String name=iToken.getUserInfo().getUsername();
     if(!fileBean.getUserId().equals(name)){
        return  MessageData.ofError("违规");
     }

     new Thread(){
         @Override
         public void run() {
             super.run();
             File rootpath= null;
             try {
                 rootpath = new File(ResourceUtils.getURL("classpath:").getPath());
             } catch (FileNotFoundException e) {
                 e.printStackTrace();
             }
             String path=rootpath.getAbsolutePath()+"/static/uploadfiles/"+name;
             if(fileBean.getType()==2){
                 path=path+"/video/";
             }else{
                 path=path+"/vr-video/";
             }

             rootpath=new File(path,fileBean.getFileName());
             System.out.println(rootpath.getPath());
             if(rootpath.exists()){
                 rootpath.delete();
             }
         }
     }.start();
     fileService.delete(fid);

        return  MessageData.ofSuccess("删除成功");
    }


    @ResponseBody
    @PostMapping("/monitor")
    public Object Monitor(@RequestParam("uid") String uid,@RequestParam("file")MultipartFile file) throws IOException {

        File rootpath=new File(ResourceUtils.getURL("classpath:").getPath());
        if(!rootpath.exists()){
            rootpath=new File("");
        }
        String path=rootpath.getAbsolutePath()+"/static/monitor/"+uid;
        //被传上来的源文件名
        String file_Name=file.getOriginalFilename();
        File filepath = new File(path, file_Name);
        //判断路径是否存在，不存在则创建一个
        if (!filepath.getParentFile().exists()) {
            filepath.getParentFile().mkdirs();//创建
        }
        file.transferTo(filepath);//写进硬盘 liveDance


        return MessageData.ofSuccess("成功");
    }


}
