package org.example.impati.core.method_invoker;

import org.example.impati.core.MStore;

public abstract class MRepositoryMethodInvoker<E> implements MethodInvoker {

    protected final MStore<Object, E> store;

    protected MRepositoryMethodInvoker(MStore<Object, E> store) {
        this.store = store;
    }
}
