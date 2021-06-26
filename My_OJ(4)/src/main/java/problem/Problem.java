package problem;

//实体类 这个类的每个实例就对应数据库当中的一条记录
//这个类包含的字段，就和数据库当中的字段相匹配
public class Problem {
    private int id;  //题目编号
    private  String title;//题目的标题
    private String level;//题目的难度级别
    private  String description;//题目的详细描述
    private  String templateCode;//题目的代码模块
    private  String testCode;//题目的测试用例代码

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getTestCode() {
        return testCode;
    }

    public void setTestCode(String testCode) {
        this.testCode = testCode;
    }

    @Override
    public String toString() {
        return "Problem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", level='" + level + '\'' +
                ", description='" + description + '\'' +
                ", templateCode='" + templateCode + '\'' +
                ", testCode='" + testCode + '\'' +
                '}';
    }
}
