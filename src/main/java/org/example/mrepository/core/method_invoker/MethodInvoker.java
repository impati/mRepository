package org.example.mrepository.core.method_invoker;

import java.lang.reflect.Method;

public interface MethodInvoker {

    boolean supports(Method method);

    Object invoke(Method method, Object[] args);
}
