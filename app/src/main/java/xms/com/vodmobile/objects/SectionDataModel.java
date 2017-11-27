package xms.com.vodmobile.objects;

import java.util.List;

/**
 * Created by Elio on 11/23/2017.
 */

public class SectionDataModel {
    private String Type;
    private String headerTitle;
    private List<Object> objectList;


    public SectionDataModel(String headerTitle,String type, List<Object> objectList) {
        this.headerTitle = headerTitle;
        this.objectList= objectList;
        this.Type = type;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public String getType() {
        return Type;
    }

    public List<Object> getAllItemsInSection() {
        return this.objectList;
    }
    public void setType(String type) {
        Type = type;
    }
}
