package com.fachinis.mc.plugins.services;

import java.util.ArrayList;
import java.util.List;

public class InjectorService {
    
    private InjectorService() {}

    private static InjectorService instance;

    public static InjectorService getInstance() {
        if (instance == null) {
            instance = new InjectorService();
        }
        return instance;
    }

    private ArrayList<Component> components = new ArrayList<>();

    public void registerComponent(Component component) {
        components.add(component);
    }

    public void registerMultipleComponents(List<Component> components) {
        for (Component item: components) {
            this.components.add(item);
        }
    }

    public <T extends Component> T inject(Class<T> type) {
        for (Component service : this.components) {
            if (type.isInstance(service)) {
                return type.cast(service);
            }
        }
        return null;
    }
}
