# C++ Learning
## Language
* ```const vector<Student>& getStudents() const{``` first constant is to say the returned value should not be modified. 
The second const is to say the function will not change any internal value.
* default member variable in class are private
* Stack/Heap Memory: In summary, stack memory is fast and automatically managed, but limited in size and scope, while heap memory allows for dynamic allocation and deallocation but requires manual management and can be slower due to indirection.
      * Using Stack is much faster than Heap
    * Stack memory deallocated when leaves scope
    * > int val = 5 // stack
    * > int* hval = new int // heap
## Data Structure