package org.example.impati.core.method_invoker;

import java.lang.reflect.Method;
import org.example.impati.core.MStore;

public class UnloadInvoker<E> implements MRepositoryMethodInvoker<E> {

    public boolean supports(Method method) {
        return ("unload".equals(method.getName()) && method.getParameterCount() == 1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(final MStore<Object, E> store, final Method method, final Object[] args) {
        Object obj = args[0];
        return store.delete((E) obj);
    }
}
