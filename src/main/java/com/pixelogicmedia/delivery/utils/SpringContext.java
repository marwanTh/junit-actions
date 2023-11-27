package com.pixelogicmedia.delivery.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class SpringContext implements ApplicationContextAware {

    private static ApplicationContext context;

    private static Environment environment;

    /**
     * Returns the Spring managed bean instance of the given class type if it exists.
     * Returns null otherwise.
     *
     * @param beanClass
     * @return
     */
    public static <T extends Object> T getBean(final Class<T> beanClass) {
        return context.getBean(beanClass);
    }

    /**
     * Private method context setting (better practice for setting a static field in a bean
     * instance - see comments of this article for more info).
     */
    private static synchronized void setContext(final ApplicationContext context) {
        SpringContext.context = context;
        SpringContext.environment = context.getEnvironment();
    }

    public static String getConfigurationValue(final String key) {
        return SpringContext.environment.getProperty(key);
    }

    @Override
    public void setApplicationContext(final ApplicationContext context) throws BeansException {
        // store ApplicationContext reference to access required beans later on
        setContext(context);
    }
}