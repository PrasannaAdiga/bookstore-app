# bookstore-app

# Distributed Configuration
By using Spring Cloud Config Server and Config Client

Register Config Server with Consul
#### Config Server
 - Add the dependency 'spring-cloud-config-server' and 'spring-cloud-starter-consul-all'
 - Add the annotation '@EnableConfigServer' into main application
 ```
 @SpringBootApplication
 @EnableConfigServer
 public class BookstoreConfigServerApplication {
 }
 ```
 - Add below configuration details to bootstrap.yml
 ```
 spring:
   cloud:
     config:
       server:
         #git:
         #uri: #Provide Git URI here if we use to connect to Git Repo
         native:
           searchLocations: classpath:/config-repo
     cloud:
       consul:
         discovery:
           instance-id: ${spring.application.name}:${random.int[1,999999]}
 ```
 - Create a new folder 'config-repo' under the path 'src/main/resources' and create different yml files for each of the microservice with corresponding microservice's application name as file name. Keep all configurations related to different profile here.
  
#### Config Client
 - Add the dependency 'spring-cloud-starter-config' and 'spring-retry' in each of the microservices which needs to connect to Config Server
 - Add the below configuration details in each microservice 'bootstrap.yml' file in order to connect to Config Server while booting up along with spring retry configuration(Required to make microservice to wait until config server is up and running).
 ```
 cloud:
   config:
     uri: http://localhost:8888
     fail-fast: true
     retry:
       initial-interval: 60000
       multiplier: 1.5
       max-attempts: 1000
       max-interval: 5000
 ```

# Naming Server or Discovery Server or Registry Server. 
By Using Consul

#### Consul Server
To Create Consul Cluster using Docker follow the below steps:
1. Type the command ‘docker run -d --name consul-1 -p 8500:8500 -e CONSUL_BIND_INTERFACE=eth0 consul’
2. Type the command ‘docker inspect consul-1’. Copy the IP address.
3. Type the command ‘docker run -d --name consul-2 -e CONSUL_BIND_INTERFACE=eth0 -p 8501:8500 consul agent -dev -join=172.17.0.2<REPLACE THIS BY PREVIOUSLY COPIED IP ADDRESS>’
4. Type the command ‘docker run -d --name consul-3 -e CONSUL_BIND_INTERFACE=eth0 -p 8502:8500 consul agent -dev -join=172.17.0.2<REPLACE THIS BY PREVIOUSLY COPIED IP ADDRESS>’
5. Test the Consul Cluster by typing the command: ‘docker exec -t consul-1 consul members’. This will list down details of all 3 Consul nodes.
6. Access the Consul UI: http://localhost:8500/
7. Refer: https://piotrminkowski.com/2019/11/06/microservices-with-spring-boot-spring-cloud-gateway-and-consul-cluster/

#### Microservices as Consul Client
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

Register API Gateway server with Consul
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
Along with Client Side Load Balancer, Circuit Breaker and Retry

By Using Spring Cloud OpenFeign with Spring Cloud LoadBalancer(as a Load Balancer) and spring-cloud-starter-circuitbreaker-resilience4j(as a Circuit Breaker)  

 - Add the dependency 'spring-cloud-starter-openfeign', 'spring-cloud-loadbalancer and 'spring-cloud-starter-circuitbreaker-resilience4j'
 - Add the dependency 'resilience4j-micrometer' along with 'spring-boot-actuator', which will provide additional metrics related to resilience4j.
 - The OpenFeign will auto-integrate with service discovery like Consul, if 'spring-cloud-starter-consul-all' is in the classpath to get the details of running micro service instances
 - To use it we need to declare an interface with required methods for communication. Method signature must be similar to the one which is defined in the actual microservice.
 - The interface has to be annotated with @FeignClient that points to the service using its discovery name as registered in Consul.
 ```
 @FeignClient("bookstore-address-service")
 public interface AddressFeignClient { 
     @GetMapping("/v1/billing-addresses/{id}")
     BillingAddressResponse getBillingAddressById(@PathVariable("id") String id);
 }
 ```
 - Also, we can define the User Access token as a Request Parameter in Feign Interface Definition Methods. And then pass the actual user access token when we call this API from any services. 
 ```
 ProductResponse getProductById(@RequestHeader(value = "Authorization", required = true) String accessToken, @PathVariable("id") String id);
 ```
- Also, we can write custom exception for Feign Exception and global handler for the same.
- We can keep all the properties of this library in Config Server

