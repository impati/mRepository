package org.example.impati.core.method_invoker;

import java.lang.reflect.Method;
import java.util.List;
import org.example.impati.core.MStore;
import org.example.impati.utils.CollectionUtils;

public class DeleteInvoker<E> extends MRepositoryMethodInvoker<E> {

    protected DeleteInvoker(final MStore<Object, E> store) {
        super(store);
    }

    @Override
    public boolean supports(final Method method) {
        return ("delete".equals(method.getName()) && method.getParameterCount() == 1) ||
                ("deleteAll".equals(method.getName()) && method.getParameterCount() == 1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(final Method method, final Object[] args) {
        Object obj = args[0];
        if (obj instanceof List<?>) {
            store.deleteAll((List<E>) CollectionUtils.toCollection(obj));
            return "";
        }

        return store.delete((E) obj);
    }
}
