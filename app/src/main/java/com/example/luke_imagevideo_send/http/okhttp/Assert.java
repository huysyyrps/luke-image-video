package com.example.luke_imagevideo_send.http.okhttp;

/**
 * @author xingwei.huang (xwdz9989@gamil.com)
 * @since 2019/3/21
 */
public class Assert {

    public static void checkNull(Object object, String message) {
        if (object == null)
            throw new NullPointerException(message);
    }
}
