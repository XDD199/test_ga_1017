package com.example.test_ga_1017.dto;

import java.io.Serializable;

public class Recipe implements Serializable {
    private int serialNo;
    private String ID;
    private String name;
    private String type;
    private String kind;
    private String special;
    private double rawWeight;
    private String main;
    private int number;
    private double energy = 0;
    private double carbohydrate = 0;
    private double protein = 0;
    private double fat = 0;
    private double df = 0;
    private double vita = 0;
    private double vitb1 = 0;
    private double vitb2 = 0;
    private double vitc = 0;
    private double k = 0;
    private double ca = 0;
    private double na = 0;
    private double mg = 0;
    private double zn = 0;
    private double fe = 0;

    private double dark = 0;
    private double both = 0;

    public static Recipe useRecipe(String name) {
        Recipe recipe = new Recipe();
        recipe.setName(name);
        int i = 0;
        if (name.equals("燕麦杂粮饭")) {
            i = 0;
        } else if (name.equals("尖椒爆鸭")) {
            i = 1;
        } else if (name.equals("西葫芦炒蛋")) {
            i = 2;
        } else if (name.equals("蒜泥秋葵")) {
            i = 3;
        }
        double[] rawWeight = {130, 108.1, 113.8, 112.6};
        double[] energy = {447.4, 180.827, 87.8897, 43.8215};
        double[] protein = {10.93, 9.8596, 3.05699, 2.17029};
        double[] fat = {0.96, 15.359, 7.03833, 1.51393};
        double[] carbohydrate = {100.42, 0.94392, 3.26378, 7.64839};
        double[] df = {2.4, 0.2944, 0.379296, 1.89149};
        double[] vita = {0, 12.961, 14.5963, 6.15366};
        double[] vitb1 = {0.288, 0.052988, 0.0240179, 0.0745946};
        double[] vitb2 = {0.061, 0.144142, 0.0559038, 0.056038};
        double[] vitc = {0, 13.248, 3.68796, 7.66385};
        double[] k = {218.8, 143.15, 86.4825, 46.0565};
        double[] na = {2.43, 376.255, 978.837, 143.423};
        double[] ca = {25.4, 7.7256, 20.4708, 104.035};
        double[] mg = {65.8, 10.5954, 7.59064, 40.6981};
        double[] zn = {2.065, 0.881578, 0.278987, 0.290028};
        double[] fe = {1.97, 1.64742, 0.70621, 0.366882};

        recipe.setRawWeight(rawWeight[i]);
        recipe.setEnergy(energy[i]);
        recipe.setProtein(protein[i]);
        recipe.setFat(fat[i]);
        recipe.setCarbohydrate(carbohydrate[i]);
        recipe.setDf(df[i]);
        recipe.setVita(vita[i]);
        recipe.setVitb1(vitb1[i]);
        recipe.setVitb2(vitb2[i]);
        recipe.setVitc(vitc[i]);
        recipe.setK(k[i]);
        recipe.setNa(na[i]);
        recipe.setCa(ca[i]);
        recipe.setMg(mg[i]);
        recipe.setZn(zn[i]);
        recipe.setFe(fe[i]);

        return recipe;
    }


    public double getDark() {
        return dark;
    }

    public void setDark(double dark) {
        this.dark = dark;
    }

    public double getBoth() {
        return both;
    }

    public void setBoth(double both) {
        this.both = both;
    }

    public Recipe newMain() {
        return null;
    }

    public Recipe newMeat() {
        return null;
    }

    public Recipe newHalfMeat() {
        return null;
    }

    public Recipe newVegetable() {
        return null;
    }

    public int getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(int serialNo) {
        this.serialNo = serialNo;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public double getRawWeight() {
        return rawWeight;
    }

    public void setRawWeight(double rawWeight) {
        this.rawWeight = rawWeight;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public double getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(double carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getDf() {
        return df;
    }

    public void setDf(double df) {
        this.df = df;
    }

    public double getVita() {
        return vita;
    }

    public void setVita(double vita) {
        this.vita = vita;
    }

    public double getVitb1() {
        return vitb1;
    }

    public void setVitb1(double vitb1) {
        this.vitb1 = vitb1;
    }

    public double getVitb2() {
        return vitb2;
    }

    public void setVitb2(double vitb2) {
        this.vitb2 = vitb2;
    }

    public double getVitc() {
        return vitc;
    }

    public void setVitc(double vitc) {
        this.vitc = vitc;
    }

    public double getK() {
        return k;
    }

    public void setK(double k) {
        this.k = k;
    }

    public double getCa() {
        return ca;
    }

    public void setCa(double ca) {
        this.ca = ca;
    }

    public double getNa() {
        return na;
    }

    public void setNa(double na) {
        this.na = na;
    }

    public double getMg() {
        return mg;
    }

    public void setMg(double mg) {
        this.mg = mg;
    }

    public double getZn() {
        return zn;
    }

    public void setZn(double zn) {
        this.zn = zn;
    }

    public double getFe() {
        return fe;
    }

    public void setFe(double fe) {
        this.fe = fe;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "energy=" + energy +
                ", carbohydrate=" + carbohydrate +
                ", protein=" + protein +
                ", fat=" + fat +
                ", df=" + df +
                ", vita=" + vita +
                ", vitb1=" + vitb1 +
                ", vitb2=" + vitb2 +
                ", vitc=" + vitc +
                ", k=" + k +
                ", ca=" + ca +
                ", na=" + na +
                ", mg=" + mg +
                ", zn=" + zn +
                ", fe=" + fe +
                ", dark=" + dark +
                ", both=" + both +
                '}';
    }
}
