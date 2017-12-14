package com.yhkj.yymall.http.api;

/**
 * Created by Administrator on 2017/5/24.
 */

public interface ApiService {

    String SHARE_SHOP_URL = "https://webapp.yiyiyaya.cc/share.html";//分享URL
    String OFFLINE_HOME_URL = "https://webapp.yiyiyaya.cc/wode.html";//线下活动url
    String OFFLINE_SHARE_URL = "https://webapp.yiyiyaya.cc/fenxiang.html";//线下活动分享url
    String OFFLINE_SHAREES_URL = "https://webapp.yiyiyaya.cc/sharees.html";//线下活动分享url
    String SHARE_CODE_URL = "https://webapp.yiyiyaya.cc/Invitation.html";
    String SHOP_DETAIL = "https://webapp.yiyiyaya.cc/detail.html";//商品详情
    String SHOP_DETAIL_TEST = "http://yiyiyaya-oss.oss-cn-zhangjiakou.aliyuncs.com/webapp/detail.html";
    String YYWEB = "https://api.yiyiyaya.cc/article/getArticle?tag=";
    String SERVER_URL = "https://api.yiyiyaya.cc/article/getArticleInfo?articleId=";

    String HOST = "https://api.yiyiyaya.cc";

    String GETMES = HOST + "/article/jPushInfo";

    String VIDEODESC = HOST + "/video/baseData";

    String BANNER = HOST + "/homepage/slides";  //轮播图
    String HOMEACT = HOST + "/homepage/acitvity"; //首页活动相关

    String OFFLINECALLBACK = HOST + "/offline/shareCallBack";

    String HOMEREMMOND = HOST + "/homepage/categorys";

    String GETPLACES = HOST + "/other/getAddress"; //获取地区

    String OFFLINEACT = HOST + "/offline/info";//线下活动

    String SENDMSG = HOST + "/other/sendMsg";//发送验证码

    String REGISTER = HOST + "/auth/signUp";//注册

    String LOGIN = HOST + "/auth/signIn";//登录

    String RESETPASSWORD = HOST + "/auth/resetPassword";//找回密码

    String GETADDRESSLIST = HOST + "/ucenter/addressList"; //收货地址

    String ADDADDRESS = HOST + "/ucenter/addAddress"; //添加收货地址

    String OFFLINEACTVALID = HOST + "/offline/isValid";//线下活动是否有效

    String UPDATEADDRESS = HOST + "/ucenter/editAddress"; //修改收货地址

    String SETDEFADDRESS = HOST + "/ucenter/setDefaultAddress";//设置默认地址

    String DELADDRESS = HOST + "/ucenter/deleteAddress"; //删除地址

    String GETLOGO = HOST + "/other/appLead";//获取logo

    String GETLOTTROYINFO = HOST + "/roll/getrollinfo"; //获取抽奖信息
    String DOLOTTERY = HOST + "/roll/roll_act"; //抽奖

    String CARLIST = HOST + "/car/lists";//购物车数据

    String LEASE_HOT = HOST + "/rent/hot"; //热门领用

    String ORDER_PREVIEW = HOST + "/order/preview"; //订单预览

    String ORDER_SUBMIT = HOST + "/order/submit"; //生成订单
    String SHOPCAR_SELECTSTATE = HOST + "/car/setChecked"; //设置选中状态

    String LEASELISTS = HOST + "/rent/lists"; //租X分类筛选列表

    String ENABLESPEC = HOST + "/goods/goodsSpec";//可选规格

    String RECHARGEDETAIL = HOST + "/recharge/rechargeContent";//充值详情

//    String GETUSER

    String MINEINFO = HOST + "/ucenter/memberInfo";//我的页面

    String PERSONINFO = HOST + "/ucenter/memberData"; //"个人资料"

    String ORDERALL = HOST + "/order/orderAll";//全部订单

    String COLLECTLIST = HOST + "/collect/collectList"; //"收藏商品列表"

    String MYINTEYAYA = HOST + "/other/getPointsYY";//我的积分丫丫

    String ADD_COLLECTSHOP = HOST + "/collect/addCollect"; //添加收藏商品

    String DELETE_COLLECTSHOP = HOST + "/collect/delCollect"; //删除收藏商品

    String CLASSIFY_COLLECTLIST = HOST + "/collect/cateList"; //获得收藏分类

    String SHOP_INFO = HOST + "/goods/info"; //商品详情

    String CANCELORDER = HOST + "/order/cancelOrder";//取消订单

    String MODIFADDRESS = HOST + "/order/editOrderAdr";//修改订单地址

    String GETORDERADDRESS = HOST + "/order/getOrderAdr"; //获取订单收货地址

    String UPDATEUSERINFO = HOST + "/ucenter/setMemberData";//修改个人信息

    String LOADGOODSPEC = HOST + "/goods/product"; //读取货品规格

    String ORDERDETAIL = HOST + "/order/orderContent"; //订单详情

    String LOGISTICSDETAIL = HOST + "/express/tracesList"; //物流详情

    String SHOPCLASSIFY = HOST + "/goods/categorys";//商品分类

    String HOTKEYVALUE = HOST + "/goods/hotsearch"; //热门关键字

