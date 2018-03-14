package sample.Entity;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;

public class Item extends RecursiveTreeObject<Item> {

    public static int PROPERTY_TYPE_STRING = 0;
    public static int PROPERTY_TYPE_INT = 1;
    public static int PROPERTY_TYPE_DOUBLE = 2;

    private ArrayList<Property> attributes ;

    public Item(){
        attributes = new ArrayList<Property>();
    }

    public Item(ArrayList<Property> attributes){
        this.attributes = attributes;
    }

    public void addAttr(Object p, int type){
        switch (type){
            case 0:{
                if (p instanceof String )
                    attributes.add(new SimpleStringProperty((String) p));
                break;
            }
            case 1:{
                if (p instanceof Integer )
                    attributes.add(new SimpleIntegerProperty((int) p));
                break;
            }
            case 2:{
                if (p instanceof Double )
                    attributes.add(new SimpleDoubleProperty((Double) p));
                break;
            }
        }

    }

    public Property getProperty(int index){
        return attributes.get(index);
    }

    public int getAttrNum(){
        return attributes.size();
    }

    public Property removeAttr(int index){
        return attributes.remove(index);
    }
}
