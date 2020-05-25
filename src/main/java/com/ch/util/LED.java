package com.ch.util;

import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;
import onbon.bx05.Bx5GEnv;
import onbon.bx05.Bx5GException;
import onbon.bx05.Bx5GScreen;
import onbon.bx05.Bx5GScreenClient;
import onbon.bx05.area.DateStyle;
import onbon.bx05.area.DateTimeBxArea;
import onbon.bx05.area.TextCaptionBxArea;
import onbon.bx05.area.TimeStyle;
import onbon.bx05.area.WeekStyle;
import onbon.bx05.area.page.TextBxPage;
import onbon.bx05.cmd.dyn7.DynamicBxAreaRule;
import onbon.bx05.file.BxFileWriterListener;
import onbon.bx05.file.ProgramBxFile;
import onbon.bx05.utils.DisplayStyleFactory;
import onbon.bx05.utils.DisplayStyleFactory.DisplayStyle;

public class LED implements BxFileWriterListener<Bx5GScreen>{
    
    
 // 动态区和节目一起播放
    public static void SendDynamicProgam(String text)throws Exception
    {
    	
        DisplayStyle[] styles = DisplayStyleFactory.getStyles().toArray(new DisplayStyle[0]);

        Bx5GScreenClient screen = new Bx5GScreenClient( "MyScreen" );

        screen.connect( "192.168.1.31",5005 );
    
        ProgramBxFile pf = new ProgramBxFile( "P000",screen.getProfile() );
        // 创建一个时间区
        DateTimeBxArea dtArea = new DateTimeBxArea( 0,0,160,32,screen.getProfile() );
        // 设定时间区多行显示
        dtArea.setMultiline( false );
        
        // 设定日期显示格式NULL表示不显示日期
        dtArea.setDateStyle( DateStyle.YYYY_MM_DD_2);
                
        // 设定时间显示格式NULL表示不显示时间     
        dtArea.setTimeStyle( TimeStyle.HH_MM_SS_2 );
        
        // 设定星期显示格式NULL表示不显示星期
        dtArea.setWeekStyle( WeekStyle.CHINESE );

        dtArea.setFont( new Font("宋体",Font.PLAIN,58) );

        pf.addArea( dtArea );      

        screen.writeProgram( pf );
     
        DynamicBxAreaRule rule = new DynamicBxAreaRule( 0,(byte)0,(byte)0,0 );

        TextCaptionBxArea darea = new TextCaptionBxArea( 150,64,500,800,screen.getProfile() );

        TextBxPage dpage = new TextBxPage( text );
        
        dpage.setFont( new Font("宋体",Font.PLAIN,42));
      
        darea.addPage( dpage );
        
        dpage.setDisplayStyle( styles[2] );
        
        screen.writeDynamic( rule,darea );

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
