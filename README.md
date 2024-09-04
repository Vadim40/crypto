# Crypto Exchange Simulation  
This application simulates a cryptocurrency exchange platform using Spring Framework, Kafka, and Spring Cloud.    

![image](https://github.com/user-attachments/assets/c6d69082-ef91-4b5c-8ae4-cfb4454cd5b2)   


## Services Overview  
### Authentication Service  
Handles user authentication with OTP (One-Time Password) and JWT (JSON Web Tokens) generation. It manages user data.

### Wallet Service  
Manages wallet data and implements the logic for token transactions. It is responsible for handling and recording financial operations involving tokens.

### Exchange Service  
Handles the logic for token exchanges. It fetches current exchange rates from external APIs, updates, and temporarily stores this data for processing.

### Notification 
Manages the sending of email notifications, including OTPs for authentication and codes for token transfer confirmation.

