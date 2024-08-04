package model;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SubclassFinder {

    public static List<Class<?>> getAllClasses(String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(URLDecoder.decode(resource.getFile(), "UTF-8")));
        }
        List<Class<?>> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    assert !file.getName().contains(".");
                    classes.addAll(findClasses(file, packageName + "." + file.getName()));
                } else if (file.getName().endsWith(".class")) {
                    classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
                }
            }
        }
        return classes;
    }

    public static CopyOnWriteArrayList<Class<?>> findEnemySubclasses(String packageName, Class<?> superClass) throws ClassNotFoundException, IOException {
        List<Class<?>> allClasses = getAllClasses(packageName);
        CopyOnWriteArrayList<Class<?>> subclasses = new CopyOnWriteArrayList<>();
        for (Class<?> clazz : allClasses) {
            if (superClass.isAssignableFrom(clazz) && !clazz.equals(superClass)) {
                subclasses.add(clazz);
            }
        }
        return subclasses;
    }
}

