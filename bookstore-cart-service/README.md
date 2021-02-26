# Supported functionalities
 - Extract Logged In User Email from the provided OAuth2 JWT Access token
 - Create an empty Cart for the logged in User Mail through REST API. At a time there can be only one single Cart for each logged in user.
 - Create Cart Item for a specific product ID and it's required quantity through REST API
 - Create such multiple Cart Items if required for each product
 - By default, all these created Cart Items will be added to user's default Cart which is created earlier
 - User can get their Cart details through REST API, which contains each cart item details which they have added along with total price
 - Also, user can remove specific Cart Item through Cart Item ID or can remove all added Cart Items through their Cart ID by using provided REST APIs
 
# Note
 - For Inter service communication, add another project 'bookstore-feignclient-service' as a dependency in this project. The required configurations are defined in the README.md file of 'bookstore-feignclient-service' project. 
 