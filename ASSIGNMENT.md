# Technical assessment

## Short description
You work at a parcel delivery company and you are asked to design a system to automate the internal handling of parcels coming in. 
The parcels are coming in at the distribution center and need to be handled by different departments based on their weight and value.
Currently management is making plans that could lead to the adding or removal of departments in the future.

## Features

### Feature 1
The current business rules are as follows:
- Parcels with a weight up to 1 kg are handled by the "Mail" departement.
- Parcels with a weight up to 10 kg are handled by the "Regular" department.
- Parcels with a weight over 10 kg are handled by the "Heavy" department.

### Feature 2
Parcels with a value of over € 1000,- need to be signed off by the "Insurance" department, before being processed by the other departments.

### Feature 3 (optional)
The company has decided to productize the software and sell it under a license subscription model. The business rules will vary per customer and need to be configurable and changed at runtime.

## Excercise
- Build a working application using Java (or Kotlin) and frameworks of your choosing
- Make sure each feature is committed separately
- Parse the XML file (Container_68465468.xml) containing the data of a set of incoming parcels
- Add automated tests
- Present the functionality in a simple Javascript app (using any frameworks of your choosing)

## During the technical interview
- Demonstrate the behavior of the code.
- Show us how you needed to change the code when a new feature was added.
- Show us how adding or removing of a department would be done.
