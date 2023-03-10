package com.zeyi.user.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 表单信息
 *
 * @author sujiahua
 * @date 2023/3/9
 */
@Data
public final class DataTable implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 9020751687627851631L;

    /**
     * 总数
     */
    private Long total;

    /**
     * 数据
     */
    private List<?> data;

}