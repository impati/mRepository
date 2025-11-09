package org.example.impati.core.method_invoker;

import java.lang.reflect.Method;
import java.util.List;
import org.example.impati.core.MStore;
import org.example.impati.utils.CollectionUtils;

public class SaveInvoker<E> extends MRepositoryMethodInvoker<E> {

    public SaveInvoker(final MStore<Object, E> store) {
        super(store);
    }

    public boolean supports(Method method) {
        return ("save".equals(method.getName()) && method.getParameterCount() == 1) ||
                ("saveAll".equals(method.getName()) && method.getParameterCount() == 1);
    }

    @SuppressWarnings("unchecked")
    public Object invoke(Method method, Object[] args) {
        Object obj = args[0];
        if (obj instanceof List<?>) {
            store.saveAll((List<E>) CollectionUtils.toCollection(obj));
            return "";
        }

        return store.save((E) obj);
    }
}
