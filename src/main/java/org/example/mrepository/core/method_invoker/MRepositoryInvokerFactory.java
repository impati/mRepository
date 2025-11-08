package org.example.mrepository.core.method_invoker;

import java.util.ArrayList;
import java.util.List;
import org.example.mrepository.core.MStore;
import org.example.mrepository.core.PropertyAccess;

public class MRepositoryInvokerFactory {

    public static <E> List<MRepositoryMethodInvoker> create(Class<E> entityType) {
        MStore<Object, Object> store = new MStore(entityType);

        List<MRepositoryMethodInvoker> methodInvokers = new ArrayList<>();
        methodInvokers.add(new FindByIdInvoker(store));
        methodInvokers.add(new SaveInvoker(store));
        methodInvokers.add(new FindByInvoker(store, PropertyAccess.forType(store.entityType())));
        methodInvokers.add(new FindAllInvoker(store));
        methodInvokers.add(new SaveInvoker(store));
        methodInvokers.add(new DeleteInvoker(store));

        return methodInvokers;
    }
}
