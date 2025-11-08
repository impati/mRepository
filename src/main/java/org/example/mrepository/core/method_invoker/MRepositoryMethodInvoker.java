package org.example.mrepository.core.method_invoker;

import org.example.mrepository.core.MStore;

public abstract class MRepositoryMethodInvoker implements MethodInvoker {

    protected final MStore<Object, Object> store;

    protected MRepositoryMethodInvoker(MStore<Object, Object> store) {
        this.store = store;
    }
}
