package com.brok1n.java.web.cmccserver;

import com.cmcc.mid.softdetector.json.update.CheckUpdateResultNode;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;

import java.io.*;
import java.util.*;

/**
 * Created by jll on 2017/8/1.
 */
@Controller
public class ApiController {

    public static final String VERSION_LIST_FILE = "versionList.json";
    public static final String PLAY_CONFIG_FILE = "playConfig.json";
    public static final String UPLOAD_FOLDER = "upload";
    public String REQUEST_BASE_URL = "";
    public String FILE_BASE_PATH = "";

    Gson gson = new Gson();

    //版本列表
    List<CheckUpdateResultNode> versionList = new ArrayList<>();

    //天津播放地址列表
    List<String> tjPlayUrlList = new ArrayList<>();
    //新疆播放地址
    List<String> xjPlayUrlList = new ArrayList<>();

    public ApiController(){
        tjPlayUrlList.add( Constant.DEFAULT_TJ_PLAY);
        xjPlayUrlList.add(Constant.DEFAULT_XJ_PATH);
    }


    @ResponseBody
    @RequestMapping("hello")
    public String hello() {
        return "hello ApiController";
    }

    //http://sqmserver.cmri.cn:8080/family/checkUpdate

    @ResponseBody
    @RequestMapping("checkUpdate")
    public String checkUpdate() {

        /**
         * update	N	boolean	是否升级
         url	N	String	升级包下载地址
         bakurl	N	String	升级包下载备用地址
         versionCode	N	int	升级包版本号
         patchVersionCode	N	int	升级补丁号
         * */
        Collections.sort(versionList, new Comparator<CheckUpdateResultNode>() {
            @Override
            public int compare(CheckUpdateResultNode o1, CheckUpdateResultNode o2) {
                if ( o1 == null || o2 == null ) return 0;
                if ( o1.getVersionCode() > o2.getVersionCode() ) {
                    return -1;
                } else if ( o1.getVersionCode() == o2.getVersionCode() ) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });

        if ( !versionList.isEmpty() ) {
            String json = gson.toJson( versionList.get(0) );
            System.out.println( json );
            return json;
        }
        return "";
    }

    /*
     *采用spring提供的上传文件的方法
     */
    @ResponseBody
    @RequestMapping("springUpload")
    public String  springUpload(HttpServletRequest request) {
        initPath(request, "/springUpload");
        System.out.println("REQUEST_BASE_URL:" + REQUEST_BASE_URL );
        System.out.println("FILE_BASE_PATH:" + FILE_BASE_PATH );

        JsonObject obj = new JsonObject();

        long  startTime=System.currentTimeMillis();
        //本次升级的版本信息对象
        CheckUpdateResultNode checkUpdateResultNode = null;
        //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
        CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(
                request.getSession().getServletContext());
        //检查form中是否有enctype="multipart/form-data"
        if(multipartResolver.isMultipart(request))
        {
            //将request变成多部分request
            MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
            //获取multiRequest 中所有的文件名
            Iterator iter=multiRequest.getFileNames();

            while(iter.hasNext())
            {
                //一次遍历所有文件
                MultipartFile file=multiRequest.getFile(iter.next().toString());
                if(file!=null)
                {
                    String name = file.getOriginalFilename();

                    String[] infoSp = name.split("_");
                    if ( infoSp.length < 5 ) {
                        obj.addProperty("msg", "上传文件名格式不正确");
                        break;
                    }

                    String typeName = name.substring( name.lastIndexOf(".") + 1);
                    String prefixName = name.substring( 0, name.lastIndexOf(".") );
                    String fileName = prefixName + "_" + System.currentTimeMillis() + "." + typeName;

                    String path= FILE_BASE_PATH + File.separator + fileName;
                    File uploadFile = null;
                    try {
                        uploadFile = new File( path );
                        if ( !uploadFile.exists() ) {
                            uploadFile.getParentFile().mkdirs();
                            uploadFile.createNewFile();
                        }

                        System.out.println("upload path:" + uploadFile);

                        //上传
                        file.transferTo(new File(path));
                    } catch (IOException e) {
                        e.printStackTrace();
                        obj.addProperty("msg", "文件传输异常");
                    }

                    checkUpdateResultNode = new CheckUpdateResultNode();
                    checkUpdateResultNode.setBakurl(REQUEST_BASE_URL + File.separator +fileName);
                    checkUpdateResultNode.setPatchVersionCode(0);
                    checkUpdateResultNode.setUpdate(true);
                    checkUpdateResultNode.setUrl(REQUEST_BASE_URL + File.separator +fileName);
                    checkUpdateResultNode.setVersionCode(Integer.parseInt(infoSp[3]));
                    versionList.add( checkUpdateResultNode );
                }
            }
        } else {
            System.out.println("is not a multipart from ");
            obj.addProperty("msg", "不是文件上传表单");
        }

        Class cl = System.class.getClass();


        long  endTime=System.currentTimeMillis();
        System.out.println("方法三的运行时间："+String.valueOf(endTime-startTime)+"ms");

        if ( checkUpdateResultNode != null ) {
            String json = gson.toJson( versionList, new TypeToken<List<CheckUpdateResultNode>>() {}.getType() );
            String path = FILE_BASE_PATH + File.separator + VERSION_LIST_FILE;
            File uploadFile = new File( path );
            try {
                BufferedWriter bw = new BufferedWriter( new FileWriter( uploadFile ) );
                bw.write( json );
                bw.flush();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            obj.addProperty("result", "success");
            obj.add( "data", gson.toJsonTree( checkUpdateResultNode ));
            return gson.toJson( obj );
        } else {
            obj.addProperty("result", "failed");
            return gson.toJson( obj );
        }
    }


    @ResponseBody
    @RequestMapping("versionList")
    public String versionList(HttpServletRequest request) {

        initPath(request, "/versionList");
        initVersionList();

        return  gson.toJson( versionList );
    }

    //play
    @ResponseBody
    @RequestMapping("tjplaytest")
    public String tjPlayTest( ) {
        return Constant.TJ_PLAY;
    }

    @RequestMapping("tj/{id}")
    public String tjPlay(@PathVariable int id) {
        if ( tjPlayUrlList.isEmpty() || tjPlayUrlList.size() <= id ) {
            return "tjplaytest";
        }
        String url = tjPlayUrlList.get(id);
        if ( url == null || url.length() < 1 || url.toLowerCase().equals("null") ) {
            return "tjplaytest";
        }
        return "redirect:/tjplaytest?playurl=" + url;
    }

    @ResponseBody
    @RequestMapping("xj/{id}")
    public String xjPlay( @PathVariable int id ) {
        if ( xjPlayUrlList.isEmpty() || xjPlayUrlList.size() <= id ) {
            return "xjplaytest";
        }
        String url = xjPlayUrlList.get(id);
        if ( url == null || url.length() < 1 || url.toLowerCase().equals("null") ) {
            return "xjplaytest";
        }

        String xjContent = Constant.XJ_PLAY;
        xjContent = xjContent.replace("{{igmp}}", url);

        return xjContent;
    }

    @ResponseBody
    @RequestMapping("getXjPlayList")
    public String getXJPlayList() {
        return gson.toJson( xjPlayUrlList);
    }

    @ResponseBody
    @RequestMapping("getTjPlayList")
    public String getTjPlayList() {
        return gson.toJson( tjPlayUrlList);
    }

    @ResponseBody
    @RequestMapping("addTjPlayList")
    public String addTjPlayList( String url ) {
        String  tjUrl = new String( Base64Utils.decode( url.getBytes() ) );
        tjPlayUrlList.add( tjUrl );
        return "success";
    }

    @ResponseBody
    @RequestMapping("addXjPlayList")
    public String addXjPlayList( String url ) {
        String  xjUrl = new String( Base64Utils.decode( url.getBytes() ) );
        xjPlayUrlList.add( xjUrl );
        return "success";
    }

    private void initPath( HttpServletRequest request, String url  ){
        if ( REQUEST_BASE_URL == null || REQUEST_BASE_URL.length() < 5 ) {
            REQUEST_BASE_URL = request.getRequestURL().toString().replace( url, "");
            REQUEST_BASE_URL = REQUEST_BASE_URL.replace("family", "");
            REQUEST_BASE_URL = REQUEST_BASE_URL + UPLOAD_FOLDER + File.separator;
        }

        if ( FILE_BASE_PATH == null || FILE_BASE_PATH.length() < 5 ) {
            FILE_BASE_PATH = request.getServletContext().getRealPath("/") + File.separator + UPLOAD_FOLDER + File.separator;
            FILE_BASE_PATH = FILE_BASE_PATH.replace("family", "");
        }
    }

    private void initVersionList( ) {
        if ( versionList.isEmpty() ) {
            String listFilePath = FILE_BASE_PATH + File.separator + VERSION_LIST_FILE;
            File listFile = new File( listFilePath );
            if ( listFile.exists() ) {
                String json = null;
                try {
                    json = org.apache.commons.io.IOUtils.toString(new FileInputStream( listFile ));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                List<CheckUpdateResultNode> list =  gson.fromJson(json, new TypeToken<List<CheckUpdateResultNode>>() {}.getType());
                if ( list != null ) {
                    versionList.addAll(list);
                }
            }
        }
    }

}
