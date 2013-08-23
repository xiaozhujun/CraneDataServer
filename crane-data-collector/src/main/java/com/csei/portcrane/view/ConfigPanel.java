package com.csei.portcrane.view;

import com.csei.portcrane.server.DataAcquisitionServer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class ConfigPanel {
    private JPanel container;
    private JButton serverSaveBtn;
    private JButton serverStartBtn;
    private JButton serverStopBtn;
    private JTextField serverIpTxt;
    private JTextField serverPortTxt;
    private JTextField serverIntervalTxt;
    private JLabel serverFileLabel;
    private JLabel serverIpLabel;
    private JLabel serverPortLabel;
    private JLabel serverIntervalLabel;
    private JLabel redisFileLabel;
    private JLabel redisIpLabel;
    private JTextField redisIpTxt;
    private JLabel redisPortLabel;
    private JTextField redisPortTxt;
    private JLabel redisExpireLabel;
    private JTextField redisExpireTxt;
    private JPanel serverContainer;
    private JPanel redisContainer;
    private JPanel serverConfigPanel;
    private JPanel serverControlPanel;
    private JPanel redisConfigPanel;
    private JTextField serverFileTxt;
    private JButton serverFileOpener;
    private JTextField redisFileTxt;
    private JButton redisFileOpener;
    private JPanel redisControlPanel;
    private JButton redisSaveBtn;
    private JButton redisStartBtn;
    private JButton redisStopBtn;

    public ConfigPanel() {
        serverFileOpener.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int flag = jfc.showOpenDialog(null);
                if(flag==JFileChooser.APPROVE_OPTION){
                    File file = jfc.getSelectedFile();
                    Map<String,String> map = null;
                    try {
                        map = new PropertyFileOperator().read(file);
                    } catch (IOException e) {
                    }
                    serverIpTxt.setText(map.get("server.ip"));
                    serverPortTxt.setText(map.get("server.port"));
                    serverIntervalTxt.setText(map.get("server.interval"));
                    serverFileTxt.setText(file.getAbsolutePath());
                }
            }
        });
        serverSaveBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                File file =null;
                if("".equals(serverFileTxt.getText())){
                    JFileChooser jfc = new JFileChooser();
                    jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    int flag = jfc.showSaveDialog(null);
                    if(flag ==JFileChooser.APPROVE_OPTION){
                        file = jfc.getSelectedFile();
                        serverFileTxt.setText(file.getAbsolutePath());
                    }
                }else{
                    file = new File(serverFileTxt.getText());
                }
                Map<String,String> map = new HashMap<String,String>();
                map.put("server.ip",serverIpTxt.getText());
                map.put("server.port",serverPortTxt.getText());
                map.put("server.interval",serverIntervalTxt.getText());
                try {
                    new PropertyFileOperator().write(map,file);
                } catch (IOException e) {
                }
            }
        });
        serverStartBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                InetAddress address = null;
                try {
                    address = InetAddress.getByName("127.0.0.1");
                } catch (UnknownHostException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                DataAcquisitionServer server = null;
                try {
                    server = new DataAcquisitionServer(address);
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                new Thread(server).start();
            }
        });
        serverStopBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
            }
        });
        redisFileOpener.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int flag = jfc.showOpenDialog(null);
                if(flag==JFileChooser.APPROVE_OPTION){
                    File file = jfc.getSelectedFile();
                    Map<String,String> map = null;
                    try {
                        map = new PropertyFileOperator().read(file);
                    } catch (IOException e) {
                    }
                    redisIpTxt.setText(map.get("redis.ip"));
                    redisPortTxt.setText(map.get("redis.port"));
                    redisExpireTxt.setText(map.get("redis.expire"));
                    redisFileTxt.setText(file.getAbsolutePath());
                }
            }
        });
        redisSaveBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                File file =null;
                if("".equals(redisFileTxt.getText())){
                    JFileChooser jfc = new JFileChooser();
                    jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    int flag = jfc.showSaveDialog(null);
                    if(flag ==JFileChooser.APPROVE_OPTION){
                        file = jfc.getSelectedFile();
                        redisFileTxt.setText(file.getAbsolutePath());
                    }
                }else{
                    file = new File(redisFileTxt.getText());
                }
                Map<String,String> map = new HashMap<String,String>();
                map.put("redis.ip",redisIpTxt.getText());
                map.put("redis.port",redisPortTxt.getText());
                map.put("redis.expire",redisExpireTxt.getText());
                try {
                    new PropertyFileOperator().write(map,file);
                } catch (IOException e) {
                }
            }
        });
        redisStopBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
            }
        });
        redisStartBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
            }
        });
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        container = new JPanel();
        container.setLayout(new FormLayout("fill:335px:noGrow,left:4dlu:noGrow,fill:334px:noGrow", "center:d:grow"));
        container.setBorder(BorderFactory.createTitledBorder(null, "港机数据采集服务配置", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font(container.getFont().getName(), container.getFont().getStyle(), container.getFont().getSize()), new Color(-16777216)));
        serverContainer = new JPanel();
        serverContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        CellConstraints cc = new CellConstraints();
        container.add(serverContainer, cc.xy(1, 1, CellConstraints.FILL, CellConstraints.FILL));
        serverContainer.setBorder(BorderFactory.createTitledBorder("服务器配置"));
        serverConfigPanel = new JPanel();
        serverConfigPanel.setLayout(new FormLayout("fill:80px:noGrow,left:4dlu:noGrow,fill:225px:grow", "center:40px:noGrow,top:4dlu:noGrow,center:40px:noGrow,top:4dlu:noGrow,center:40px:noGrow,top:4dlu:noGrow,center:40px:noGrow"));
        serverContainer.add(serverConfigPanel);
        serverIpLabel = new JLabel();
        serverIpLabel.setText("IP地址");
        serverConfigPanel.add(serverIpLabel, cc.xy(1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
        serverIpTxt = new JTextField();
        serverConfigPanel.add(serverIpTxt, cc.xy(3, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        serverPortLabel = new JLabel();
        serverPortLabel.setText("端口");
        serverConfigPanel.add(serverPortLabel, cc.xy(1, 3, CellConstraints.CENTER, CellConstraints.DEFAULT));
        serverPortTxt = new JTextField();
        serverConfigPanel.add(serverPortTxt, cc.xy(3, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        serverIntervalLabel = new JLabel();
        serverIntervalLabel.setText("监听间隔");
        serverConfigPanel.add(serverIntervalLabel, cc.xy(1, 5, CellConstraints.CENTER, CellConstraints.DEFAULT));
        serverIntervalTxt = new JTextField();
        serverConfigPanel.add(serverIntervalTxt, cc.xy(3, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        serverFileLabel = new JLabel();
        serverFileLabel.setText("配置文件路径");
        serverConfigPanel.add(serverFileLabel, cc.xy(1, 7, CellConstraints.CENTER, CellConstraints.DEFAULT));
        serverFileTxt = new JTextField();
        serverConfigPanel.add(serverFileTxt, cc.xy(3, 7, CellConstraints.FILL, CellConstraints.DEFAULT));
        serverControlPanel = new JPanel();
        serverControlPanel.setLayout(new FormLayout("fill:d:grow,left:4dlu:noGrow,fill:150px:noGrow,left:4dlu:noGrow,fill:150px:grow", "center:d:grow,top:4dlu:noGrow,center:42px:grow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        serverContainer.add(serverControlPanel);
        serverSaveBtn = new JButton();
        serverSaveBtn.setText("保存");
        serverControlPanel.add(serverSaveBtn, cc.xy(5, 3));
        serverFileOpener = new JButton();
        serverFileOpener.setText("打开");
        serverControlPanel.add(serverFileOpener, cc.xy(3, 3));
        serverStartBtn = new JButton();
        serverStartBtn.setText("启动服务器");
        serverControlPanel.add(serverStartBtn, cc.xy(3, 5));
        serverStopBtn = new JButton();
        serverStopBtn.setText("停止服务器");
        serverControlPanel.add(serverStopBtn, cc.xy(5, 5));
        redisContainer = new JPanel();
        redisContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        container.add(redisContainer, cc.xy(3, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
        redisContainer.setBorder(BorderFactory.createTitledBorder("Redis配置"));
        redisConfigPanel = new JPanel();
        redisConfigPanel.setLayout(new FormLayout("fill:83px:noGrow,left:4dlu:noGrow,fill:221px:grow", "center:40px:noGrow,top:4dlu:noGrow,center:40px:noGrow,top:4dlu:noGrow,center:40px:noGrow,top:4dlu:noGrow,center:40px:noGrow"));
        redisContainer.add(redisConfigPanel);
        redisIpLabel = new JLabel();
        redisIpLabel.setText("IP地址");
        redisConfigPanel.add(redisIpLabel, cc.xy(1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
        redisIpTxt = new JTextField();
        redisConfigPanel.add(redisIpTxt, cc.xy(3, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        redisPortLabel = new JLabel();
        redisPortLabel.setText("端口");
        redisConfigPanel.add(redisPortLabel, cc.xy(1, 3, CellConstraints.CENTER, CellConstraints.DEFAULT));
        redisPortTxt = new JTextField();
        redisConfigPanel.add(redisPortTxt, cc.xy(3, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        redisExpireLabel = new JLabel();
        redisExpireLabel.setText("过期时间");
        redisConfigPanel.add(redisExpireLabel, cc.xy(1, 5, CellConstraints.CENTER, CellConstraints.DEFAULT));
        redisExpireTxt = new JTextField();
        redisConfigPanel.add(redisExpireTxt, cc.xy(3, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        redisFileLabel = new JLabel();
        redisFileLabel.setText("配置文件路径");
        redisConfigPanel.add(redisFileLabel, cc.xy(1, 7, CellConstraints.CENTER, CellConstraints.DEFAULT));
        redisFileTxt = new JTextField();
        redisConfigPanel.add(redisFileTxt, cc.xy(3, 7, CellConstraints.FILL, CellConstraints.DEFAULT));
        redisControlPanel = new JPanel();
        redisControlPanel.setLayout(new FormLayout("fill:d:grow,left:4dlu:noGrow,fill:150px:noGrow,left:4dlu:noGrow,fill:150px:grow", "center:d:grow,top:4dlu:noGrow,center:42px:grow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        redisContainer.add(redisControlPanel);
        redisSaveBtn = new JButton();
        redisSaveBtn.setText("保存");
        redisControlPanel.add(redisSaveBtn, cc.xy(5, 3));
        redisFileOpener = new JButton();
        redisFileOpener.setText("打开");
        redisControlPanel.add(redisFileOpener, cc.xy(3, 3));
        redisStartBtn = new JButton();
        redisStartBtn.setText("启动服务器");
        redisControlPanel.add(redisStartBtn, cc.xy(3, 5));
        redisStopBtn = new JButton();
        redisStopBtn.setText("停止服务器");
        redisControlPanel.add(redisStopBtn, cc.xy(5, 5));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return container;
    }
}
