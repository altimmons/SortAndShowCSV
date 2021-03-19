
@echo off

echo ...

if exist .\out\results.csv (
    rem Results file found.

    if exist .\out/csvfileview_64.exe (
        pushd out
        csvfileview_64.exe results.csv

         ) else (
             rem Viewer not found.
             GOTO:eof
         )
) else (
    rem Results file not found.
    GOTO:eof
)



rem Completed, returning.