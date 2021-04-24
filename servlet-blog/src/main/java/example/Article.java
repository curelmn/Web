package example;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class Article {
    private Integer id;
    private  String title;
    private String content;
    private java.util.Date createTime;
    private Integer view_count;
    private  Integer uesrId;
}
