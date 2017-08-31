package com.brok1n.java.web.cmccserver;

/**
 * Created by jll on 2017/8/21.
 */
public class Constant {

    public static final String TJ_PLAY = "<html>\n" +
            "<head>\n" +
            "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
            "<meta name=\"page-view-size\" content=\"1280*720\" />\n" +
            "<script type=\"text/javascript\">\n" +
            "  var playIcon;\n" +
            "  var playIconTimer;\n" +
            "\tvar mediaPlayer = new MediaPlayer();\n" +
            "\tvar playurl =\"\";\n" +
            "\tfunction init() {\n" +
            "\t\tplayIcon = document.getElementById(\"playIcon\");\n" +
            "\t\tvar tmpUrl = window.location.href;\n" +
            "\t\tvar startIndex = tmpUrl.indexOf(\"?\");\n" +
            "\t\tvar tmpUrl = tmpUrl.substring(startIndex + 1, tmpUrl.length);\n" +
            "\t\tstartIndex = tmpUrl.indexOf(\"=\");\n" +
            "\t  playurl = tmpUrl.substring(startIndex + 1, tmpUrl.length);\n" +
            "\t\tconsole.log(\"playurl =\" + playurl);\n" +
            "\t\tmediaPlayer.setSingleMedia(\"[{mediaURL:'\" + playurl + \"' ,mediaCode:'code1',mediaType:2,entryID:'entry1'}]\");\n" +
            "\t\tmediaPlayer.playFromStart();\n" +
            "\t}\n" +
            "\n" +
            "\tfunction exit() {\n" +
            "\t\tmediaPlayer.stop();\n" +
            "\t}\n" +
            "\n" +
            "\tfunction handleMediaEvent() {\n" +
            "\t\tconsole.log(\"media event :\"+ Utility.getEvent());\n" +
            "\t\teval(\"var mediaEvent=\" + Utility.getEvent());\n" +
            "     \tswitch(mediaEvent.type){\n" +
            "     \tcase \"EVENT_PLAYMODE_CHANGE\":\n" +
            "     \t\tplayIcon.innerHTML = \"\";\n" +
            "     \t\tif(mediaEvent.new_play_mode == 2){\n" +
            "     \t\t\tclearTimeout(playIconTimer);\n" +
            "     \t\t\tplayIcon.style.display = \"block\";\n" +
            "     \t\t\tplayIcon.style.backgroundImage = \"url('./images/play.png')\";\n" +
            "     \t\t\tsetTimeout(\"playIconCtrl()\",5*1000);\n" +
            "     \t\t}else if(mediaEvent.new_play_mode == 1){\n" +
            "     \t\t\tclearTimeout(playIconTimer);\n" +
            "     \t\t\tplayIcon.style.display = \"block\";\n" +
            "     \t\t\tplayIcon.style.backgroundImage = \"url('./images/pause.png')\";\n" +
            "     \t\t}else if(mediaEvent.new_play_mode == 0){\n" +
            "         \t\tclearTimeout(playIconTimer);\n" +
            "         \t\tplayIcon.innerHTML = \"播放结束\";\n" +
            "         \t\tplayIcon.style.backgroundImage = \"url('./images/none.png')\";\n" +
            "     \t\t\tplayIcon.style.display = \"block\";\n" +
            "     \t\t}\n" +
            "     \t\tbreak;\n" +
            "     \tcase \"EVENT_MEDIA_END\":\n" +
            "     \t\tclearTimeout(playIconTimer);\n" +
            "     \t\tplayIcon.innerHTML = \"播放结束\";\n" +
            "     \t\tplayIcon.style.backgroundImage = \"url('./images/none.png')\";\n" +
            " \t\t\tplayIcon.style.display = \"block\";\n" +
            " \t\t\tmediaPlayer.stop();\n" +
            " \t\t\tbreak;\n" +
            "     \t}\n" +
            "\t}\n" +
            "\t\n" +
            "\tfunction playIconCtrl(){\n" +
            "\t\tplayIcon.style.display = \"none\";\n" +
            "\t}\n" +
            "\n" +
            "\tdocument.onkeypress = function(event) {\n" +
            "\t\n" +
            "\t\tvar keyCode = event.which ? event.which : event.keyCode;\n" +
            "\t\tconsole.log(\"keyCode = \"+ keyCode);\n" +
            "\t\tif (keyCode == 0x300) {\n" +
            "\t\t\thandleMediaEvent();\n" +
            "\t\t\treturn;\n" +
            "\t\t}\n" +
            "\t\tswitch (keyCode) {\n" +
            "\t\tcase 263:\n" +
            "\t\t\tmediaPlayer.pause();\n" +
            "\t\t\tbreak;\n" +
            "\t\tcase 8: \n" +
            "\t\tcase 270:\n" +
            "\t\t\tmediaPlayer.stop();\n" +
            "\t\t\tbreak;\n" +
            "\t\tcase 13:\n" +
            "\t\t\tmediaPlayer.resume();\n" +
            "\t\t\tbreak;\n" +
            "\t\tcase 259://vol+\n" +
            "\t\t\tvar volume = mediaPlayer.getVolume();\n" +
            "\t\t\tvolume = volume + 10;\n" +
            "\t\t\tif(volume > 100){\n" +
            "\t\t\t\tvolume = 100;\n" +
            "\t\t\t}\n" +
            "\t\t\tmediaPlayer.setVolume(volume);\n" +
            "\t\t\tbreak;\n" +
            "\t\tcase 260://vol-\n" +
            "\t\t\tvar volume = mediaPlayer.getVolume();\n" +
            "\t\t\tvolume = volume - 10;\n" +
            "\t\t\tif(volume < 0){\n" +
            "\t\t\t\tvolume = 0;\n" +
            "\t\t\t}\n" +
            "\t\t\tmediaPlayer.setVolume(volume);\n" +
            "\t\t\tbreak;\n" +
            "\t\tcase 48:\n" +
            "\t\t\tmediaPlayer.setVideoDisplayMode(0);\n" +
            "\t\t\tmediaPlayer.setVideoDisplayArea(200,200,800,600);\n" +
            "\t\t  mediaPlayer.refreshVideoDisplay();\n" +
            "\t\t  break;\n" +
            "\t\t case 49:\n" +
            "\t\t  mediaPlayer.setVideoDisplayMode(1);\n" +
            "\t\t\tmediaPlayer.setVideoDisplayArea(0,0,1280,720);\n" +
            "\t\t  mediaPlayer.refreshVideoDisplay();\n" +
            "\t\t  break;\n" +
            "\t\t\n" +
            "\t\t    \n" +
            "\t\t   \n" +
            "\t\t}\n" +
            "\t}\n" +
            "</script>\n" +
            "</head>\n" +
            "<body bgcolor=\"transparent\" style=\"position: absolute; width: 1280px; height: 720px;text-align: center;\" onload=\"init()\" onunload=\"exit()\">\n" +
            "\t<div id=\"playIcon\" style=\"position:absolute; top:232px;left:512px; width: 256px; height: 256px; display: none;color: white;font-size: 50px;\"></div>\n" +
            "</body>\n" +
            "</html>";

