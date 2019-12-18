package client;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * 聊天客户端消息收发类
 */
public class ClientReceive extends Thread {
    private JComboBox<String> combobox;
    private JTextArea textarea;
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

    Socket socket;
    ObjectOutputStream output;
    ObjectInputStream input;
    JTextField showStatus;
    String userName;
    public ClientReceive(Socket socket,ObjectOutputStream output,
                         ObjectInputStream input,JComboBox<String> combobox,JTextArea textarea,JTextField showStatus,String userName){
        this.socket = socket;
        this.output = output;
        this.input = input;
        this.combobox = combobox;
        this.textarea = textarea;
        this.showStatus = showStatus;
        this.userName = userName;
    }
    public void run(){
        while(!socket.isClosed()){
            try{
                String type = (String)input.readObject();
                if(type.equalsIgnoreCase("系统信息")){
                    String sysmsg = (String)input.readObject();

                    textarea.append(df.format(new Date())+"  系统信息: "+sysmsg);
                }
                else if(type.equalsIgnoreCase("服务关闭")){
                    output.close();
                    input.close();
                    socket.close();
                    textarea.append(df.format(new Date())+"  服务器已关闭～\n");

                    break;
                }
                //被T出群聊
                else if(type.equalsIgnoreCase("out")){
                    output.writeObject("T出用户");
                    output.flush();
                    output.close();
                    input.close();
                    socket.close();
                    textarea.append(df.format(new Date())+"  你被管理员T出群聊！！"+"\n");
                    textarea.append(df.format(new Date())+"  3秒钟之后自动退出！！"+"\n");
                    for (int i =3;i>=0;i--){
                        Thread.sleep(1000);
                        textarea.append(df.format(new Date())+"  倒计时："+i+"\n");
                    }

                    System.exit(0);
                    break;
                }

                else if(type.equalsIgnoreCase("聊天信息")){
                    String message = (String)input.readObject();
                    textarea.append(df.format(new Date())+"  "+ message);
                }
                else if(type.equalsIgnoreCase("用户列表")){
                    String userlist = (String)input.readObject();
                    String usernames[] = userlist.split("\n");
                    combobox.removeAllItems();
                    int i =0;
                    combobox.addItem("所有人");
                    while(i < usernames.length){
                        combobox.addItem(usernames[i]);
                        i ++;
                    }
                    combobox.setSelectedIndex(0);

                    showStatus.setText("当前用户："+userName+"                                              " +"在线用户" + usernames.length + " 人");
                }
            }
            catch (Exception e ){
                System.out.println(e);
            }
        }
    }
}