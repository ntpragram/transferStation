package com.ch.util;

import java.awt.Color;
import java.awt.Font;

import onbon.bx05.Bx5GEnv;
import onbon.bx05.Bx5GException;
import onbon.bx05.Bx5GScreen;
import onbon.bx05.Bx5GScreenClient;
import onbon.bx05.area.TextCaptionBxArea;
import onbon.bx05.area.page.TextBxPage;
import onbon.bx05.cmd.dyn7.DynamicBxAreaRule;
import onbon.bx05.file.BxFileWriterListener;
import onbon.bx05.file.ProgramBxFile;
import onbon.bx05.utils.DisplayStyleFactory;
import onbon.bx05.utils.TextBinary;

public class TrafficLightsUtil implements BxFileWriterListener<Bx5GScreen>{

    public static void main(String[] args) throws Exception {

        Bx5GEnv.initial("log.properties",30000);
        /*SendDynamic("192.168.1.12",5005,"12");
        SendDynamic("192.168.1.13",5005,"13");
        SendDynamic("192.168.1.14",5005,"14");
        SendDynamic("192.168.1.15",5005,"15");
        SendDynamic("192.168.1.16",5005,"16");
        SendDynamic("192.168.1.17",5005,"17");
        SendDynamic("192.168.1.18",5005,"18");
        SendDynamic("192.168.1.19",5005,"19");*/
        //SendDynamic("192.168.1.20",5005,"20");
        //SendDynamic("192.168.1.21",5005,"21");
       // SendDynamic("192.168.1.22",5005,"22");
   /*     trafficLightConection("192.168.1.203","12车请入场");
        trafficLightConection("192.168.1.204","123车请入场");
        trafficLightConection("192.168.1.205","1234车请入场");*/


    }


    public static void SendDynamic(String ip,int port,String text)throws Exception
    {
        DisplayStyleFactory.DisplayStyle[] styles = DisplayStyleFactory.getStyles().toArray(new DisplayStyleFactory.DisplayStyle[0]);
        Bx5GScreenClient screen = new Bx5GScreenClient( "MyScreen" );
        screen.connect(ip,port);
        // DynamicBxRule(id,runMode,immediatePlay,timeout);
        // runMode 运行模式
        // 0:循环显示
        // 1:显示完成后静止显示最后一页数据
        // 2:循环显示，超过设定时间后数据仍未更新时不再显示
        // 3:循环显示，超过设定时间后数据仍未更新时显示Logo信息
        // 4:循环显示，显示完最后一页后就不再显示
        // immediatePlay 是否立即播放
        // 0:与异步节目一起播放
        // 1:异步节目停止播放，仅播放动态区
        // 2:当播放完节目编号最高的异步节目后播放该动态区

        // BX-5E系列控制卡最高支持4个动态区，当屏幕上需要显示多个动态区时，动态区ID不可以相同
        // 定义一个动态区，此处动态区ID为0
        DynamicBxAreaRule rule = new DynamicBxAreaRule( 0,(byte)0,(byte)1 ,0);
        TextCaptionBxArea darea = new TextCaptionBxArea( 0,0,96,48,screen.getProfile() );
        TextBxPage dpage = new TextBxPage(text);
        dpage.setDisplayStyle( styles[2] );
        dpage.setFont( new Font( "宋体",Font.PLAIN,15 ) );
        darea.addPage( dpage );
        // 更新动态区
        screen.writeDynamic( rule,darea );
        screen.disconnect();
    }







