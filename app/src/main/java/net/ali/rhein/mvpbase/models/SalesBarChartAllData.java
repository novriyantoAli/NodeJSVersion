package net.ali.rhein.mvpbase.models;

/**
 * Created by rhein on 4/30/18.
 */

public class SalesBarChartAllData {

    private String dateName;
    private int productSelling;

    public void setDateName(String dateName){
        this.dateName = dateName;
    }

    public String getDateName(){
        return this.dateName;
    }

    public void setProductSelling(int productSelling){
        this.productSelling = productSelling;
    }

    public int getProductSelling(){
        return this.productSelling;
    }
}
