package com.example.week2_01.bean;

public class PhoneBean {

    /**
     * msg : 登录成功
     * code : 0
     * data : {"age":null,"appkey":"065011dc634e597b","appsecret":"A86AA83A05ED2AC134C5BB5011C66377","createtime":"2018-12-07T18:49:17","email":null,"fans":null,"follow":null,"gender":null,"icon":null,"latitude":null,"longitude":null,"mobile":"18525398551","money":null,"nickname":null,"password":"8F669074CAF5513351A2DE5CC22AC04C","praiseNum":null,"token":"6281B57C994BA3275EB4EB82E3811312","uid":23013,"userId":null,"username":"18525398551"}
     */

    private String msg;
    private String code;
    private final String SUCCESS_CODE="0";
    public boolean isSuccess(){
        return SUCCESS_CODE.equals(code);
    }
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
