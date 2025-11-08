package org.example.mrepository.core.method_invoker;

import java.lang.reflect.Method;
import java.util.List;
import org.example.mrepository.core.MStore;
import org.example.mrepository.utils.CollectionUtils;

public class DeleteInvoker extends MRepositoryMethodInvoker {

    protected DeleteInvoker(final MStore<Object, Object> store) {
        super(store);
    }

    @Override
    public boolean supports(final Method method) {
        return ("delete".equals(method.getName()) && method.getParameterCount() == 1) ||
                ("deleteAll".equals(method.getName()) && method.getParameterCount() == 1);
    }

    @Override
    public Object invoke(final Method method, final Object[] args) {
        Object obj = args[0];
        if (obj instanceof List<?>) {
            store.deleteAll(CollectionUtils.toCollection(obj));
            return "";
        }

        return store.delete(obj);
    }
}
