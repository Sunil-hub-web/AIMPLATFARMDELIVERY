package com.aimplatfarm.aimplatfarmdelivery.Models.requestDto;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product implements Serializable
{

@SerializedName("_id")
@Expose
private String id;
@SerializedName("productId")
@Expose
private String productId;
@SerializedName("product")
@Expose
private Product__1 product;
@SerializedName("productQuantity")
@Expose
private Integer productQuantity;
private final static long serialVersionUID = -8366965514073857277L;

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getProductId() {
return productId;
}

public void setProductId(String productId) {
this.productId = productId;
}

public Product__1 getProduct() {
return product;
}

public void setProduct(Product__1 product) {
this.product = product;
}

public Integer getProductQuantity() {
return productQuantity;
}

public void setProductQuantity(Integer productQuantity) {
this.productQuantity = productQuantity;
}

}