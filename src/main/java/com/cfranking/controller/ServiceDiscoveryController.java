package com.cfranking.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceDiscoveryController {

   /* @Autowired
    private DiscoveryClient discoveryClient;

    @RequestMapping("/service-instances/{applicationName}")
    public List<ServiceInstance> serviceInstancesByApplicationName(
            @PathVariable String applicationName) {
        return this.discoveryClient.getInstances(applicationName);
    }*/
}
