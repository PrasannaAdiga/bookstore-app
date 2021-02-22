# API Gateway 
By using Spring Cloud Gateway

 - Use API Gateway server to expose each microservice on a static port to an external client applications, as microservices will be running on random ports
 - We don’t need to register gateway in Consul discovery, because it is not accessed internally. But, we integrate Gateway server to Consul discovery as Gateway server needs to get details of running microservice instances
 - If the Zone Affinity Mechanism in Consul is enabled, then we can run multiple instances of API Gateway in each zones
 - Add the dependencies 'spring-cloud-starter-gateway', 'spring-cloud-starter-consul-all' and 'spring-boot-starter-actuator'. If we add spring-boot-actuator, there will be new endpoint '/actuator/gateway' added by spring-cloud-gateway.
 - We can define multiple spring profiles corresponding to each zone in API Gateway
 ```
 spring:
   profiles: zone1
   cloud:
     consul:
       discovery:
         instanceZone: zone1
         register: false
         registerHealthCheck: false
         tags: zone=zone1
 server:  
   port: ${PORT:8080}
 ```
 - To enable integration with Consul discovery, set the below property
 ```
 spring:
   cloud:
     gateway:
       discovery:
         locator:
           enabled: true
 ```
 - Then we can define multiple Route definition for each microservice. By default, Spring Cloud Gateway uses Spring Cloud Load Balancer for load balancing.
 - Run multiple instances of gateway server by passing environment variable '-Dspring.profiles.active' as either zone1 or zone2. This will create different gateway server in their respective zones by adding the corresponding tags as zone1 or zone2 in Consul.
 - Now we can be sure that each incoming request to gateway server started in zone1 would be forwarded to only those microservices which are also having the tag of 'zone1'. And the same for Zone2.
 - To access gateway server running on port 8080(zone1)/9080(zone2): http://localhost:<port>/<service-name-defined-in-gateway-server>/<service-path>
