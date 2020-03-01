package com.xurent.live.utils;

import com.xurent.live.exception.GlobalException;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

public class UploadFileUtil {


    private static FileListner onFileListner;


    public static void setOnFileListner(FileListner filename) {
        onFileListner = filename;
    }

    public static String getFileUrl(MultipartFile file, String name, int type, HttpServletRequest request){

        try {

            //上传文件路径
            File rootpath=new File(ResourceUtils.getURL("classpath:").getPath());
            if(!rootpath.exists()){
                rootpath=new File("");
            }
            String path=rootpath.getAbsolutePath()+"/static/uploadfiles/"+name;
            switch (type){
                case 0:  //默认
                    //头像
                    break;
                case 1:
                    path=path+"/room/";//封面
                    break;
                case 2:   path=path+"/video/";
                    //2d作品
                    break;
                case 3:  path=path+"/vr-video/";
                    //vr作品
                    break;
            }

            //被传上来的源文件名
            String file_Name=file.getOriginalFilename();
            if(onFileListner!=null){
                onFileListner.getFileName(file_Name);
            }
            int pointIndex = file_Name.indexOf(".");                        //点号的位置
            String fileSuffix = file_Name.substring(pointIndex);             //截取文件后缀
            String savedFileName =name.toString().concat(fileSuffix);
            File filepath = new File(path, savedFileName);
            //判断路径是否存在，不存在则创建一个
            if (!filepath.getParentFile().exists()) {
                filepath.getParentFile().mkdirs();//创建
            }
            file.transferTo(filepath);//写进硬盘
            path="http://"+request.getServerName()+":"+request.getLocalPort()+"/uploadfiles/"+name;
            switch (type){
                case 0:
                    path=path+filepath.getName();
                    //头像
                    break;
                case 1:
                    path=path+"/room/"+filepath.getName();
                    //封面
                    break;
                case 2:  path=path+"/video/"+filepath.getName();
                    //作品
                    break;
                case 3:path=path+"/vr-video/"+filepath.getName();
                    break;
            }

            return path;

        }catch (Exception e){

            e.printStackTrace();
        }

       throw  new GlobalException("上传文件失败");
    }




    public interface  FileListner{

        public void getFileName(String filename);

    }


}
