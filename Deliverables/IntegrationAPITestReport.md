# Integration and API Test Documentation

Authors: Simone Alberto, Marco Barca, Umberto Ferrero, Gabriele Vernetti

Date: 12/06/2021

Version: 2.0

Edits:
 - 2.0:
    - Updated integration approach considering RFID's CR

# Contents

- [Dependency graph](#dependency graph)
- [Integration approach](#integration)
- [Tests](#tests)
- [Scenarios](#scenarios)
- [Coverage of scenarios and FR](#scenario-coverage)
- [Coverage of non-functional requirements](#nfr-coverage)

# Dependency graph 

     <report the here the dependency graph of the classes in EzShop, using plantuml>


```plantuml
left to right direction

rectangle GUI
rectangle EZShop as "EZShop API"
rectangle DBAPIs
rectangle BalanceOperationClass
rectangle CustomerClass
rectangle OrderClass
rectangle ProductTypeClass
rectangle SaleTransactionClass
rectangle TicketEntryClass
rectangle UserClass
rectangle Utils

GUI --> EZShop
EZShop --> DBAPIs
DBAPIs --> BalanceOperationClass
DBAPIs --> CustomerClass
DBAPIs --> OrderClass
DBAPIs --> ProductTypeClass
DBAPIs --> SaleTransactionClass
DBAPIs --> TicketEntryClass
DBAPIs --> UserClass

EZShop --> BalanceOperationClass
EZShop --> CustomerClass
EZShop --> OrderClass
EZShop --> ProductTypeClass
EZShop --> SaleTransactionClass
EZShop --> TicketEntryClass
EZShop --> UserClass
EZShop --> Utils

```
     
# Integration approach

    <Write here the integration sequence you adopted, in general terms (top down, bottom up, mixed) and as sequence
    (ex: step1: class A, step 2: class A+B, step 3: class A+B+C, etc)> 
    <Some steps may  correspond to unit testing (ex step1 in ex above), presented in other document UnitTestReport.md>
    <One step will  correspond to API testing>
    
The integration approach adopted is bottom up, due both to the project and deliverable structure:
having simple base classes (only setters and getters), they have been tested during unit testing, as well as DBAPIs, which was tested during unit testing too, as its integrations are only with the set and get methods just named, and it was possible to test its methods, separated, as part of the basic units
Hence just the API is missing and will be tested here, incrementally.
The changes neede to add the RFID management have been tested as lasts, as they came after everything else was already consolidated.

Summarising (more details under the next paragraph):
- Step 1: elementary classes
- Step 2: DBAPIs Class (during unit testing)
- Step 3: EZShop class with first basic classes (e.g. Product Type)
- Step 4: EZShop class with "middle" class, TicketEntryClass
- Step 5: API testing, last classes added
- Step 6: RFID management, no new class, tests added to the already existing ones


#  Tests

   <define below a table for each integration step. For each integration step report the group of classes under test, and the names of
     JUnit test cases applied to them> JUnit test classes should be here src/test/java/it/polito/ezshop

## Step 1
| Classes  | JUnit test cases |
|--|--|
|BalanceOperationClass<br />(gets, sets)|it.polito.ezshop.acceptanceTests.TestEZShop.testBalanceOperationClass|
|CustomerClass<br />(gets, sets)|it.polito.ezshop.acceptanceTests.TestEZShop.testCustomerClass|
|OrderClass<br />(gets, sets)|it.polito.ezshop.acceptanceTests.TestEZShop.testOrderClass|
|ProductTypeClass<br />(gets, sets)|it.polito.ezshop.acceptanceTests.TestEZShop.testProductTypeClass|
|SaleTransactionClass<br />(gets, sets)|it.polito.ezshop.acceptanceTests.TestEZShop.testSaleTransactionClass|
|TicketEntryClass<br />(gets, sets)|it.polito.ezshop.acceptanceTests.TestEZShop.testTicketEntryClass|
|UserClass<br />(gets, sets)|it.polito.ezshop.acceptanceTests.TestEZShop.testUserClass|

## Step 2
| Classes  | JUnit test cases |
|--|--|
|DBAPIs + BalanceOperationClass<br />|it.polito.ezshop.acceptanceTests.TestEZShop.testDBAPIsBalanceOperation|
|DBAPIs + CustomerClass<br />|it.polito.ezshop.acceptanceTests.TestEZShop.testDBAPIsCustomer|
|DBAPIs + OrderClass<br />|it.polito.ezshop.acceptanceTests.TestEZShop.testDBAPIsOrder|
|DBAPIs + ProductTypeClass<br />|it.polito.ezshop.acceptanceTests.TestEZShop.testDBAPIsProductType|
|DBAPIs + SaleTransactionClass<br />|it.polito.ezshop.acceptanceTests.TestEZShop.testDBAPIsClosedSaleTransaction <br/> it.polito.ezshop.acceptanceTests.TestEZShop.testDBAPIsReturnSaleTransaction|
|DBAPIs + TicketEntryClass<br />|it.polito.ezshop.acceptanceTests.TestEZShop.testDBAPIsTicketEntry <br/> it.polito.ezshop.acceptanceTests.TestEZShop.testDBAPIsReturnTicketEntry|
|DBAPIs + UserClass<br />|it.polito.ezshop.acceptanceTests.TestEZShop.testDBAPIsUser|

## Step 3 
| Classes  | JUnit test cases |
|--|--|
|EZShop + DBAPIs + UserClassClass|it.polito.ezshop.acceptanceTests.TestEZShop.testFR1|
|EZShop + DBAPIs + ProductTypeClass|it.polito.ezshop.acceptanceTests.TestEZShop.testFR3|
|EZShop + DBAPIs + CustomerClass|it.polito.ezshop.acceptanceTests.TestEZShop.testFR5|
|EZShop + DBAPIs + BalanceOperationClass|it.polito.ezshop.acceptanceTests.TestEZShop.testFR8|

## Step 4
| Classes  | JUnit test cases |
|--|--|
|EZShop + DBAPIs + UserClass + ProductTypeClass + TicketEntryClass|it.polito.ezshop.acceptanceTests.TestEZShop.testFR7|

## Step 5
| Classes  | JUnit test cases |
|--|--|
|EZShop + DBAPIs + UserClass + ProductTypeClass + BalanceOperation + OrderClass|it.polito.ezshop.acceptanceTests.TestEZShop.testFR4|
|EZShop + DBAPIs + UserClass + ProductTypeClass + CustomerClass + TicketEntryClass + BalanceOperation + SaleTransactionClass|it.polito.ezshop.acceptanceTests.TestEZShop.testFR6|

## Step 6
Same as step 5, the classes are the same, but has been tested the RFID related methods, included in different tests.

# Scenarios

<If needed, define here additional scenarios for the application. Scenarios should be named
 referring the UC in the OfficialRequirements that they detail>

Since the coverage is already high (93%), there is no need for additional scenarios.

# Coverage of Scenarios and FR

<Report in the following table the coverage of  scenarios (from official requirements and from above) vs FR. 
Report also for each of the scenarios the (one or more) API JUnit tests that cover it. >

| Scenario ID | Functional Requirements covered | JUnit  Test(s) | 
| ----------- | ------------------------------- | ----------- | 
| Scenario 1-1      | FR3                             | it.polito.ezshop.acceptanceTests.TestEZShop.testFR3            |             
| Scenario 1-2      | FR3                             | it.polito.ezshop.acceptanceTests.TestEZShop.testFR3            |             
| Scenario 1-3      | FR3                             | it.polito.ezshop.acceptanceTests.TestEZShop.testFR3            |             
| Scenario 2-1      | FR1                             | it.polito.ezshop.acceptanceTests.TestEZShop.testFR1            |             
| Scenario 2-2      | FR1                             | it.polito.ezshop.acceptanceTests.TestEZShop.testFR1            |             
| Scenario 2-3      | FR1                             | it.polito.ezshop.acceptanceTests.TestEZShop.testFR1            |         
| Scenario 3-1      | FR4                             | it.polito.ezshop.acceptanceTests.TestEZShop.testFR4            |    
| Scenario 3-2      | FR4                             | it.polito.ezshop.acceptanceTests.TestEZShop.testFR4            |    
| Scenario 3-3      | FR4                             | it.polito.ezshop.acceptanceTests.TestEZShop.testFR4            |    
| Scenario 4-1      | FR5                             | it.polito.ezshop.acceptanceTests.TestEZShop.testFR5            |    
| Scenario 4-2      | FR5                             | it.polito.ezshop.acceptanceTests.TestEZShop.testFR5            |    
| Scenario 4-3      | FR5                             | it.polito.ezshop.acceptanceTests.TestEZShop.testFR5            |    
| Scenario 4-4      | FR5                             | it.polito.ezshop.acceptanceTests.TestEZShop.testFR5            |    
| Scenario 5-1      | FR1                             | it.polito.ezshop.acceptanceTests.TestEZShop.testFR1            |    
| Scenario 5-2      | FR1                             | it.polito.ezshop.acceptanceTests.TestEZShop.testFR1            |    
| Scenario 6-1      | FR6                             | it.polito.ezshop.acceptanceTests.TestEZShop.testFR6            |    
| Scenario 6-2      | FR6                             | it.polito.ezshop.acceptanceTests.TestEZShop.testFR6            |    
| Scenario 6-3      | FR6                             | it.polito.ezshop.acceptanceTests.TestEZShop.testFR6            |    
| Scenario 6-4      | FR6                             | it.polito.ezshop.acceptanceTests.TestEZShop.testFR6            |    
| Scenario 6-5      | FR6                             | it.polito.ezshop.acceptanceTests.TestEZShop.testFR6            |    
| Scenario 6-6      | FR6                             | it.polito.ezshop.acceptanceTests.TestEZShop.testFR6            |    
| Scenario 7-1      | FR7                             | it.polito.ezshop.acceptanceTests.TestEZShop.testFR7            |    
| Scenario 7-2      | FR7                             | it.polito.ezshop.acceptanceTests.TestEZShop.testFR7            |    
| Scenario 7-3      | FR7                             | it.polito.ezshop.acceptanceTests.TestEZShop.testFR7            |    
| Scenario 7-4      | FR7                             | it.polito.ezshop.acceptanceTests.TestEZShop.testFR7            |    
| Scenario 8-1      | FR6                             | it.polito.ezshop.acceptanceTests.TestEZShop.testFR6            | 
| Scenario 8-2      | FR6                             | it.polito.ezshop.acceptanceTests.TestEZShop.testFR6            | 
| Scenario 9-1      | FR8                             | it.polito.ezshop.acceptanceTests.TestEZShop.testFR8            | 
| Scenario 10-1     | FR7                             | it.polito.ezshop.acceptanceTests.TestEZShop.testFR7            |    
| Scenario 10-2     | FR7                             | it.polito.ezshop.acceptanceTests.TestEZShop.testFR7            | 

# Coverage of Non Functional Requirements

<Report in the following table the coverage of the Non Functional Requirements of the application - only those that can be tested with automated testing frameworks.>

### 

|NFR ID| Non Functional Requirement | Test name |
|------| -------------------------- | --------- |
| NFR1 | Application should be used with no specific training for the users | Manual test |
| NFR2 | All functions should complete in < 0.5 sec | Reported by JUnit testing |
| NFR3 | The data of a customer should not be disclosed outside the application | By Desing. Manual test |
| NFR4 | The barcode number related to a product type should be a string of digits of either 12, 13 or 14 numbers validated following this algorithm  https://www.gs1.org/services/how-calculate-check-digit-manually | De facto tested in the implementation under Utils/BarcodeCheck.java |
| NFR5 | The credit cards numbers should be validated through the Luhn algorithm | De facto tested in the implementation under Utils/LuhnAlgorithm.java |
| NFR6 | The customer's card should be a string of 10 digits | De facto tested in the implementation under Customer Card creation |


