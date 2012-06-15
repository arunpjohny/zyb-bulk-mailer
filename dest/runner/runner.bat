@echo OFF

set basedir=%CD%
set basedir=%basedir:\=/%

cls

java -jar in.co.zybotech.mailer.bulk.jar "%basedir%/properties.properties" "%basedir%/people.txt" "email,name" "This is a test mail" "%basedir%/mail.html" "%basedir%/mail.txt"