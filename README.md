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
import dev.qinx.faker.Faker
import dev.qinx.faker.annotation.base.DoubleType
import dev.qinx.faker.annotation.datetime.Date
import dev.qinx.faker.annotation.geo.{Lat, Lon}
import dev.qinx.faker.annotation.person.Name
import dev.qinx.faker.enums.Locale

case class Outer(@Date date: String,
                 @Lat lat: Double,
                 @Lon lon: Double,
                 inner: Inner)

case class Inner(@Name(locale = Locale.zh_CN) nameCN: String,
                 @Name nameEN: String,
                 inner2: Inner2)

case class Inner2(@DoubleType db: Option[Double])

// fake it!
new Faker[Outer].get(10).foreach(println)

//  Outer(1975-06-20,-55.54456619823381,105.7976149011921,Inner(劳英,Raymundo Wolf,Inner2(Some(0.6802689710914326))))
//  Outer(2009-03-08,-80.47633383033715,117.74642116486496,Inner(闻畅,Kaia Howell,Inner2(Some(0.3232512987026902))))
//  Outer(1978-06-30,-26.519165012672254,-141.75735542089546,Inner(盛玉华,Olevia Davis,Inner2(Some(0.06980875790105556))))
//  Outer(1984-12-01,37.476895789699384,9.29234104256102,Inner(隆秀荣,Harden Fadel,Inner2(Some(0.02075769707758013))))
//  Outer(1984-02-21,-31.894558633392776,42.324438019180974,Inner(戴峰,Colonel Wisozk,Inner2(Some(0.5647694196248825))))
//  Outer(1972-02-08,-46.53325908319379,-70.96844656132821,Inner(伊军,Lexus Armstrong,Inner2(Some(0.17526148664772911))))
//  Outer(1980-12-10,-36.11910484788625,18.959975300805013,Inner(明浩,Olga Bayer,Inner2(Some(0.23892571525946327))))
//  Outer(2015-01-07,55.054814464224535,-168.0906165342038,Inner(简凤英,Marlin Macejkovic,Inner2(Some(0.6119473303637243))))
//  Outer(1985-12-17,-25.288438631194964,-116.0055677254131,Inner(易磊,Bailey Goodwin,Inner2(Some(0.2013667637508989))))
//  Outer(1990-02-28,78.71188964192572,69.35016034176547,Inner(赖小红,Hilmer Harber,Inner2(Some(0.10355297303863198))))
```

## Available Annotations
- base
  - `@IntType`
  - `@LongType`
  - `@FloatType`
  - `@DoubleType`
  - `@Text`
- datetime
  - `@Date`
  - `@Time`
  - `@DateTime`
- geo
  - `@Lat`
  - `@Lon`
- person
  - `@Name`

More features will be added.
