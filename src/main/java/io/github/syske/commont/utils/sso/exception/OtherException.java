package io.github.syske.commont.utils.sso.exception;

/**
 * @program: syske-common-utils
 * @description: 其他异常
 * @author: syske
 * @create: 2020-03-21 16:19
 */
public class OtherException extends RuntimeException{
    private String message;

    public OtherException() {
        super();
    }


    public OtherException(String message, Exception e) {
        super(e);
        this.message = message;
    }

    public OtherException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        if (message == null || message.length() == 0) {
            return super.getMessage();
        } else {
            return message;
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
