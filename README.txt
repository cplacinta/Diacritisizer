Diacritisizer
======
To build & start the test parser (the component that parses the text and persists them into the DB), do the following:
1. Navigate to the project's root folder.
2. mvn clean package
3. db/init.sh && db/create-tables.sh
4. java -cp text-parser/target/text-parser-1.0-SNAPSHOT-jar-with-dependencies.jar com.placinta.diacritisizer.TextParser text-parser/src/main/resources/

*Notice: There's a text sample at text-parser/src/main/resources/, feel free to provide a different path that contains correctly spelled text corporas.
