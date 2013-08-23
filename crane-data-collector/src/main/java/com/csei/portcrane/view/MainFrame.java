package com.csei.portcrane.view;

import javax.swing.*;
import java.awt.*;

public class MainFrame {

    public static void main(String[] args){
        JFrame jf = new JFrame();
        ConfigPanel sm = new ConfigPanel();
        jf.add(sm.$$$getRootComponent$$$());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        jf.setSize(700,400);
        jf.setResizable(false);
        jf.setLocation((screenSize.width - jf.getWidth()) / 2, (screenSize.height - jf.getHeight()) / 2);
        jf.setTitle("港机数据采集服务配置");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }
}
