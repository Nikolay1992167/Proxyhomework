package by.clevertec.proxy;

import by.clevertec.cach.Cache;
import by.clevertec.dao.CarDAO;
import by.clevertec.entity.Car;
import by.clevertec.proxy.annotations.ReflectionCheck;
import by.clevertec.proxy.factory.Action;
import by.clevertec.proxy.factory.ActionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.UUID;

@Component
public class MyInvocationHandler implements InvocationHandler {

    private final CarDAO target;

    private final Class<?> targetClass;

    @Autowired
    private Cache<UUID, Car> cache;

    public MyInvocationHandler(CarDAO target) {
        this.target = target;
        this.targetClass = target.getClass();
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

    private Optional<ReflectionCheck> getTransactionalMethod(Method method) {

        return Optional.ofNullable(method.getDeclaredAnnotation(ReflectionCheck.class));
    }

    private Object uncheckedInvoke(Method method, Object[] args) {

        try {
            return method.invoke(target, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else {
                throw new RuntimeException(cause);
            }
        }
    }

    private Method getOverriddenMethod(Method method) throws NoSuchMethodException {

        return targetClass.getDeclaredMethod(method.getName(), method.getParameterTypes());
    }
}
