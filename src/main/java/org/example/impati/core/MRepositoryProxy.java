package org.example.impati.core;

import java.lang.reflect.Method;
import java.util.List;
import org.example.impati.core.method_invoker.MRepositoryMethodInvoker;

public class MRepositoryProxy<E> extends AbstractInvocationHandler {

    private final MStore<Object, E> store;
    private final List<MRepositoryMethodInvoker<E>> methodInvokers;

    public MRepositoryProxy(Class<?> repoInterface, MStore<Object, E> store, List<MRepositoryMethodInvoker<E>> MRepositoryMethodInvokers) {
        super(repoInterface);
        this.store = store;
        this.methodInvokers = MRepositoryMethodInvokers;
    }

    @Override
    public Object doInvoke(Object proxy, Method method, Object[] args) {
        for (MRepositoryMethodInvoker<E> methodInvoker : methodInvokers) {
            if (methodInvoker.supports(method)) {
                return methodInvoker.invoke(store, method, args);
            }
        }

        throw new IllegalStateException("unsupported MRepositoryMethodInvoker proxy:%s,method:%s,args:%s".formatted(proxy, method, args));
    }
}
