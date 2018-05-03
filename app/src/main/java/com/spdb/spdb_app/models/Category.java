package com.spdb.spdb_app.models;

import io.realm.RealmObject;

public class Category extends RealmObject{
    private String polishItemName;
    private String realCategoryName;

    public Category(){

    }
    public Category(String plName, String engName){
        this.polishItemName = plName;
        this.realCategoryName = engName;
    }

    public String getPolishItemName() {
        return polishItemName;
    }

    public void setPolishItemName(String polishItemName) {
        this.polishItemName = polishItemName;
    }

    public String getRealCategoryName() {
        return realCategoryName;
    }

    public void setRealCategoryName(String realCategoryName) {
        this.realCategoryName = realCategoryName;
    }
}
