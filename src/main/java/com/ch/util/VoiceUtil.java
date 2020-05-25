package com.ch.util;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class VoiceUtil {


    public static void voicePlayBack(String voice){
        ActiveXComponent sap = new ActiveXComponent("Sapi.SpVoice");
        try {
// 音量 0-100
            sap.setProperty("Volume", new Variant(100));
// 语音朗读速度 -10 到 +10
            sap.setProperty("Rate", new Variant(-2));
// 获取执行对象
            Dispatch sapo = sap.getObject();
// 执行朗读
            for (int i =0;i<3;i++) {
                Dispatch.call(sapo, "Speak", new Variant(voice));
            }
// 关闭执行对象
            sapo.safeRelease();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
// 关闭应用程序连接
            sap.safeRelease();
        }
    }

    public static void main(String[] args) {
        voicePlayBack("苏FE8001号车请入场");
    }
}
