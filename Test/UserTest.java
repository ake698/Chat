import Server.Node;
import Server.UserLinkList;
import org.junit.Test;


public class UserTest {
    @Test
    public void Case1(){
        Node firstUser = new Node();
        firstUser.username = "firstUser";
        UserLinkList userLinkList = new UserLinkList();
        userLinkList.addUser(firstUser);
        System.out.println(userLinkList.getCount());
        userLinkList.delUser(firstUser);//语句覆盖
        System.out.println(userLinkList.getCount());
    }


    @Test
    public void Case2(){
        UserLinkList userLinkList = new UserLinkList();
        System.out.println(userLinkList.findUser("firstUser"));
    }

    @Test
    public void Case3(){
        UserLinkList userLinkList = new UserLinkList();
        Node firstUser = new Node();
        firstUser.username = "firstUser";
        userLinkList.addUser(firstUser);
        System.out.println(userLinkList.findUser("firstUser"));
    }
    @Test
    public void Case4(){
        UserLinkList userLinkList = new UserLinkList();
        System.out.println(userLinkList.findUser(null));
    }
    @Test
    public void Case5(){
        UserLinkList userLinkList = new UserLinkList();
        System.out.println(userLinkList.findUser("firstUser"));
    }

    @Test
    public void Case6(){
        UserLinkList userLinkList = new UserLinkList();
        Node firstUser = new Node();
        firstUser.username = "firstUser";
        userLinkList.addUser(firstUser);
        System.out.println(userLinkList.findUser("firstUser"));
    }
    @Test
    public void Case7(){
        UserLinkList userLinkList = new UserLinkList();
        System.out.println(userLinkList.findUser(null));
    }

}
