package com.example.admin.myapplication.bean;

import java.util.List;

public class ElementData {

    /**
     * channelNum : 1
     * channelType : T
     * endTime : 0
     * list : [{"time":1529249340,"value":26.381199},{"time":1529249345,"value":26.396938},{"time":1529249350,"value":26.36546},{"time":1529249355,"value":26.34972},{"time":1529249360,"value":26.34972},{"time":1529249365,"value":26.35759},{"time":1529249370,"value":26.35759},{"time":1529249375,"value":26.37333},{"time":1529249380,"value":26.396938},{"time":1529249385,"value":26.34185},{"time":1529249390,"value":26.36546},{"time":1529249395,"value":26.33398},{"time":1529249400,"value":26.37333},{"time":1529249405,"value":26.381199},{"time":1529249410,"value":26.32611},{"time":1529249415,"value":26.35759},{"time":1529249420,"value":26.34972},{"time":1529249425,"value":26.33398},{"time":1529249430,"value":26.34972},{"time":1529249435,"value":26.36546},{"time":1529249440,"value":26.35759},{"time":1529249445,"value":26.37333},{"time":1529249450,"value":26.34972},{"time":1529249455,"value":26.33398},{"time":1529249460,"value":26.34972},{"time":1529249465,"value":26.33398},{"time":1529249470,"value":26.34185},{"time":1529249475,"value":26.33398},{"time":1529249480,"value":26.33398},{"time":1529249485,"value":26.318241},{"time":1529249490,"value":26.36546},{"time":1529249495,"value":26.33398},{"time":1529249500,"value":26.34185},{"time":1529249505,"value":26.32611},{"time":1529249510,"value":26.34972},{"time":1529249515,"value":26.32611},{"time":1529249520,"value":26.34972},{"time":1529249525,"value":26.310371},{"time":1529249530,"value":26.32611},{"time":1529249535,"value":26.34185},{"time":1529249410,"value":26.32611},{"time":1529249415,"value":26.35759},{"time":1529249420,"value":26.34972},{"time":1529249425,"value":26.33398},{"time":1529249430,"value":26.34972},{"time":1529249435,"value":26.36546},{"time":1529249440,"value":26.35759},{"time":1529249445,"value":26.37333},{"time":1529249450,"value":26.34972},{"time":1529249455,"value":26.33398},{"time":1529249460,"value":26.34972},{"time":1529249465,"value":26.33398}]
     * maxv : 0.0
     * minv : 0.0
     * mode : 0
     * size : 0
     * startTime : 0
     * type : 0
     */

    public int channelNum;
    public String channelType;
    public int endTime;
    public double maxv;
    public double minv;
    public int mode;
    public int size;
    public int startTime;
    public int type;
    public List<ListBean> list;

    public static class ListBean {
        /**
         * time : 1529249340
         * value : 26.381199
         */

        public int time;
        public double value;
    }
}
