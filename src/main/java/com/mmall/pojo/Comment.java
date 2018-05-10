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

    public Comment(Integer commentId, Long orderId, Integer userId, Integer productId, String content, String images) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        if (!commentId.equals(comment.commentId)) return false;
        if (orderId != null ? !orderId.equals(comment.orderId) : comment.orderId != null) return false;
        if (userId != null ? !userId.equals(comment.userId) : comment.userId != null) return false;
        if (productId != null ? !productId.equals(comment.productId) : comment.productId != null) return false;
        if (content != null ? !content.equals(comment.content) : comment.content != null) return false;
        return images != null ? images.equals(comment.images) : comment.images == null;
    }

    @Override
    public int hashCode() {
        int result = commentId.hashCode();
        result = 31 * result + (orderId != null ? orderId.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (productId != null ? productId.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (images != null ? images.hashCode() : 0);
        return result;
    }
}
