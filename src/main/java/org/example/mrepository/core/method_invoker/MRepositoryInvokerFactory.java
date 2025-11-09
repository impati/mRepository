package org.example.mrepository.core.method_invoker;

import java.util.List;
import org.example.mrepository.core.MStore;
import org.example.mrepository.core.PropertyAccess;

public class MRepositoryInvokerFactory {

    private MRepositoryInvokerFactory() {
    }

    public static <E> List<MRepositoryMethodInvoker<E>> create(Class<E> entityType) {
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