#### Load Balancer
 - Spring Cloud OpenFeign can use Spring Cloud LoadBalance as a client side load balancer, if it exists in the classpath, and the ribbon load balancer disabled.
 ```
 spring:
   cloud:
     loadbalancer:
       ribbon:
         enabled: false
 ```

#### Circuit Breaker
 - Spring Cloud OpenFeign uses Resilience4j as Circuit Breaker if 'spring-cloud-starter-circuitbreaker-resilience4j' is in the classpath, Hystrix is disabled and the below configuration set:
 ```
 feign:
   circuitbreaker:
     enabled: true
   hystrix:
     enabled: false
 ```
 - To implement Fallback method for Circuit Breaker, the above plugin provides FallbackFactory interface, which can be implemented to provide default handler and can catch and throw the Actual Cause of the Client Calls
 - Below is such Custom FallbackFactory classes and Handlers:
 ```
 @Component
 public class ProductClientFactory implements FallbackFactory<ProductClientHandler> {
     @Override
     public ProductClientHandler create(Throwable cause) {
         return new ProductClientHandler();
     }
 }

 public class ProductClientHandler implements ProductFeignClient {
     @Override
     public ProductResponse getProductById(String id) {
         return ProductResponse.builder()
                 .availableCount(10)
                 .build();
     }
 }
 ```
 - And, finally tell Feign Client to use this Fallback method on failure:
 ```
 @FeignClient(name = "${address.service.name:bookstore-address-service}",
         fallbackFactory = AddressClientFactory.class)
 public interface AddressFeignClient {
 }
 ```

#### Retry
 - To implement retry mechanism for the failure calls for a specific number of times, if the target service is down or unable to respond.
 - For that, create a custom Retry class and configure it as below:
 ```
 package com.learning.bookstore.retry;
 @Component
 public class FeignClientRetry extends Retryer.Default {
     public FeignClientRetry() {
         super();
     }
 }
 ----------------
 application.yml:
 feign:
   client:
     config:
       default:
         retryer: com.learning.bookstore.retry.FeignClientRetry
 ``` 
 - Here we are using Default Retry which retries 5 number of times for failed client calls, and if all retry fails then calls the fallback process to handle the failure 
 
#### Error Decoder
 - We can also, write custom error decoder where, we can extract the status code of the error and then accordingly throw RetryableException exception which will trigger Retry mechanism or else can bubble the exception which will be finally handled by Fallback Handlers.
 ```
 @Component
 public class FeignClientErrorDecoder implements ErrorDecoder {
     private final ErrorDecoder defaultErrorDecoder = new Default();
     @Override
     public Exception decode(String s, Response response) {
         Exception exception = defaultErrorDecoder.decode(s, response);
         if(response.status() == HttpStatus.INTERNAL_SERVER_ERROR.value()){
            return new RetryableException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "500 Internal Server error",
                   response.request().httpMethod(), null, null );
         }
         return exception;
     }
 }
 ----------------
  application.yml:
  feign:
    client:
      config:
        default:
          retryer: com.learning.bookstore.decoder.FeignClientErrorDecoder
 ``` 

# Authorization Server
By Using Keycloak 
 - Keycloak is an OAuth2 Authorization Server which will implement both OAuth2 and OpenId Connect protocols to provide access and id tokens for OAuth2 Client applications. 
 - These tokens will contains user identity and access/role related information
 - Later OAuth2 client applications will use these tokens to access any protected resources from OAuth2 Resource Server.
 - In this product, we have used Embedded Keycloak along with customized themes as mentioned in the series of article in the link: https://www.baeldung.com/tag/keycloak/ 
 - By following the above link, we can implement an Embedded Keycloak Authorization server with build in support for the user registration, user login, Forgot Password, Remember Me, SSO, custom Themes and Pages etc
 - bookstore-realm.json file contains all the configuration in the json form, to create Keycloak Realm(Bookstore), Client(product-service), Roles(Admin, Buyer), Users(Admin User, Buyer User) with a password and mapped roles etc.
 
#Testing
By using JUnit
 - Add the dependency 'spring-boot-starter-test'
 - Add a new folder 'resources' in the path 'src/test' and create a new file 'bootstrap.yml' under that
 - Add the below configuration details in this yml file to disable spring cloud config server and spring cloud consul server while running test cases along with providing required properties.
 ```
 spring:
   cloud:
     config:
       enabled: false
     discovery:
       enabled: false
     bus:
       enabled: false
     consul:
       enabled: false
       discovery:
         enabled: false
       config:
         enabled: false
 ```