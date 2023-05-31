# 3D Java Game Project Progress Report

## Day 1
### Setting up the project
- Imported LWJGL3 library in order to kickstart the project off.
- The project is created and ran within Eclipse IDE environment, and for consistency sake, will be executed in 1.8 Java Environment.
- Using **Threads** to run and execute the main functions for the game.
    - **Threads** allows a program to operate more efficiently by doing multiple things at the same time, basically running different tasks on different threads allows these tasks to be ran concurrently along with the main program without interrupting the main thread and slowing down the program.

### Setting up Window.java and Input.java classes
- Setup the `Window.java` class so that the main program can set up a window that can pop up on the screen, although there is an error where the window is non-responsive and can't be closed unless the program is forcefully terminated.
    - The solution to this problem is to just ensure that within the `run()` method that the loop is checking whether the window should close or not with the built-in `.shouldClose()` method.
- The window created by `Window.java` would also not be rendering anything.
    - This is caused by not having any specifications within the class to actually render graphics, a fix is to just call `GLFW.glfwSwapBuffers(window)` so that the program actually knows to render graphics within the window.
- Setup `Input.java`, this class processes all the possible user inputs, mouseButtons, keyboardKeys and mousePositions in order to allow for the main program to accept user inputs and processes them through methods setup within the class itself.