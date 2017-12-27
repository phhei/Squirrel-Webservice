##########################################################################################################
##################################### SPRING BUILD #######################################################
##########################################################################################################

Needed repositories: https://github.com/phhei/SquirrelWebObject.git

1. Copy the current SquirrelWebObjectJar in the root folder
2. Install it into your maven-repo: mvn install:install-file -DgroupId=SquirrelWebObject -DartifactId=SquirrelWebObjectJar -Dpackaging=jar -Dversion=1.1 -Dfile=SquirrelWeb
   ObjectJar-<currentVersion>.jar -DgeneratePom=true
3. Compile it: mvn clean package