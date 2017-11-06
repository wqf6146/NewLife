package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/20.
 */

public class HotKeyBean {

    /**
     * code : 0
     * msg : 1
     * data : {"list":["1","as","刘朋 修路","商品","拉菲","玩具","电器，哈哈，嘻嘻","短袖","自行车","说说"]}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<String> list;

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }
    }
}
