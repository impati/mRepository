package org.example.impati.core;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public abstract class AbstractInvocationHandler implements InvocationHandler {

    private final Class<?> repoInterface;

    protected AbstractInvocationHandler(Class<?> repoInterface) {
        this.repoInterface = repoInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Object 기본 메서드
        if (method.getDeclaringClass() == Object.class) {
            return switch (method.getName()) {
                case "toString" -> repoInterface.getName() + " proxy";
                case "hashCode" -> System.identityHashCode(proxy);
                case "equals" -> proxy == args[0];
                default -> method.invoke(this, args);
            };
        }

        Object doneInvoke = doInvoke(proxy, method, args);
        if (doneInvoke != null) {
            return doneInvoke;
        }

        // default method
        if (method.isDefault()) {
            final Constructor<Lookup> c =
                    MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
            c.setAccessible(true);
            return c.newInstance(repoInterface, MethodHandles.Lookup.PRIVATE)
                    .unreflectSpecial(method, repoInterface)
                    .bindTo(proxy)
                    .invokeWithArguments(args == null ? new Object[0] : args);
        }

        throw new UnsupportedOperationException("Unsupported method: " + method);
    }

    @SuppressWarnings("unchecked")
    public <T> T create() {
        return (T) Proxy.newProxyInstance(
                repoInterface.getClassLoader(),
                new Class<?>[]{repoInterface},
                this
        );
    }

    public abstract Object doInvoke(Object proxy, Method method, Object[] args);
}
