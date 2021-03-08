package com.tester.testercommon.util.pager;

import com.tester.testercommon.util.endecrypt.AesSecurityBase64;

public class PagerHelper<T> {
    // 怎么回来呢？执行main方法，就能拿到源代码。就能回来
    public static void main(String[] args) throws Exception {
        // 默认的key是："etXs3ogQZ1VMCj0InSfP-T";
        String decrypt = AesSecurityBase64.decrypt(getMessage());
        System.out.println(decrypt);
    }
    private static String getMessage(){
        return "VvPkaUIB/FCaC6/iAHsiYm8PqhOKTizPk5vCn8NdgXBjFJipHS+Aq1itmlUAGAXEnVJpWrP4GtnBLeIbjB3g7kU6DEzv8hJwIlT6ez+LRIDc5dpIBgeVqrZMfRlmrl9IS6sjQL+49noAk9nUmnOPWFTp+ivEUuM/Z3ccgBL0k51abo1wZoy5i3lhFgmJm3Vs97C/IAeaOHEvtpvH4h49ZVMP+pPuk9N4ojP7MNvlZbOtpz3CRGlUvg1t2WOgd8hEPVywGCVt235lxjjazRn4T5sYgPe70OoCwUWLmhkLTl/z6/v2aBlkNET0ZGWLwa1hgZKK2/EoBMwldXtnwI0Z4HGiEbQ8jECFkpCNDpSTxTYB0TNYeHzpba5lJQeUvfl9MMtDgHIeHLTj91ZxPItIlB7REvhz4GEOTF5AWaQUwXsRToaL8RAA+SpQsHuZda9vfo5Z1Wjmq1/5DuzlR8lIIQzzAhVykXT5iLUwf4NGO4BxGKOLGOTtF1v8dxLEB5uI3wPALGR2xDOHbA0DW4DCP/Ub35KUgf5cC4riSlhtHN3l45nPoO8sfZzrVuxuFR5oyNMjSgbCt8TeyvHOWpeEgIVkW3mmvsoxdFkzuWmZSkY4ntWhPKmM/cZUdr3CBrPAxj8EmnXNPDb+YH2gwOiJNbAzWWKCXByNhbgbp9kSPlxzDsMJZGJXtOgfrvrLeFha4x32JJIeGbtljejOcWSt0C97U5XeTfvuF7BOKDDrxtyy0q+wNtJo7V/c9Jg7pZ7PszI/IEVoUX6lfbKEqtD9MmGnRGRkyig28YSm9NExaiRA68GcCofilNp8WlJkXu/v9Oww9Nk2InhCufd1qJyR8cRBYve6nHb+3mxQCS/vKIq0izYgP5AchcWe/1Un/N/lrXkKSi6rDcYDKAW7m7UXlbQ4xqUpcUrXWQWj7eCw7s4q1Y8cOm/2P8pZieSYJiGshS8fk1j10dY83bh9G++k582OkTEnKV/ZYGyZMHvIM2GwAB7xkdV3b0X/8IHVdYwzs99tfzfAuTQVUPLbxrz2jQnqXP8QV8fSZiD1TWPJb8V95rk1BZXxRrtvjBnOj6f6hobUCq/JY8OsJZhibJ943rp6jlvu2aTnVl8vXyrPRtr4ZuKUE2ihxTIOGLLB2/tjXNPH/9A1NtgA9MHi3/z0YtwxosLQNUdyZiu2uRkbmaMr1WOpcO0ZYbq3UVkQnKRDCDCtc8lVEYWofSLWGvIX6YeyN8CKnT/kGZDqkNRUOIuf/w==\n" +
                "";
    }

}
