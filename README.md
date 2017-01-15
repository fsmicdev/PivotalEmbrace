# -----------------------------------
# PivotalEmbrace, by Mic Giansiracusa
# -----------------------------------

REST-based API with Swagger web-browser Test-Driven front-end interface.

Back-end is: 
  o [ Spring 4 ] (Annotation-based) web-app (web-app is a Work-In-Progress). 
  o Querying and updating to a [ MongoDB NoSQL ] database.
  o Unit tests utilise [ Mockito ] mocking framework library (Work-In-Progress).
  o Integration tests utilise [ Rest-Assured ] library (Work-In-Progress).
  o maven build/lifecycle controlled.
  
    Run 'mvn clean install' to run unit and integration tests.

#####################################################################################    
N.B. For setting up sequence for collections, there is a helper 
MongoDB script in src/main/resources/mongoDB/scripts/insertCounters.js
which can be run by a shell command similar to below 
'mongo localhost/pivotalembrace src/main/resources/mongoDB/scripts/insertCounters.js'
#####################################################################################    