    String KEYSEARCH = HOST + "/goods/search"; //关键字搜索商品

    String ADD_CAR = HOST + "/car/add"; //加入购物车

    String ORDER_PREVIEWSINGLE = HOST + "/order/previewSingle"; //预览订单(单件)

    String ORDER_SUBMITSINGLE = HOST + "/order/submitSingle"; //创建订单(单件)

    String YIYA_SHOP = HOST + "/shop/home"; //yiya商城

    String PAY_CHECKPAYTOKEN = HOST + "/pay/checkPayToken"; //验证设置支付密码

    String PAY_BALANCEPAY = HOST + "/pay/balancePay"; //余额支付

    String CAR_SET_NUMS = HOST + "/car/setNums"; //修改购买数量

    String CAR_REMOVE = HOST + "/car/remove"; //移出购物车

    String LEASE_PREVIEWORDER = HOST + "/order/previewRent";//"租宁预览订单"

    String DISCOUNT_PREVIEWORDER = HOST + "/order/previewDiscount";//"折扣预览订单"

    String DAILY_PREVIEWORDER = HOST + "/order/previewDaily";//"日常活动预览订单"

    String LEASE_SUBMITORDER = HOST + "/order/submitRent"; //创建订单

    String DISCOUNT_SUBMITORDER = HOST + "/order/submitDiscount"; //创建订单

    String DAILY_SUBMITORDER = HOST + "/order/submitDaily"; //创建订单 日常活动

    String INTEGRAL_SUBMITORDER = HOST + "/order/submitInteger"; //创建订单

    String MYEXPERIENCE = HOST + "/ucenter/myExperience"; //我的等级

    String GETPEYPWDTIKER = HOST + "/pay/getTicket";//获取支付密码修改凭证

    String UPDATEPAYPWD = HOST + "/pay/resetPayToken";//修改支付密码

    String DAYSIGN = HOST + "/ucenter/daySign";//用户签到

    String MESSAGELIST = HOST + "/message/MessageList";//获得列表

    String READMESSAGE = HOST + "/message/readMessage";//标为已读

    String INTEGRAL_PREVIEWORDER = HOST + "/order/previewIntegral";//积分预览订单

    String DELETEMESSAGE = HOST + "/message/deleteMessage";//消息删除

    String READALL_MESSAGE = HOST + "/message/readAllMessage";//全部标位已读

    String DELEALL_MESSAGE = HOST + "/message/deleteAllMessage";// 全部消息删除

    String GOODS_LIKE = HOST + "/goods/like";// 获取猜你喜欢

    String KILLTIME = HOST + "/panicbuy/allInfo";//限时抢购时间

    String KILLSHOPLIST = HOST + "/panicbuy/lists";//限时抢购列表

    String KILLCLASSIFY = HOST + "/panicbuy/topCategoryList"; //限时分类

    String KILLDETAIL = HOST + "/panicbuy/info";//抢购详情

    String TIMEKILL_PREORDER = HOST + "/order/previewPanicBuy";//限时抢购预览订单
    String TIMEKILL_SUBMITORDER = HOST + "/order/submitPanicBuy";//提交订单

    String ORDER_THREE = HOST + "/order/orderReceipt"; //我的订单（待收货，待评价）

    String SHARECODE = HOST + "/share/shareInvite";//分享邀请码
    String CHECKCODE = HOST + "/share/checkShareCode";//接受邀请码

    String GOODSIMG = HOST + "/comment/impression";//商品印象标签

    String ORDEREVA = HOST + "/order/orderComment"; //订单评论页面

    String SUBMITEVA = HOST + "/order/orderCommentAdd";//提交商品评论

    String SUBMITAFTERMALL = HOST + "/order/afterSaleApply";//提交售后

    String NEWSUBMITAFET = HOST + "/order/afterSaleSubmit";//提交售后

    String GETEVAALLLIST = HOST + "/comment/commentContent";//获取商品评论

    String GETSHOPEVA = HOST + "/comment/impression"; //获取商品印象标签

    String GETEVACLASSIFY = HOST + "/comment/commentImpressions";//获取印象标签分类

    String VERIFYTAKE = HOST + "/order/orderConfirm"; //确认收货

    String USERRECORD = HOST + "/rental/record";//用户租赁情况

    String GOODSLIST = HOST + "/goods/lists";//分类商品列表

    String RENTDETAIL = HOST + "/rental/recordContent";//置换详情

    String RENTCASH = HOST + "/rental/substitution";//置换押金

    String GROUP_CREATE = HOST + "/group/create"; // 确认建团

    String ORDER_PREVIEWGROUPBUY = HOST + "/order/previewGroupBuy"; // 预览订单(拼团)

    String ORDER_SUBMITGROUPBUY = HOST + "/order/submitGroupBuy"; // 创建订单(拼团)

    String GROUP_JOIN = HOST + "/group/join"; // 参团详情页

    String APPLYWITHDRAW = HOST + "/withdraw/applyWithdraw";//申请提现

    String APPLYLIST = HOST + "/withdraw/withdrawRecord";//提现记录

