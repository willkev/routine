@echo off 
java -classpath routine.jar;JNativeHook.jar core.Tester
if %errorlevel%  neq 0 (
	echo.
	echo + routine.jar e JNativeHook.jar devem estar na mesma pasta que %0
	pause
)
