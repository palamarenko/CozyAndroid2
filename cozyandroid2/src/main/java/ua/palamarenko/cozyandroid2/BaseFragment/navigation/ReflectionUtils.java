package ua.palamarenko.cozyandroid2.BaseFragment.navigation;


import java.lang.reflect.ParameterizedType;

public class ReflectionUtils {


    public static Class getGenericParameterClass(Class actualClass, int parameterIndex) {
        return (Class) ((ParameterizedType) actualClass.getGenericSuperclass()).getActualTypeArguments()[parameterIndex];
    }
}