    //新疆播放文件
    public static final String XJ_PLAY = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            "<head>\n" +
            "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
            "<title>无标题文档</title>\n" +
            "</head>\n" +
            "\n" +
            "<body bgcolor=\"transparent\">\n" +
            "\t<div id=\"volume\" style=\"position:absolute; top:50px; left:50px; font-size:30px; color:#f0f;\"></div>\n" +
            "</body>\n" +
            "</html>\n" +
            "<script type=\"text/javascript\">\n" +
            "\tAuthentication.CTCSetConfig('UserToken','skyworth');\n" +
            "\tAuthentication.CTCSetConfig('ChannelCount','1');\n" +
            "\tAuthentication.CTCSetConfig('Channel','ChannelID=\"1\",ChannelName=\"Channel1\",UserChannelID=\"1\",ChannelURL=\"{{igmp}}\",TimeShift=\"1\",TimeShiftLength=\"3600\",ChannelSDP=\"rtsp://172.28.88.2:8554/mpeg2TransportStreamTest\",TimeShiftURL=\"rtsp://172.28.88.2:8554/mpeg2TransportStreamTest\",ChannelType=\"1\",IsHDChannel=\"2\",PreviewEnable=\"1\",ChannelPurchased=\"1\",ChannelLocked=\"0\",ChannelLogURL=\"\",PositionX=\"null\",PositionY=\"null\",BeginTime=\"null\",Interval=\"null\",Lasting=\"null\",ActionType=\"1\",FCCEnable=\"0\",ChannelFECPort=\"5139\"');\n" +
            "\n" +
            "\tvar mp = new MediaPlayer();\n" +
            "\tmp.setVideoDisplayMode(0);\n" +
            "\tmp.joinChannel(1);\n" +
            "\n" +
            "\tdocument.onkeypress = function(evt){\n" +
            "\t\tvar keyCode = evt.which;\n" +
            "\n" +
            "\t\tif(keyCode == 768){\n" +
            "\t\t\tvar event = Utility.getEvent();\n" +
            "\t\t\t//alert(event);\n" +
            "\t\t}\n" +
            "\n" +
            "\t\tif(keyCode == 38 || keyCode == 40){\n" +
            "\t\t\tmp.leaveChannel();\n" +
            "\t\t\tmp.joinChannel(1);\n" +
            "\t\t}else if(keyCode == 259){\n" +
            "\t\t\tvar volume = mp.getVolume();\n" +
            "\t\t\tvolume += 5;\n" +
            "\n" +
            "\t\t\tif(volume > 100){\n" +
            "\t\t\t\tvolume = 100;\n" +
            "\t\t\t}\n" +
            "\n" +
            "\t\t\tmp.setVolume(volume);\n" +
            "\n" +
            "\t\t\tdocument.getElementById(\"volume\").innerHTML = volume;\n" +
            "\t\t}else if(keyCode == 260){\n" +
            "\t\t\tvar volume = mp.getVolume();\n" +
            "\t\t\tvolume -= 5;\n" +
            "\n" +
            "\t\t\tif(volume < 0){\n" +
            "\t\t\t\tvolume = 0;\n" +
            "\t\t\t}\n" +
            "\n" +
            "\t\t\tmp.setVolume(volume);\n" +
            "\t\t\tdocument.getElementById(\"volume\").innerHTML = volume;\n" +
            "\t\t}\n" +
            "\t}\n" +
            "</script>";

    //天津默认播放地址
    public static final String DEFAULT_TJ_PLAY = "rtsp://120.205.32.36:554/live/ch15021114164639857244.sdp?playtype=1&boid=001&backupagent=120.205.32.36:554&clienttype=1&time=20170629180835+08&life=172800&ifpricereqsnd=1&vcdnid=001&userid=bjyjytest01&mediaid=ch15021114164639857244&ctype=2&TSTVTimeLife=7200&contname=&authid=0&terminalflag=1&UserLiveType=0&stbid=00100399010495101612208B37693FD2&nodelevel=3&AuthInfo=c16toKZT9TVQuYrbW7Tp1BoCyuHj6JCL89ArssAtIBakp%2Bb8WvV%2B67dls1zqXVfJUbDApxKtcEhO%0AQ%2FCaYFFVKw%3D%3D&bitrate=2000";

    //新疆默认播放地址
    public static final String DEFAULT_XJ_PATH = "igmp://239.10.0.211:1025";

}
