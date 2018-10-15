# CatchLogged
This are my first steps into Kotlin. There is nothing here I want to show you right now.
Come back later. I might turn this project into some useful tool and/or tutorial.

## The task
Learning a technique requires applying it to some real task, so one does not fall for the lots of 
"Hello-World-Tutorials" which are on the net.

### Problem
Many complex software projects have insufficient error handling. Especially Exceptions are
often not treated consistently. In some support cases a needed stack trace was not found in
the log files whereas other Exceptions have been logged several times cluttering the output.

### Proposed solution
An automatic test should read the whole source code and mark such parts of the software where

- Exceptions are caught but not logged or re-thrown, causing information loss
- Exceptions are both logged and re-thrown, causing duplicate log output of the same information

Special comments should indicate when one of the above cases is done on purpose.
Using such tool should make log output more helpful.