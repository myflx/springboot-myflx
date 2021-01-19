package excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExcelTest {
    public static void main(String[] args) {
        write();
    }

    public static void write() {
        ExcelWriter excelWriter = null;

        try {
            excelWriter = EasyExcel.write(write_path).build();

            WriteSheet writeSheet = EasyExcel.writerSheet().relativeHeadRowIndex(5).build();

            WriteTable writeTable = EasyExcel.writerTable(0).head(Person.class).needHead(true).relativeHeadRowIndex(0).build();
            WriteTable writeTable2 = EasyExcel.writerTable(1).head(Fruit.class).needHead(true).build();

            excelWriter.write(data(), writeSheet, writeTable);
            excelWriter.write(data2(), writeSheet, writeTable2);
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    private static final String write_path = "e:" + File.separator + "write2.xlsx";

    private static List<Person> data() {
        List<Person> list = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Person person = new Person();
            person.setId(i);
            person.setName("瓜田李下" + i);
            person.setAge(20 + i);

            list.add(person);
        }

        return list;
    }

    private static List<Fruit> data2() {
        List<Fruit> list = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Fruit fruit = new Fruit();
            fruit.setId(i);
            fruit.setName("apple" + i);
            fruit.setPrice((double) (4 + i));

            list.add(fruit);
        }

        return list;
    }
}

@Data
class Person {

    private Integer id;
    private String name;
    private Integer age;
}

@Data
class Fruit {

    private Integer id;
    private String name;
    private Double price;
}
