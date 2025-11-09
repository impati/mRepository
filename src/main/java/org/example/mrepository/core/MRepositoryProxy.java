package org.example.mrepository.core;

import java.lang.reflect.Method;
import java.util.List;
import org.example.mrepository.core.method_invoker.MRepositoryMethodInvoker;

public class MRepositoryProxy<E> extends AbstractInvocationHandler {

    private final List<MRepositoryMethodInvoker<E>> methodInvokers;

    public MRepositoryProxy(Class<?> repoInterface, List<MRepositoryMethodInvoker<E>> methodInvokers) {
        super(repoInterface);
        this.methodInvokers = methodInvokers;
    }

    @Override
    public Object doInvoke(Object proxy, Method method, Object[] args) {
        for (MRepositoryMethodInvoker<E> methodInvoker : methodInvokers) {
            if (methodInvoker.supports(method)) {
                return methodInvoker.invoke(method, args);
            }
        }

        return null;
    }
}
