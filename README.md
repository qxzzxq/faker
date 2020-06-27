# faker

![build](https://github.com/qxzzxq/faker/workflows/build/badge.svg) [![codecov](https://codecov.io/gh/qxzzxq/faker/branch/master/graph/badge.svg)](https://codecov.io/gh/qxzzxq/faker)

Faker is a Scala library that can generate fake data.

This project is inspired by the [Python package Faker](https://github.com/joke2k/faker).

## Get faker
This project does not have a stable release yet (maybe soon). To test faker, use the snapshot version:
```xml
<repositories>
  <repository>
    <id>ossrh-snapshots</id>
    <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>dev.qinx</groupId>
    <artifactId>faker_2.12</artifactId>
    <version>0.0.1-SNAPSHOT</version>
  </dependency>
</dependencies>
```

## Usage

Just add *faker*'s annotations into a standard Scala case class and *faker* will handle the rest.
```scala
import java.time.LocalTime

import dev.qinx.faker.Faker
import dev.qinx.faker.annotation.base.{IntType, Text}
import dev.qinx.faker.annotation.datetime.{Date, Time}
import dev.qinx.faker.annotation.geo.{Lat, Lon}
import dev.qinx.faker.annotation.person.Name
import dev.qinx.faker.annotation.transport.Airport
import dev.qinx.faker.enums.Locale

case class MyClass1(@Date date: String,
                    @Time time: LocalTime,
                    @Airport(country = "FR") airportCode: String,
                    @Name(locale = Locale.zh_CN) nameCN: String,
                    @Text(pattern = "??-###") id: String,
                    int1: Int,
                    @IntType(seed = "123") int2: String,
                    @IntType(min = 0, max = 10) int3: Int)

new Faker[MyClass1] get 10 foreach println

// MyClass1(2014-01-13,18:17:11,ORY,平磊,bm-250,-1501092464,1018954901,1)
// MyClass1(2012-09-30,19:23:54,TLS,申秀芳,XK-741,-252380286,1295249578,9)
// MyClass1(1994-05-22,03:27:26,ORY,敬桂英,VY-110,1097976093,-1829099982,9)
// MyClass1(2008-02-02,03:09:18,ORY,苑波,YX-343,-43394488,1111887674,5)
// MyClass1(1973-07-31,10:57:45,ORY,迟俊,ah-905,1558683535,-1621910390,7)
// MyClass1(1984-04-09,04:24:45,BOD,芦秀珍,Uo-900,366140728,-1935747844,2)
// MyClass1(2016-05-01,03:42:27,MRS,季兰英,qV-446,-995036697,696711130,7)
// MyClass1(1977-10-08,10:04:50,MRS,荆红,pk-108,-1973051050,-1366603797,2)
// MyClass1(1974-09-16,23:26:43,BOD,廖秀梅,He-851,-462826625,1149563170,5)
// MyClass1(1981-04-19,08:03:33,BSL,韩超,hk-371,1977755351,1041944832,6)```
```

Faker can also handle array (the support of other collections are still a WIP)
```scala
case class MyClass2(myClass1: Array[MyClass1])

val fake = new Faker[MyClass2].get()
fake.myClass1 foreach println
// MyClass1(1990-08-17,23:56:35,NCE,匡健,uE-846,838894708,1018954901,8)
// MyClass1(1974-10-16,01:45:04,BSL,傅雪,ch-192,1582091789,1295249578,8)
// MyClass1(1988-04-09,07:44:15,LYS,查雷,fx-476,1388926418,-1829099982,2)

// By default Faker generate an array of totalLength 3, you can adjust it by adding @ArrayType annotation
case class MyClass2Bis(@ArrayType(length = 10) myClass1: Array[MyClass1])
```

You can also generate fake data by using the `Faker` singleton:
```scala
import dev.qinx.faker.Faker

Faker.name()
Faker.localDate()
Faker.array[String](5)
Faker.array[MyClass1](5)
``` 

It's also possible to cross join one field to another to generate all the possible combination. In such a use case, use `@Series` annotation
```scala
case class CrossJoinExample(@Series(length = 2) @Date date: String,
                            @Series(length = 3, crossJoin = "date") @Text(pattern = "??-###") id: String,
                            @Series(id = "myInput", crossJoin = "id") name: String,
                            @FloatType(min = 10, max = 20) price: Float)

val faker = new Faker[CrossJoinExample]
faker.putSeries("myInput", Array("apple", "banana", "orange"))

import spark.implicits._  // to visualize as a spark dataset
faker.getDataSeries.toDS().show()
// +----------+------+------+---------+
// |      date|    id|  name|    price|
// +----------+------+------+---------+
// |1984-11-13|ym-025| apple|15.212642|
// |1984-11-13|ym-025|banana|17.103207|
// |1984-11-13|ym-025|orange|17.610806|
// |1984-11-13|eN-286| apple|11.953639|
// |1984-11-13|eN-286|banana|10.136742|
// |1984-11-13|eN-286|orange|12.635193|
// |1984-11-13|wA-500| apple| 12.24368|
// |1984-11-13|wA-500|banana|16.292076|
// |1984-11-13|wA-500|orange|15.748799|
// |1980-09-04|ym-025| apple| 16.94527|
// |1980-09-04|ym-025|banana| 18.91407|
// |1980-09-04|ym-025|orange|11.317879|
// |1980-09-04|eN-286| apple|19.948784|
// |1980-09-04|eN-286|banana|14.381845|
// |1980-09-04|eN-286|orange|16.428938|
// |1980-09-04|wA-500| apple|19.262041|
// |1980-09-04|wA-500|banana|10.533231|
// |1980-09-04|wA-500|orange|19.337175|
// +----------+------+------+---------+
```




## Available Annotations
- base
  - `@IntType`
  - `@LongType`
  - `@FloatType`
  - `@DoubleType`
  - `@Text`
  - `@UpperCase`
  - `@LowerCase`
  - `@Digit`
- datetime
  - `@Date`
  - `@Time`
  - `@DateTime`
- geo
  - `@Lat`
  - `@Lon`
- person
  - `@Name`
- transport
  - `@Airport`
  
More features will be added.

## Localization
Some annotations can handle locales, just configure the annotation with 
the corresponding `Locale` value like this:
```Scala
@Name(locale = Locale.zh_CN)
```
