package org.example.mrepository.core.method_invoker;

import org.example.mrepository.core.MStore;

public abstract class MRepositoryMethodInvoker<E> implements MethodInvoker {

    protected final MStore<Object, E> store;

    protected MRepositoryMethodInvoker(MStore<Object, E> store) {
        this.store = store;
    }
}
