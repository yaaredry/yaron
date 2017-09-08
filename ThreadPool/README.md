# yaron

This program is my easy-and-simple implementation for thread-pool design pattern.

MyThreadPool.java file contains the main function,

The program creates matrices with user input, then apply multiplication for all matrices using all threads in the pool.

The flow:

1. the user may choose number of active threads (2-20)
2. the user may choose number of matrices
3. the user may choose the dimensions of the matrices

The product of the multiplication is printed to a txt file.

The most important classes are:
myThreadPool.java - maintain the threads array and my tasks queue
myThreadPoolThread.java - simply poll task from the queue and run it

documentation with javadocs is available :)

cheers
