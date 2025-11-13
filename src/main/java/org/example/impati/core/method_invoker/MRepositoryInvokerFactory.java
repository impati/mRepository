package org.example.impati.core.method_invoker;

import java.util.List;
import org.example.impati.core.MStore;
import org.example.impati.core.PropertyAccess;

public class MRepositoryInvokerFactory {

    private MRepositoryInvokerFactory() {
    }

    public static <E> List<MRepositoryMethodInvoker<E>> defaultCreate(Class<E> entityType) {
        MStore<Object, E> store;
        store = new MStore<>(entityType);

        return List.of(
                new SaveInvoker<>(store),
                new FindByIdInvoker<>(store),
                new FindAllInvoker<>(store),
                new FindByInvoker<>(store, PropertyAccess.forType(store.entityType())),
                new DeleteInvoker<>(store)
        );
    }
}
