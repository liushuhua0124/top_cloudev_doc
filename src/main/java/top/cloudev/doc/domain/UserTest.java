package top.cloudev.doc.domain;

public class UserTest {
    public static void main(String[] args) {
        User user = new User();
        user.setType((short)1);
        System.out.println(user.getType());
    }
}
