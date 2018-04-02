# Generic Functions
### 1st Advanced Programming Project - 17'18

---

### Problem Description

The Common Lisp Object System (CLOS) is an object-oriented layer for Common Lisp
that is very different from the typical object-oriented languages such as Java.
In CLOS, the programmer organizes its program using the concept of *generic
function*. A generic function is a function containing different specialized
implementations for different types of arguments. Each specialization of a
generic function is a method.

Everytime a generic function is applied to a set of arguments, the actual types
of the arguments are used to find the set of applicable methods, which are
sorted according to its specificity and, then, combined to create the effective
method. The application of the effective method to the provided arguments will
then produce the intended results. This form of operation supports multiple
dispatch, where the applicable methods are determined based on the types of all
arguments.

The main goal of this project is the implementation of generic functions in
Java.

---

More information in the [Problem Statement](statement.pdf)
