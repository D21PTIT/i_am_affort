package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Doc {
    private int certificates_of_land_use_rights;
    private int house_ownership_certificate;
    private int sale_contract;
    private int construction_permit;
    private int valid_document;
    private int retal_license;

    // Thêm default constructor cho Jackson
    public Doc() {
    }

    public Doc(int certificates_of_land_use_rights, int house_ownership_certificate, int sale_contract,
            int construction_permit, int valid_document, int retal_license) {
        this.certificates_of_land_use_rights = certificates_of_land_use_rights;
        this.house_ownership_certificate = house_ownership_certificate;
        this.sale_contract = sale_contract;
        this.construction_permit = construction_permit;
        this.valid_document = valid_document;
        this.retal_license = retal_license;
    }

    public int getCertificates_of_land_use_rights() {
        return certificates_of_land_use_rights;
    }

    public void setCertificates_of_land_use_rights(int certificates_of_land_use_rights) {
        this.certificates_of_land_use_rights = certificates_of_land_use_rights;
    }

    public int getHouse_ownership_certificate() {
        return house_ownership_certificate;
    }

    public void setHouse_ownership_certificate(int house_ownership_certificate) {
        this.house_ownership_certificate = house_ownership_certificate;
    }

    public int getSale_contract() {
        return sale_contract;
    }

    public void setSale_contract(int sale_contract) {
        this.sale_contract = sale_contract;
    }

    public int getConstruction_permit() {
        return construction_permit;
    }

    public void setConstruction_permit(int construction_permit) {
        this.construction_permit = construction_permit;
    }

    public int getValid_document() {
        return valid_document;
    }

    public void setValid_document(int valid_document) {
        this.valid_document = valid_document;
    }

    public int getRetal_license() {
        return retal_license;
    }

    public void setRetal_license(int retal_license) {
        this.retal_license = retal_license;
    }
}