package com.tester.testercommon.util.pager;

import com.tester.base.dto.model.request.PagerInfo;
import com.tester.testercommon.util.endecrypt.AesSecurityBase64;

public class PagerHelper<T> {
    public static void main(String[] args) throws Exception {
        AesSecurityBase64.encrypt(getMessage());
    }

    private static String getMessage() {
        return "";
    }

    protected static final ThreadLocal<PagerInfo> LOCAL_PAGE = new ThreadLocal();

    public PagerHelper() {
    }

    protected static void setLocalPage(PagerInfo page) {
        LOCAL_PAGE.set(page);
    }

    public static <T> PagerInfo<T> getLocalPage() {
        return (PagerInfo) LOCAL_PAGE.get();
    }

    public static void clearPage() {
        LOCAL_PAGE.remove();
    }

    public static <E> PagerInfo<E> startPage(PagerInfo pagerInfo) {
        if (pagerInfo == null) {
            pagerInfo = new PagerInfo();
        }

        setLocalPage(pagerInfo);
        return pagerInfo;
    }
}