	public static void trafficLightConection(String ip,String text) throws Exception {
		// TODO Auto-generated method stub
		// Bx5GEnv.initial();
        Bx5GEnv.initial("log.properties");

        //
        Bx5GScreenClient screen = new Bx5GScreenClient("MyScreen");
        if (!screen.connect(ip, 5005)) {
            System.out.println("connect failed");
        }
        
     // 下面开始创建第一个节目，P009
        // 此节目包括只包括一个图文区, 图文区中包括两个数据页：一页文本，一页图片
        // 显示节目边框
        // 区域显示边框

        // 创建节目文件
        ProgramBxFile program = new ProgramBxFile("P000", screen.getProfile());
        // 是否显示节目边框
        program.setFrameShow(false);
        // 节目边框的移动速度
        program.setFrameSpeed(20);
        // 使用第几个内置边框
        program.loadFrameImage(13);
        
        DynamicBxAreaRule dynRule = new DynamicBxAreaRule(0, (byte) 4, (byte) 1, 0);

		// 创建一个文本区
        // 分别输入其X,Y,W,H
        // 屏幕左上角坐标为 (0, 0)
        // 注意区域坐标和宽高，不要越界
//        TextCaptionBxArea tArea = new TextCaptionBxArea(0, 0, 180, 50, screen.getProfile());
        TextCaptionBxArea tArea = new TextCaptionBxArea(0, 0, 85, 50, screen.getProfile());
        // 使能区域边框
        tArea.setFrameShow(false);
        // 使用内置边框3
        tArea.loadFrameImage(3);

        // 创建一个数据页，并希望显示 “P009” 这几个文字
        //社会主义核心价值观:富强、民主、文明、和谐、自由、平等、公正、法治、爱国、敬业、诚信、友善
        TextBxPage page = new TextBxPage(text);
        
        // 对文本的处理是否自动换行
        page.setLineBreak(true);
        // 设置文本水平对齐方式
        page.setHorizontalAlignment(TextBinary.Alignment.NEAR);
        // 设置文本垂直居中方式
        page.setVerticalAlignment(TextBinary.Alignment.CENTER);
     // 设置文本字体
//        page.setFont(new Font("宋体", Font.PLAIN, 13));
        page.setFont(new Font("宋体", Font.PLAIN, 15));
        // 设置文本颜色
        page.setForeground(Color.red);
        // 设置区域背景色，默认为黑色
        page.setBackground(Color.darkGray);

        // 调整特技速度
        page.setSpeed(50);
        //
        page.setHeadTailInterval(-2);
        // 调整停留时间, 单位 10ms
        page.setStayTime(1000);

        // 将前面创建的 page 添加到 area 中
        tArea.addPage(page);
        
        //
        program.addArea(tArea);
        //
        screen.writeProgramAsync(program, new TrafficLightsUtil());
        Thread.sleep(5000);

        //
        screen.readProgramList();

        screen.disconnect();
	}


    public static void trafficLightConection4(String ip,String text) throws Exception {
        // TODO Auto-generated method stub
        // Bx5GEnv.initial();
        Bx5GEnv.initial("log.properties");

        //
        Bx5GScreenClient screen = new Bx5GScreenClient("MyScreen");
        if (!screen.connect("192.168.1.205", 5005)) {
            System.out.println("connect failed");
        }

        // 下面开始创建第一个节目，P009
        // 此节目包括只包括一个图文区, 图文区中包括两个数据页：一页文本，一页图片
        // 显示节目边框
        // 区域显示边框

        // 创建节目文件
        ProgramBxFile program = new ProgramBxFile("P000", screen.getProfile());
        // 是否显示节目边框
        program.setFrameShow(false);
        // 节目边框的移动速度
        program.setFrameSpeed(20);
        // 使用第几个内置边框
        program.loadFrameImage(13);

        DynamicBxAreaRule dynRule = new DynamicBxAreaRule(0, (byte) 4, (byte) 1, 0);

        // 创建一个文本区
        // 分别输入其X,Y,W,H
        // 屏幕左上角坐标为 (0, 0)
        // 注意区域坐标和宽高，不要越界
//        TextCaptionBxArea tArea = new TextCaptionBxArea(0, 0, 180, 50, screen.getProfile());
        TextCaptionBxArea tArea = new TextCaptionBxArea(0, 0, 85, 50, screen.getProfile());
        // 使能区域边框
        tArea.setFrameShow(false);
        // 使用内置边框3
        tArea.loadFrameImage(3);

        // 创建一个数据页，并希望显示 “P009” 这几个文字
        //社会主义核心价值观:富强、民主、文明、和谐、自由、平等、公正、法治、爱国、敬业、诚信、友善
        TextBxPage page = new TextBxPage(text);

        // 对文本的处理是否自动换行
        page.setLineBreak(true);
        // 设置文本水平对齐方式
        page.setHorizontalAlignment(TextBinary.Alignment.NEAR);
        // 设置文本垂直居中方式
        page.setVerticalAlignment(TextBinary.Alignment.CENTER);
        // 设置文本字体
//        page.setFont(new Font("宋体", Font.PLAIN, 13));
        page.setFont(new Font("宋体", Font.PLAIN, 15));
        // 设置文本颜色
        page.setForeground(Color.red);
        // 设置区域背景色，默认为黑色
        page.setBackground(Color.darkGray);

        // 调整特技速度
        page.setSpeed(50);
        //
        page.setHeadTailInterval(-2);
        // 调整停留时间, 单位 10ms
        page.setStayTime(1000);

        // 将前面创建的 page 添加到 area 中
        tArea.addPage(page);

        //
        program.addArea(tArea);
        //
        screen.writeProgramAsync(program, new TrafficLightsUtil());
        Thread.sleep(5000);

        //
        screen.readProgramList();

        screen.disconnect();
    }

