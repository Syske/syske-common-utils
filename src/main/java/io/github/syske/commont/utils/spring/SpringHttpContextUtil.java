package io.github.syske.commont.utils.spring;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: syske-common-utils
 * @description: 从RequestContextHolder中获取HttpServletRequest和HttpServletResponse
 * @author: syske
 * @create: 2019-11-21 09:47
 */
public class SpringHttpContextUtil {

    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }


    public static HttpServletResponse getHttpServletResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }



}
