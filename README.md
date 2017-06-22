# purchase-tracker
An example app for an interview

to run, use the following command (this command will run tests as well)


mvn clean package exec:java


Sample curl:

curl  -X POST  -i  -F file=@example.csv localhost:8080/legacy


Note: All DDL created can be viewed after a run of the application

cat createDDL_ddlGeneration.jdbc
