package com.mmall.pojo;

/**
 * Created by Eliza Liu on 2018/5/10
 */
public class Comment {

    private Integer  commentId;
    private Long orderId;
    private Integer userId;
    private Integer productId;
    private String content;
    private String images;

    public Comment(Integer commentId, Integer userId, Integer productId,  Long orderId, String content, String images) {
        this.commentId = commentId;
        this.orderId = orderId;
        this.userId = userId;
        this.productId = productId;
        this.content = content;
        this.images = images;
    }



    public Comment() {
        super();
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }


}
