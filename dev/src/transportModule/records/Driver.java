package transportModule.records;

import com.google.gson.reflect.TypeToken;
import utils.JSON;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.Objects;

public record Driver (int id, String name, LicenseType licenseType){

    public enum LicenseType{
        A1,A2,A3,B1,B2,B3,C1,C2,C3
    }

    public static Driver getLookupObject(int id){
        return new Driver(id, null, null);
    }

    public String toJson(){
        return JSON.serialize(this);
    }

    public static Driver fromJson(String json){
        return JSON.deserialize(json, Driver.class);
    }

    public static LinkedList<Driver> listFromJson(String json){
        Type type = new TypeToken<LinkedList<Driver>>(){}.getType();
        return JSON.deserialize(json, type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Driver driver = (Driver) o;
        return id == driver.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
