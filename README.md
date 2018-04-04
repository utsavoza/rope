Rope
====
A **rope** is a tree-like data structure that provides a more efficient way of concatenating strings, 
with each internal node representing the concatenation of its children, and the leaves consisting of
flat strings, usually represented as contiguous array of characters.  

The project provides an implementation of **rope** data structure. The current API is inspired by
the rope implementation in **Raph Levien's [xi-editor](https://github.com/google/xi-editor)**. 
The project doesn't provide all it's operations and optimizations and is solely for my learning purposes.

Usage
-----

- Create a Rope from String.
  ```java
  Rope a = Rope.from("hello");
  Rope b = Rope.from("world");
  assertEquals(a.toString(), "hello");
  assertEquals(b.toString(), "world");
  ```

- Concatenate two Ropes.
  ```java
  Rope a = Rope.from("hello");
  Rope b = Rope.from(" world");
  Rope c = a.concat(b);
  assertEquals(c.toString(), "hello world");
  ```

- Replace a part of a Rope.
  ```java
  Rope a = Rope.from("hello world");
  Rope b = a.replace(1, 9, "era");
  assertEquals(b.toString(), "herald");
  ```

- Get a slice of Rope.
  ```java
  Rope a = Rope.from("hello world");
  Rope b = a.slice(1, 9);
  assertEquals(b.toString(), "ello wor");
  ```
  
- Construct a Rope using Builder utility
  ```java
  Rope a = new Rope.Builder()
    .pushString("<<")
    .pushRope(Rope.from("hello"))
    .pushString(">>")
    .build();
  assertEquals(a.toString(), "<<hello>>");
  ```
  
License
-------

    MIT License
    
    Copyright (c) 2018 utsavoza
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
