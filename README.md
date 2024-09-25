# Crypto Exchange Simulation  
This application simulates a cryptocurrency exchange platform using Spring Framework, Docker containers, Kafka .        

![image](https://github.com/user-attachments/assets/749c3afd-99f9-4e52-ab19-188afff47adb)



## Services Overview  
### Gateway
Validates JWT tokens and forwards requests,  headers from JWT to the appropriate services.
### Authentication Service  
Handles user authentication. Responsible for generating OTPs, JWT access and refresh tokens, as well as managing and storing user-related data such as OTPs, access tokens, and refresh tokens.  
  
![image](https://github.com/user-attachments/assets/a44bb168-8887-41a4-bf07-d6342d8671f1)     
   
User Info: Stores email:id pairs in Redis to minimize the number of requests to the Authentication Service.   
event_id_hash: Stores Kafka message hashes in Redis to ensure idempotency of the consumer, preventing the processing of duplicate events.  



### Wallet Service  
Manages wallet data and implements the logic for token transactions. It is responsible for handling and recording financial operations involving tokens. 
   
![image](https://github.com/user-attachments/assets/c0a870bd-ebbe-45b6-8339-916b4a868c69)


### Exchange Service  
Handles the logic for token exchanges. It fetches current exchange rates from external APIs, updates, and temporarily stores this data for processing.   
  
![image](https://github.com/user-attachments/assets/32f91171-01ad-4530-85ba-066fa7a30c35)  

User Info: Stores email:id pairs in Redis to minimize the number of requests to the Authentication Service.  
Rate: Stores the top exchange rates in Redis for efficient access and processing.  




### Notification 
Manages the sending of email notifications, including OTPs for authentication , info transfer confirmation, login events.

