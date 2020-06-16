# faker

Faker is a Scala library that can generate fake data.

This project is inspired by the [Python package Faker](https://github.com/joke2k/faker).

## Example

```scala
import dev.qinx.faker.Faker
import dev.qinx.faker.annotation.datetime.Date
import dev.qinx.faker.annotation.person.Name

case class MyClass(@Name name: String,
                   @Date date: String,
                   text: String,
                   number: Int)

val faker = new Faker[MyClass]
faker.get(10).foreach(println)

//  MyClass(Shelva Mraz,2016-03-15,aMoDSY8GTW,-265649627)
//  MyClass(Claire Runolfsdottir,1981-03-19,alpzLQpMyc,-939479793)
//  MyClass(Harris Shanahan,1987-01-20,4fcNSuRDve,1167712054)
//  MyClass(Bobby West,2001-02-08,XkYRyCy0oU,-1500391502)
//  MyClass(Esley Block,2004-12-13,AQtSvfSp3N,-989864590)
//  MyClass(Theo Jakubowski,1988-03-09,Ng1VBWUn4c,424981943)
//  MyClass(Oda Jaskolski,1976-10-07,rvhUluV5v8,-1902120628)
//  MyClass(Watt Rogahn,2015-09-30,fGN17G61AF,-166817659)
//  MyClass(Salome Langworth,2010-09-18,Mjcyu0J0Nk,24282519)
//  MyClass(Malik Koch,2012-01-03,MBNibRTPZZ,-2044558830)  

```

More features will be added.
