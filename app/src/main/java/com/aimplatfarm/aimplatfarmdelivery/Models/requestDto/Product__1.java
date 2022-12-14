package com.aimplatfarm.aimplatfarmdelivery.Models.requestDto;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product__1 implements Serializable
{

@SerializedName("discount")
@Expose
private double discount;
@SerializedName("images")
@Expose
private List<String> images = null;
@SerializedName("weight")
@Expose
private List<String> weight = null;
@SerializedName("subcategoryId")
@Expose
private String subcategoryId;
@SerializedName("totalRating")
@Expose
private double totalRating;
@SerializedName("avgRating")
@Expose
private double avgRating;
@SerializedName("sold")
@Expose
private double sold;
@SerializedName("offers")
@Expose
private List<Object> offers = null;
@SerializedName("benifits")
@Expose
private List<Object> benifits = null;
@SerializedName("colors")
@Expose
private List<Object> colors = null;
@SerializedName("_id")
@Expose
private String id;
@SerializedName("addedBy")
@Expose
private String addedBy;
@SerializedName("title")
@Expose
private String title;
@SerializedName("price")
@Expose
private double price;
@SerializedName("type")
@Expose
private String type;
@SerializedName("description")
@Expose
private String description;
@SerializedName("soldBy")
@Expose
private String soldBy;
@SerializedName("inStock")
@Expose
private double inStock;
@SerializedName("experience")
@Expose
private String experience;
@SerializedName("categoryId")
@Expose
private String categoryId;
@SerializedName("createdAt")
@Expose
private String createdAt;
@SerializedName("updatedAt")
@Expose
private String updatedAt;
@SerializedName("__v")
@Expose
private double v;
private final static long serialVersionUID = 2600253960656132866L;

public double getDiscount() {
return discount;
}

public void setDiscount(double discount) {
this.discount = discount;
}

public List<String> getImages() {
return images;
}

public void setImages(List<String> images) {
this.images = images;
}

public List<String> getWeight() {
return weight;
}

public void setWeight(List<String> weight) {
this.weight = weight;
}

public String getSubcategoryId() {
return subcategoryId;
}

public void setSubcategoryId(String subcategoryId) {
this.subcategoryId = subcategoryId;
}

public double getTotalRating() {
return totalRating;
}

public void setTotalRating(double totalRating) {
this.totalRating = totalRating;
}

public double getAvgRating() {
return avgRating;
}

public void setAvgRating(double avgRating) {
this.avgRating = avgRating;
}

public double getSold() {
return sold;
}

public void setSold(double sold) {
this.sold = sold;
}

public List<Object> getOffers() {
return offers;
}

public void setOffers(List<Object> offers) {
this.offers = offers;
}

public List<Object> getBenifits() {
return benifits;
}

public void setBenifits(List<Object> benifits) {
this.benifits = benifits;
}

public List<Object> getColors() {
return colors;
}

public void setColors(List<Object> colors) {
this.colors = colors;
}

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getAddedBy() {
return addedBy;
}

public void setAddedBy(String addedBy) {
this.addedBy = addedBy;
}

public String getTitle() {
return title;
}

public void setTitle(String title) {
this.title = title;
}

public double getPrice() {
return price;
}

public void setPrice(double price) {
this.price = price;
}

public String getType() {
return type;
}

public void setType(String type) {
this.type = type;
}

public String getDescription() {
return description;
}

public void setDescription(String description) {
this.description = description;
}

public String getSoldBy() {
return soldBy;
}

public void setSoldBy(String soldBy) {
this.soldBy = soldBy;
}

public double getInStock() {
return inStock;
}

public void setInStock(double inStock) {
this.inStock = inStock;
}

public String getExperience() {
return experience;
}

public void setExperience(String experience) {
this.experience = experience;
}

public String getCategoryId() {
return categoryId;
}

public void setCategoryId(String categoryId) {
this.categoryId = categoryId;
}

public String getCreatedAt() {
return createdAt;
}

public void setCreatedAt(String createdAt) {
this.createdAt = createdAt;
}

public String getUpdatedAt() {
return updatedAt;
}

public void setUpdatedAt(String updatedAt) {
this.updatedAt = updatedAt;
}

public double getV() {
return v;
}

public void setV(double v) {
this.v = v;
}

}