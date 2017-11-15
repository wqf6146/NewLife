package com.yhkj.yymall.config;

/**
 * Created by Administrator on 2017/11/14.
 */

public class LocalActUltils {
    public static final int ACT_OFFLINE = 0;

    /**
     * 对比指定bit位上的值
     * @param val
     * @return
     */
    public static boolean compareActBitVal(int val,int bitpos){
        return ((val & (1 << bitpos)) == 1);//true 表示第i位为1,否则为0
    }
    //将 整数 val 的第 i 位的值 置为 1
    public static int setActBit(int val,int bitpos) {
        return (val | (1 << bitpos));
    }
}
