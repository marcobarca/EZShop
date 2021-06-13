# Requirements Document

Authors: Simone Alberto, Marco Barca, Umberto Ferrero, Gabriele Vernetti

Date: 18/04/2021

Version: 1.0

# Contents

- [Essential description](#essential-description)
- [Stakeholders](#stakeholders)
- [Context Diagram and interfaces](#context-diagram-and-interfaces)
	+ [Context Diagram](#context-diagram)
	+ [Interfaces](#interfaces)
- [Stories and personas](#stories-and-personas)
- [Functional and non functional requirements](#functional-and-non-functional-requirements)
	+ [Functional Requirements](#functional-requirements)
	+ [Non functional requirements](#non-functional-requirements)
- [Use case diagram and use cases](#use-case-diagram-and-use-cases)
	+ [Use case diagram](#use-case-diagram)
	+ [Use cases](#use-cases)
- [Glossary](#glossary)
- [Class Diagram](#class-diagram)
- [System design](#system-design)
- [Deployment diagram](#deployment-diagram)

# Essential description

Small shops require a simple application to support the owner or manager. A small shop (ex a food shop) occupies 50-200 square meters, sells 500-2000 different item types, has one or a few cash registers
EZShop is a software application to:
* manage sales
* manage inventory
* manage customers
* support accounting


# Stakeholders

| Stakeholder name  | Description |
|:-----------------: |:-----------:|
|Admin         |User who has the the highest level of permissions. He can manage sales, inventory, customers and also the permission level of the other users.|
|Manager       |User who has the the middle level of permissions. He can manage sales, inventory, customers and he can support accounting in order to track sales and take business choices.|
|Worker        |User who has the lowest level of permissions. He can manage sales, inventory and customers. Examples of workers are the cashier and the inventory manager.|
|Customer      |Person who buys products in the store. He can be identified by the system if he has a Fidelity Card.|
|Fidelity Card |Card that permits to identify the customer.|
|POS		   |System used to settle financial transactions.|


# Context Diagram and interfaces

## Context Diagram

```plantuml
actor Admin as a
actor Manager as m
actor Worker as w
actor FidelityCard as f
actor POS as po
actor Product as p

a -- (EZShop)
m -- (EZShop)
w -- (EZShop)
f -- (EZShop)
po -- (EZShop)
p -- (EZShop)
```


## Interfaces

| Actor 					   | Physical Interface        | Logical Interface  |
|:-----------------------------|:-------------------------:|:------------------:|
|Owner, Manager, Worker		   |Computer, Keyboard, Screen |GUI 		        |
|Payment infrastracture 	   |POS 			  		   |API                 |
|POS Terminal*	   			   |Computer, Keyboard, Screen |API                 |
|Product				 	   |Bar Code Reader            |Bar Code	        |
|Fidelity Card			 	   |Bar Code Reader            |Bar Code	        |

*POS Terminal: Adyen P400 Plus; all API requests are made from the system directly to the terminal IP address.

# Stories and personas

* Mario is 73, he is the store owner, and because of this he will be profiled as admin. He bought the business a couple of years ago with the money he invested during his life. He has always wanted to own a shop, and despite his age he is a technology enthusiast. Because of this he wanted a smart application to manage the core activities of his business. As the admin of the shop he wants to directly configure his collaborators’ profiles, and check the economic data after every day of work.

* Josep, 73, is Mario’s brother. He has just retired from work but decided to help Mario in his new adventure. He is very different from his brother, and he’s quite bothered by “these things for millennials”. He helps mainly by managing the inventory at 360°, since he has worked with different suppliers in his life. On the application side, he is only interested in something easy, that will allow him to easily input quantities and register new products. While being profiled as manager to be a backup of Mario, he prefers to stick with the inventory, and work as a cashier, while using the accounting functions only when strictly needed.

* Antonia, 20, is a student at Politecnico di Torino, and works part time in the shop to pay for her studies. She is interested in not overcomplicating her job, and is happy to have an application that integrates different functions in one single place. She works mainly as a cashier, but is often involved in inventory management activities, which she can execute directly on the same device, between one client and the following one.

* Ursula, 36, part time worker, mother of 3. She has always wanted to be focused both on her family and her work. She is precise in her job and doesn’t like unforeseen events, as raising 3 children already brings unpredictability in her life. She is the main cahier of the shop, since she also likes to keep good relations with customers from which she can gather useful information on what to change in the stock. This type of information will then be passed to her colleagues

* Charles, 40, full time worker. He likes to travel around countries, and usually works in small shops to save for his next destination, since he thinks that in small neighbourhood shops he can meet more “real” people to really discover a country. He is eager to help, and with his worker account he can help at the cash desk, with the inventory, or with whatever may be useful for the shop itself.

# Functional and non functional requirements

## Functional Requirements

| ID    |-       |  Description  |
| ------|--------|:-------------:|
|FR1   	|	       |Manage users|
|-   	|FR1.1     |Create new user with associated role|
|-   	|FR1.2     |Delete user|
|-		|FR1.3	   |List all users|
|-   	|FR1.4     |Modify user role|
|FR2   	|          |Support Accounting|
|-		|FR2.1	   |Calculate Income|
|-		|FR2.2	   |Calculate Outcome|
|-		|FR2.3	   |Generate accounting aggregates on the analytics section|
|FR3	|	       |Manage Customer|
|-   	|FR3.1	   |Create Fidelity Card|
|-		|FR3.2	   |Add Fidelity Card to Database|
|-		|FR3.3	   |Modify Fidelity Card data|
|-		|FR3.4	   |List all Fidelized Customers|
|-		|FR3.5	   |Search a Fidelized Customer|
|FR4	|	       |Manage sales|
|-		|FR4.1	   |Scan Fidelity Card|
|-		|FR4.2	   |Add Fidelity Card Code manually|
|-		|FR4.3	   |Scan product|
|-		|FR4.4	   |Add product barcode manually|
|-		|FR4.5	   |Update products quantity during the sale|
|-  	|FR4.6	   |Allow cash payment|
|-   	|FR4.7	   |Allow credit card payement|
|-   	|FR4.8     |Complete checkout|
|-		|FR4.9	   |Print the recepit|
|-   	|FR4.10	   |Cancel sale|
|-   	|FR4.11	   |Search a sale|
|FR5	|	   	   |Manage Inventory|
|-		|FR5.1	   |Create new product|
|-		|FR5.2	   |Delete product from inventory|
|-		|FR5.3	   |Modify product amount|
|-		|FR5.4	   |Modify product details|
|-		|FR5.5	   |List all products|
|-		|FR5.6	   |Search a product|

<br>

### Access right, actor vs function

| Function | Admin | Manager | Worker |
| ------------- |-------------|--|--|
|FR1| yes | no	| no  |
|FR2| yes | yes | no  |
|FR3| yes | yes | yes |
|FR4| yes | yes | yes |
|FR5| yes | yes | yes |


<br>

## Non Functional Requirements

| ID   | Type (efficiency, reliability, ..) | Description  | Refers to |
|:------:|:-------------:| :-----:| -----:|
|NFR1    |Usability	     | Time to learn how to use the system for the worker <30 min|All FR |
|NFR2    |-       	     | Intuitive and quick menus |All FR |
|NFR3    |-      	     | Friendly graphics | All FR|
|NFR4    |Availability	 | Downtime <1% |All FR|
|NFR5    |Performance	 | Response time to any button pressed < 0.5 sec |All FR|
|NFR6    |-	             | Response time to Database <5s |All FR|
|NFR7    |Robustness	 | Transaction Atomicity granted |All FR|
|NFR8    |Portability    | The application should be available for all the most used Operating Systems (Windows, Machintosh, Linux).|All FR|
|NFR9    |Privacy	 	 | Credit Card info is never stored in the system |All FR|


# Use case diagram and use cases

## Use case diagram

```plantuml
actor Admin as a
actor Manager as m
actor Worker as w
actor FidelityCard as f
actor POS as po
actor Product as p

a --> (Manage Users)#red
a --> (Support Accounting)#red
a --> (Manage Sales)#red
a --> (Manage Customer)#red
a --> (Manage Inventory)#red

m --> (Manage Sales) #blue
m --> (Support Accounting)#blue
m --> (Manage Customer)#blue
m --> (Manage Inventory)#blue
w --> (Manage Sales)#green
w --> (Manage Customer)#green
w --> (Manage Inventory)#green

(Manage Sales) --> po
(Manage Sales) --> p
(Manage Sales) --> f
(Manage Inventory) --> p
(Manage Customer) --> f
```

<br>

### Use case 1, UC1 - Create new user 
| Actors Involved     | Admin |
| ------------------- |:-------------:|
|  Precondition       |System is ON|
|					  |Admin has logged in |
|  Post condition     |The new user is created successfully|
|  Nominal Scenario   |The admin creates a new user by assigning him a role (Admin, Manager, Worker).|

<br>

### Use case 2, UC2 - Delete user 
| Actors Involved     | Admin |
| ------------------- |:-------------:|
|  Precondition       |System is ON|
|					  |Admin has logged in |
|  Post condition     |The user has been successfully deleted and he no longer has the ability to access the system. |
|  Nominal Scenario   |The admin deletes the user.|

<br>

### Use case 3, UC3 - List all users 
| Actors Involved     | Admin |
| ------------------- |:-------------:|
|  Precondition       |System is ON|
|					  |Admin has logged in |
|  Post condition     |The system displays all the Users|
|  Nominal Scenario   |The Admin selects the Settings button from the GUI|

<br>

### Use case 4, UC4 - Modify user role 
| Actors Involved     | Admin |
| ------------------- |:-------------:|
|  Precondition       |System is ON|
|					  |Admin has logged in |
|  Post condition     |The user role is changed|
|  Nominal Scenario   |The administrator changes the user role by downgrading or upgrading it|

<br>

### Use case 5, UC5 - Check aggregates on the analytics section 
| Actors Involved   | Manager |
| ----------------- |:-------------:|
|  Precondition     |System is ON|
|					|Manager has logged in|
|  Post condition   |The Manager enters the analytics section to check the sales trend and, if necessary, undertake specific marketing strategies|
|  Nominal Scenario |The Manager selects the analytics section from the GUI|

<br>

### Use case 6, UC6 - Create new Fidelity Card 
| Actors Involved   | User |
| ----------------- |:-------------:|
|  Precondition     |System is ON|
|					|User has logged in|
|  Post condition   |New Fidelity Card is correctly created|
|  Nominal Scenario |The User creates a new Fidelity Card in the system; takes a new unassociated card and enters its code into the system along with the remaining required fields|

<br>

### Use case 7, UC7 - Modify Fidelity Card data 
| Actors Involved   | User |
|-------------------|:-------------:|
|  Precondition     |System is ON|
|					|User has logged in|
|  Post condition   |The Fidelity Card is correctly modified|
|  Nominal Scenario |The User scans the Fidelity Card and once it appears in the system, he can modify the desired fields|
|  Variants         |The User needs to change the code of a lost Fidelity Card with a new one|

<br>

#### Scenario 7.1
| Scenario 7.1        |Physical Fidelity Card lost|
| -------------------- |:-------------:|
|  Precondition        |System is ON|
|					   |User has logged in |
|					   |A physical Fidelity Card has been lost|
|  Post condition      |The new physical Fidelity card has been correctly associated with the data relating to the old one|
| Step#        		   | Description  |
|  1     	| The User search for the old Fidelity Card using the data associated with it|
|  2        | The User has found the fidelity card and changes its code with the one associated with the new card|

<br>

### Use case 8, UC8 - List all Fidelized Customers
| Actors Involved   | User |
| ----------------- |:-------------:|
|  Precondition     |System is ON|
|					|User has logged in|
|  Post condition   |The system displays all the Fidelized Customers|
|  Nominal Scenario |The User selects the Customers section from the GUI|

<br>

### Use case 9, UC9 - Search Fidelized Customer
| Actors Involved   | User |
| ----------------- |:-------------:|
|  Precondition     |System is ON|
|					|User has logged in|
|  Post condition   |The Fidelized Customer is displayed|
|  Nominal Scenario |The User search a certain Fidelized Customer by its code|

<br>

### Use case 10, UC10 - Sell a product
| Actors Involved     | User |
| ------------------- |:-------------:|
|  Precondition       |System is ON|
|					  |POS is correctly connected to the system|
|					  |User has logged in |
|  Post condition     |The sale concluded successfully|
|					  |The products quantity has been updated in the inventory|
|					  |The sale has been recorded to the database, binding it to the Fidelity Card|
|Nominal Scenario     |The User creates a new sale in the system, scans both the products and the fidelity card with the barcode reader; the products and the fidelity card have been successfully scanned; the system provides to the User the total amount; the User selects the cash payment and completes the checkout; the User allows the system to print the recepit; the system updates the products quantity in the inventory and adds the sale to the database, binding it to the Fidelity Card. |
|Variants|The barcode reader returns an error while scanning the product; the system allows the User to manually add the barcode; the sale goes on.|
||The barcode reader returns an error while scanning the fidelity card; the system allows the User to manually add the fidelity card code; the sale goes on.|
||The User selects the credit card payment method; the POS signals to the system that the payment has been successful; the User completes the checkout; the sale goes on; |
||The User selects the credit card payment method; The POS signals to the User that the payment failed; The User selects the cash payment and confirm the transaction;|
||The Operator creates a new sale in the system, scans the products with the barcode reader; the sale is not bound to a Fidelity Card;|

<br>

#### Scenario 10.1

| Scenario 10.1         |Nominal Scenario|
| -------------------- |:-------------:|
|  Precondition        |System is ON|
|					   |POS is correctly connected to the system|
|					   |User has logged in |
|  Post condition      |The sale concluded successfully|
|					   |The products quantity has been updated in the inventory|
|					   |The sale has been recorded to the database, binding it to the Fidelity Card|
| Step#  | Description |
|  1     | The User creates a new sale in the system |
|  2     | The User scans both the products and the fidelity card with the barcode reader|
|  3     | The System provides to the User the total amount|
|  4     | The User selects the cash payment and completes the checkout|
|  5     | The User allows the system to print the recepit|
|  6     | The system updates the products quantity in the inventory and adds the sale to the database, binding it to the Fidelity Card|

<br>

#### Scenario 10.2

| Scenario 10.2        |Error while scanning the Product|
| -------------------- |:-------------:|
|  Precondition        |System is ON|
|					   |POS is correctly connected to the system|
|					   |User has logged in |
|  Post condition      |The sale concluded successfully|
|					   |The products quantity has been updated in the inventory|
|					   |The sale has been recorded to the database, binding it to the Fidelity Card|
| Step#  | Description |
|  1     | The User creates a new sale in the system |
|  2     | The User scans both the products and the fidelity card with the barcode reader|
|  3     | The barcode reader returns an error while scanning the Product|
|  4	 | The User adds the barcode manually into the system|
|  5     | The Products is successfully recognized|
|  6     | The System provides to the User the total amount|
|  7     | The User selects the cash payment and completes the checkout|
|  8     | The User allows the system to print the recepit|
|  9     | The system updates the products quantity in the inventory and adds the sale to the database, binding it to the Fidelity Card|

<br>

#### Scenario 10.3

| Scenario 10.3        |Error while scanning the Fidelity Card|
| -------------------- |:-------------:|
|  Precondition        |System is ON|
|					   |POS is correctly connected to the system|
|					   |User has logged in |
|  Post condition      |The sale concluded successfully|
|					   |The products quantity has been updated in the inventory|
|					   |The sale has been recorded to the database, binding it to the Fidelity Card|
| Step#  | Description |
|  1     | The User creates a new sale in the system |
|  2     | The User scans both the products and the fidelity card with the barcode reader|
|  3     | The barcode reader returns an error while scanning the Fidelity Card|
|  4	 | The User adds the Fidelity Card Code manually into the system|
|  5     | The Fidelity Card is successfully recognized|
|  6     | The System provides to the User the total amount|
|  7     | The User selects the cash payment and completes the checkout|
|  8     | The User allows the system to print the recepit|
|  9     | The system updates the products quantity in the inventory and adds the sale to the database, binding it to the Fidelity Card|

<br>

#### Scenario 10.4

| Scenario 10.4        |Credit Card payment|
| -------------------- |:-------------:|
|  Precondition        |System is ON|
|					   |POS is correctly connected to the system|
|					   |User has logged in |
|  Post condition      |The sale concluded successfully|
|					   |The products quantity has been updated in the inventory|
|					   |The sale has been recorded to the database, binding it to the Fidelity Card|
| Step#  | Description |
|  1     | The User creates a new sale in the system |
|  2     | The User scans both the products and the Fidelity Card with the barcode reader|
|  3     | The System provides to the User the total amount|
|  4     | The User selects the Credit Card payment and completes the checkout|
|  5     | The POS signals to the User that the payment has been successful|
|  6     | The User allows the system to print the recepit|
|  7     | The system updates the products quantity in the inventory and adds the sale to the database, binding it to the Fidelity Card|

<br>

#### Scenario 10.5

| Scenario 10.5         |Credit Card payment fail|
| -------------------- |:-------------:|
|  Precondition        |System is ON|
|					   |POS is correctly connected to the system|
|					   |User has logged in |
|  Post condition      |The sale concluded successfully|
|					   |The products quantity has been updated in the inventory|
|					   |The sale has been recorded to the database, binding it to the Fidelity Card|
| Step#  | Description |
|  1     | The User creates a new sale in the system |
|  2     | The User scans both the products and the Fidelity Card with the barcode reader|
|  3     | The System provides to the User the total amount|
|  4     | The User selects the Credit Card payment and completes the checkout|
|  5     | The POS signals to the User that the payment failed|
|  6     | The User selects the cash payment and completes the checkout|
|  7     | The User allows the system to print the recepit|
|  8     | The system updates the products quantity in the inventory and adds the sale to the database, binding it to the Fidelity Card|

<br>

#### Scenario 10.6

| Scenario 10.6        |Sale not bound to a Fidelity Card|
| -------------------- |:-------------:|
|  Precondition        |System is ON|
|					   |User has logged in |
|  Post condition      |The sale concluded successfully|
|					   |The products quantity has been updated in the inventory|
|					   |The sale has been recorded to the database|
| Step#  | Description |
|  1     | The User creates a new sale in the system |
|  2     | The User scans the products with the barcode reader|
|  3     | The System provides to the User the total amount|
|  7     | The User selects the cash payment and completes the checkout|
|  6     | The User allows the system to print the recepit|
|  7     | The system updates the products quantity in the inventory and adds the sale to the database (not bound to any Fidelity Card)|

<br>

### Use case 11, UC11 - Update products quantity during the sale
| Actors Involved   | User |
| ----------------- |:-------------:|
|  Precondition       |System is ON|
|					  |User has logged in |
|  Post condition     |The products quantity is updated successfully|
|  Nominal Scenario   |To better manage multiple pieces of the same product, the User scans the first one and manually updates the quantity on screen instead of scanning multiple times|

<br>

### Use case 12, UC12 - Cancel the sale made with cash payment
| Actors Involved   | User |
| ----------------- |:-------------:|
|  Precondition     |System is ON|
|					|User has logged in |
|  Post condition   |The sale is cancelled from the database|
|					|The product is added back to the inventory |
|					|Economic statistics are successfully updated |
|  Nominal Scenario |The User selects the sale to void from archived sales; the system adds the product back to the inventory, deletes the sale from the database and updates the economic statistics|

<br>

### Use case 13, UC13 - Cancel the sale made with credit card payment
| Actors Involved   | User |
| ----------------- |:-------------:|
|  Precondition     |System is ON |
|					|POS is correctly connected to the system|
|					|User has logged in |
|  Post condition   |The sale is cancelled from the database|
|					|The POS cancel the payment |
|					|The product is added back to the inventory |
|					|Economic statistics are successfully updated |
|  Nominal Scenario |The User selects the sale to void from archived sales; the system signals to the POS to cancel the payment; the system adds the Product back to the inventory, deletes the sale from the database and updates the economic statistics|

<br>

### Use case 14, UC14 - Search Sale
| Actors Involved   | User |
| ----------------- |:-------------:|
|  Precondition     |System is ON|
|					|User has logged in|
|  Post condition   |The Sale is displayed|
|  Nominal Scenario |The User search a certain sale by its name|

<br>

### Use case 15, UC15 - Create new product
| Actors Involved   | User |
| ----------------- |:-------------:|
|  Precondition     |System is ON|
|					|User has logged in |
|  Post condition   |The Product is created and the inventory is updated successfully|
|  Nominal Scenario |The User creates a new product and populates all the required fields|

<br>

### Use case 16, UC16 - Delete product from inventory
| Actors Involved   | User |
| ----------------- |:-------------:|
|  Precondition     |System is ON|
|					|User has logged in|
|  Post condition   |The Product is deleted and the inventory is updated successfully|
|  Nominal Scenario |The User deletes the product|

<br>

### Use case 17, UC17 - Modify product amount
| Actors Involved   | User |
| ----------------- |:-------------:|
|  Precondition     |System is ON|
|					|User has logged in|
|  Post condition   |The Product quantity in the inventory is updated successfully|
|  Nominal Scenario |The User searches for the desidered product in the inventory section, or in the product page, and updates the related quantity in order to refill the stock|

<br>

### Use case 18, UC18 - Modify product details
| Actors Involved   | User |
| ----------------- |:-------------:|
|  Precondition     |System is ON|
|					|User has logged in|
|  Post condition   |The Product details are updated successfully|
|  Nominal Scenario |The User searches for the desidered product in the inventory section and updates the desidered details|

<br>

### Use case 19, UC19 - List all products
| Actors Involved   | User |
| ----------------- |:-------------:|
|  Precondition     |System is ON|
|					|User has logged in|
|  Post condition   |The system displays all the products|
|  Nominal Scenario |The User selects the product section from the GUI|

<br>

### Use case 20, UC20 - Search product
| Actors Involved   | User |
| ----------------- |:-------------:|
|  Precondition     |System is ON|
|					|User has logged in|
|  Post condition   |The Product is displayed|
|  Nominal Scenario |The User search a certain product by its name|

<br>

# Glossary
* User: Every person who use the EZshop app. Every user has an associated role (Admin, Manager, Worker).
* Admin: User who has the the highest level of permissions. He can manage sales, inventory, customers and also the permission level of the other users. Might be the owner of the shop.
* Manager: User who has the the middle level of permissions. He can manage sales, inventory, customers and he can support accounting in order to track sales and take business choices.
* Worker: User who has the lowest level of permissions. He can manage sales, inventory and customers . Examples of workers are the cashier and the inventory manager.
* Customer: person who buys products from the EZshop. He can be identified by the system if he has a Fidelity Card.
* Fidelity Card Descriptor: entity that describes the Fidelity Card.
* Product Descriptor: entity that describes the product.
* Product Variant: adding variants to a product that comes in more than one option, such as size or color. Each combination of option values for a product can be a variant for that product. The variant is identified by its owns SKU and ProductId. Each variant has its owns VariantAttribute (e.g. size) and VariantValue (e.g. small, medium, ...).
* Product In Sale: is the product involved in the current sale.
* Product In Inventory: represents the product and its own quantity in the inventory.
* Sale: process triggered by Worker to achieve a commercial transaction between customer and EZshop; it involves different functionalities inside the system.
* Sale With FC: represents a sale with an associated Fidelity Card.
* Checkout: action that consolidates the sale finalazing the commercial transaction.
* User List: list of all users. It's stored in the database.
* Sales History: list of all sales. It's stored in the database.
* Product Inventory: list of all products. It's stored in the database.
* Fidelity Card List: list of all Fidelity Cards. It's stored in the database.
* Database: database where are stored all the informations about the system (sales, users, inventory, Fidelity Cards).

# Class Diagram

```plantuml
class EZShop
class User {
 	firstName
	lastName
 	roleuserID
 	email
 	password
}
class Admin 
class Manager 
class Worker 
class ProductInSale {
	quantity
}
class Sale {
	saleID
	paymentType
	subTotal
	VAT
	total
}
class SaleWithFC {
	cardID
}
class SalesHistory
class ProductDescriptor {
	productID
	SKU
	barcode
	productName
	productCost
	productPrice
}
class ProductVariant {
	variantAttribute
	variantValue
}
class ProductInventory
class ProductInInventory{
	quantity
}
class UsersList
class FidelityCardDescriptor{
	firstName
	lastName
	email
	phoneNumber
	address
	addressNumber
	city
	province
	postalCode
}
class FidelityCardList

note "Admin can creates, deletes and modifies users" as N
Admin .. N

Admin "*" -- "*" User 
Admin -up-|> User
Manager -up-|> User
Worker -up-|> User
EZShop -- User 
EZShop -- FidelityCardList 
EZShop -- SalesHistory 
EZShop -- Sale
EZShop -- UsersList
EZShop -- ProductInventory

FidelityCardDescriptor "1" -- "*" User
FidelityCard "1" -- "*" User
SalesHistory "*" -- "1" User
Sale "1" -- "*" User
ProductInSale "1" -- "*" User
ProductDescriptor "1" -- "*" User
ProductInventory "*" -- "1" User
UsersList "*" -- "1" User

FidelityCardDescriptor -- FidelityCard
FidelityCardList "*" -- "1" FidelityCard
SaleWithFC -- FidelityCard
Sale "0..1" -- "*" FidelityCard
SaleWithFC -up-|> Sale
SalesHistory "1" -- "*" Sale
Sale -- ProductInventory
Sale "*" -- "1" ProductInSale
ProductInSale "*" -- "1" ProductDescriptor
ProductDescriptor "1" -- "1" ProductInInventory
ProductInInventory "1" -- "*" ProductInventory
ProductVariant -up-|> ProductDescriptor

















```

# System Design

```plantuml
artifact "EZShop" as ez
node "Recepit printer" as printer
node "Barcode reader" as barcode
ez -- printer
ez -- barcode
```

# Deployment Diagram
```plantuml
artifact "EZShop server" as back
artifact "EZShop client" as ez
artifact "DBMS" as dbms
node "Local Server" as s
node "PC" as pc
node "POS" as pos
node "Modem Router" as r
node "Internet" as in
s -- back
s -- r
r -- "*" pos : "WIFI"
r -- "*" pc : "ETH"
s -- dbms
pc -- ez
in -- r
```
