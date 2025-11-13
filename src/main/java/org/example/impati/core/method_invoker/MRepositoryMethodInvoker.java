package org.example.impati.core.method_invoker;

import java.lang.reflect.Method;
import org.example.impati.core.MStore;

public interface MRepositoryMethodInvoker<E> {

    boolean supports(Method method);

    Object invoke(MStore<Object, E> store, Method method, Object[] args);
}
