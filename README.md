# bookstore-app

# Distributed Configuration
By using Consul


# Naming Server or Discovery Server or Registry Server. 
By Using Consul

To Create Consul Cluster using Docker follow the below steps:
1. Type the command ‘docker run -d --name consul-1 -p 8500:8500 -e CONSUL_BIND_INTERFACE=eth0 consul’
2. Type the command ‘docker inspect consul-1’. Copy the IP address.
3. Type the command ‘docker run -d --name consul-2 -e CONSUL_BIND_INTERFACE=eth0 -p 8501:8500 consul agent -dev -join=172.17.0.2<REPLACE THIS BY PREVIOUSLY COPIED IP ADDRESS>’
4. Type the command ‘docker run -d --name consul-3 -e CONSUL_BIND_INTERFACE=eth0 -p 8502:8500 consul agent -dev -join=172.17.0.2<REPLACE THIS BY PREVIOUSLY COPIED IP ADDRESS>’
5. Test the Consul Cluster by typing the command: ‘docker exec -t consul-1 consul members’. This will list down details of all 3 Consul nodes.
6. Access the Consul UI: http://localhost:8500/
7. Refer: https://piotrminkowski.com/2019/11/06/microservices-with-spring-boot-spring-cloud-gateway-and-consul-cluster/

# Microservices to register with Consul
By using Spring Cloud Consul

 - Add the dependency 'spring-cloud-starter-consul-all' --> This includes both spring-cloud-consul-discovery and spring-cloud-consul-config
 - Add the below configuration in bootstrap.yml
 ```
 spring:
   application:
     name: bookstore-cart-service
 ```
- Add the below configurations in application.yml
 ```
  server:
    port: 0 -> To launch multiple instances of this microservice
      name: bookstore-cart-service

  spring:
    cloud:
      consul:
        discovery:
          instance-id: ${spring.application.name}:${random.int[1,999999]}
          health-check-path: /actuator/health -> By default this url will be selected by spring clud consul if actuator is in classpath.
                                                 We need to set permitAll permission to this endpoint if spring security is in classpath
  ```
  - Check for registered information of this service in Consul by access the Consul URL: http://localhost:8500

#### To Enable Zone Affinity Mechanism in Consul
 - Define below 2 different spring profile in each application, one for zone1 and another for zone2
 ```
 spring:
   profiles: zone1
   cloud:
     consul:
       discovery:
         instanceZone: zone1
 ---
 spring:
   profiles: zone2
   cloud:
     consul:
       discovery:
         instanceZone: zone2
 ```
- Run multiple instances of each microservice by passing environment variable '-Dspring.profiles.active' as either zone1 or zone2. This will register each microservice in their respective zones by adding the corresponding tags as zone1 or zone2 in Consul.

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
  
# Inter Service Communication
By Using Spring Cloud OpenFeign  

 - Add the dependency 'spring-cloud-starter-openfeign'
 - The OpenFeign client is auto-integrated with service discovery. To use it we need to declare an interface with required methods for communication. 
 - The interface has to be annotated with @FeignClient that points to the service using its discovery name.
 ```
 @FeignClient("bookstore-address-service")
 public interface AddressFeignClient { 
     @GetMapping("/v1/billing-addresses/{id}")
     BillingAddressResponse getBillingAddressById(@PathVariable("id") String id);
 }
 ```
 - Finally, add the annotation @EnableFeignClients to main application
 ```
 @EnableFeignClients(basePackages = {"com.bookstore.learning.adapter.client"})
 public class BookstoreOrderServiceApplication {
     public static void main(String[] args) {
       SpringApplication.run(BookstoreOrderServiceApplication.class, args);
     }
 }
 ```

# Authorization Server
By Using Embedded Keycloak
 - Keycloak is an OAuth2 Authorization Server which will implement both OAuth2 and OpenId Connect protocols to provide access and id tokens for OAuth2 Client applications. 
 - These tokens will contains user identity and access/role related information
 - Later OAuth2 client applications will use these tokens to access any protected resources from OAuth2 Resource Server.
 - Follow the list of available links here: https://www.baeldung.com/tag/keycloak/
 - By following the above link, we can implement an Embedded Keycloak Authorization server with build in support for the user registration, user login, Forgot Password, Remember Me, SSO, custom Themes and Pages etc
 - bookstore-realm.json file contains all the configuration in the json form, to create Keycloak Realm(Bookstore), Client(product-service), Roles(Admin, Buyer), Users(Admin User, Buyer User) with a password and mapped roles etc.
 
