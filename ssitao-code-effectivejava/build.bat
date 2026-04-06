@echo off
REM Effective Java Examples Build Script (Windows)

echo ===================================
echo Effective Java Examples
echo ===================================
echo.

cd /d "%~dp0"

REM Create output directory
if not exist out rmdir /s /q out
mkdir out

REM Compile all Java files from src/main/java
echo Compiling all examples...
for /r src\main\java %%i in (*.java) do (
    javac -encoding UTF-8 -d out "%%i"
)
echo Compilation complete!
echo.

REM Run Chapter 1 examples
echo ========== Chapter 1 ==========
echo.
java -cp out com.ssitao.code.effectivejava.ch01.item01.BooleanCache
echo.
java -cp out com.ssitao.code.effectivejava.ch01.item02.NutritionFacts
echo.
java -cp out com.ssitao.code.effectivejava.ch01.item03.Elvis
echo.
java -cp out com.ssitao.code.effectivejava.ch01.item05.SpellChecker
echo.
java -cp out com.ssitao.code.effectivejava.ch01.item06.AvoidUnnecessaryObjects
echo.

REM Run Chapter 2 examples
echo ========== Chapter 2 ==========
echo.
java -cp out com.ssitao.code.effectivejava.ch02.item08.PhoneNumber
echo.
java -cp out com.ssitao.code.effectivejava.ch02.item09.PhoneNumberWithHash
echo.
java -cp out com.ssitao.code.effectivejava.ch02.item10.PhoneNumberWithToString
echo.
java -cp out com.ssitao.code.effectivejava.ch02.item12.WordList
echo.

REM Run Chapter 3 examples
echo ========== Chapter 3 ==========
echo.
java -cp out com.ssitao.code.effectivejava.ch03.item17.Complex
echo.
java -cp out com.ssitao.code.effectivejava.ch03.item18.composition.InstrumentedSetDemo
echo.
java -cp out com.ssitao.code.effectivejava.ch03.item22.FigureDemo
echo.

REM Run Chapter 4 examples
echo ========== Chapter 4 ==========
echo.
java -cp out com.ssitao.code.effectivejava.ch04.item28.ArrayVsList
echo.
java -cp out com.ssitao.code.effectivejava.ch04.item29.Chooser
echo.
java -cp out com.ssitao.code.effectivejava.ch04.item31.Stack
echo.
java -cp out com.ssitao.code.effectivejava.ch04.item33.Favorites
echo.

echo ===================================
echo All examples completed!
echo ===================================
pause
