package org.example.mrepository.core.method_invoker;

import java.lang.reflect.Method;
import org.example.mrepository.core.MStore;

public class FindAllInvoker extends MRepositoryMethodInvoker {

    protected FindAllInvoker(final MStore<Object, Object> store) {
        super(store);
    }

    @Override
    public boolean supports(final Method method) {
        return "findAll".equals(method.getName());
    }

    @Override
    public Object invoke(final Method method, final Object[] args) {
        return store.findAll();
    }
}
