package com.yhkj.yymall.bean;

/**
 * Created by Administrator on 2018/2/3.
 */

public class TestRegisterBean {

    /**
     * status : true
     * msg : 成功
     * data : {"id":"4c4508e676074897a7e520410f5797c1","username":"18330273526","password":"14e1b600b1fd579f47433b88e8d85291","email":null,"uname":null,"firstname":null,"midname":null,"lastname":null,"language":null,"jobtitle":null,"gender":null,"mobile":null,"birthday":null,"nickname":null,"secureconf":"5","usertype":null,"country":null,"region":null,"useragent":null,"isp":null,"playertype":null,"orgi":null,"creater":null,"createtime":1517627501227,"updatetime":1517627501227,"passupdatetime":1517627501226,"openid":null,"qqid":null,"ostype":null,"browser":null,"memo":null,"city":null,"province":null,"login":false,"online":false,"status":null,"disabled":false,"datastatus":false,"headimg":false,"playerlevel":null,"experience":0,"secquestion":false,"lastlogintime":1517627501227,"fans":0,"follows":0,"integral":0,"cards":0,"goldcoins":0,"diamonds":0,"sign":null}
     * code : 200
     * gametype : null
     * enableai : false
     * waittime : 0
     * noaiwaitime : 0
     * noaimsg : null
     * token : null
     * games : null
     */

    private boolean status;
    private String msg;
    private DataBean data;
    private String code;
    private Object gametype;
    private boolean enableai;
    private int waittime;
    private int noaiwaitime;
    private Object noaimsg;
    private Object token;
    private Object games;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getGametype() {
        return gametype;
    }

    public void setGametype(Object gametype) {
        this.gametype = gametype;
    }

    public boolean isEnableai() {
        return enableai;
    }

    public void setEnableai(boolean enableai) {
        this.enableai = enableai;
    }

    public int getWaittime() {
        return waittime;
    }

    public void setWaittime(int waittime) {
        this.waittime = waittime;
    }

    public int getNoaiwaitime() {
        return noaiwaitime;
    }

    public void setNoaiwaitime(int noaiwaitime) {
        this.noaiwaitime = noaiwaitime;
    }

    public Object getNoaimsg() {
        return noaimsg;
    }

    public void setNoaimsg(Object noaimsg) {
        this.noaimsg = noaimsg;
    }

    public Object getToken() {
        return token;
    }

    public void setToken(Object token) {
        this.token = token;
    }

    public Object getGames() {
        return games;
    }

    public void setGames(Object games) {
        this.games = games;
    }

    public static class DataBean {
        /**
         * id : 4c4508e676074897a7e520410f5797c1
         * username : 18330273526
         * password : 14e1b600b1fd579f47433b88e8d85291
         * email : null
         * uname : null
         * firstname : null
         * midname : null
         * lastname : null
         * language : null
         * jobtitle : null
         * gender : null
         * mobile : null
         * birthday : null
         * nickname : null
         * secureconf : 5
         * usertype : null
         * country : null
         * region : null
         * useragent : null
         * isp : null
         * playertype : null
         * orgi : null
         * creater : null
         * createtime : 1517627501227
         * updatetime : 1517627501227
         * passupdatetime : 1517627501226
         * openid : null
         * qqid : null
         * ostype : null
         * browser : null
         * memo : null
         * city : null
         * province : null
         * login : false
         * online : false
         * status : null
         * disabled : false
         * datastatus : false
         * headimg : false
         * playerlevel : null
         * experience : 0
         * secquestion : false
         * lastlogintime : 1517627501227
         * fans : 0
         * follows : 0
         * integral : 0
         * cards : 0
         * goldcoins : 0
         * diamonds : 0
         * sign : null
         */

        private String id;
        private String username;
        private String password;
        private Object email;
        private Object uname;
        private Object firstname;
        private Object midname;
        private Object lastname;
        private Object language;
        private Object jobtitle;
        private Object gender;
        private Object mobile;
        private Object birthday;
        private Object nickname;
        private String secureconf;
        private Object usertype;
        private Object country;
        private Object region;
        private Object useragent;
        private Object isp;
        private Object playertype;
        private Object orgi;
        private Object creater;
        private long createtime;
        private long updatetime;
        private long passupdatetime;
        private Object openid;
        private Object qqid;
        private Object ostype;
        private Object browser;
        private Object memo;
        private Object city;
        private Object province;
        private boolean login;
        private boolean online;
        private Object status;
        private boolean disabled;
        private boolean datastatus;
        private boolean headimg;
        private Object playerlevel;
        private int experience;
        private boolean secquestion;
        private long lastlogintime;
        private int fans;
        private int follows;
        private int integral;
        private int cards;
        private int goldcoins;
        private int diamonds;
        private Object sign;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Object getEmail() {
            return email;
        }

        public void setEmail(Object email) {
            this.email = email;
        }

        public Object getUname() {
            return uname;
        }

        public void setUname(Object uname) {
            this.uname = uname;
        }

        public Object getFirstname() {
            return firstname;
        }

        public void setFirstname(Object firstname) {
            this.firstname = firstname;
        }

        public Object getMidname() {
            return midname;
        }

        public void setMidname(Object midname) {
            this.midname = midname;
        }

        public Object getLastname() {
            return lastname;
        }

