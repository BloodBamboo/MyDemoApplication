package com.example.admin.myapplication.bean;


public class Article extends BaseBean {
    /**
     * istop : true
     * linkAddress : null
     * columnBankId : null
     * lastUpdateTime : 1433224540000
     * columnId : 4
     * type : 1
     * id : 2295
     * categoryName : 职场攻略
     * articleBankId : null
     * title : 2014年全国热门职位薪酬调查报告——工程师类
     * level : 1
     * description : 注：本报告中的数据由华夏大地教育网（www.edu-edu.com.cn）收集整理，内容均来自相关网站或由网友填写的调查问卷，本报告免费公益提供。我们对于明显虚假的信息已经给予删除，以及采用各种手段以确保信息的真实性。
     * penName : 贾斌
     * articleSource :
     * shared : 0
     * tags : null
     * keywords : 工程师薪酬调查
     * creatorAlias : 贾斌
     * publishTime : 1433226887000
     * createdTime : 1433224540000
     * status : true
     * titleImagePath : Qda0MKHsgqxbPJAdOG9m.png
     * categoryId : 4
     * creatorId : 171
     * code : 1433224659924
     * article :
     * titleImage : Qda0MKHsgqxbPJAdOG9m.png/{Q9{7NN_[`9[ELI23HX(~QO.png
     * topicId : null
     * topTime : 1433226291000
     * sharedName : 否
     */

    public boolean istop;
    public Object linkAddress;
    public Object columnBankId;
    public long lastUpdateTime;
    public int columnId;
    public int type;
    public String categoryName;
    public Object articleBankId;
    public String title;
    public int level;
    public String description;
    public String penName;
    public String articleSource;
    public int shared;
    public Object tags;
    public String keywords;
    public String creatorAlias;
    public long publishTime;
    public long createdTime;
    public boolean status;
    public String titleImagePath;
    public int categoryId;
    public int creatorId;
    public String code;
    public String article;
    public String titleImage;
    public Object topicId;
    public long topTime;
    public String sharedName;
//    public String title = "";
//    public String categoryName = "";
//    public String penName = "";
//    public String publishTime = "";
//    public String shortContent = "";
//    public boolean isTop = false;
//    public int level = 0;
//    public String titleImage = "";


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(istop+"\n");
        sb.append(linkAddress+"\n");
        sb.append(columnBankId+"\n");
        sb.append(lastUpdateTime+"\n");
        sb.append(columnId+"\n");
        sb.append(type+"\n");
        sb.append(categoryName+"\n");
        return sb.toString();
    }
}
