# Resource Server
OAuth2 Resource Server with Spring Security 5
 - Add 'spring-boot-starter-oauth2-resource-server' dependency
 - This will convert any spring boot application as OAuth2 Resource Server and by default it protects all available rest API's and expect a valid JWT Access token from a client as an Authorization Header
 - Once it receives an access token, it will validate for its token validation by connecting to an OAuth2 Authorization Server which will be either Keycloak, Okta etc
 - It will allow a user only if this token is valid. Also, token will contain details of provided user role and permission details. Each API access will be allowed only if a user has the valid role and permissions.
 - Add the below configurations in WebSecurityConfigurationAdapter class, which will take care of validating the provided JWT token by connecting to Authorization Server
    ```
     http.oauth2ResourceServer().jwt())
    ```
 - Also, write a custom JwtAuthenticationConverter, which will decode and extract the required user data or roles information from the given access token, and to use those in the application  
 - Also add the below details of OAuth2 Authorization server in the application.yml file
    ```
    spring:
      security:
        oauth2:
          resourceserver:
            jwt:
              issuer-uri: http://localhost:8083/auth/realms/Bookstore
              jwk-set-uri: http://localhost:8083/auth/realms/Bookstore/protocol/openid-connect/certs  
    ``` 
 - With these configurations, to access any REST API now user has to provide a valid access token as a Header parameter in the form 'Authorization: Bearer <token>' 
 - And, the access token must contain either 'Admin' or 'Buyer' role inorder to access the REST API, as each REST APIs are protected by either Admin or Buyer role.
 - To get a valid access token, call the '/token' endpoint of Keycloak with required parameters as provided in the postman_collection folder of 'bookstore-authorization-server'
 - Refer: https://dev.to/toojannarong/spring-security-with-jwt-the-easiest-way-2i43, https://www.baeldung.com/spring-security-oauth-resource-server
 
# Hexagonal Architecture
To implement a micro service 
 - Follow the link to implement a hexagonal architectureL https://reflectoring.io/spring-hexagonal/
 - Below is the structure which is used in this product-service application
    - adapter => Contains Web, Client and Persistence layers. This layer uses the support of Spring Framework.
        - persistence
            - entity => Defines Hibernate ORM Entity classes
            - impl => Actual implementation of the 'Output Port' which are defined in the application layer.
            - mapper => Mapping between Domain to Hibernate Entity and vice versa
            - repository => Repository classes implemented through Spring Data JPA
        - web
            - exception => Contains all Exception Handlers and Error response message
            - v1 => Contains Controllers which will be exposed as REST APIs and implemented by using Facade design pattern
    - application => Contains Input Port along with their implementation as Service and Output Port. This package is purely JAVA based and no other frameworks are used here.
        - exception => Contains custom exceptions specific to application
        - port => Contains Port definitions for both Input/Inbound and Output/Outbound
            - in => Contains Request, Response and specification for Inbound Ports
            - out => Contains specification for Outbound Ports
        - service => Actual implementation classes of Inbound Ports
    - domain => Just contains pure Java based POJO classes with their functional logic if any. Used Lombok to implement Builder and Getter pattern for the domain, so that no setter operation is allowed.
    - infrastructure => This package contains configurations related to 'application' package and other configuration details with the help of Spring Framework.
        - annotation => Contains any custom Spring Annotations
        - config => Contains Spring Configuration classes for Services, OpenAPI docs, JPA Auditor, Keycloak etc
        - Security => Contains security related classes for OAuth2 Resource Server
    - MainSpringBootApplication