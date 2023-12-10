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
* Pointer variables store memory locations. When dealing with pointers, you use the * symbol in two distinct but related ways:
  * As part of a type specifier (such as in ```int* p```), meaning p is a pointer to an ```int```.
  * As the dereference operator (such as in ```*p```), meaning use the value a pointer points to.
  * > int x = 42;\
      int* p = &x;
* References: It's useful to think of a C++ reference as an alias — another name for an existing object. Unlike pointers, references cannot (legally) be null; they always refer to something.\
  Reference variables are defined using the ```&``` symbol, but don't confuse that usage with the address-of operator. **You use the & symbol in two completely unrelated ways:**
  * As part of a type specifier (such as ```int& r```), meaning r is another name for some ```int``` object.
  * As the address-of operator (such as &r), meaning get the address of a variable.
  * > int x = 42;\
      int& r = x;        // r is another name for x\
      assert(r == x);    // they have the same value\
      assert(&r == &x);  // they have the same address!
* Points vs Reference: Pointers can be reassigned with different value, ref is fixed 
  https://www.youtube.com/watch?v=sxHng1iufQE
* Pass By Pointer and Pass By Pointer Reference (```int *``` and ```int * &```): 
    Pass By Pointer Reference will not copy original pointer
    https://www.youtube.com/watch?v=7HmCb343xR8
## Data Structure