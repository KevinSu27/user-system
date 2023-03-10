package com.zeyi.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户操作记录-撤销使用
 *
 * @author sujiahua
 * @date 2023/3/9
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationRecord {

    /**
     * 操作方式
     */
    private String operation;

    /**
     * 用户ID
     */
    private List<Integer> idList;

}
