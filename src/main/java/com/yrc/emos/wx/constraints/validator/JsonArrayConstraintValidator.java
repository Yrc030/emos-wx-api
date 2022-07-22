package com.yrc.emos.wx.constraints.validator;

import cn.hutool.json.JSONUtil;
import com.yrc.emos.wx.constraints.annotations.JSONArrayType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Description:
 * User: joker
 * Date: 2022-06-11-17:22
 * Time: 17:22
 */
public class JsonArrayConstraintValidator implements ConstraintValidator<JSONArrayType, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return JSONUtil.isTypeJSONArray(value);
    }
}
