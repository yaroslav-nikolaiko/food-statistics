mvn clean package
java -jar target/food-statistics-1.0-SNAPSHOT.jar --input="/home/ynikolaiko/Downloads/amazon-fine-foods/Reviews.csv" -Xmx512m
java -jar target/food-statistics-1.0-SNAPSHOT.jar --input="/home/ynikolaiko/Downloads/amazon-fine-foods/Reviews.csv" --translateOutput="/tmp/translated.csv" -Xmx768m
