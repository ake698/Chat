package client;

import Server.UserLinkList;

import java.awt.*;
import java.awt.event.*; 
import javax.swing.*; 
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * 聊天客户端的主框架类
 */
public class ChatClient extends JFrame implements ActionListener{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    String ip = "127.0.0.1"; //连接到服务端的ip地址
    int port = 8888;//连接到服务端的端口号
    String userName = "client";//用户名
    int type = 0;//0表示未连接，1表示已连接
   
    JComboBox<String> combobox;//选择发送消息的接受者
    JTextArea messageShow;//客户端的信息显示
    JScrollPane messageScrollPane;//信息显示的滚动条
    JLabel express,sendToLabel,messageLabel ;
    JTextField clientMessage;//客户端消息的发送
    JCheckBox checkbox;//悄悄话
    JComboBox<String> actionlist;//表情选择
    JButton clientMessageButton;//发送消息
    JTextField showStatus;//显示用户连接状态
    Socket socket;
    ObjectOutputStream output;//网络套接字输出流
    ObjectInputStream input;//网络套接字输入流
    ClientReceive recvThread;
    //建立菜单栏
    JMenuBar jMenuBar = new JMenuBar();
//建立菜单组 

    JMenu operateMenu = new JMenu ("操作");
    //建立菜单项
    JMenuItem loginItem = new JMenuItem ("用户登录");
    JMenuItem logoffItem = new JMenuItem ("用户注销");
    JMenuItem exitItem=new JMenuItem ("退出");
    JMenu conMenu=new JMenu ("设置");
    JMenuItem userItem=new JMenuItem ("用户设置");
    JMenuItem connectItem=new JMenuItem ("连接设置");
    JMenu helpMenu=new JMenu ("帮助");
    JMenuItem helpItem=new JMenuItem ("帮助");
    //建立工具栏
    JToolBar toolBar = new JToolBar();
    //建立工具栏中的按钮组件
    JButton loginButton;//用户登录
    JButton logoffButton;//用户注销
    JButton userButton;//用户信息的设置
    JButton connectButton;//连接设置
    JButton clearButton; //清屏按钮
    JButton exitButton;//退出按钮

