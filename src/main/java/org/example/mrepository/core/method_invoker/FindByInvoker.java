package org.example.mrepository.core.method_invoker;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.example.mrepository.core.MStore;
import org.example.mrepository.core.PropertyAccess;

public class FindByInvoker<E> extends MRepositoryMethodInvoker<E> {

    private final PropertyAccess<E> props;

    public FindByInvoker(MStore<Object, E> store, PropertyAccess<E> props) {
        super(store);
        this.props = props;
    }

    @Override
    public boolean supports(final Method method) {
        return method.getName().startsWith("findBy");
    }

    @Override
    public Object invoke(final Method method, final Object[] args) {
        var sig = method.getName().substring("findBy".length()); // e.g. "AgeAndName"
        var tokens = splitByAnd(sig);                            // ["Age", "Name"]
        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("findBy requires at least one property");
        }

        if (args == null || args.length != tokens.size()) {
            throw new IllegalArgumentException("Parameter count mismatch for " + method.getName());
        }

        // 프로퍼티 이름을 camelCase로 정규화
        var propsNames = tokens.stream().map(this::lowerFirst).toList();

        // 필터링 (동등 비교만)
        List<E> result = store.values().stream()
                .filter(e -> matchesAll(e, propsNames, args))
                .toList();

        // 반환 타입 처리 (List<E> 또는 Optional<E>/E 등으로 확장 가능)
        Class<?> rt = method.getReturnType();
        if (List.class.isAssignableFrom(rt)) {
            return result;
        }
        if (!result.isEmpty() && rt.isAssignableFrom(store.entityType())) {
            return result.get(0); // 단건 반환 시 첫 번째
        }
        if (Optional.class.isAssignableFrom(rt)) {
            return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
        }
        return null; // not found
    }

    private boolean matchesAll(E entity, List<String> propsNames, Object[] args) {
        for (int i = 0; i < propsNames.size(); i++) {
            Object v = props.get(entity, propsNames.get(i));
            Object p = args[i];
            if (!Objects.equals(v, p)) {
                return false;
            }
        }
        return true;
    }

    private static List<String> splitByAnd(String sig) {
        // "AgeAndName" -> ["Age","Name"]
        List<String> out = new ArrayList<>();
        int start = 0;
        for (int i = 1; i < sig.length(); i++) {
            if (sig.regionMatches(true, i, "And", 0, 3)) {
                out.add(sig.substring(start, i));
                start = i + 3;
                i += 2;
            }
        }
        out.add(sig.substring(start));
        return out.stream().filter(s -> !s.isEmpty()).toList();
    }

    private String lowerFirst(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }
}
