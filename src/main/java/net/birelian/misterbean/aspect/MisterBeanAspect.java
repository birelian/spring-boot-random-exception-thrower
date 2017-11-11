package net.birelian.misterbean.aspect;

import org.apache.commons.lang3.RandomUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * MisterBean aspect.
 *
 * This class is responsible of throwing (or not) a random exception of a type defined in the application properties.
 */
@Aspect
@Component
public class MisterBeanAspect {

    /** List of exceptions */
    @Value("#{'${misterBean.exceptions}'.split(',')}")
    private List<String> exceptions;

    /** Message used to create the exceptions */
    @Value("${misterBean.exceptionMessage}")
    private String message;

    @Before("@annotation(MisterBean)")
    public void execute(JoinPoint joinPoint) throws Exception {

        if (RandomUtils.nextBoolean()) {
            return; // You are lucky! No exception thrown
        }

        // Get a random exception
        String exception = exceptions.get(RandomUtils.nextInt(0, exceptions.size()));

        // Throwable class
        Class<?> clazz = Class.forName(exception);

        // Constructor that accepts a message
        Constructor<?> constructor = clazz.getConstructor(String.class);

        throw (Exception) constructor.newInstance(message);

    }
}
