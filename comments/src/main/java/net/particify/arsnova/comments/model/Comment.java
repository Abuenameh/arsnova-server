package net.particify.arsnova.comments.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Comment {
  public static final int MAX_BODY_LENGTH = 500;
  public static final int MAX_ANSWER_LENGTH = 500;

  @Id
  private UUID id;
  private UUID roomId;
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private UUID creatorId;
  private UUID archiveId;
  @Column(columnDefinition = "TEXT")
  private String body;
  private Date timestamp;
  private boolean read;
  private boolean favorite;
  private int correct;
  private boolean ack;
  private String tag;
  @Column(columnDefinition = "TEXT")
  private String answer;

  @Transient
  private int score;

  public Comment() {

  }

  /**
   * Copying constructor which adopts most of the original comments'
   * properties which are not used to store relations to other data.
   */
  public Comment(final Comment comment) {
    this.body = comment.body;
    this.timestamp = comment.timestamp;
    this.read = comment.read;
    this.favorite = comment.favorite;
    this.correct = comment.correct;
    this.ack = comment.ack;
    this.tag = comment.tag;
    this.answer = comment.answer;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getRoomId() {
    return roomId;
  }

  public void setRoomId(UUID roomId) {
    this.roomId = roomId;
  }

  public UUID getCreatorId() {
    return creatorId;
  }

  public void setCreatorId(UUID creatorId) {
    this.creatorId = creatorId;
  }

  public UUID getArchiveId() {
    return archiveId;
  }

  public void setArchiveId(final UUID archiveId) {
    this.archiveId = archiveId;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body.length() > MAX_BODY_LENGTH ? body.substring(0, MAX_BODY_LENGTH) : body;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  public boolean isRead() {
    return read;
  }

  public void setRead(boolean read) {
    this.read = read;
  }

  public boolean isFavorite() {
    return favorite;
  }

  public void setFavorite(boolean favorite) {
    this.favorite = favorite;
  }

  public int getCorrect() {
    return correct;
  }

  public void setCorrect(int correct) {
    this.correct = correct;
  }

  public boolean isAck() {
    return ack;
  }

  public void setAck(boolean ack) {
    this.ack = ack;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer.length() > MAX_ANSWER_LENGTH ? answer.substring(0, MAX_ANSWER_LENGTH) : answer;
  }

  @Override
  public String toString() {
    return "Comment{" +
        "id='" + id + '\'' +
        ", roomId='" + roomId + '\'' +
        ", creatorId='" + creatorId + '\'' +
        ", archiveId='" + archiveId + '\'' +
        ", body='" + body + '\'' +
        ", timestamp=" + timestamp +
        ", read=" + read +
        ", favorite=" + favorite +
        ", correct=" + correct +
        ", ack=" + ack +
        ", score=" + score +
        ", tag=" + tag +
        ", answer=" + answer +
        '}';
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final Comment comment = (Comment) o;
    return read == comment.read && favorite == comment.favorite && correct == comment.correct && ack == comment.ack && score == comment.score && Objects.equals(
        id,
        comment.id) && Objects.equals(roomId, comment.roomId) && Objects.equals(
        creatorId,
        comment.creatorId) && Objects.equals(archiveId, comment.archiveId) && Objects.equals(
        body,
        comment.body) && Objects.equals(timestamp, comment.timestamp) && Objects.equals(
        tag,
        comment.tag) && Objects.equals(answer, comment.answer);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        id,
        roomId,
        creatorId,
        archiveId,
        body,
        timestamp,
        read,
        favorite,
        correct,
        ack,
        score,
        tag,
        answer);
  }
}
