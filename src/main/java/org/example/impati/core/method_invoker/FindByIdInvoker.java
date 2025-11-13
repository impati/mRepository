package org.example.impati.core.method_invoker;

import java.lang.reflect.Method;
import org.example.impati.core.MStore;

public class FindByIdInvoker<E> implements MRepositoryMethodInvoker<E> {

    @Override
    public boolean supports(final Method method) {
        return "findById".equals(method.getName()) && method.getParameterCount() == 1;
    }

    @Override
    public Object invoke(final MStore<Object, E> store, final Method method, final Object[] args) {
        return store.findById(args[0]);
    }
}
