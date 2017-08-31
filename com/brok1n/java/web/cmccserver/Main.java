package com.brok1n.java.web.cmccserver;

/**
 * Created by jll on 2017/8/24.
 */
public class Main {


    public static void main(String args[]) {
        //http://112.5.254.243/hd.yinyuetai.com/uploads/videos/common/EB0D01505EFEDC6797C416686A8CA7C0.flv?sc\u003d30f5892f23cf7faa\u0026br\u003d1101\u0026vid\u003d2393129\u0026aid\u003d39257\u0026area\u003dML\u0026vst\u003d0
        //http://111.13.171.20/vlive.qqvideo.tc.qq.com/o0022136kvm.mp4?vkey=E3BA4F2FE74DA06AE5E879E6E714C5E0B2172605273E3D74DCF8889F6E8F4064AB8926D1D35E07799285C62FBC2636CEB6C5E916589F0AE7B8D6C15F1E55B8DF3D3583137DEECA0DBCD1B5A674056806EE93A1B379B040449AEFDB5A40518B081F2B9288D3A04A4E9F8209D020C5713E339EDE6F010BA8B4&locid=21aa9ea6-7ebf-43db-8845-4ef5e427e989&size=12519318&ocid=2386892204
        //RemoteVideoPlayTask remoteVideoPlayTask = new RemoteVideoPlayTask( "192.168.1.222", 8080, "/2.ts" );
        //RemoteVideoPlayTask remoteVideoPlayTask = new RemoteVideoPlayTask( "111.13.171.20", 80, "/vlive.qqvideo.tc.qq.com/o0022136kvm.mp4?vkey=E3BA4F2FE74DA06AE5E879E6E714C5E0B2172605273E3D74DCF8889F6E8F4064AB8926D1D35E07799285C62FBC2636CEB6C5E916589F0AE7B8D6C15F1E55B8DF3D3583137DEECA0DBCD1B5A674056806EE93A1B379B040449AEFDB5A40518B081F2B9288D3A04A4E9F8209D020C5713E339EDE6F010BA8B4&locid=21aa9ea6-7ebf-43db-8845-4ef5e427e989&size=12519318&ocid=2386892204" );
        //RemoteVideoPlayTask remoteVideoPlayTask = new RemoteVideoPlayTask( "112.5.254.243", 80, "/hd.yinyuetai.com/uploads/videos/common/EB0D01505EFEDC6797C416686A8CA7C0.flv?sc\\u003d30f5892f23cf7faa\\u0026br\\u003d1101\\u0026vid\\u003d2393129\\u0026aid\\u003d39257\\u0026area\\u003dML\\u0026vst\\u003d0" );
        //RemoteVideoPlayTask remoteVideoPlayTask = new RemoteVideoPlayTask( "www.baidu.com", 80, "/" );
        //RemoteTCPDownloadTask remoteVideoPlayTask = new RemoteTCPDownloadTask( "112.5.254.243", 80, "/hc.yinyuetai.com/uploads/videos/common/8063015A8F40658E463185BAAD29142D.mp4" );
//        RemoteVideoPlayTask remoteVideoPlayTask = new RemoteVideoPlayTask( "36.189.240.18", 8090, "/" );
//
//        Thread th = new Thread( remoteVideoPlayTask );
//        th.start();

        //DownloadTask downloadTask = new DownloadTask("192.168.1.222", 8080, "/2.ts");
        //DownloadTask downloadTask = new DownloadTask("php.brok1n.com", 80, "/2.ts");
        //ScanPortTask scanPortTask = new ScanPortTask("111.23.13.63", 78, 65535);
        //ScanPortTask scanPortTask = new ScanPortTask("www.brok1n.com", 78, 65535, 120, 500);
        //ScanPortTask scanPortTask = new ScanPortTask("192.168.1.222", 0, 65535, 5, 500);
        ScanPortTask scanPortTask = new ScanPortTask("www.baidu.com", 0, 65535, 300, 500);

        Thread thread = new Thread( scanPortTask);
        thread.start();

    }

}
