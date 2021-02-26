# Authorization Server
By Using Embedded Keycloak
 - Keycloak is an OAuth2 Authorization Server which will implement both OAuth2 and OpenId Connect protocols to provide access and id tokens for OAuth2 Client applications. 
 - These tokens will contains user identity and access/role related information
 - Later OAuth2 client applications will use these tokens to access any protected resources from OAuth2 Resource Server.
 - Follow the list of available links here: https://www.baeldung.com/tag/keycloak/
 - By following the above link, we can implement an Embedded Keycloak Authorization server with build in support for the user registration, user login, Forgot Password, Remember Me, SSO, custom Themes and Pages etc
 - bookstore-realm.json file contains all the configuration in the json form, to create Keycloak Realm(Bookstore), Client(product-service), Roles(Admin, Buyer), Users(Admin User, Buyer User) with a password and mapped roles etc.
  
##### NOTE: Refer the attached postman_collection to access the different URL's of Keycloak Server, like to get an access token, user info etc
