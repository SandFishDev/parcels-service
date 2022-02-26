# READ ME

Feature 1 is on assignment-1 branch. <br>
The addition of Feature 2 is on assignment-2 branch. <br>
The addition of Feature 3 is on the main branch.

# Changes for Feature 2

1. Add strategy for new department
2. Add priority to each strategy to decide order of operations
3. Add self referencing many-to-many departent relation to map relations between departments
4. When processing a parcel find a successor match and assign the parcel to that instead of finishing the process

# Changes for Feature 3

1. Remove strategies and replace by @ManyToMany self-reference in combination with priority field in department to decide order
2. Add 'Rule' entity to replace the old strategies. 
3. Add CRUD for rules & departments since they now have to be managed by admin user


