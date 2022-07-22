package com.yrc.emos.wx.constraints.validator;

/**
 * Description:
 * User: joker
 * Date: 2022-06-11-17:05
 * Time: 17:05
 */
import com.yrc.emos.wx.constraints.annotations.IntListValue;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ListValueConstraintValidator implements ConstraintValidator<IntListValue, Integer> {

    private Set<Integer> set = new HashSet<>();
    /**
     * 初始化校验器
     * @param constraintAnnotation 注解的元数据
     */
    @Override
    public void initialize(IntListValue constraintAnnotation) {
        int[] values = constraintAnnotation.values();
        for (int value : values) {
            set.add(value);
        }
    }

    /**
     * 验证给定的 value 是否有效
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return set.contains(value);
    }
}