    public static void trafficLightConection3(String ip,String text) throws Exception {
        // TODO Auto-generated method stub
        // Bx5GEnv.initial();
        Bx5GEnv.initial("log.properties");

        //
        Bx5GScreenClient screen = new Bx5GScreenClient("MyScreen");
        if (!screen.connect("192.168.1.204", 5005)) {
            System.out.println("connect failed");
        }

        // 下面开始创建第一个节目，P009
        // 此节目包括只包括一个图文区, 图文区中包括两个数据页：一页文本，一页图片
        // 显示节目边框
        // 区域显示边框

        // 创建节目文件
        ProgramBxFile program = new ProgramBxFile("P000", screen.getProfile());
        // 是否显示节目边框
        program.setFrameShow(false);
        // 节目边框的移动速度
        program.setFrameSpeed(20);
        // 使用第几个内置边框
        program.loadFrameImage(13);

        DynamicBxAreaRule dynRule = new DynamicBxAreaRule(0, (byte) 4, (byte) 1, 0);

        // 创建一个文本区
        // 分别输入其X,Y,W,H
        // 屏幕左上角坐标为 (0, 0)
        // 注意区域坐标和宽高，不要越界
//        TextCaptionBxArea tArea = new TextCaptionBxArea(0, 0, 180, 50, screen.getProfile());
        TextCaptionBxArea tArea = new TextCaptionBxArea(0, 0, 85, 50, screen.getProfile());
        // 使能区域边框
        tArea.setFrameShow(false);
        // 使用内置边框3
        tArea.loadFrameImage(3);

        // 创建一个数据页，并希望显示 “P009” 这几个文字
        //社会主义核心价值观:富强、民主、文明、和谐、自由、平等、公正、法治、爱国、敬业、诚信、友善
        TextBxPage page = new TextBxPage(text);

        // 对文本的处理是否自动换行
        page.setLineBreak(true);
        // 设置文本水平对齐方式
        page.setHorizontalAlignment(TextBinary.Alignment.NEAR);
        // 设置文本垂直居中方式
        page.setVerticalAlignment(TextBinary.Alignment.CENTER);
        // 设置文本字体
//        page.setFont(new Font("宋体", Font.PLAIN, 13));
        page.setFont(new Font("宋体", Font.PLAIN, 15));
        // 设置文本颜色
        page.setForeground(Color.red);
        // 设置区域背景色，默认为黑色
        page.setBackground(Color.darkGray);

        // 调整特技速度
        page.setSpeed(50);
        //
        page.setHeadTailInterval(-2);
        // 调整停留时间, 单位 10ms
        page.setStayTime(1000);

        // 将前面创建的 page 添加到 area 中
        tArea.addPage(page);

        //
        program.addArea(tArea);
        //
        screen.writeProgramAsync(program, new TrafficLightsUtil());
        Thread.sleep(5000);

        //
        screen.readProgramList();

        screen.disconnect();
    }

    public static void trafficLightConection2(String ip,String text) throws Exception {
        // TODO Auto-generated method stub
        // Bx5GEnv.initial();
        Bx5GEnv.initial("log.properties");

        //
        Bx5GScreenClient screen = new Bx5GScreenClient("MyScreen");
        if (!screen.connect("192.168.1.203", 5005)) {
            System.out.println("connect failed");
        }

        // 下面开始创建第一个节目，P009
        // 此节目包括只包括一个图文区, 图文区中包括两个数据页：一页文本，一页图片
        // 显示节目边框
        // 区域显示边框

        // 创建节目文件
        ProgramBxFile program = new ProgramBxFile("P000", screen.getProfile());
        // 是否显示节目边框
        program.setFrameShow(false);
        // 节目边框的移动速度
        program.setFrameSpeed(20);
        // 使用第几个内置边框
        program.loadFrameImage(13);

        DynamicBxAreaRule dynRule = new DynamicBxAreaRule(0, (byte) 4, (byte) 1, 0);

        // 创建一个文本区
        // 分别输入其X,Y,W,H
        // 屏幕左上角坐标为 (0, 0)
        // 注意区域坐标和宽高，不要越界
//        TextCaptionBxArea tArea = new TextCaptionBxArea(0, 0, 180, 50, screen.getProfile());
        TextCaptionBxArea tArea = new TextCaptionBxArea(0, 0, 85, 50, screen.getProfile());
        // 使能区域边框
        tArea.setFrameShow(false);
        // 使用内置边框3
        tArea.loadFrameImage(3);

        // 创建一个数据页，并希望显示 “P009” 这几个文字
        //社会主义核心价值观:富强、民主、文明、和谐、自由、平等、公正、法治、爱国、敬业、诚信、友善
        TextBxPage page = new TextBxPage(text);

        // 对文本的处理是否自动换行
        page.setLineBreak(true);
        // 设置文本水平对齐方式
        page.setHorizontalAlignment(TextBinary.Alignment.NEAR);
        // 设置文本垂直居中方式
        page.setVerticalAlignment(TextBinary.Alignment.CENTER);
        // 设置文本字体
//        page.setFont(new Font("宋体", Font.PLAIN, 13));
        page.setFont(new Font("宋体", Font.PLAIN, 15));
        // 设置文本颜色
        page.setForeground(Color.red);
        // 设置区域背景色，默认为黑色
        page.setBackground(Color.darkGray);

        // 调整特技速度
        page.setSpeed(50);
        //
        page.setHeadTailInterval(-2);
        // 调整停留时间, 单位 10ms
        page.setStayTime(1000);

        // 将前面创建的 page 添加到 area 中
        tArea.addPage(page);

        //
        program.addArea(tArea);
        //
        screen.writeProgramAsync(program, new TrafficLightsUtil());
        Thread.sleep(5000);

        //
        screen.readProgramList();

        screen.disconnect();
    }


