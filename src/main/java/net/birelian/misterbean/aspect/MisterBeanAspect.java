package net.birelian.misterbean.aspect;

import net.birelian.misterbean.exception.MisterBeanException;
import org.apache.commons.lang3.RandomUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
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

    /** Value set by Spring by using property setter so validations can be during bean creation */
    private int defaultProbabilityFactor;

    @Before("@annotation(MisterBean)")
    public void execute(JoinPoint joinPoint) throws Exception {

        if (RandomUtils.nextInt(0, getProbabilityFactor(joinPoint)) != 0) {
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

    /**
     * Get the probability factor. Use the probabilityFactor attribute of @MisterBean when a value greater than 0 is set
     * or use the default probability factor otherwise.
     *
     * @param joinPoint The join point
     *
     * @return The probability factor
     *
     * @throws NoSuchMethodException If any
     */
    private int getProbabilityFactor(JoinPoint joinPoint) throws NoSuchMethodException {

        int annotationProbabilityFactor = getAnnotationProbabilityFactor(joinPoint);

        return annotationProbabilityFactor > 0 ? annotationProbabilityFactor : defaultProbabilityFactor;

    }

    /**
     * Get the probability factor from the @MisterBean annotation
     *
     * @param joinPoint The join point
     *
     * @return The probability factor
     *
     * @throws NoSuchMethodException If any
     */
    private int getAnnotationProbabilityFactor(JoinPoint joinPoint) throws NoSuchMethodException {

        final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        Method method = methodSignature.getMethod();

        if (method.getDeclaringClass().isInterface()) {

            final String methodName = joinPoint.getSignature().getName();
            method = joinPoint.getTarget().getClass().getDeclaredMethod(methodName, method.getParameterTypes());
        }

        MisterBean misterBean = method.getAnnotation(MisterBean.class);

        return misterBean.probabilityFactor();

    }

    @Value("${misterBean.defaultProbabilityFactor}")
    public void setDefaultProbabilityFactor(final int defaultProbabilityFactor) {

        // Validate the value
        if (defaultProbabilityFactor <= 0) {
            throw new MisterBeanException("Ha ha ha!!! What are you trying to do??? Hacker...");
        }

        this.defaultProbabilityFactor = defaultProbabilityFactor;
    }
}
