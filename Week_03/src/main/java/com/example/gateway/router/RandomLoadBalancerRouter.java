package com.example.gateway.router;

import java.util.List;
import java.util.Random;


public class RandomLoadBalancerRouter implements HttpEndpointRouter {
    private Random random = new Random();

    @Override
    public String route(List<String> endpoints) {
        int size = endpoints.size();
        int nextInt = random.nextInt(size * 100);
        int slot = nextInt % size;
        return endpoints.get(slot);
    }
}
