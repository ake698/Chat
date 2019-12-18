package client;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;

/**
 * 生成用户信息输入对话框的类
 * 让用户输入自己的用户名
 */
public class UserConf extends JDialog {
   
	private static final long serialVersionUID = 1L;
	JPanel panelUserConf = new JPanel();
    JButton save = new JButton();
    JButton cancel = new JButton();
    JButton randomName = new JButton();
    JLabel DLGINFO=new JLabel(
            " 默认用户名为:client");
    JPanel panelSave = new JPanel();
    JLabel message = new JLabel();
    String userInputName;
    JTextField userName ;
    public UserConf(JFrame frame,String str) {
        super(frame, true);
        this.userInputName = str;
        try {
            jbInit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
//设置运行位置，使对话框居中
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation( (int) (screenSize.width - 400) / 2 + 50,
                (int) (screenSize.height - 600) / 2 + 150);
        this.setResizable(false);
    }
    private void jbInit() throws Exception {
        this.setSize(new Dimension(300, 120));
        this.setTitle("用户设置");
        message.setText("请输入用户名:");
        userName = new JTextField(10);
        userName.setText(userInputName);
        save.setText("保存");
        cancel.setText("取消");
        randomName.setText("随机");
        panelUserConf.setLayout(new FlowLayout());
        panelUserConf.add(message);
        panelUserConf.add(userName);
        panelSave.add(new Label(" "));
        panelSave.add(save);
        panelSave.add(cancel);
        panelSave.add(randomName);
        panelSave.add(new Label(" "));
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(panelUserConf, BorderLayout.NORTH);
        contentPane.add(DLGINFO, BorderLayout.CENTER);
        contentPane.add(panelSave, BorderLayout.SOUTH);

//随机按钮的事件处理
        randomName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuffer rName = new StringBuffer("user") ;
                Random random = new Random();
                for (int i = 0; i < 4 ; i++) {
                    rName.append(random.nextInt(10));
                }
                userName.setText(rName.toString());

            }
        });


//保存按钮的事件处理


        save.addActionListener(
                new ActionListener() {
                    public void actionPerformed (ActionEvent a) {
                        if(userName.getText().equals("")){
                            DLGINFO.setText(
                                    " 用户名不能为空～");
                            userName.setText(userInputName);
                            return;
                        }
                        else if(userName.getText().length() > 15){
                            DLGINFO.setText(" 用户名长度不能大于15个字符～");
                            userName.setText(userInputName);
                            return;
                        }
                        userInputName = userName.getText();
                        dispose();
                    }
                }
        );
//关闭对话框时的操作
        this.addWindowListener(
                new WindowAdapter(){
                    public void windowClosing(WindowEvent e){
                        DLGINFO.setText(" 默认用户名为:吉大大");
                    }
                }
        );
//取消按钮的事件处理
        cancel.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        DLGINFO.setText(" 默认用户名为:吉大大");
                        dispose();
                    }
                }
        );
    }
}


