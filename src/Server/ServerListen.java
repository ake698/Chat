package Server;

 import javax.swing.*;


import java.io.*;
import java.net.*;
 import java.text.SimpleDateFormat;
 import java.util.Date;

/*
 * 服务端的侦听类
 */
public class ServerListen extends Thread {
    ServerSocket server;
    JComboBox<String> combobox;
    JTextArea textarea;
    JTextField textfield;
    UserLinkList userLinkList;//用户链表 
    Node client;
    ServerReceive recvThread;
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

    public boolean isStop;
    /*
     * 聊天服务端的用户上线于下线侦听类
     */
    public ServerListen(ServerSocket server,JComboBox<String> combobox,
                        JTextArea textarea,JTextField textfield,UserLinkList userLinkList){
        this.server = server;
        this.combobox = combobox;
        this.textarea = textarea;
        this.textfield = textfield;
        this.userLinkList = userLinkList;
        isStop = false;
    }
    public void run(){
        while(!isStop && !server.isClosed()){
            try{
                client = new Node();
                // 阻塞直到有客户连接
                client.socket = server.accept();
                client.output = new ObjectOutputStream(client.socket.getOutputStream());
                client.output.flush();

                client.input = new ObjectInputStream(client.socket.getInputStream());
                client.username = (String)client.input.readObject();


                //验证用户名字是否可用
                if(userLinkList.findUser(client.username) != null){
                    client.output.writeObject("error");  //不可用发送 字符e
                    client.output.flush();
                    client.socket.close();
                    continue; //还需要回到while 循环中 继续监听其他连接
                }else{
                    client.output.writeObject("s");
                    client.output.flush();
                }



     //显示提示信息 
                combobox.addItem(client.username);
                userLinkList.addUser(client);
                textarea.append(df.format(new Date())+"  "+"用户 " + client.username + " 上线" + "\n");
                textfield.setText("在线用户" + userLinkList.getCount() + "人\n");
                // 启动服务器收发消息线程
                recvThread = new ServerReceive(textarea,textfield,
                        combobox,client,userLinkList);
                recvThread.start();
            }
            catch(Exception e){
            }
        }
    }
} 