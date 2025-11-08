
#### check dependencies last versions:
````
mvn versions:display-dependency-updates
````

#### check dependencies tree:
````
mvn dependency:tree
````

#### base installation like in git
````
mvn install -Dmaven.test.skip=true -DcreateChecksum=false
````

#### base deploy like in git
````
mvn deploy -s settings_local.xml -Dmaven.test.skip=true
````



# FOR MAVEN CENTRAL

#### installation with checksum and asc
````
mvn install -s settings_local.xml -Dmaven.test.skip=true -P release
````
