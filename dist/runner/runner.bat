@echo OFF

set basedir=%CD%
set basedir=%basedir:\=/%

cls

:input-type
set type="v1"
set /P type="Enter the type of the mailer ( v1 | v2 ):" %=%
if "%type%"=="" goto input-type

:input-fields
set fields=
set /P fields=Enter the fileds in the source file: %=%

:input-subject
set subject="v1"
set /P subject=Enter the subject of the mail: %=%
if "%subject%"=="" goto input-subject


java -jar in.co.zybotech.mailer.bulk.jar "%type%" "%basedir%/properties.properties" "%basedir%/people.txt" "%fields%" "%subject%" "%basedir%/mail.html" "%basedir%/mail.txt" "%basedir%/attachments"