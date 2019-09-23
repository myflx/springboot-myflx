package com.myflx.validation.validator;

import com.myflx.validation.IValid;
import com.myflx.validation.annotation.UserConstraint;
import com.myflx.vo.UserVO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/**
 * @author LuoShangLin
 */
public class UserConstraintValidator implements ConstraintValidator<UserConstraint, UserVO> {
    /**
     * Initializes the validator in preparation for
     * {@link #isValid(Object, ConstraintValidatorContext)} calls.
     * The constraint annotation for a given constraint declaration
     * is passed.
     * <p>
     * This method is guaranteed to be called before any use of this instance for
     * validation.
     * <p>
     * The default implementation is a no-op.
     *
     * @param constraintAnnotation annotation instance for a given constraint declaration
     */
    @Override
    public void initialize(UserConstraint constraintAnnotation) {

    }

    /**
     * Implements the validation logic.
     * The state of {@code value} must not be altered.
     * <p>
     * This method can be accessed concurrently, thread-safety must be ensured
     * by the implementation.
     *
     * @param value   object to validate
     * @param context context in which the constraint is evaluated
     * @return {@code false} if {@code value} does not pass the constraint
     */
    @Override
    public boolean isValid(UserVO value, ConstraintValidatorContext context) {
        if (value instanceof IValid){

        }
        return false;
    }
}
