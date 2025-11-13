package org.example.impati.core.method_invoker;

import java.lang.reflect.Method;
import org.example.impati.core.MStore;

public class FindAllInvoker<E> implements MRepositoryMethodInvoker<E> {

    @Override
    public boolean supports(final Method method) {
        return "findAll".equals(method.getName());
    }

    @Override
    public Object invoke(final MStore<Object, E> store, final Method method, final Object[] args) {
        return store.findAll();
    }
}
