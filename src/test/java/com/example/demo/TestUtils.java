package com.example.demo;

import java.lang.reflect.Field;

public class TestUtils {

    public static void injectObject(Object target, String fieldName, Object toInject){

        boolean wasPrivate = false;
        try{
            Field f = target.getClass().getDeclaredField(fieldName);

            //Reviewer(s): I am using Java 11! what is shown in the content which is:  "isAccessible()" was deprecated.
            //https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/reflect/AccessibleObject.html
            if(!f.canAccess(target)){
                f.setAccessible(true);
                wasPrivate = true;
            }

            f.set(target, toInject);

            if(wasPrivate){
                f.setAccessible(false);
            }

        }catch (NoSuchFieldException e){
            e.printStackTrace();
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }


    }
}