    String APPLYDETAIL = HOST + "/withdraw/withdrawDetails";//提现记录详情

    String GETBALANCE = HOST + "/withdraw/getMeberBalance";//获取余额

    String GETLEASECLASSIFY = HOST + "/rent/getBands";//获取租赁分类筛选

    String GROUP_COMFIRM = HOST + "/group/comfirm"; // 确认参团

    String GROUP_WAITGROUP = HOST + "/group/waitGroup"; // 获取待成团列表

    String GROUP_GROUPLIST = HOST + "/group/groupList"; // 获取当前活动下所有拼团组

    String PRIVILEDGE = HOST + "/ucenter/vipRights"; //等级特权

    String GETBABYYAYA = HOST + "/other/getYyByBaby";//宝宝信息丫丫

    String GROUP_LISTS = HOST + "/group/lists"; // 拼团好货

    String INTELOG = HOST + "/ucenter/pointLog";//积分日志

    String BALANCELOG = HOST + "/ucenter/accountLog";//余额日志

    String YYLOG = HOST + "/ucenter/yayaLog";//丫丫日志

    String ALLGROUPLIST = HOST + "/group/allGroup";//全部拼团列表

    String SUCGROUPLIST = HOST + "/group/doneGroup";//成功拼团列表

    String FAILDGROUPLIST = HOST + "/group/failGroup";//失败拼团列表

    String WAITGROUPLIST = HOST + "/group/waitGroup";//等待成团列表

    String ARTICLE_GETARTICLELIST = HOST + "/article/getArticleList"; // 问题列表

    String AUTH_THDSIGNIN = HOST + "/auth/thdSignIn"; // 第三方登录

    String AUTH_THDBIND = HOST + "/auth/thdBind"; // 第三方绑定

    String APPLYAFTERMALLLIST = HOST + "/order/afterSaleList";//申请售后列表

    String COLLECT_ADDCOLLECT = HOST + "/collect/addCollect"; // 添加收藏商品

    String COLLECT_DELALL = HOST + "/collect/delAllCollect";//删除全部收藏

    String ADVANCE = HOST + "/ucenter/addComplain";//意见反馈

    String UPDATE = HOST + "/other/getVersion";//版本更新

    String TIMEKILLCLASSIFY = HOST + "/panicbuy/topCategoryList";//限时抢购分类

    String GROUPDETAIL = HOST + "/group/groupContent";//拼团详情

    String ADDRECHARGE = HOST + "/recharge/addRecharge";//充值

    String PREWDRAWLOTTE = HOST + "/order/previewRoll";//预览中奖订单
    String SUBMITDRAW = HOST + "/order/submitRoll";//提交中奖订单

    String UPDATEPHONE = HOST + "/ucenter/setAccount";//修改登录手机号

    String GETAFTERMALLINFO = HOST + "/order/afterSaleContent";//获取售后信息

    String GETLOSGTIEMES = HOST + "/message/expressList"; //获取物流通知

    String GETMINEASSETS = HOST + "/message/assetsList";//获取我的资产列表

    String GETMESCATGORYLIST = HOST + "/message/categoryList";//获取我的消息分类

    String REFUNDDETAIL = HOST + "/order/afterSaleDetail";//退款详情

    String GETSERVICE = HOST + "/other/getCustomer";//获取客服

    String GETUSERASSET = HOST + "/ucenter/memberAsset";//获取用户资产

    String UNREADMESEXIT = HOST + "/message/getReadState";//是否存在未读消息

    String ADDBABY = HOST + "/ucenter/perfectBabyInfo";//添加宝宝信息

    String GETBABYINFO = HOST + "/ucenter/getBabyInfo";//获取宝贝信息

    String UPDATEBABY = HOST + "/ucenter/updateBabyInfo";//更新宝宝信息

    String DELBABY = HOST + "/ucenter/delBabyInfo";//删除宝宝信息

    String SHOPGIFT = HOST + "/goods/getGiftLists";//赠品

    String MINEEVALIST = HOST + "/comment/mySelfCommentLists";//我的评价列表

    String PAYDETAIL = HOST + "/ucenter/accountLogContent";//充值详情

    String NEWORDERDETAIL = HOST + "/order/orderDetail";//订单详情

    String NEWAFTERMALlPAGE = HOST + "/order/afterSalePage";//售后显示页

    String GETAFTERMALLPRICE = HOST + "/order/refData";//售后件数价格

    String RECHARGE = HOST + "/recharge/addRecharge";//充值

    String RECHARGECALLBACK = HOST + "/recharge/rechargeBack";//充值回调

    String RECHARGELIST = HOST + "/recharge/rechargeList";//充值记录

    String RANKCHART = HOST + "/ucenter/expStrategy"; //等级梯形图

    String DISCOUNTLIST = HOST + "/goods/discountLists";//折扣商品

    String INTEGRALLIST = HOST + "/goods/integralLists";//折扣商品

    String YIYASHOPLIST = HOST + "/shop/commandGoods";//YIya商城商品列表

    String DAILYACTHEAD = HOST + "/daily/allInfo";//日常活动名称和顶部

    String DAILYLIST = HOST + "/daily/lists";//日常活动列表
}
