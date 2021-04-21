import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    //序列化操作


    public static void main(String[] args) throws  JsonProcessingException{
        //序列化操作：java对象转变为json字符串
        ObjectMapper mapper = new ObjectMapper();
        User user = new User();
        user.setUsername("猴哥");
        user.setPassword("悟空，快来救我");
        String json = mapper.writeValueAsString(user);
        System.out.println(json);

        //反序列化：把json字符串转变为java对象
        String s = "{\"username\":\"猴哥和八戒\",\"password\":\"悟空，快来救我\"}";
        User u2 = mapper.readValue(s, User.class);
        System.out.println(u2);

        //反序列化时，json键必须对应类中的成员变量，找不到就会报错
        String s3 = "{\"username1\":\"猴哥和八戒\",\"password\":\"悟空，快来救我\"}";
        User u3 = mapper.readValue(s3, User.class);
        System.out.println(u3);
    }

}
