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
Along with Client Side Load Balancer and Circuit Breaker

By Using Spring Cloud OpenFeign with Spring Cloud LoadBalancer(Load Balancer) and spring-cloud-starter-circuitbreaker-resilience4j(Circuit Breaker)  

 - Add the dependency 'spring-cloud-starter-openfeign' and 'spring-cloud-starter-circuitbreaker-resilience4j'
 - By default, Spring Cloud OpenFeign uses Spring Cloud LoadBalance as a client side load balancer.
 - The OpenFeign will auto-integrate with service discovery like Consul. 
 - To use it we need to declare an interface with required methods for communication. Method signature must be similar to the one which is defined in the actual microservice.
 - The interface has to be annotated with @FeignClient that points to the service using its discovery name as registered in Consul.
 ```
 @FeignClient("bookstore-address-service")
 public interface AddressFeignClient { 
     @GetMapping("/v1/billing-addresses/{id}")
     BillingAddressResponse getBillingAddressById(@PathVariable("id") String id);
 }
 ```
#### Resilience4j
 - Spring Cloud OpenFeign uses Resilience4j as Circuit Breaker if 'spring-cloud-starter-circuitbreaker-resilience4j' is in the classpath, and the below configuration set:
 ```
 feign:
   circuitbreaker:
     enabled: true
 ```
 - To implement Fallback method for Circuit Breaker, the above plugin provides FallbackFactory interface, which can be implemented to provide default handler and can catch the Actual Cause of the Client Calls
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