        public void setLastname(Object lastname) {
            this.lastname = lastname;
        }

        public Object getLanguage() {
            return language;
        }

        public void setLanguage(Object language) {
            this.language = language;
        }

        public Object getJobtitle() {
            return jobtitle;
        }

        public void setJobtitle(Object jobtitle) {
            this.jobtitle = jobtitle;
        }

        public Object getGender() {
            return gender;
        }

        public void setGender(Object gender) {
            this.gender = gender;
        }

        public Object getMobile() {
            return mobile;
        }

        public void setMobile(Object mobile) {
            this.mobile = mobile;
        }

        public Object getBirthday() {
            return birthday;
        }

        public void setBirthday(Object birthday) {
            this.birthday = birthday;
        }

        public Object getNickname() {
            return nickname;
        }

        public void setNickname(Object nickname) {
            this.nickname = nickname;
        }

        public String getSecureconf() {
            return secureconf;
        }

        public void setSecureconf(String secureconf) {
            this.secureconf = secureconf;
        }

        public Object getUsertype() {
            return usertype;
        }

        public void setUsertype(Object usertype) {
            this.usertype = usertype;
        }

        public Object getCountry() {
            return country;
        }

        public void setCountry(Object country) {
            this.country = country;
        }

        public Object getRegion() {
            return region;
        }

        public void setRegion(Object region) {
            this.region = region;
        }

        public Object getUseragent() {
            return useragent;
        }

        public void setUseragent(Object useragent) {
            this.useragent = useragent;
        }

        public Object getIsp() {
            return isp;
        }

        public void setIsp(Object isp) {
            this.isp = isp;
        }

        public Object getPlayertype() {
            return playertype;
        }

        public void setPlayertype(Object playertype) {
            this.playertype = playertype;
        }

        public Object getOrgi() {
            return orgi;
        }

        public void setOrgi(Object orgi) {
            this.orgi = orgi;
        }

        public Object getCreater() {
            return creater;
        }

        public void setCreater(Object creater) {
            this.creater = creater;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public long getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(long updatetime) {
            this.updatetime = updatetime;
        }

        public long getPassupdatetime() {
            return passupdatetime;
        }

        public void setPassupdatetime(long passupdatetime) {
            this.passupdatetime = passupdatetime;
        }

        public Object getOpenid() {
            return openid;
        }

        public void setOpenid(Object openid) {
            this.openid = openid;
        }

        public Object getQqid() {
            return qqid;
        }

        public void setQqid(Object qqid) {
            this.qqid = qqid;
        }

        public Object getOstype() {
            return ostype;
        }

        public void setOstype(Object ostype) {
            this.ostype = ostype;
        }

        public Object getBrowser() {
            return browser;
        }

        public void setBrowser(Object browser) {
            this.browser = browser;
        }

        public Object getMemo() {
            return memo;
        }

        public void setMemo(Object memo) {
            this.memo = memo;
        }

        public Object getCity() {
            return city;
        }

        public void setCity(Object city) {
            this.city = city;
        }

        public Object getProvince() {
            return province;
        }

        public void setProvince(Object province) {
            this.province = province;
        }

        public boolean isLogin() {
            return login;
        }

        public void setLogin(boolean login) {
            this.login = login;
        }

        public boolean isOnline() {
            return online;
        }

        public void setOnline(boolean online) {
            this.online = online;
        }

        public Object getStatus() {
            return status;
        }

        public void setStatus(Object status) {
            this.status = status;
        }

        public boolean isDisabled() {
            return disabled;
        }

        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
        }

        public boolean isDatastatus() {
            return datastatus;
        }

        public void setDatastatus(boolean datastatus) {
            this.datastatus = datastatus;
        }

        public boolean isHeadimg() {
            return headimg;
        }

        public void setHeadimg(boolean headimg) {
            this.headimg = headimg;
        }

        public Object getPlayerlevel() {
            return playerlevel;
        }

        public void setPlayerlevel(Object playerlevel) {
            this.playerlevel = playerlevel;
        }

        public int getExperience() {
            return experience;
        }

        public void setExperience(int experience) {
            this.experience = experience;
        }

        public boolean isSecquestion() {
            return secquestion;
        }

        public void setSecquestion(boolean secquestion) {
            this.secquestion = secquestion;
        }

        public long getLastlogintime() {
            return lastlogintime;
        }

        public void setLastlogintime(long lastlogintime) {
            this.lastlogintime = lastlogintime;
        }

        public int getFans() {
            return fans;
        }

        public void setFans(int fans) {
            this.fans = fans;
        }

        public int getFollows() {
            return follows;
        }

        public void setFollows(int follows) {
            this.follows = follows;
        }

        public int getIntegral() {
            return integral;
        }

        public void setIntegral(int integral) {
            this.integral = integral;
        }

        public int getCards() {
            return cards;
        }

        public void setCards(int cards) {
            this.cards = cards;
        }

        public int getGoldcoins() {
            return goldcoins;
        }

        public void setGoldcoins(int goldcoins) {
            this.goldcoins = goldcoins;
        }

        public int getDiamonds() {
            return diamonds;
        }

        public void setDiamonds(int diamonds) {
            this.diamonds = diamonds;
        }

        public Object getSign() {
            return sign;
        }

        public void setSign(Object sign) {
            this.sign = sign;
        }
    }
}
