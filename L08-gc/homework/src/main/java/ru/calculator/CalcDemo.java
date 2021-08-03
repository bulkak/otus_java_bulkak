package ru.calculator;


/*
-Xms256m
-Xmx256m
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=./logs/heapdump.hprof
-XX:+UseG1GC
-Xlog:gc=debug:file=./logs/gc-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=10m
*/

/*
1)  Before optimization Integer -> int
Heep Size | Time, sec |
-----------------------
   256M   |    31     |
  2048M   |    16     |
  1024M   |    18     |
   512M   |    21     |
  1536M   |    18     |
   768M   |    18     |- optimal
   640M   |    21     |
   704M   |    19     |
_______________________

2) After optimization Integer -> int (+ long -> int)
Heep Size | Time, sec |
-----------------------
   768M   |    3      |
  2048M   |    3      |
   256M   |    4      |
   512M   |    3      | - optimal
-----------------------

2) After optimization MaxGCPauseMillis

-XX:MaxGCPauseMillis=100000
Heep Size | Time, sec |
-----------------------
   512M   |    3      |
-----------------------

-XX:MaxGCPauseMillis=10
Heep Size | Time, sec |
-----------------------
   512M   |    4      |

-XX:MaxGCPauseMillis=1000
Heep Size | Time, sec |
-----------------------
   512M   |    3      |
-----------------------

3) After optimization initialCapacity for ArrayList
Heep Size | Time, sec |
-----------------------
   512M   |    3      | - don't need it here
-----------------------

4) since the values from the object 'Data' are not used in calculations,
 we do not need so many instances of it
 we will use one object to transfer data to the 'Summator'
 now the GC is resting!
Heep Size | Time, sec |
-----------------------
   512M   |    2      | - not bad
   128M   |    2      | - shock
-----------------------
 */


import java.time.LocalDateTime;

public class CalcDemo {
    public static void main(String[] args) {
        int counter = 100_000_000;
        var summator = new Summator();
        long startTime = System.currentTimeMillis();

        Data data = new Data(0);
        for (var idx = 0; idx < counter; idx++) {
            data.setValue(idx);
            summator.calc(data);
            if (idx % 10_000_000 == 0) {
                System.out.println(LocalDateTime.now() + " current idx:" + idx);
            }
        }

        long delta = System.currentTimeMillis() - startTime;
        System.out.println(summator.getPrevValue());
        System.out.println(summator.getPrevPrevValue());
        System.out.println(summator.getSumLastThreeValues());
        System.out.println(summator.getSomeValue());
        System.out.println(summator.getSum());
        System.out.println("spend msec:" + delta + ", sec:" + (delta / 1000));
    }
}
