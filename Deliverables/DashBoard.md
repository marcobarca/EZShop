# Project Dashboard

The dashboard collects the key measures about the project.
To be used to control the project, and compare with others. These measures will not be used to grade the project. <br>
We consider two phases: <br>
-New development: From project start (april 5) to delivery of version 0 (release 0, may 28) <br>
-Corrective Maintenance: fix of defects (if any)  (may 28 to june 4)   <br>
Report effort figures from the timesheet or timesheetCR document, compute size from the source code.

## New development (release 0  -- april 5 to may 28)
| Measure| Value | Notes |
|---|---|---|
|effort E (report here effort in person hours, for New development, from timesheet)  | 424 | Requirements, Design, Code, Unit and Integration testing, overhead for these activities|
|size S (report here size in LOC of all code written, excluding test cases)  | 3820 | As per Structure 101|
|productivity = S/E | = 3820/424 = 9 ||
|defects before release D_before (number of defects found and fixed before may 28) |5| Most of defects have been corrected by trial and error while writing the code. The ones counted here are the errors found during Unit and Integration testing |


## Corrective Maintenance (release 1 -- may 28 to june 4)

| Measure | Value| Notes |
|---|---|---|
| effort for non-quality ENQ (effort for release 1, or effort to fix defects found when running official acceptance tests) |20|Computing also the time for tests themselves, performed as a unique activity|
| effort for non quality, relative = ENQ / E | = 20/424 = 0,0474 = 5% ||
|defects after release D (number of defects found running official acceptance tests and  fixed in release 1) |14||
| defects before release vs defects after release = D/D_before | = 14/5 = 2,8 | See comment for D_before|
|defect density = D/S| = 5/3820 = 0,13% ||
|overall productivity = S/(E + ENQ)| = 3820/(424+20) = 8,6 ||
