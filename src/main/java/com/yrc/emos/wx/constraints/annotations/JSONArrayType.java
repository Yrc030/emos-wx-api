package com.yrc.emos.wx.constraints.annotations;

import com.yrc.emos.wx.constraints.validator.JsonArrayConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Description:
 * User: joker
 * Date: 2022-06-11-17:21
 * Time: 17:21
 */
@Documented
@Constraint(validatedBy = {JsonArrayConstraintValidator.class})
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface JSONArrayType {

    String message() default "{com.yrc.emos.wx.constraints.annotations.JSONArrayType.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
