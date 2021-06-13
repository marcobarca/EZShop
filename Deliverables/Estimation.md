# Project Estimation  
- Authors: Simone Alberto, Marco Barca, Umberto Ferrero, Gabriele Vernetti
- Date: 30/04/2021
- Version: 1.0

# Contents
- [Estimate by product decomposition](#estimate-by-product-decomposition)
- [Estimate by activity decomposition](#estimate-by-activity-decomposition)

# Estimation approach
Estimation based on Requirement Document drafted by Group 49.

# Estimate by product decomposition
### 
|             | Estimate                        |             
| ----------- | ------------------------------- |  
| NC =  Estimated number of classes to be developed   | 17 |             
| A = Estimated average size per class, in LOC       | 180 | 
| S = Estimated size of project, in LOC (= NC * A) | 3060 |             
| E = Estimated effort, in person hours (here use productivity 10 LOC per person hour)  | 306 |   
| C = Estimated cost, in euro (here use 1 person hour cost = 30 euro) | 9180 | 
| Estimated calendar time, in calendar weeks (Assume team of 4 people, 8 hours per day, 5 days per week ) | 1.5 |               
# Estimate by activity decomposition
### 
|         Activity name    | Estimated effort (person hours)   |             
| ----------- | ------------------------------- | 
| RequirementEngineering | 80 |
| Design | 48 |
| Coding | 306 |
| UnitTesting | 192 |
| IntegrationTesting | 128 |
| AcceptanceTesting | 128 |
| GitMaven | 160 |
| Management | 136 (1 team hour per day) |
###

The total duration of the Gantt takes into consideration constraints, dependencies and parallelisation of the activities

```plantuml
[-] lasts 45 days and is colored in LightBlue
[EZShop] lasts 34 days
[Management] lasts 34 days

[RequirementEngineering] lasts 3 days
[Requirement and GUI prototype delivery] happens at [RequirementEngineering]'s end

[Design] lasts 2 days
[Design] starts at [RequirementEngineering]'s end
[Design Delivery] happens at [Design]'s end

[Coding] lasts 17 days
[Coding] starts at [Design]'s end

[UnitTesting] lasts 6 days
[UnitTesting] starts 4 days after [Coding]'s start
[UnitTesting] ends 1 days after [Coding]'s end
[Code and Unit Test Delivery] happens at [UnitTesting]'s end

[IntegrationTesting] lasts 4 days
[IntegrationTesting] starts 6 days after [UnitTesting]'s start
[IntegrationTesting] ends 2 days after [UnitTesting]'s end

[AcceptanceTesting] lasts 5 days
[AcceptanceTesting] starts 1 days before [IntegrationTesting]'s end
[Integration and GUI Tests Delivery] happens at [AcceptanceTesting]'s end

[GitMaven] lasts 5 days
[GitMaven] starts at [AcceptanceTesting]'s end
[First Change Delivery] happens 2 days before [GitMaven]'s end
[Second Change Delivery] happens at [GitMaven]'s end
```

