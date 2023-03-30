package net.particify.arsnova.comments.model.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.UUID;

import net.particify.arsnova.comments.model.Comment;
import net.particify.arsnova.comments.model.WebSocketPayload;

public class CreateCommentPayload implements WebSocketPayload {
  private UUID roomId;
  private UUID creatorId;
  private String body;
  private String tag;

  public CreateCommentPayload() {
  }

  public CreateCommentPayload(Comment c) {
    this.creatorId = c.getCreatorId();
    this.roomId = c.getRoomId();
    this.body = c.getBody();
    this.tag = c.getTag();
  }

  @JsonProperty("roomId")
  public UUID getRoomId() {
    return roomId;
  }

  @JsonProperty("roomId")
  public void setRoomId(UUID roomId) {
    this.roomId = roomId;
  }

  @JsonProperty("creatorId")
  public UUID getCreatorId() {
    return creatorId;
  }

  @JsonProperty("creatorId")
  public void setCreatorId(UUID creatorId) {
    this.creatorId = creatorId;
  }

  @JsonProperty("body")
  public String getBody() {
    return body;
  }

  @JsonProperty("body")
  public void setBody(String body) {
    this.body = body;
  }

  @JsonProperty("tag")
  public String getTag() {
    return tag;
  }

  @JsonProperty("tag")
  public void setTag(String tag) {
    this.tag = tag;
  }

  @Override
  public String toString() {
    return "CreateCommentPayload{" +
        "creatorId='" + creatorId + '\'' +
        ", roomId='" + roomId + '\'' +
        ", body='" + body + '\'' +
        ", tag='" + tag + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CreateCommentPayload that = (CreateCommentPayload) o;
    return Objects.equals(roomId, that.roomId) &&
        Objects.equals(creatorId, that.creatorId) &&
        Objects.equals(body, that.body) &&
        Objects.equals(tag, that.tag);
  }

  @Override
  public int hashCode() {
    return Objects.hash(roomId, creatorId, body, tag);
  }
}
