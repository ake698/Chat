package Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 生成设置对话框的类
 */
public class DeleteUser extends JDialog {

	private static final long serialVersionUID = 1L;
    JComboBox<String> combobox = new JComboBox<String>();//删除的用户列表
    JButton deleteButton = new JButton("请他离开"); //删除按钮
	JPanel titlePanel = new JPanel();
    JPanel contentPanel = new JPanel();
    JPanel closePanel = new JPanel();
    JButton close = new JButton();
    JLabel title = new JLabel("聊天成员管理");

    Color bg = new Color(255,255,255);
    private UserLinkList userLinkList;
    public DeleteUser(JFrame frame,UserLinkList userLinkList) {
        super(frame, true);
        this.userLinkList = userLinkList;
        try {
            jbInit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
//设置运行位置，使对话框居中
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation( (int) (screenSize.width - 400) / 2,
                (int) (screenSize.height - 320) / 2);
        this.setResizable(false);
    }
    private void jbInit() throws Exception {
        this.setSize(new Dimension(400, 200));
        this.setTitle("请他离开");

        if(userLinkList.getCount()==0){
            combobox.addItem("暂无");
            deleteButton.setEnabled(false);
        }else{
            int i = 0;
            while (i<userLinkList.getCount()){ //添加其他用户
                combobox.addItem(userLinkList.findUser(i).username);
                i++;
            }
            deleteButton.setEnabled(true);
        }



        titlePanel.setBackground(bg);;
        contentPanel.setBackground(bg);
        closePanel.setBackground(bg);

        titlePanel.add(new Label(" "));
        titlePanel.add(title);
        titlePanel.add(new Label(" "));

        contentPanel.add(combobox);
        contentPanel.add(deleteButton);

        closePanel.add(new Label(" "));
        closePanel.add(close);
        closePanel.add(new Label(" "));
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(titlePanel, BorderLayout.NORTH);
        contentPane.add(contentPanel, BorderLayout.CENTER);
        contentPane.add(closePanel, BorderLayout.SOUTH);
        close.setText("关闭");
//事件处理
        close.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                }
        );

        //T出某个人
        deleteButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String toSomebody = combobox.getSelectedItem().toString();
                        //向所有人发送消息
                        Node node = userLinkList.findUser(toSomebody);
                        try{
                            node.output.writeObject("out");
                            node.output.flush();
                        } catch(Exception ex){
                            System.out.println(ex);
                        }
                    }
                }
        );
    }
}
