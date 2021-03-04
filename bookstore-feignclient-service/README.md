This Project Contains all the required Feign Client Interface definitions.

#### To include this project as a dependency in any other project, add the below configurations in the target project

 - settings.gradle file of other project
 ```
 pluginManagement {
     plugins {
         id 'org.springframework.boot' version "2.3.9.RELEASE"
         id 'io.spring.dependency-management' version '1.0.11.RELEASE'
     }
 }
 rootProject.name = 'bookstore-cart-service'
 include ':bookstore-feignclient-service'
 project(':bookstore-feignclient-service').projectDir = new File(settingsDir, '../bookstore-feignclient-service')
 ```
 - build.gradle file of other project
 ```
 dependencies {
    compile project(':bookstore-feignclient-service')
    implementation 'org.springframework.cloud:spring-cloud-openfeign-core' #Required to use the annotation @EnableFeignClients in main class
 }
 ```
 - Also, add the below configuration in Main Spring Boot Application class of other project
 ```
 @SpringBootApplication
 @EnableFeignClients("com.learning.bookstore.client")
 public class BookstoreCartServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookstoreCartServiceApplication.class, args);
    }
 }
 ```

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
