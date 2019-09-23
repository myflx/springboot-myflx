package com.myflx.validation;


/**
 * @author LuoShangLin
 */
public class MyflxParamException extends RuntimeException{
    private static final long serialVersionUID = 7458349545356463860L;

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public MyflxParamException(String message) {
        super(message);
    }
}