    public static void trafficLightConection1(String ip,String text) throws Exception {
        // TODO Auto-generated method stub
        // Bx5GEnv.initial();
        Bx5GEnv.initial("log.properties");

        //
        Bx5GScreenClient screen = new Bx5GScreenClient("MyScreen");
        if (!screen.connect("192.168.1.202", 5005)) {
            System.out.println("connect failed");
        }

        // 下面开始创建第一个节目，P009
        // 此节目包括只包括一个图文区, 图文区中包括两个数据页：一页文本，一页图片
        // 显示节目边框
        // 区域显示边框

        // 创建节目文件
        ProgramBxFile program = new ProgramBxFile("P000", screen.getProfile());
        // 是否显示节目边框
        program.setFrameShow(false);
        // 节目边框的移动速度
        program.setFrameSpeed(20);
        // 使用第几个内置边框
        program.loadFrameImage(13);

        DynamicBxAreaRule dynRule = new DynamicBxAreaRule(0, (byte) 4, (byte) 1, 0);

        // 创建一个文本区
        // 分别输入其X,Y,W,H
        // 屏幕左上角坐标为 (0, 0)
        // 注意区域坐标和宽高，不要越界
//        TextCaptionBxArea tArea = new TextCaptionBxArea(0, 0, 180, 50, screen.getProfile());
        TextCaptionBxArea tArea = new TextCaptionBxArea(0, 0, 85, 50, screen.getProfile());
        // 使能区域边框
        tArea.setFrameShow(false);
        // 使用内置边框3
        tArea.loadFrameImage(3);

        // 创建一个数据页，并希望显示 “P009” 这几个文字
        //社会主义核心价值观:富强、民主、文明、和谐、自由、平等、公正、法治、爱国、敬业、诚信、友善
        TextBxPage page = new TextBxPage(text);

        // 对文本的处理是否自动换行
        page.setLineBreak(true);
        // 设置文本水平对齐方式
        page.setHorizontalAlignment(TextBinary.Alignment.NEAR);
        // 设置文本垂直居中方式
        page.setVerticalAlignment(TextBinary.Alignment.CENTER);
        // 设置文本字体
//        page.setFont(new Font("宋体", Font.PLAIN, 13));
        page.setFont(new Font("宋体", Font.PLAIN, 15));
        // 设置文本颜色
        page.setForeground(Color.red);
        // 设置区域背景色，默认为黑色
        page.setBackground(Color.darkGray);

        // 调整特技速度
        page.setSpeed(50);
        //
        page.setHeadTailInterval(-2);
        // 调整停留时间, 单位 10ms
        page.setStayTime(1000);

        // 将前面创建的 page 添加到 area 中
        tArea.addPage(page);

        //
        program.addArea(tArea);
        //
        screen.writeProgramAsync(program, new TrafficLightsUtil());
        Thread.sleep(5000);

        //
        screen.readProgramList();

        screen.disconnect();
    }

	@Override
	public void cancel(Bx5GScreen arg0, String arg1, Bx5GException arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void done(Bx5GScreen arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fileFinish(Bx5GScreen arg0, String arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fileWriting(Bx5GScreen arg0, String arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void progressChanged(Bx5GScreen arg0, String arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	

}
