package com.zeyi.user.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 返回信息
 *
 * @author sujiahua
 * @date 2023/3/9
 */
@Data
public final class MsgInfo implements Serializable {


    public static final String MSG_SUCCESS = "操作成功!";

    public static final String MSG_FIALURE = "操作失败,请联系管理员!";

    private static final long serialVersionUID = 304173106457825499L;

    public MsgInfo() {
        this.setMassage(MSG_SUCCESS);
    }

    /**
     * 返回码
     */
    private Integer code;
    /**
     * 返回信息
     */
    private String massage;
    /**
     * 返回数据
     */
    private Object data;

}
