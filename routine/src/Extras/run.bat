@echo off 
title routine
java -classpath routine.jar;JNativeHook.jar core.Executor
if %errorlevel%  neq 0 (
	echo.
	echo Info: routine.jar e JNativeHook.jar devem estar na mesma pasta deste Batch!
        echo.
        echo.
	pause
)
