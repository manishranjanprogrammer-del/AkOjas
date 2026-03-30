package com.ojassoft.astrosage.tagmanager;

import android.util.Log;

import com.google.android.gms.tagmanager.Container;
import com.google.android.gms.tagmanager.ContainerHolder;

import java.util.Map;

/**
 * Created by ojas on 28/12/16.
 */

public class ContainerLoadedCallback implements ContainerHolder.ContainerAvailableListener {
    @Override
    public void onContainerAvailable(ContainerHolder containerHolder, String s) {
        // We load each container when it becomes available.
        Container container = containerHolder.getContainer();
        registerCallbacksForContainer(container);
    }

    public static void registerCallbacksForContainer(Container container) {
        // Register two custom function call macros to the container.
        container.registerFunctionCallMacroCallback("increment", new CustomMacroCallback());
        container.registerFunctionCallMacroCallback("mod", new CustomMacroCallback());
        // Register a custom function call tag to the container.
        container.registerFunctionCallTagCallback("custom_tag", new CustomTagCallback());
    }

    private static class CustomMacroCallback implements Container.FunctionCallMacroCallback {
        private int numCalls;

        @Override
        public Object getValue(String name, Map<String, Object> parameters) {
            if ("increment".equals(name)) {
                return ++numCalls;
            } else if ("mod".equals(name)) {
                return (Long) parameters.get("key1") % Integer.valueOf((String) parameters.get("key2"));
            } else {
                throw new IllegalArgumentException("Custom macro name: " + name + " is not supported.");
            }
        }
    }

    private static class CustomTagCallback implements Container.FunctionCallTagCallback {
        @Override
        public void execute(String tagName, Map<String, Object> parameters) {
            // The code for firing this custom tag.
            //Log.i("CuteAnimals", "Custom function call tag :" + tagName + " is fired.");
        }
    }
}
