package by.clevertec.proxy;

import by.clevertec.cach.Cache;
import by.clevertec.cach.cacheImpl.LFUCacheImpl;
import by.clevertec.cach.cacheImpl.LRUCacheImpl;
import by.clevertec.config.LoadProperties;
import by.clevertec.dao.CarDAO;
import by.clevertec.entity.Car;
import by.clevertec.exception.ProxyInvocationFailedException;
import by.clevertec.proxy.annotations.MyAnnotation;
import by.clevertec.proxy.pattern.Action;
import by.clevertec.proxy.pattern.ActionFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.UUID;

public class MyInvocationHandler implements InvocationHandler {

    private final CarDAO target;
    private final Class<?> targetClass;
    private final Cache<UUID, Car> cache;

    public MyInvocationHandler(CarDAO target) {
        this.target = target;
        this.targetClass = target.getClass();
        this.cache = getTypeAlgorithmCache();
    }

    private Cache<UUID, Car> getTypeAlgorithmCache() {
        return switch (new LoadProperties().getCACHE_ALGORITHM()) {
            case "LFUCacheImpl" -> new LFUCacheImpl<>();
            case "LRUCacheImpl" -> new LRUCacheImpl<>();
            default -> throw new IllegalArgumentException("Неверно указан алгоритм кэширования.");
        };
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Method targetMethod = getOverriddenMethod(method);
        return getTransactionalMethod(targetMethod)
                .map(annotation -> handleTransactionalMethod(method, args))
                .orElseGet(() -> uncheckedInvoke(method, args));
    }

    private Object handleTransactionalMethod(Method method, Object[] args) {

        String methodName = method.getName();
        ActionFactory actionFactory = new ActionFactory(target, cache);
        Action action = actionFactory.getAction(methodName);
        return action.execute(args);
    }

    private Optional<MyAnnotation> getTransactionalMethod(Method method) {
        return Optional.ofNullable(method.getDeclaredAnnotation(MyAnnotation.class));
    }

    private Object uncheckedInvoke(Method method, Object[] args) throws RuntimeException {

        try {
            return method.invoke(target, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ProxyInvocationFailedException("Не удалось вызвать метод " + method.getName(), e);
        }
    }

    private Method getOverriddenMethod(Method method) throws NoSuchMethodException {
        return targetClass.getDeclaredMethod(method.getName(), method.getParameterTypes());
    }
}
