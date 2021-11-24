package com.tester.testercommon.util;

import io.netty.util.internal.ReflectionUtil;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * @Author 温昌营
 * @Date 2021-11-24 14:11:26
 */
public class UnsafeUtil {
    public static final Unsafe UNSAFE;

    static {
        final Object maybeUnsafe = AccessController.doPrivileged(new PrivilegedAction<Object>() {
            @Override
            public Object run() {
                try {
                    final Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
                    // We always want to try using Unsafe as the access still works on java9 as well and
                    // we need it for out native-transports and many optimizations.
                    Throwable cause = ReflectionUtil.trySetAccessible(unsafeField, false);
                    if (cause != null) {
                        return cause;
                    }
                    // the unsafe instance
                    return unsafeField.get(null);
                } catch (NoSuchFieldException e) {
                    return e;
                } catch (SecurityException e) {
                    return e;
                } catch (IllegalAccessException e) {
                    return e;
                } catch (NoClassDefFoundError e) {
                    // Also catch NoClassDefFoundError in case someone uses for example OSGI and it made
                    // Unsafe unloadable.
                    return e;
                }
            }
        });
        UNSAFE = (Unsafe) maybeUnsafe;
    }
}
