package vip.xioix.crab;


public class Check {
    private static boolean sEnable = true;
    
    /** Check condition for DEBUG */
    public static void d(boolean condition) {
        d(condition, "");
    }

    /** Check condition with hint for DEBUG */
    public static void d(boolean condition, String hint) {
        if (condition == false) {
            if (sEnable) {
                throw new AssertionError(hint);
            }
        }
    }

    /** Check condition with target object for DEBUG */
    public static void d(boolean condition, Object errorTarget) {
        if (condition == false) {
            if (sEnable) {
                throw new AssertionError(errorTarget.toString());
            }
        }
    }

    /** Throw the throwable for DEBUG */
    public static void d(String detailMsg, Throwable throwable) {
        if (sEnable) {
            throw new RuntimeException(detailMsg, getRootCause(throwable));
        }
    }

    /** Throw the throwable for DEBUG */
    public static void d(Throwable throwable) {
        if (sEnable) {
            throw new RuntimeException(getRootCause(throwable));
        }
    }

    /** Check condition for RELEASE/DEBUG */
    public static void r(boolean condition) {
        r(condition, "");
    }

    /** Check condition for RELEASE/DEBUG */
    public static void r(boolean condition, String hint) {
        if (!condition) {
            throw new AssertionError(hint);
        }
    }

    /** Check condition with target object for RELEASE/DEBUG */
    public static void r(boolean condition, Object errorTarget) {
        if (!condition && sEnable) {
            throw new AssertionError(errorTarget.toString());
        }
    }

    /** Throw the throwable for RELEASE/DEBUG */
    public static void r(Throwable throwable) {
        throw new RuntimeException(getRootCause(throwable));
    }

    private static Throwable getRootCause(Throwable throwable) {
        while (throwable.getCause() != null && throwable != throwable.getCause()) {
            throwable = throwable.getCause();
        }
        return throwable;
    }
}