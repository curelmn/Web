package example;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JSONResponse {
    //业务操作是否成功
    private boolean success;
    //业务数据
    private  Object data;
    private String code;
    private String message;


}