    //框架的大小
    Dimension faceSize = new Dimension(400, 600);
    JPanel downPanel ;
    GridBagConstraints girdBagCon;
    public ChatClient(){
        init();//初始化程序
//添加框架的关闭事件处理 
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
//设置框架的大小 
        this.setSize(faceSize);
//设置运行时窗口的位置 
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation( (int) (screenSize.width - faceSize.getWidth()) / 2,
                (int) (screenSize.height - faceSize.getHeight()) / 2);
        this.setResizable(false);
        this.setTitle("大学生在线激情聊天-客户端"); //设置标题

        this.setVisible(true);;

    }
    /**
     * 程序初始化函数
     */
    public void init(){
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
//添加菜单栏 
        operateMenu.add (loginItem);
        operateMenu.add (logoffItem);
        operateMenu.add (exitItem);
        jMenuBar.add (operateMenu);
        conMenu.add (userItem);
        conMenu.add (connectItem);
        jMenuBar.add (conMenu);
        helpMenu.add (helpItem);
        jMenuBar.add (helpMenu);
        setJMenuBar (jMenuBar);
//初始化按钮 
        loginButton = new JButton("登录");
        logoffButton = new JButton("注销");
        userButton = new JButton("用户设置" );
        connectButton = new JButton("连接设置" );
        clearButton = new JButton("清屏");
        exitButton = new JButton("退出" );

//当鼠标放上显示信息
        clearButton.setToolTipText("清理屏幕中的聊天内容");
        loginButton.setToolTipText("连接到指定的服务器");
        logoffButton.setToolTipText("与服务器断开连接");
        userButton.setToolTipText("设置用户信息");
        connectButton.setToolTipText("设置所要连接到的服务器信息");
//将按钮添加到工具栏 
        toolBar.add(userButton);
        toolBar.add(connectButton);
        toolBar.addSeparator();//添加分隔栏
        toolBar.add(loginButton);
        toolBar.add(logoffButton);
        toolBar.addSeparator();//添加分隔栏
        toolBar.add(clearButton);
        toolBar.addSeparator();//添加分隔栏
        toolBar.add(exitButton);
        contentPane.add(toolBar,BorderLayout.NORTH);
        checkbox = new JCheckBox("悄悄话");
        checkbox.setSelected(false);
        actionlist = new JComboBox<String>();
        actionlist.addItem("微笑地");
        actionlist.addItem("高兴地");
        actionlist.addItem("轻轻地");
        actionlist.addItem("生气地");
        actionlist.addItem("小心地");
        actionlist.addItem("静静地");
        actionlist.setSelectedIndex(0);
//初始时 
        loginButton.setEnabled(true);
        logoffButton.setEnabled(false);
//为菜单栏添加事件监听 
        loginItem.addActionListener(this);
        logoffItem.addActionListener(this);
        exitItem.addActionListener(this);
        userItem.addActionListener(this);
        connectItem.addActionListener(this);
        helpItem.addActionListener(this);
//添加按钮的事件侦听 
        loginButton.addActionListener(this);
        logoffButton.addActionListener(this);
        userButton.addActionListener(this);
        connectButton.addActionListener(this);
        clearButton.addActionListener(this);
        exitButton.addActionListener(this);
        combobox = new JComboBox<String>();
        combobox.insertItemAt("所有人",0);
        combobox.setSelectedIndex(0);
        messageShow = new JTextArea();
        messageShow.setEditable(false);
//添加滚动条 
        messageScrollPane = new JScrollPane(messageShow,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        messageScrollPane.setPreferredSize(new Dimension(400,400));
        messageScrollPane.revalidate();
        clientMessage = new JTextField(23);
        clientMessage.setEnabled(false);
        clientMessageButton = new JButton();
        clientMessageButton.setText("发送");
//添加系统消息的事件侦听 
        clientMessage.addActionListener(this);
        clientMessageButton.addActionListener(this);
        sendToLabel = new JLabel("发送至:");
        express = new JLabel(" 表情: ");
        messageLabel = new JLabel("发送消息:");
        downPanel = new JPanel();
        downPanel.setPreferredSize(new Dimension(400,100));
        downPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JLabel none = new JLabel(" ");
        downPanel.add(none);
        downPanel.add(sendToLabel);
        downPanel.add(combobox);
        downPanel.add(express);
        downPanel.add(actionlist);
        downPanel.add(checkbox);
        downPanel.add(messageLabel);
        downPanel.add(clientMessage);
        downPanel.add(clientMessageButton);
        showStatus = new JTextField(35);
        showStatus.setEditable(false);
        
        downPanel.add(showStatus);
        contentPane.add(messageScrollPane,BorderLayout.CENTER);
        contentPane.add(downPanel,BorderLayout.SOUTH);
//关闭程序时的操作 
        this.addWindowListener(
                new WindowAdapter(){
                    public void windowClosing(WindowEvent e){
                        if(type == 1){
                            DisConnect();
                        }
                        System.exit(0);
                    }
                }
        );
    }
    /**
     * 事件处理
     */
    public void actionPerformed(ActionEvent e) {
    	// 通过getSource是判断这个事件是由哪个组件发出的,返回的是object对象
        Object obj = e.getSource();
        if (obj == userItem || obj == userButton) { //用户信息设置
//调出用户信息设置对话框 
            UserConf userConf = new UserConf(this,userName);
            userConf.setVisible(true);;
            userName = userConf.userInputName;
        }
        else if (obj == connectItem || obj == connectButton) { //连接服务端设置
//调出连接设置对话框 
            ConnectConf conConf = new ConnectConf(this,ip,port);
            conConf.setVisible(true);;
            ip = conConf.userInputIp;
            port = conConf.userInputPort;
        }
        else if (obj == loginItem || obj == loginButton) { //登录
            Connect();
        }
        else if (obj == logoffItem || obj == logoffButton) { //注销
            DisConnect();
            showStatus.setText("");
        }
        else if (obj == clientMessage || obj == clientMessageButton) { //发送消息
            SendMessage();
            clientMessage.setText("");
        }
        else if(obj == clearButton){ //清屏
            messageShow.setText("");
        }

        else if (obj == exitButton || obj == exitItem) { //退出
            int j=JOptionPane.showConfirmDialog(
                    this,"真的要退出吗?","退出",
                    JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE);
            if (j == JOptionPane.YES_OPTION){
                if(type == 1){
                    DisConnect();
                }
                System.exit(0);
            }
        }
        else if (obj == helpItem) { //菜单栏中的帮助
//调出帮助对话框 
            Help helpDialog = new Help(this);
            helpDialog.setVisible(true);
        }
    }
    public void Connect(){
        try{
            socket = new Socket(ip,port);
        }
        catch (Exception e){
            JOptionPane.showConfirmDialog(
                    this,"不能连接到指定的服务器。\n请确认连接设置是否正确。","提示",
                    JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
            return;
        }
        try{
            output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();
            input = new ObjectInputStream(socket.getInputStream() );

            output.writeObject(userName);
            output.flush();

            //读取服务端发送过来的数据是否等于 error  等于error 说明用户名字已经被占用
            String result = (String)input.readObject();
            System.out.println(result);
            if (result.equals("error")){
                JOptionPane.showConfirmDialog(
                        this,"该用户名字已经存在\n请点击用户设置重新为自己取名。","连接失败",
                        JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
                return;
            }

            System.out.println("验证通过");

            //开始收发消息

            recvThread = new ClientReceive(socket,output,input,combobox,messageShow,showStatus,userName);
            recvThread.start();
            loginButton.setEnabled(false);
            loginItem.setEnabled(false);
            userButton.setEnabled(false);

            userItem.setEnabled(false);
            connectButton.setEnabled(false);
            connectItem.setEnabled(false);
            logoffButton.setEnabled(true);
            logoffItem.setEnabled(true);
            clientMessage.setEnabled(true);
            messageShow.append(df.format(new Date()) + "  连接服务器 "+ip+":"+port+" 成功...\n");
            type = 1;//标志位设为已连接
        }
        catch (Exception e){
            System.out.println(e);
            return;
        }
    }
    public void DisConnect(){
        loginButton.setEnabled(true);
        loginItem.setEnabled(true);
        userButton.setEnabled(true);
        userItem.setEnabled(true);
        connectButton.setEnabled(true);
        connectItem.setEnabled(true);
        logoffButton.setEnabled(false);
        logoffItem.setEnabled(false);
        clientMessage.setEnabled(false);
        if(socket.isClosed()){
            return ;
        }
        try{
            output.writeObject("用户下线");
            output.flush();
            input.close();
            output.close();
            socket.close();
            messageShow.append(df.format(new Date()) + "  已经与服务器断开连接...\n");
            type = 0;//标志位设为未连接
        }
        catch (Exception e){
// 
        }
    }
    public void SendMessage(){
        String toSomebody = combobox.getSelectedItem().toString();
        String status = "";
        if(checkbox.isSelected()){

            status = "悄悄话";
        }
        String action = actionlist.getSelectedItem().toString();
        String message = clientMessage.getText();
        if(socket.isClosed()){
            return ;
        }
        try{
            output.writeObject("聊天信息");
            output.flush();
            output.writeObject(toSomebody);
            output.flush();
            output.writeObject(status);
            output.flush();
            output.writeObject(action);
            output.flush();
            output.writeObject(message);
            output.flush();
        }
        catch (Exception e){
// 
        }
    }
   
    public static void main(String[] args) {
         new ChatClient();
    }
}