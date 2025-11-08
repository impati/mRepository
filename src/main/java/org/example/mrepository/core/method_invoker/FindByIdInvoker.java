package org.example.mrepository.core.method_invoker;

import java.lang.reflect.Method;
import org.example.mrepository.core.MStore;

public class FindByIdInvoker extends MRepositoryMethodInvoker {

    public FindByIdInvoker(MStore<Object, Object> store) {
        super(store);
    }

    @Override
    public boolean supports(final Method method) {
        return "findById".equals(method.getName()) && method.getParameterCount() == 1;
    }

    @Override
    public Object invoke(final Method method, final Object[] args) {
        return store.findById(args[0]);
    }
}
