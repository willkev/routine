@echo off 
java -classpath routine.jar;JNativeHook.jar core.Executor
if %errorlevel%  neq 0 (
	echo.
	echo Info: routine.jar e JNativeHook.jar devem estar na mesma pasta!
        echo.
        echo.
	pause
)
