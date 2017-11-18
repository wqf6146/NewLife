package com.yhkj.yymall.bean;

/**
 * Created by Administrator on 2017/10/18.
 */

public class UpdateBean {

    /**
     * code : 0
     * msg : 1
     * data : {"info":{"version ":"v1.1.6","versionDescription":"重大更新","versionUrl":"baidu.com"}}
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
        /**
         * info : {"version ":"v1.1.6","versionDescription":"重大更新","versionUrl":"baidu.com"}
         */

        private InfoBean info;

        public InfoBean getInfo() {
            return info;
        }

        public void setInfo(InfoBean info) {
            this.info = info;
        }

        public static class InfoBean {
            /**
             * version  : v1.1.6
             * versionDescription : 重大更新
             * versionUrl : baidu.com
             */

            private String version;
            private String versionNo;
            private String versionDescription;
            private String versionUrl;
            private String md5;

            public void setMd5(String md5) {
                this.md5 = md5;
            }

            public String getMd5() {
                return md5;
            }

            public String getVersionNo() {
                return versionNo;
            }

            public void setVersionNo(String versionNo) {
                this.versionNo = versionNo;
            }

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }

            public String getVersionDescription() {
                return versionDescription;
            }

            public void setVersionDescription(String versionDescription) {
                this.versionDescription = versionDescription;
            }

            public String getVersionUrl() {
                return versionUrl;
            }

            public void setVersionUrl(String versionUrl) {
                this.versionUrl = versionUrl;
            }
        }
    }
}
