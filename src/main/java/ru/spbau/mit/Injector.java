package ru.spbau.mit;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;


public final class Injector {

    private static Map<Class, Object> instances = new HashMap<>();

    private Injector() {
    }

    public static Object initializeClass(Class root, List<Class> implClasses, List<Class> dependencies) throws Exception {
        Constructor constructor = root.getDeclaredConstructors()[0];

        Class[] constructorParameters = constructor.getParameterTypes();
        if (constructorParameters.equals(emptyList())) {
            Object instance = root.newInstance();
            instances.put(root, instance);
            return instance;
        }

        for (Class parameter : constructorParameters) {
            dependencies.add(parameter);
            initializeClass(parameter, implClasses, dependencies);

        }
        
        int cntImpl = 0;
        return null;
    }


    public static Object initialize(String rootClassName, List<String> implementationClassNames) throws Exception {
        Class root = Class.forName(rootClassName);

        List<Class> implClasses = new ArrayList<>();
        for (String implClassName : implementationClassNames) {
            implClasses.add(Class.forName(implClassName));
        }

        List<Class> dependencies = new ArrayList<>();
        dependencies.add(root);
        return initializeClass(root, implClasses, dependencies);
    }

}
