package com.orionweller.collegehousing;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.InputStream;
import java.util.List;

@Entity
public class Apartments {

    public String name;
    public String price;
    public String distance;
    public String reviews;
    public String type;

    public Apartments(String name, String price, String distance, String reviews, String type) {
        this.name = name;
        this.price = price;
        this.distance = distance;
        this.reviews = reviews;
        this.type = type;
    }

    @PrimaryKey
    private int uid;

    @ColumnInfo(name = "first_name")
    private String firstName;

    @ColumnInfo(name = "last_name")
    private String lastName;


    public static Apartments[] populateData() {
        InputStream inputStream;
        return new Apartments[]{
                inputStream = getResources().openRawResource(R.raw.apartments);
                CSVFile csvFile = new CSVFile(inputStream);
                List<String[]> apartmentos = csvFile.read();

        for (int i = 0; i < apartmentos.size(); i++) {
            new Apartments(apartmentos.get(i)[0], apartmentos.get(i)[1], apartmentos.get(i)[2],
                    apartmentos.get(i)[3], apartmentos.get(i)[4]);
        }
        ;
    }
    }
}


    public static DataEntity[] populateData() {
        return new DataEntity[] {
                new DataEntity("image1.jpg", "title1", "text1"),
                new DataEntity("image2.jpg", "title2", "text2"),
                new DataEntity("image3.jpg", "title3", "text3"),
                new DataEntity("image4.jpg", "title4", "text4"),
                new DataEntity("image5.jpg", "title5", "text5")
        };
    }
}

}
